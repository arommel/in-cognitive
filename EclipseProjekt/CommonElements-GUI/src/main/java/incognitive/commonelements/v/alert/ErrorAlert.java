package incognitive.commonelements.v.alert;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {

	public ErrorAlert(final String titleAdd, final String content) {
		super(AlertType.ERROR);
		
		this.setTitle("Fehler" + " " + titleAdd);
		this.setHeaderText("Fehler");
		this.setContentText(content);
		this.getDialogPane().getStylesheets().add("dialog.css");
		this.showAndWait();
	}

}
