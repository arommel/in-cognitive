package incognitive.commonelements.mc.control;

import incognitive.commonelements.mc.model.entity.DataObject;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;
/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * 
 * Interface für das Implementieren verschiedener Ähnlichkeitsalgorithmen
 *
 */
public interface SimilarityFinder {

	/**
	 * Gibt den Task zum Finden von ähnlichen Kategorien zurück
	 * @return Task mit dem Rückgabetyp HashMap<String, Double>
	 */
	public Task<HashMap<String, Double>> getFindSimilaritys();
	
	/**
	 * Setzt den Task für das Finden von ähnlichen Kategorien
	 */
	public void setFindSimilaritys(Task<HashMap<String, Double>> findSimilaritys);
	
	/**
	 * Gibt eine HashMap von Kategorievorschlägen als String mit einem Matchingscore als double zurück
	 */
	public HashMap<String, Double> getCategoryWithMatches();
	
	/**
	 * Setzt die categoryWithMatches HashMap
	 */
	public void setCategoryWithMatches(HashMap<String, Double> categoryWithMatches);
}
