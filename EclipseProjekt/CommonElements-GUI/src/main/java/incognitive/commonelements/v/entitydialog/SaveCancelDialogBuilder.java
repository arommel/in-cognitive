package incognitive.commonelements.v.entitydialog;




/**
 * Builder der mithilfe von angegbenen Daten(Titel, View, SpeicherAktion)
 *  einen Speichern/ Abbrechen Dialog baut
 * 
 * @author Lisa Leuschner
 *
 */
public class SaveCancelDialogBuilder {
	
	private DialogAction action;
	
	private String title;
	
	private DialogComponent dialogLayout;
	
	/**
	 * Baut einen Speichern Abbrechen Dialog nachdem die Daten Übergeben wurden.</br>
	 * Notwendige Angaben sind : </br>
	 *	- Title</br>
	 *	- Save Action</br>
	 *  - DialogComponent </br>
	 *
	 * 
	 * @return initialisierter Dialog mit Speichern/ Abbrechen Button
	 */
	public SaveCancelDialog build(){
		if(title == null || action == null || dialogLayout == null){
			System.err.println("Dialog konnte nicht gebaut werden. Fehlende Angaben!");
			return null;
		} else {
			return new SaveCancelDialog(this);
		}
	}
	
	public SaveCancelDialogBuilder withTitle(final String title){
		this.title = title;
		return this;
	}
	
	public SaveCancelDialogBuilder withSaveAction(final DialogAction saveAction){
		this.action = saveAction;
		return this;
	}
	
	public SaveCancelDialogBuilder withDialogComponent(final DialogComponent component){
		this.dialogLayout = component;
		return this;
	}

	public DialogAction getAction() {
		return action;
	}

	public String getTitle() {
		return title;
	}

	public DialogComponent getDialogLayout() {
		return dialogLayout;
	}

	
}
