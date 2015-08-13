package incognitive.commonelements.v.entitydialog.editdialog;

import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.alert.InformationAlert;
import incognitive.commonelements.v.component.DataAttributeTable;
import incognitive.commonelements.v.component.properties.DataAttributeTableProperties;
import incognitive.commonelements.v.entitydialog.DialogComponent;
import incognitive.commonelements.v.entitydialog.DialogHandler;
import incognitive.database.DataWorker;
import incognitive.database.model.AbstractEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jdom2.JDOMException;


/**
 * Klasse zum Erstellen der GUI für neuanlegen und bearbeiten einer Relation
 * @author E. Müller
 *
 */
public class DataRelationView extends BorderPane implements DialogComponent{
	
	public static final int VIEW_HEIGHT = 600;
	public static final int VIEW_WIDTH = 500;
		
	//Name der Relation
	private Label relationNameLabel = new Label("Name der Relation:");
	private Label relationbetweenLabel = new Label ("Relation erstellen zwischen");
	private TextField relationNameTextfield = new TextField();
	
	//Auswahl der Relation-Art
	private ToggleGroup relationChoiceLeft = new ToggleGroup ();
	private ToggleGroup relationChoiceRight = new ToggleGroup ();
	private RadioButton relationChoiceLeftObj = new RadioButton("Objekten");
	private RadioButton relationChoiceLeftCat = new RadioButton("Kategorien");
	private RadioButton relationChoiceRightObj = new RadioButton("Objekten");
	private RadioButton relationChoiceRightCat = new RadioButton("Kategorien");
	
	//Auswahl der Relations-Partner
	private ListView<Object> leftList = new ListView<Object>();
	private ListView<Object> rightList = new ListView<Object>();

	
	//Attributtabelle 
	private DataAttributeTable table = new DataAttributeTable(getComponentWidth());
	
	private DataRelation tempRelation;
	
	private DataRelationDao relationDao;
	
	//Konstruktor für Bearbeiten einer Relation
	public DataRelationView(DataRelation relationEdit){
		tempRelation =  relationEdit;
		this.relationDao = DataRelationDao.getInstance();
		
		initDataRelationEdit();
	}

	/**
	 * Initialisieren der GUI: Fenstlegen der Layouts, Hinzufügen der Panels, Listener für Buttons
	 */
	public void init() {
		initPanel();
		this.table.init();
		this.table.initContextMenu();
	}
	
	public void initPanel(){
		
		relationChoiceLeftObj.setPadding(new Insets(10, 10, 10, 0));
		relationChoiceRightObj.setPadding(new Insets(10, 10, 10, 0));
		relationChoiceLeftCat.setPadding(new Insets(10, 0, 0, 0));
		relationChoiceRightCat.setPadding(new Insets(10, 0, 0, 0));
		
		relationChoiceLeftCat.setToggleGroup(relationChoiceLeft);
		relationChoiceLeftObj.setToggleGroup(relationChoiceLeft);
		relationChoiceRightCat.setToggleGroup(relationChoiceRight);
		relationChoiceRightObj.setToggleGroup(relationChoiceRight);
		
		leftList.setMinHeight(200);
		rightList.setMinHeight(200);
	    	    		
		HBox relationNameBox = new HBox();
		
	    relationNameBox.getChildren().addAll(relationNameLabel, relationNameTextfield);
	    relationNameTextfield.setPrefSize(360, 30);
	    relationNameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    relationNameBox.setSpacing(20);
	    relationNameBox.setPadding(new Insets(0, 0, 10, 0));
	    setTop(relationNameBox);
	    
	    VBox relationChoiceLeftBox = new VBox();
	    relationChoiceLeftBox.getChildren().addAll(relationChoiceLeftCat, relationChoiceLeftObj);
	    relationChoiceLeftBox.setPadding(new Insets(0, 0, 0, 75));
	    
	    VBox relationChoiceRightBox = new VBox();
	    relationChoiceRightBox.getChildren().addAll(relationChoiceRightCat,relationChoiceRightObj);
	    relationChoiceRightBox.setPadding(new Insets(0, 0, 0, 175));
	    
	    HBox relationChoiceRadioBox = new HBox();
	    relationChoiceRadioBox.getChildren().addAll(relationChoiceLeftBox, relationChoiceRightBox);  
	    
	    VBox relationChoiceBox = new VBox();
	    relationChoiceBox.getChildren().addAll(relationbetweenLabel, relationChoiceRadioBox);
	    relationChoiceBox.setAlignment(Pos.CENTER);
	    	    
	    HBox listBox = new HBox();
	    listBox.getChildren().addAll(leftList, rightList);
	    listBox.setSpacing(5);
	    
	    VBox centerBox = new VBox();
	    centerBox.getChildren().addAll(relationChoiceBox, listBox, table);    
	    centerBox.setSpacing(5);
	    
	    setCenter(centerBox);
	    setPadding(new Insets(5,5,5,5));
	    
//		EventHandler für Radiobuttons		
		relationChoiceLeftObj.setOnAction(new EventHandler<ActionEvent>(){
			
			final ObservableList<Object> lefto = FXCollections.observableArrayList(DataWorker.INSTANCE.getObjects().values().toArray());
			public void handle(ActionEvent event) {
				leftList.setVisible(true);
				leftList.setItems(lefto);
			}
			
		});
		
		relationChoiceLeftCat.setOnAction(new EventHandler<ActionEvent>(){
			final ObservableList<Object> leftc = FXCollections.observableArrayList(DataWorker.INSTANCE.getCategories().values().toArray());

			@Override
			public void handle(ActionEvent event) {
				leftList.setVisible(true);
				leftList.setItems(leftc);
			}
			
		});
		
		relationChoiceRightObj.setOnAction(new EventHandler<ActionEvent>(){
			final ObservableList<Object> righto = FXCollections.observableArrayList(DataWorker.INSTANCE.getObjects().values().toArray());

			@Override
			public void handle(ActionEvent event) {
				rightList.setVisible(true);
				rightList.setItems(righto);
			}
			
		});
		
		relationChoiceRightCat.setOnAction(new EventHandler<ActionEvent>(){
			final ObservableList<Object> rightc = FXCollections.observableArrayList(DataWorker.INSTANCE.getCategories().values().toArray());

			@Override
			public void handle(ActionEvent event) {
				rightList.setVisible(true);
				rightList.setItems(rightc);
			}
			
		});
	}

