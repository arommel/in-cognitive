package incognitive.mainframework.v;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.ImportView;
import incognitive.commonelements.v.alert.ErrorAlert;
import incognitive.commonelements.v.entitydialog.DialogHandler;
import incognitive.commonelements.v.entitydialog.SaveCancelDialog;
import incognitive.commonelements.v.entitydialog.SaveCancelDialogBuilder;
import incognitive.commonelements.v.entitydialog.editdialog.DataCategoryView;
import incognitive.commonelements.v.entitydialog.editdialog.DataObjectView;
import incognitive.commonelements.v.entitydialog.editdialog.DataRelationView;
import incognitive.commonelements.v.icons.IconResource;
import incognitive.database.configuration.Settings;
import incognitive.navigation.v.CategoryObjectTree;
import incognitive.navigation.v.NavigationTabPane;
import incognitive.navigation.v.NavigationTreeComponent;
import incognitive.navigation.v.RelationTreeView;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainwindowMenubar extends MenuBar {
	
	protected Settings settings;
	NavigationTabPane navigationTabSheet;
	RightContainer rightContainer;
	
	Menu menuEdit = new Menu("Bearbeiten");
	
	MenuItem menuItemImport = new MenuItem("Importieren");
	MenuItem menuItemObjectEdit = new MenuItem("Ausgewähltes Objekt bearbeiten");
	MenuItem menuItemCategoryEdit = new MenuItem("Ausgewählte Kategorie bearbeiten");
	MenuItem menuItemRelationEdit = new MenuItem("Ausgewählte Relation bearbeiten");
	MenuItem menuItemDatabaseReset = new MenuItem("Datenbank zurücksetzen");
	Menu menuPaste = new Menu("Einfügen");
	
	MenuItem menuItemCategory = new MenuItem("Kategorie");
	MenuItem menuItemObject = new MenuItem("Objekt");
	MenuItem menuItemRelation = new MenuItem("Relation");
	SeparatorMenuItem seperator1 = new SeparatorMenuItem();
	MenuItem menuItemTab = new MenuItem("Neuer Tab");
	
	Menu menuSettings = new Menu("Einstellungen");
	MenuItem pathConfigItem = new MenuItem("Datenpfad");
	
	Menu menuSearch = new Menu("Suche");
	MenuItem menuItemSearch	 = new MenuItem("Suche");
	
	Menu menuInfo = new Menu("?");
	MenuItem help = new MenuItem("Hilfe");
	MenuItem impressum = new MenuItem("Impressum");
	
	
	public MainwindowMenubar(final NavigationTabPane navigationTabSheet, RightContainer container) {
		rightContainer = container;
		this.settings = new Settings();
		this.navigationTabSheet = navigationTabSheet;
		initMenuBar();
	}

		
	public void initMenuBar(){
	
		this.getMenus().add(menuEdit);
	
		menuEdit.getItems().add(menuItemImport);
		menuEdit.getItems().add(menuItemObjectEdit);
		menuEdit.getItems().add(menuItemCategoryEdit);
		menuEdit.getItems().add(menuItemRelationEdit);
		menuEdit.getItems().add(menuItemDatabaseReset);
	
		this.getMenus().add(menuPaste);
		menuPaste.getItems().add(menuItemCategory);
		menuPaste.getItems().add(menuItemObject);
		menuPaste.getItems().add(menuItemRelation);
		menuPaste.getItems().add(seperator1);
		menuPaste.getItems().add(menuItemTab);
		
		this.getMenus().add(menuSettings);
		menuSettings.getItems().add(pathConfigItem);
	
		this.getMenus().add(menuSearch);
		menuSearch.getItems().add(menuItemSearch);
		
		this.getMenus().add(menuInfo);
		menuInfo.getItems().add(help);
		menuInfo.getItems().add(impressum);
		
	
		dataObjectListeners();
		dataCategoryListeners();
		dataRelationListeners();
		databaseResetListeners();
		settingsListeners();
		importListeners();
		infoListeners();
		newTabListeners();
	
	}
	
	
	public void importListeners(){
		menuItemImport.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				createImportDialog();
			}
			
		});
	}
	public void dataObjectListeners () {
		menuItemObject.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				if (!DialogHandler.isObjectDialogOpen()){
					createDataObjectDialog(null);
				}
				else {
					DialogHandler.getNewObjectDialog().toFront();
				}
			}
		});  
			
		menuItemSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					createSearchDialog();
			}
		});	

		
		menuItemObjectEdit.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent arg0) {
				final NavigationTreeComponent comp =  navigationTabSheet.getSelectedTabComponent();
				Object obj = comp.getTreeElement();
	
				if (obj instanceof DataObject){
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataObject) obj)){
						createDataObjectDialog((DataObject) obj);
					}
					else{
						DialogHandler.getInstance().getTempDialogMap().get(obj).toFront();
					}
				} else {
					new ErrorAlert("Dialog", "Es wurde kein Objekt ausgewählt!");
				}
			}
	
		});
	}

	public void dataCategoryListeners (){
		menuItemCategory.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!DialogHandler.isCategoryDialogOpen()){
					createDataCategoryDialog(null);
				}
				else {
					DialogHandler.getNewCategoryDialog().toFront();
				}
			}
		});
		menuItemCategoryEdit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				final NavigationTreeComponent comp = navigationTabSheet
						.getSelectedTabComponent();
				Object object = comp.getTreeElement();

				if (object instanceof DataCategory
						&&!((((DataCategory) object).getName()).equals("unkategorisierte Objekte"))) {
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataCategory) object)){
						createDataCategoryDialog((DataCategory) object);
					}
					else{
						DialogHandler.getInstance().getTempDialogMap().get(object).toFront();
					}
				} else {
					new ErrorAlert("Dialog", "Es wurde keine Kategorie ausgewählt!");
				}
			}
		});
	}
	
	public void dataRelationListeners (){
		menuItemRelation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!DialogHandler.isRelationDialogOpen()){
					createDataRelationDialog(null);
				}
				else {
					DialogHandler.getNewRelationDialog().toFront();
				}
			}
		});
		
		menuItemRelationEdit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				final NavigationTreeComponent comp = navigationTabSheet
						.getSelectedTabComponent();
				Object object = comp.getTreeElement();

				if (object instanceof DataRelation) {
					if(!DialogHandler.getInstance().getTempDialogMap().containsKey((DataRelation) object)){
						createDataRelationDialog((DataRelation) object);
					}
					else{
						DialogHandler.getInstance().getTempDialogMap().get(object).toFront();
					}
				} else {
					new ErrorAlert("Dialog", "Es wurde keine Relation ausgewählt!");
				}
			}
		});
	}
	
	public void databaseResetListeners (){
		menuItemDatabaseReset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Datenbank zurücksetzen");
				alert.setHeaderText("Wollen Sie die Datenbank wirklich zurücksetzen?");
				alert.setContentText(null);
				alert.getDialogPane().getStylesheets().add("application.css");
				
				ButtonType resetButton = new ButtonType("Ja");
				ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);	
					
				alert.getButtonTypes().setAll(resetButton, cancelButton);
								
				final Optional<ButtonType> result = alert.showAndWait();;
				if(result.get() == resetButton){
					
					DatabaseReset.deleteMain();
				}
					
			}
			
		});
	}
	
	

	public void newTabListeners (){
		menuItemTab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				rightContainer.newTab();
			}
		});
	}
	
	public void settingsListeners (){
		pathConfigItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if(settings.openDataPathChangeWindow()){
					try {
						Main.restart();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void infoListeners(){
		help.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(!DialogHandler.isHelpDialogOpen()){
					createHelpDialog();
				}			
				else{
					DialogHandler.getNewHelpDialog().show();
				}
			}
		});
		impressum.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if (!DialogHandler.isImpressumDialogOpen()){
					createImpressum();
				}
				else DialogHandler.getNewImpressumDialog().show();
			}
		});
	}

	private void createSearchDialog() {
		SearchDialog searchDialog = new SearchDialog(navigationTabSheet, rightContainer);
		Stage st = new Stage();
		st.setScene(new Scene(searchDialog, 400, 600));
		st.getIcons().add(IconResource.LOGO);
		st.setTitle("Suche");
	}

	public void createImportDialog(){
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
			SaveCancelDialog objectDialog = 
					new SaveCancelDialogBuilder()
					.withTitle(title)
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						navigationTabSheet.getCategoryTree().addTreeNode(dataObject);
					})
					.build();
			DialogHandler.setNewObjectDialog(objectDialog);
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
			SaveCancelDialog categoryDialog = 
					new SaveCancelDialogBuilder()
					.withTitle(title)
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						navigationTabSheet.getCategoryTree().addTreeNode(dataCategory);
					})
					.build();
			DialogHandler.setNewCategoryDialog(categoryDialog);
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
			SaveCancelDialog relationDialog =
					new SaveCancelDialogBuilder()
					.withTitle(title)
					.withDialogComponent(dialogCompoenent)
					.withSaveAction(() -> {
						comp.addTreeNode(dataRelation);
					})
					.build();
			DialogHandler.setNewRelationDialog(relationDialog);
		}
	}
	
	private void createHelpDialog() {
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
	
	private void createImpressum() {
		final Stage impressum = new Impressum();
		impressum.setResizable(false);
		impressum.setTitle("Impressum");
		impressum.getIcons().add(IconResource.LOGO);
		impressum.show();
		impressum.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent e) {
				DialogHandler.setNewImpressumDialog(null);
			}

        });
        DialogHandler.setNewImpressumDialog(impressum);
	}
}



