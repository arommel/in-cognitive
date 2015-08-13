package incognitive.commonelements.v;



import incognitive.commonelements.mc.control.LevenshteinSimilarityFinder;
import incognitive.commonelements.mc.control.SimilarityFinder;
import incognitive.commonelements.mc.control.TextImportController;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.entitydialog.ApplyCancelDialog;
import incognitive.commonelements.v.entitydialog.DialogAction;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;

import java.util.Arrays;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * 
 * Klasse zum Importieren von unstrukturiertem Text
 *
 */
public class TextImport extends Stage{

	private Scene scene;
	private TextArea textArea;
	private BorderPane bPane;
	private String text;
	private DataObject dataObject;
	private MenuItem addAsObjectName;
	private MenuItem addAsAttribute;
	private MenuItem addAsValue;
	private String attributeString; 
	private TextImportController controller;
	private EventHandler<ActionEvent> mouseHandler;
	private EventHandler<WindowEvent> onCloseRequest;
	private DialogAction saveAction;
	private DataObjectView dialogCompoenent;
	private Thread thread;
	private LoadingDialog loadingDialog;
	private ImportView importView;
	
	public TextImport(String text) {
		this.text = text;
		this.controller = new TextImportController(text);
		initEventHandler();
		initGUI();
		
		buildContextMenu();
	}
	
	/**
	 * Ändert das Defaultcontextmenu und fügt Neues Objekt, neues Attribute und Value als Punkte hinzu
	 */
	private void buildContextMenu() {
		// TODO Auto-generated method stub
		addAsObjectName = new MenuItem("neues Objekt aus markiertem Text");
		addAsObjectName.setOnAction(mouseHandler);
		addAsAttribute = new MenuItem("markierten Text als Attribut hinzufügen");
		addAsAttribute.setOnAction(mouseHandler);
		addAsValue = new MenuItem("markierten Text als Value hinzufügen");
		addAsValue.setOnAction(mouseHandler);
		
	    ContextMenu contextMenu = new ContextMenu();
	    contextMenu.getItems().add(addAsObjectName);
	    contextMenu.getItems().add(addAsAttribute);
	    contextMenu.getItems().add(addAsValue);
	    contextMenu.getItems().addAll(createDefaultMenuItems(textArea));
	   
	    textArea.setContextMenu(contextMenu);
	}

	/**
	 * initialisert die EventHandler für den Klick auf einen Kontextmenupunkt und 
	 * den CloseRequest für das Fenster
	 */
	private void initEventHandler() {
		// TODO AutDataObjectView auto-generated method stub
		 mouseHandler = new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		        	if(textArea.getSelectedText() != null){
			        	if(event.getSource() == addAsObjectName){
			        		openDataObjectWindow(textArea.getSelectedText());
			        	}
			        	if(event.getSource() == addAsAttribute){
			        		if(attributeString == null){
				        		attributeString = textArea.getSelectedText();
				        		dataObject.getObjectMap().put(attributeString, new String(""));		
				        		addAsAttribute.setDisable(true);
				        		if(dialogCompoenent != null){
			        				dialogCompoenent.updateComponent();
			        			}
			        		}
			        	}
			        	if(event.getSource() == addAsValue){
			        		if(attributeString != null){
			        			
			        			dataObject.getObjectMap().put(attributeString, textArea.getSelectedText());
			        			attributeString = null;
			        			addAsAttribute.setDisable(false);
			        			if(dialogCompoenent != null){
			        				dialogCompoenent.updateComponent();
			        			}
			        		}
			        	}
		        	}
		        }
		    };
		    
		    this.onCloseRequest = new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
					if(importView != null){
						importView.close();
					}
				}
			};
		    
		   
	}
	
	/**
	 * Öffnet ein ApplyCancelDialog mit einem DataObjectView im Hintergrund.
	 * Übergibt dem DataObjectView ein erzeugtes DataObject mit dem übergebenen Stringnamen
	 * @param name
	 */
	private void openDataObjectWindow(String name) {
		this.dataObject = new DataObject();
		dataObject.setName(name);
		dialogCompoenent = new DataObjectView(dataObject);
		
		ApplyCancelDialog objectDialog = 	new ApplyCancelDialog(dialogCompoenent, "Objekt importieren");
		objectDialog.withCloseAction(() -> {	this.save();	});
		dialogCompoenent.updateComponent();	
		objectDialog.show();
		this.setAlwaysOnTop(true);
		addAsObjectName.setDisable(true);
	}
	
	/**
	 * Initialisiert die grafische Oberfläche
	 */
	private void initGUI(){
		
		this.bPane = new BorderPane();
		this.textArea = new TextArea();
		this.setTitle("Objekte aus unstrukturiertem Text erstellen");
		textArea.setText(text);		
		this.bPane.setCenter(textArea);
		this.scene = new Scene(bPane);
		this.setScene(scene);
		this.setOnCloseRequest(onCloseRequest);
		
	}
	
	/**
	 * Erstellt die Funktionalitäten des Defaultcontextmenu für das übergebene Textfeld
	 * @param t
	 * @return Liste vom Typ MenuItem mit den Standardfunktionalitäten des Kontextmenüs im Textfeld
	 */
	private List<MenuItem> createDefaultMenuItems(TextInputControl t) {
	    MenuItem cut = new MenuItem("Cut");
	    cut.setOnAction(e -> t.cut());
	    MenuItem copy = new MenuItem("Copy");
	    copy.setOnAction(e -> t.copy());
	    MenuItem paste = new MenuItem("Paste");
	    paste.setOnAction(e -> t.paste());
	    MenuItem delete = new MenuItem("Delete");
	    delete.setOnAction(e -> t.deleteText(t.getSelection()));
	    MenuItem selectAll = new MenuItem("Select All");
	    selectAll.setOnAction(e -> t.selectAll());

	    BooleanBinding emptySelection = Bindings.createBooleanBinding(() ->
	        t.getSelection().getLength() == 0,
	        t.selectionProperty());

	    cut.disableProperty().bind(emptySelection);
	    copy.disableProperty().bind(emptySelection);
	    delete.disableProperty().bind(emptySelection);

	    return Arrays.asList(cut, copy, paste, delete, new SeparatorMenuItem(), selectAll);
	}
	
	/**
	 * Setzt die Aktion die beim Speichern mit ausgeführt werden soll
	 * @param action
	 */
	public void withSaveAction(DialogAction action){
		this.saveAction = action;
	}
	
	/**
	 * Initialisiert den SimilarityFinder und veranlasst das Öffnen einer neuen 
	 * ImportNotification nachdem ähnliche Kategorien gefunden wurden.
	 * 
	 */
	public void save(){
		SimilarityFinder similarityFinder = new LevenshteinSimilarityFinder(dataObject);
		ProgressBar pBar = new ProgressBar();
		pBar.progressProperty().bind(similarityFinder.getFindSimilaritys().progressProperty());
		loadingDialog = new LoadingDialog(pBar,"Die Daten werden ausgewertet","Bitte haben Sie einen Moment Geduld.",
				() -> {similarityFinder.getFindSimilaritys().cancel(); });
		thread = new Thread(similarityFinder.getFindSimilaritys());
		thread.start();
		similarityFinder.getFindSimilaritys().setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                loadingDialog.close();
                ImportNotification importNotification = new ImportNotification(dataObject, null, similarityFinder.getCategoryWithMatches());
               
                importNotification.show();
            }

			
        });
		saveAction.perform();
		addAsObjectName.setDisable(false);
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
