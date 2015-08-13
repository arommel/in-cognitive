package incognitive.commonelements.v.entitydialog.editdialog;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.control.LevenshteinSimilarityFinder;
import incognitive.commonelements.mc.control.SimilarityFinder;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.LoadingDialog;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.alert.InformationAlert;
import incognitive.commonelements.v.component.DataAttributeTable;
import incognitive.commonelements.v.component.properties.DataAttributeTableProperties;
import incognitive.commonelements.v.entitydialog.DialogComponent;
import incognitive.commonelements.v.entitydialog.DialogHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jdom2.JDOMException;
/**
 * Klasse zum Erstellen der GUI für das Erstellen und Bearbeiten eines Objektes
 * @author Sarah Richter
 *
 */
public class DataObjectView extends BorderPane implements DialogComponent {	
	
	private CommonElementsController controller;
	
	//Name des Objektes
	private TextField nameObjectField = new TextField(); 							//Textfeld erstellen
	private Label nameLabel = new Label("Name des Objektes : "); 							//Label für Objektnamen erstellen
	
	//Attribut-Tabelle 
	private DataAttributeTable table = new DataAttributeTable(getComponentWidth()); //Tabelle erstellen
	
	//Kategorievorschläge
	private Button categoryProposal = new Button("Neu laden");
	Label categories = new Label("enthalten in folgenden Kategorien:");
	Label categoriesProposal = new Label("Kategorievorschläge:");	
	ListView<DataCategory> categoriesList = new ListView<DataCategory>();							
	ListView<String> categoriesProposalList = new ListView<String>();
	
	private DataObject obj; 
	
	private final DataObjectDao objectDao;
	
	private SimilarityFinder similarityFinder;
	private Thread thread;

	//Konstruktor für das erstellen eines Objektes
	public DataObjectView() {
		this.obj = new DataObject();
		this.objectDao = DataObjectDao.getInstance();
	}
	//Konstruktor für das Bearbeiten eines Objektes
	public DataObjectView(DataObject object) {
		this.obj = object;
		this.objectDao = DataObjectDao.getInstance();
	}
	
	/**
	 * Initialisieren der GUI: Fenstlegen der Layouts, Hinzufügen der Panels, Listener für Buttons
	 */
	
