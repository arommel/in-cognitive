package incognitive.mainframework.v;

import incognitive.commonelements.mc.model.DragDropModel;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.graphicpresentation.EntityGraphEditor;
import incognitive.commonelements.v.tablepresentation.DataCategoryTablePresentation;
import incognitive.commonelements.v.tablepresentation.DataObjectTablePresentation;
import incognitive.commonelements.v.tablepresentation.DataRelationTablePresentation;
import incognitive.database.model.AbstractEntity;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
/**
 * 
 * Klasse für das rechte TabPane im Hauptfenster
 * @author Peter Hornik
 *
 **/


public class RightContainer extends TabPane{
    private HashMap<Tab, AbstractEntity> entityTabReg; //Registry, in der die tabellarischen Tabs geführt sind
    
	private Tab tab;
    private TextField textField = new TextField();
 	private ContextMenu menu;
	
	public RightContainer() {
		this.entityTabReg = new HashMap<Tab, AbstractEntity>();

		initComponents();
		//Listener für Doppelklick auf einen Tab
		
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

   		    @Override
   		    public void handle(MouseEvent e) {
   		    	
   		    	if((e.getButton() == MouseButton.PRIMARY)&&!(RightContainer.this.getSelectionModel().isEmpty())){
   		         	if(e.getClickCount() == 2 ){
   		         	  tab = RightContainer.this.getSelectionModel().getSelectedItem();	//angeklickten Tab holen
   		         	  textField.setText(((Label)tab.getGraphic()).getText());  			//Textfeld zum bearbeiten deklarieren und initialiseren
   		         	  tab.setGraphic(textField);  										//Textfeld statt Label in Tab-Header
   		         	  textField.selectAll();  											//alles markieren
	   		          textField.requestFocus();  										//Focus auf Textfeld
   		         	}
   		    	}
   		    	else if(e.getButton() == MouseButton.SECONDARY){
   		    		initContextMenu();
		         
				}
   		    }
   		});

   		textField.setOnAction(new EventHandler<ActionEvent>() {  						//Listener für Entertaste
   		  @Override  
   		  public void handle(ActionEvent event) {  
   		    tab.setGraphic(new Label(textField.getText()));  							//Text wieder im Tabheader anzeigen
   		  }  
   		});


   		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {  		//Listener für außerhalb klicken
   		  @Override  
   		  public void changed(ObservableValue<? extends Boolean> observable,  
   		      Boolean focusBefore, Boolean focusAfter) {  
   		    if (!focusAfter) {  														// Focus verloren
   		    	tab.setGraphic(new Label(textField.getText()));    
   		    }  
   		  }  
   		});  
   		
   		this.setOnDragOver(new EventHandler<DragEvent>(){
   			private int counter = 0; 

			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != this &&
		                event.getDragboard().hasContent(DragDropModel.customFormat)) {
		            event.acceptTransferModes(TransferMode.ANY);
		            counter++;
		        }
		        event.consume();
				
			}
        	
        	
        });
        
   		this.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Dragboard db = event.getDragboard();
				ClipboardContent content = new ClipboardContent();
		        boolean success = false;
		        
		        if (db.hasContent(DragDropModel.customFormat)) {
		        	
		        	content.put(DragDropModel.customFormat, db.getContent(DragDropModel.customFormat));
		            success = true;
		            
		         }
		        
		         event.setDropCompleted(success);
		         
				if (event.isDropCompleted()) {
					final Node tabcontent = getSelectionModel().getSelectedItem().getContent();
					if(tabcontent instanceof EntityGraphEditor){
						final EntityGraphEditor currentEditor = (EntityGraphEditor) tabcontent;
						AbstractEntity entity = (AbstractEntity) content.get(DragDropModel.customFormat);
						currentEditor.addNodeAsEntity(entity);
					}
				}
		         event.consume();
				
			}
   			
   			
   		});	
	}

	//Ein erster leerer Tab wird erzeugt
	private void initComponents(){
		tab = new Tab();  
   		tab.setGraphic(new Label("Object Tab"));
   		tab.setContent(new EntityGraphEditor());
   	
   		this.getTabs().add(tab); 	   
	}
	
	/**
	 * Erzeugt einen leeren nummerierten Tab im rechten TabPane
	 **/  
	
	public void newTab(){
		newGraphicTab(("Object Tab " + (RightContainer.this.getTabs().size() + 1)));
    }
	
	/**
	 * Erzeugt einen neuen Tab zur grafischen Darstellung einer DataObjectFxPresentation
	 * @param title bestimmt die Beschriftung des Tabs.
	 * @param entity ist das Objekt, welches der Tab anzeigen soll.
	 **/
	
	public void newGraphicTab(String title){
		tab = new Tab();
   		tab.setGraphic(new Label(title));
	    tab.setContent(new EntityGraphEditor());
	    RightContainer.this.getTabs().add(tab);
	    RightContainer.this.getSelectionModel().select(tab);
  	}
	
	/**
	 * Erzeugt einen neuen Tab zur tabellarischen Darstellung eines AbstractEntity
	 * @param title bestimmt die Beschriftung des Tabs.
	 * @param entity ist das Objekt, welches der Tab anzeigen soll.
	 **/
	
	public void newTableTab(String title, AbstractEntity entity){
		Tab tab = new Tab();
   		tab.setGraphic(new Label(title));
   		
	    if (registerTab(tab, entity)){		//Wenn entity noch nicht als Tab dargestellt, dann					
	   		//Klassifizierung der AbstractEntity und Tab-Setup
		    setupTab(tab, entity);

		    RightContainer.this.getTabs().add(tab);
		    RightContainer.this.getSelectionModel().select(tab);
		    
		    tab.setOnClosed(new EventHandler<javafx.event.Event>() {
			     @Override
			     public void handle(javafx.event.Event e) {
			    	 deregisterTab(tab);
			     }
			});
        }
	    else{
	    	//Tab mit der entsprechenden entity auswählen.  
	    	RightContainer.this.getSelectionModel().select(getTabFromEntity(entity));
	    }
  	}
	

	private void initContextMenu() {
		menu = new ContextMenu();
		MenuItem closeAll = new MenuItem("Alle Tabs löschen");
		closeAll.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				getTabs().clear();
				entityTabReg.clear();
			}
			
		});
		menu.getItems().add(closeAll);
		for (Tab tab : this.getTabs()){
			tab.setContextMenu(menu);
		}
	}

