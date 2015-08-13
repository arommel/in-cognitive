package incognitive.navigation.v;

import incognitive.commonelements.mc.model.entity.DataCategory;
import incognitive.commonelements.mc.model.entity.DataObject;
import incognitive.commonelements.mc.model.entity.DataRelation;
import incognitive.database.model.AbstractEntity;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;

public class NavigationTreeViewCellRenderer extends TreeCell<AbstractEntity> {
	
	@Override
    public void updateItem(AbstractEntity item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
        	if(item instanceof DataCategory){
        		setGraphic(new ImageView(TreeIconResource.CATEGORY));
        	} else if(item instanceof DataObject){
        		setGraphic(new ImageView(TreeIconResource.OBJECT));
        		
        	} else if (item instanceof DataRelation){
        		setGraphic(new ImageView(TreeIconResource.RELATION));
        	}
        	setText(item.toString());
        }
    }

}