	public void init() {
		controller = CommonElementsController.getInstance();
        initPanel();
        this.table.init();
        this.table.initContextMenu();

	}
	//Elemente auf BorderPane anordnen
	private void initPanel() {
		
        table.setPrefHeight(250);
		
	    HBox tablebox = new HBox(); 													//Erstellen eine HBox
	    tablebox.getChildren().addAll(nameLabel, nameObjectField); 						//der HBox die Elemente übergeben
	    nameObjectField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

	    nameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    HBox.setHgrow(nameObjectField, Priority.ALWAYS);
					
	    this.setPadding(new Insets(20, 10, 5, 10)); 								//allgemeine Abstände
	    this.setCenter(table);														//Tabelle auf BorderPane anordnen

	    nameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
	    HBox.setHgrow(nameObjectField, Priority.ALWAYS);
	    tablebox.setSpacing(20);														//Abstände zwischen einzelnen Elementen innerhalb der HBox
	    tablebox.setPadding(new Insets(0, 0, 10, 0)); 
	    
	    HBox categoryBox = new HBox();
	    
	    categoryBox.getChildren().addAll(categoriesProposal, categoryProposal);
	    categoriesProposal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    categoryProposal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    categoryBox.setSpacing(35);
	    
	    categoryProposal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				updateModel();
				DataObjectView.this.similarityFinder = new LevenshteinSimilarityFinder(obj);
				DataObjectView.this.initSimilarityFinder();
			}
			
		});
	    
	    GridPane grid = new GridPane();
		   
	    grid.setVgap(10);
	    grid.setPadding(new Insets(10,0,10,0));
	    
	    categoriesList.setPrefHeight(170);
	    categoriesProposalList.setPrefHeight(170);
	    
	    grid.add(categories,1,0);
	    grid.add(categoriesList,1,1);
	    grid.add(categoryBox,12,0);
	    grid.add(categoriesProposalList,12,1);
	    
	
		final ObservableList<String> rightProp = FXCollections.observableArrayList(obj.getCategoryProposals());
		categoriesProposalList.setItems(rightProp);

		final List<DataCategory> categories = controller.getCategoriesFromObject(obj.getId());

		final ObservableList<DataCategory> leftCat = FXCollections.observableArrayList(categories);
		categoriesList.setItems(leftCat);
	    
	    VBox bla = new VBox();
	    
	    bla.getChildren().addAll(table);
	    bla.setSpacing(20);														
	    bla.setPadding(new Insets(0, 0, 10, 0));
	    
	    
	    this.setTop(tablebox);
	    this.setCenter(bla);
	    this.setBottom(grid);
	    
	    this.setPadding(new Insets(20, 10, 5, 10));
	}

	public void updateComponent() {
		nameObjectField.setText(obj.getName()); 									//Textfeld den Objektnamen übergeben
		table.fillTable(obj.getObjectMap()); 
	}
	
	public void updateModel() {
			//Hole Attribute aus Table
			final List<DataAttributeTableProperties> datas = table.getItems();
			final HashMap<String, Object> objectMap = new HashMap<>();
			for(DataAttributeTableProperties attribue : datas) {
				final boolean isComplete = table.isCompletly(attribue);
				if(isComplete){
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
			obj.setName(nameObjectField.getText()); 								//Textfeld Objektnamen übergeben
			obj.setObjectMap(objectMap);
			try {
			objectDao.save(obj);
			} catch (JDOMException | IOException | ReflectiveOperationException e) {
				new ErrorAlert("beim Speichern", "Objekt konnte nicht in der Datei gespeichert werden!");
			}
		}
	public void save(){
		try {
			objectDao.save(obj);
		} catch (JDOMException | IOException | ReflectiveOperationException e) {
			new ErrorAlert("beim Speichern", "Objekt konnte nicht in der Datei gespeichert werden!");
		}
	}
	
	
	@Override
	public boolean validateComponent() {
		//Dialogfenster, falls kein Objektname eingegeben wurde
		if(nameObjectField.getText().equals("") || nameObjectField.getText() == null) { 	//Bedingung (wenn keine Eingabe im Textfeld)
			new InformationAlert("Es wurden kein Name eingegeben!");
			return false;
			
		}else {
			boolean oneComplete = false;
			for(DataAttributeTableProperties property : table.getItems()){
				final boolean isComplete = table.isCompletly(property);
				if(isComplete){
					oneComplete = true;
				}
				if(isComplete && !table.isTypeRight(property)){
					new InformationAlert("Der Wert von Attribut : " + property.getAttributName() + " stimmt nicht mit dem angegeben Typen überein!");														   	//Dialog anzeigen
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
	
	@Override
	public int getComponentHeight() { //Fensterhöhe
		return 600;
	}
	@Override
	public int getComponentWidth() { //Fensterbreite
		return 500;
	}
	@Override
	public void onClose() {
		DialogHandler.getInstance().getTempDialogMap().remove(obj);
		if (DialogHandler.isObjectDialogOpen()){
			DialogHandler.setNewObjectDialog(null);
		}
	}
	
	
	private void initSimilarityFinder(){
		ProgressBar pBar = new ProgressBar();
		pBar.progressProperty().bind(this.similarityFinder.getFindSimilaritys().progressProperty());
		LoadingDialog loadingDialog = new LoadingDialog(pBar,"Die Daten werden ausgewertet","Bitte haben Sie einen Moment Geduld."
				, () -> {DataObjectView.this.similarityFinder.getFindSimilaritys().cancel(); });
		thread = new Thread(this.similarityFinder.getFindSimilaritys());
		thread.start();
		this.similarityFinder.getFindSimilaritys().setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                loadingDialog.close();
                
                obj.setCategoryProp(new ArrayList<String>(DataObjectView.this.similarityFinder.getCategoryWithMatches().keySet()));
				
				final ObservableList<String> rightProp = FXCollections.observableArrayList(obj.getCategoryProposals());
				categoriesProposalList.setItems(rightProp);
            }
        });
	}
	
}
