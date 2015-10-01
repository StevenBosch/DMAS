/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civilviolence;

import java.util.*;

/**
 *
 * @author maleco
 */
public class Cell {
    /* The standard variables of each cell */

    // The available peoples

    private int nrHostiles;
    private int nrNeutral;
    private int nrGood;

    private int saves;
    private int kills;
    private int lossesCops;
    private int lossesNeutral;

    // The available agents on this cell
    public List<Agent> agents = new ArrayList<>();

    // The general consensus on the cell (0 - 256)
    private int despair;

    Cell(HashMap<String, Integer> param) {
        Random rand = new Random();
        this.despair = rand.nextInt(255);
        this.nrNeutral = (int) Math.round(rand.nextGaussian() * param.get("STDNEUTRAL") + param.get("MEANNEUTRAL"));
        this.nrHostiles = (int) Math.round(rand.nextGaussian() * param.get("STDHOSTILES") + param.get("MEANHOSTILES"));
        param.put("TOTALNRNEUTRAL", param.get("TOTALNRNEUTRAL")+this.nrNeutral);
        param.put("TOTALNRHOSTILES", param.get("TOTALNRHOSTILES")+this.nrHostiles);
        this.nrGood = (int) Math.round(rand.nextGaussian() * param.get("STDHOSTILES") + param.get("MEANHOSTILES"));
   }
    
    public double getSuccess() {
        return 1;//((this.saves + this.kills - this.lossesCops - this.lossesNeutral) / (this.saves + this.kills + this.lossesCops + this.lossesNeutral));
    }

    /**
     * @return the nrHostiles
     */
    public int getNrHostiles() {
        return nrHostiles;
    }

    /**
     * @param nrHostiles the nrHostiles to set
     */
    public void setNrHostiles(int nrHostiles) {
        this.nrHostiles = nrHostiles;
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

    /**
     * @return the saves
     */
    public int getSaves() {
        return saves;
    }

    /**
     * @param saves the saves to set
     */
    public void setSaves(int saves) {
        this.saves = saves;
    }

    /**
     * @return the kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @param kills the kills to set
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * @return the lossesCops
     */
    public int getLossesCops() {
        return lossesCops;
    }

    /**
     * @param losses the lossesCops to set
     */
    public void setLossesCops(int lossesCops) {
        this.lossesCops = lossesCops;
    }

    /**
     * @return the nrGood
     */
    public int getNrGood() {
        return nrGood;
    }

    /**
     * @param nrGood the nrGood to set
     */
    public void setNrGood(int nrGood) {
        this.nrGood = nrGood;
    }

    /**
     * @return the lossesNeutral
     */
    public int getLossesNeutral() {
        return lossesNeutral;
    }

    /**
     * @param lossesNeutral the lossesNeutral to set
     */
    public void setLossesNeutral(int lossesNeutral) {
        this.lossesNeutral = lossesNeutral;
    }

}
