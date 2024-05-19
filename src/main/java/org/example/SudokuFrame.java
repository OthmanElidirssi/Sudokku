package org.example;

import javax.swing.*;

public class SudokuFrame {

    private final JFrame frame = new JFrame("Sudoku solver");
    private SudokuGrid grid;


    public SudokuFrame(SudokuGrid grid) {
        this.grid=grid;
        frame.getContentPane().add(this.grid);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }



}
