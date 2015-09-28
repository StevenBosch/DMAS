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
import java.util.HashMap;
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

    public static void initGrid(Cell grid[][], HashMap<String, Integer> param) {
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                grid[i][j] = new Cell();
            }
        }
    }

    public static void initAgents(Agent[] agents, Cell cells[][], HashMap<String, Integer> param) {
        // Initialize the grid with a new set of agents
        for (int i = 0; i < param.get("NRAGENTS"); ++i)
            agents[i] = new Agent();

        int totalCops;
        int totalHostiles;
        int totalCivilians;

    }

    public static void determineAction(Agent ag) {

    }

    public static void playGame(Cell cell, double noise) {
        // Let all the agents in the cell determine their action and update the number of saves, kills and losses accordingly
        for (Agent ag : cell.agents) {
            ag.setAwareness((double) (cell.getNrGood() / cell.getNrBad()) * noise);
            determineAction(ag);
            if (ag.getAction() == agentActions.SAVE) {
                cell.setSaves(cell.getSaves() + 1);
            }
        }

        cell.setNrNeutral(cell.getNrNeutral() - cell.getSaves());
    }

    public static void updateAgents(Cell cell) {
        // Update the decision tables of the agents
        double successRate = 0;
        for (Agent ag : cell.agents) {

        }
    }

    public static void updateCells(Cell cells[][], HashMap<String, Integer> param) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; i < param.get("WIDTH"); ++j) {
                playGame(cells[i][j], param.get("NOISE"));

                updateAgents(cells[i][j]);
            }
        }
    }
    
    // Run one iteration of a simulation
    public static void run (Cell cells[][]) {
        //Determine the actions of each agents
    }
            

    public static void main(String[] args) {

        // The parameters
        HashMap<String, Integer> param = new HashMap<String, Integer>()
        {{
            put("LENGTH", 20);
            put("WIDTH", 20);
            put("FOV", 1);
            put("NOISE", 0);
            put("NRAGENTS", 10);    
        }};
        
        // Create the griddy
        Cell[][] grid = new Cell[param.get("LENGTH")][param.get("WIDTH")];
        initGrid(grid, param);

        // Initialize the griddy
        Agent[] agents = new Agent[param.get("NRAGENTS")];
        initAgents(agents, grid, param);

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import/Edit the layout to show the griddy
        GUIFrame gFrame = new GUIFrame();
        gFrame.jPanel1.setLayout(
                new GridLayout(
                        param.get("LENGTH"), 
                        param.get("WIDTH")
                ));
        
        gFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        gFrame.jSplitPane1.setDividerLocation(screenSize.height);
        gFrame.jSplitPane1.setEnabled(false);

        // Add the grid buttons
        for (int row = 0; row < param.get("LENGTH"); ++row) {
            for (int col = 0; col < param.get("WIDTH"); ++col) {
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
                    gFrame.infoField.setText("ouch, you clicked row" + finalRow + ", column " + finalCol);
                });
                gFrame.buttonGroup1.add(btn);
                gFrame.jPanel1.add(btn);
            }
        }

        // Display the gui frame
        gFrame.setVisible(true);

        // Lets do a simulation
        //updateCells(LENGTH, WIDTH, grid, noise);
    }
}
