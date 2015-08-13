package incognitive.navigation.mc.control;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.navigation.mc.model.CategoryObjectTreeModel;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class CategoryObjectTreeController {

	private final CategoryObjectTreeModel model;

	public CategoryObjectTreeController() {
		model = new CategoryObjectTreeModel();
	}
	
	public Map<BigInteger,DataCategory> getCategoryList(){
		return this.model.getCategoryList();
	}
	
	public DataObject getDataObject(BigInteger id){
		return this.model.getDataObject(id);
	}
	
	public Map<BigInteger, DataObject> getObjectList(){
		return this.model.getObjectList();
	}
	
	/**
	 * Liefert alle Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von IDs
	 */
	public List<BigInteger> getObjectIDsToCategory(final BigInteger id){
		return this.model.getObjectIDsToCategory(id);
	}
	
	/**
	 * Liefert alle Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von Objekten
	 */
	public List<DataObject> getObjectsToCategory(BigInteger id){
		return this.model.getObjectsToCategory(id);
	}

}
