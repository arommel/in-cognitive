package incognitive.commonelements.v.component;

import incognitive.commonelements.v.component.properties.TableColumnProperties;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

class EditingTextCell extends  TableCell<TableColumnProperties, String> {

	private final static int TOOLTP_WIDTH = 350;
	
	 private TextField textField;
	    public EditingTextCell() {
	    }
	    @Override
	    public void startEdit() {
	        super.startEdit();
	        if (textField == null) {
	            createTextField();
	        }
	        setGraphic(textField);
	        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	                textField.requestFocus();
	                textField.selectAll();
	            }
	        });
	    }
	    @Override
	    public void cancelEdit() {
	        super.cancelEdit();
	        setText((String) getItem());
	        setContentDisplay(ContentDisplay.TEXT_ONLY);
	    }
	    @Override
	    public void updateItem(String item, boolean empty) {
	        super.updateItem(item, empty);
	        if (empty) {
	            setText(null);
	            setGraphic(null);
	        } else {
	            if (isEditing()) {
	                if (textField != null) {
	                    textField.setText(getString());
	                }
	                setGraphic(textField);
	                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	            } else {
	                setText(getString());
	                setContentDisplay(ContentDisplay.TEXT_ONLY);
	            }
	        }
	        makeToolTip(item);
	    }
	    private void makeToolTip(String item) {
	    	Tooltip tip = new Tooltip(item);
	  		tip.setPrefWidth(TOOLTP_WIDTH);
	  		tip.setWrapText(true);
	        Tooltip.install(this, tip);

		}
		private void createTextField() {
	        textField = new TextField(getString());
	        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
	       
	        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	            @SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
	            public void handle(KeyEvent t) {
	            	TableColumn currentCol = getTableColumn();
	                TableColumn lastColumn = getTableView().getColumns().get(getTableView().getColumns().size() - 1);
	                
	                if (t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.TAB) {
	                    commitEdit(textField.getText());
	                   	                    
	                    if (t.getCode() == KeyCode.TAB && !currentCol.equals(lastColumn)) {
	                    	TableColumn nextColumn = getTableView().getColumns().get(getTableView().getColumns().size() - 2);
	                        getTableView().edit(getTableRow().getIndex(), nextColumn);
	                    }
	                    
	                    if(currentCol.equals(lastColumn)){
		                	int dataSize = getTableView().getItems().size();
		                	if(dataSize == (getTableRow().getIndex()+1)){
		                		((EditableTable)getTableView()).addEmptyTableRow();	
		                	}
		                }
	                } else if (t.getCode() == KeyCode.ESCAPE) {
	                    cancelEdit();
	                } 
	            }
	        });
	        
//	       
	        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        	TableColumn<TableColumnProperties, String> currentCol = getTableColumn();
                TableColumn<TableColumnProperties, ?> lastColumn = getTableView().getColumns().get(getTableView().getColumns().size() - 1);
	            @SuppressWarnings("rawtypes")
				@Override
	            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
	                if (!newValue && textField != null) {
	                	commitEdit(textField.getText());
	                	if(currentCol.equals(lastColumn)){
		                	int dataSize = getTableView().getItems().size();
		                	if(dataSize == (getTableRow().getIndex()+1)){
		                		((EditableTable)getTableView()).addEmptyTableRow();		                	
		                	}
		                } 
	                }
	            }
	        });
	    }

	    private String getString() {
	    	if(getItem() == null){
	    		return "";
	    	} else {
	    		return getItem().toString();
	    	}
	    }
	    
	   
}
