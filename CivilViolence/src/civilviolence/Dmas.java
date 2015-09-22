/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.awt.Color;
import java.util.Enumeration;
import javax.swing.AbstractButton;
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

        // Create the grid
        Cell[][] grid = new Cell[5][5];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
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

        JFrame frame = new JFrame();
       //Display the window.
        GUIframe test = new GUIframe();
        for (int i = 0; i < 5; ++i) {
            System.out.print("|\t");
            for (int j = 0; j < 5; ++j) {

        }
        for (Enumeration<AbstractButton> buttons = test.buttonGroup1.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            button.setBackground(Color.yellow);
        }
        test.getComponents();
        test.setVisible(true);



    }
    }
}

