package civilviolence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maleco
 */
import java.util.Random;
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
            }
        }
    }

    public static void initAgents(Cell grid[][], HashMap<String, Integer> param) {
        Random rand = new Random();

        // Initialize the grid with a new set of agents
        for (int i = 0; i < param.get("NRCOPS"); ++i) {
            Agent agent = new Agent();
            grid[rand.nextInt(param.get("LENGTH"))][rand.nextInt(param.get("WIDTH"))].addAgent(agent);
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
            if ((cell.getNrHostiles()+cell.getAgents().size()) != 0) {
                ag.setAwareness((double)(cell.getAgents().size()/(cell.getNrHostiles()+cell.getAgents().size()))*noise);
            } else ag.setAwareness(1);
            if ((cell.getNrHostiles()+cell.getNrNeutral()) != 0) {
                ag.setDanger((double)(cell.getNrNeutral()/(cell.getNrHostiles()+cell.getNrNeutral()))*noise);
            } else ag.setDanger(1);
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
        if ((aim * hostileAimCops * cell.getNrHostiles()) > cell.getAgents().size()) { // If the hostiles would kill more cops than there are
            cell.setLossesCops(cell.getAgents().size()); // All the cops are killed
        } else {
            cell.setLossesCops((int) (aim * hostileAimCops * cell.getNrHostiles())); // Set the number of cop losses
        }
        if ((aim * (1 - hostileAimCops) * cell.getNrHostiles()) > cell.getNrNeutral()) { // Same for civilian losses
            cell.setLossesNeutral(cell.getNrNeutral());
        }
        cell.setLossesNeutral((int) (aim * (1 - hostileAimCops) * cell.getNrHostiles()));

        // Update the numbers in the cell
        cell.setNrHostiles(cell.getNrHostiles() - cell.getKills());
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

    public static void main(String[] args) {

        // The parameters
        final HashMap<String, Integer> param = new HashMap<String, Integer>() {
            {
                put("LENGTH", 20);
                put("WIDTH", 20);
                put("FOV", 1);
                put("NOISE", 0);
                put("NRCOPS", 4000);
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
        initAgents(grid, param);

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import/Edit the layout to show the griddy
        final GUIFrame gFrame = new GUIFrame(grid, param);
        
        JButton btn = new javax.swing.JButton("Doe een rondje");
        final Cell[][] grid2 = grid;
        btn.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                updateCells(grid2, param);
            }
        });
        gFrame.ControlFrame.add(btn);
        // Display the gui frame
        gFrame.setVisible(true);
        
        // Run the simulation
        updateCells(grid, param);
    }
}
