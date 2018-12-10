package org.mavriksc.messin;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ReadPi {
    public static void main(String[] args) throws Exception {
        // instantiate input file
        java.io.File file = new java.io.File(ClassLoader.getSystemResource("pi.txt").getFile());

        // instantiate scanner to read file
        Scanner scanner = new Scanner(file);

        // instantiate array
        String[][] array = new String[20][45];
        scanner.useDelimiter("");

        // instantiate counter
        int i = 1;

        // loop to read file and assign a character to each array element
        for (int rows = 0; rows < array.length; rows++) {
            for (int columns = 0; columns < array[rows].length; columns++) {
                System.out.println("Row: " + rows + ", column: " + columns + ", counter = " + i);
                i++;
                array[rows][columns] = scanner.next();

            } // end inner for loop

        } // end outer for loop
        scanner.close();
    }// end main method
}
