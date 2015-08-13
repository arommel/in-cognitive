package incognitive.database.configuration;

import incognitive.database.model.AbstractEntity;
import incognitive.database.model.EntityFolderName;

import java.io.File;
import java.math.BigInteger;

import javafx.stage.DirectoryChooser;

import javax.xml.bind.JAXB;
/**
 * Übernimmt die Veränderung des dataPath in XML Datei und stell JFileChooser zum Ändern bereit
 * @author christoph pfoertner
 * @version 1.0
 * 
 */
public class Settings{
	
	private Config config;
	private String configPath;
	private DirectoryChooser fChooseWindow;
	
	private static Settings INSTANCE;
	
	public Settings(){
	
		this.configPath = new File("").getAbsolutePath() + "/config.xml";
		File f = new File(configPath);
		if(!f.exists()){			
			this.config = new Config();
			openDataPathChangeWindow();
			config.setLastAssignedObjectId(BigInteger.valueOf(-1));
			config.setLastAssignedCategoryId(BigInteger.valueOf(-1));
			config.setLastAssignedRelationId(BigInteger.valueOf(-1));
			JAXB.marshal(config, f);
		}else{
			this.config = JAXB.unmarshal(f, Config.class);
		}
		 
	}
	
	/**
	 * öffnet einen JFileChooser Dialog in dem der dataPath geändert werden kann
	 * dataPath wird danach in Configdatei gespeichert
	 * @return true: wenn neuer Pfad gewählt wurde
	 */
	public boolean openDataPathChangeWindow(){

		String beforeChange = config.getDataUrl();
		
		fChooseWindow = new DirectoryChooser ();
		
		fChooseWindow.setTitle("Pfad für das Speichern der Daten wählen");
		
		if(config.getDataUrl() != null){
			fChooseWindow.setInitialDirectory(new File(config.getDataUrl()));
		}
		
		File file = fChooseWindow.showDialog(null);
	    if(file != null) {
	       this.config.setDataUrl(file.getAbsolutePath());
	      
	       File f = new File(file.getAbsolutePath());
	       f.mkdir();
	       File rel = new File(f.getAbsolutePath() + "/" + EntityFolderName.DataCategory.getFolderName());
	       if(!rel.isDirectory()){     // if(!rel.exists()){
	    	   rel.mkdir();
	       }
	       File obj = new File(f.getAbsolutePath() + "/" + EntityFolderName.DataObject.getFolderName());
	       if(!obj.isDirectory()){
	    	   obj.mkdir();
	       }
	       File cat = new File(f.getAbsolutePath() + "/" + EntityFolderName.DataRelation.getFolderName());
	       if(!cat.isDirectory()){
	    	   cat.mkdir();
	       }	       
	    }
	    JAXB.marshal(config, new File(configPath));
	    if(config.getDataUrl().equals(beforeChange)){
	    	return false;
	    }else{
	    	return true;
	    }
	}
	
	
	/**
	 * gibt den Pfad zu den XML-Dateien zurück, in den Objekte und Kategorieren gespeichert werden
	 * @return Pfad zu den XML-Dateien
	 */

	public String getDataPath(){
		return this.config.getDataUrl();
	}
	
	/**
	 * Erhöht die zuletzt vergebene ID in der Config Datei.
	 * @return gibt die letzte vergebene ID + 1 zurück
	 */
	
	public BigInteger requestNewId(AbstractEntity object){
		BigInteger id = null;
		if(object.getClass().getSimpleName().equals("DataObject")){
			id = config.getLastAssignedObjectId().add(BigInteger.valueOf(1));
			config.setLastAssignedObjectId(id);
		}
		else if(object.getClass().getSimpleName().equals("DataCategory")){
			id = config.getLastAssignedCategoryId().add(BigInteger.valueOf(1));
			config.setLastAssignedCategoryId(id);
		}
		else if(object.getClass().getSimpleName().equals("DataRelation")){
			id = config.getLastAssignedRelationId().add(BigInteger.valueOf(1));
			config.setLastAssignedRelationId(id);
		}
		File f = new File(configPath);
		JAXB.marshal(config, f);
		return id;
	}
	
	public static Settings getInstance(){
		if(INSTANCE == null){
			INSTANCE = new Settings();
		}
		 return INSTANCE;
	}
}