package incognitive.commonelements.v.entitydialog.deletedialog;

import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.entitydialog.DialogAction;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DataRelationDelete extends Alert{
	
	private DialogAction deleteAction;
	
	private final DataRelation relation;
	
	private final DataRelationDao relationDao;


	public DataRelationDelete(final DataRelation relation) {
		super(AlertType.CONFIRMATION);
		
		this.relation = relation;
		this.relationDao = DataRelationDao.getInstance();
		
		this.setTitle("Relation Löschen");
		this.setHeaderText("Löschen einer Relation");
		this.setContentText("Wollen Sie die Relation wirklich Löschen?");
		this.getDialogPane().getStylesheets().add("dialog.css");
	}
	
	/** Zeigt den Dialog an */
	public void showDialog(){
		final Optional<ButtonType> result = this.showAndWait();
		if(result.get() == ButtonType.OK){
			try {
				this.relationDao.delete(relation);
				this.deleteAction.perform();
			} catch (Exception e) {
				new ErrorAlert("beim Löschen", "Relation konnte nicht aus der Datei gelöscht werden!");
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
	public DataRelationDelete withDeleteAction(final DialogAction action){
		this.deleteAction = action;
		return this;
	}

}
