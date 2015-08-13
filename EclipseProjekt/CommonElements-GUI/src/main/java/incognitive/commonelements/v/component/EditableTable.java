package incognitive.commonelements.v.component;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

/**
 * Abstrakte Tabellen Klasse für eine bestimmte Property bzw. für eine Liste von Properties,
 *  welche gemeinsame Methoden anbietet
 * 
 * @author Lisa
 *
 * @param <PROPERTY>
 */
public abstract class EditableTable<PROPERTY> extends TableView<PROPERTY> {

	public void initContextMenu(){
		ContextMenu menu = new ContextMenu();
		MenuItem deleteAttribute = new MenuItem("Ausgewähltes Attribut löschen");
		MenuItem addTableRow = new MenuItem("Tabellenzeile einfügen");
		deleteAttribute.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				deleteSelectedRow();
			}
			
		});
		
		addTableRow.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				addEmptyTableRow();
				
			}
		});
		menu.getItems().addAll(deleteAttribute, addTableRow);
		this.setContextMenu(menu);
		this.contextMenuProperty().bind(Bindings.when(this.getSelectionModel().selectedItemProperty().isNull()).then((ContextMenu)null).otherwise(menu));
	}
	
	private void deleteSelectedRow(){
		this.getItems().remove(this.getSelectionModel().getSelectedItem());
		if(this.getItems().isEmpty()){
			addEmptyTableRow();
		}
		this.setItems(getItems());
	}
	/**
	 * Fügt eine leere Zeile in eine Tabelle ein 
	 */
	public abstract void addEmptyTableRow();
}
