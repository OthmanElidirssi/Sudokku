package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SudokuFrame {


    public SudokuFrame(SudokuGrid grid) {
        Image icon = null;
        try {
            ClassLoader classLoader = EntryPoint.class.getClassLoader();

            URL url = classLoader.getResource("passe-temps.png");
            icon = ImageIO.read(Objects.requireNonNull(url));
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }

        JFrame frame = new JFrame("Sudoku solver");
        if (icon != null) {
            frame.setIconImage(icon);
        }
        frame.getContentPane().add(grid);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }



}
