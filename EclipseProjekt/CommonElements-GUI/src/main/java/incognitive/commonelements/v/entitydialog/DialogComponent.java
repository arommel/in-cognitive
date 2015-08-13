package incognitive.commonelements.v.entitydialog;

/**
 * Interface welches Methoden  für Dialoge bereitstellt,<br>
 * welche zum speichern und darstellen der Daten angesprochen werden können.<br>
 * 
 * @author Lisa Leuschner
 */
public interface DialogComponent {
	
	/**
	 * Initialisiert die Komponenten des Dialogs
	 */
	public  void init();
	
	/**
	 * Updated die Komponenten des Dialogs mit übergebenen Daten
	 */
	public  void updateComponent();
	
	/**
	 * Speichert eingegebenen Daten in der Datenbank
	 */
	public void save();
	
	/**
	 * lädt die eingebenen Daten in das lokale Objekt
	 */
	public  void updateModel();
	
	
	/**
	 * Prüft ob alle Eingaben in den Komponenten korrekt sind und<br>
	 * liefert je nach dem true oder false zurück
	 * 
	 * @return valide?
	 */
	public  boolean validateComponent();
	
	/**
	 * Höhe die der Dialog letztendlich hat
	 * 
	 * @return Dialog Höhe
	 */
	public int getComponentHeight();
	
	/**
	 * Breite die der Dialog letztendlich hat
	 * 
	 * @return Dialog Breite
	 */
	public int getComponentWidth();

	/**
	 * Eventhandler für das Schließen des Fensters
	 */
	public void onClose();
}
