package incognitive.commonelements.mc.model.dao;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.DataWorker;
import incognitive.database.model.AbstractEntity;
import incognitive.database.model.GenericEntityDao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jdom2.JDOMException;

public class DataCategoryDao implements GenericEntityDao<DataCategory> {
	
	private final DataWorker worker;	

	private static DataCategoryDao INSTANCE;
	
	private DataCategoryDao() {
		this.worker = DataWorker.getInstance();
	}

	@Override
	public void save(DataCategory entity) throws JDOMException, IOException, ReflectiveOperationException{
		this.worker.getfWorker().updateObject(entity);
		this.worker.getCategories().put(entity.getId(), entity);
	}

	@Override
	public void delete(DataCategory entity) throws JDOMException, IOException {
		final String entityName = entity.getClass().getSimpleName();
		
		//Kategorie löschen
		worker.getfWorker().deleteEntity(entity, null);
		worker.getCategories().remove(entity.getId());
		
		//Relationen in den die Kategorie zugeordnet wurde heraussuchen und löschen
		final List<DataRelation> relations = new ArrayList<DataRelation>();
		for(AbstractEntity relation : worker.getRelations().values()){
			if((((DataRelation)relation).getTypeOfObjectOne().equals(entityName)) && (((DataRelation)relation).getIdfromObjectOne().compareTo(entity.getId()) == 0)
				|| (((DataRelation)relation).getTypeOfObjectTwo().equals(entityName)) && (((DataRelation)relation).getIdfromObjectTwo().compareTo(entity.getId()) == 0)){
				relations.add((DataRelation) relation);
			}
		}
		
		for(DataRelation relation : relations){
			worker.getfWorker().deleteEntity(relation, null);
			worker.getRelations().remove(relation.getId());
		}
		
		
		
	}

	@Override
	public Map<BigInteger, DataCategory> findAll() {
		return worker.getCategories().entrySet().stream()
				.collect(Collectors.toMap(
						entry -> entry.getKey() ,
						entry -> (DataCategory)entry.getValue()
				));
	}

	@Override
	public DataCategory findByID(BigInteger id) {
		return (DataCategory) worker.getCategories().get(id);
	}
	
	/**
	 * Gibt die einzige Instanz des Daos zurück.
	 * 
	 * @return Instanz
	 */
	public static DataCategoryDao getInstance() {
		if(INSTANCE == null){
			INSTANCE = new DataCategoryDao();
		}
		return INSTANCE;
	}	
}
