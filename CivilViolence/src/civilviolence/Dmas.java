/* TODO:
2. Succes functie
3. Movement
5. Success zichtbaar
*/

package civilviolence;
/**
 *
 * @author maleco
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.Random;

public class Dmas {

    public enum agentActions {
        SAVE, SHOOT;
    }

    public static void initGrid(Cell grid[][], HashMap<String, Integer> param) {
        for (int i = 0; i < param.get("LENGTH"); ++i)
            for (int j = 0; j < param.get("WIDTH"); ++j) 
                grid[i][j] = new Cell(param);
    }

    public static void initAgents(Cell grid[][], HashMap<String, Integer> param) {
        Random rand = new Random();
        // Initialize the grid with a new set of agents
        for (int i = 0; i < param.get("NRCOPS"); ++i) 
            grid[rand.nextInt(param.get("LENGTH"))][rand.nextInt(param.get("WIDTH"))].addAgent(new Agent());        
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
        } else if (ag.getAwareness() <= 0.5 && ag.getDanger() <= 0.5) { // Cops and civilians are a minority
            ag.setCurrentSituation(3);
            if (ag.getDecTable()[0][3] > ag.getDecTable()[1][3]){ // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
        }
    }

    // Let all the agents in the cell determine their action and update the number of saves, kills and losses accordingly
    public static void playGame(Cell cell, HashMap<String, Integer> param) {
        // Some cell variables 
        int nrHost = cell.getNrHostiles();
        int nrNeutral = cell.getNrNeutral();
        int nrCops = cell.getAgents().size();
        
        // The number of neutrals saved
        int nrNeutralSaves = 0; 
        // The uncertainty of actually hitting someone
        double aim = 0.5; 
        double noise = param.get("NOISE")/100;
        // The extent to which hostiles aim at cops.
        double hostileAimCops = 0.5; 
        
        // For every agent
        for (Agent ag: cell.getAgents()) {
            // Calculate the awareness and danger in this cell
            ag.setAwareness(
                    ((nrHost + nrCops) != 0)
                            ? (double) (nrCops / (nrHost + nrCops)) * noise
                            : 1
            );
            ag.setDanger(
                    ((nrHost + nrNeutral) != 0)
                            ? (double) (nrNeutral / (nrHost + nrNeutral)) * noise
                            : 1
            );

            // Determine the action based on awareness and danger
            determineAction(ag);
            System.out.println(ag.getAction());
            // If the current agent chooses to save a civilian decrease the number of civilians
            if (ag.getAction() == agentActions.SAVE && nrNeutral > 0) nrNeutralSaves++; 
            
        }
        // The number of shooting cops == all - the saves
        int shootingCops = nrCops - nrNeutralSaves;
        
        // Set the number of kills in this cell        
        // If the cops would kill more hostiles than available ,the amount of kills is the amount of hostiles
        // Else just set the number of kills by cops
        int nrHostileDeaths = (int) (((shootingCops * aim) > nrHost)
                ? nrHost
                : shootingCops * aim
                );
        int nrCopDeaths = (int) (((aim * hostileAimCops * nrHost) > nrCops)
                ? nrCops
                : aim * hostileAimCops * nrHost
                );        
        int nrNeutralDeaths = (int) (((aim * (1 - hostileAimCops) * nrHost) > nrNeutral)
                ? nrNeutral
                : (aim * (1 - hostileAimCops) * nrHost) 
                );
        
        // Update the numbers in the cell
        cell.setNrHostiles(nrHost - nrHostileDeaths);
        cell.setNrNeutral(nrNeutral - nrNeutralDeaths - nrNeutralSaves);
        cell.killAgents(nrCopDeaths);
        cell.setNrNeutralsSaved(cell.getNrNeutralsSaved() + nrNeutralSaves); 

        // Update the parameters
        param.put("REMAININGNRNEUTRALS", param.get("REMAININGNRNEUTRALS")-nrNeutralDeaths - nrNeutralSaves);
        param.put("REMAININGNRHOSTILES", param.get("REMAININGNRHOSTILES") - nrHostileDeaths);
        param.put("REMAININGNRCOPS", param.get("REMAININGNRCOPS") - nrCopDeaths);
        
        updateAgents(cell, param, (double) (nrNeutralSaves + nrHostileDeaths - nrCopDeaths - nrNeutralDeaths) / (double) (nrNeutralSaves + nrHostileDeaths + nrCopDeaths + nrNeutralDeaths));
    }

    public static void updateAgents(Cell cell, HashMap<String, Integer> param, double success) {
        // Update the decision tables of the agents
        double alpha = (double)param.get("LEARNINGRATE")/100;
        System.out.println(success);
        for (Agent ag : cell.getAgents()) {
            if (ag.getAction() == agentActions.SAVE) {
                ag.getDecTable()[0][ag.getCurrentSituation()] = ((((ag.getDecTable()[0][ag.getCurrentSituation()] + cell.getSuccess() * alpha) / (1 + alpha)) < 1)
                        ? (ag.getDecTable()[0][ag.getCurrentSituation()] + cell.getSuccess() * alpha) / (1 + alpha)
                        : 1
                        );
                ag.getDecTable()[1][ag.getCurrentSituation()] = ((((ag.getDecTable()[1][ag.getCurrentSituation()] - cell.getSuccess() * alpha) / (1 + alpha)) > 0)
                        ? (ag.getDecTable()[1][ag.getCurrentSituation()] - cell.getSuccess() * alpha) / (1 + alpha)
                        : 0
                        );
            } else {
                ag.getDecTable()[1][ag.getCurrentSituation()] = ((((ag.getDecTable()[1][ag.getCurrentSituation()] + cell.getSuccess() * alpha) / (1 + alpha)) < 1)
                        ? (ag.getDecTable()[1][ag.getCurrentSituation()] + cell.getSuccess() * alpha) / (1 + alpha)
                        : 1
                        );
                ag.getDecTable()[0][ag.getCurrentSituation()] = ((((ag.getDecTable()[0][ag.getCurrentSituation()] - cell.getSuccess() * alpha) / (1 + alpha)) > 0)
                        ? (ag.getDecTable()[0][ag.getCurrentSituation()] - cell.getSuccess() * alpha) / (1 + alpha)
                        : 0
                        );
            }
        }
    }

    public static void updateMovements(Cell[][] grid, Cell cell, HashMap<String, Integer> param) {
            
    }
    
    public static int playOneRound (Cell[][] grid, HashMap<String, Integer> param, GUIFrame gFrame) {
        int oldNrNeutrals = param.get("REMAININGNRNEUTRALS");
        updateCells(grid, param);
        gFrame.updateGridButtons(grid, param);
        param.put("EPOCH", param.get("EPOCH")+1);
        
        // Click a button to update the infotext field
        gFrame.clickAButton();
        
        if (param.get("REMAININGNRNEUTRALS") == oldNrNeutrals) return 0;
        return 1;
    }
    
    public static void updateCells(Cell[][] grid, HashMap<String, Integer> param) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < param.get("LENGTH"); ++i) 
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                playGame(grid[i][j], param);                
                updateMovements(grid, grid[i][j],param);
            }
    }

    public static void main(String[] args) {

        // The parameters
        final HashMap<String, Integer> param = new HashMap<String, Integer>() {
            {
                put("LENGTH", 20);
                put("WIDTH", 20);
                put("FOV", 1);                
                put("EPOCH", 0);                
                
                put("NRCOPS", 30000);
                put("MEANNEUTRAL", 200);
                put("STDNEUTRAL", 40);
                put("MEANHOSTILES", 100);
                put("STDHOSTILES", 5);
                
                put("TOTALNRNEUTRAL", 0);
                put("TOTALNRHOSTILES", 0);
                put("REMAININGNRNEUTRALS", 0);
                put("REMAININGNRHOSTILES", 0);
                put("REMAININGNRCOPS", 0);
                
                
                // Following parameters are in percentages! 
                // (So actual value is divided by 100)
                put("LEARNINGRATE", 50);
                put("NOISE", 30);
            }
        };

        // Create and initialize the griddy
        Cell[][] grid = new Cell[param.get("LENGTH")][param.get("WIDTH")];
        initGrid(grid, param);
        initAgents(grid, param);
        param.put("REMAININGNRNEUTRALS", param.get("TOTALNRNEUTRAL"));
        param.put("REMAININGNRHOSTILES", param.get("TOTALNRHOSTILES"));
        param.put("REMAININGNRCOPS", param.get("NRCOPS"));

        // Create a nice frame to show the griddy
        JFrame frame = new JFrame();

        // Import/Edit the layout to show the griddy
        final GUIFrame gFrame = new GUIFrame(grid, param);
        
        // Add the epoch button
        JButton btn = new javax.swing.JButton("Epoch");
        final Cell[][] grid2 = grid;
        btn.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                playOneRound(grid2, param, gFrame);
//                updateCells(grid2, param);
//                gFrame.updateGridButtons(grid2, param);
//                gFrame.clickSelectedButton(param);
                
            }
        });
        gFrame.ControlFrame.add(btn);
        
        JButton btn2 = new javax.swing.JButton("10 Epochs");
        btn2.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                  for (int count = 0; count < 10; ++count)
                      if(playOneRound(grid2, param, gFrame) == 0)                           
                          break;
                    
            }
        });
        gFrame.ControlFrame.add(btn2);
                
        JButton btn3 = new javax.swing.JButton("All Epochs");
        btn3.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                  while(true)
                      if(playOneRound(grid2, param, gFrame) == 0)
                              break;
            }
        });
        gFrame.ControlFrame.add(btn3);
        
        // SHOW IT ALL!!!
        gFrame.setVisible(true);    
        gFrame.clickAButton();

    }
}
