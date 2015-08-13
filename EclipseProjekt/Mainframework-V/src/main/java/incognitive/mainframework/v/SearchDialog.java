package incognitive.mainframework.v;

import incognitive.commonelements.mc.model.DragDropModel;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.entitydialog.DialogHandler;
import incognitive.commonelements.v.entitydialog.SaveCancelDialogBuilder;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;
import incognitive.database.model.AbstractEntity;
import incognitive.mainframework.mc.control.SearchDialogController;
import incognitive.navigation.mc.control.RightContainerListener;
import incognitive.navigation.v.NavigationTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * View-Klasse für den Suchedialog
 * @author Peter Hornik
 **/

public class SearchDialog extends BorderPane implements RightContainerListener {

	private SearchDialogController controller;
	private RightContainer rightContainer;
	private NavigationTabPane navigationTabSheet;

	private ObservableList<DataObject> searchResults ;
	
	private TextField searchString;
	private CheckBox checkObjects;
	private CheckBox checkAttributes;
	private ListView<DataObject> resultList;
	

	public SearchDialog(NavigationTabPane navSheet, RightContainer container){
		this.navigationTabSheet = navSheet;
		this.rightContainer = container;
		controller = new SearchDialogController();

		//GUI-Elemente:
		Button searchButton = new Button("Suchen");
		searchString = new TextField();
		resultList = new ListView<DataObject>();
		checkObjects = new CheckBox("Objektnamen");
		checkObjects.setSelected(true);
		checkAttributes = new CheckBox("Attribute");
		checkAttributes.setSelected(true);
		
		HBox highBox = new HBox(10);
		highBox.getChildren().addAll(searchString, searchButton, checkAttributes);
		highBox.setPadding(new Insets(0, 0, 10, 0));
		
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setTop(highBox);
		this.setCenter(resultList);
		
		//Handler:
		
		//Wenn Suche-Button gedrückt wird: 
		searchButton.setOnAction(new EventHandler(){
			@Override
			public void handle(Event arg0) {
				SearchDialog.this.search();
			}
		});
		
		//Wenn Eingabetaste im Suchtextfeld gedrückt wird:
		this.searchString.setOnKeyPressed(new EventHandler<KeyEvent>(){
	        @Override
	        public void handle(KeyEvent ke){
	            if (ke.getCode().equals(KeyCode.ENTER)){
	                SearchDialog.this.search();
	            }
	        }
	    });
		
		//Wenn ein Objekt aus der ListView gedragt wird: 
		this.resultList.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				final AbstractEntity entity = SearchDialog.this.resultList.getSelectionModel().getSelectedItem();
				if(entity != null){
					Dragboard dragboard = startDragAndDrop(TransferMode.LINK);
					ClipboardContent content = new ClipboardContent();
					content.put(DragDropModel.customFormat, entity);
					dragboard.setContent(content);
				}
			}
		});
		
		//Wenn in die ListView geklickt wird:
		this.resultList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
   		    @Override
   		    public void handle(MouseEvent e) {
   		    	if (!SearchDialog.this.resultList.getSelectionModel().isEmpty()){ //Wenn etwas markiert ist, dann
	   		    	
   		    		//Wenn Doppellinksklick, dann lade Objekt tabellarisch
   		    		if((e.getButton() == MouseButton.PRIMARY) && (e.getClickCount() == 2 )){ 
	   		         	SearchDialog.this.setupRightContainer((AbstractEntity)SearchDialog.this.resultList.getSelectionModel().getSelectedItem());
	   		    	}
   		    		//Wenn Rechtsklick dann öffne Kontextmenü
	   		    	else if(e.getButton() == MouseButton.SECONDARY){
	   		    		initContextMenu();
					}
   		    	}
   		    }
   		});
	}
	
	private void initContextMenu() {
		ContextMenu menu = new ContextMenu();
		resultList.setContextMenu(menu);
		MenuItem edit = new MenuItem("Bearbeiten");
		menu.getItems().add(edit);

		//Bearbeiten aus dem Kontextmenü heraus
		edit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				SearchDialog.this.editObject(
						SearchDialog.this.resultList.getSelectionModel().getSelectedItem());
			}
		});
	}

	private void editObject(AbstractEntity abstractEntity){
		DataObject dataObject = (DataObject) abstractEntity;
		DataObjectView dialogCompoenent = new DataObjectView(dataObject);
		if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataObject) abstractEntity)){
			DialogHandler.getInstance().getTempDialogMap().put((DataObject) abstractEntity, new SaveCancelDialogBuilder()
			.withTitle("Objekt bearbeiten")
			.withDialogComponent(dialogCompoenent)
			.withSaveAction(() -> {
				navigationTabSheet.refreshSelectedEntityInTrees(dataObject);
				resultList.setItems(null);
				resultList.setItems(searchResults);
				rightContainer.updateTab(dataObject);
			})
			.build());
			dialogCompoenent.updateComponent();
		}
		else{
			DialogHandler.getInstance().getTempDialogMap().get((DataObject) abstractEntity).toFront();
		}
	}
	
	private void search() {
		searchResults = FXCollections.observableArrayList(
				controller.getResults(
						searchString.getText(), 
						checkObjects.isSelected(), 
						checkAttributes.isSelected() ));
		resultList.setItems(searchResults);
		
	}

	@Override
	public void setupRightContainer(AbstractEntity entity) {
		String title = entity.toString();
		rightContainer.newTableTab(title, entity);
	}

	@Override
	public void updateRightContainerTab(AbstractEntity entity) {
		
	}
}
