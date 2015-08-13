package incognitive.commonelements.v.graphicpresentation.basicpresentation;

import incognitive.commonelements.v.graphicpresentation.DataRelationNodePresentation;

import java.util.List;

import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;

public class GraphicNodeSkin extends GNodeSkin {

    private static final String STYLE_CLASS_BORDER = "graphic-node-border";
    private static final String STYLE_CLASS_BACKGROUND = "graphic-node-background";
    private static final String STYLE_CLASS_SELECTION_HALO = "graphic-node-selection-halo";
    private static final String STYLE_CLASS_TITLE = "graphic-node-title";
    private static final String STYLE_CLASS_BUTTON = "graphic-node-close-button";

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;

    private static final double MIN_WIDTH = 81;
    private static final double MIN_HEIGHT = 81;

    private static final int BORDER_WIDTH = 1;
    private static final int HEADER_HEIGHT = 20;

    private final Rectangle selectionHalo = new Rectangle();

    private VBox contentRoot = new VBox();
    private HBox header = new HBox();
    private Label title = new Label();

    private GConnectorSkin inputConnectorSkin;
    private GConnectorSkin outputConnectorSkin;

    private final Rectangle border = new Rectangle();

    /**
     * Creates a new {@link TitledNodeSkin} instance.
     *
     * @param node the {link GNode} this skin is representing
     */
    public GraphicNodeSkin(final GNode node) {
        super(node);

        border.getStyleClass().setAll(STYLE_CLASS_BORDER);
        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());

        getRoot().getChildren().add(border);
        getRoot().setMinSize(MIN_WIDTH, MIN_HEIGHT);

        addSelectionHalo();
        addSelectionListener();

        createContent();

