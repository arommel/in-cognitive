package incognitive.commonelements.v.graphicpresentation;

import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.AbstractEntityGNode;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicSkinConstants;
import incognitive.database.model.AbstractEntity;

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

/**
 * 
 * Grafische Darstellung einer Relation
 * @author Yuliya Akulova
 *
 **/

public class DataRelationNodePresentation extends AbstractEntityGNode {
	
	private final DataRelation relation;
	
	private final static String STYLE_CLASS_HEADER = "graphic-node-relation-header";

	public DataRelationNodePresentation(AbstractEntity entity) {
		super(entity);
		super.setType(GraphicSkinConstants.GRAPHIC_NODE);
		
		//Output Connector
		GConnector output = GraphFactory.eINSTANCE.createGConnector(); 
		super.getConnectors().add(output);
		output.setType(GraphicSkinConstants.GRAPHIC_NODE_OUTPUT_CONNECTOR);
		
		this.relation = (DataRelation) entity;
	}

	@Override
	public Node getNodeLayout() {
		final GridPane gridLayout = new GridPane();
		gridLayout.setPadding(new Insets(5, 5, 5, 5));
		gridLayout.setVgap(5);
		
		int row = 0;
		for(Entry<String, Object> entry : relation.getAttributes().entrySet()){
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
