package incognitive.commonelements.v;

import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.entitydialog.ApplyCancelDialog;
import incognitive.commonelements.v.entitydialog.DialogAction;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * 
 * DialogFenster, dass Hinweise über den Import eines DataObjectes informiert
 * und die Möglichkeit bietet das Objekt zu bearbeiten/speichern, sowie Kategorievorschläge anzusehen 
 * und das Objekt diesen hinzuzufügen.
 */
public class ImportNotification extends Stage implements EventHandler<ActionEvent>{

	private Button saveButton, editButton, editCatButton, cancelButton;
	private ImportView importView;
	private Label contentText;
	private Scene scene;
	private DialogAction saveAction;
	private CategoryObjectLink catObjectLink;
	private ArrayList<String> possibleCategories, selectCategories;
	private HashMap<String, Double> categoryWithMatches;
	private DataObject dataObject;
	
	
	public ImportNotification(DataObject dataObject, ArrayList<String> possibleCategories, HashMap<String, Double> categoryWithMatches){
		this.setTitle("Objekt gefunden");
		this.dataObject = dataObject;
		this.possibleCategories = possibleCategories;
		this.categoryWithMatches = categoryWithMatches;
		initGUI();
	}
	
	/**
	 * initialisiert die grafische Oberfläche
	 */
	private void initGUI() {
		this.setHeight(200);
		this.setWidth(600);
		BorderPane bPane = new BorderPane();
		contentText = new Label("Es wurde ein neues Objekt gefunden ("
				+ dataObject.getName() + ")");
		contentText.setAlignment(Pos.CENTER);
		
		HBox hBox = new HBox(10);
		hBox.setPadding(new Insets(10,30,10,30));
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(contentText);
		bPane.setCenter(hBox);
		
		saveButton = new Button("Speichern");
		saveButton.setOnAction(this);
		editButton = new Button("Bearbeiten");
		editButton.setOnAction(this);
		editCatButton = new Button("Kategorievorschläge");
		editCatButton.setOnAction(this);
		cancelButton = new Button("Abbrechen");
		cancelButton.setOnAction(this);
		
		hBox = new HBox(20);
		hBox.setPadding(new Insets(10,10,10,10));
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(saveButton, editButton, editCatButton, cancelButton);
		
		
		bPane.setBottom(hBox);
		
		
		scene = new Scene(bPane);
		scene.getStylesheets().add("dialog.css");
		this.setScene(scene);
		
	}
	
	
	
