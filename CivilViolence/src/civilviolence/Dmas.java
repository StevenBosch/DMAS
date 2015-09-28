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
import java.util.*;

/**
 *
 * @author maleco
 */
public class Dmas {
    
    public enum agentActions {
        SAVE, SHOOT;
    }
    
    public static void initAgents(Cell cells[][]) {
        // Initialize the grid with a new set of agents
        
        int totalCops;
        int totalHostiles;
        int totalCivilians;
        
    }
    
    public static void determineAction(Agent ag) {
        
    }
    
    public static void playGame(Cell cell, double noise) {
        // Let all the agents in the cell determine their action and update the number of saves, kills and losses accordingly
        for (Agent ag: cell.agents) {
            ag.setAwareness((double)(cell.getNrGood()/cell.getNrBad())*noise);
            determineAction(ag);
            if (ag.getAction() == agentActions.SAVE) {
                cell.setSaves(cell.getSaves()+1);
            }
        }

        
        cell.setNrNeutral(cell.getNrNeutral()-cell.getSaves());
    }
    
    public static void updateAgents(Cell cell) {
        // Update the decision tables of the agents
        double successRate = 0;
        for (Agent ag: cell.agents) {
                
        }
    }
    
    public static void updateCells(int length, int width, Cell cells[][], double noise) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < length; ++i) {
            for (int j = 0; i < width; ++j) {
                playGame(cells[i][j], noise);
                
                updateAgents(cells[i][j]);
            }
        }
    }
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!");
        int vision = 1; // Field of view for every agent
        double noise = 0; // Noise for the awareness of agents
        
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
        
        initAgents(grid);
        updateCells(LENGTH, WIDTH, grid, noise);
        

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
