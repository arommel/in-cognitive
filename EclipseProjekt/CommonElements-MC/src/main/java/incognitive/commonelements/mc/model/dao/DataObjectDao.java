package incognitive.commonelements.mc.model.dao;

import incognitive.commonelements.mc.model.entity.DataObject;
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

public class DataObjectDao implements GenericEntityDao<DataObject>{
	
	private final DataWorker worker;
	
	private static DataObjectDao INSTANCE;

	private DataObjectDao() {
		this.worker = DataWorker.getInstance();
	}

	@Override
	public void save(final DataObject entity) throws JDOMException, IOException, ReflectiveOperationException {
		worker.getfWorker().updateObject(entity);
		worker.getObjects().put(entity.getId(), entity);
	}


	@Override
	public void delete(final DataObject entity) throws JDOMException, IOException {
		final String entityName = entity.getClass().getSimpleName();
		
		//Objekt löschen
		worker.getfWorker().deleteEntity(entity, null);
		worker.getObjects().remove(entity.getId());
		
		//Relationen in den das Objekt zugeordnet wurde heraussuchen und löschen
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
	public Map<BigInteger, DataObject> findAll() {
		return worker.getObjects().entrySet().stream()
				.collect(Collectors.toMap(
						entry -> entry.getKey() ,
						entry -> (DataObject)entry.getValue()
				));
	}

	@Override
	public DataObject findByID(final BigInteger id) {
		return (DataObject) worker.getObjects().get(id);
	}

	/**
	 * Gibt die einzige Instanz des Daos zurück.
	 * 
	 * @return Instanz
	 */
	public static DataObjectDao getInstance() {
		if(INSTANCE == null){
			INSTANCE = new DataObjectDao();
		}
		return INSTANCE;
	}	

}
