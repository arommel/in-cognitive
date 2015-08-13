package incognitive.database;

import incognitive.database.configuration.Settings;
import incognitive.database.model.AbstractEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Klasse welche auf den XML Dateien arbeitet.
 * 
 * Sie bietet Anfangs die Methode zum laden der Entities an und führt für die Datenzugriffsobjekte die Speichern (Update) 
 * und Lösch Zugriffe auf den Dateien aus. 
 * 
 * Die zu bearbeitetende Datei wird für bestehende Objekte aus dem Filename Attribut der Entity geholt
 * und für neue Objekte ermittelt, indem die letzte Datei im Entity Ordner genommen wird, eine neue erstellt wird wenn die letzte Datei voll ist (= MAX_OBJECT_SIZE)
 * oder falls noch keine Dateien vorhanden sind die Datei 0 erstellt wird.
 * 
 * Die Entities werden als XML mittels JDOM gespeichert in dem die @Xml... Annatotationen der Entity Klassen ausgenutzt werden.
 * 
 * @author Lisa Leuschner
 */
public class FileWorker {
	private String configPath = "";
	private String dataPath;

	private final static int MAX_OBJECT_SIZE = 100;
	
	private final static String XML_TYPE_INT = "xs:integer";
	private final static String XML_TYPE_DBL = "xs:decimal";
	private final static String XML_TYPE_STRING = "xs:string";

