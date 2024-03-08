package org.example;

import java.util.Scanner;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String name;
        String network ="";
        String id ="2";
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        name = sc.nextLine();
        System.out.println("Choose the Network. \n1. Testnet-magic \n2. Mainnet \n3. Exit");
        int a = sc.nextInt();
        switch (a){
            case 1:
                network = "--testnet-magic";
                break;
            case 2:
                network = "--mainnet";
                break;
            case 3:
                exit(0);
            default:
                System.out.println("You Have to choose the network");

        }


        CardanoCLIJava.exe(name,network,id);
    }
}