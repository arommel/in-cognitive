package incognitive.commonelements.v.entitydialog;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ApplyCancelDialog extends Stage{
	Button saveButton = new Button("Übernehmen");
	Button cancelButton = new Button("Abbrechen");
	DialogAction closeAction;
	
	private final DialogComponent dialogComponent;
	
	/**
	 * Builder der den Dialog zusammen baut
	 * 
	 * @param builder
	 */
	public ApplyCancelDialog(DialogComponent dialogComponent, String title ) {
		this.dialogComponent = dialogComponent;
		
		
		final int width = dialogComponent.getComponentWidth();
		final int height =  dialogComponent.getComponentHeight();
		

		final BorderPane rootLayout = init();
		this.setScene(new Scene(rootLayout,width, height));
		this.setTitle (title);
		this.setResizable(false);
		this.setOnHiding(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				dialogComponent.onClose();
			}
			
		});
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
				if(closeAction != null){
					closeAction.perform();
				}
				this.close();
			}
		});
		
		//Add Abbrechen Listener
		cancelButton.setOnAction((e) -> {
			this.close();
		});

		dialogLayout.setCenter((Node) dialogComponent);
		
		dialogLayout.setBottom(buttonLayout);
		
		return dialogLayout;
	}
	
	public void withCloseAction(DialogAction action){
		this.closeAction = action;
	}
}
