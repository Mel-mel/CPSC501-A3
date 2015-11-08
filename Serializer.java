import java.util.HashMap;
import java.util.IdentityHashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.rmi.server.ObjID;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/*
 * Melissa Ta, CPSC 501, Assignment 3, UCID# 10110850
 */
public class Serializer {

	HashMap<Object, Integer> referenceMap = new HashMap<Object, Integer>();
	private int referenceID = 0;
	private Document docs = null;
	public Document serialize(Object obj)
	{
		Class<?> classObject = obj.getClass();
		
		Element serialized = new Element("serialized");
		docs = new Document(serialized);
		
		String className = classObject.getName();

		Integer id = getObjID(obj);
		
		Element objectElem = new Element("object");
		objectElem.setAttribute(new Attribute("class", className));
		objectElem.setAttribute(new Attribute("id", id.toString()));
		
		
		if(classObject.isArray())
		{
			/*Object anArray = obj;
			objectElem.setAttribute(new Attribute("length", Integer.toString(Array.getLength(anArray))));
			if(classObject.getComponentType().isPrimitive())
			{
				System.out.println(classObject.getComponentType());
				for (int i = 0; i < Array.getLength(anArray); i++)
				{
					System.out.println(Array.get(anArray, i).toString());
					objectElem.addContent(new Element("value").setText(Array.get(anArray, i).toString()));
				}
				//objectElem.addContent(new Element("value").setText(classObject.toString()));//Seems like it's null? from debug
			}
			else
			{
				System.out.println(Array.getLength(anArray));
				for(int i = 0; i < Array.getLength(anArray); i++)
				{
					id = getObjID(Array.get(anArray, i));
					System.out.println("id " + id);
					objectElem.addContent(new Element("reference").setText(id.toString()));
					
					//objectElem.addContent(new Element("reference").setText(Integer.toString(getObjID(Array.get(anArray, i)))));
					
					for(int k = 0; k < Array.getLength(anArray); k++)
					{
						serialize(Array.get(anArray, k));
					}
				}
			}*/
		}
		else
		{
			Field[] list = classObject.getDeclaredFields();
			for (int i = 0; i < list.length; i++)
			{
				serialField(list[i], obj, classObject, objectElem);
			}
			
		}
		
		docs.getRootElement().addContent(objectElem);
		return docs;
	}
	
	private void serialField(Field field, Object obj, Class classObject, Element objectElem)
	{
		Element newField = new Element("field");
		try {
			if(!field.isAccessible())
			{
				field.setAccessible(true);
			}
			newField.setAttribute(new Attribute("name", field.getName()));
			newField.setAttribute(new Attribute("declaringclass", obj.getClass().getName()));
			if(field.getType().isPrimitive())
			{
				
					String str = String.valueOf(field.get(obj));
				
				newField.addContent(new Element("value").setText(str));
			}
			else
			{
				Integer id = getObjID(field.get(obj));
				newField.addContent(new Element("reference").setText(id.toString()));
			}
			/*
			Field[] listFields = classObject.getDeclaredFields();
			Element field = new Element("field");
			
			for(int i = 0; i < listFields.length; i++)
			{
				listFields[i].setAccessible(true);
				
				field.setAttribute(new Attribute("name", listFields[i].getName()));
				field.setAttribute(new Attribute("declaringclass", obj.getClass().getName()));
				
				if(listFields[i].getType().isPrimitive())
				{
					//Get value of a field and store its content as a value element
					try {
						field.addContent(new Element("value").setText(String.valueOf(listFields[i].get(obj))));
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
				
				//When the field is not a primitive then it's an object so store its reference id
				//and make it an object element. doesnt deal with arrays
				else
				{
				/*	Integer objID;
					Field oneField = listFields[i];
					try {
						objID = getObjID(listFields[i].get(obj));
						field.addContent(new Element("reference").setText(objID.toString()));
						serialize(oneField.get(obj));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//serializeObject(obj, classObject);
					
				}
			}*/
		} 
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objectElem.addContent(newField);
	}
	
	public void writeXMLFile(Document docs)
	{
		try {
			new XMLOutputter().output(docs, System.out);
			XMLOutputter outputs = new XMLOutputter();
			
			//On personal computer, xml file is located right on the D drive.
			outputs.setFormat(Format.getPrettyFormat());
			outputs.output(docs, new FileWriter("d:\\xmlFile.xml"));
			//outputs.output(docs, new FileWriter(System.getProperty("user.dir")));
			
			System.out.println("File made and saved.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This was used from the tutorial 1 section.
	//This gets the objects id number and returns that value as an int
	private int getObjID(Object obj)
	{
		Integer id = referenceID;
		if(referenceMap.containsKey(obj))
		{
			id = referenceMap.get(obj);
		}
		else
		{
			referenceMap.put(obj, id);
			referenceID++;
		}
		return id;
	}
}
