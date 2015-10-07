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
    // The available peoples
    
    private int nrHostiles;
    private int nrNeutral;
    private int nrNeutralsSaved;

    // The available agents on this cell
    private List<Agent> agents = new ArrayList<>();

    Cell(HashMap<String, Integer> param) {
        Random rand = new Random();
        this.nrNeutral  = (int) Math.round(rand.nextGaussian() * param.get("STDNEUTRAL") + param.get("MEANNEUTRAL"));
        this.nrHostiles = (int) Math.round(rand.nextGaussian() * param.get("STDHOSTILES") + param.get("MEANHOSTILES"));
        param.put("TOTALNRNEUTRAL", param.get("TOTALNRNEUTRAL")+this.nrNeutral);
        param.put("TOTALNRHOSTILES", param.get("TOTALNRHOSTILES")+this.nrHostiles);
        
        this.nrNeutralsSaved = 0;
   }
    
    public void addAgent(Agent agent) {
        getAgents().add(agent);
    }
    
    public void removeAgent(Agent agent) {
        getAgents().remove(agent);
    }
    
    // Kill the number of agents == lossesCops
    public void killAgents (int kills) {
        setAgents(getAgents().subList(0, getAgents().size() - kills));
    }

    public int getNrHostiles() {
        return nrHostiles;
    }

    public void setNrHostiles(int nrHostiles) {
        this.nrHostiles = nrHostiles;
    }

    public int getNrNeutral() {
        return nrNeutral;
    }

    public void setNrNeutral(int nrNeutral) {
        this.nrNeutral = nrNeutral;
    }

    public int getNrNeutralsSaved() {
        return nrNeutralsSaved;
    }

    public void setNrNeutralsSaved(int saves) {
        this.nrNeutralsSaved = saves;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }    
}
