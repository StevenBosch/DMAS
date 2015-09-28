/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maleco
 */
public class Dmas implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
    
    public static void updateCells(int length, int width, Cell cells[][], double noise) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < length; ++i) {
            for (int j = 0; i < width; ++j) {
                playGame(cells[i][j], noise);
            }
        }
    }
    
        public static void updateAgentSuccess(Agent agent) {
        // Update the success rates of the Agents
        double successRate = 0;
        
        agent.setSuccessRate(successRate);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        int LENGTH = 20;
        int WIDTH = 20;
        int vision = 1; // Field of view for every agent
        double noise = 0; // Noise for the awareness of agents

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
}
