package incognitive.commonelements.v.component;

import incognitive.commonelements.mc.model.DataAttributType;
import incognitive.commonelements.v.component.properties.DataAttributeTableProperties;

import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DataAttributeTable extends EditableTable<DataAttributeTableProperties> {

	private final int dialogWidth;
	
	private final ObservableList<DataAttributeTableProperties> data =  FXCollections.observableArrayList(
			 new DataAttributeTableProperties("", DataAttributType.String, ""));
	
	public DataAttributeTable(int dialogWidth) {
		super();
		this.dialogWidth = dialogWidth;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void init(){
				
		//Cellfactory für Textfield Zellen
        Callback<TableColumn, TableCell> cellFactory = (column) -> {
                return new EditingTextCell();
        };
        
        TableColumn attributNameCol = new TableColumn("Attributname");
        attributNameCol.setMinWidth((dialogWidth - 20) / 2.5);
        attributNameCol.setCellValueFactory(new PropertyValueFactory<DataAttributeTableProperties, String>("attributName"));
        attributNameCol.setCellFactory(cellFactory);
        
        TableColumn attributValueCol = new TableColumn("Attributwert");
        attributValueCol.setMinWidth((dialogWidth - 20) / 2.5);
        attributValueCol.setCellValueFactory(new PropertyValueFactory<DataAttributeTableProperties, String>("attributValue"));
        attributValueCol.setCellFactory(cellFactory);
       
        TableColumn typeCol = new TableColumn("Typ");
    	typeCol.setMinWidth((dialogWidth - 20) / 6);
    	typeCol.setCellValueFactory(new PropertyValueFactory<DataAttributeTableProperties, DataAttributType>("attributTyp"));
        typeCol.setCellFactory((column) -> {
            return new EditAttributeTypeCell();
        });

        this.setItems(data);
        this.getColumns().addAll(attributNameCol, typeCol, attributValueCol);
        this.setEditable(true);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
  
        attributNameCol.setOnEditCommit(new EventHandler<CellEditEvent<DataAttributeTableProperties, String>>() {
            @Override
            public void handle(CellEditEvent<DataAttributeTableProperties, String> t) {
                ((DataAttributeTableProperties) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAttributName(t.getNewValue());
            }
        });
        
        typeCol.setOnEditCommit(new EventHandler<CellEditEvent<DataAttributeTableProperties, DataAttributType>>() {
            @Override
            public void handle(CellEditEvent<DataAttributeTableProperties, DataAttributType> t) {
                ((DataAttributeTableProperties) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAttributTyp(t.getNewValue());
            }
        });
        
        attributValueCol.setOnEditCommit(new EventHandler<CellEditEvent<DataAttributeTableProperties, String>>() {
            @Override
            public void handle(CellEditEvent<DataAttributeTableProperties, String> t) {
                ((DataAttributeTableProperties) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAttributValue(t.getNewValue());
            }
        });		
	}
	
	public void fillTable(final Map<String, Object> attributeMap) {
		clearTable();
		for (Entry<String,Object> entry : attributeMap.entrySet()) {
			final Object value = entry.getValue();
			final DataAttributType type;
			if(value instanceof Double){
				type = DataAttributType.Double;
			} else if (value instanceof Integer){
				type = DataAttributType.Integer;
			} else {
				type = DataAttributType.String;
			}
			
			DataAttributeTableProperties attribut 
				= new DataAttributeTableProperties(entry.getKey(),type, value.toString());
			this.data.add(attribut);
		}
		//neue Zeile
		this.data.add(new DataAttributeTableProperties("", DataAttributType.String, ""));
		this.setItems(data);
	}
	
	public void clearTable() {
		data.clear();
	}

	@Override
	public void addEmptyTableRow() {
		this.getItems().add(new DataAttributeTableProperties("", DataAttributType.String, ""));	
	}
	
	/**
	 * Checkt ob das Attribut name, typ und value vorhanden sind , sowie ob value zum typ passt
	 * @param attribute
	 * @return
	 */
	public boolean isCompletly(final DataAttributeTableProperties attribute){
		
		if(attribute.getAttributName() == null || attribute.getAttributName().equals("")){
			return false;
		}
		
		if(attribute.getAttributTyp() == null){
			return false;
		}
		
		if(attribute.getAttributValue() == null || attribute.getAttributValue().equals("")){
			return false;
		}
		
		return true;
	}
	
	public boolean isTypeRight(final DataAttributeTableProperties attribute){

		try {
			switch (attribute.getAttributTyp() ) {
				case Integer :
					Integer.valueOf(attribute.getAttributValue());//Cast probieren
					break;

				case Double :
					Double.valueOf(attribute.getAttributValue());//Cast probieren
					break;

				default :
					break;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
}
