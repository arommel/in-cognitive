package incognitive.commonelements.mc.model;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author christoph pförtner
 * @version 1.0
 *
 * Datenmodellklasse einer Wikipediaseite
 */
public class WikipediaSite {
	
	private DataObject dataObject = null;
	private DataCategory dataCategory = null;
	private HashMap<String, Object> attributes;
	private HashSet<String> tags;
	private Document doc;
	private ArrayList<String> possibleCategories;
	
	
	public WikipediaSite(DataObject dataObject, Document doc){
		this.dataObject = dataObject;
		this.attributes = new HashMap<String, Object>();
		this.tags = new HashSet<String>();
		this.doc = doc;
		this.possibleCategories = new ArrayList<String>();
		parseObject();
	}
	
	public WikipediaSite(DataCategory dataCategory, Document doc){
		this.dataCategory = dataCategory;
		this.attributes = new HashMap<String, Object>();
		this.tags = new HashSet<String>();
		this.doc = doc;
		parseCategory();
	}
	
	/**
	 * Überführt eine Wikipediaseite in eine DataCategory
	 */
	private void parseCategory(){
		dataCategory.setCategoryName(doc.getElementById("firstHeading").text());
		Element content = doc.getElementById("mw-content-text");
		parseTags(content.getElementsByTag("p").first());

		int counter = -1;
		ArrayList attributeValue = new ArrayList<String>();
		String recognizedHeadline = null;
		
		for(Element element : content.getAllElements()){
			if(element.hasClass("mw-headline")){
				if(recognizedHeadline != null){
					if(attributeValue.size() > 0){
						attributes.put(recognizedHeadline, cleanValue(attributeValue));
					}
					recognizedHeadline = element.text();
					attributeValue = new ArrayList<String>();
				}
				recognizedHeadline = element.text();
			}else{
				if(recognizedHeadline != null && element.hasText() && isAllowedTag(element.tag().toString())){
					attributeValue.add(element.text());
					//parseTags(element);
				}
			}
		}
		
		dataCategory.setTagsSet(tags);
	}
	/**
	 * Überführt eine Wikipediaseite in ein DataObject
	 */
	private void parseObject(){
		dataObject.setName(doc.getElementById("firstHeading").text());
		Element content = doc.getElementById("mw-content-text");
		parseTags(content.getElementsByTag("p").first());

		int counter = -1;
		ArrayList attributeValue = new ArrayList<String>();
		String recognizedHeadline = null;
		
		for(Element element : content.getAllElements()){
			if(element.hasClass("mw-headline")){
				if(recognizedHeadline != null){
					if(attributeValue.size() > 0){
						attributes.put(recognizedHeadline, cleanValue(attributeValue));
					}
					recognizedHeadline = element.text();
					attributeValue = new ArrayList<String>();
				}
				recognizedHeadline = element.text();
			}else{
				if(recognizedHeadline != null && element.hasText() && isAllowedTag(element.tag().toString())){
					attributeValue.add(element.text());
				}
			}
		}
		
		parsePossibleCategories();
		
		dataObject.setObjectMap(attributes);
		dataObject.setTagsSet(tags);
	}
	
    private void parsePossibleCategories() {
		Element catDiv = doc.getElementById("mw-normal-catlinks");
		Elements li = catDiv.getElementsByTag("li");
		for(Element el : li){
			possibleCategories.add(el.child(0).text());		
		}
	}
	/**
     *  Liest alle Links aus dem übergebenen Element und schreibt diese in das Tagsset des DataObjects
     *  @param element, ist das Element aus dem die Links gelesen werden sollen
     */
	private void parseTags(Element element){
		if(element.getClass().toString() != "references"){
			for(Element link : element.getElementsByTag("a")){
				if(!link.className().contains("cite_note") && !link.className().contains("internal")){
					this.tags.add(link.text());
				}
			}
		}
	}

	@Override
	public String toString() {
		return "WikipediaSite [dataObject=" + dataObject + ", attributes=" + attributes
				+ ", tags=" + tags + "]";
	}

	public DataObject getObj() {
		return dataObject;
	}

	public void setObj(DataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	

	public DataCategory getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(DataCategory dataCategory) {
		this.dataCategory = dataCategory;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	/**
	 * Überprüft ob der übergebene Tagname ein zulässiges Element ist
	 * @param tagName
	 * @return Gibt bei einem erlaubten element true zurück, ansonsten false
	 */
	public boolean isAllowedTag(String tagName){
		String[] allowedElements = {"p", "ul", "li", "ol"};
		for(String element : allowedElements){
			if(element == tagName){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gibt einen String mit der komplette Value zurück, bei dem [Bearbeiten] links entfernt wurden
	 * @param values
	 * @return bereinigter String
	 */
	public String cleanValue(ArrayList<String> values){
		String buffer = new String();
		for(String value : values){
			buffer += " " + value.replace("[Bearbeiten]", " ");
		}
		return buffer;
	}
	public ArrayList<String> getPossibleCategories() {
		return possibleCategories;
	}
	public void setPossibleCategories(ArrayList<String> possibleCategories) {
		this.possibleCategories = possibleCategories;
	}
	
	
	
}