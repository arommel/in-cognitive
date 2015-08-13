package incognitive.commonelements.mc.model.entity;

import incognitive.database.model.AbstractEntity;
import incognitive.database.model.EntityFolderName;

import java.math.BigInteger;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="relation")
public class DataRelation implements AbstractEntity {

	@XmlAttribute(name="id")
	private BigInteger id;

	@XmlElement(name="name")
	private String name = "";
	
	private String fileName;
	
	@XmlElement(name="id_ObjectOne")
	private BigInteger idfromObjectOne;
	@XmlElement(name="type_ObjectOne")
	private String typeOfObjectOne;

	@XmlElement(name="type_ObjectTwo")
	private String typeOfObjectTwo;
	@XmlElement(name="id_ObjectTwo")
	private BigInteger idfromObjectTwo;
	
	@XmlElementWrapper(name="attribut")
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	public static final String CATEGORY_OBJECT_RELATION = "category_object";
	
	public String getTypeOfObjectOne() {
		return typeOfObjectOne;
	}

	public void setTypeOfObjectOne(String typeOfObjectOne) {
		this.typeOfObjectOne = typeOfObjectOne;
	}

	public String getTypeOfObjectTwo() {
		return typeOfObjectTwo;
	}

	public void setTypeOfObjectTwo(String typeOfObjectTwo) {
		this.typeOfObjectTwo = typeOfObjectTwo;
	}

	public BigInteger getIdfromObjectOne() {
		return idfromObjectOne;
	}

	public void setIdfromObjectOne(BigInteger idfromObjectOne) {
		this.idfromObjectOne = idfromObjectOne;
	}

	public BigInteger getIdfromObjectTwo() {
		return idfromObjectTwo;
	}

	public void setIdfromObjectTwo(BigInteger idfromObjectTwo) {
		this.idfromObjectTwo = idfromObjectTwo;
	}
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
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
		return EntityFolderName.DataRelation.getFolderName();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
