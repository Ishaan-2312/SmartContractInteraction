package com.IshaanBansal.Web3jDemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



//ipackage com.example.ethereum;

import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.abi.FunctionEncoder;
//import org.web3j.abi.FunctionDecoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class EthereumService {

    private final Web3j web3j;
    private final Credentials credentials;
    @Value("${ethereum.contractAddress}")
    private  String contractAddress ;

    @Value("${ethereum.privateKey}")
    private  String privateKey;
    public EthereumService() {
         // Replace with your private key
        String infuraUrl = "https://sepolia.infura.io/v3/3a699b648b924eaf8f0263bbb862578e"; // Use Rinkeby or another Ethereum network

        this.web3j = Web3j.build(new HttpService(infuraUrl));
        this.credentials = Credentials.create(privateKey);
    }

    // Encode the setValue method call and send the transaction
    public String setValue(BigInteger value) throws Exception {
        // Function name and parameters
        Function function = new Function(
                "setValue",
                Collections.singletonList(new Uint256(value)),
                Collections.emptyList()
        );

        // Encode function
        String encodedFunction = FunctionEncoder.encode(function);

        // Get the current nonce for the account
        BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
        ).send().getTransactionCount();

        // Estimate gas price and gas limit
        BigInteger gasPrice = DefaultGasProvider.GAS_PRICE;
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

        // Create the raw transaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                encodedFunction
        );

        // Sign the transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        // Send the signed transaction
        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).send();

        // Check for errors in the response
        if (transactionResponse.getError() != null) {
            throw new RuntimeException("Transaction Error: " + transactionResponse.getError().getMessage());
        }

        // Return the transaction hash
        return transactionResponse.getTransactionHash();
    }
    // Call the getValue method and decode the response
    public BigInteger getValue() throws Exception {
        Function function = new Function(
                "getValue",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {})
        );

        String encodedFunction = FunctionEncoder.encode(function);

        // Call the contract method
        EthCall response = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                        credentials.getAddress(), contractAddress, encodedFunction
                ),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
        ).send();

        if (response.isReverted()) {
            throw new RuntimeException("Contract call reverted: " + response.getRevertReason());
        }

        // Decode the response
        List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return (BigInteger) result.get(0).getValue();
    }

}