/**
 * @param entity, zu welcher der tabellarische Tab gesucht ist
 * @return der zur entity gehörende Tab, oder null
 */
	
	private Tab getTabFromEntity(AbstractEntity entity){
		for (HashMap.Entry<Tab, AbstractEntity> entry : entityTabReg.entrySet()){
    		if (entity==entry.getValue()) return entry.getKey();
    	}
		return null;
	}
	
	/**
	 * Setzt den Inhalt eines Tabs mit einer AbstractEntity
	 * @param t ist der zu füllende Tab
	 * @param entity ist die entity, die Inhalt des Tabs ist
	 */
	
	private void setupTab(Tab t, AbstractEntity entity){
		if (entity instanceof DataObject){		    
	    	DataObjectTablePresentation objectTable = new DataObjectTablePresentation((DataObject)entity);
	    	t.setContent(objectTable);
	    }
	    else if (entity instanceof DataCategory){
	    	DataCategoryTablePresentation categoryTable = new DataCategoryTablePresentation((DataCategory)entity);
	    	t.setContent(categoryTable);
	    }
	    else if (entity instanceof DataRelation){
	    	DataRelationTablePresentation relationTable = new DataRelationTablePresentation((DataRelation)entity);
	    	t.setContent(relationTable);
	    }
		
	}
	
	/**
	 * Updatet einen tabellarischen Tab
	 * @param entity , zu welcher der Tab geupdatet werden soll
	 */
	
	public void updateTab(AbstractEntity entity){
		Tab t = getTabFromEntity(entity);
		if (t!=null){
			t.setContent(null);
			setupTab(t, entity);
		}
	}

	/**
	 * Registriert einen Tab zur tabellarischen Darstellung zur Verhinderung von Duplikaten
	 * @param t ist der zu registrierende Tab
	 * @param entity ist das tabellarisch angezeigte Objekt
	 * @return False, wenn ein Tab mit diesem Objekt schon registriert ist, True sonst. 
	 **/

	private boolean registerTab(Tab t, AbstractEntity entity){
		if (entityTabReg.containsValue(entity)){
			return false;
		}
		else{
			entityTabReg.put(t, entity);
			return true;
		}
	}
	
	/**
	 * Deregistriert einen Tab
	 * @param t ist der zu deregistrierende Tab
	 **/
	
	private void deregisterTab(Tab t){
		if (entityTabReg.containsKey(t)){
			entityTabReg.remove(t);
		}
	}
}