package incognitive.commonelements.v.entitydialog.editdialog;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.alert.InformationAlert;
import incognitive.commonelements.v.component.DataObjectTable;
import incognitive.commonelements.v.component.TabTable;
import incognitive.commonelements.v.component.properties.DataObjectTableProperty;
import incognitive.commonelements.v.component.properties.TagTableProperty;
import incognitive.commonelements.v.entitydialog.DialogComponent;
import incognitive.commonelements.v.entitydialog.DialogHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jdom2.JDOMException;
/**
 * Klasse zum Erstellen der GUI für Erstellen und Bearbeiten einer Kategorie
 * 
 * @author Andreea-Maria Somesan
 *
 */

public class DataCategoryView extends BorderPane implements DialogComponent{
	
	private DataCategory category; 
	
	private CommonElementsController controller;
	private DataCategoryDao categoryDao;
	private DataObjectDao objectDao;
	private DataRelationDao relationDao;
	
	
	private ArrayList<BigInteger> objectsInCategory;
	private ArrayList<BigInteger> objectsInCategoryBeforeEdit;
	Map<BigInteger, DataObject> objectMap;

	//Kategorie
	private Label nameLabel = new Label("Name der Kategorie:");
	private TextField inputTextField = new TextField();

	//Tabelle für Tags
	TabTable tagTable = new TabTable (); 							//Tabelle für Tags erstellen

	// Objekte
	ObservableList<DataObject> objects = FXCollections.observableArrayList();
	
	private Label objectLabel = new Label("Objekt hinzufügen:");
	private ComboBox<DataObject> objectSelect = new ComboBox<>();
	
	private DataObjectTable objectTable = new DataObjectTable(getComponentWidth());
	private Button add = new Button("Hinzufügen");
	


	//Konstruktor für die Erstellung einer Kategorie
	public DataCategoryView() {
		objectsInCategory = new ArrayList<BigInteger>();
		objectsInCategoryBeforeEdit = new ArrayList<BigInteger>();
		this.category = new DataCategory();
	}

	// Konstruktor für die Bearbeitung einer Kategorie
	public DataCategoryView(DataCategory category) {
		this.category = category;
	};
	