	/**
	 * Überschriebene handle Methode, die die Klicks auf die Buttons verarbeitet
	 */
	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == saveButton){
			if(importView != null){
				Alert alert = new Alert(AlertType.CONFIRMATION, "Möchten Sie ein weiteres Objekt importieren? ", ButtonType.YES, ButtonType.NO);
				alert.setTitle("Importhinweis");
				alert.setHeaderText("Bestätigung");
				alert.getDialogPane().getStylesheets().add("dialog.css");
	
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.YES){
					save();
					close();
					reopen();
					
				    
				} else if (result.get() == ButtonType.NO) {
				    save();
				    close();
				}
			}else{
				save();
				close();
			}
		}else if(arg0.getSource() == editButton){
			createDataObjectDialog();
		}else if(arg0.getSource() == editCatButton){
			createCategoryLinkDialog();
		}else if(arg0.getSource() == cancelButton){
		
			this.close();
		}
	}
	
	/**
	 * Öffnet den ImportView erneut
	 */
	private void reopen() {
		DataObject dataObject = new DataObject();
		ImportView importView = new ImportView(dataObject);
		importView.withSaveAction(saveAction);
	}
	
	/**
	 * Speichert das DataObject und die ausgewählten Kategorievorschläge sofern diese 
	 * nicht bereits vorhanden sind und fügt das Objekt diesen hinzu
	 */
	private void save() {
		DataObjectDao objectDao = DataObjectDao.getInstance();
		DataCategoryDao categoryDao = DataCategoryDao.getInstance();
		DataRelationDao relationDao = DataRelationDao.getInstance();
		
		try {
			objectDao.save(dataObject);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		if(this.selectCategories != null){
			for(String categoryName : this.selectCategories){
				BigInteger existingCatId = getCategoryId(categoryName, categoryDao);
				if(existingCatId == null){
					DataCategory tmpCat;
					try {
						tmpCat = importView.getController().getCategoryWithTags(categoryName);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						tmpCat = new DataCategory();
						tmpCat.setCategoryName(categoryName);
					}
					
					try{
					
					categoryDao.save(tmpCat);
					DataRelation tmpRelation = new DataRelation();
					tmpRelation.setName(DataRelation.CATEGORY_OBJECT_RELATION);
					tmpRelation.setIdfromObjectTwo(tmpCat.getId());
					tmpRelation.setIdfromObjectOne(dataObject.getId());
					tmpRelation.setTypeOfObjectTwo(DataCategory.class.getSimpleName());
				    tmpRelation.setTypeOfObjectOne(DataObject.class.getSimpleName());
					relationDao.save(tmpRelation);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					DataRelation tmpRelation = new DataRelation();
					tmpRelation.setName(DataRelation.CATEGORY_OBJECT_RELATION);
					tmpRelation.setIdfromObjectTwo(existingCatId);
					tmpRelation.setIdfromObjectOne(dataObject.getId());
					tmpRelation.setTypeOfObjectTwo(DataCategory.class.getSimpleName());
				    tmpRelation.setTypeOfObjectOne(DataObject.class.getSimpleName());
					
					try {
						relationDao.save(tmpRelation);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		}
		if(saveAction != null){
			saveAction.perform();	
		}
	}
	
	/**
	 * Setzt die Übergebene Aktion, die beim Speichern passieren soll
	 * @param action
	 */
	public void withSaveAction(DialogAction action){
		saveAction = action;
	}
	
	/**
	 * 
	 * @param categoryName
	 * @param categoryDao
	 * @return Boolean: true, wenn die Kategorie bereits existiert, false wenn diese nicht exisitiert
	 */
	private boolean isCategoryKnown(String categoryName, DataCategoryDao categoryDao){
		HashMap<BigInteger, DataCategory> knownCategories = (HashMap<BigInteger, DataCategory>) categoryDao.findAll();
		for(DataCategory dCategory : knownCategories.values()){
			if(dCategory.getName() == categoryName){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Erstellt und öffnet das Fenster für Kategorievorschläge
	 */
	private void createCategoryLinkDialog() {
		// TODO Auto-generated method stub
		if(catObjectLink == null){
			this.catObjectLink = new CategoryObjectLink(this);
			final Stage st = new Stage();
			final Scene scene = new Scene(catObjectLink, 400, 500);
			scene.getStylesheets().add("dialog.css");
			st.setScene(scene);
			st.setTitle("Zu Kategorien zuweisen");
			st.show();
		}else{
			catObjectLink.show();
		}
		
	}
	
	/**
	 * Erzeugt einen DataObjectDialog
	 */
	
	private void createDataObjectDialog() {
		String title;
		DataObject dataObject;
		
	
		title = "Objekt bearbeiten";
	
		DataObjectView dialogCompoenent = new DataObjectView(importView.getDataObject());
		
		new ApplyCancelDialog(dialogCompoenent, "Objekt bearbeiten");
		dialogCompoenent.updateComponent();	
		
		
	}
	
	/**
	 * Schließt den Importhinweis und sofern übergeben, das ImportView Fenster
	 */
	@Override
	public void close() {
		super.close();
		if(importView != null){
			importView.close();
		}
	}
	
	/**
	 * Gibt zu einem übergebenem Kategorienamen die Id zurück
	 * @param categoryName
	 * @param categoryDao
	 * @return Kategorie Id vom Typ BigInteger
	 */
	private BigInteger getCategoryId(String categoryName, DataCategoryDao categoryDao){
		HashMap<BigInteger, DataCategory> knownCategories = (HashMap<BigInteger, DataCategory>) categoryDao.findAll();
		for(DataCategory dCategory : knownCategories.values()){
			if(dCategory.getName().equals(categoryName)){
				return dCategory.getId();
			}
		}
		return null;
	}
	
	/**
	 * Gibt die Liste der Kategorievorschläge zurück
	 * @return ArrayList vom Typ String mit den Kategorievorschlägen
	 */
	public ArrayList<String> getPossibleCategories() {
		return possibleCategories;
	}
	
	/**
	 * Gibt die ausgewählten Kategorievorschläge zurück
	 * @return ArraList vom Typ String mit den ausgewählten Kategorievorschlägen
	 */
	public ArrayList<String> getSelectedCategories(){
		return selectCategories;
	}

	/**
	 * Setzt die Liste der ausgewählten Kategorien
	 * @param selectedCategories
	 */
	public void setSelectedCategories(ArrayList<String> selectedCategories) {
		this.selectCategories = selectedCategories;
		if(this.selectCategories.size() > 0){
			this.dataObject.setCategoryProp(selectCategories);
		}
	}

	/**
	 * Gibt eine HashMap der Kategorievorschläge mit Matchingscore zurück
	 * @return HashMap vom Typ String, Double mit den Kategorievorschlägen und Matchingscore
	 */
	public HashMap<String, Double> getCategoryWithMatches() {
		return categoryWithMatches;
	}

	/**
	 * Setzt die HashMap der Kategorievorschläge
	 * @param categoryWithMatches
	 */
	public void setCategoryWithMatches(HashMap<String, Double> categoryWithMatches) {
		this.categoryWithMatches = categoryWithMatches;
	}
	
	/**
	 * Gibt den aufrufenden ImportView zurück
	 * @return ImportView
	 */
	public ImportView getImportView() {
		return importView;
	}
	
	/**
	 * Setzt den aufrufenden ImportView
	 * @param importView
	 */
	public void setImportView(ImportView importView) {
		this.importView = importView;
	}
	
	
}
