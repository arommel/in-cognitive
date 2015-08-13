package incognitive.commonelements.v.entitydialog.deletedialog;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.entitydialog.DialogAction;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class DataCategoryDelete extends Alert {
	
	/**Kategorie löschen Aktion*/
	private DialogAction normalSaveAction;
	/**Im falle es werden alle Objekte gelöscht*/
	private DialogAction additionalSaveAction;
	
	private final DataCategory category;
	
	private final DataCategoryDao categoryDao;
	
	private final DataObjectDao objectDao;
	
	private final CommonElementsController controller;
	
	ButtonType deleteButton = new ButtonType("Nur Kategorie löschen");
	ButtonType deleteObjectButton = new ButtonType("Kategorie u. Objekte löschen");
	ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

	public DataCategoryDelete(final DataCategory category) {
		super(AlertType.CONFIRMATION);
		
		this.category = category;
		
		this.controller = CommonElementsController.getInstance();
		this.categoryDao = DataCategoryDao.getInstance();
		this.objectDao = DataObjectDao.getInstance();
		
		this.setTitle("Kategorie Löschen");
		this.setHeaderText("Löschen einer Kategorie");
		this.setContentText("Wollen Sie die Kategorie wirklich Löschen?\n"
				+ "Alle Beziehungen zu Relationen werden ebenfalls gelöscht!");
		
		this.getButtonTypes().setAll(deleteButton, deleteObjectButton, cancelButton);
		this.getDialogPane().getStylesheets().add("dialog.css");
	}
	
	/**
	 * Aktion die ausgeführt wird wenn nur Kategorie gelöscht wird
	 * 
	 * @param action Aktion
	 * 
	 * @return Dialog
	 */
	public DataCategoryDelete withNormalSaveAction(final DialogAction action){
		this.normalSaveAction = action;
		return this;
	}
	
	/**
	 * Aktion die ausgeführt wird wenn auch die Objekte gelöscht werden
	 * 
	 * @param action Aktion
	 * 
	 * @return Dialog
	 */
	public DataCategoryDelete withAdditionalSaveAction(final DialogAction action){
		this.additionalSaveAction = action;
		return this;
	}

	/** Zeigt den Dialog an */
	public void showDialog(){
		final Optional<ButtonType> result = this.showAndWait();
		if(result.get() == deleteButton){
			try {
				this.categoryDao.delete(category);
				this.normalSaveAction.perform();
			} catch (Exception e) {
				new ErrorAlert("beim Löschen", "Kategorie konnte nicht aus der Datei gelöscht werden!");
			}
		} else if (result.get()== deleteObjectButton){
			try {
				List<DataObject> objectList = controller.getObjectsToCategory(category.getId());
				for(DataObject o : objectList){
					this.objectDao.delete(o);
				}
				this.categoryDao.delete(category);
				this.additionalSaveAction.perform();
				this.normalSaveAction.perform();
				
			} catch (Exception e) {
				new ErrorAlert("beim Löschen", "Kategorie konnte nicht aus der Datei gelöscht werden!");
			}
			
		}
	}
}
