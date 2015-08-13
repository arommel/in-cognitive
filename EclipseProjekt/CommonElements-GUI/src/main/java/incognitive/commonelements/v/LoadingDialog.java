package incognitive.commonelements.v;

import incognitive.commonelements.v.entitydialog.DialogAction;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * 
 * Klasse zum Anzeigen eines Ladendialogs
 *
 */
public class LoadingDialog extends Stage {
	/**
	 * Erstellt einen Ladendialog aus der übergebenen Progressbar, dem Text und der Abbruchaktion
	 * @param pBar
	 * @param title
	 * @param text
	 * @param cancel
	 */
	public LoadingDialog(ProgressBar pBar, String title, String text, DialogAction cancel ){
		BorderPane bPane = new BorderPane();
		bPane.setCenter(pBar);
		HBox hBox = new HBox(30);
		hBox.setPadding(new Insets(20,10,10,10));
		hBox.setAlignment(Pos.CENTER);
		Label explanation = new Label(text);
		hBox.getChildren().add(explanation);
		bPane.setTop(hBox);
		Scene scene = new Scene(bPane, 320, 200);
		scene.getStylesheets().add("dialog.css");
		this.setTitle(title);
		this.setScene(scene);
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	 cancel.perform();
	        	 
	             LoadingDialog.this.close();
	             
	          }
	      });
		this.show();
	}
	
}
