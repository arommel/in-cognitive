package incognitive.commonelements.v.graphicpresentation.basicpresentation;

import incognitive.database.model.AbstractEntity;
import javafx.scene.Node;
import de.tesis.dynaware.grapheditor.model.impl.GNodeImpl;

/**
 * Workarround um eine spezielles Node Layout zu erstellen
 * 
 * @author Lisa Leuschner
 */
public abstract class AbstractEntityGNode extends GNodeImpl  {
	
	private final AbstractEntity entity;

	public AbstractEntityGNode(AbstractEntity entity) {
		super();
		this.entity = entity;
	}

	/**
	 * Gibt den Title der Node zurück
	 * 
	 * @return Titel
	 */
	public String getNodeTitle(){
		return entity.getName();
	}
	
	/**
	 * Gibt das spezielle Layout der Node zurück
	 * 
	 * @return Layout
	 */
	public abstract Node getNodeLayout();
	
	/**
	 * Gibt die CSS Klasse für den Header zurück
	 * 
	 * @return CSS Klasse als String
	 */
	public abstract String getHeaderStyle();
}
