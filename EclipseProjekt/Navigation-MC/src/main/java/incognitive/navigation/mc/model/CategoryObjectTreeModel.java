package incognitive.navigation.mc.model;
import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class CategoryObjectTreeModel {
	
	private final DataCategoryDao categoryDao;
	private final DataObjectDao objectDao;
	
	private final CommonElementsController controller;
	
	public CategoryObjectTreeModel(){
		this.categoryDao = DataCategoryDao.getInstance();
		this.objectDao = DataObjectDao.getInstance();
		
		this.controller = CommonElementsController.getInstance();
	}
	
	public DataObject getDataObject(final BigInteger id){
		return this.objectDao.findByID(id);
	}
	
	public Map<BigInteger,DataCategory> getCategoryList(){
		return this.categoryDao.findAll();
	}
	
	/**
	 * Gibt alle Objekte zurück
	 * 
	 * @return Objekte
	 */
	public Map<BigInteger, DataObject> getObjectList(){
		return this.objectDao.findAll();
	}
	
	/**
	 * Liefert alle Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von IDs
	 */
	public List<BigInteger> getObjectIDsToCategory(BigInteger id){
		return this.controller.getObjectIDsToCategory(id);
	}
	
	/**
	 * Liefert alle Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von Objekten
	 */
	public List<DataObject> getObjectsToCategory(BigInteger id){
		return this.controller.getObjectsToCategory(id);
	}
	
}
