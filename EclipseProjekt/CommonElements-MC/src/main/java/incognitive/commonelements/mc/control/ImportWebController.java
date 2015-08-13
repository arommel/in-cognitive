package incognitive.commonelements.mc.control;

import incognitive.commonelements.mc.model.WikipediaSite;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * Controllerklasse der Importview, die sich um das Verarbeiten der eingebenen Daten kümmert.
 *
 */
public class ImportWebController {
	
	
	
	private static final int MINIMAL_MATCHES = 0;
	private static final int MAXIMAL_MATCHES = 400;
	private double lowestMatch = 0, highestMatch = 0;
	private DataObject dataObject;
	private ArrayList<String> possibleCategories;
	private HashMap<String, ArrayList<String>> categoryTags;
	private HashMap<String, Double> categoryWithMatches;
	private SimilarityFinder similarityFinder;
	private Task similarityTask;
	private Alert alert;
	
	
	public ImportWebController(DataObject dataObject){
		this.categoryWithMatches = new HashMap<String, Double>();
		this.possibleCategories = new ArrayList<String>();
		this.dataObject = dataObject;
		this.categoryTags = new HashMap<String,ArrayList<String>>();
	
	}
	
	/**
	 * Funktion die Unterscheided, welches Datenmodell anzuwenden ist und dessen parse Funktion aufruft
	 * @param html String
	 * @return DataObject 
	 * @throws JDOMException
	 * @throws IOException
	 */
	public DataObject parseHtml(String html) throws JDOMException, IOException{
		Document doc = Jsoup.parse(html);
		if(doc.getElementsByTag("title").text().contains("Wikipedia")){
			
		    WikipediaSite wikiSite = new WikipediaSite(dataObject, doc);
		    
		    if(wikiSite.getObj() != null){
		    	
		    	this.possibleCategories = wikiSite.getPossibleCategories();
		    	this.dataObject = wikiSite.getObj();
		    	ImportWebController.this.similarityFinder = new LevenshteinSimilarityFinder(dataObject);
		    	
		    	return dataObject;
		    }
		}
		
		return null;
		
	}
	/**
	 * Sucht für einen übergebenen String Categoryname nach Tags auf Wikipedia.de
	 * @param categoryName
	 * @return gibt ein DataCategoryObjekt mit Tags zurück
	 * @throws IOException
	 */
	public DataCategory getCategoryWithTags(String categoryName) throws IOException{
		DataCategory dataCategory = new DataCategory();
		WikipediaSite wikiCategory = new WikipediaSite(dataCategory, Jsoup.connect("http://de.wikipedia.org/wiki/"+categoryName).get());
		return wikiCategory.getDataCategory();
	}
	
	/**
	 * Gibt eine Liste von Kategorievorschlägen zurück
	 * @return ArrayList vom Typ String 
	 */
	public ArrayList<String> getPossibleCategories(){
		return possibleCategories;
	}
	/**
	 * Gibt eine HashMap von Kategorien mit einem Übereinstimmungscore für das DataObject zurück
	 * @return HashMap vom Typ String, Double
	 */
	public HashMap<String, Double> getCategoryWithMatches(){
		return this.categoryWithMatches;
	}
	
    /**
     * Setzt die CategoryWithMatches HashMap
     * @param categoryWithMatches
     */
	public void setCategoryWithMatches(HashMap<String, Double> categoryWithMatches) {
		this.categoryWithMatches = categoryWithMatches;
	}
	
	/**
	 * Gibt die Instanz des Similarityfinders zurück
	 * @return Instanz von SimilarityFinder
	 */
	public SimilarityFinder getSimilarityFinder() {
		return similarityFinder;
	}
	/**
	 * Setzt die Instanz des SimilarityFinders
	 * @param similarityFinder
	 */
	public void setSimilarityFinder(SimilarityFinder similarityFinder) {
		this.similarityFinder = similarityFinder;
	}

	
}