	@Override
	public void updateComponent() {
		table.fillTable(tempRelation.getAttributes());
	}

	@Override
	public void updateModel() {
		if(relationChoiceLeftObj.isSelected()){
			if(relationChoiceRightObj.isSelected()){	//Beziehung Zwischen Object und Object
				tempRelation.setTypeOfObjectOne("DataObject");
				tempRelation.setTypeOfObjectTwo("DataObject");
				tempRelation.setIdfromObjectOne(((DataObject)leftList.getSelectionModel().getSelectedItem()).getId());
				tempRelation.setIdfromObjectTwo(((DataObject)rightList.getSelectionModel().getSelectedItem()).getId());
				
			}else{										//Beziehung Zwischen Object und Kategorie
				tempRelation.setTypeOfObjectOne("DataObject");
				tempRelation.setTypeOfObjectTwo("DataCategory");
				tempRelation.setIdfromObjectOne(((DataObject)leftList.getSelectionModel().getSelectedItem()).getId());
				tempRelation.setIdfromObjectTwo(((DataCategory)rightList.getSelectionModel().getSelectedItem()).getId());
			}
		}
		else{
			if(relationChoiceRightObj.isSelected()){	//Beziehung zwischen Kategorie und Object
				tempRelation.setTypeOfObjectOne("DataCategory");
				tempRelation.setTypeOfObjectTwo("DataObject");
				tempRelation.setIdfromObjectOne(((DataCategory)leftList.getSelectionModel().getSelectedItem()).getId());
				tempRelation.setIdfromObjectTwo(((DataObject)rightList.getSelectionModel().getSelectedItem()).getId());
			}else{										//Beziehung zwischen Kategorie und Kategorie
				tempRelation.setTypeOfObjectOne("DataCategory");
				tempRelation.setTypeOfObjectTwo("DataCategory");
				tempRelation.setIdfromObjectOne(((DataCategory)leftList.getSelectionModel().getSelectedItem()).getId());
				tempRelation.setIdfromObjectTwo(((DataCategory)rightList.getSelectionModel().getSelectedItem()).getId());
			}
		}
		tempRelation.setName(relationNameTextfield.getText());	//Namen festlegen
		tempRelation.setAttributes(getAttributesFromTable());	//Attribute auslesen und festlegen
					
	}

	private HashMap<String, Object> getAttributesFromTable() {
		final List<DataAttributeTableProperties> datas = table.getItems();
		final HashMap<String, Object> objectMap = new HashMap<>();
		for(DataAttributeTableProperties attribue : datas) {
			if(table.isCompletly(attribue)){
				final Object attributeValue;
				switch (attribue.getAttributTyp()) {
					case String :
						attributeValue = attribue.getAttributValue();
						break;
					case Integer :
						attributeValue = Integer.valueOf(attribue.getAttributValue());
						break;
						
					case Double :
						attributeValue = Double.valueOf(attribue.getAttributValue());
						break;
					default : attributeValue = null; break;
				}
				objectMap.put(attribue.getAttributName(), attributeValue);
			}
		}
		return objectMap;
	}

