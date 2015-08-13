package incognitive.navigation.v;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.dao.DataRelationDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.entitydialog.DialogHandler;
import incognitive.commonelements.v.entitydialog.SaveCancelDialog;
import incognitive.commonelements.v.entitydialog.SaveCancelDialogBuilder;
import incognitive.commonelements.v.entitydialog.deletedialog.DataCategoryDelete;
import incognitive.commonelements.v.entitydialog.deletedialog.DataObjectDelete;
import incognitive.commonelements.v.entitydialog.deletedialog.DataRelationDelete;
import incognitive.commonelements.v.entitydialog.editdialog.DataCategoryView;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;
import incognitive.commonelements.v.entitydialog.editdialog.DataRelationView;
import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.control.RightContainerListener;

import java.util.Collection;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class TreeContextMenu extends ContextMenu implements EventHandler {

	/**
	 * @author Christoph Pförtner
	 * Klasse für das Kontextmenü im Baum
	*/

	private MenuItem addObject, addCategory, addRelation, editItem, deleteItem, editRelation;
	private Menu newEntityMenu;
	private NavigationTabPane tabSheet;
	private RightContainerListener rightContainerListener;
	private final CommonElementsController controller;
	
	public TreeContextMenu(final NavigationTabPane tabSheet, RightContainerListener rightContainerListener){
		this.rightContainerListener = rightContainerListener;
		this.tabSheet = tabSheet;
		this.controller = CommonElementsController.getInstance();
	}

	/**
	 * Unterscheidet den Typ des Übergabeparameters und ruft die EditFunktion des Typs auf
	 * 
	 * @param Entity die bearbeitet werden soll
	 * 
	 */	
	private void edit(AbstractEntity abstractEntity){

			if(abstractEntity instanceof DataObject){
				editObject(abstractEntity);
			}else if(abstractEntity instanceof DataCategory){
				editCategory(abstractEntity);
			}else if(abstractEntity instanceof DataRelation){
				editRelation(abstractEntity);
			}
		
	}
	
	
	/**
	 * Unterscheidet den Typ des Übergabeparameters und ruft die Lösch-Funktion des Typs auf
	 * 
	 * @param Entity die bearbeitet werden soll
	 * 
	 */	
	private void delete(AbstractEntity abstractEntity){
		if(abstractEntity instanceof DataObject){
			deleteObject(abstractEntity);
		}else if(abstractEntity instanceof DataCategory){
			deleteCategory(abstractEntity);
		}else if(abstractEntity instanceof DataRelation){
			deleteRelation(abstractEntity);
		}
	}
	
	/**
	 * Ruft den Löschen DataRelationDeleteDialog auf und löscht Element bei Bestätigung aus Baum
	 * 
	 * @param Entity die gelöscht werden soll
	 * 
	 */	
	private void deleteRelation(AbstractEntity abstractEntity) {
		final DataRelationDelete alert = new DataRelationDelete((DataRelation) abstractEntity);
		alert.withDeleteAction(() -> {
			final AbstractEntity selectedEntity = tabSheet.getSelectedTabComponent().getTreeElement();
			tabSheet.deleteSelectedEntityInTrees(selectedEntity);
		});
		alert.showDialog();
	}
	/**
	 * Ruft den Löschen DataCategoryDeleteDialog auf und löscht Element bei Bestätigung aus Baum
	 * 
	 * @param Entity die gelöscht werden soll
	 * 
	 */	
	private void deleteCategory(AbstractEntity abstractEntity) {
		final DataCategoryDelete alert = new DataCategoryDelete((DataCategory) abstractEntity);
		final AbstractEntity selectedEntity = tabSheet.getSelectedTabComponent().getTreeElement();
		final List<DataObject> objectsInCategory = controller.getObjectsToCategory(selectedEntity.getId());
		alert.withNormalSaveAction(() -> {
			tabSheet.deleteSelectedEntityInTrees(selectedEntity);
		});
		alert.withAdditionalSaveAction(() -> {
			for(DataObject o : objectsInCategory){
				tabSheet.deleteSelectedEntityInTrees(o);
			}
		});
		alert.showDialog();
	}
	/**
	 * Ruft den Löschen DataRObjectDeleteDialog auf und löscht Element bei Bestätigung aus Baum
	 * 
	 * @param Entity die gelöscht werden soll
	 * 
	 */	
	private void deleteObject(AbstractEntity abstractEntity) {
		final DataObjectDelete alert = new DataObjectDelete((DataObject) abstractEntity);
		alert.withDeleteAction(() -> {
			final AbstractEntity selectedEntity = tabSheet.getSelectedTabComponent().getTreeElement();
			tabSheet.deleteSelectedEntityInTrees(selectedEntity);
		});
		alert.showDialog();
	}
	
	/**
	 * Ruft DataObjectView mit der zu editierenden Entität auf und aktualisiert bei Bestätigung diese im Baum
	 * 
	 * @param Entity die editiert werden soll
	 * 
	 */	
	private void editObject(AbstractEntity abstractEntity){
		DataObject dataObject = (DataObject) abstractEntity;
		DataObjectView dialogCompoenent = new DataObjectView(dataObject);
		if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataObject) abstractEntity)){
			DialogHandler.getInstance().getTempDialogMap().put((DataObject) abstractEntity, new SaveCancelDialogBuilder()
			.withTitle("Objekt bearbeiten")
			.withDialogComponent(dialogCompoenent)
			.withSaveAction(() -> {
				tabSheet.refreshSelectedEntityInTrees(dataObject);
				rightContainerListener.updateRightContainerTab(abstractEntity);
			})
			.build());
			dialogCompoenent.updateComponent();
		}
		else{
			DialogHandler.getInstance().getTempDialogMap().get((DataObject) abstractEntity).toFront();
		}
	}
	/**
	 * Ruft DataCategoryView mit der zu editierenden Entität auf und aktualisiert bei Bestätigung diese im Baum
	 * 
	 * @param Entity die editiert werden soll
	 * 
	 */	
	private void editCategory(AbstractEntity abstractEntity){
		DataCategory dataCategory = (DataCategory) abstractEntity;
		DataCategoryView dialogCompoenent = new DataCategoryView(dataCategory);
		if(!(((DataCategory) abstractEntity).getName()).equals("unkategorisierte Objekte")){
			if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataCategory) abstractEntity)){	
					DialogHandler.getInstance().getTempDialogMap().put((DataCategory) abstractEntity, new SaveCancelDialogBuilder()
					.withTitle("Kategorie bearbeiten")
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						tabSheet.refreshSelectedEntityInTrees(dataCategory);
						rightContainerListener.updateRightContainerTab(abstractEntity);
					})
					.build());
					dialogCompoenent.updateComponent();
				}
			else{
				DialogHandler.getInstance().getTempDialogMap().get((DataCategory) abstractEntity).toFront();
			}
		}
	}
	/**
	 * Ruft DataRelationView mit der zu editierenden Entität auf und aktualisiert bei Bestätigung diese im Baum
	 * 
	 * @param Entity die editiert werden soll
	 * 
	 */	
	private void editRelation(AbstractEntity abstractEntity){
		
		final DataRelation dataRelation;
		if(abstractEntity instanceof DataRelation){
			dataRelation = (DataRelation) abstractEntity;
		}else{
			dataRelation = getRelationOfEntity(abstractEntity);
		}
		final DataRelationView dialogCompoenent =  new DataRelationView(dataRelation);
		if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataRelation) dataRelation)){
			DialogHandler.getInstance().getTempDialogMap().put(dataRelation, new SaveCancelDialogBuilder()
			.withTitle("Relation bearbeiten")
			.withDialogComponent(dialogCompoenent)
			.withSaveAction(() -> {
				tabSheet.refreshSelectedEntityInTrees(dataRelation);
				rightContainerListener.updateRightContainerTab(abstractEntity);
			})
			.build());
			dialogCompoenent.updateComponent();
		}
		else{
			DialogHandler.getInstance().getTempDialogMap().get((DataRelation) abstractEntity).toFront();
		}
	}
	
	/**
	 * Ruft DataObjectView auf und fügt erstelltes Objekt im Baum hinzu 
	 */	
	private void addObject(){
		if(!DialogHandler.isObjectDialogOpen()){
			DataObject dataObject = new DataObject();
			DataObjectView dialogCompoenent = new DataObjectView();
			SaveCancelDialog objectDialog =
				new SaveCancelDialogBuilder()
					.withTitle("Objekt hinzufügen")
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						tabSheet.getCategoryTree().addTreeNode(dataObject);
					})
					.build();
			DialogHandler.setNewObjectDialog(objectDialog);
		}
		else {
			DialogHandler.getNewObjectDialog().toFront();
		}
	}
	
	/**
	 * Ruft DataObjectView auf und fügt erstellte Kategorie im Baum hinzu 
	 */	
	private void addCategory(){
		if(!DialogHandler.isCategoryDialogOpen()){
			DataCategory dataCategory = new DataCategory();
			
			DataCategoryView dialogCompoenent = new DataCategoryView(dataCategory);
			SaveCancelDialog categoryDialog =
				new SaveCancelDialogBuilder()
					.withTitle("Kategorie hinzufügen")
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						tabSheet.getCategoryTree().addTreeNode(dataCategory);
					})
					.build();
			DialogHandler.setNewCategoryDialog(categoryDialog);
		}
		else {
			DialogHandler.getNewCategoryDialog().toFront();
		}
	}
	
	/**
	 * Ruft DataRelationView auf und fügt erstellte Relation im Baum hinzu 
	 */	
	private void addRelation(){
		if(!DialogHandler.isRelationDialogOpen()){
			DataRelation dataRelation = new DataRelation();
			DataRelationView dialogCompoenent = new DataRelationView(dataRelation);
			SaveCancelDialog relationDialog =
				new SaveCancelDialogBuilder()
					.withTitle("Relation hinzufügen")
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						dialogCompoenent.updateModel();
						dialogCompoenent.validateComponent();
						tabSheet.getRelationTree().addTreeNode(dataRelation);
					})
					.build();
			DialogHandler.setNewRelationDialog(relationDialog);
		}
		else {
			DialogHandler.getNewRelationDialog().toFront();
		}
	}
	
	/**
	 * Überprüft ob für eine Entität eine Relation ensteht
	 * @param Entity, für die zugehörige Relationen geprüft werden
	 * @return Die zugehörige Relation, wenn eine existiert, sonst null
	 */	
	private DataRelation getRelationOfEntity(AbstractEntity abstractEntity){
		Collection<DataRelation> relationCollection = DataRelationDao.getInstance().findAll().values();
		
		for(DataRelation relation : relationCollection){
			if(!relation.getName().equals(DataRelation.CATEGORY_OBJECT_RELATION)){
				if(relation.getIdfromObjectOne().equals(abstractEntity.getId()) 
					|| relation.getIdfromObjectTwo().equals(abstractEntity.getId())){
					return relation;
				}
			}
		}
		return null;
	}

	@Override
	public void handle(Event e) {
		if(e.getSource() == editItem){
			edit(tabSheet.getSelectedTabComponent().getTreeElement());
		}else if(e.getSource() == addObject){
			addObject();
		}else if(e.getSource() == addCategory){
			addCategory();
		}else if(e.getSource() == addRelation){
			addRelation();
		}else if(e.getSource() == editRelation){
			editRelation(tabSheet.getRelationTree().getTreeElement());
		}else if(e.getSource() == deleteItem){
			delete(tabSheet.getSelectedTabComponent().getTreeElement());
		}
		
	}
	/**
	 * Setzt die anzuzeigenden MenuItems passend zum treeElement
	 * @param treeElement
	 */
	public void updateItems(AbstractEntity treeElement){
		this.getItems().removeAll(editItem, deleteItem, addCategory,addObject,addRelation, editRelation, newEntityMenu);
		this.editItem = new MenuItem("Bearbeiten");
		this.deleteItem = new MenuItem("Löschen");
		this.addCategory = new MenuItem("Kategorie hinzufügen");
		this.addObject = new MenuItem("Objekt hinzufügen");
		this.addRelation = new MenuItem("Relation hinzufügen");
		this.editRelation = new MenuItem("zugehörige Relation bearbeiten");
		this.editRelation.setDisable(false);
		this.editRelation.setOnAction(this);
		newEntityMenu = new Menu("Neu");
		
		newEntityMenu.getItems().addAll(addCategory, addObject, addRelation);
		this.getItems().add(newEntityMenu);
		
		if(treeElement == null){
			/* 
			getItems().add(addCategory);
			getItems().add(addObject);
			getItems().add(addRelation);
			*/
		}else{
			getItems().add(editItem);
			getItems().add(deleteItem);
			getItems().add(editRelation);
			if(this.getRelationOfEntity(treeElement) == null){
				editRelation.setDisable(true);
			}
		}
						
		editItem.setOnAction(this);
		deleteItem.setOnAction(this);
		addObject.setOnAction(this);
		addCategory.setOnAction(this);
		addRelation.setOnAction(this);
		
	}

	
}
