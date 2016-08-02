package util;
import java.util.LinkedList;
import java.util.List;
/**
 * Classe che mantiene il risultato della
 * simulazione
 * @author antonio
 *
 */
public class SimulationResult {
	
	private final int totalCell;
	
	private double waste;
	
	private double accuracy;
	
	private List<PathMatcher> results = new LinkedList<>();
	
	public SimulationResult(int totalCell){
		this.totalCell = totalCell;
	}
	
	public List<PathMatcher> getResults() {
		return results;
	}
	
	public void setResults(List<PathMatcher> results) {
		this.results = results;
	}
	
	
	public void addPathMatcher(PathMatcher pm){
		results.add(pm);
	}
	
	/**
	 * Restituisce la percentuale media
	 * di celle sprecate per path
	 * celle sprecate
	 * 
	 * @return
	 */
	public double getWaste(){
		if(waste!=0)
			return waste;
		
		double wastedCell = 0;
		for(PathMatcher p:results){
			wastedCell += p.getWaste().size()/(double)p.getPredicted().size()*100;
					//(p.getPredicted().size()-p.getRealPath().size())/(double)p.getRealPath().size();
		}
		
		this.waste = wastedCell/results.size();
		return waste;
	}
	
	/**
	 * Restituisce la percentuale
	 * medi di celle correttamente predette
	 * 
	 * @return
	 */
	public double getAccuracy(){
		if(accuracy!=0)
			return accuracy;
		
		double accuracyCell = 0;
		for(PathMatcher p:results){
			accuracyCell += (double)p.getRightPredicted().size()/p.getRealPath().size()*100;
		}
		this.accuracy = accuracyCell/results.size();
		
		return accuracy;
	}
	
	
}
