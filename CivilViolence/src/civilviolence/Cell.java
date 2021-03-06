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
        this.nrNeutral  = Math.max((int) Math.round(rand.nextGaussian() * param.get("STDNEUTRAL") + param.get("MEANNEUTRAL")),0);
        this.nrHostiles = Math.max((int) Math.round(rand.nextGaussian() * param.get("STDHOSTILES") + param.get("MEANHOSTILES")),0);
        param.put("TOTALNRNEUTRAL", param.get("TOTALNRNEUTRAL")+this.nrNeutral);
        param.put("TOTALNRHOSTILES", param.get("TOTALNRHOSTILES")+this.nrHostiles);
        
        this.nrNeutralsSaved = 0;
   }
    
    public void addAgent(Agent agent) {
        agents.add(agent);
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