	public void updateObject(final AbstractEntity entity) throws JDOMException,
			IOException, ReflectiveOperationException {
		final String filePath = entity.getFileName();
		this.dataPath = getDatapath();
		SAXBuilder builder = new SAXBuilder();
		final Class<?> c = entity.getClass();

		final String fileName;
		final Document doc;
		if (filePath == null) {
			String fileFolderPath = dataPath + "/" + entity.getFolderName()
					+ "/";
			File f = new File(fileFolderPath);
			String[] temp = f.list();
			if (temp.length == 0) {
				final String entityName = 
						entity.getFolderName().substring(0, 1).toUpperCase() + entity.getFolderName().substring(1,entity.getFolderName().length());
				fileName = fileFolderPath + entityName + "_0.xml";
				doc = new Document(new Element(entity.getFolderName()));
			} else {
				final String lastFileName = temp[temp.length - 1];
				Document document = (Document) builder.build(fileFolderPath
						+ lastFileName);
				final int currentSize = document
						.getRootElement()
						.getChildren(
								c.getAnnotation(XmlRootElement.class).name())
						.size();
				if (currentSize < MAX_OBJECT_SIZE) {
					doc = document;
					fileName = fileFolderPath + lastFileName;
				} else {
					fileName = fileFolderPath+ lastFileName.replaceFirst(
							Integer.toString(temp.length - 1),
							Integer.toString(temp.length));
					doc = new Document(new Element(entity.getFolderName()));
				}
			}
		} else {
			// Entity löschen wenn schon vorhanden und neu erstellen
			fileName = entity.getFileName();
			doc = (Document) builder.build(entity.getFileName());
			deleteEntity(entity, doc);
		}
		if (entity.getId() == null) {
			entity.setId(new Settings().requestNewId(entity));
		}

		final Element newElement = createXMLElement(entity);
		doc.getRootElement().addContent(newElement);

		OutputStreamWriter streamWriter = null;
		BufferedWriter bw = null;
		try {
			final XMLOutputter xmlOutput = new XMLOutputter();
			streamWriter = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
			bw = new BufferedWriter(streamWriter);
			
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, bw);
			entity.setFileName(fileName);
		} finally {
			streamWriter.close();
			bw.close();
		}
	}

	public Map<BigInteger, AbstractEntity> loadFilesForEntity(final String classPath, final String folderName)
			throws JDOMException, IOException, ReflectiveOperationException {
		final Map<BigInteger, AbstractEntity> entityList = new HashMap<BigInteger, AbstractEntity>();

		this.dataPath = getDatapath();
		SAXBuilder builder = new SAXBuilder();

		Class<?> c = Class.forName(classPath);
		String fileFolderPath = dataPath + "/" + folderName + "/";
		File f = new File(fileFolderPath);
		String[] temp = f.list();
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {

				Document document = (Document) builder.build(fileFolderPath
						+ temp[i]);
				Element rootNode = document.getRootElement();

				for (Element e : rootNode.getChildren(c.getAnnotation(
						XmlRootElement.class).name())) {
					final AbstractEntity entity = (AbstractEntity) createEntity(
							e, c);
					entity.setFileName(fileFolderPath + temp[i]);
					entityList.put(entity.getId(), entity);
				}

			}
		}

		return entityList;
	}

	/**
	 * Erstellt ein Entität Objekt aus dem XML Element dabei werden alle
	 * Unterelemente(auch in Maps/Sets) als Strings behandelt mit Ausnahme von @XMLAttribute
	 * notierten, sowie @XMLElement Element Annotationen die im namen "id"
	 * enthalten diese werden als BigInteger gespeichert
	 * 
	 * @param xmlElement
	 *            Element im XML Baum
	 * @param c
	 *            Klasse des Objekts
	 * 
	 * @return ausgelesene Entität
	 * 
	 * @throws ReflectiveOperationException
	 * @throws ClassNotFoundException
	 */
	public Object createEntity(final Element xmlElement, final Class<?> c) throws ReflectiveOperationException, ClassNotFoundException {
		final Object o = c.newInstance();

		for (Field field : c.getDeclaredFields()) {
			field.setAccessible(true);

			String childElementname = null;
			if (field.getAnnotation(XmlElement.class) != null) {
				childElementname = field.getAnnotation(XmlElement.class).name();
			}

			// Collections, Map
			if (field.getAnnotation(XmlElementWrapper.class) != null) {
				final String fieldTypeName = field.getType().getSimpleName().toLowerCase();
				final Element wrapperElement = xmlElement.getChild(field.getAnnotation(XmlElementWrapper.class).name());
				// Feld ist ein Set<> oder eine List<>
				if (fieldTypeName.contains("set") || fieldTypeName.contains("list")) {
					setCollectionFieldValue(o, field, wrapperElement, childElementname);
				// Feld ist eine Map<>
				} else if (fieldTypeName.contains("map")) {
					setMapFieldValue(o, field, wrapperElement);
				}
			// Attribute
			} else if (field.getAnnotation(XmlAttribute.class) != null) {
				final String id = xmlElement.getAttribute(field.getAnnotation(XmlAttribute.class).name()).getValue();
				field.set(o, new BigInteger(id));

			// Normales Feld
			} else if (childElementname != null) {
				final String element = xmlElement.getChild(childElementname).getText();
				if (childElementname.contains("id")) {
					field.set(o, new BigInteger(element));
				} else {
					field.set(o, element);
				}
			}
		}

		return o;
	}

	// Setzt automatisch die Werte ins Feld
	@SuppressWarnings("unchecked")
	private void setCollectionFieldValue(
			final Object o, final Field field, final Element wrapperElement, final String childElementname)
					throws ReflectiveOperationException, ClassNotFoundException {
		
		final Collection<Object> collection = (Collection<Object>) field.get(o).getClass().newInstance();
		final Class<?> innerType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		for (Element entryChild : wrapperElement.getChildren(childElementname)) {
			collection.add(getCorrectValue(innerType, entryChild.getValue()));
		}
		field.set(o, collection);
	}

	@SuppressWarnings("unchecked")
	private void setMapFieldValue(final Object o, final Field field, final Element wrapperElement) throws ReflectiveOperationException {
		final Map<Object, Object> map = (Map<Object, Object>) field.get(o).getClass().newInstance();
		for (Element entryChild : wrapperElement.getChildren("entry")) {
			final String key = entryChild.getChild("key").getValue();
			final Element valueElement = entryChild.getChild("value");
			final Attribute valueType = valueElement.getAttribute("type");
			Object value = null;
			if(valueType!=null){
				try {
					if(valueType.getValue().equals(XML_TYPE_INT)){
						value = Integer.valueOf(entryChild.getChild("value").getValue());
					} else if(valueType.getValue().equals(XML_TYPE_DBL)){
						value = Double.valueOf(entryChild.getChild("value").getValue());
					}
				} catch (NumberFormatException e) {
					value = null;
				}
			}
			
			if(value == null){
				value = entryChild.getChild("value").getValue();
			}
			map.put(key, value);
		}
		field.set(o, map);
	}

	private Object getCorrectValue(final Class<?> returningClass,
			final String xmlElementValue) {
		if (returningClass == BigInteger.class) {
			return new BigInteger(xmlElementValue);
		} else {
			return xmlElementValue;
		}
	}

	@SuppressWarnings("unchecked")
	private Element createXMLElement(final AbstractEntity entity) throws ReflectiveOperationException {
		final Class<?> classToSave = entity.getClass();
		final String elementName = classToSave.getAnnotation(XmlRootElement.class).name();
		final Element element = new Element(elementName);
		
		for (Field field : classToSave.getDeclaredFields()) {
			field.setAccessible(true);

			String childElementname = null;
			if (field.getAnnotation(XmlElement.class) != null) {
				childElementname = field.getAnnotation(XmlElement.class).name();
			}
			
			// Collections, Map
			if (field.getAnnotation(XmlElementWrapper.class) != null) {
				final String fieldTypeName = field.getType().getSimpleName().toLowerCase();
				final Element wrapperElement = new Element(field.getAnnotation(XmlElementWrapper.class).name());

				// Feld ist ein Set<> oder eine List<>
				if (fieldTypeName.contains("set") || fieldTypeName.contains("list")) {
					final Collection<Object> collection = (Collection<Object>) field.get(entity);
					for (Object o : collection) {
						final Element innerElement = new Element(childElementname).setText(o.toString());
						wrapperElement.addContent(innerElement);
					}
					// Feld ist eine Map<>
				} else if (fieldTypeName.contains("map")) {
					final Map<Object, Object> map = (Map<Object, Object>) field.get(entity);
					for (Entry<Object, Object> entry : map.entrySet()) {
						final Element entryElement = new Element("entry");
						final Element keyElement = new Element("key").setText(entry.getKey().toString());
						final Element valueElement = new Element("value").setText(entry.getValue().toString());
						
						if(entry.getValue() instanceof Integer){
							valueElement.setAttribute(new Attribute("type",XML_TYPE_INT));
						} else if(entry.getValue() instanceof Double){
							valueElement.setAttribute(new Attribute("type",XML_TYPE_DBL));
						} else {
							valueElement.setAttribute(new Attribute("type",XML_TYPE_STRING));
						}
						entryElement.addContent(keyElement);
						entryElement.addContent(valueElement);
						wrapperElement.addContent(entryElement);
					}
				}
				element.addContent(wrapperElement);
			// Attribute
			} else if (field.getAnnotation(XmlAttribute.class) != null) {
				element.setAttribute(new Attribute(field.getAnnotation(XmlAttribute.class).name(), field.get(entity).toString()));
			// Normales Feld
			} else if (childElementname != null) {
				element.addContent(new Element(childElementname).setText(field.get(entity).toString()));
			}
		}

		return element;
	}

	/**
	 * Löscht eine Entity
	 * 
	 * @param entity
	 * @param document
	 *            Dokument wenn schon eines geöffnet ist oder null wenn keins
	 *            vorhanden ist
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void deleteEntity(final AbstractEntity entity, final Document document) throws JDOMException, IOException {
		final Class<?> c = entity.getClass();
		final SAXBuilder builder = new SAXBuilder();
		
		final Document doc;
		if (document != null) {
			doc = document;
		} else {
			doc = (Document) builder.build(entity.getFileName());
		}

		final Element toDeleteElement = doc
				.getRootElement()
				.getChildren(c.getAnnotation(XmlRootElement.class).name())
				.stream()
				.filter(e -> new BigInteger(e.getAttribute("id").getValue())
						.equals(entity.getId())).collect(Collectors.toList())
				.get(0);
		toDeleteElement.detach();
		
		if (document == null) {
			OutputStreamWriter streamWriter = null;
			BufferedWriter bw = null;
			try {
				final XMLOutputter xmlOutput = new XMLOutputter();
				streamWriter = new OutputStreamWriter(new FileOutputStream(entity.getFileName()), "UTF-8");
				bw = new BufferedWriter(streamWriter);
				
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, bw);
			} finally {
				streamWriter.close();
				bw.close();
			}
		}
	}
	
	/**
	 * Löscht den ID Eintrag in der Objektliste in der Kategorie Datei
	 * 
	 * @param objectID ID des DataObjekts
	 * @param entity die betreffende Kategorie
	 * 
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void deleteDataObjectInCategory(final BigInteger objectID, final AbstractEntity entity) throws JDOMException, IOException {
		final Class<?> c = entity.getClass();
		final SAXBuilder builder = new SAXBuilder();
		final Document doc = (Document) builder.build(entity.getFileName());

		final Element categoryElement = doc
				.getRootElement()
				.getChildren(c.getAnnotation(XmlRootElement.class).name())
				.stream()
				.filter(e -> new BigInteger(e.getAttribute("id").getValue()).equals(entity.getId()))
				.collect(Collectors.toList()).get(0);

		categoryElement.getChildren("objects")
		.stream()
		.filter(e -> new BigInteger(e.getChild("object").getValue()).equals(objectID))
		.forEach(e -> e.getChild("object").detach());

		OutputStreamWriter streamWriter = null;
		BufferedWriter bw = null;
		try {
			final XMLOutputter xmlOutput = new XMLOutputter();
			streamWriter = new OutputStreamWriter(new FileOutputStream(entity.getFileName()), "UTF-8");
			bw = new BufferedWriter(streamWriter);
			
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, bw);
		} finally {
			streamWriter.close();
			bw.close();
		}
	}

	/**
	 * Gibt den Pfad der config.xml wieder.
	 * 
	 * @return
	 */
	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String path) {
		this.configPath = path;
	}
	
	private String getDatapath(){
		if(this.dataPath == null){
			return Settings.getInstance().getDataPath();
		}
		
		return dataPath;
	}
	
}
