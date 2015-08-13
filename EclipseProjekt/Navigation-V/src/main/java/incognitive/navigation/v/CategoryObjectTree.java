package incognitive.navigation.v;


import incognitive.commonelements.mc.model.DragDropModel;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.control.CategoryObjectTreeController;
import incognitive.navigation.mc.control.RightContainerListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class CategoryObjectTree extends TreeView<AbstractEntity> implements NavigationTreeComponent {
	
	private final RightContainerListener rightContainerFillListener;
	
	private TreeItem<AbstractEntity> root;
	
	private final CategoryObjectTreeController controller;
	
	/**Enthält alle zum Baum hinzugefügten Entities mit deren entsprechender Knoten Repräsentation*/
	private final Map<AbstractEntity, List<TreeItem<AbstractEntity>>> treeNodesToEntities;
	/**Enthält alle unkategorisierten Objekte, mit deren entsprechender Knoten Repräsentation*/
	private final Map<DataObject, TreeItem<AbstractEntity>> treeNodeToUncategorizedDataObjects;
	
	private final TreeItem<AbstractEntity> uncategorizedObjectNode;
	private final TreeContextMenu contextMenu;
	DataCategory uncategorized = new DataCategory();
	
	public CategoryObjectTree(
			final RightContainerListener rightContainerFillListener,
			final TreeContextMenu contexMenu) {
		this.rightContainerFillListener = rightContainerFillListener;
		this.setWidth(200);
		
		this.treeNodesToEntities = new HashMap<>();
		this.treeNodeToUncategorizedDataObjects = new HashMap<>();
		
		this.controller = new CategoryObjectTreeController();
		this.root = new TreeItem<AbstractEntity>();
		this.contextMenu = contexMenu;
		
		uncategorized.setCategoryName("unkategorisierte Objekte");
		this.uncategorizedObjectNode = new TreeItem<AbstractEntity>((AbstractEntity) uncategorized);
		initGUI();
		fillTree();
		setContextMenu(contextMenu);
	}
	
	
	private void initGUI(){
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	//2 x Links Click Mouse
		    	
		    	if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
		    		final AbstractEntity entity = getTreeElement();
		    		
		    		if( (entity != null) && (entity.getName() != uncategorized.getName()) ){
		    			rightContainerFillListener.setupRightContainer(entity);
		    		}
		        }
		    	
		    	//Rechtsclick Mouse
		    	if(mouseEvent.getButton() == MouseButton.SECONDARY){
		    		final AbstractEntity entity = getTreeElement();
		    		
		    		if(!(entity==null) && !(entity.getName() == uncategorized.getName())){
						contextMenu.updateItems(getTreeElement());
		    		}
		    	}
		    	
		    }
		});
		
		this.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				final AbstractEntity entity = getTreeElement();
				//Null save machen, da get tree element hier null zurück geben kann
				if(entity != null){
					Dragboard dragboard = startDragAndDrop(TransferMode.LINK);
					ClipboardContent content = new ClipboardContent();
					content.put(DragDropModel.customFormat, entity);
					dragboard.setContent(content);
				}
			}
		});
		
		this.setRoot(root);
		this.setShowRoot(false);
		this.setCellFactory((p) -> {
                return new NavigationTreeViewCellRenderer();
        });
		
	}
	
	@Override
	public AbstractEntity getTreeElement(){
		try{
			return ((TreeItem<AbstractEntity>) this.getSelectionModel().getSelectedItem()).getValue();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Füllt den Baum nach der initialisierung mit bereits vorhanden Daten
	 */
	public void fillTree(){
		
		Map<BigInteger, DataObject> objects = this.controller.getObjectList();
		for(AbstractEntity category : this.controller.getCategoryList().values()){
			//Kategorie
			final TreeItem<AbstractEntity> categoryNode = new TreeItem<AbstractEntity>(category);
			saveTreeNodesToEntities(category, categoryNode);
			root.getChildren().add(categoryNode);
			//Zugehörige Objekte
			for(BigInteger id: controller.getObjectIDsToCategory(category.getId())){
				DataObject object = this.controller.getDataObject(id);
				TreeItem<AbstractEntity> objectNode = new TreeItem<AbstractEntity>((AbstractEntity)object);
				saveTreeNodesToEntities(object, objectNode);
				categoryNode.getChildren().add(objectNode);
				objects.remove(id);
				categoryNode.getChildren().sort(
						(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
			}
			root.getChildren().sort(
					(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
			
		}
		root.getChildren().add(uncategorizedObjectNode);
		for(DataObject dataObject : objects.values()){
			final TreeItem<AbstractEntity> entity = new TreeItem<AbstractEntity>(dataObject);
			treeNodeToUncategorizedDataObjects.put(dataObject, entity);
			uncategorizedObjectNode.getChildren().add(entity);
			uncategorizedObjectNode.getChildren().sort(
					(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
			
		}
	}

	/**
	 * Fügt eine Kategorie Node mit zugehörigen Objekten in den Baum ein
	 * 
	 * @param category hinzuzufügende Kategorie
	 */
	public void addTreeNode(final DataCategory category){
		//Erstelle Kategorie
		TreeItem<AbstractEntity> categoryItem = new TreeItem<AbstractEntity>(category);
		saveTreeNodesToEntities(category, categoryItem);
		//Füge Kindsknoten hinzu
		categoryItem.getChildren().addAll(getObjectItemsOfCategory(category));
		root.getChildren().add(categoryItem);
		root.getChildren().sort(
				(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
		root.getChildren().remove(uncategorizedObjectNode);
		root.getChildren().add(uncategorizedObjectNode);
	}
	
	/**
	 * Fügt ein Objekt Node in den Baum ein
	 * 
	 * @param object hinzuzufügendes Objekt
	 */
	public void addTreeNode(final DataObject object){
		final TreeItem<AbstractEntity> objectItem = new TreeItem<AbstractEntity>(object);
		treeNodeToUncategorizedDataObjects.put(object, objectItem);
		uncategorizedObjectNode.getChildren().add(objectItem);
		uncategorizedObjectNode.getChildren().sort(
				(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
	}

	@Override
	public void refreshSelectedNode(final AbstractEntity toRefreshEntity) {
		//Ist es eine Kategorie oder ein Kategorie Objekt?
		if(treeNodesToEntities.containsKey(toRefreshEntity)){
			final  List<TreeItem<AbstractEntity>> selectedTreeNodeList = treeNodesToEntities.get(toRefreshEntity);
			//Knoten ist eine Kategorie
			if(toRefreshEntity instanceof DataCategory){
				TreeItem<AbstractEntity> categoryItem = selectedTreeNodeList.get(0);
				//Zur Kategorie gehörige Objekte werden alle entfernt -> auch TreeNodes aus liste hauen
				removeObjectTreeNodesOfCategoryFromMap(categoryItem);
				//Kinder löschen und neu hinzufügen
				categoryItem.getChildren().removeAll(categoryItem.getChildren());
				categoryItem.setValue(null);
				categoryItem.setValue(toRefreshEntity);
				categoryItem.getChildren().addAll(getObjectItemsOfCategory((DataCategory)categoryItem.getValue()));
				categoryItem.getParent().getChildren().sort(
						(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
				root.getChildren().remove(uncategorizedObjectNode);
				root.getChildren().add(uncategorizedObjectNode);
			//Knoten ist ein DataObject
			} else {
				TreeItem<AbstractEntity> datObjectItem = selectedTreeNodeList.get(0);
				datObjectItem.setValue(null);
				datObjectItem.setValue(toRefreshEntity);
				datObjectItem.getParent().getChildren().sort(
						(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
			}
			
		//Ist es ein unkategorisiertes Objekt?
		} else if(treeNodeToUncategorizedDataObjects.containsKey(toRefreshEntity)){
			final  TreeItem<AbstractEntity> selectedNode = treeNodeToUncategorizedDataObjects.get(toRefreshEntity);
			selectedNode.setValue(null);
			selectedNode.setValue(toRefreshEntity);
			uncategorizedObjectNode.getChildren().sort(
					(o1, o2) -> o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) );
		}
	}


	@Override
	public void deleteSelectedNode(final AbstractEntity toDeleteEntity) {
		if(treeNodesToEntities.containsKey(toDeleteEntity)){
			final  List<TreeItem<AbstractEntity>> selectedTreeNodeList = treeNodesToEntities.get(toDeleteEntity);
			//Knoten ist eine Kategorie
			if(toDeleteEntity instanceof DataCategory){
				final TreeItem<AbstractEntity> categoryItem = selectedTreeNodeList.get(0);
				//Zur Kategorie gehörige Objekte werden alle entfernt -> auch TreeNodes aus liste hauen
				removeObjectTreeNodesOfCategoryFromMap(categoryItem);
				this.root.getChildren().remove(categoryItem);
			//Knoten ist ein DataObject
			} else {
				for(TreeItem<AbstractEntity> item : selectedTreeNodeList){
					final TreeItem<AbstractEntity> parentItem = item.getParent();
					parentItem.getChildren().remove(item);
				}
			}
			treeNodesToEntities.remove(toDeleteEntity);
		} else if(treeNodeToUncategorizedDataObjects.containsKey(toDeleteEntity)){
			final  TreeItem<AbstractEntity> selectedNode = treeNodeToUncategorizedDataObjects.get(toDeleteEntity);
			uncategorizedObjectNode.getChildren().remove(selectedNode);
		}
	}
	
	public void updateTree(){
		this.root.getChildren().clear();
		this.uncategorizedObjectNode.getChildren().clear();
		this.treeNodesToEntities.clear();
		this.treeNodeToUncategorizedDataObjects.clear();
		fillTree();
	}
	
	private void saveTreeNodesToEntities(final AbstractEntity entity, final TreeItem<AbstractEntity> node){
		if(!treeNodesToEntities.containsKey(entity)){
			treeNodesToEntities.put(entity, new ArrayList<TreeItem<AbstractEntity>>());
		}
		treeNodesToEntities.get(entity).add(node);
	}


	
	private Set<TreeItem<AbstractEntity>> getObjectItemsOfCategory(DataCategory category){
		Set<TreeItem<AbstractEntity>> dataObjectItems = new HashSet<TreeItem<AbstractEntity>>();
		for(DataObject dataObject : controller.getObjectsToCategory(category.getId())){
			//Erstelle Objekt Node
			final TreeItem<AbstractEntity> objectItem = new TreeItem<AbstractEntity>(dataObject);
			saveTreeNodesToEntities(dataObject, objectItem);
			dataObjectItems.add(objectItem);
			//Aus unkategorisiert Liste werfen wenn es dort angezeigt wurde
			if(treeNodeToUncategorizedDataObjects.containsKey(dataObject)){
				this.uncategorizedObjectNode.getChildren().remove(treeNodeToUncategorizedDataObjects.get(dataObject));
				treeNodeToUncategorizedDataObjects.remove(dataObject);
			}
		}
		return dataObjectItems;
	}
	
	private void removeObjectTreeNodesOfCategoryFromMap(final TreeItem<AbstractEntity> categoryItem){
		List<DataObject> newObjects = controller.getObjectsToCategory(categoryItem.getValue().getId());
		
		//Zur Kategorie gehörige Objekte werden alle entfernt -> auch TreeNodes aus liste hauen
		for(TreeItem<AbstractEntity> item : categoryItem.getChildren()){
			if(treeNodesToEntities.containsKey(item.getValue())){
				treeNodesToEntities.get(item.getValue()).remove(item);
			}
			//Objekt wurde aus kategorie geworfen -> wieder nicht zugeordneten zuordnen
			if(!newObjects.contains(item.getValue())){
				addTreeNode((DataObject) item.getValue());
			}
		}
	}
}
