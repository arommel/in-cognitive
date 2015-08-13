package incognitive.commonelements.v.entitydialog;

import incognitive.database.model.AbstractEntity;

import java.util.HashMap;

import javafx.stage.Stage;

public class DialogHandler {

	private static DialogHandler instance;

	private HashMap<AbstractEntity, SaveCancelDialog> tempDialogMap;
	private static SaveCancelDialog newObjectDialog;
	private static SaveCancelDialog newCategoryDialog;
	private static SaveCancelDialog newRelationDialog;
	private static Stage newHelpDialog;
	private static Stage newImpressumDialog;
	

	private DialogHandler(){
		tempDialogMap = new HashMap<AbstractEntity, SaveCancelDialog>();
		
	}
	
	public static DialogHandler getInstance () {
	    if (DialogHandler.instance == null) {
	    	DialogHandler.instance = new DialogHandler();
	    }
	    return DialogHandler.instance;
	}

	public HashMap<AbstractEntity, SaveCancelDialog> getTempDialogMap() {
		return tempDialogMap;
	}

	public static boolean isObjectDialogOpen(){
		if (newObjectDialog == null){
			return false;
		}
		else return true;
	}
	
	public static boolean isCategoryDialogOpen(){
		if (newCategoryDialog == null){
			return false;
		}
		else return true;
	}
	
	public static boolean isRelationDialogOpen(){
		if (newRelationDialog == null){
			return false;
		}
		else return true;
	}
	
	public static boolean isHelpDialogOpen(){
		if (newHelpDialog == null){
			return false;
		}
		else return true;
	}
	
	public static boolean isImpressumDialogOpen(){
		if (newImpressumDialog == null){
			return false;
		}
		else return true;
	}

	public static SaveCancelDialog getNewObjectDialog() {
		return newObjectDialog;
	}

	public static void setNewObjectDialog(SaveCancelDialog newObjectDialog) {
		DialogHandler.newObjectDialog = newObjectDialog;
		
	}

	public static SaveCancelDialog getNewCategoryDialog() {
		return newCategoryDialog;
	}
	
	public static void setNewCategoryDialog(SaveCancelDialog newCategoryDialog) {
		DialogHandler.newCategoryDialog = newCategoryDialog;
	}

	public static SaveCancelDialog getNewRelationDialog() {
		return newRelationDialog;
	}

	public static void setNewRelationDialog(SaveCancelDialog newRelationDialog) {
		DialogHandler.newRelationDialog = newRelationDialog;
	}
	
	public static Stage getNewHelpDialog() {
		return newHelpDialog;
	}

	public static void setNewHelpDialog(Stage newHelpDialog) {
		DialogHandler.newHelpDialog = newHelpDialog;
	}
	
	public static Stage getNewImpressumDialog() {
		return newImpressumDialog;
	}

	public static void setNewImpressumDialog(Stage newImpressumDialog) {
		DialogHandler.newImpressumDialog = newImpressumDialog;
	}

}
