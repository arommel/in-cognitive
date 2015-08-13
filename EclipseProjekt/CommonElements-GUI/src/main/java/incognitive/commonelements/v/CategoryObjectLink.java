package incognitive.commonelements.v;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author christoph pförtner
 * @version 1.0
 * 
 * Klasse zum Anzeigen und Auswählen von Kategorievorschlägen
 */
public class CategoryObjectLink extends BorderPane implements EventHandler<ActionEvent> {

	
	private ListView listView;
	private Label introductionText;
	private ArrayList<String> possibleCategories, selectedItems;
	HashMap<String, Double> categoryWithMatches;
	private Button takeOverBtn, cancelBtn;
	private ImportNotification importNotification;
	private double lowestMatchShown = 0;
	private Slider matchSlider;
	private double highestMatch = 0, lowestMatch = 0;
	
	public CategoryObjectLink(ImportNotification importNotification){
		this.importNotification = importNotification;
		this.possibleCategories = new ArrayList<String>();
		this.selectedItems = importNotification.getSelectedCategories();
		this.categoryWithMatches = importNotification.getCategoryWithMatches();
		loadMatchBorders();
		listView = new ListView();
		loadPossibleCategories();
		listViewListener();
		
		initGUI();
		
	}
	
	/**
	 * Ermittelt den höchsten und den niedrigsten Matchscore
	 */
	private void loadMatchBorders(){
		if(categoryWithMatches.size() != 0){
			ArrayList<Double> matchValues = new ArrayList<Double>(categoryWithMatches.values());
			lowestMatch = matchValues.get(0);
			highestMatch = matchValues.get(0);
			for(Double matchVal : matchValues){
				if(matchVal > highestMatch){
					highestMatch = matchVal;
				}
				if(matchVal < lowestMatch){
					lowestMatch = matchVal;
				}
			}
			lowestMatchShown = (lowestMatch + highestMatch) / 2;
		}else{
			lowestMatchShown = 0;
		}
	}
	
	/**
	 * Lädt die Kategorievorschläge und die durch Similarityfinder ermittelten 
	 * Kategorievorschläge und fügt sie zu einer Liste zusammen und zeigt diese 
	 * im Listview an
	 */
	private void loadPossibleCategories(){
		
		if(importNotification.getPossibleCategories() != null){
			this.possibleCategories = new ArrayList<String>(importNotification.getPossibleCategories());
		}else{
			this.possibleCategories = new ArrayList<String>();
		}
		
		for(String categoryName : categoryWithMatches.keySet()){
			if(categoryWithMatches.get(categoryName) >= lowestMatchShown){
				if(!possibleCategories.contains(categoryName)){
					possibleCategories.add(categoryName);
				}
			}
		}
		listView.setItems(null);
		listView.setItems(FXCollections.observableList(possibleCategories));
		
	}
	
	/**
	 * Initialisiert den Listviewlistener und implementiert das Abwählen von Kategorien
	 */
	private void listViewListener() {
		// TODO Auto-generated method stub
		
		MultipleSelectionModel selectionModel = listView.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
		
		listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listView.requestFocus();
                if (! cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                    } else {
                        selectionModel.select(index);
                    }
                    event.consume();
                }
            });
            return cell ;
        });
	}
	
	/**
	 * Initialisiert die grafische Oberfläche
	 */
	private void initGUI() {
		selectedItems = new ArrayList<String>();
		takeOverBtn = new Button("Übernehmen");
		cancelBtn = new Button("Abbrechen");
		takeOverBtn.setOnAction(this);
		cancelBtn.setOnAction(this);
		Slider slider = new Slider();
		slider.setMax(highestMatch);
		slider.setMin(lowestMatch);
		slider.setValue((double) lowestMatchShown);
		
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                        CategoryObjectLink.this.lowestMatchShown = (int) Math.floor((double) new_val);
                        CategoryObjectLink.this.loadPossibleCategories();
            		}
            });
		
		
		Bindings.bindContent(selectedItems, listView.getSelectionModel().getSelectedItems());
		
		
		VBox leftVBox = new VBox(5);
		leftVBox.setPadding(new Insets(10,10,10,10));
		leftVBox.setAlignment(Pos.CENTER_LEFT);
		leftVBox.getChildren().add(takeOverBtn);
		
		VBox rightVBox = new VBox(5);
		leftVBox.setPadding(new Insets(10,10,10,10));
		leftVBox.setAlignment(Pos.CENTER_RIGHT);
		leftVBox.getChildren().add(cancelBtn);
		
		HBox hBox = new HBox(20);
		hBox.setPadding(new Insets(20,10,10,10));
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(takeOverBtn, cancelBtn);
		this.setBottom(hBox);
		
		introductionText = new Label("Sind die ausgewählten Kategorien noch nicht vorhanden, "
				+ "\n werden diese automatisch angelegt.");
		hBox = new HBox(10);
		hBox.setPadding(new Insets(20,10,10,10));
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(introductionText);
		this.setTop(hBox);
		
		
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		VBox listViewBox = new VBox(40);
		listViewBox.setPadding(new Insets(20,10,10,10));
		
		listViewBox.getChildren().add(listView);
		
		VBox sliderBox = new VBox(10);
		sliderBox.setPadding(new Insets(20,10,10,10));
		
		Label matchDescription = new Label("Grad der Übereinstimmung");
		matchDescription.setAlignment(Pos.TOP_CENTER);
		Label lowMatchLbl = new Label("niedrig");
		Label highMatchLbl = new Label("hoch");
		lowMatchLbl.setAlignment(Pos.BASELINE_LEFT);
		highMatchLbl.setAlignment(Pos.BASELINE_RIGHT);
		HBox matchDescriptionBox = new HBox(400);
		matchDescriptionBox.setAlignment(Pos.CENTER);
		matchDescriptionBox.getChildren().add(matchDescription);
		
		HBox indicatorBox = new HBox(250);
		indicatorBox.setAlignment(Pos.CENTER);
		
		indicatorBox.getChildren().addAll(lowMatchLbl, highMatchLbl);
		sliderBox.setAlignment(Pos.CENTER);
		sliderBox.getChildren().addAll(matchDescriptionBox, indicatorBox, slider);
		
		
		VBox centerBox = new VBox(50);
		centerBox.setPadding(new Insets(20,10,10,10));
		centerBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(sliderBox, listViewBox);
		
		this.setCenter(centerBox);
		
		
	}
	
	/**
	 * Handle Funktion für Übernehmen und Abbrechen Button
	 */
	@Override
	public void handle(ActionEvent arg0) {
		if(arg0.getSource() == takeOverBtn){
			validate();
			hide();
		}else if(arg0.getSource() == cancelBtn){
			close();
		}
		
		
	}
	
	/**
	 * Übergibt die ausgewählten Kategorien an die ImportNotification
	 */
	private void validate() {
		importNotification.setSelectedCategories(selectedItems);
	}
	
	/**
	 * Blendet das CategoryObjectLinkFenster aus
	 */
	private void hide(){
		((Stage)this.getScene().getWindow()).hide();
	}
	
	/**
	 * Blendet das CategoryObjectLinkFenster (wieder) ein
	 */
	public void show(){
		((Stage)this.getScene().getWindow()).show();
	}
	
	/**
	 * Schließt das CategoryObjectLinkFenster
	 */
	private void close(){
		((Stage)this.getScene().getWindow()).close();
	}
	
	
}
