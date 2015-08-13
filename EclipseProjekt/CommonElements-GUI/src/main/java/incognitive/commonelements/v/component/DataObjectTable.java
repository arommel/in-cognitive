package incognitive.commonelements.v.component;

import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.component.properties.DataObjectTableProperty;

import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DataObjectTable extends EditableTable<DataObjectTableProperty>{

	private final int dialogWidth;
	
	private final ObservableList<DataObjectTableProperty> data =  FXCollections.observableArrayList();
	
	
	public DataObjectTable(int dialogWidth) {
		super();
		this.dialogWidth = dialogWidth;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void init(){
		
        TableColumn objectNameCol = new TableColumn("Objekte");
        objectNameCol.setMinWidth(dialogWidth);
        objectNameCol.setCellValueFactory(new PropertyValueFactory<DataObjectTableProperty, String>("objectProperty"));
        
        this.setItems(data);
        this.getColumns().addAll(objectNameCol);
        this.setEditable(true);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.setPlaceholder(new Label("Keine Objekte in dieser Kategorie"));
        
        objectNameCol.setOnEditCommit(new EventHandler<CellEditEvent<DataObjectTableProperty, DataObject>>() {
            public void handle(CellEditEvent<DataObjectTableProperty, DataObject> t) {
                ((DataObjectTableProperty) t.getTableView().getItems().get(t.getTablePosition().getRow())).setObjectName(t.getNewValue());
            }
        });
        
        objectNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataObjectTableProperty,String>,ObservableValue<String>>(){

			public ObservableValue<String> call(CellDataFeatures<DataObjectTableProperty, String> arg0) {
				return new ReadOnlyStringWrapper(arg0.getValue().getObject().getName());
			}
			
		});
	}
	
	public void fillTable(final List<DataObject> objectList) {
		clearTable();
		for (DataObject object : objectList) {
			DataObjectTableProperty objectProperty 
				= new DataObjectTableProperty(object);
			this.data.add(objectProperty);
		}
		this.setItems(data);
	}
	
	public void addDataObject(final DataObject object){
		data.add(new DataObjectTableProperty(object));
	}
	
	public void clearTable() {
		data.clear();
	}

	@Override
	public void addEmptyTableRow() {
		//wurde nicht gebraucht
		
	}

}
