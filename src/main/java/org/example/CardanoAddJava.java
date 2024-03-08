package org.example;

import java.io.*;

public class CardanoAddJava {
    public static void main(String[] args) {
        exe();
    }

    public static void exe() {
        try {
            // Path to the cardano-address executable within your project directory
            String path = "src/main/resources/bin/";
            String addressPath = path + "cardano-address"; // Path to cardano-address executable

            // Read the seed phrase from the file
            String seedPhraseFilePath = "seed_phrase.txt";
            String seedPhrase = readFromFile(seedPhraseFilePath);
            System.out.println("Seed phrase: " + seedPhrase);

            // Derive the root private key from the seed phrase
            String rootPrivateKey = deriveRootPrivateKey(addressPath, seedPhraseFilePath);
            System.out.println("Root private key: " + rootPrivateKey);

            // Derive the payment public key from the root private key
            String paymentPublicKey = derivePaymentPublicKey(addressPath, rootPrivateKey);
            System.out.println("Payment public key: " + paymentPublicKey);

            // Generate the address from the payment public key
            generateAddress(addressPath, paymentPublicKey);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator());
            }
        }
        return contentBuilder.toString().trim();
    }

    private static String deriveRootPrivateKey(String addressPath, String seedPhraseFilePath) throws IOException, InterruptedException {
        // Derive root private key from the seed phrase
        String[] rootPrivateKeyCommand = {
                addressPath, "key", "from-recovery-phrase", "Shelley"
        };

        ProcessBuilder pbRootPrivateKey = new ProcessBuilder(rootPrivateKeyCommand);
        pbRootPrivateKey.redirectInput(new File(seedPhraseFilePath)); // Pass seed phrase file as input
        pbRootPrivateKey.redirectErrorStream(true); // Redirect error stream to input stream

        Process processRootPrivateKey = pbRootPrivateKey.start();

        // Read the output stream for root private key derivation
        BufferedReader readerRootPrivateKey = new BufferedReader(new InputStreamReader(processRootPrivateKey.getInputStream()));
        StringBuilder rootPrivateKeyBuilder = new StringBuilder();
        String lineRootPrivateKey;
        while ((lineRootPrivateKey = readerRootPrivateKey.readLine()) != null) {
            rootPrivateKeyBuilder.append(lineRootPrivateKey).append("\n");
        }

        // Wait for the root private key derivation process to finish
        int exitCodeRootPrivateKey = processRootPrivateKey.waitFor();
        System.out.println("Root private key derivation exited with error code: " + exitCodeRootPrivateKey);

        // Store the root private key in a variable
        return rootPrivateKeyBuilder.toString().trim();
    }

    private static String derivePaymentPublicKey(String addressPath, String rootPrivateKey) throws IOException, InterruptedException {
        // Derive payment public key from the root private key
        String[] paymentPublicKeyCommand = {
                addressPath, "key", "child", "1852H/1815H/0H/0/0", rootPrivateKey
        };

        ProcessBuilder pbPaymentPublicKey = new ProcessBuilder(paymentPublicKeyCommand);
        pbPaymentPublicKey.redirectErrorStream(true);
        Process processPaymentPublicKey = pbPaymentPublicKey.start();

        // Read the output stream for payment public key derivation
        BufferedReader readerPaymentPublicKey = new BufferedReader(new InputStreamReader(processPaymentPublicKey.getInputStream()));
        StringBuilder paymentPublicKeyBuilder = new StringBuilder();
        String linePaymentPublicKey;
        while ((linePaymentPublicKey = readerPaymentPublicKey.readLine()) != null) {
            paymentPublicKeyBuilder.append(linePaymentPublicKey).append("\n");
        }

        // Wait for the payment public key derivation process to finish
        int exitCodePaymentPublicKey = processPaymentPublicKey.waitFor();
        System.out.println("Payment public key derivation exited with error code: " + exitCodePaymentPublicKey);

        // Store the payment public key in a variable
        return paymentPublicKeyBuilder.toString().trim();
    }

    private static void generateAddress(String addressPath, String paymentPublicKey) throws IOException, InterruptedException {
        // Generate the address from the payment public key
        String[] addressCommand = {
                addressPath, "address", "build", "--payment-verification-key-file", paymentPublicKey,
                "--testnet-magic", "42", "--out-file", "generated_address.txt"
        };

        ProcessBuilder pbAddress = new ProcessBuilder(addressCommand);
        pbAddress.redirectErrorStream(true); // Redirect error stream to input stream
        Process processAddress = pbAddress.start();

        // Read the output stream for address generation
        BufferedReader readerAddress = new BufferedReader(new InputStreamReader(processAddress.getInputStream()));
        String lineAddress;
        while ((lineAddress = readerAddress.readLine()) != null) {
            System.out.println(lineAddress);
        }

        // Wait for the address generation process to finish
        int exitCodeAddress = processAddress.waitFor();
        System.out.println("Address generation exited with error code: " + exitCodeAddress);
    }
}
