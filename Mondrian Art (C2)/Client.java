// Ishaan Srivastava 
// 11/5/25
// CSE 123 
// P2: Mondrian Art
// TA: Aidan Suen

import java.awt.*;
import java.util.*;

// This class interacts with the user to generate Mondrian art.
// It prompts the user for their choice of art complexity and image dimensions,
// then creates and displays the artwork.
public class Client {
    public static void main(String[] args) throws Exception {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 123 Mondrian Art Generator!");

        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.print("Enter 1 for a basic Mondrian or 2 for a complex Mondrian: ");
            choice = console.nextInt();
        }
        System.out.print("Enter image width (>= 300px): ");
        int width = console.nextInt();
        System.out.print("Enter image height (>= 300px): ");
        int height = console.nextInt();

        Mondrian mond = new Mondrian();
        Picture pic = new Picture(width, height);
        Color[][] pixels = pic.getPixels();

        if (choice == 1) {
            mond.paintBasicMondrian(pixels);
        } else {    // choice == 2
            mond.paintComplexMondrian(pixels);
        }
        
        pic.setPixels(pixels);
        pic.save(choice == 1 ? "basic.png" : "extension.png");
        pic.show();
        System.out.println("Enjoy your artwork!");
        console.close();
    }
}

