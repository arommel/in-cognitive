package incognitive.commonelements.mc.control;

import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.model.AbstractEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.JDOMException;


public class CommonElementsController {
	
	private final DataObjectDao dataObjectDao;
	private final DataCategoryDao dataCategoryDao;
	private final DataRelationDao dataRelationDao;
	
	private static CommonElementsController INSTANCE;
	
	private CommonElementsController() {
		this.dataObjectDao = DataObjectDao.getInstance();
		this.dataCategoryDao = DataCategoryDao.getInstance();
		this.dataRelationDao = DataRelationDao.getInstance();
	}
	
	/**
	 * Findet die ID zum Namen einer Kategorie
	 * @param categoryName Name zudem ID gesucht wird
	 * 
	 * @return ID der ersten Kategorie oder null, wenn nicht gefunden.
	 */
	public BigInteger findIDByName(String categoryName){
		for(DataCategory e :dataCategoryDao.findAll().values()){
			if(e.getName().equals(categoryName)){
				return e.getId();
			}
		}
		return null;
	}
	
	public void deleteObjectFromCategory(DataObject object, DataCategory category) throws JDOMException, IOException{
		final List<DataRelation> relations = new ArrayList<DataRelation>();
		for(DataRelation relation : dataRelationDao.findAll().values()){
			if(relation.getName().equals(DataRelation.CATEGORY_OBJECT_RELATION)){
				if(relation.getIdfromObjectOne().equals(object.getId()) && relation.getIdfromObjectTwo().equals(category.getId())){
					relations.add(relation);
				}
			}
		}
		
		for(DataRelation relation : relations){
			dataRelationDao.delete(relation);
		}
	}
	
	/**
	 * Liefert das Entity Objekt zu den Angaben (Typ und ID) in der Relation zurück
	 * 
	 * @param typ Typ der Entity
	 * @param id ID der Entity
	 * 
	 * @return Entity
	 */
	public AbstractEntity getCorrectedEntity(String typ, BigInteger id){
		final AbstractEntity entity;
		
		if(typ.equals("DataCategory")){
			entity = dataCategoryDao.findByID(id);
		}else{
			entity = dataObjectDao.findByID(id);
		}
		
		return entity;
	}

	/**
	 * Liefert alle IDs der Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von IDs
	 */
	public ArrayList<BigInteger> getObjectIDsToCategory(BigInteger id){
		ArrayList<BigInteger> objectList = new ArrayList<BigInteger>();
		Map<BigInteger, DataRelation> relations = dataRelationDao.findAll();
		for(DataRelation rel : relations.values()){
			//Ktageorie bei Kategorie-Objekt Beziehung immer als zweites speichern
			if(rel.getTypeOfObjectTwo().equals("DataCategory") && rel.getIdfromObjectTwo().equals(id)){
				if(rel.getTypeOfObjectOne().equals("DataObject") && rel.getName().equals(DataRelation.CATEGORY_OBJECT_RELATION)){
					objectList.add(rel.getIdfromObjectOne());
				}
			}
		}
		return objectList;
	}
	
	/**
	 * Liefert alle Objekte zur jeweiligen Kategorie ID zurück
	 * 
	 * @param id Kategorie ID
	 * 
	 * @return Liste von Objekten
	 */
	public List<DataObject> getObjectsToCategory(BigInteger id){
		final List<DataObject> objectList = new ArrayList<DataObject>();
		final List<BigInteger> objectsID = getObjectIDsToCategory(id);
		final Map<BigInteger, DataObject>dataObjectMap = dataObjectDao.findAll();
		for(Entry<BigInteger, DataObject> entry:dataObjectMap.entrySet()){
			if(objectsID.contains(entry.getKey())){
				objectList.add(entry.getValue());
			}
		}
		return objectList;
	}
	
	/**
	 * Gibt die einzige Instanz des Controllers zurück.
	 * 
	 * @return Instanz
	 */
	public static CommonElementsController getInstance() {
		if(INSTANCE == null){
			INSTANCE = new CommonElementsController();
		}
		return INSTANCE;
	}	
	
	/**
	 * Gibt die Kategorien zurück in welchen sich ein Objekt befindet.
	 * 
	 * @param id Id des Objektes
	 * 
	 * @return Liste von Kategorien
	 */
	public  List<DataCategory> getCategoriesFromObject(BigInteger id){
		final List<DataCategory> categoryList = new ArrayList<DataCategory>();
		for(DataRelation relation : dataRelationDao.findAll().values()){
			if(relation.getName().equals(DataRelation.CATEGORY_OBJECT_RELATION)){
				if(relation.getIdfromObjectOne().equals(id)){
					categoryList.add(dataCategoryDao.findByID(relation.getIdfromObjectTwo()));
				}
			}
		}
		return categoryList;
	}
}
