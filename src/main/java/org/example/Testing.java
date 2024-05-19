package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Testing {

    private static final int GRID_SIZE = 6;
    private static final int BLOCK_SIZE = 2;

    public static void main(String[] args) {

        Cell cell = new Cell((byte) 1, 1, 1);
        cell.setNumber(1);
       new SudokuFrame(new SudokuGrid(6));

        int number = extractNumber("<http://projet.org#23>");

        System.out.println(number);

        int[]a =SudokuUtils.getIndexesInOriginalMatrix(6,2,3);

        System.out.println(a[0]+" "+a[1]);


    }

    public static int extractNumber(String input) {
        // Define the pattern to match the number
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        // Find the first occurrence of the number and return it
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        // Return 0 if no number is found
        return 0;
    }
}



