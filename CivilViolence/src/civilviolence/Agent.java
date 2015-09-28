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
    private double successRate;
   
    private agentActions action;
    
    private double[][] decTable;
   
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
     * @return the successRate
     */
    public double getSuccessRate() {
        return successRate;
    }

    /**
     * @param successRate the successRate to set
     */
    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
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
   
}
