package incognitive.commonelements.mc.model.entity;

import incognitive.database.model.AbstractEntity;
import incognitive.database.model.EntityFolderName;

import java.math.BigInteger;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="category")
/**
 * Erzeugt eine neue Kategorie
 * @author	Eric Müller
 *
 */
public class DataCategory implements AbstractEntity {

	@XmlAttribute(name="id")
	private BigInteger id;
	
	@XmlElement(name="name")
	private String name;

	@XmlElementWrapper(name="tags")
	@XmlElement(name="tag")
	private HashSet<String> tags = new HashSet<String>();
	
	private String fileName;
	
	/**
	 * Gibt den Namen der Kategorie zurück. Dies ist nur ein Bezeichner.
	 * Nicht zu verwechseln mit der (einzigartigen) ID.
	 * @return Name der Kategorie
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setCategoryName(String name) {
		this.name = name;
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
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return  name;
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
		return EntityFolderName.DataCategory.getFolderName();
	}
}
