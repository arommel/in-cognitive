package incognitive.navigation.mc.model;

import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;

import java.math.BigInteger;
import java.util.Map;

public class RelationTreeModel {
	
	private final DataRelationDao relationDao;
	private final DataObjectDao objectDao;
	private final DataCategoryDao categoryDao;
	
	public RelationTreeModel(){
		this.relationDao = DataRelationDao.getInstance();
		this.objectDao = DataObjectDao.getInstance();
		this.categoryDao = DataCategoryDao.getInstance();
	}


	public Map<BigInteger, DataRelation> getRelationList() {
		return this.relationDao.findAll();
		 
	}
	
	public DataObject getDataObjectChild(final BigInteger id){
		return this.objectDao.findByID(id);
	}

	public DataCategory getDataCategoryChild(final BigInteger id){
		return this.categoryDao.findByID(id);
	}
}
