package incognitive.commonelements.v.tablepresentation;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.component.DataAttributeTable;
import incognitive.database.model.AbstractEntity;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


/**
 * 
 * Klasse für tabellarische Darstellung einer Relation
 * @author Yuliya Akulova
 *
 **/
public class DataRelationTablePresentation extends BorderPane {
	
	private final CommonElementsController controller;


	public DataRelationTablePresentation(DataRelation relation){
		this.controller = CommonElementsController.getInstance();
		
		int fontSize = 30; 											//Schriftgröße für Label
		Label labelRelationName = new Label();							//Label für den RelationName erstellen
		
		//Attribut-Tabelle erstellen
		DataAttributeTable attributTable = new DataAttributeTable(390); 			
		
		// Tabelleneinstellungen
		attributTable.init();	
		attributTable.setEditable(false); 								//Tabelle kann nicht bearbeitet werden
		attributTable.setVisible(true); 								//Tabelle sichtbar machen
		
		//Füllen der Tabelle
		attributTable.fillTable(relation.getAttributes()); 
		attributTable.getItems().remove(attributTable.getItems().size()-1);
        
        //Label für Relation hinzufügen
	    labelRelationName.setText(relation.getName()); 					//Label den RelationName übergeben
	    this.setTop(labelRelationName); 								//Label auf BorderPane anordnen
	    labelRelationName.setFont(new Font(fontSize)); 				//Schriftgröße für das Label festlegen
	    this.setPadding(new Insets(5, 10, 5, 10)); 						//Abstände festlegen
	  
	    //Labels für die in Relation stehenden Objekte
	    Label labelObject1 = new Label();
	    AbstractEntity object1 = controller.getCorrectedEntity(relation.getTypeOfObjectOne(), relation.getIdfromObjectOne());
	    if (relation.getTypeOfObjectOne().equals("DataCategory")){
	    	labelObject1.setText("Kategorie: " + object1.getName());
	    }else{
	    	labelObject1.setText("Objekt: " + object1.getName());
	    }
	    
	    Label labelObject2 = new Label();
	    AbstractEntity object2 = controller.getCorrectedEntity(relation.getTypeOfObjectTwo(), relation.getIdfromObjectTwo());

	    if (relation.getTypeOfObjectTwo().equals("DataCategory")){
	    	labelObject2.setText("Kategorie: " + object2.getName());
	    }else{
	    	labelObject2.setText("Objekt: " + object2.getName());
	    }

	  	labelObject1.setFont(new Font(16));
	  	labelObject2.setFont(new Font(16));
	  
	  
		 VBox vbox = new VBox(); 												//HBox erstellen
		 vbox.getChildren().addAll(labelObject1, labelObject2, attributTable); 	//Tabellen nebeneinander anordnen
		 VBox.setVgrow(attributTable, Priority.ALWAYS); 						//Tabellen über die gesamte Breite und Höhe des Fensters
		
		 this.setPadding(new Insets(5, 10, 5, 10));
		 this.setCenter(vbox); 			
	
	}
	
}
