package incognitive.commonelements.mc.control;

import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import info.debatty.java.stringsimilarity.Levenshtein;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * Klasse für das finden von Kategorievorschlägen für ein DataObject
 */
public class LevenshteinSimilarityFinder implements SimilarityFinder{
	
	private final double LOWEST_MATCHING_SCORE = 0.5;
	private DataObject dataObject;
	private Levenshtein levenshtein = new Levenshtein();
	private HashMap<String, Double> categoryWithMatches;
	private CommonElementsController commonElementsController;
	private DataCategoryDao categoryDao;
	private DataObjectDao objectDao;
	private DataRelationDao relationDao;
	private ImportWebController importWebController;
	private Alert alert;
	private Task<HashMap<String, Double>> findSimilaritys;
	
	public LevenshteinSimilarityFinder(DataObject dataObject) {
		this.commonElementsController = CommonElementsController.getInstance();
		this.objectDao = DataObjectDao.getInstance();
		this.categoryDao = DataCategoryDao.getInstance();
		this.relationDao = DataRelationDao.getInstance();
		
		this.categoryWithMatches = new HashMap<String, Double>();
		this.dataObject = dataObject;
		this.importWebController = importWebController;
		findSimilaritys = new Task<HashMap<String, Double>>(){

			@Override
			protected HashMap<String, Double> call() throws Exception {
				// TODO Auto-generated method stub
				findSimilarCategories();
				findSimilarObjects();
				
				return LevenshteinSimilarityFinder.this.categoryWithMatches;
			}

		};
	}
	
	
    /**
     * Vergleicht das DataObject, das in der Klasse gespeichert ist und ein übergebenes Dataobject miteinander und gibt einen Ähnlichkeitsscore zurück
     * @param tmpObject
     * @return Ähnlichkeitsscore vom Typ double
     */
	public double compareObjects(DataObject tmpObject){
		double matches = 0;
		for(String attributeKey : tmpObject.getObjectMap().keySet()){
			for(String attributeImportObjectKey : dataObject.getObjectMap().keySet()){
				if(attributeKey.toLowerCase().equals(attributeImportObjectKey.toLowerCase())){
					matches++;
				}
				if(this.dataObject.getObjectMap().get(attributeImportObjectKey).toString().toLowerCase().contains(attributeKey)){
					matches++;
				}
				if(tmpObject.getObjectMap().get(attributeKey).toString().toLowerCase().contains(attributeImportObjectKey.toLowerCase())){
					matches++;
				}
				matches += levenshtein.similarity(tmpObject.getObjectMap().get(attributeKey).toString(), this.dataObject.getObjectMap().get(attributeImportObjectKey).toString()) / tmpObject.getObjectMap().get(attributeKey).toString().length();
			}
		}
		matches = matches / tmpObject.getObjectMap().size(); // Matches = Anzahl matches relativ zu keys
		
		return matches*10; // mal 10 um Slider Abständen zu entsprechen
	}
	 /**
     * Vergleicht das DataObject, das in der Klasse gespeichert ist und eine übergebene DataCategory miteinander und gibt einen Ähnlichkeitsscore zurück
     * @param category
     * @return Ähnlichkeitsscore vom Typ double
     */
	private double compareCategory(DataCategory category){
		double matches = 0;
		for(String tag : category.getTagsSet()){
			for(String key: dataObject.getObjectMap().keySet()){

				if(dataObject.getObjectMap().get(key).toString().toLowerCase().contains(tag.toLowerCase())){
					matches++;
				}
				matches += levenshtein.similarity(tag, dataObject.getName());
				matches += levenshtein.similarity(key, tag);
			}
			for(String key : dataObject.getTagsSet()){
				matches += levenshtein.similarity(key, tag);
			}
			
		}
		matches  = matches / category.getTagsSet().size(); // Matches relativ zur Anzahl verglichener Tags
		
		return matches;
	}
	/**
	 * Sucht ähnliche Objekte und fügt Kategorien der categoryWithMatches HashMap hinzu
	 * 
	 */
	private void findSimilarObjects(){
		HashMap<BigInteger, DataObject> dataObjects = (HashMap<BigInteger, DataObject>) objectDao.findAll();
		for(DataCategory category : categoryDao.findAll().values()){
			if(categoryWithMatches.get(category.getName()) != null){
				ArrayList<BigInteger> dataObjectIds = commonElementsController.getObjectIDsToCategory(category.getId());
				for(BigInteger id : dataObjectIds){
					double matches = compareObjects(dataObjects.get(id));
					categoryWithMatches.put(category.getName(),categoryWithMatches.get(category.getName()) + matches);
	//				categoryWithMatches.put(category.getName(),  matches);
					
				}
	//			categoryWithMatches.put(category.getName(),categoryWithMatches.get(category.getName()) / dataObjectIds.size());
			}
		}
		
	}
	
	/**
	 * Such ähnliche Kategorien und fügt diese zur categoryWithMatches HashMap hinzu
	 */
	public void findSimilarCategories(){
		
		for(DataCategory category : categoryDao.findAll().values()){
			double matches;
			if((matches = compareCategory(category)) > LOWEST_MATCHING_SCORE){
//				categoryWithMatches.put(category.getName(), categoryWithMatches.get(category.getName()) + matches);
				categoryWithMatches.put(category.getName(), matches);
			}
		}
		
	}
	/**
	 * Gibt eine HashMap von Kategorievorschlägen als String mit einem Matchingscore als double zurück
	 */
	public HashMap<String, Double> getCategoryWithMatches() {
		return categoryWithMatches;
	}
	/**
	 * Setzt die categoryWithMatches HashMap
	 */
	public void setCategoryWithMatches(HashMap<String, Double> categoryWithMatches) {
		this.categoryWithMatches = categoryWithMatches;
	}



	/**
	 * Gibt den Task zum Finden von ähnlichen Kategorien zurück
	 * @return Task mit dem Rückgabetyp HashMap<String, Double>
	 */
	public Task<HashMap<String, Double>> getFindSimilaritys() {
		return findSimilaritys;
	}


	/**
	 * Setzt den Task für das Finden von ähnlichen Kategorien
	 */
	public void setFindSimilaritys(Task<HashMap<String, Double>> findSimilaritys) {
		this.findSimilaritys = findSimilaritys;
	}
	
	
}
