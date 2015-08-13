package incognitive.commonelements.v.component.properties;

import javafx.beans.property.SimpleStringProperty;

public class TagTableProperty implements TableColumnProperties{

	private final SimpleStringProperty tag;

	public TagTableProperty(String tag) {
		super();
		this.tag = new SimpleStringProperty(tag);
	}

	public String getTag() {
		return tag.get();
	}
	
	public void setTag(final String tag){
		this.tag.set(tag);
	}
}
