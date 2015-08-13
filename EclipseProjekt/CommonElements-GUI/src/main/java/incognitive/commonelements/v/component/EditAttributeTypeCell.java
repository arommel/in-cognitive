package incognitive.commonelements.v.component;

import incognitive.commonelements.mc.model.DataAttributType;
import incognitive.commonelements.v.component.properties.DataAttributeTableProperties;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditAttributeTypeCell extends TableCell<DataAttributeTableProperties, DataAttributType>{

	private ObservableList<DataAttributType> types = 
			FXCollections.observableArrayList(DataAttributType.values());
	
	private ComboBox<DataAttributType> comboBox;
		
	@Override
    public void startEdit() {
    	 super.startEdit();
        if (comboBox == null) {
            createCombobox();
        }
        setText(null);
        setGraphic(comboBox);
    }

    private void createCombobox() {
    	comboBox = new ComboBox<DataAttributType>(types);
    	comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
    	comboBox.setValue(DataAttributType.String);
    	comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
	            @SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
	            public void handle(KeyEvent t) {
	                if (t.getCode() == KeyCode.ENTER) {
	                    commitEdit(comboBox.getValue());
	                } else if (t.getCode() == KeyCode.ESCAPE) {
	                    cancelEdit();
	                } else if (t.getCode() == KeyCode.TAB) {
	                    TableColumn lastColumn = getTableView().getColumns().get(getTableView().getColumns().size() - 1);
	                    getTableView().edit(getTableRow().getIndex(), lastColumn);
	                }
	            }
	        });
    	
		comboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (!newValue && comboBox != null) {
					commitEdit(comboBox.getValue());
				}
			}
		});
    }

	@Override
    public void cancelEdit() {
        super.cancelEdit();

        if(comboBox.getValue() != null){
        	setText(comboBox.getValue().name());
        }
        setGraphic(null);
    }
	

    @Override
    public void updateItem(DataAttributType item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                	comboBox.setValue(item);
                }
                setText(null);
                setGraphic(comboBox);
            } else if (item == null) {
            	 setText(null);
                 setGraphic(null);
            } else {
            	 setText(item.name());
                 setGraphic(null);
            }
        }
        
    }

    

	
}