	public void init() {
		this.controller = CommonElementsController.getInstance();
		this.categoryDao = DataCategoryDao.getInstance();
		this.objectDao = DataObjectDao.getInstance();
		this.relationDao = DataRelationDao.getInstance();
		
		objectsInCategory = controller.getObjectIDsToCategory(this.category.getId());
		objectsInCategoryBeforeEdit = controller.getObjectIDsToCategory(this.category.getId());
		
		tagTable.initContextMenu();
		initPanel();								

		//ContextMenü für Objekttabelle
		ContextMenu menu = new ContextMenu();
		MenuItem deleteObjectItem = new MenuItem("Ausgewähltes Objekt löschen");
		deleteObjectItem.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(objectTable.getSelectionModel().getSelectedItem() != null && (objectTable.getSelectionModel().getSelectedItem() instanceof DataObjectTableProperty)){
					removeSelectedObjectFromTable((DataObjectTableProperty) objectTable.getSelectionModel().getSelectedItem());
				}
			}
			
		});
		menu.getItems().add(deleteObjectItem);
		
		objectTable.init();
		objectTable.setContextMenu(menu);
		
	}

	private void initPanel() {
		
	    HBox hbox = new HBox();
	    hbox.getChildren().addAll(nameLabel, inputTextField);
	    nameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    inputTextField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    HBox.setHgrow(inputTextField, Priority.ALWAYS);
	    hbox.setSpacing(20);
	    hbox.setPadding(new Insets(0, 0, 10, 0));
	    
	    VBox vbox = new VBox(20);
	    vbox.getChildren().addAll(tagTable);
	    vbox.setPadding(new Insets(0, 0, 10, 0));
	    
	    HBox ohbox = new HBox();
	    fillComboBox();
	    ohbox.getChildren().addAll(objectLabel, objectSelect, add);
	    
	    add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                addSelectObjectToTable();
            }
        });
	    objectLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    objectSelect.setPrefSize(240, 30);
	    add.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    ohbox.setSpacing(20);
	    ohbox.setPadding(new Insets(0, 0, 10, 0));
	    
	    VBox objectBox = new VBox();
	    objectBox.getChildren().addAll(vbox, ohbox, objectTable);
	    objectBox.setSpacing(20);
	    objectBox.setPadding(new Insets(0, 0, 10, 0));

	    this.setTop(hbox);
	    this.setCenter(objectBox);
	    this.setPadding(new Insets(20, 10, 0, 10));
	}
	
	@Override
	public void updateComponent() {
		fillTable();
		this.inputTextField.setText(category.getName());
	}

	@Override
	public void updateModel() {
		//Speicher CategoryName
		category.setCategoryName(inputTextField.getText());
		//Speichere Tags
		category.setTagsSet(tagTable.getTagSet());
	}
	
	@Override
	public void save(){
		try {
	            categoryDao.save(category);
	            
	            for(BigInteger i: objectsInCategory){
	                if(!objectsInCategoryBeforeEdit.contains(i)){
	                    DataRelation temp = new DataRelation();
	                    temp.setIdfromObjectOne(i);
	                    temp.setIdfromObjectTwo(this.category.getId());
	                    temp.setName(DataRelation.CATEGORY_OBJECT_RELATION);
	                    temp.setTypeOfObjectOne(DataObject.class.getSimpleName());
	                    temp.setTypeOfObjectTwo(DataCategory.class.getSimpleName());
	                    relationDao.save(temp);
	                }
	            }

			for(BigInteger i: objectsInCategoryBeforeEdit){
				if(!objectsInCategory.contains(i)){
					controller.deleteObjectFromCategory(objectDao.findByID(i), category);
				}
			}	
			
		} catch (JDOMException | IOException | ReflectiveOperationException e) {
			new ErrorAlert("beim Speichern", "Kategorie konnte nicht in der Datei gespeichert werden!");
		} 
	}

	@Override
	public boolean validateComponent() {
		if(inputTextField.getText().equals("") || inputTextField.getText() == null) {
			new InformationAlert("Es wurde kein Name eingegeben!");
			return false;
			
		}else {
			final HashSet<String> tags = new HashSet<String>();
			final List<String> dblTags = new ArrayList<String>();
			for(TagTableProperty tagProperty : tagTable.getItems()){
				final String tag = tagProperty.getTag();
				if(tags.contains(tag)){
					dblTags.add(tag);
				} else if (!tag.equals("")){
					tags.add(tag);
				}
			}
			if(!dblTags.isEmpty()){
				String dialogContent = "Es wurden doppelte Tags gefunden : \n";
				for(String s : dblTags){
					dialogContent += " -" + s + "\n";
				}
				 new InformationAlert(dialogContent);
				return false;
			}
		}
		return true;
	}

	@Override
	public int getComponentHeight() {
		return 600;
	}

	@Override
	public int getComponentWidth() {
		return 500;
	}
	
	public void fillComboBox() {
		final Map<BigInteger, DataObject> allObjects =	this.objectDao.findAll();
		for(DataObject o : allObjects.values()){
			objects.add(o);
		}
		objectSelect.setItems(objects);
	}

	@Override
	public void onClose() {
		DialogHandler.getInstance().getTempDialogMap().remove(category);
		if(DialogHandler.isCategoryDialogOpen()){
			DialogHandler.setNewCategoryDialog(null);
		}
		
	}
	
	public void addSelectObjectToTable(){
		final DataObject o = (DataObject) objectSelect.getSelectionModel().getSelectedItem();
		BigInteger id = o.getId();
		if (!objectsInCategory.contains(id)) {
			DataObject data = (DataObject) objectSelect.getSelectionModel().getSelectedItem();
			objectsInCategory.add(data.getId());
			objectTable.addDataObject(data);
		} else {
			new InformationAlert("Objekt kann nicht mehrmals ausgewählt werden!");
		}
	}
	
	public void removeSelectedObjectFromTable(DataObjectTableProperty o){
		objectsInCategory.remove(o.getObject().getId());
		objectTable.getItems().remove(o);
	}
	
	private void fillTable() {
		objectTable.fillTable(controller.getObjectsToCategory(category.getId()));

		//Tags
		tagTable.fillTable(category.getTagsSet());
		tagTable.addEmptyTableRow();
	}
}
