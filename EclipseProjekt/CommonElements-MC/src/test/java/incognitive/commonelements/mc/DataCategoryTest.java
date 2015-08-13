package incognitive.commonelements.mc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.database.FileWorker;
import incognitive.database.model.AbstractEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Map;

import org.jdom2.JDOMException;
import org.junit.Test;

public class DataCategoryTest {

	// Laden einer Kategorie
	
		@Test 
		public void testReadDataCategory() throws SecurityException, IllegalArgumentException, JDOMException, IOException, ReflectiveOperationException  {
			
			final DataCategory entity = new DataCategory();
			
			entity.getClass().getSimpleName();
		
			final FileWorker fileWorker = new FileWorker();
			Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
			dataPathField.setAccessible(true);
			dataPathField.set(fileWorker, "src/test/resources");
			Map<BigInteger, AbstractEntity> s =fileWorker
							.loadFilesForEntity(DataCategory.class.getCanonicalName(), "categories/read");
			
		//Prüfen ob Kategorien vorhanden sind bzw. einen Wert haben.
			DataCategory dataCat1 = (DataCategory) s.get(new BigInteger("3"));
			DataCategory dataCat2 = (DataCategory) s.get(new BigInteger("1"));
			
			assertEquals("Katzen", dataCat1.getName());
			assertEquals("Hunde", dataCat2.getName());
			
			assertNotNull(dataCat1.getName());
			assertNotNull(dataCat2.getName());
			
			
		//Prüfen der Kinder, bzw. der Tags der einzelnen Kategorien.
		    for(String key : dataCat1.getTagsSet()) {
		    	assertEquals("perser", key);
		    	if(key.equals("perser")){
		    			break;
		    	} 
		    }
		    for(String key : dataCat2.getTagsSet()) {
		    	assertEquals("chihuahua", key);
		    	if(key.equals("chihuahua")){
		    		break;
		    	}
		    }
		}
		
		
	//Bearbeiten einer Kategorie
		
		@Test 
		public void testChangeDataCategory() throws JDOMException, IOException, SecurityException, ReflectiveOperationException {
			
			final DataCategory entity = new DataCategory();
			entity.getClass().getSimpleName();
		
			final FileWorker fileWorker = new FileWorker();
			Field dataPathField = fileWorker.getClass().getDeclaredField("dataPath");
			dataPathField.setAccessible(true);
			dataPathField.set(fileWorker, "src/test/resources");
			
			Map<BigInteger, AbstractEntity> mapFirst = fileWorker
							.loadFilesForEntity(DataCategory.class.getCanonicalName(), "categories/change");
			DataCategory dataCat3 = (DataCategory) mapFirst.get(new BigInteger("3"));
			
			DataCategory dataChange = new DataCategory();
			dataChange.setId(dataCat3.getId());
			dataChange.setFileName(dataCat3.getFileName());
			dataChange.setCategoryName("Katzen3");
			
			fileWorker.updateObject(dataChange);
		
			Map<BigInteger, AbstractEntity> mapAfter = fileWorker
							.loadFilesForEntity(DataCategory.class.getCanonicalName(), "categories/change");
			DataCategory dataCat3New = (DataCategory) mapAfter.get(new BigInteger("3"));
			
			assertNotEquals(dataCat3.getName(), dataCat3New.getName());
					
			fileWorker.updateObject(dataCat3);
		}

}
