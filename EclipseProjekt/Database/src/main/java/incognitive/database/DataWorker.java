package incognitive.database;

import incognitive.database.model.AbstractEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import org.jdom2.JDOMException;

/**
 * Klasse die Daten während der Laufzeit des Programmes behält. Methoden für
 * Laden, Speichern und Löschen Objekte/Kategorien
 * 
 * @author christoph pfoertner
 * @version 1.0
 * 
 */
public class DataWorker {

	private FileWorker fWorker;
	private HashMap<BigInteger, AbstractEntity> objects;
	private HashMap<BigInteger, AbstractEntity> categories;
	private HashMap<BigInteger, AbstractEntity> relations;

	public static DataWorker INSTANCE;
	
	private DataWorker() {
		this.fWorker = new FileWorker();
		this.objects = new HashMap<BigInteger, AbstractEntity>();
		this.categories = new HashMap<BigInteger, AbstractEntity>();
		this.relations = new HashMap<BigInteger, AbstractEntity>();
		
		try {
			loadData();
		} catch (ReflectiveOperationException | JDOMException | IOException e) {
			final Alert alert = new Alert(AlertType.ERROR);
			
			alert.setTitle("Fehler beim Laden der Daten");
			alert.setHeaderText("Fehler");
			alert.setContentText("Daten konnten nicht aus den XML geladen werden !");
			alert.showAndWait();
		}
	}

	/**
	 * lädt die Kategorien und Objekte in die jeweiligen Attribute der Klasse
	 * 
	 */
	private void loadData() throws ClassNotFoundException, ReflectiveOperationException, JDOMException, IOException {
		this.objects.putAll(fWorker.loadFilesForEntity("incognitive.commonelements.mc.model.entity.DataObject", "objects"));
		this.categories.putAll(fWorker.loadFilesForEntity("incognitive.commonelements.mc.model.entity.DataCategory", "categories"));
		this.relations.putAll(fWorker.loadFilesForEntity("incognitive.commonelements.mc.model.entity.DataRelation", "relations"));
	}

	public FileWorker getfWorker() {
		return fWorker;
	}

	public void setfWorker(FileWorker fWorker) {
		this.fWorker = fWorker;
	}

	public static DataWorker getInstance() {
		if(INSTANCE == null){
			INSTANCE = new DataWorker();
		}
		return INSTANCE;
	}

	public HashMap<BigInteger, AbstractEntity> getObjects() {
		return objects;
	}

	public HashMap<BigInteger, AbstractEntity> getCategories() {
		return categories;
	}
	
	public HashMap<BigInteger, AbstractEntity> getRelations() {
		return relations;
	}
}
