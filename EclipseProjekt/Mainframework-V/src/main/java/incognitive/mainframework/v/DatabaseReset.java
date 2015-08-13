package incognitive.mainframework.v;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.configuration.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * 
 * Klasse für Datenbankzurücksetzen
 * @author Yuliya Akulova
 *
 **/

public class DatabaseReset {
	
	private static final String SRC_FOLDER = Settings.getInstance().getDataPath();
	
	public static void deleteMain () {
		
	File directory = new File(SRC_FOLDER);
	 
		if(!directory.exists()){                                     //prüfen, ob Ordner existiert
			System.exit(0);
		}else{
			try{
				delete();
				Main.restart();
				
			}catch(Exception e){
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private static void delete() throws IOException{

		String objectPath = SRC_FOLDER + "/" + new DataObject().getFolderName() + "/";
		String categoryPath = SRC_FOLDER + "/" + new DataCategory().getFolderName() + "/";
		String relationPath = SRC_FOLDER + "/" + new DataRelation().getFolderName() + "/";
		
	// xml-Dateien aus Ordner "objects" löschen
		File fObj = new File(objectPath);
		for(File file : fObj.listFiles()){
			try {
				Files.delete(FileSystems.getDefault().getPath(file.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	// xml-Dateien aus Ordner "categories" löschen
		File fCat = new File(categoryPath);
		for(File file : fCat.listFiles()){
			try {
				Files.delete(FileSystems.getDefault().getPath(file.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	// xml-Dateien aus Ordner "relations" löschen
		File fRel = new File(relationPath);
		for(File file : fRel.listFiles()){
			try {
				Files.delete(FileSystems.getDefault().getPath(file.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
