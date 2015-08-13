package incognitive.navigation.v;

import incognitive.database.model.AbstractEntity;

/**
 * Interface für alle Komponenten in der Navigation, welches gemeinsame Methoden zur Knoten-<br>
 * bearbeitung kapselt
 * 
 * @author Lisa Leuschner
 */
public interface NavigationTreeComponent {
	
	/**
	 * Ermittelt die aktuell ausgewählte Entity aus der Tree Node 
	 * 
	 * @return Node als Entity
	 */
	public AbstractEntity getTreeElement();
	
	/**
	 * Aktualisiert den Knoten zur übergebenen Entity, wenn ein Knoten dazu existiert.
	 */
	public void refreshSelectedNode(final AbstractEntity toRefreshEntity);
	
	/**
	 * Löscht den Knoten zur übergebenen Entity, wenn ein Knoten dazu existiert.
	 */
	public void deleteSelectedNode(final AbstractEntity toDeleteEntity);
}
