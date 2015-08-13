package incognitive.commonelements.v.graphicpresentation;

import incognitive.commonelements.mc.model.dao.DataCategoryDao;
import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicNodeConnectorSkin;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicNodeSkin;
import incognitive.commonelements.v.graphicpresentation.basicpresentation.GraphicSkinConstants;
import incognitive.database.model.AbstractEntity;
import javafx.scene.layout.BorderPane;
import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.model.GJoint;
import de.tesis.dynaware.grapheditor.model.GModel;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.model.GraphFactory;

public class EntityGraphEditor extends BorderPane {

	private final GModel graphModel;
	
	private final DataObjectDao objectDao;
	private final DataCategoryDao categoryDao;

	public EntityGraphEditor() {
		final GraphEditor graphEditor = new DefaultGraphEditor();
		
		this.setCenter(graphEditor.getView());
		this.objectDao = DataObjectDao.getInstance();
		this.categoryDao = DataCategoryDao.getInstance();
		
		//Model für die Nodes entwerfen
		this.graphModel = GraphFactory.eINSTANCE.createGModel();
		graphEditor.setModel(graphModel);
		graphEditor.setNodeSkin(GraphicSkinConstants.GRAPHIC_NODE,GraphicNodeSkin.class);
		graphEditor.setConnectorSkin(GraphicSkinConstants.GRAPHIC_NODE_INPUT_CONNECTOR,GraphicNodeConnectorSkin.class);
		graphEditor.setConnectorSkin(GraphicSkinConstants.GRAPHIC_NODE_OUTPUT_CONNECTOR,GraphicNodeConnectorSkin.class);
	}
	
	public void addNodeAsEntity(final AbstractEntity entity){
		if(entity instanceof DataObject){
			GNode node = new DataObjectNodePresentation((DataObject) entity);
			Commands.addNode(graphModel, node);
		} else if(entity instanceof DataCategory){
			GNode node = new DataCategoryNodePresentation((DataCategory) entity);
			Commands.addNode(graphModel, node);
		} else if(entity instanceof DataRelation){
			DataRelationNodePresentation relation  = new DataRelationNodePresentation(entity);
			Commands.addNode(graphModel, relation);
			GNode leftNode;
			if (((DataRelation) entity).getTypeOfObjectOne().equals("DataObject")){
				DataObject object = objectDao.findByID(((DataRelation)entity).getIdfromObjectOne());
				leftNode = new DataObjectNodePresentation(object);
				leftNode.setType(GraphicSkinConstants.GRAPHIC_NODE);
				leftNode.setX(50);
				leftNode.setY(50);
			}else{
				DataCategory category = categoryDao.findByID(((DataRelation)entity).getIdfromObjectOne());
				leftNode = new DataCategoryNodePresentation(category);
				
				leftNode.setType(GraphicSkinConstants.GRAPHIC_NODE);
				leftNode.setX(50);
				leftNode.setY(50);
			}
			
			GNode rightNode;
			if (((DataRelation) entity).getTypeOfObjectTwo().equals("DataObject")){
				DataObject object = objectDao.findByID(((DataRelation)entity).getIdfromObjectTwo());
				rightNode = new DataObjectNodePresentation(object);
				rightNode.setType(GraphicSkinConstants.GRAPHIC_NODE);
				rightNode.setX(300);
				rightNode.setY(50);
			}else{
				DataCategory category = categoryDao.findByID(((DataRelation)entity).getIdfromObjectTwo());
				rightNode = new DataCategoryNodePresentation(category);
				rightNode.setType(GraphicSkinConstants.GRAPHIC_NODE);
				rightNode.setX(300);
				rightNode.setY(50);
			}
			
			GConnection connection = GraphFactory.eINSTANCE.createGConnection();
			connection.setSource(relation.getConnectors().get(0));
			connection.setTarget(leftNode.getConnectors().get(0));
			
			GConnection connection2 = GraphFactory.eINSTANCE.createGConnection();
			connection.setSource(relation.getConnectors().get(0));
			connection.setTarget(rightNode.getConnectors().get(0));
			
			GJoint joint = GraphFactory.eINSTANCE.createGJoint();
			joint.setConnection(connection);
			connection.getJoints().add(joint);
			
			GJoint joint2 = GraphFactory.eINSTANCE.createGJoint();
			joint2.setConnection(connection2);
			connection2.getJoints().add(joint2);

			Commands.addNode(graphModel, leftNode);
			Commands.addNode(graphModel, rightNode);
			
			ConnectionCommands.addConnection(graphModel, relation.getConnectors().get(0), leftNode.getConnectors().get(0), "", connection.getJoints());	
			ConnectionCommands.addConnection(graphModel, relation.getConnectors().get(0), rightNode.getConnectors().get(0), "", connection2.getJoints());
		}
	}
}
