/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.awt.Color;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maleco
 */
public class Dmas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!");

        int LENGTH = 5;
        int WIDTH = 5;

        // Create the griddy
        Cell[][] grid = new Cell[5][5];
        for (int i = 0; i < LENGTH; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                grid[i][j] = new Cell();
            }
        }

        // Print the griddy
        for (int i = 0; i < 5; ++i) {
            System.out.print("|\t");
            for (int j = 0; j < 5; ++j) {
                System.out.print(grid[i][j].getDespair() + "\t|\t");
            }
            System.out.print('\n');
        }

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import the layout to show the griddy
        GUIframe test = new GUIframe();

        JButton testbutton = new javax.swing.JButton();
        testbutton.setBackground(Color.BLACK);
        test.buttonGroup1.add(testbutton);
        test.jPanel1.add(testbutton);
        test.setVisible(true);

    }
}