        contentRoot.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::filterMouseDragged);
    }

    @Override
    public void initialize() {
        super.initialize();
        title.setText(((AbstractEntityGNode)getNode()).getNodeTitle());
    }
    
    @Override
    public void setConnectorSkins(final List<GConnectorSkin> connectorSkins) {

        removeConnectors();

        if (connectorSkins != null) {
            for (final GConnectorSkin skin : connectorSkins) {

            	if (GraphicSkinConstants.GRAPHIC_NODE_OUTPUT_CONNECTOR.equals(skin.getConnector().getType())) {
                    outputConnectorSkin = skin;
                    getRoot().getChildren().add(skin.getRoot());
                } else if (GraphicSkinConstants.GRAPHIC_NODE_INPUT_CONNECTOR.equals(skin.getConnector().getType())) {
                    inputConnectorSkin = skin;
                    getRoot().getChildren().add(skin.getRoot());
                }
            }
        }

        setConnectorsSelected(isSelected());
    }

    @Override
    public void layoutConnectors() {
    	if(getNode() instanceof DataRelationNodePresentation){
    		layoutTopAndBottomConnectors();
    	} else {
    		layoutLeftAndRightConnectors();
    	}
        layoutSelectionHalo();
    }

    @Override
    public Point2D getConnectorPosition(final GConnectorSkin connectorSkin) {

        final Node connectorRoot = connectorSkin.getRoot();

        final double x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
        final double y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;

        return new Point2D(x, y);
    }

    /**
     * Creates the content of the node skin - header, title, close button, etc.
     */
    private void createContent() {
        header.getStyleClass().setAll(((AbstractEntityGNode)getNode()).getHeaderStyle());
        header.setAlignment(Pos.CENTER);
        
        //Entity Name
        title.getStyleClass().setAll(STYLE_CLASS_TITLE);
        
        //Filler
        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        
        //Close Button
        final Button closeButton = new Button();
        closeButton.getStyleClass().setAll(STYLE_CLASS_BUTTON);
        final Label text = new Label("X");
        text.getStyleClass().setAll(STYLE_CLASS_TITLE);
        closeButton.setGraphic(text);
        closeButton.setCursor(Cursor.DEFAULT);
        closeButton.setOnAction(event -> Commands.removeNode(getGraphEditor().getModel(), getNode()));
        
        header.getChildren().addAll(title, filler, closeButton);
        
        //Baue Content
        contentRoot.getChildren().add(header);
        //Adde Node Layout
        contentRoot.getChildren().add(((AbstractEntityGNode)getNode()).getNodeLayout());
        
        contentRoot.minWidthProperty().bind(getRoot().widthProperty());
        contentRoot.prefWidthProperty().bind(getRoot().widthProperty());
        contentRoot.maxWidthProperty().bind(getRoot().widthProperty());
        contentRoot.minHeightProperty().bind(getRoot().heightProperty());
        contentRoot.prefHeightProperty().bind(getRoot().heightProperty());
        contentRoot.maxHeightProperty().bind(getRoot().heightProperty());

        contentRoot.setLayoutX(BORDER_WIDTH);
        contentRoot.setLayoutY(BORDER_WIDTH);
        contentRoot.getStyleClass().setAll(STYLE_CLASS_BACKGROUND);
        
        getRoot().getChildren().add(contentRoot);
    }

    /**
     * Lays out all connectors.
     */
    private void layoutLeftAndRightConnectors() {

    	//Input
        final double inputOffsetY = (getRoot().getHeight() - HEADER_HEIGHT) / 2;

        final Node connectorRoot = inputConnectorSkin.getRoot();

        final double layoutX = GeometryUtils.moveOnPixel(0 - inputConnectorSkin.getWidth() / 2);
        final double layoutY = GeometryUtils.moveOnPixel(inputOffsetY - inputConnectorSkin.getHeight() / 2);

        connectorRoot.setLayoutX(layoutX);
        connectorRoot.setLayoutY(layoutY + HEADER_HEIGHT);

        //Output
        final double outputOffsetY = (getRoot().getHeight() - HEADER_HEIGHT) / 2;

        final Node connectorRoot1 = outputConnectorSkin.getRoot();

        final double layoutX1 = GeometryUtils.moveOnPixel(getRoot().getWidth() - outputConnectorSkin.getWidth() / 2);
        final double layoutY1 = GeometryUtils.moveOnPixel(outputOffsetY - outputConnectorSkin.getHeight() / 2);

        connectorRoot1.setLayoutX(layoutX1);
        connectorRoot1.setLayoutY(layoutY1 + HEADER_HEIGHT);
        
    }
    
    /**
     * Lays out all connectors.
     */
    private void layoutTopAndBottomConnectors() {

        if (inputConnectorSkin != null) {

            final double inputX = (getRoot().getWidth() - inputConnectorSkin.getWidth()) / 2;
            final double inputY = -inputConnectorSkin.getHeight() / 2;

            inputConnectorSkin.getRoot().setLayoutX(inputX);
            inputConnectorSkin.getRoot().setLayoutY(inputY);
        }

        if (outputConnectorSkin != null) {

            final double outputX = (getRoot().getWidth() - outputConnectorSkin.getWidth()) / 2;
            final double outputY = getRoot().getHeight() - outputConnectorSkin.getHeight() / 2;

            outputConnectorSkin.getRoot().setLayoutX(outputX);
            outputConnectorSkin.getRoot().setLayoutY(outputY);
        }
    }


    /**
     * Adds the selection halo and initializes some of its values.
     */
    private void addSelectionHalo() {

        getRoot().getChildren().add(selectionHalo);

        selectionHalo.setManaged(false);
        selectionHalo.setMouseTransparent(false);
        selectionHalo.setVisible(false);

        selectionHalo.setLayoutX(-HALO_OFFSET);
        selectionHalo.setLayoutY(-HALO_OFFSET);

        selectionHalo.getStyleClass().add(STYLE_CLASS_SELECTION_HALO);
    }

    /**
     * Lays out the selection halo based on the current width and height of the node skin region.
     */
    private void layoutSelectionHalo() {

        if (selectionHalo.isVisible()) {

            selectionHalo.setWidth(getRoot().getWidth() + 2 * HALO_OFFSET);
            selectionHalo.setHeight(getRoot().getHeight() + 2 * HALO_OFFSET);

            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = getRoot().getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = getRoot().getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            selectionHalo.setStrokeDashOffset(HALO_CORNER_SIZE);
            selectionHalo.getStrokeDashArray().setAll(cornerLength, yGap, cornerLength, xGap);
        }
    }

    /**
     * Adds a listener to react to whether the node is selected or not and change the style accordingly.
     */
    private void addSelectionListener() {

        selectedProperty().addListener((v, o, n) -> {

            if (n) {
                selectionHalo.setVisible(true);
                layoutSelectionHalo();
                contentRoot.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
                getRoot().toFront();
            } else {
                selectionHalo.setVisible(false);
                contentRoot.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false);
            }

            setConnectorsSelected(n);
        });
    }

    /**
     * Removes any input and output connectors from the list of children, if they exist.
     */
    private void removeConnectors() {

        if (inputConnectorSkin != null) {
            getRoot().getChildren().remove(inputConnectorSkin.getRoot());
        }

        if (outputConnectorSkin != null) {
            getRoot().getChildren().remove(outputConnectorSkin.getRoot());
        }
    }

    /**
     * Adds or removes the 'selected' pseudo-class from all connectors belonging to this node.
     * 
     * @param isSelected {@code true} to add the 'selected' pseudo-class, {@code false} to remove it
     */
    private void setConnectorsSelected(final boolean isSelected) {

    	 if (inputConnectorSkin != null) {
    		 inputConnectorSkin.setSelected(isSelected);
         }
    	 
    	 if (outputConnectorSkin != null) {
    		 outputConnectorSkin.setSelected(isSelected);
         }
    }

    /**
     * Stops the node being dragged if it isn't selected.
     * 
     * @param event a mouse-dragged event on the node
     */
    private void filterMouseDragged(final MouseEvent event) {
        if (event.isPrimaryButtonDown() && !isSelected()) {
            event.consume();
        }
    }
}
