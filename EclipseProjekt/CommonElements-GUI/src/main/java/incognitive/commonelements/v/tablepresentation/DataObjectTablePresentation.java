package incognitive.commonelements.v.tablepresentation;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.component.DataAttributeTable;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import jfxtras.scene.layout.GridPane;
/**
 * Klasse zum Erstellen der GUI für das Erstellen und Bearbeiten eines Objektes
 * @author Sarah Richter
 *
 */
public class DataObjectTablePresentation extends BorderPane {
	
	private CommonElementsController controller;
	
	public DataObjectTablePresentation(DataObject obj){
		controller = CommonElementsController.getInstance();
		int fontSize = 30; 														//Schriftgröße für Label
		Label labelObjectName = new Label();										//Label für den Objektnamen erstellen
		Label categoryName = new Label("enthalten in folgenden Kategorien:");		//Label für Kategorien in welcher das Objekt enthalten ist
		Label categoryproposalName = new Label("Kategorievorschläge:");				//Label für Kategorievorschläge
		DataAttributeTable table = new DataAttributeTable(getComponentWidth()); 	//Tabelle erstellen
		ListView<DataCategory> leftList = new ListView<DataCategory>();							//Liste für die Kategorien
		ListView<String> rightList = new ListView<String>();						//Liste für die Kategorievorschläge
		
		// Tabelleneinstellungen
		table.init();	
		table.setEditable(false); 													//Tabelle kann nicht bearbeitet werden
		table.setVisible(true); 													//Tabelle sichtbar machen
		
		leftList.setPrefHeight(170);												//Listen-Höhe setzen
		
		rightList.setPrefHeight(170);			
		
		//Füllen der Tabelle
        table.fillTable(obj.getObjectMap()); 
	    table.getItems().remove(table.getItems().size()-1);
        
        //Label + Tabelle hinzufügen
	    labelObjectName.setText(obj.getName()); 									//Label den Objektnamen übergeben
	    this.setTop(labelObjectName); 												//Label auf BorderPane anordnen
	    labelObjectName.setFont(new Font(fontSize)); 								//Schriftgröße für das Label festlegen
	    this.setPadding(new Insets(5, 10, 10, 10)); 								//Abstände festlegen
	    this.setCenter(table); 														//Tabelle auf BorderPane anordnen
	     
	
	    GridPane grid = new GridPane();
	   
	    grid.setVgap(10);
	    grid.setPadding(new Insets(10,0,10,0));
	    
	    grid.add(categoryName,1,0);
	    grid.add(leftList,1,1);
	    grid.add(categoryproposalName,10,0);
	    grid.add(rightList,10,1);
	    
	    GridPane.setHgrow(leftList, Priority.ALWAYS);
	    GridPane.setHgrow(rightList, Priority.ALWAYS);
	    this.setBottom(grid);
	    
	
		final ObservableList<String> rightProp = FXCollections.observableArrayList(obj.getCategoryProposals());
		rightList.setItems(rightProp);

		final List<DataCategory> categories = controller.getCategoriesFromObject(obj.getId());

		final ObservableList<DataCategory> leftCat = FXCollections.observableArrayList(categories);
		leftList.setItems(leftCat);
	}

	public int getComponentWidth() {
		return 590; 																//Fensterbreite
	}
	
	
}
