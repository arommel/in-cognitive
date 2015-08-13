package incognitive.commonelements.v.alert;

import javafx.scene.control.Alert;

public class InformationAlert extends Alert {

	public InformationAlert(final String infoMessage) {
		super(AlertType.INFORMATION);
		
		this.setTitle("Informations Dialog");
		this.setHeaderText("Fehler");
		this.setContentText(infoMessage);
		this.getDialogPane().getStylesheets().add("dialog.css");
		this.show();
	}

}
