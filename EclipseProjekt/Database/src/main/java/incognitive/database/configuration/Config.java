package incognitive.database.configuration;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="config")
/**
 * @author christoph
 * Klasse für die Speicherung der Konfigurationseinstellungen
 */
public class Config {
	
	private BigInteger lastAssignedObjectId;
	private BigInteger lastAssignedCategoryId;
	private BigInteger lastAssignedRelationId;
	
	private String dataUrl;
	
	public Config(){
		
	}
	
	@XmlElement(name="lastAssignedObjectId")
	public BigInteger getLastAssignedObjectId() {
		return lastAssignedObjectId;
	}

	public void setLastAssignedObjectId(BigInteger lastRequestedID) {
		this.lastAssignedObjectId = lastRequestedID;
	}
	
	@XmlElement(name="lastAssignedCategoryId")
	public BigInteger getLastAssignedCategoryId() {
		return lastAssignedCategoryId;
	}

	public void setLastAssignedCategoryId(BigInteger lastRequestedID) {
		this.lastAssignedCategoryId = lastRequestedID;
	}
	
	@XmlElement(name="lastAssignedRelationId")
	public BigInteger getLastAssignedRelationId() {
		return lastAssignedRelationId;
	}

	public void setLastAssignedRelationId(BigInteger lastRequestedID) {
		this.lastAssignedRelationId = lastRequestedID;
	}
	
	@XmlElement(name="dataUrl")
	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}	
}
