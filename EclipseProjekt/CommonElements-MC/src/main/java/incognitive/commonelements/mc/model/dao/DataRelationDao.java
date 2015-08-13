package incognitive.commonelements.mc.model.dao;

import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.DataWorker;
import incognitive.database.model.GenericEntityDao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

import org.jdom2.JDOMException;

public class DataRelationDao implements GenericEntityDao<DataRelation>{

	private final DataWorker worker;
	
	private static DataRelationDao INSTANCE;
	
	private DataRelationDao() {
		this.worker = DataWorker.getInstance();
	}
	
	@Override
	public void save(DataRelation entity) throws JDOMException, IOException, ReflectiveOperationException {
		worker.getfWorker().updateObject(entity);
		worker.getRelations().put(entity.getId(), entity);
	}

	@Override
	public Map<BigInteger, DataRelation> findAll() {
		return worker.getRelations().entrySet().stream()
				.collect(Collectors.toMap(
						entry -> entry.getKey() ,
						entry -> (DataRelation)entry.getValue()
				));
	}

	@Override
	public DataRelation findByID(final BigInteger id) {
		return (DataRelation) worker.getRelations().get(id);
	}

	@Override
	public void delete(DataRelation entity) throws JDOMException, IOException {
		worker.getfWorker().deleteEntity(entity, null);
		worker.getRelations().remove(entity);
	}
	
	/**
	 * Gibt die einzige Instanz des Daos zurück.
	 * 
	 * @return Instanz
	 */
	public static DataRelationDao getInstance() {
		if(INSTANCE == null){
			INSTANCE = new DataRelationDao();
		}
		return INSTANCE;
	}	
}
