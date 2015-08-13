package incognitive.mainframework.v;


import incognitive.commonelements.v.icons.IconResource;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Impressum extends Stage {
	private BorderPane pane = new BorderPane();
	private Label nameLabel = new Label("Entwickler :");
	private Label functionLabel = new Label("Funktionen:");
	private Text text = new Text();
	private Text text2 = new Text();
	private Text text3 = new Text();
	private TextFlow textFlow1 = new TextFlow(text);
	private TextFlow textFlow2 = new TextFlow(text2);
	private TextFlow textFlow3 = new TextFlow(text3);
	private ImageView imgView = new ImageView(IconResource.SPLASH);
	
	
	public Impressum (){
		final Scene scene = new Scene(pane ,400, 580);
		scene.getStylesheets().add("application.css");
		this.setScene(scene);
	
		imgView.setFitWidth(400);
		imgView.setFitHeight(150);
		
	
		text.setText(	  "\nIn-cognitive ermöglicht es Ihnen, große Datenmengen einfach "
    			+ 		  "zu verwalten. Sie haben die Möglichkeit, diese Daten manuell "
    			+ 		  "einzugeben, oder aber auf die Import-Funktion zurückzugreifen. "
    			+ 		  "In-cognitive ist in der Lage, Wikipediaartikel einzulesen und "
    			+ 		  "selbstständig Objekte, sowie dazugehörige Kategorien zu erkennen. "
    			+ 		  "Diese Datenmengen stellt Ihnen das Programm sowohl grafisch, "
    			+ 		  "als auch in tabellarischer Form zur Verfügung. Sie "
    			+ 		  "haben die Möglichkeit, schon bestehende Objekte, Kategorien "
    			+ 		  "bzw Relationen nachträglich zu bearbeiten. Durch die Speicherung "
    			+ 		  "in XML ist In-cognitive auf verschiedenen Systemen "
    			+ 		  "einsetzbar.");
		
		text2.setText(	  "Andreas Knappe\n"
				+ 		  "Adriano Pinnau\n"
				+ 		  "Andreea-Maria Somesan\n"
				+ 		  "Anja Rommel\n"
				+ 		  "Christoph Pförtner\n"
				+ 		  "Eric Müller");
		
		text3.setText(	  "Florian Mewes\n"
				+ 		  "Lisa Leuschner\n"
				+ 		  "Peter Hornik\n"
				+ 		  "Sarah Richter\n"
				+ 		  "Yuliya Akulova");
		
		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(functionLabel,textFlow1);
		vbox2.setSpacing(5);
		vbox2.setPadding(new Insets(0, 20, 20, 20)); 
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(imgView);
		hbox.setStyle("-fx-background-color: white");
		
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(hbox, vbox2);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(0, 0, 20, 0)); 
		pane.setTop(vbox);
		
		HBox hbox1 = new HBox();
		hbox1.setSpacing(80);
		hbox1.getChildren().addAll(textFlow2, textFlow3);
		
		VBox vbox3 = new VBox();
		vbox3.getChildren().addAll(nameLabel,hbox1);
		vbox3.setSpacing(5);
		vbox3.setPadding(new Insets(20, 20, 20, 20)); 
		pane.setCenter(vbox3);
		
		
		
		
	}
	
	
}
