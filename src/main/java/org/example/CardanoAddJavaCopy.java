package org.example;

import java.io.*;

public class CardanoAddJavaCopy {
    public static void exe() {
        try {
            // Path to the cardano-address executable within your project directory
            String path = "src/main/resources/bin/";
            String addressPath = path + "cardano-address"; // Path to cardano-address executable

            // Read the seed phrase from the file
            String seedPhraseFilePath = "seed_phrase.txt";
            String seedPhrase = readFromFile(seedPhraseFilePath);
            System.out.println("Seed phrase: " + seedPhrase);

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
            String rootPrivateKey = rootPrivateKeyBuilder.toString().trim();
            System.out.println("Root private key: " + rootPrivateKey);
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
}
