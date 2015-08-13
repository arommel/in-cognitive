package incognitive.commonelements.v.component.properties;

import incognitive.commonelements.mc.model.entity.DataObject;
import javafx.beans.property.SimpleObjectProperty;

public class DataObjectTableProperty implements TableColumnProperties {

	private final SimpleObjectProperty<DataObject> objectProperty;
	
	public DataObjectTableProperty(DataObject object) {
		this.objectProperty = new SimpleObjectProperty<DataObject>(object);
	}
	
	public DataObject getObject() {
		return objectProperty.get();
	}

	public void setObjectName(DataObject object) {
		this.objectProperty.set(object);
	}
	
}
