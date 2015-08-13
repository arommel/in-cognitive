package incognitive.navigation.v;

import incognitive.commonelements.mc.model.DragDropModel;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.control.RelationTreeController;
import incognitive.navigation.mc.control.RightContainerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class RelationTreeView extends TreeView<AbstractEntity> implements NavigationTreeComponent {

	private final RightContainerListener rightContainerFillListener;

	private TreeItem<AbstractEntity> root;
	
	private final Map<AbstractEntity, List<TreeItem<AbstractEntity>>> entitiesToTreeNodes;
	
	private final RelationTreeController controller;

	private TreeContextMenu contextMenu;
	
	public RelationTreeView(final RightContainerListener rightContainerFillListener, final TreeContextMenu contextMenu) {
		this.rightContainerFillListener = rightContainerFillListener;
		this.entitiesToTreeNodes = new HashMap<AbstractEntity, List<TreeItem<AbstractEntity>>>();
		
		this.setWidth(200);
		
		this.controller = new RelationTreeController();
		this.root = new TreeItem<AbstractEntity>();
		
		initGUI();
		fillTree();
		this.contextMenu = contextMenu;
		setContextMenu(contextMenu);
	}
	
	private void initGUI(){		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	//2 x Links Click Mouse
		    	if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
		    		final AbstractEntity entity = getTreeElement();
					rightContainerFillListener.setupRightContainer(entity);
		        }
		    	//Rechtsclick Mouse
		    	if(mouseEvent.getButton() == MouseButton.SECONDARY){
		    		contextMenu.updateItems(getTreeElement());
		    	}
		    }
		});
		
		this.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				final AbstractEntity entity = getTreeElement();
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
	
	/**
	 * Füllt den Baum nach der initialisierung mit bereits vorhanden Daten
	 */
	private void fillTree(){
		for(DataRelation relation : this.controller.getRelationList().values()){
			if(relation.getName().compareTo(DataRelation.CATEGORY_OBJECT_RELATION)!=0){
				final TreeItem<AbstractEntity> relationNode = createRelationNode(relation);
				root.getChildren().add(relationNode);
			}
		}
	}
	
	/**
	 * Erstellt ein Parent Knoten (Relation) und zwei Unterknoten(Entities der Relation)
	 * 
	 * @param relation Relation die als Knoten erstellt werden soll
	 * 
	 * @return Relation als ParentKnoten für den Baum 
	 */
	private TreeItem<AbstractEntity> createRelationNode(final DataRelation relation){
		//Relation Node
		final  TreeItem<AbstractEntity> relationNode = new  TreeItem<AbstractEntity>(relation);
		if(!entitiesToTreeNodes.containsKey(relationNode)){
			entitiesToTreeNodes.put(relation, new ArrayList<TreeItem<AbstractEntity>>());
		}
		entitiesToTreeNodes.get(relation).add(relationNode);
		
		//Erste Entity
		final AbstractEntity firstObject 
			= controller.getChild(relation.getTypeOfObjectOne(), relation.getIdfromObjectOne());
		final  TreeItem<AbstractEntity> firstNode = new  TreeItem<AbstractEntity>(firstObject);
		relationNode.getChildren().add(firstNode);
		if(!entitiesToTreeNodes.containsKey(firstObject)){
			entitiesToTreeNodes.put(firstObject, new ArrayList<TreeItem<AbstractEntity>>());
		}
		entitiesToTreeNodes.get(firstObject).add(firstNode);
		
		//Zweite Entity
		final AbstractEntity secondObject 
			= controller.getChild(relation.getTypeOfObjectTwo(), relation.getIdfromObjectTwo());
		final  TreeItem<AbstractEntity> secondNode = new TreeItem<AbstractEntity>(secondObject);
		relationNode.getChildren().add(secondNode);
		if(!entitiesToTreeNodes.containsKey(secondObject)){
			entitiesToTreeNodes.put(secondObject, new ArrayList<TreeItem<AbstractEntity>>());
		}
		entitiesToTreeNodes.get(secondObject).add(secondNode);
		
		return relationNode;
	}

	@Override
	public AbstractEntity getTreeElement() {
		try{
			return this.getSelectionModel().getSelectedItem().getValue();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public void refreshSelectedNode(final AbstractEntity toRefreshEntity) {
		if(entitiesToTreeNodes.containsKey(toRefreshEntity)){
			final  TreeItem<AbstractEntity> selectedNode = entitiesToTreeNodes.get(toRefreshEntity).get(0);
			final AbstractEntity selectedEntity = selectedNode.getValue();
			selectedNode.setValue(null);
			selectedNode.setValue(selectedEntity);
		}
	}

	@Override
	public void deleteSelectedNode(final AbstractEntity toDeleteEntity) {
		if(entitiesToTreeNodes.containsKey(toDeleteEntity)){
			for(TreeItem<AbstractEntity> node : entitiesToTreeNodes.get(toDeleteEntity)){
				//Für blätter -> parents entfernen
				if(node.getParent()!= null){
					this.root.getChildren().remove(node.getParent());
				//direkter relations knoten entfernen
				} else {
					this.root.getChildren().remove(node);
				}
			}
		}
	}


	public void addTreeNode(DataRelation relation) {
		final TreeItem<AbstractEntity> relationNode = createRelationNode(relation);
		root.getChildren().add(relationNode);
	}
	
}
