package incognitive.commonelements.v;




import incognitive.commonelements.mc.control.ImportWebController;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.entitydialog.DialogAction;
import incognitive.commonelements.v.icons.IconResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * GUI Klasse für das Importieren von Daten
 * @author christoph pförtner
 * @version 1.0
 * 
 *
 */
public class ImportView extends Stage {
	private javafx.scene.control.TextArea loadedText;
	private TextField fileUrl, webUrl;
	private int FONT_SIZE = 15;
	private Label label;
	private Label inputLabel = new Label("Eingabetyp : ");
	private Label urlLabel = new Label ("URL hinzufügen : ");
	private Label fileLabel = new Label ("Datei hinzufügen : ");
	private Scene scene;
	private Button parseButton;
	private Button fileInputBtn, urlInputBtn;
	private ImportWebController controller;
	private DialogAction saveAction;
	private DataObject dataObject;
	private ButtonType saveButton = new ButtonType("Speichern");
	private ButtonType editButton = new ButtonType("Bearbeiten");
	private ButtonType categoryButton = new ButtonType("Kategorie vorschläge");
	private ButtonType cancelButton = new ButtonType("Abbrechen");
	private ComboBox selectInputType = new ComboBox();
	private File file;
	private ArrayList<String> possibleCategories, selectCategories;
	private Stage stage = null;
	private Thread thread;
	private BorderPane rootLayout;
	
	public ImportView(DataObject dataObject){
		this.dataObject = dataObject;
		this.rootLayout = new BorderPane();
		
		initGUI();

		final Scene dialogScene = new Scene(rootLayout,400, 300);
		dialogScene.getStylesheets().add("dialog.css");
		this.setScene(dialogScene);
		this.getIcons().add(IconResource.LOGO);
		this.setTitle("Daten importieren");
		this.show();
	}
	
	@SuppressWarnings("restriction")
	/**
	 * Erstellt und ordnet die Elemente auf der Stage an
	 */
	public void initGUI(){
		
    	selectInputType.getItems().addAll("URL", "Datei", "(Quell)text");
    	selectInputType.setPromptText("Bitte Eingabetyp wählen");
    	selectInputType.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
               if(t1 == "(Quell)text"){
            	 	   setSourceCodeLayout();
               }else if(t1 == "URL"){
            	   setUrlLayout();
               }else if(t1 == "Datei"){
            	   setFileLayout();
               }
            }    
        });
//
//        setSourceCodeLayout();
		fileUrl = new TextField();
		fileUrl.setPrefWidth(180);
		webUrl = new TextField();
		webUrl.setPrefWidth(180);
		label = new Label("Kopierten (Quell)text hier einfügen.");
		
		loadedText = new javafx.scene.control.TextArea();
		loadedText.setPromptText("Bitte (Quell)text hier einfügen");
		parseButton = new Button("Importieren");
		parseButton.setDisable(true);
		parseButton.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				
				ImportView.this.controller = new ImportWebController(dataObject);
				try {
					String text = loadedText.getText();
					if(text.contains("</html>")){
						ImportView.this.dataObject = controller.parseHtml(loadedText.getText());
						if(dataObject.getName().length() != 0){
							ImportView.this.possibleCategories = controller.getPossibleCategories();
							initSimilarityFinder();
						}else{
							showInfoDialog("Keine Wikipediaseite", "Zurzeit werden leider nur Wikipediaseiten unterstützt", AlertType.INFORMATION);
						}
					}else{
						// Freier Text
						TextImport txtImport = new TextImport(ImportView.this.loadedText.getText());
						txtImport.setImportView(ImportView.this);
						txtImport.withSaveAction(saveAction);
						txtImport.show();
					}
					
					
					
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			

			
		});
		
		loadedText.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				parseButton.setDisable(false);
			}
		});
		
		
		fileInputBtn = new Button("Datei auswählen");
		fileInputBtn.setPrefWidth(180);
		fileInputBtn.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Datei auswählen");
				File file = fileChooser.showOpenDialog(null);
				fileUrl.setText(file.getAbsolutePath());
				loadedText.setText(readFile(file));
			}
			
		});
		
		urlInputBtn = new Button("Seitenquelltext laden");
		urlInputBtn.setPrefWidth(180);
		urlInputBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Document doc = null;	
				try {
					
					URL url = new URL(webUrl.getText());
				    URLConnection conn = url.openConnection();
				    conn.connect();
				    doc = Jsoup.connect(webUrl.getText()).get();
				    
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					showInfoDialog("Keine gültige Url", "Die Seite konnte nicht geladen werden.", AlertType.ERROR);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
				}
				if(doc != null){
					loadedText.setText(doc.html());
				}
			}
		});
		
//		inputLabel.setFont(new Font(F	private ArrayList<String> possibleCategories, selectCategories;ONT_SIZE));
		VBox selectBox = new VBox();
		selectBox.getChildren().addAll(inputLabel,selectInputType);
		selectBox.setPadding(new Insets(20,0,0,20));
		selectBox.setSpacing(10);
		rootLayout.setTop(selectBox);
		
//		HBox selectBox = new HBox(30);
//		selectBox.setPadding(new Insets(20,10,10,10));
//		selectBox.setAlignment(Pos.CENTER);
//		selectBox.getChildren().add(selectInputType);
//		this.setTop(selectBox);
		
		
		
