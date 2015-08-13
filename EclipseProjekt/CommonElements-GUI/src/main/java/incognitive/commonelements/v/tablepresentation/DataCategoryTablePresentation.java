package incognitive.commonelements.v.tablepresentation;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.v.component.DataObjectTable;
import incognitive.commonelements.v.component.TabTable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 * 
 * Klasse für tabellarische Darstellung einer Kategorie
 * @author Yuliya Akulova
 * @author Sarah Richter
 * 
 *
 **/
public class DataCategoryTablePresentation extends BorderPane  {
	
	private DataObjectTable objectTable = new DataObjectTable(275); //Tabelle für Objekte erstellen
	
	public DataCategoryTablePresentation(DataCategory category){
		//Label
		Label labelCategoryName = new Label();
		final double fontSize = 30.0; 									//Schriftgröße für Label
		
		//Tabelle für Tags
		TabTable tagTable = new TabTable();
		tagTable.fillTable(category.getTagsSet());
		
		//Tabelle für Objekte
		objectTable.init();
		
		//auf BorderPane anordnen
        labelCategoryName.setText(category.getName()); 					//Kategoriename übergeben
	    this.setTop(labelCategoryName);									//Label anordnen
	    labelCategoryName.setFont(new Font(fontSize)); 				//Schriftgröße des Labels festlegen
	    
	    //Tabellen auf HBox anordnen und hinzufügen
	    HBox hbox = new HBox(); 										//HBox erstellen
	    hbox.getChildren().addAll(tagTable, objectTable); 				//Tabellen nebeneinander anordnen
	    HBox.setHgrow(tagTable, Priority.ALWAYS); 					//Tabellen über die gesamte Breite und Höhe des Fensters
	    HBox.setHgrow(objectTable, Priority.ALWAYS);
	    this.setPadding(new Insets(5, 10, 5, 10));
	    this.setCenter(hbox); 											//HBox anordnen    
	   
	}

	public int getComponentWidth() {
		return 590;														//Fensterbreite
	}       
}
