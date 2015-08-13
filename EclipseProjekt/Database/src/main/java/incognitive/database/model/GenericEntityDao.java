package incognitive.database.model;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import org.jdom2.JDOMException;

/**
 * Interface für Datenzugriffsklassen um gemeinsame Methoden anzubieten
 * 
 * @author Lisa
 *
 * @param <T> Typ der Entity (DataObject...)
 */
public interface GenericEntityDao<T> {

	/**
	 * Speichert eine Entity in der entsprechenden Datei
	 * 
	 * @param entity Entity
	 */
	void save(final T entity) throws JDOMException, IOException, ReflectiveOperationException ;
	
	/**
	 * Liefert die Entity des Typs T zur übergebenen ID zurück
	 * 
	 * @param id zu suchende ID
	 * 
	 * @return Entity
	 */
	T findByID(final BigInteger id);
	
	/**
	 * Löscht eine Entity aus ihrer Datei
	 * 
	 * @param entity zu löschende Entity
	 */
	void delete(final T entity) throws JDOMException, IOException;
	
	/**
	 * Gibt die Map aller Entities des Typs T zu ihren IDs zurück
	 * 
	 * @return Entity Map
	 */
	Map<BigInteger,T> findAll();
}
