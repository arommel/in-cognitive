package incognitive.database.model;

/**
 * Enum für Entities welche die angelegten Ordnamen für Entities enthält
 * 
 * @author Lisa
 */
public enum EntityFolderName {
	
	DataObject("objects"),
	
	DataCategory("categories"),
	
	DataRelation("relations");
	
	public String folderName;

	private EntityFolderName(final String folderName) {
		this.folderName = folderName;
	}

	public String getFolderName() {
		return folderName;
	}
}
