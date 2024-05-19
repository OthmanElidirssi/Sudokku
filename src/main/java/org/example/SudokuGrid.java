package org.example;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


public class SudokuGrid extends JPanel {

    private final Cell[][] grid;
    private final SudokuSolver solver;
    private final int dimension;
    private final JPanel gridPanel;

    SudokuGrid(int dimension) {
        this.grid = new Cell[dimension][dimension];
        this.solver = new SudokuSolver(this.grid);
        this.dimension = dimension;

        this.populateGridObject();

        this.gridPanel = new JPanel();
        JPanel buttonPanel = new JPanel();


        this.gridPanel.setLayout(new GridLayout(this.dimension, this.dimension));
        this.gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.drawBoard();

        JButton clearButton = new JButton("Clear");
        JButton solveButton = new JButton("Solve");
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(clearButton, BorderLayout.WEST);
        buttonPanel.add(solveButton, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);


        clearButton.addActionListener(e -> clearGrid());

        solveButton.addActionListener(e -> solveSudoku());
    }


    public void drawBoard(){
        this.gridPanel.removeAll();
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        Dimension fieldDimension = new Dimension(70, 70);
        for (int y = 0; y < this.dimension; ++y) {
            for (int x = 0; x < this.dimension; ++x) {
                Cell cell =  grid[y][x];
                cell.setBorder(border);
                cell.setPreferredSize(fieldDimension);
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.PLAIN, 20));

                int number = cell.getNumber();

                if(cell.isManuallySet){
                    cell.setBackground(Config.MANUALY_SET_COLOR);
                    cell.setText(number+"");
                }
                else if(number >0){
                    cell.setBackground(Config.REASONER_SET_VALUE);
                    cell.setText(number+"");
                }
                this.gridPanel.add(cell);
            }
        }
    }

    public void populateGridObject(){
        for (int row = 0; row < this.dimension; ++row) {
            for (int col = 0; col < this.dimension; ++col) {
                int block = SudokuUtils.getBlockNumber(row + 1, col + 1);
                int[] coordInBlock = SudokuUtils.getIndexesInBlockMatrix(block, row + 1, col + 1);
                grid[row][col] = new Cell(block, coordInBlock[0], coordInBlock[1]);
            }
        }
    }

    private void clearGrid() {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                grid[row][col].setText("");
                grid[row][col].setManuallySet(false);
                grid[row][col].setNumber(0);
                grid[row][col].setBackground(Config.UNSET_COLOR);
            }
        }
    }

    private void solveSudoku() {
        try {
            ArrayList<String> manuallySetCellsLabels = new ArrayList<>();
            for (Cell[] cells : grid) {
                for (Cell cell : cells) {
                    if (cell.isManuallySet) {
                        manuallySetCellsLabels.add(cell.getLabel());
                    }
                }
            }
            this.solver.solve(manuallySetCellsLabels);
            this.drawBoard();
        } catch (OWLOntologyCreationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

}
