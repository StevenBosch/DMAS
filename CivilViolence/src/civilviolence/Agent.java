/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import civilviolence.Dmas.agentActions;
import java.util.*;

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
    }
    
    /**
     * @return the learningRate
     */
    public int getLearningRate() {
        return learningRate;
    }

    /**
     * @param learningRate the learningRate to set
     */
    public void setLearningRate(int learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * @return the awareness
     */
    public double getAwareness() {
        return awareness;
    }

    /**
     * @param awareness the awareness to set
     */
    public void setAwareness(double awareness) {
        this.awareness = awareness;
    }

    /**
     * @return the action
     */
    public agentActions getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(agentActions action) {
        this.action = action;
    }

    /**
     * @return the decTable
     */
    public double[][] getDecTable() {
        return decTable;
    }

    /**
     * @param decTable the decTable to set
     */
    public void setDecTable(double[][] decTable) {
        this.decTable = decTable;
    }
   
}
