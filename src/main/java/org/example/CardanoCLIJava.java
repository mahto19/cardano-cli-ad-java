package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CardanoCLIJava {
    public static void exe(String name,String network, String id) {
        try {
            // Path to the cardano-cli executable within your project directory
            String path = "src/main/resources/bin/";
            String cliPath = path + "cardano-cli"; // Adjust the path to the cardano-cli executable

            // First command to execute

            String[] command1 = {cliPath, "address", "key-gen", "--verification-key-file", name+".vkey",
                    "--signing-key-file", name+".skey"};

            // Second command to execute
            String[] command2;
            if (network.equals("--mainnet")){
                 command2 = new String[]{cliPath, "address", "build",
                        "--payment-verification-key-file", name + ".vkey", network,
                        "--out-file", name + ".addr"};
            }else {
                command2 = new String[]{cliPath, "address", "build",
                        "--payment-verification-key-file", name + ".vkey", network, id,
                        "--out-file", name + ".addr"};
            }

            // Start both processes
            ProcessBuilder pb1 = new ProcessBuilder(command1);
            ProcessBuilder pb2 = new ProcessBuilder(command2);

            Process process1 = pb1.start();
            int exitCode1 = process1.waitFor();
            if (exitCode1 == 0){
                readOutput(process1.getInputStream(), process1.getErrorStream());
                System.out.println("Exited with error code for command1: " + exitCode1);
                Process process2 = pb2.start();
                readOutput(process2.getInputStream(), process2.getErrorStream());
                int exitCode2 = process2.waitFor();
                System.out.println("Exited with error code for command2: " + exitCode2);
            }else{
                System.out.println("Command1 not executed successfully");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void readOutput(InputStream inputStream, InputStream errorStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

        // Read the output of the process
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Read the error output of the process
        while ((line = errorReader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
