/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import civilviolence.Dmas.agentActions;

/**
 *
 * @author maleco
 */
public class Agent {
    private int learningRate;
    private double awareness; // The rate agents/(hostiles+agents)
    private double danger; // The rate civilians/(hostiles+civilians)
    private int[] location; // x, y
   
    private agentActions action;
    
    private double[][] decTable; // row 0: action save, row 1: action shoot. Column 0, 1: majority cops; column 0: majority civilians, column 1: minority civilians. Column 2, 3: minority cops; column 2: majority civilians; column 3: minority civilians.
    public Agent() {

        this.decTable = new double[][] {
        {0.5, 0.5, 0.5, 0.5}, // Action save. ++, +-, -+, --
        {0.5, 0.5, 0.5, 0.5} // Action shoot. ++, +-, -+, --
        };
    }
    private int currentSituation;
    
    /**
     * @return the learningRate
     */

    public int getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(int learningRate) {
        this.learningRate = learningRate;
    }

    public double getAwareness() {
        return awareness;
    }

    public void setAwareness(double awareness) {
        this.awareness = awareness;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public agentActions getAction() {
        return action;
    }

    public void setAction(agentActions action) {
        this.action = action;
    }

    public double[][] getDecTable() {
        return decTable;
    }

    public void setDecTable(double[][] decTable) {
        this.decTable = decTable;
    }

    /**
     * @return the danger
     */
    public double getDanger() {
        return danger;
    }

    /**
     * @param danger the danger to set
     */
    public void setDanger(double danger) {
        this.danger = danger;
    }

    /**
     * @return the currentSituation
     */
    public int getCurrentSituation() {
        return currentSituation;
    }

    /**
     * @param currentSituation the currentSituation to set
     */
    public void setCurrentSituation(int currentSituation) {
        this.currentSituation = currentSituation;
    }

}
