package incognitive.navigation.mc.control;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.model.RelationTreeModel;

import java.math.BigInteger;
import java.util.Map;

/**
 * Controller für den Relationenbaum mit Zugriff auf das Modell um die richtigen Daten
 * für den Baum zu laden
 * 
 */
public class RelationTreeController {
	
	private final RelationTreeModel model;

	public RelationTreeController() {
		this.model = new RelationTreeModel();
	}
	

	public Map<BigInteger, DataRelation> getRelationList() {
		return this.model.getRelationList();
	}
	
	/**
	 * Liefert die richtige Entity(DataCategory bzw DataObjekt) zu dem in der
	 * Relation angegbenen Daten(Entity als String, und ID der Entity) zurück
	 * 
	 * @param entityTypeOfChild Entity Name als String
	 * @param id ID der Entity
	 * 
	 * @return ermittelte Entity mit richtigem Typ
	 */
	public AbstractEntity getChild(final String entityTypeOfChild, final BigInteger id){
		switch (entityTypeOfChild) {
		case "DataObject":
			return getDataObjectChild(id);
			
		case "DataCategory":
			return getDataCategoryChild(id);
		default:
			return null;
		}
	}
	
	private DataObject getDataObjectChild(final BigInteger id){
		return this.model.getDataObjectChild(id);	
	}
	
	private DataCategory getDataCategoryChild(final BigInteger id){
		return this.model.getDataCategoryChild(id);
	}
}
