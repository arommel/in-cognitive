package incognitive.commonelements.mc.model.entity;

import incognitive.database.model.AbstractEntity;
import incognitive.database.model.EntityFolderName;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="object")
/**
 * Erzeugt ein neues Objekt
 * @author Eric Müller
 *
 */
public class DataObject implements AbstractEntity {

	@XmlAttribute(name="id")
	private BigInteger id;
	
	@XmlElementWrapper(name="kategorievorschlaege")
	@XmlElement(name="vorschlag")
	private ArrayList<String> categoryProposals = new ArrayList<String>();
	
	private String fileName;
	
	/**
	 * Gibt die (einzigartige) ID des Objektes zurück.
	 * @return Die ID des Objektes.
	 */
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	@XmlElement(name="name")
	private String name = "";

	/**
	 * Gibt den Namen des Objektes zurück.
	 * Nicht zu verwechseln mit der (einzigartigen) ID.
	 * @return Name des Objektes
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name="attribut")
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	@XmlElementWrapper(name="tags")
	@XmlElement(name="tag")
	private HashSet<String> tags = new HashSet<String>();

	/**
	 * Gibt die Attribute des Objektes zurück.
	 * @return HashMap<name des Attributes, Attribut-Wert>
	 */
	public HashMap<String, Object> getObjectMap() {
		return attributes;
	}

	public void setObjectMap(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Gibt die zugehörigen Tags der Kategorie als HashSet<String> zurück
	 * @return Tag-HashMap
	 */
	public HashSet<String> getTagsSet() {
		return tags;
	}

	public void setTagsSet(HashSet<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}
	
	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getFolderName() {
		return EntityFolderName.DataObject.getFolderName();
	}

	/**
	 * Gibt die Liste mit Kategorievorschlägen für das Objekt zurück.
	 * @return Liste mit Vorschlägen
	 */
	public ArrayList<String> getCategoryProposals() {
		return categoryProposals;
	}

	public void setCategoryProp(ArrayList<String> categoryProposals) {
		this.categoryProposals = categoryProposals;
	}
}