	@Override
	public boolean validateComponent() {
		
		
		if(relationNameTextfield.getText().equals("") || relationNameTextfield.getText() == null){
			new InformationAlert("Es wurden kein Name eingegeben!");
			return false;
		}
		
		else if ((((rightList.getSelectionModel().getSelectedItem()) == null)
				|| ((leftList.getSelectionModel().getSelectedItem())) == null)){
			new InformationAlert("Es wurden keine Relationspartner ausgewählt!");
			return false;
		}
		
		else if(((AbstractEntity)rightList.getSelectionModel().getSelectedItem())
				.equals((AbstractEntity)leftList.getSelectionModel().getSelectedItem())) {
			new InformationAlert("Etwas darf nicht zu sich selbst in Relation stehen");
			return false;
		}
		else{
			boolean oneComplete = false;
			for(DataAttributeTableProperties property : table.getItems()){
				final boolean isComplete = table.isCompletly(property);
				if(isComplete){
					oneComplete = true;
				}
				if(isComplete && !table.isTypeRight(property)){
					new InformationAlert("Der Wert von Attribut : " + property.getAttributName() + " stimmt nicht mit dem angegeben Typen überein!");
					return false;
				}
			}

			//Dialogfenster, falls kein Attribut hinzugefügt wurde
			if(!oneComplete){
				new InformationAlert("Es wurde kein vollständiges Attribut hinzugefügt!");
				return false;
			}
		}
		return true;
	}

	/**
	 * Initialiserung der Relation beim Bearbeiten und Füllen des Fenster mit Daten
	 */
	private void initDataRelationEdit() {
		
		if(tempRelation.getName().length() == 0){
			tempRelation.setTypeOfObjectOne("");
			tempRelation.setTypeOfObjectTwo("");
		}
		
		relationNameTextfield.setText(tempRelation.getName());			//Name setzen
		final ObservableList<Object> lefto = FXCollections.observableArrayList(DataWorker.INSTANCE.getObjects().values().toArray());
		final ObservableList<Object> leftc = FXCollections.observableArrayList(DataWorker.INSTANCE.getCategories().values().toArray());
		final ObservableList<Object> righto = FXCollections.observableArrayList(DataWorker.INSTANCE.getObjects().values().toArray());
		final ObservableList<Object> rightc = FXCollections.observableArrayList(DataWorker.INSTANCE.getCategories().values().toArray());

		//Listen füllen
		if(tempRelation.getTypeOfObjectOne().equals("DataObject")){	
			relationChoiceLeftObj.setSelected(true);
			leftList.setItems(lefto);
			if(tempRelation.getIdfromObjectOne() != null)
				leftList.getSelectionModel().select(getObjectToSelect(lefto, tempRelation.getIdfromObjectOne(), "DataObject"));
		}else{
			relationChoiceLeftCat.setSelected(true);
			leftList.setItems(leftc);
			if(tempRelation.getIdfromObjectOne() != null)
				leftList.getSelectionModel().select(getObjectToSelect(leftc, tempRelation.getIdfromObjectOne(), "DataCategory"));
		}
		if(tempRelation.getTypeOfObjectTwo().equals("DataObject")){
			relationChoiceRightObj.setSelected(true);
			rightList.setItems(righto);
			if(tempRelation.getIdfromObjectTwo() != null)
				rightList.getSelectionModel().select(getObjectToSelect(righto, tempRelation.getIdfromObjectTwo(), "DataObject"));
		}else{
			relationChoiceRightCat.setSelected(true);
			rightList.setItems(rightc);
			if(tempRelation.getIdfromObjectTwo() != null)
				rightList.getSelectionModel().select(getObjectToSelect(rightc, tempRelation.getIdfromObjectTwo(), "DataCategory"));
		}
	}
	
	/**
	 * Gibt das Object wieder, das in der übergebenen Liste die ID hat
	 * @param list
	 * @param id
	 * @param type
	 * @return
	 */
	private Object getObjectToSelect(ObservableList<Object> list, BigInteger id, String type){
		Object temp = new Object();
		if(type.equals("DataObject")){
			for(Object o:list){
				if(((DataObject)o).getId().compareTo(id) == 0){
					temp = o;
					break;
				}
			}
		}
		else{
			for(Object o:list){
				if(((DataCategory)o).getId().compareTo(id) == 0){
					temp = o;
					break;
				}
			}
		}
		return temp;
	}
	
	
	@Override
	public int getComponentHeight() {
		return VIEW_HEIGHT;
	}

	@Override
	public int getComponentWidth() {
		return VIEW_WIDTH;
	}

	@Override
	public void onClose() {
		DialogHandler.getInstance().getTempDialogMap().remove(tempRelation);
		if(DialogHandler.isRelationDialogOpen()){
			DialogHandler.setNewRelationDialog(null);
		}
	}

	@Override
	public void save() {
		try {
			this.relationDao.save(tempRelation);
		} catch (JDOMException | IOException | ReflectiveOperationException e) {
			new ErrorAlert("beim Speichern", "Relation konnte nicht in der Datei gespeichert werden!");
		}	
	}	
}