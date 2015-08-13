package incognitive.commonelements.v.graphicpresentation;

import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.AbstractEntityGNode;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicSkinConstants;

import java.util.Map.Entry;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GraphFactory;

public class DataObjectNodePresentation extends AbstractEntityGNode {
	
	private final DataObject object;
	
	private final static String STYLE_CLASS_HEADER = "graphic-node-object-header";

	public DataObjectNodePresentation(final DataObject entity) {
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
		
		this.object = entity;
	}

	@Override
	public Node getNodeLayout() {
		final GridPane gridLayout = new GridPane();
		gridLayout.setPadding(new Insets(5, 5, 5, 5));
		gridLayout.setVgap(5);
		
		int row = 0;
		for(Entry<String, Object> entry : object.getObjectMap().entrySet()){
			final Label key = new Label(entry.getKey() +  " : "  );
			final Label value = new Label(entry.getValue().toString());
			gridLayout.addRow(row, key, value);
			row ++;
		}
		ScrollPane sp = new ScrollPane(gridLayout);
		VBox.setVgrow(sp, Priority.ALWAYS);
		
		return sp;
	}

	@Override
	public String getHeaderStyle() {
		return STYLE_CLASS_HEADER;
	}

}
