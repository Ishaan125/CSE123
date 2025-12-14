// Ishaan Srivastava 
// 12/5/25
// CSE 123 
// C3: B(e)ST of the B(e)ST
// TA: Aidan Suen

import java.util.*;
import java.io.*;

// Client class to manage the collection of games and interact with the user.
public class Client {
    // Main method to run the collection manager program.
    // Throws FileNotFoundException if the specified file is not found.
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 123 Collection Manager! " +
                           "To begin, enter your desired mode of operation:");
        System.out.println();
        System.out.println("1) Start with an empty collection manager");
        System.out.println("2) Load collection from file");
        System.out.print("Enter your choice here: ");

        int choice = Integer.parseInt(console.nextLine());
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid choice! Try again");
            choice = Integer.parseInt(console.nextLine());
        }

        CollectionManager collectionManager = null;
        if (choice == 1) {
            collectionManager = new CollectionManager();
        } else { // choice == 2
            System.out.print("Enter file to read: ");
            String inFileName = console.nextLine();
            File inFile = new File(inFileName);
            while (!inFile.exists()) {
                System.out.println("  File does not exist. Please try again.");
                System.out.print("Enter file to read: ");
                inFileName = console.nextLine();
                inFile = new File(inFileName);
            }
    
            collectionManager = new CollectionManager(new Scanner(inFile));
            System.out.println("Collection manager created!");
            System.out.println();
        }

        menu();
        String option = console.nextLine();
        while (!option.equalsIgnoreCase("quit")) {
            System.out.println();

            if (option.equalsIgnoreCase("add")) {
                collectionManager.add(Game.parse(console));
                System.out.println();
                System.out.println();
            } else if (option.equalsIgnoreCase("contains")) {
                System.out.println(collectionManager.contains(Game.parse(console)));
                System.out.println();
            } else if (option.equalsIgnoreCase("print")) {
                System.out.println(collectionManager.toString());
                System.out.println();
            } else if (option.equalsIgnoreCase("filter pop")) {
                System.out.println("Enter an integer to filter to a list of the games that have " +
                    "at least that many copies sold: ");
                System.out.println(collectionManager.filterExtension(Integer.parseInt(
                    console.nextLine())));
                System.out.println();
            } else if (option.equalsIgnoreCase("filter rating")) {
                System.out.println("Enter a double to filter to a list of " +
                    "the games that have at least that rating: ");
                System.out.println(collectionManager.filterExtension(Double.parseDouble(
                    console.nextLine())));
                System.out.println();
            } else if (option.equalsIgnoreCase("save")) {
                System.out.print("Enter file to save to: ");
                String outFileName = console.nextLine();
                PrintStream outFile = new PrintStream(new File(outFileName));
                collectionManager.save(outFile);
                System.out.println("Collection Manager exported!");
                System.out.println();
            } else if (!option.equalsIgnoreCase("quit")) {
                System.out.println("  Invalid choice. Please try again.");
                System.out.println();
            }

            menu();
            option = console.nextLine();
        }
    }

    // Prints the menu of options for the user.
    private static void menu() {
        System.out.println("What would you like to do? Choose an option in brackets.");
        System.out.println("  [add] item");
        System.out.println("  [contains] item");
        System.out.println("  [print] my collection");
        System.out.println("  [save] my collection");
        System.out.println("  [filter pop] extension");
        System.out.println("  [filter rating] extension");
        System.out.println("  [quit] program");
    }
}

