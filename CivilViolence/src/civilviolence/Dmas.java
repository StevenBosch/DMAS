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
                grid[i][j] = new Cell(param);
                initAgents(grid[i][j], param);
            }
        }
    }

    public static void initAgents(Cell cell, HashMap<String, Integer> param) {
        // Initialize the grid with a new set of agents
        for (int i = 0; i < param.get("NRCOPS"); ++i) {
            Agent agent = new Agent();
            cell.addAgent(agent);
        }
    }

    public static void determineAction(Agent ag) {
        if (ag.getAwareness() > 0.5 && ag.getDanger() > 0.5) { // Cops and civilians are a majority
            ag.setCurrentSituation(0);
            if (ag.getDecTable()[0][0] > ag.getDecTable()[1][0]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() > 0.5 && ag.getDanger() <= 0.5) { // Cops are a majority and civilians are a minority
            ag.setCurrentSituation(1);
            if (ag.getDecTable()[0][1] > ag.getDecTable()[1][1]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() <= 0.5 && ag.getDanger() > 0.5) { // Cops are a minority and civilians are a majority
            ag.setCurrentSituation(2);
            if (ag.getDecTable()[0][2] > ag.getDecTable()[1][2]){ // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() >= 0.5 && ag.getDanger() <= 0.5) { // Cops and civilians are a minority
            ag.setCurrentSituation(3);
            if (ag.getDecTable()[0][3] > ag.getDecTable()[1][3]){ // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
        }
    }

    public static void playGame(Cell cell, double noise) {
        // Let all the agents in the cell determine their action and update the number of saves, kills and losses accordingly
        int shootingCops = cell.getAgents().size(); // The number of cops that chooses to shoot
        double aim = 0.5; // The uncertainty of actually hitting someone
        double hostileAimCops = 0.5; // The extent to which hostiles aim at cops.
        
        for (Agent ag: cell.getAgents()) {
            ag.setAwareness((double)(cell.getNrGood()/(cell.getNrHostiles()+cell.getNrGood()))*noise);
            ag.setDanger((double)(cell.getNrNeutral()/(cell.getNrHostiles()+cell.getNrNeutral()))*noise);
            determineAction(ag);
            if (ag.getAction() == agentActions.SAVE) { // If the current agent chooses to save a civilian
                if (cell.getNrNeutral() > 0) { // And there are still civilians left
                    cell.setSaves(cell.getSaves() + 1); // Increase the number of saves
                    cell.setNrNeutral(cell.getNrNeutral() - 1); // Decrease the number of civilians
                }
                shootingCops--; // Decrease the number of shooting cops
            }
        }

        if ((shootingCops * aim) > cell.getNrHostiles()) { // If the cops would kill more hostiles than there are
            cell.setKills((int) (cell.getNrHostiles())); // The amount of kills is the amount of hostiles
        } else {
            cell.setKills((int) (shootingCops * aim)); // Else just set the number of kills by cops
        }
        if ((aim * hostileAimCops * cell.getNrHostiles()) > cell.getNrGood()) { // If the hostiles would kill more cops than there are
            cell.setLossesCops(cell.getNrGood()); // All the cops are killed
        } else {
            cell.setLossesCops((int) (aim * hostileAimCops * cell.getNrHostiles())); // Set the number of cop losses
        }
        if ((aim * (1 - hostileAimCops) * cell.getNrHostiles()) > cell.getNrNeutral()) { // Same for civilian losses
            cell.setLossesNeutral(cell.getNrNeutral());
        }
        cell.setLossesNeutral((int) (aim * (1 - hostileAimCops) * cell.getNrHostiles()));

        // Update the numbers in the cell
        cell.setNrHostiles(cell.getNrHostiles() - cell.getKills());
        cell.setNrGood(cell.getNrGood() - cell.getLossesCops());
        cell.setNrNeutral(cell.getNrNeutral() - cell.getLossesNeutral());
    }

    public static void killAgents(Cell cell) {
        // Remove killed agents from the list 
        cell.setAgents(cell.getAgents().subList(0, cell.getAgents().size() - cell.getLossesCops()));
    }

    public static void updateAgents(Cell cell) {
        // Update the decision tables of the agents
        double success = cell.getSuccess();
        for (Agent ag : cell.getAgents()) {
            if(ag.getAction() == agentActions.SAVE) {
                ag.getDecTable()[0][ag.getCurrentSituation()] = (ag.getDecTable()[0][ag.getCurrentSituation()] + cell.getSuccess() * ag.getLearningRate())/(1+ag.getLearningRate());
            } else ag.getDecTable()[1][ag.getCurrentSituation()] = (ag.getDecTable()[1][ag.getCurrentSituation()] + cell.getSuccess() * ag.getLearningRate())/(1+ag.getLearningRate());
        }
    }

    public static void updateMovements(Cell[][] grid, Cell cell) {

    }

    public static void updateCells(Cell[][] grid, HashMap<String, Integer> param) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                playGame(grid[i][j], param.get("NOISE"));
                killAgents(grid[i][j]);
                updateAgents(grid[i][j]);
                updateMovements(grid, grid[i][j]);
            }
        }
    }

    // Run one iteration of a simulation
    public static void run(Cell cells[][]) {
        //Determine the actions of each agents
    }

    public static void main(String[] args) {

        // The parameters
        final HashMap<String, Integer> param = new HashMap<String, Integer>() {
            {
                put("LENGTH", 20);
                put("WIDTH", 20);
                put("FOV", 1);
                put("NOISE", 0);
                put("NRCOPS", 400);
                put("NRHOSTILES", 10);
                put("MEANNEUTRAL", 200);
                put("STDNEUTRAL", 40);
                put("MEANHOSTILES", 20);
                put("STDHOSTILES", 5);
                put("SUCCESCRIT", 0);
                put("TOTALNRNEUTRAL", 0);
                put("TOTALNRHOSTILES", 0);
            }
        };

        // Create and initialize the griddy
        Cell[][] grid = new Cell[param.get("LENGTH")][param.get("WIDTH")];
        initGrid(grid, param);

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import/Edit the layout to show the griddy
        final GUIFrame gFrame = new GUIFrame();
        gFrame.GridPanel.setLayout(
                new GridLayout(
                        param.get("LENGTH"),
                        param.get("WIDTH")
                ));
        gFrame.ControlFrame.setLayout(new GridLayout(1,3));

        gFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        gFrame.jSplitPane1.setDividerLocation(screenSize.height);
        gFrame.jSplitPane1.setEnabled(false);

        // Add the grid buttons
        for (int row = 0; row < param.get("LENGTH"); ++row) {
            for (int col = 0; col < param.get("WIDTH"); ++col) {

                // Variables to be used in the action listener
                // (have to be final for some reason)
                final int finalRow = row;
                final int finalCol = col;
                final int nrNeutral = grid[row][col].getNrNeutral();
                final int nrHostiles = grid[row][col].getNrHostiles();
                final int nrCops = grid[row][col].getNrGood();

                // Create the button
                JButton btn = new javax.swing.JButton();

                // Set a random background
                if (nrHostiles == 0) {
                    btn.setBackground(new Color(0, 0, 255));
                } else if (nrCops == 0 & nrHostiles != 0) {
                    btn.setBackground(new Color(255, 0, 0));
                } else {
                    // Range from -1 to 1
                    float temp = ((((float) (nrCops - nrHostiles) / (nrCops + nrHostiles)) + 1) / 2) * 255;
                    btn.setBackground(new Color(
                            (int) (255 - temp),
                            0,
                            (int) temp
                    ));
                }
                // Set the text as number of neutrals
                btn.setFont(new Font("Arial", Font.PLAIN, 12));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setForeground(Color.WHITE);
                btn.setText("" + grid[row][col].getNrNeutral());

                // Adjust the information box on buttonclick
                // btn.addActionListener((ActionEvent e) -> {
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        gFrame.infoField.setText(
                                "\n\n\t Number of neutrals on this site:\t" + nrNeutral + '\n'
                                + "\t Number of hostiles on this site:\t" + nrHostiles + '\n'
                                + "\t Number of cops on this site:\t\t" + nrCops
                                + "\n\n"
                                + "\t Total of neutral on this site:\t" + param.get("TOTALNRNEUTRAL") + '\n'
                                + "\t Total of hostiles on this site:\t" + param.get("TOTALNRHOSTILES")
                        );
                    }
                });

                // Add the button to the group and the panel
                gFrame.buttonGroup1.add(btn);
                gFrame.GridPanel.add(btn);
            }
        }

        // Add the control buttons
        JButton btn = new javax.swing.JButton("Knopje 1");
        gFrame.ControlFrame.add(btn);
        JButton btn2 = new javax.swing.JButton("Knopje 2");
        gFrame.ControlFrame.add(btn2);
        JButton btn3 = new javax.swing.JButton("Knopje 3");
        gFrame.ControlFrame.add(btn3);

        // Display the gui frame
        gFrame.setVisible(true);

        // Lets do a simulation
        //updateCells(grid, param);
    }
}
