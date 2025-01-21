package com.IshaanBansal.Web3jDemo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/contract")
public class EthereumController {

    @Autowired
    private EthereumService ethereumService;

    @PostMapping("/setValue")
    public String setValue(@RequestParam BigInteger value) {
        try {
            String transactionHash = ethereumService.setValue(value);
            return "Transaction successful! Hash: " + transactionHash;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/getValue")
    public String getValue() {
        try {
            BigInteger storedValue = ethereumService.getValue();
            return "Stored Value: " + storedValue.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

