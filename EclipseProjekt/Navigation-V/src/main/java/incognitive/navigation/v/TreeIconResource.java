package incognitive.navigation.v;

import javafx.scene.image.Image;

public class TreeIconResource {

	private final static String FOLDER = "Icons/";
	
	public final static Image OBJECT = new Image(ClassLoader.getSystemResource(FOLDER +"3D-icon.png").toString());
	
	public final static Image CATEGORY = new Image(ClassLoader.getSystemResource(FOLDER +"Folder-Open-icon.png").toString());

	public final static Image RELATION = new Image(ClassLoader.getSystemResource(FOLDER +"databases-relation-icon.png").toString());
}
