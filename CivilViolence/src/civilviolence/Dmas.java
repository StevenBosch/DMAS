/* TODO:
 2. Is success function really a good succses function that says much?
 3. Run number of epochs and export output to cmv file with multiple options:
 - Run every simulation with new agents
 - Train agents and then run 100 simulations with those agents
 - Let agents train during the 100 simulations and keep filling the agent pool
 */
package civilviolence;

/**
 *
 * @author maleco
 */
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Dmas {

    public enum agentActions {

        SAVE, SHOOT;
    }

    public enum agentMovements {

        NORTH, SOUTH, EAST, WEST;
    }

    public static void initGrid(Cell grid[][], HashMap<String, Integer> param) {
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                grid[i][j] = new Cell(param);
            }
        }
    }

       public static void initAgents(Cell grid[][], HashMap<String, Integer> param, List<Agent> agentList) {
        Random rand = new Random();
        for (int i = 0; i < param.get("NRCOPS"); ++i) {
            if (param.get("KEEPAGENTS") == 0) {
                // Initialize the grid with a new set of agents
                Agent ag = new Agent();
                grid[rand.nextInt(param.get("LENGTH"))][rand.nextInt(param.get("WIDTH"))].addAgent(ag);
                agentList.add(ag);
            } else
                // Initialize the grid with an old set of agents
                grid[rand.nextInt(param.get("LENGTH"))][rand.nextInt(param.get("WIDTH"))].addAgent(agentList.get(i));
            }
    }

    public static void determineAction(Agent ag) {
        Random rand = new Random();
        if (ag.getAwareness() > 0.5 && ag.getDanger() > 0.5) { // Cops and civilians are a majority
            ag.setCurrentSituation(0);
            if (rand.nextDouble() < ag.getDecTable()[0][0]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() > 0.5 && ag.getDanger() <= 0.5) { // Cops are a majority and civilians are a minority
            ag.setCurrentSituation(1);
            if (rand.nextDouble() < ag.getDecTable()[0][1]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() <= 0.5 && ag.getDanger() > 0.5) { // Cops are a minority and civilians are a majority
            ag.setCurrentSituation(2);
            if (rand.nextDouble() < ag.getDecTable()[0][2]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        } else if (ag.getAwareness() <= 0.5 && ag.getDanger() <= 0.5) { // Cops and civilians are a minority
            ag.setCurrentSituation(3);
            if (rand.nextDouble() < ag.getDecTable()[0][3]) { // SAVE save has a higher utility
                ag.setAction(agentActions.SAVE);
            } else {
                ag.setAction(agentActions.SHOOT); // SHOOT has a higher utility
            }
        }
    }

    // Let all the agents in the cell determine their action and update the number of saves, kills and losses accordingly
    public static void playGame(Cell cell, HashMap<String, Integer> param) {
        // Some cell variables 
        int nrHost = cell.getNrHostiles();
        int nrNeutral = cell.getNrNeutral();
        int nrCops = cell.getAgents().size();

        // The number of cops that want to save
        int savingCops = 0;
        // The number of shooting cops
        int shootingCops = 0;

        // The uncertainty of actually hitting someone
        double aim = (double) param.get("AIM") / 100;
        double noise = (double) param.get("NOISE") / 100;
        // The extent to which hostiles aim at cops.

        double hostileAimCops = (double) param.get("HOSTILEAIMCOPS") / 100;
        // The probability that a cop saves
        double saveProb = (double) param.get("SAVEPROB") / 100;

        // For every agent
        for (Agent ag : cell.getAgents()) {
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
            // If the current agent chooses to save a civilian decrease the number of civilians

            if (ag.getAction() == agentActions.SAVE && nrNeutral > 0) {
                savingCops++;
            }

        }
        // The number of shooting cops == all - the saves
        shootingCops = nrCops - savingCops;

        // Set the number of kills in this cell        
        // If the cops would kill more hostiles than available ,the amount of kills is the amount of hostiles
        // Else just set the number of kills by cops
        int nrHostileDeaths = (int) (((shootingCops * aim) > nrHost)
                ? nrHost
                : shootingCops * aim);
        int nrCopDeaths = (int) (((aim * hostileAimCops * nrHost) > nrCops)
                ? nrCops
                : aim * hostileAimCops * nrHost);
        int nrNeutralSaves = (int) ((savingCops * saveProb > nrNeutral)
                ? nrNeutral
                : savingCops * saveProb);

        cell.setNrNeutral(nrNeutral - nrNeutralSaves);
        nrNeutral -= nrNeutralSaves;

        int nrNeutralDeaths = (int) (((aim * (1 - hostileAimCops) * nrHost) > nrNeutral)
                ? nrNeutral
                : (aim * (1 - hostileAimCops) * nrHost));

        // Update the numbers in the cell
        cell.setNrHostiles(nrHost - nrHostileDeaths);
        cell.setNrNeutral(nrNeutral - nrNeutralDeaths);
        cell.killAgents(nrCopDeaths);
        cell.setNrNeutralsSaved(cell.getNrNeutralsSaved() + nrNeutralSaves);

        // Update the parameters
        param.put("REMAININGNRNEUTRALS", param.get("REMAININGNRNEUTRALS") - nrNeutralDeaths - nrNeutralSaves);
        param.put("SAVEDNRNEUTRALS", param.get("SAVEDNRNEUTRALS") + nrNeutralSaves);
        param.put("REMAININGNRHOSTILES", param.get("REMAININGNRHOSTILES") - nrHostileDeaths);
        param.put("REMAININGNRCOPS", param.get("REMAININGNRCOPS") - nrCopDeaths);

        double success = ((nrNeutralSaves + nrHostileDeaths + nrCopDeaths + nrNeutralDeaths) == 0) ? 0.000000
                : ((double) (nrNeutralSaves + nrHostileDeaths - nrCopDeaths - nrNeutralDeaths)
                / (double) (nrNeutralSaves + nrHostileDeaths + nrCopDeaths + nrNeutralDeaths));

        updateAgents(cell, param, success);
    }

    public static void updateAgents(Cell cell, HashMap<String, Integer> param, double success) {
        // Update the decision tables of the agents
        double alpha = (double) param.get("LEARNINGRATE") / 100;
        //System.out.println(success);
        for (Agent ag : cell.getAgents()) {
            if (ag.getAction() == agentActions.SAVE) {
                ag.getDecTable()[0][ag.getCurrentSituation()] = ((((ag.getDecTable()[0][ag.getCurrentSituation()] + success * alpha) / (1 + alpha)) < 1)
                        ? (ag.getDecTable()[0][ag.getCurrentSituation()] + success * alpha) / (1 + alpha)
                        : 1);
            } else {
                ag.getDecTable()[1][ag.getCurrentSituation()] = ((((ag.getDecTable()[1][ag.getCurrentSituation()] + success * alpha) / (1 + alpha)) < 1)
                        ? (ag.getDecTable()[1][ag.getCurrentSituation()] + success * alpha) / (1 + alpha)
                        : 1);
            }
            double temp = ag.getDecTable()[0][ag.getCurrentSituation()];

            // Scale the utilities to between 0 and 1
            ag.getDecTable()[0][ag.getCurrentSituation()] = ag.getDecTable()[0][ag.getCurrentSituation()] / (ag.getDecTable()[0][ag.getCurrentSituation()] + ag.getDecTable()[1][ag.getCurrentSituation()]);
            ag.getDecTable()[1][ag.getCurrentSituation()] = ag.getDecTable()[1][ag.getCurrentSituation()] / (temp + ag.getDecTable()[1][ag.getCurrentSituation()]);
        }
    }

    public static void updateMovements(Cell[][] grid, HashMap<String, Integer> param) {
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                // Only move if hostiles are gone
                if (grid[i][j].getNrNeutral() == 0) {
                    List<Agent> agentlistCopy = new ArrayList<>(grid[i][j].getAgents());
                    for (Agent ag : grid[i][j].getAgents()) {
                        int scoreNorth = (j == 0) ? Integer.MIN_VALUE
                                : grid[i][j - 1].getNrHostiles() + grid[i][j - 1].getNrNeutral() - grid[i][j - 1].getAgents().size();
                        int scoreSouth = (j == param.get("LENGTH") - 1) ? Integer.MIN_VALUE
                                : grid[i][j + 1].getNrHostiles() + grid[i][j + 1].getNrNeutral() - grid[i][j + 1].getAgents().size();
                        int scoreWest = (i == 0) ? Integer.MIN_VALUE
                                : grid[i - 1][j].getNrHostiles() + grid[i - 1][j].getNrNeutral() - grid[i - 1][j].getAgents().size();
                        int scoreEast = (i == param.get("WIDTH") - 1) ? Integer.MIN_VALUE
                                : grid[i + 1][j].getNrHostiles() + grid[i + 1][j].getNrNeutral() - grid[i + 1][j].getAgents().size();

                        int move = 0;
                        if ((Math.max(Math.max(scoreNorth, scoreSouth), Math.max(scoreEast, scoreWest))) == scoreNorth) {
                            move = calcMove(0, param, i, j);
                        } else if ((Math.max(Math.max(scoreNorth, scoreSouth), Math.max(scoreEast, scoreWest))) == scoreSouth) {
                            move = calcMove(1, param, i, j);
                        } else if ((Math.max(Math.max(scoreNorth, scoreSouth), Math.max(scoreEast, scoreWest))) == scoreWest) {
                            move = calcMove(2, param, i, j);
                        } else if ((Math.max(Math.max(scoreNorth, scoreSouth), Math.max(scoreEast, scoreWest))) == scoreEast) {
                            move = calcMove(3, param, i, j);
                        }

                        switch (move) {
                            case 0:
                                grid[i][j - 1].getAgents().add(ag);
                                break;
                            case 1:
                                grid[i][j + 1].getAgents().add(ag);
                                break;
                            case 2:
                                grid[i - 1][j].getAgents().add(ag);
                                break;
                            case 3:
                                grid[i + 1][j].getAgents().add(ag);
                                break;
                        }
                        agentlistCopy.remove(ag);
                    }
                    grid[i][j].setAgents(agentlistCopy);
                }
            }
        }
    }

    public static int calcMove(int desiredMove, HashMap<String, Integer> param, int i, int j) {
        Random rand = new Random();
        if (1 + rand.nextInt(101) > param.get("MOVENOISE")) {
            return desiredMove;
        } else {
            int move;
            // Catch all the forbidden moves
            do {
                move = rand.nextInt(4);
            } while ((move == 0 && j == 0)
                    || (move == 1 && j == (param.get("LENGTH") - 1))
                    || (move == 2 && i == 0)
                    || (move == 3 && i == (param.get("WIDTH") - 1)));
            //System.out.println(i + " " + j + " " + "Move " + move);
            return move;
        }

    }

    public static int playOneRound(Cell[][] grid, HashMap<String, Integer> param, GUIFrame gFrame) {
        updateCells(grid, param);
        gFrame.updateGridButtons(grid, param);

        param.put("EPOCH", param.get("EPOCH") + 1);
        param.put("LEARNINGRATE", param.get("LEARNINGRATE") / 4 * 3); //*0.75
        param.put("LASTSUCCESS1", ((param.get("SAVEDNRNEUTRALS")
                + (param.get("TOTALNRHOSTILES") - param.get("REMAININGNRHOSTILES")) - (param.get("NRCOPS") - param.get("REMAININGNRCOPS"))
                - (param.get("TOTALNRNEUTRAL") - param.get("REMAININGNRNEUTRALS") - param.get("SAVEDNRNEUTRALS")))));
        param.put("LASTSUCCESS2", (param.get("SAVEDNRNEUTRALS")
                + (param.get("TOTALNRHOSTILES") - param.get("REMAININGNRHOSTILES")) + (param.get("NRCOPS") - param.get("REMAININGNRCOPS"))
                + (param.get("TOTALNRNEUTRAL") - param.get("REMAININGNRNEUTRALS"))));

        // Click a button to update the infotext field
        gFrame.clickAButton();

        if (param.get("REMAININGNRNEUTRALS") == 0) {
            return 0;
        }
        return 1;
    }

    public static void updateCells(Cell[][] grid, HashMap<String, Integer> param) {
        // Loop through all the cells, run the simulation in each cell and let all the agents move
        for (int i = 0; i < param.get("LENGTH"); ++i) {
            for (int j = 0; j < param.get("WIDTH"); ++j) {
                playGame(grid[i][j], param);
            }
        }
        updateMovements(grid, param);
    }

    public static void writeOutput(HashMap<String, Integer> param, FileWriter writer, int n) {
        // Append results for this simulation to the output file
        try {
            writer.append(String.format("%d", n));
            writer.append(';');
            writer.append(String.format("%d", param.get("EPOCH")));
            writer.append(';');
            writer.append(String.format("%.3g%n", (double) param.get("LASTSUCCESS1") / (double) param.get("LASTSUCCESS2")));
            //writer.append('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Agent> agentList = new ArrayList<>();
        FileWriter writer = null;
        try {
            writer = new FileWriter("Output");

            // Run the simulation a number of times
            for (int i = 0; i < 5; i++) {
                System.out.println("Epoch: " + (i + 1));
                // The parameters
                final HashMap<String, Integer> param = new HashMap<String, Integer>() {
                    {
                        put("LENGTH", 20);
                        put("WIDTH", 20);
                        put("EPOCH", 0);

                        put("NRCOPS", 3700);
                        put("MEANNEUTRAL", 20);
                        put("STDNEUTRAL", 40);
                        put("MEANHOSTILES", 10);
                        put("STDHOSTILES", 5);

                        put("TOTALNRNEUTRAL", 0);
                        put("TOTALNRHOSTILES", 0);
                        put("REMAININGNRNEUTRALS", 0);
                        put("SAVEDNRNEUTRALS", 0);
                        put("REMAININGNRHOSTILES", 0);
                        put("REMAININGNRCOPS", 0);

                        //These are used to determine the success per epoch, but have to be stored seperately because the result of their division is a double
                        put("LASTSUCCESS1", 0);
                        put("LASTSUCCESS2", 0);

                    // Following parameters are in percentages! 
                        // (So actual value is divided by 100)
                        put("LEARNINGRATE", 80);
                        put("NOISE", 30);
                        put("MOVENOISE", 70);
                        put("AIM", 50);
                        put("HOSTILEAIMCOPS", 50);
                        put("SAVEPROB", 50);
                        put("KEEPAGENTS", 0);                    
                    }
                };

                // Create and initialize the griddy
                Cell[][] grid = new Cell[param.get("LENGTH")][param.get("WIDTH")];
                initGrid(grid, param);
                initAgents(grid, param, agentList);
                 
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
                        for (int count = 0; count < 10; ++count) {
                            if (playOneRound(grid2, param, gFrame) == 0) {
                                break;
                            }
                        }

                    }
                });
                gFrame.ControlFrame.add(btn2);

                JButton btn3 = new javax.swing.JButton("All Epochs");
                btn3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        while (true) {
                            if (playOneRound(grid2, param, gFrame) == 0) {
                                break;
                            }
                        }
                    }
                });
                gFrame.ControlFrame.add(btn3);

            // This while runs the simulation until it's finished and writes the final success to a csv-file
                // Comment this loop and set i in the outer loop to 1 to run a simulation by hand
                while (true) {
                    if (playOneRound(grid2, param, gFrame) == 0) {
                        writeOutput(param, writer, i + 1);
                        break;
                    }
                }
                param.put("KEEPAGENTS", 1);
                // SHOW IT ALL!!! (Uncomment to show the GUI)
//            gFrame.setVisible(true);    
//            gFrame.clickAButton();
            }
        } catch (IOException | HeadlessException e) {
            e.printStackTrace();
        }
        try {
            // Write the output file
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter");
            e.printStackTrace();
        }
    }
}
