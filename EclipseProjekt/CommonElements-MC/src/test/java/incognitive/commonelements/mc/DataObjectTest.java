package incognitive.commonelements.mc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import incognitive.commonelements.mc.model.entity.DataObject;
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

public class DataObjectTest {
	
// Laden eines Objektes
	@Test 
	public void testReadDataObject() throws JDOMException, IOException, SecurityException, ReflectiveOperationException {
		
		final DataObject entity = new DataObject();
		
		entity.getClass().getSimpleName();
	
		final FileWorker fileWorker = new FileWorker();
		Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
		dataPathField.setAccessible(true);
		dataPathField.set(fileWorker, "src/test/resources");
		Map<BigInteger, AbstractEntity> s =fileWorker
						.loadFilesForEntity(DataObject.class.getCanonicalName(), "objects/read");
		
	
		DataObject dataObj1 = (DataObject) s.get(new BigInteger("5"));
		DataObject dataObj2 = (DataObject) s.get(new BigInteger("6"));
		
		assertEquals("Handy", dataObj1.getName());
		assertEquals("Studiumobjekt", dataObj2.getName());
		
		assertNotNull(dataObj1.getObjectMap());
		assertNotNull(dataObj2.getObjectMap());
		
		HashMap<String, Object> map = dataObj1.getObjectMap();
	    for(Entry<String, Object> key : map.entrySet()) {
	    	assertEquals("Samsung", key.getKey());
	    	assertEquals("S5", key.getValue());
	    }
	    
	    HashMap<String, Object> map2 = dataObj2.getObjectMap();
	    for(Entry<String, Object> key : map2.entrySet()) {
	    	assertEquals("Studiengang", key.getKey());
	    	assertEquals("Informatik", key.getValue());
	    }
	    
	}

	

// Bearbeiten eines Objektes
	
	@Test 
	public void testChangeDataObject() throws JDOMException, IOException, SecurityException, ReflectiveOperationException {
		
		final DataObject entity = new DataObject();
		entity.getClass().getSimpleName();
	
		final FileWorker fileWorker = new FileWorker();
		Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
		dataPathField.setAccessible(true);
		dataPathField.set(fileWorker, "src/test/resources");
		
		Map<BigInteger, AbstractEntity> mapFirst = fileWorker
						.loadFilesForEntity(DataObject.class.getCanonicalName(), "objects/change");
		DataObject dataObj3 = (DataObject) mapFirst.get(new BigInteger("7"));
		
		DataObject dataChange = new DataObject();
		dataChange.setId(dataObj3.getId());
		dataChange.setFileName(dataObj3.getFileName());
		dataChange.setName("Uni");
		
		fileWorker.updateObject(dataChange);
	
		Map<BigInteger, AbstractEntity> mapAfter = fileWorker
						.loadFilesForEntity(DataObject.class.getCanonicalName(), "objects/change");
		DataObject dataObj3New = (DataObject) mapAfter.get(new BigInteger("7"));
		
		assertNotEquals(dataObj3.getName(), dataObj3New.getName());
				
		fileWorker.updateObject(dataObj3);
	}
}
