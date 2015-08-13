package incognitive.commonelements.mc.control;

/**
 * 
 * @author christoph pförtner
 * Controller für den Textimport, der den eingegebenen Text parsed und Vorschläge für Objekte und Attribute liefert
 *
 */
public class TextImportController {
	
	private String text; 
	private SimilarityFinder similarityFinder;
	public TextImportController(String text){
		this.text = text;
	}
	
	/**
	 * 
	 * @return String mit gekennzeichneten Substantiven, Verben und Adjektiven
	 */
	public String parse(){
		//TODO: Implement parsing function
		return null;
	}
}
