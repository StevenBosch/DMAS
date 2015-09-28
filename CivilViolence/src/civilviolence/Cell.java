/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.util.Random;

/**
 *
 * @author maleco
 */
public class Cell {
    /* The standard variables of each cell */
    // The available peoples
    private int nrBad;
    private int nrNeutral;
    
    // The available agents on this cell
    public Agent[] availableAgents;

    // The general consensus on the cell (0 - 256)
    private int despair;
    
    public Cell() {
        Random rand = new Random();
        this.despair =  rand.nextInt(255);
    }
    
    /**
     * @return the nrBad
     */
    public int getNrBad() {
        return nrBad;
    }

    /**
     * @param nrBad the nrBad to set
     */
    public void setNrBad(int nrBad) {
        this.nrBad = nrBad;
    }

    /**
     * @return the nrNeutral
     */
    public int getNrNeutral() {
        return nrNeutral;
    }

    /**
     * @param nrNeutral the nrNeutral to set
     */
    public void setNrNeutral(int nrNeutral) {
        this.nrNeutral = nrNeutral;
    }

    /**
     * @return the despair
     */
    public int getDespair() {
        return despair;
    }

    /**
     * @param despair the despair to set
     */
    public void setDespair(int despair) {
        this.despair = despair;
    }
    
    
}
