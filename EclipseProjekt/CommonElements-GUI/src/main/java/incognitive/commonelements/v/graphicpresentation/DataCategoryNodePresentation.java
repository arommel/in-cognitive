package incognitive.commonelements.v.graphicpresentation;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.AbstractEntityGNode;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicSkinConstants;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GraphFactory;

public class DataCategoryNodePresentation extends AbstractEntityGNode{
	
	private final DataCategory category;
	
	private final static String STYLE_CLASS_HEADER = "graphic-node-category-header";

	public DataCategoryNodePresentation(final DataCategory entity) {
		super(entity);
		
		super.setType(GraphicSkinConstants.GRAPHIC_NODE);
		
		//Input Connector
		GConnector input = GraphFactory.eINSTANCE.createGConnector();
		super.getConnectors().add(input);
		input.setType(GraphicSkinConstants.GRAPHIC_NODE_INPUT_CONNECTOR);

		//Output Connector
		GConnector output = GraphFactory.eINSTANCE.createGConnector(); 
		super.getConnectors().add(output);
		output.setType(GraphicSkinConstants.GRAPHIC_NODE_OUTPUT_CONNECTOR);
		
		this.category = entity;
	}

	public Node getNodeLayout() {
		final VBox vbox = new VBox();
		vbox.setPadding(new Insets(5, 5, 5, 5));
		vbox.setSpacing(5);
		
		for(String tag : category.getTagsSet()){
			final Label taglabel = new Label(tag);
			vbox.getChildren().add(taglabel);
		}
		ScrollPane sp = new ScrollPane(vbox);
		VBox.setVgrow(sp, Priority.ALWAYS);
		
		return sp;
	}

	@Override
	public String getHeaderStyle() {
		return STYLE_CLASS_HEADER;
	}


}
