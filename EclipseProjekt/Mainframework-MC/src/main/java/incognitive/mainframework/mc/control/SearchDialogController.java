package incognitive.mainframework.mc.control;


import incognitive.commonelements.mc.model.dao.DataObjectDao;
import incognitive.commonelements.mc.model.entity.DataObject;

import java.util.ArrayList;

/**
 * Controller für den Suchedialog
 * @author Peter Hornik
 **/

public class SearchDialogController{
	private DataObjectDao objectDao;

	private ArrayList<DataObject> results;
	
	public SearchDialogController(){
		objectDao = DataObjectDao.getInstance();
		results = new ArrayList<DataObject>();
	}

	/**
	 * Durchsucht alle Objekte nach einer Zeichenkette
	 * @param text ist die gesuchte Zeichenkette
	 * @param searchForObj Es werden Objektnamen durchsucht 
	 * @param searchForAtt Es werden Objektattribute durchsucht
	 * @return Objekte, welche die Zeichenkette enthalten
	 **/
	
	public ArrayList<DataObject> getResults(String text, boolean searchForObj, boolean searchForAtt) {
		results.clear();
		
		objectDao.findAll().forEach((id, obj) -> {	//Alle Objekte durchgehen

			//Objektname auf Suchtext untersuchen
			if (searchForObj && obj.getName().toLowerCase().contains(text.toLowerCase())){
				results.add(obj); 	
			}

			//Attribute durchsuchen
			else if (searchForAtt &&
					(obj.getObjectMap().keySet()
						.stream().anyMatch(aKey -> aKey.toString().toLowerCase().contains(text.toLowerCase())) 
				  || obj.getObjectMap().values()
						.stream().anyMatch(aVal -> aVal.toString().toLowerCase().contains(text.toLowerCase()))) ){
				results.add(obj);
			}
				
//				for(Entry<String, Object> entry : obj.getObjectMap().entrySet()){ 
//					if (entry.getKey().toString().toLowerCase().contains(text)) {
//						results.add(obj);
//						break; 
//					}
//					else if (entry.getValue().toString().toLowerCase().contains(text)) {
//						results.add(obj);
//						break;
//					}
//				}
		});
		return results;
	}
}
