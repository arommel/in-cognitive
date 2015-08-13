package incognitive.mainframework.v;

import incognitive.database.model.AbstractEntity;
import incognitive.navigation.mc.control.RightContainerListener;
import incognitive.navigation.v.NavigationTabPane;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Mainwindow extends BorderPane implements RightContainerListener {
	
	private SplitPane splitPane;
	
	private NavigationTabPane tabSheet;
	
	private RightContainer rightContainer = new RightContainer();
	
	public Mainwindow() {
		initGUI();
	}
	
	private void initGUI() {
		this.tabSheet = new NavigationTabPane(this);
		
		final VBox barLayout = new VBox();
		barLayout.getChildren().add(new MainwindowMenubar(tabSheet, rightContainer));
		barLayout.getChildren().add(new MainwindowToolBar(tabSheet, rightContainer));
		
		this.setTop(barLayout);
		
		this.splitPane = new SplitPane();
	    splitPane.setOrientation(Orientation.HORIZONTAL);
	    splitPane.getItems().add(tabSheet);
	    splitPane.getItems().add(rightContainer);
	    
	    this.setCenter(splitPane);
	}

	@Override
	public void setupRightContainer(final AbstractEntity entity) {
		
		String title = entity.toString();
		rightContainer.newTableTab(title, entity);
	}

	@Override
	public void updateRightContainerTab(AbstractEntity entity) {
		rightContainer.updateTab(entity);
		
	}
}
