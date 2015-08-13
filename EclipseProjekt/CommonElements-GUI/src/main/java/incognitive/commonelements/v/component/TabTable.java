package incognitive.commonelements.v.component;

import incognitive.commonelements.v.component.properties.TagTableProperty;

import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class TabTable extends EditableTable<TagTableProperty> {
	
	private final ObservableList<TagTableProperty> data =  FXCollections.observableArrayList(
			 new TagTableProperty(""));

	public TabTable() {
		init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init() {
		
		//Cellfactory für Textfield Zellen
        Callback<TableColumn, TableCell> cellFactory = (column) -> {
                return new EditingTextCell();
        };
        
        TableColumn attributNameCol = new TableColumn("Tag");
        attributNameCol.setCellValueFactory(new PropertyValueFactory<TagTableProperty, String>("tag"));
        attributNameCol.setCellFactory(cellFactory);
        
        this.setItems(data);
        this.getColumns().addAll(attributNameCol);
        this.setEditable(true);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setPlaceholder(new Label("Keine Tags in dieser Kategorie"));
  
        attributNameCol.setOnEditCommit(new EventHandler<CellEditEvent<TagTableProperty, String>>() {
            @Override
            public void handle(CellEditEvent<TagTableProperty, String> t) {
                ((TagTableProperty) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTag(t.getNewValue());
            }
        });  
	}

	@Override
	public void addEmptyTableRow() {
		this.data.add(new TagTableProperty(""));
	}
	
	public void fillTable(final HashSet<String> tagSet) {
		clearTable();
		for (String tag : tagSet) {
			this.data.add(new TagTableProperty(tag));
		}
		this.setItems(data);
	}
	
	public void clearTable() {
		data.clear();
	}
	
	public HashSet<String> getTagSet(){
		final HashSet<String> tags = new HashSet<String>();
		for(TagTableProperty tagProperty : data){
			final String tag = tagProperty.getTag();
			if (!tag.equals("")){
				tags.add(tag);
			}
		}
		
		return tags;
	}

}