//		HBox lblBox = new HBox(30);
//		lblBox.setPadding(new Insets(20,10,10,10));
//		lblBox.setAlignment(Pos.CENTER);
//		lblBox.getChildren().add(label);
//		this.setTop(lblBox);
		
		HBox hbBtn = new HBox(10);
		hbBtn.setPadding(new Insets(10,10,10,10));
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(parseButton);
		rootLayout.setBottom(hbBtn);
		
		
		
	}
	
	/**
	 * Zeigt einen InfoDialog mit dem Übergebenen Title, Header und AlertType
	 * @param title
	 * @param header
	 * @param type
	 */
	private void showInfoDialog(String title, String header, AlertType type) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.getDialogPane().getStylesheets().add("dialog.css");
		alert.showAndWait();
	}
	
	/**
	 * Speichert die Saveaction als Attribut
	 * @param action
	 */
	public void withSaveAction(DialogAction action){
		this.saveAction = action;
	}
	
	
	/**
	 * Veranlasst das Parsen der eingebenen Daten und kümmert sich um die weitere Verarbeitung des zurückgegebenes DataObjects
	 * @param dataObject
	 */
	private void validate() {
		// TODO Auto-generated method stub
		if(dataObject != null){
			ImportNotification importNotification = new ImportNotification(dataObject, possibleCategories, this.controller.getSimilarityFinder().getCategoryWithMatches());
			importNotification.withSaveAction(saveAction);
			importNotification.setImportView(this);
			importNotification.show();		
		}
		
	}
	
	/**
	 * Zeigt das Layout für den Import einer Datei an
	 */
	private void setFileLayout() {
		// TODO Auto-generated method stub
		
		loadedText.setPromptText(" ");
		loadedText.setEditable(false);
		
		HBox file = new HBox();
		file.getChildren().addAll(fileUrl, fileInputBtn);
		file.setPadding(new Insets(10,0,20,0));
		file.setSpacing(20);
		
		
		VBox all = new VBox();
		all.getChildren().addAll(fileLabel,file, loadedText);
		all.setPadding(new Insets(10,20,20,20));
		rootLayout.setCenter(all);
		
		 if (loadedText.getText().equals("")){
			 parseButton.setDisable(true);
		 } else if (loadedText.getText() != null) {
			 parseButton.setDisable(false);
		 }

	}
	
	/**
	 * Zeigt das Layout für den Import einer URL an
	 */
	private void setUrlLayout() {
		// TODO Auto-generated method stub
				
		webUrl.setPromptText("Hier den Link einfügen");
		loadedText.setPromptText(" ");
		loadedText.setEditable(false);
		
		HBox url = new HBox();
		url.getChildren().addAll(webUrl, urlInputBtn);
		url.setPadding(new Insets(10,0,20,0));
		url.setSpacing(20);
		
		
		VBox all = new VBox();
		all.getChildren().addAll(urlLabel, url, loadedText);
		all.setPadding(new Insets(10,20,20,20));
		rootLayout.setCenter(all);
		
//		if (loadedText.getText().equals("")){
//			 parseButton.setDisable(true);
//		 } else {
//			 parseButton.setDisable(false);
//		 }

		
	}
	
	/**
	 * Zeigt das Layout für den Import von Text bzw. Quelltext
	 */
	private void setSourceCodeLayout() {
		// TODO Auto-generated method stub
		loadedText.setPromptText("Quelltext hier einfügen");
		loadedText.setEditable(true);
		HBox txt = new HBox(18);
		txt.setPadding(new Insets(10,10,10,10));
		txt.setAlignment(Pos.TOP_CENTER);
		txt.getChildren().add(loadedText);
		rootLayout.setCenter(txt);
	}
	
	/**
	 * Liest das ausgewählte File ein und gibt den enthaltenen Text zurück
	 * @param file
	 * @return Text aus Datei vom Typ String
	 */
	private String readFile(File file){
		BufferedReader in;
		String buf = new String();
		try {
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while(line != null){
			  buf += line + "\n";
			  line = in.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			buf = "Datei nicht gefunden";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			buf = "Kein Inhalt in Datei gefunden";
		}
		
		
		
		return buf;
	}


	
	/**
	 * Gibt den ImportWebcontroller zurück
	 * @return
	 */
	public ImportWebController getController() {
		return controller;
	}
	
	/**
	 * Setzt den ImportWebController
	 * @param controller
	 */
	public void setController(ImportWebController controller) {
		this.controller = controller;
	}


	/**
	 * Gibt das DatenObjekt zurück
	 * @return DataObject
	 */
	public DataObject getDataObject() {
		return dataObject;
	}

	/**
	 * Setzt das DatenObjekt
	 * @param dataObject
	 */
	public void setDataObject(DataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	
	/**
	 * Initialisiert den SimlarityFinder und führt den Task zum Finden von ähnlichen
	 * Kategorien und zeigt während der Ausführung einen LoadingDialog an
	 */
	private void initSimilarityFinder(){
		ProgressBar pBar = new ProgressBar();
		pBar.progressProperty().bind(controller.getSimilarityFinder().getFindSimilaritys().progressProperty());
		LoadingDialog loadingDialog = new LoadingDialog(pBar,"Die Daten werden ausgewertet","Bitte haben Sie einen Moment Geduld.",
				() -> {controller.getSimilarityFinder().getFindSimilaritys().cancel();});
		thread = new Thread(controller.getSimilarityFinder().getFindSimilaritys());
		thread.start();
		controller.getSimilarityFinder().getFindSimilaritys().setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                loadingDialog.close();
                validate();
            }
        });
	}
}