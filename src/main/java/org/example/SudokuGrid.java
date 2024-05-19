package org.example;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


public class SudokuGrid extends JPanel {

    private  Cell[][] grid;
    private SudokuSolver solver;
    private int dimension;
    private JPanel gridPanel;
    private JPanel buttonPanel;
    private JButton solveButton;
    private JButton clearButton;
    private ArrayList<String> manuallySetCellsLabels;

    SudokuGrid(int dimension) {
        this.grid = new Cell[dimension][dimension];
        this.solver = new SudokuSolver(this.grid);
        this.dimension = dimension;

        this.populateGridObject();

        this.gridPanel = new JPanel();
        this.buttonPanel = new JPanel();


        this.gridPanel.setLayout(new GridLayout(this.dimension, this.dimension));
        this.gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.drawBoard();

        this.clearButton = new JButton("Clear");
        this.solveButton = new JButton("Solve");
        this.buttonPanel.setLayout(new BorderLayout());
        this.buttonPanel.add(clearButton, BorderLayout.WEST);
        this.buttonPanel.add(solveButton, BorderLayout.EAST);
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
                JTextField field = cell;
                field.setBorder(border);
                field.setPreferredSize(fieldDimension);
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(new Font("Arial", Font.PLAIN, 20));

                int number = cell.getNumber();

                if(cell.isManuallySet){
                    field.setBackground(Config.MANUALY_SET_COLOR);
                    field.setText(number+"");
                }
                else if(number >0){
                    field.setBackground(Config.REASONER_SET_VALUE);
                    field.setText(number+"");
                }
                this.gridPanel.add(field);
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
                //TODO :After Clearing the Grid The Background Colors Saty the Same
            }
        }
    }

    private void solveSudoku() {

        //JOptionPane.showMessageDialog(this, "Solve button pressed!");
        try {
            this.manuallySetCellsLabels =new ArrayList<>();
            for (Cell[] cells : grid) {
                for (Cell cell : cells) {
                    if (cell.isManuallySet) {
                        this.manuallySetCellsLabels.add(cell.getLabel());
                    }
                }
            }
            this.solver.solve(this.manuallySetCellsLabels);
            this.drawBoard();
        } catch (OWLOntologyCreationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

}
