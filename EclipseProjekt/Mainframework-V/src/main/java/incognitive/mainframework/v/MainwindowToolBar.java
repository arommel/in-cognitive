package incognitive.mainframework.v;

import incognitive.commonelements.mc.control.CommonElementsController;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.ImportView;
import incognitive.commonelements.v.entitydialog.DialogHandler;
import incognitive.commonelements.v.entitydialog.SaveCancelDialogBuilder;
import incognitive.commonelements.v.entitydialog.deletedialog.DataCategoryDelete;
import incognitive.commonelements.v.entitydialog.deletedialog.DataObjectDelete;
import incognitive.commonelements.v.entitydialog.deletedialog.DataRelationDelete;
import incognitive.commonelements.v.entitydialog.editdialog.DataCategoryView;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;
import incognitive.commonelements.v.entitydialog.editdialog.DataRelationView;
import incognitive.commonelements.v.icons.IconResource;
import incognitive.database.model.AbstractEntity;
import incognitive.navigation.v.CategoryObjectTree;
import incognitive.navigation.v.NavigationTabPane;
import incognitive.navigation.v.NavigationTreeComponent;
import incognitive.navigation.v.RelationTreeView;

import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainwindowToolBar extends ToolBar {
		
	Button toolItemImport = new Button("");
	Button toolItemEdit = new Button("");
	Button toolItemDelete = new Button("");
	Button toolItemPaste = new Button("");
//	Button toolItemUndo = new Button("");
	Separator separator = new Separator();
	Separator separator2 = new Separator();
	Button toolItemSearch = new Button("");
	Button toolItemHelp = new Button("");
	Button toolItemGraphic = new Button ("");
//	Button toolItemTable = new Button ("");
	
	NavigationTabPane navigationTabSheet;
	RightContainer rightContainer;
	private final CommonElementsController controller;
	
	public MainwindowToolBar(final NavigationTabPane navigationTabSheet, RightContainer container) {
		rightContainer = container;
		this.navigationTabSheet = navigationTabSheet;
		this.controller = CommonElementsController.getInstance();
		
		initToolBar();
	}
	
	public void initToolBar(){
		this.getItems().add(toolItemImport);
		toolItemImport.setGraphic(new ImageView(IconResource.IMPORT));
		toolItemImport.setTooltip(new Tooltip("Importieren"));
		
		this.getItems().add(toolItemEdit);
		toolItemEdit.setGraphic(new ImageView(IconResource.EDIT));
		toolItemEdit.setTooltip(new Tooltip("Bearbeiten"));
		
		this.getItems().add(toolItemDelete);
		toolItemDelete.setGraphic(new ImageView(IconResource.DELETE));
		toolItemDelete.setTooltip(new Tooltip("Löschen"));
		
		this.getItems().add(toolItemPaste);
		toolItemPaste.setGraphic(new ImageView(IconResource.PASTE));
		toolItemPaste.setTooltip(new Tooltip("Einfügen"));
		
//		this.getItems().add(toolItemUndo);
//		toolItemUndo.setGraphic(new ImageView(IconResource.UNDO));
//		toolItemUndo.setTooltip(new Tooltip("Zurück"));
		
		this.getItems().add(separator2);

		this.getItems().add(toolItemGraphic);
		toolItemGraphic.setGraphic(new ImageView(IconResource.GRAPHIC));
		toolItemGraphic.setTooltip(new Tooltip("Tab hinzufügen"));
		
		this.getItems().add(separator);
		
		this.getItems().add(toolItemSearch);
		toolItemSearch.setGraphic(new ImageView(IconResource.SEARCH));
		toolItemSearch.setTooltip(new Tooltip("Suche"));
		
		this.getItems().add(toolItemHelp);
		toolItemHelp.setGraphic(new ImageView(IconResource.HELP));
		toolItemHelp.setTooltip(new Tooltip("Hilfe"));
		
		toolBarListeners();
	}
	
		public void toolBarListeners(){
			toolItemImport.setOnAction(event -> {
				createImportDialog();
			});
			
			toolItemGraphic.setOnAction( event -> {
				rightContainer.newTab();
			});
			
			toolItemEdit.setOnAction( event  -> {
				final NavigationTreeComponent comp =  navigationTabSheet.getSelectedTabComponent();
				Object obj = comp.getTreeElement();
				
				if (obj instanceof DataObject){
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataObject) obj)){
						createDataObjectDialog((DataObject) obj);
					}
					else{
						DialogHandler.getInstance().getTempDialogMap().get((DataObject) obj).toFront();
					}
				}
				else if(obj instanceof DataCategory) {
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataCategory) obj)
							&&!((((DataCategory) obj).getName()).equals("unkategorisierte Objekte"))){
						createDataCategoryDialog((DataCategory) obj);
					}
					else{
						if(!((((DataCategory) obj).getName()).equals("unkategorisierte Objekte"))){
							DialogHandler.getInstance().getTempDialogMap().get((DataCategory) obj).toFront();
						}
					}
				}
				else if ( obj instanceof DataRelation){
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataRelation) obj)){
						createDataRelationDialog((DataRelation) obj);
					}	
					else{
						DialogHandler.getInstance().getTempDialogMap().get((DataRelation) obj).toFront();
					}
				}
			});

			toolItemDelete.setOnAction( event  -> {
				AbstractEntity entity = navigationTabSheet.getSelectedTabComponent().getTreeElement();
				
				if (entity instanceof DataObject){
					final DataObjectDelete alert = new DataObjectDelete((DataObject) entity);
					alert.withDeleteAction(() -> {
						 navigationTabSheet.deleteSelectedEntityInTrees(entity);;
					});
					alert.showDialog();
				}
				else if (entity instanceof DataCategory){ 
					final DataCategoryDelete alert = new DataCategoryDelete((DataCategory) entity);
					final AbstractEntity selectedEntity = navigationTabSheet.getSelectedTabComponent().getTreeElement();
					final List<DataObject> objectsInCategory = controller.getObjectsToCategory(selectedEntity.getId());
					alert.withNormalSaveAction(() -> {
						navigationTabSheet.deleteSelectedEntityInTrees(selectedEntity);
					});
					alert.withAdditionalSaveAction(() -> {
						for(DataObject o : objectsInCategory){
							navigationTabSheet.deleteSelectedEntityInTrees(o);
						}
					});
					alert.showDialog();
				}
				else if (entity instanceof DataRelation){
					final DataRelationDelete alert = new DataRelationDelete((DataRelation) entity);
					alert.withDeleteAction(() -> {
						navigationTabSheet.getRelationTree().deleteSelectedNode(entity);
					});
					alert.showDialog();
				}
		});
			toolItemPaste.setOnAction( event  -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Einfügen");
				alert.setHeaderText("Einfügen ");
				alert.setContentText("Was möchten Sie einfügen?");
				alert.getDialogPane().getStylesheets().add("application.css");
				
				ButtonType buttonObject = new ButtonType("Objekt");
				ButtonType buttonCategory = new ButtonType("Kategorie");
				ButtonType buttonRelation = new ButtonType("Relation");
				ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonObject, buttonCategory, buttonRelation, buttonCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonObject){
					createDataObjectDialog(null);
				} else if (result.get() == buttonCategory) {
					createDataCategoryDialog(null);
				} else if (result.get() == buttonRelation) {
				    createDataRelationDialog(null);
				} 
		});
			
			toolItemSearch.setOnAction(event -> {
				SearchDialog searchDialog = new SearchDialog(navigationTabSheet, rightContainer);
				Stage stage = new Stage();
				stage.setScene(new Scene(searchDialog, 400, 600));
				stage.getIcons().add(IconResource.LOGO);
				stage.setTitle("Suche");
				stage.show();
			});
			
			toolItemHelp.setOnAction(event -> {
				if(!DialogHandler.isHelpDialogOpen()){
					createHelpDialog();
				}			
				else{
					DialogHandler.getNewHelpDialog().show();
				}
			});
	
	}
		private void createHelpDialog(){
			Stage stage = new Stage();
			final BorderPane manual = new Manual();
	        Scene scene = new Scene(manual, 650, 550);
	        stage.setScene(scene);
	        stage.setMinWidth(600);
	        stage.setMinHeight(500);
	        stage.setTitle("Hilfe");
	        stage.getIcons().add(IconResource.LOGO);
	        scene.getStylesheets().add("application.css");
	        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

				@Override
				public void handle(WindowEvent e) {
					DialogHandler.setNewHelpDialog(null);
				}

	        });
	        DialogHandler.setNewHelpDialog(stage);
	        stage.show();
		}
	
		private void createImportDialog() {
			final CategoryObjectTree comp = navigationTabSheet
					.getCategoryTree();
			DataObject dataObject = new DataObject();
			ImportView importView = new ImportView(dataObject);
			importView.withSaveAction(() -> {
				 comp.updateTree();
			});
			
		}

		public void createDataObjectDialog(DataObject object){
			String title;
			DataObject dataObject;
		
			if(object == null){
				dataObject = new DataObject();
				title = "Objekt erstellen";
			}else{
				dataObject = object;
				title = "Objekt bearbeiten";
			}
			DataObjectView dialogCompoenent = new DataObjectView(dataObject);
			
			if(object != null){
				DialogHandler.getInstance().getTempDialogMap().put(object, new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					navigationTabSheet.refreshSelectedEntityInTrees(object);
					rightContainer.updateTab(object);
				})
				.build());
				dialogCompoenent.updateComponent();	
			}else{
				new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					navigationTabSheet.getCategoryTree().addTreeNode(dataObject);

				})
				.build();
			}
		}
		
		public void createDataCategoryDialog(DataCategory category){
			String title;
			DataCategory dataCategory;
			
			if(category == null){
				dataCategory = new DataCategory();
				title = "Kategorie erstellen";
			}else{
				dataCategory = category;
				title = "Kategorie bearbeiten";
			}
			DataCategoryView dialogCompoenent = new DataCategoryView(dataCategory);
			
			if(category !=null){
				DialogHandler.getInstance().getTempDialogMap().put(category, new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					navigationTabSheet.refreshSelectedEntityInTrees(category);
					rightContainer.updateTab(category);
				})
				.build());
				dialogCompoenent.updateComponent();
			}else{
				new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					navigationTabSheet.getCategoryTree().addTreeNode(dataCategory);
				})
				.build();
			}
		}
		
		public void createDataRelationDialog(DataRelation relation){
			String title;
			DataRelation dataRelation;
			final RelationTreeView comp = navigationTabSheet.getRelationTree();
			
			if(relation == null){
				dataRelation = new DataRelation();
				title = "Relation erstellen";
			}else{
				dataRelation = relation;
				title = "Relation bearbeiten";
			}
			final DataRelationView dialogCompoenent =  new DataRelationView(dataRelation);
		
			if(relation != null){
				DialogHandler.getInstance().getTempDialogMap().put(relation, new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					comp.refreshSelectedNode(dataRelation);
					rightContainer.updateTab(relation);
				})
				.build());
				dialogCompoenent.updateComponent();
			}else{
				new SaveCancelDialogBuilder()
				.withTitle(title)
				.withDialogComponent(dialogCompoenent)
				.withSaveAction(() -> {
					comp.addTreeNode(dataRelation);
				})
				.build();
			}
		}	
}
