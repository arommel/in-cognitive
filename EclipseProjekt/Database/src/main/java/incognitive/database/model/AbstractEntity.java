package incognitive.database.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Interface für alle Entities welches gemeinsame Methoden anbietet
 * 
 * @author Lisa
 */
public interface AbstractEntity extends Serializable {

	/**
	 * Gibt die (einzigartige) ID der Entität zurück
	 * @return ID 
	 */
	public BigInteger getId();
	
	/**
	 * Setzt die ID der Entity
	 * 
	 * @param id ID
	 */
	public void setId(final BigInteger id);
	
	/**
	 * Gibt den Dateinamen zurück in dem die Entität gespeichert ist
	 * 
	 * @return Filename
	 */
	public String getFileName();
	
	
	public void setFileName(final String fileName);
	
	/**
	 * Gibt den Dateinamen zurück in dem die Entität gespeichert ist
	 * 
	 * @return Filename
	 */
	public String getFolderName();

	/**
	 * Gibt den Namen der Entity zurück
	 * 
	 * @return Name 
	 */
	public String getName();
	
}
