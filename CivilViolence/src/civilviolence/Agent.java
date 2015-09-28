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
    private double awareness; // The rate agents/hostiles
    private int[] location; // x, y
   
    private agentActions action;
    
    private double[][] decTable; // row 0: action save, row 1: action shoot. Column 0: majority cops, column 1: minority cops.
    
    public Agent() {
        this.decTable = new double[2][2];
        this.location = new int[2];
    }

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
    
}
