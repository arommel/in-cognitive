package incognitive.commonelements.mc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.FileWorker;
import incognitive.database.model.AbstractEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.JDOMException;
import org.junit.Test;

/**
 * 
 * JUntit Testfällen für die Entity DataObject
 * @author Yuliya Akulova
 *
 **/

public class DataRelationTest {

	// Laden einer Relation
	
		@Test 
		public void testReadDataRelation() throws JDOMException, IOException, SecurityException, ReflectiveOperationException {
			
			final DataRelation RelationEntity = new DataRelation();
			
			RelationEntity.getClass().getSimpleName();
		
			final FileWorker fileWorker = new FileWorker();
			Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
			dataPathField.setAccessible(true);
			dataPathField.set(fileWorker, "src/test/resources");
			Map<BigInteger, AbstractEntity> mapRelation = fileWorker
							.loadFilesForEntity(DataRelation.class.getCanonicalName(), "relations/read");
			
		
			DataRelation dataRel1 = (DataRelation) mapRelation.get(new BigInteger("0"));
			DataRelation dataRel2 = (DataRelation) mapRelation.get(new BigInteger("1"));
			
			assertEquals("testrelation", dataRel1.getName());
			assertEquals("Tiere", dataRel2.getName());
			
			assertNotNull(dataRel1.getAttributes());
			assertNotNull(dataRel2.getAttributes());
			
			HashMap<String, Object> mapFoRel1 = dataRel1.getAttributes();
		    for(Entry<String, Object> key : mapFoRel1.entrySet()) {
		    	assertEquals("bla", key.getKey());
		    	assertEquals("bla", key.getValue());
		    }
		    
		    HashMap<String, Object> mapFoRel2 = dataRel2.getAttributes();
		    for(Entry<String, Object> key : mapFoRel2.entrySet()) {
		    	assertEquals("Pfoten", key.getKey());
		    	assertEquals("4", key.getValue());
		    }
		    
		}

		
// Bearbeiten einer Relation
		
		@Test 
		public void testChangeDataRelation() throws SecurityException, IllegalArgumentException, 
					JDOMException, IOException, ReflectiveOperationException  {
			
			final DataRelation entity = new DataRelation();
			entity.getClass().getSimpleName();
			
			final FileWorker fileWorker = new FileWorker();
			Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
			dataPathField.setAccessible(true);
			dataPathField.set(fileWorker, "src/test/resources");
			
			Map<BigInteger, AbstractEntity> mapRelationFirst = fileWorker
							.loadFilesForEntity(DataRelation.class.getCanonicalName(), "relations/change");
			
			DataRelation dataRelation = (DataRelation) mapRelationFirst.get(new BigInteger("1"));
			
			DataRelation dataSave = new DataRelation();
			dataSave.setIdfromObjectOne(dataRelation.getIdfromObjectOne());
			dataSave.setIdfromObjectTwo(dataRelation.getIdfromObjectTwo());
			dataSave.setTypeOfObjectOne(dataRelation.getTypeOfObjectOne());
			dataSave.setTypeOfObjectTwo(dataRelation.getTypeOfObjectTwo());
			dataSave.setAttributes(dataRelation.getAttributes());
			dataSave.setId(dataRelation.getId());
			dataSave.setFileName(dataRelation.getFileName());
			dataSave.setName("Animals");
			
						
			fileWorker.updateObject(dataSave);
			
			Map<BigInteger, AbstractEntity> mapRelationAfter = fileWorker
							.loadFilesForEntity(DataRelation.class.getCanonicalName(), "relations/change");
			
			DataRelation dataRelationNew = (DataRelation) mapRelationAfter.get(new BigInteger("1"));
						
			assertNotEquals(dataRelation.getName(), dataRelationNew.getName());

			fileWorker.updateObject(dataRelation);
			
			
		}

		

}
