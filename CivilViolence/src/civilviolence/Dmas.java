/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author maleco
 */
public class Dmas implements ActionListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!");

        int LENGTH = 20;
        int WIDTH = 20;

        // Create the griddy
        Cell[][] grid = new Cell[LENGTH][WIDTH];
        for (int i = 0; i < LENGTH; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                grid[i][j] = new Cell();
            }
        }

        // Print the griddy
        for (int i = 0; i < LENGTH; ++i) {
            System.out.print("|\t");
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(grid[i][j].getDespair() + "\t|\t");
            }
            System.out.print('\n');
        }

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import the layout to show the griddy
        GUIframe test = new GUIframe();
        test.jPanel1.setLayout(new GridLayout(LENGTH, WIDTH));
        test.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        test.jSplitPane1.setDividerLocation(screenSize.height);
        test.jSplitPane1.setEnabled(false);

        // Add the grid buttons
        for (int row = 0; row < LENGTH; ++row) {
            for (int col = 0; col < WIDTH; ++col) {
                final int finalRow = row;
                final int finalCol = col;
                JButton btn = new javax.swing.JButton();
                btn.setBackground(
                        new Color(
                                255 - grid[row][col].getDespair(),
                                0,
                                grid[row][col].getDespair()
                        )
                );
                btn.addActionListener((ActionEvent e) -> {
                    System.out.println("You clicked the button");
                    test.infoField.setText("ouch, you clicked row" + finalRow + ", column " + finalCol);
                });
                test.buttonGroup1.add(btn);
                test.jPanel1.add(btn);
            }
        }

        test.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
