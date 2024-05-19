package org.example;

import javax.swing.*;

public class SudokuGrid  extends JPanel {

    private  Cell[][] grid;

    private  int dimension;
    private  JPanel gridPanel;
    private  JPanel buttonPanel;
    private  JButton solveButton;
    private  JButton clearButton;
    private  JPanel[][] minisquarePanels;

    SudokuGrid(int dimension) {
        this.grid = new Cell[dimension][dimension];

        for (int row = 0; row < dimension; ++row) {
            for (int col = 0; col < dimension; ++col) {
                int block = SudokuUtils.getBlockNumber(row + 1, col + 1);
                int[] coordInBlock = SudokuUtils.getIndexesInBlockMatrix(block, row + 1, col + 1);
                grid[row][col] = new Cell(block, coordInBlock[0], coordInBlock[1]);

            }
        }

    }
    

}
