package incognitive.navigation.v;

import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.control.RightContainerListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Navigations Tabs Pane welches den Kategoriebaum, sowie den Relationenbaum anzeigt
 * 
 * @author Lisa Leuschner
 */
public class NavigationTabPane extends TabPane {
	
	private final RelationTreeView relationTree;
	
	private CategoryObjectTree categoryTree;

	public NavigationTabPane(final RightContainerListener rightContainerListener) {
		this.setMaxWidth(200);
		this.setMinWidth(150);
		
		//Contextmenu erstellen
		final TreeContextMenu contextMenu = new TreeContextMenu(this, rightContainerListener);
		
		//Tab mit dem KategorieObjektbaum
		final Tab categoryTreeTab = new Tab();
		categoryTreeTab.setText("Kategorie");
		this.categoryTree = new CategoryObjectTree(rightContainerListener, contextMenu);
		categoryTreeTab.setContent(categoryTree);
		categoryTreeTab.setClosable(false);
		
		//Tab mit dem Relationsbaum
		final Tab relationTreeTab = new Tab();
		relationTreeTab.setText("Relation");
		this.relationTree = new RelationTreeView(rightContainerListener, contextMenu);
		relationTreeTab.setContent(relationTree);
		relationTreeTab.setClosable(false);
		
		this.getTabs().add(categoryTreeTab);
		this.getTabs().add(relationTreeTab);
	}
	
	/**
	 * Gibt die aktuelle ausgewählte Tab Component zurück
	 * @return
	 */
	public NavigationTreeComponent getSelectedTabComponent(){
		return (NavigationTreeComponent) this.getSelectionModel().getSelectedItem().getContent();
	}
	
	/**
	 * Löscht die ausgewählte Entity in den Trees wenn diese vorhanden ist
	 * 
	 * @param entity Entity
	 */
	public void deleteSelectedEntityInTrees(final AbstractEntity entity){
		this.relationTree.deleteSelectedNode(entity);
		this.categoryTree.deleteSelectedNode(entity);
	}
	
	/**
	 * Aktualisiert die ausgewählte Entity in den Trees wenn diese vorhanden ist
	 * 
	 * @param entity Entity
	 */
	public void refreshSelectedEntityInTrees(final AbstractEntity entity){
		this.relationTree.refreshSelectedNode(entity);
		this.categoryTree.refreshSelectedNode(entity);
	}


	/**
	 * Gibt den Relationsbaum des Tabsheets zurück
	 * 
	 * @return Relationsbaum
	 */
	public RelationTreeView getRelationTree() {
		return relationTree;
	}

	/**
	 * Gibt den Kategoriebaum des Tabsheets zurück
	 *  
	 * @return Kategoriebaum
	 */
	public CategoryObjectTree getCategoryTree() {
		return categoryTree;
	}


}
