package org.example;

public class Main {

    public static void main(String[] args){

        // Get Singleton instance
        Splitwise splitwise = Splitwise.getInstance();
        splitwise.runSplitwiseDemo();
    }
}
