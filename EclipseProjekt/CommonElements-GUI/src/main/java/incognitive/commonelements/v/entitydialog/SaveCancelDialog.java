package incognitive.commonelements.v.entitydialog;


import incognitive.commonelements.v.icons.IconResource;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Ein Dialog mit Abbrechen und Speichern Button der eine DialogView darstellt.
 * 
 * @author Lisa Leuschner
 *
 */
public class SaveCancelDialog  extends Stage {
	
	Button saveButton = new Button("Speichern");
	Button cancelButton = new Button("Abbrechen");
	
	private final DialogComponent dialogComponent;
	
	private final DialogAction saveAction;

	/**
	 * Builder der den Dialog zusammen baut
	 * 
	 * @param builder
	 */
	public SaveCancelDialog(final SaveCancelDialogBuilder builder) {
		this.dialogComponent = builder.getDialogLayout();
		this.saveAction = builder.getAction();
		
		final int width = dialogComponent.getComponentWidth();
		final int height =  dialogComponent.getComponentHeight();
		final String title = builder.getTitle();

		final BorderPane rootLayout = init();
		final Scene dialogScene = new Scene(rootLayout,width, height);
		dialogScene.getStylesheets().add("dialog.css");
		this.setScene(dialogScene);
		this.setTitle (title);
		this.setResizable(false);
		this.getIcons().add(IconResource.LOGO);
		this.show();
	}
	
	private BorderPane init(){
		dialogComponent.init();
		
		//Dialog Layout
		final BorderPane dialogLayout = new BorderPane();
		
		//Button Layout
		final BorderPane buttonLayout = new BorderPane();
		buttonLayout.setPadding(new Insets(5, 10, 5, 0));
		final HBox box = new HBox();
		box.setSpacing(10);
		box.getChildren().add(saveButton);
		box.getChildren().add(cancelButton);
		buttonLayout.setRight(box);
		
		//Add Speichern Listener
		saveButton.setOnAction((e) -> {
			if(dialogComponent.validateComponent()){ 
				dialogComponent.updateModel();
				dialogComponent.save();
				saveAction.perform();
				this.close();
			}
		});
		
		this.setOnHiding(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				dialogComponent.onClose();
			}
			
		});
		
		//Add Abbrechen Listener
		cancelButton.setOnAction((e) -> {
			this.close();
		});
		
		//Setze zusammen
		dialogLayout.setCenter((Node) dialogComponent);
		
		dialogLayout.setBottom(buttonLayout);
		
		return dialogLayout;
	}
	

	

}
