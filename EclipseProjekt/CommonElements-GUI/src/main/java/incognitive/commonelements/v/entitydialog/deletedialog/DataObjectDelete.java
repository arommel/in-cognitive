package incognitive.commonelements.v.entitydialog.deletedialog;

import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.entitydialog.DialogAction;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DataObjectDelete extends Alert{
	
	private DialogAction deleteAction;
	
	private final DataObject object;
	
	private final DataObjectDao objectDao;

	public DataObjectDelete(final DataObject object) {
		super(AlertType.CONFIRMATION);
		
		this.object = object;
		this.objectDao = DataObjectDao.getInstance();
		
		this.setTitle("Objekt Löschen");
		this.setHeaderText("Löschen eines Objektes");
		this.setContentText("Wollen Sie das Objekt wirklich Löschen? \n"
				+ "Alle Beziehungen zu Kategorien und Relationen werden ebenfalls gelöscht!");
		this.getDialogPane().getStylesheets().add("dialog.css");
	}
	
	/** Zeigt den Dialog an */
	public void showDialog(){
		final Optional<ButtonType> result = this.showAndWait();
		if(result.get() == ButtonType.OK){
			try {
				this.objectDao.delete(object);
				this.deleteAction.perform();
			} catch (Exception e) {
				new ErrorAlert("beim Löschen", "Objekt konnte nicht aus der Datei gelöscht werden!");
			}
		}
	}
	
	/**
	 * Setzt die Aktion die nach dem Löschen aufgerufen wird.
	 * 
	 * @param action Aktion
	 * 
	 * @return Dialog
	 */
	public DataObjectDelete withDeleteAction(final DialogAction action){
		this.deleteAction = action;
		return this;
	}
}
