package incognitive.mainframework.v;

import incognitive.commonelements.v.icons.IconResource;

import java.io.File;
import java.lang.management.ManagementFactory;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
/**
 * Klasse zum Erstellen des Startbildschirms und zum Aufrufen des Programmes
 * @author Sarah Richter
 *
 */
public class Main extends Application {

	  private static Stage mainStage;
	  private static String[] arguments;
	  private Timeline timeline;
	  private Pane splashLayout;
	  private ProgressBar progressBar;
	  private Label label;
	  private static final int WIDTH = 500;
	  private static final int HEIGHT = 200;
	  private static final String FOLDER = "Icons/";
	
	public static void main(String[] args) throws Exception {
		arguments = args;
		launch(args);
	}

	public void init() {
		//Logo des Startbildschirms einbinden
	    ImageView splash = new ImageView(new Image(ClassLoader.getSystemResource(FOLDER +"Splash-Screen.png").toString())); 
	    
	    progressBar = new ProgressBar(); 										//Ladebalken erstellen
	    progressBar.setPrefWidth(WIDTH-38); 									//Größe des Ladebalken
	    
	    label = new Label("Programm wird gestartet . . ."); 					// Label erstellen
	    splashLayout = new VBox(); 												//VBox erstellen
	    splashLayout.getChildren().addAll(splash, progressBar, label); 			//Logo, Ladebalken und Label der VBox hinzufügen
	    label.setAlignment(Pos.CENTER);  										//Label mittig anordnen
	  }
	
	public static void restart() throws Exception{
		
		StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(Main.class.getName()).append(" ");
        for (String arg : arguments) {
            cmd.append(arg).append(" ");
        }
        Runtime.getRuntime().exec(cmd.toString());
        System.exit(0);
        
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		showSplash(stage);
		
		timeline = new Timeline();										//Timeline erstellen
		//Zeit einstellen wie lange Startbildschirm gezeigt wird
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() { 

			@Override
			public void handle(ActionEvent event) {
				if (stage.isShowing()) {
			          progressBar.progressProperty().unbind();
			          progressBar.setProgress(1);
			          stage.toFront(); 											//Startbildschirm in den Vordergrund stellen
			          //Weicher Übergang (verblassen des Startbildschirms innerhalb von einer Sekunde)
			          FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1), splashLayout);  
			          fadeSplash.setFromValue(1.0);
			          fadeSplash.setToValue(0.0);
			          fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			            @Override public void handle(ActionEvent actionEvent) {
			              stage.hide(); 										//Startbildschirm verbergen
			              showMainStage(); 										//Methode zum Programmfenster anzeigen
			            }
			          });
			          fadeSplash.play();
			        }
			}
		}));
		timeline.play();
	}
	    	
	private void showMainStage() { 												//Programmfenster starten
		mainStage = new Stage();
		final BorderPane mainWindow = new Mainwindow();
        Scene scene = new Scene(mainWindow, 800, 600);
        scene.getStylesheets().add("application.css");
        mainStage.setScene(scene);
        mainStage.setMinWidth(690);
        mainStage.setMinHeight(600);
        mainStage.setTitle("In-cognitive");
        mainStage.getIcons().add(IconResource.LOGO);
        mainStage.show();
	}

	private void showSplash(Stage stage) {
		Scene splashScene = new Scene(splashLayout);
	    stage.initStyle(StageStyle.UNDECORATED);
	    final Rectangle2D bounds = Screen.getPrimary().getBounds();
	    stage.setScene(splashScene);
	    stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - WIDTH / 2);
	    stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - HEIGHT / 2);
	    stage.show();
	}

}