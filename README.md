# Ethereum Smart Contract Interaction using Spring Boot

This project demonstrates how to interact with an Ethereum smart contract using a **Spring Boot application**.
The smart contract is deployed on the Ethereum blockchain, and the application provides REST endpoints to interact with it (e.g., set and get values).

---

## Features
- **Interact with Smart Contracts**:
  - Call contract functions to set or get values.
- **Local Transaction Signing**:
  - Transactions are signed locally using the private key, ensuring security.
- **REST API Interface**:
  - Simple endpoints to interact with the smart contract.

---

## Prerequisites

1. **Java and Maven**:
   - Install [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
   - Install [Maven](https://maven.apache.org/download.cgi).

2. **Smart Contract**:
   - A deployed Ethereum smart contract.
   - You can deploy the following contract using Remix IDE:
     ```solidity
     // SPDX-License-Identifier: MIT
     pragma solidity ^0.8.0;

     contract SimpleStorage {
         uint256 public storedValue;

         event ValueUpdated(uint256 oldValue, uint256 newValue);

         function setValue(uint256 newValue) public {
             uint256 oldValue = storedValue;
             storedValue = newValue;
             emit ValueUpdated(oldValue, newValue);
         }

         function getValue() public view returns (uint256) {
             return storedValue;
         }
     }
     ```

3. **Ethereum Node/Provider**:
   - Connect to an Ethereum node (e.g., [Infura](https://infura.io/) or [Alchemy](https://www.alchemy.com/)). I used Infura.

4. **Web3j**:
   - A Java library for interacting with the Ethereum blockchain.
     
     

5.**PostMan**:
-To manually test the Post and Get APIs of the application

6**Can also use the wrapper method of interacting with Smart Contract**:
-Create a wrapper of your smart contract using the Web3j library and place it in the project folder of your intellij.


## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/ethereum-spring-boot.git
cd ethereum-spring-boot
