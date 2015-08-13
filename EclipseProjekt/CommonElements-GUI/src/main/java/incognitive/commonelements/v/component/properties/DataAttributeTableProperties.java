package incognitive.commonelements.v.component.properties;

import incognitive.commonelements.mc.model.DataAttributType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class DataAttributeTableProperties implements TableColumnProperties {

	private final SimpleStringProperty attributName;
	private final SimpleObjectProperty<DataAttributType> attributTyp;
	private final SimpleStringProperty attributValue;

	public DataAttributeTableProperties(String attributName,
			DataAttributType attributTyp, String attributValue) {
		super();
		this.attributName = new SimpleStringProperty(attributName);
		this.attributTyp = new SimpleObjectProperty<DataAttributType>(
				attributTyp);
		this.attributValue = new SimpleStringProperty(attributValue);
	}

	public String getAttributName() {
		return attributName.get();
	}

	public void setAttributName(String attributName) {
		this.attributName.set(attributName);
	}

	public DataAttributType getAttributTyp() {
		return attributTyp.get();
	}

	public void setAttributTyp(DataAttributType attributTyp) {
		this.attributTyp.set(attributTyp);
	}

	public String getAttributValue() {
		return attributValue.get();
	}

	public void setAttributValue(String attributValue) {
		this.attributValue.set(attributValue);
	}

}
