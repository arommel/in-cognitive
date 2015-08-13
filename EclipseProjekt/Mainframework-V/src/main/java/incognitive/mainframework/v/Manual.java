package incognitive.mainframework.v;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Manual extends BorderPane {
	
	ListView<String> list = new ListView<String>();
	ObservableList<String> items =FXCollections.observableArrayList (
	    "Erste Schritte", "Erstellen von Objekten", "Erstellen von Kategorien", "Erstellen von Relationen",
	    "Ausgewähltes Objekt bearbeiten","Ausgewählte Kategorie bearbeiten","Ausgewählte Relation bearbeiten",
	    "Löschen von Objekten", "Löschen von Kategorien", "Löschen von Relationen", "Importieren", 
	    "Datenpfad ändern", "Drag and Drop", "Datenbank zurücksetzen", "Neue Zeichenfläche");
	private Label nameLabel = new Label();
	private Label mainLabel = new Label("Hilfe und Anleitungen");
	private Label label = new Label("Hier erhalten Sie grundlegende Informationen zur Bedienung von In-cognitive");
	Hyperlink link = new Hyperlink();
	private Text text = new Text();
	private TextFlow textFlow = new TextFlow(text, link);
	
	public Manual (){
		mainLabel.setFont(new Font("Arial", 20));
		nameLabel.setFont(new Font("Arial", 15));
		list.setItems(items);
		list.setMinHeight(450);
		list.setMinWidth(220);
		list.setMaxHeight(450);
		list.setMaxWidth(220);
		list.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            nameLabel.setText(list.getSelectionModel().getSelectedItem());
	            
	            if (list.getSelectionModel().getSelectedItem() == "Erste Schritte"){
	            	text.setText("In diesem Abschnitt finden Sie grundlegende Informationen "
	            			+ 		  "zu den speziellen Aufgaben und Funktionen von In-cognitive. "
	            			+ 		  "In-cognitive ermöglicht es Ihnen große Datenmengen einfach "
	            			+ 		  "zu verwalten. Diese Datenmengen stellt ihnen das "
	            			+ 		  "Programm sowohl grafisch, als auch übersichtlich zusammen. "
	            			+ 		  "Durch die Speicherung in XML ist es auf verschiedenen Systemen "
	            			+ 		  "einsetzbar.");
	            	link.setText("");
	            	 
	            	 
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Erstellen von Objekten"){
	            	text.setText("In In-cognitive können Sie auf drei verschiedene Arten "
	            			+ 		  "ein Objekt einfügen, indem Sie entweder auf Einfügen > Objekt "
	            			+         "klicken und dann ein Objekt manuell erstellen, oder indem Sie "
	            			+ 		  "auf das Icon 'Paste' klicken, und ebenso ein Objekt manuell "
	            			+ 		  "erstellen : \n\n"
	            			+ 		  "1.  Fenster zur Bearbeitung des Objektes öffnen \n"
	            			+ 		  "2.  Neben dem Label 'Objektname' ihren Objektnamen eingeben \n"
	            			+ 		  "3.  Attribute hinzufügen ( Dazu in die erste Zeile der "
	            			+ 		  "ersten Spalte klicken und einen Attributnamen eingeben. "
	            			+ 		  "In der zweiten Spalte können Sie nun mit einem Click "
	            			+ 		  "den Typ des Attributes einstellen. Den Wert des "
	            			+ 		  "Attributes legen Sie in der dritten Spalte fest.) \n"
	            			+ 		  "4.  Speichern Sie Ihr Objekt mit dem Click auf 'Speichern' "
	            			+ 		  "oder verwerfen Sie es mit dem Click auf 'Abbrechen' \n\n"
	            			+ 		  "Sie können Objekte aber auch importieren. \n\n"
	            					  
	            			+ 		  "Mehr dazu im Abschnitt :   ");
	            	link.setText("Importieren");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Erstellen von Kategorien"){
	            	text.setText("In In-cognitive können Sie auf drei verschiedene Arten \n"
	            			+ 		  "eine Kategorie einfügen, indem Sie entweder auf Einfügen > Kategorie "
	            			+         "klicken und dann eine Kategorie manuell erstellen, oder indem Sie "
	            			+ 		  "auf das Icon 'Paste' klicken, und ebenso eine Kategorie manuell "
	            			+ 		  "erstellen : \n\n"
	            			+ 		  "1.  Fenster zur Bearbeitung der Kategorie öffnen \n"
	            			+ 		  "2.  Neben dem Label 'Name der Kategorie' ihrer Kategorie "
	            			+		  "einen Namen geben\n"
	            			+ 		  "3.  Tags fügen Sie in der Tabelle ein, indem Sie auf die erste Zeile "
	            			+ 		  "doppelt clicken und ihren Tag eingeben. Mit Enter oder "
	            			+ 		  "dem Click ins Leere bestätigen Sie ihre Eingabe.\n"
	            			+ 		  "4.  Ordnen Sie ihrer Kategorie Objekte zu, indem sie auf das Feld "
	            			+ 		  "mit dem kleinen Pfeil drücken und ihr Objekt auswählen. "
	            			+ 		  "Mit 'Hinzufügen' wird es ihrer Kategorie zugeordnet.\n"
	            			+ 		  "5.  Speichern Sie Ihre Kategorie mit dem Click auf 'Speichern' "
	            			+ 		  "oder verwerfen Sie sie mit dem Click auf 'Abbrechen' \n\n"
	            			+ 		  "Sie können Kategorien aber auch importieren. \n\n"
	            					  
	            			+ 		  "Mehr dazu im Abschnitt :   ");
	            	link.setText("Importieren");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Erstellen von Relationen"){
	            	text.setText("In In-cognitive können Sie auf zwei verschiedene Arten "
	            			+ 		  "eine Relation einfügen, indem Sie entweder auf Einfügen > Relation "
	            			+         "klicken und dann eine Relation manuell erstellen, oder indem Sie "
	            			+ 		  "auf das Icon 'Paste' klicken, und ebenso eine Relation manuell "
	            			+ 		  "erstellen : \n\n"
	            			+ 		  "1.  Fenster zur Bearbeitung der Relation öffnen \n"
	            			+ 		  "2.  Neben dem Label 'Name' den Relationsnamen eingeben\n"
	            			+ 		  "3.  Durch den Click in die Kreise festlegen, zwischen was "
	            			+ 		  "die Relation erstellt werden soll. "
	            			+ 		  "(Nun bekommen sie die möglichen Elemente jeweils in einer "
	            			+ 		  "Liste darunter angezeigt.)\n"
	            			+ 		  "4.  Wählen Sie die Elemente mit einem Click auf die jeweilige "
	            			+ 		  "Zeile aus.\n"
	            			+ 		  "5.  Attribute hinzufügen ( Dazu in die erste Zeile der "
	            			+ 		  "ersten Spalte klicken und einen Attributnamen eingeben. "
	            			+ 		  "In der zweiten Spalte können Sie nun mit einem Click "
	            			+ 		  "den Typ des Attributes einstellen. Den Wert des "
	            			+ 		  "Attributes legen Sie in der dritten Spalte fest.) \n"
	            			+ 		  "6.  Speichern Sie Ihre Relation mit dem Click auf 'Speichern' "
	            			+ 		  "oder verwerfen Sie sie mit dem Click auf 'Abbrechen' \n\n");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Ausgewähltes Objekt bearbeiten"){
	            	text.setText("In-cognitive bietet Ihnen die Möglichkeit ein schon bestehendes "
	            			+ 		  "Objekt nachträglich zu bearbeiten. Dies können Sie auf zwei "
	            			+ 		  "verschiedene Arten machen : \n\n"
	            			+ 		  "1.  Wählen Sie ihr zu bearbeitendes Objekt auf der linken "
	            			+ 		  "Seite im Tree aus\n"
	            			+ 		  "2.  Clicken Sie auf Bearbeiten > Ausgewähltes Objekt bearbeiten\n"
	            			+ 		  "ODER\n"
	            			+ 		  "    Clicken Sie auf das Icon 'Bearbeiten'\n\n"
	            			+ 		  "Nun können Sie den Namen des Objektes, sowie die einzelnen "
	            			+ 		  "Attribute des Objektes bearbeiten, indem Sie einen Doppelklick "
	            			+ 		  "auf die entsprechende Zeile der Tabelle machen, und ihre Änderung "
	            			+ 		  "eintragen. Nun noch Speichern und die Änderungen werden übernommen. "
	            			+ 		  "Nähere Informationen zur Bearbeitung der Attributwerte in der "
	            			+ 		  "Tabelle gibt es im Abschnitt:   ");
	            	link.setText("Objekt erstellen");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Ausgewählte Kategorie bearbeiten"){
	            	text.setText("In-cognitive bietet Ihnen die Möglichkeit eine schon bestehende "
	            			+ 		  "Kategorie nachträglich zu bearbeiten. Dies können Sie auf zwei "
	            			+ 		  "verschiedene Arten machen : \n\n"
	            			+ 		  "1.  Wählen Sie ihre zu bearbeitende Kategorie auf der linken "
	            			+ 		  "Seite im Tree aus\n"
	            			+ 		  "2.  Clicken Sie auf Bearbeiten > Ausgewählte Kategorie bearbeiten\n"
	            			+ 		  "ODER\n"
	            			+ 		  "    Clicken Sie auf das passende Icon\n\n"
	            			+ 		  "Nun können Sie den Namen der Kategorie, sowie die einzelnen "
	            			+ 		  "Attribute der Kategorie bearbeiten, indem Sie einen Doppelklick "
	            			+ 		  "auf die entsprechende Zeile der Tabelle machen, und ihre Änderung "
	            			+ 		  "eintragen. Nun noch Speichern und die Änderungen werden übernommen. "
	            			+ 		  "Nähere Informationen zur Bearbeitung der Attributwerte in der "
	            			+ 		  "Tabelle und zur Zuordnung der Objekte gibt es im Abschnitt:  ");
	            	link.setText("Kategorie erstellen");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Ausgewählte Relation bearbeiten"){
	            	text.setText("In-cognitive bietet Ihnen die Möglichkeit eine schon bestehende "
	            			+ 		  "relation nachträglich zu bearbeiten. Dies können Sie auf zwei "
	            			+ 		  "verschiedene Arten machen : \n\n"
	            			+ 		  "1.  Wählen Sie ihre zu bearbeitende Relation auf der linken "
	            			+ 		  "Seite im Tree aus\n"
	            			+ 		  "2.  Clicken Sie auf Bearbeiten > Ausgewählte Relation bearbeiten\n"
	            			+ 		  "ODER\n"
	            			+ 		  "    Clicken Sie auf das passende Icon\n\n"
	            			+ 		  "Nun können Sie den Namen der Relation, sowie die einzelnen Attribute "
	            			+ 		  "der Relation bearbeiten, indem Sie einen Doppelklick auf die "
	            			+ 		  "entsprechende Zeile der Tabelle machen, und ihre Änderung eintragen. "
	            			+ 		  "Nun noch Speichern und die Änderungen werden übernommen. "
	            			+ 		  "Nähere Informationen zur Bearbeitung der Attributwerte in der "
	            			+ 		  "Tabelle und zur Zuordnung der Elemente zwischen denen eine "
	            			+ 		  "Relation erstellt werden soll, gibt es im Abschnitt:  ");
	            	link.setText("Relation erstellen");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Löschen von Objekten"){
	            	text.setText("In In-cognitive können Sie ein bestehendes Objekt auf zwei "
	            			+ 		  "verschiedene Arten löschen, indem Sie entweder auf "
	            			+         "Bearbeiten > Ausgewähltes Objekt löschen klicken oder "
	            			+ 		  "indem Sie auf das Icon 'Löschen' klicken "
	            			+ 		  "dabei sollten Sie sich bewusst sein, dass das Objekt "
	            			+ 		  "dann unwiderruflich gelöscht ist.");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Löschen von Kategorien"){
	            	text.setText("In In-cognitive können Sie eine bestehende Kategorie "
	            			+ 		  "auf zwei verschiedene Arten löschen, indem Sie entweder "
	            			+         "auf Bearbeiten > Ausgewählte Kategorie löschen klicken "
	            			+ 		  "oder indem Sie die Kategorie im linken Tree auswählen "
	            			+ 		  "und auf das Icon 'Löschen' klicken. "
	            			+ 		  "Nun müssen Sie noch wählen, ob Sie nur die Kategorie oder "
	            			+ 		  "auch die enthaltenen Objekte löschen wollen. Seien Sie sich "
	            			+ 		  "dabei bewusst, dass die Kategorie bzw auch deren Objekte "
	            			+ 		  "nun unwiderruflich gelöscht sind.");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Löschen von Relationen"){
	            	text.setText("In In-cognitive können Sie eine bestehende Relation "
	            			+ 		  "auf zwei verschiedene Arten löschen, indem Sie entweder "
	            			+         "auf Bearbeiten > Ausgewählte Relation löschen klicken "
	            			+ 		  "oder indem Sie die Relation im linken Tree auswählen "
	            			+ 		  "und auf das Icon 'Löschen' klicken."
	            			+ 		  "Nun müssen Sie noch wählen, ob Sie nur die Relation oder "
	            			+ 		  "auch die enthaltenen Objekte bzw Kategorien löschen wollen. "
	            			+ 		  "Seien Sie sich dabei bewusst, dass die Relation bzw "
	            			+ 		  "Kategorie bzw auch deren Objekte nun unwiderruflich "
	            			+ 		  "gelöscht sind");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Importieren"){
	            	text.setText("In-cognitive bietet Ihnen die Möglichkeit Objekte und "
	            			+ 		  "Kategorien von Wikipedia zu importieren :\n\n"
	            			+ 		  "1.  Clicken Sie auf Bearbeiten > Importieren\n"
	            			+ 		  "2.  Wählen Sie als Eingabetype 'URL'\n"
	            			+ 		  "3.  Fügen Sie in das Textfeld den kopierten Link ein\n"
	            			+ 		  "4.  Drücken Sie auf 'Seitenquelltext laden'\n"
	            			+ 		  "5.  Warten Sie bis der Quelltext geladen ist und "
	            			+ 		  "Drücken Sie auf 'Importieren'\n\n"
	            			+ 		  "Nun haben Sie die Möglichkeit das erkannte Objekt "
	            			+ 		  "zu bearbeiten und Kategorien zu erstellen. ");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Datenpfad ändern"){
	            	text.setText("Die Daten, welche Sie in In-cognitive anlegen, werden in "
	            			+ 	 "einem bestimmten Ordner - welchen beim aller ersten Start "
	            			+	 "des Programmes festgelegt haben - auf ihren Computer gespeichert. "
	            			+ 	 "Sie können diesen nachträglich wie folgt ändern: \n\n"
	            			+	 "1.  gehen Sie auf Einstellungen > Datenpfad ändern \n"
	            			+ 	 "2.  Wählen Sie den Ordner aus, in welchen Sie in Zukunft die "
	            			+	 "Daten speichern möchten \n"
	            			+ 	 "3.  Bestätigen Sie ihre Auswahl mit 'Ordner auswählen'\n\n"
	            			+	 "Von nun an werden Ihre Daten in diesem Ordner gespeichert.");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Drag and Drop"){
	            	text.setText("Drag and Drop erleichtert ihnen die Bedienung der grafischen "
	            			+	 "Oberfläche. Und so einfach funktioniert es :\n\n"
	            			+ "1.  Clicken sie ein Objekt, eine Kategorie oder eine Relation "
	            			+ "im linken Tree an \n"
	            			+ "2.  Halten Sie die linke Maustaste gedrückt und ziehen Sie ihr "
	            			+ "Element auf die weiße Zeichenfläche rechts daneben\n"
	            			+ "3.  Das Objekt, die Kategorie oder die Relation wird Ihnen "
	            			+ "grafisch angezeigt.");
	            	link.setText("");
	            }
	            else if (list.getSelectionModel().getSelectedItem() == "Datenbank zurücksetzen"){
	            	text.setText("Um die Datenbank zurück zu setzen, sind nur drei Clicks notwendig.\n\n"
	            			+ "1.  Wählen Sie Bearbeiten > Datenbank zurücksetzen\n"
	            			+ "2.  Bestätigen Sie die Zurücksetzung der Datenbank mit einem Click "
	            			+ "auf 'Ja' oder verwerfen Sie es mit einem Click auf 'Abbrechen'\n\n"
	            			+ "ACHTUNG ! Seien Sie sich bewusst, dass mit dem Zurücksetzen der "
	            			+ "Datenbank all ihre Daten unwiderruflich gelöscht werden.");
	            	link.setText("");
	            	
	            }
	            
	            else if (list.getSelectionModel().getSelectedItem() == "neue Zeichenfläche"){
	            	text.setText("Neue Zeichenflächen können durchaus für die Betrachtung "
	            			+	 "und den Vergleich - beispielsweise von Relationen - nützlich "
	            			+	 "sein. Möchten Sie eine neue Zeichenfläche erstellen, dann "
	            			+ 	 "dann haben Sie zwei Möglichkeiten:\n\n"
	            			+ "Möglichkeit 1: Wählen Sie Einfügen > Neuer Tab\n"
	            			+ "Möglichkeit 2: Clicken Sie auf das dafür vorgesehene Icon");
	            }
	            
	        }
	    });
		
		
		VBox vlabels = new VBox();
		vlabels.getChildren().addAll(mainLabel, label);
		vlabels.setSpacing(5);
		vlabels.setPadding(new Insets(15, 0, 20, 0)); 
		
		VBox velemente = new VBox();
		velemente.getChildren().addAll(nameLabel, textFlow);	
		velemente.setSpacing(20);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(list, velemente);
		hbox.setSpacing(20);
		
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(vlabels, hbox);
		this.setPadding(new Insets(5, 10, 5, 10)); 		
		this.setCenter(vbox);
		
		
	}
	
	
}
