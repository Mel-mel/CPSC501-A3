import java.util.ArrayList;
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
	
	private ArrayList<Object> objList = new ArrayList<Object>();
	private ArrayList<Character> charList = new ArrayList<Character>();
	
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
		
		System.out.println(classObject.isArray());
		if(classObject.isArray())
		{
			objectElem.setAttribute(new Attribute("length", Integer.toString(Array.getLength(obj))));
			for(int i = 0; i < Array.getLength(obj); i++)
			{
				objectElem.addContent(serialObject(obj, classObject, i));
			}
			
		}
		else
		{
			Field[] list = classObject.getDeclaredFields();
			for (int i = 0; i < list.length; i++)
			{	
				objectElem.addContent(serialField(list[i], obj, classObject));
			}
		}
		
		docs.getRootElement().addContent(objectElem);
		return docs;
	}
	
	private Element serialObject(Object obj, Class classObject, int i)
	{
		Object anArray = obj;
		Element value = new Element("value");
		Element reference = new Element("reference");
		Element temp = null;
		if(classObject.getComponentType().isPrimitive())
		{
			System.out.println(classObject.getComponentType());
			
			value.setText(Array.get(anArray, i).toString());
				
			temp = value;
			
			
			/*for (int i = 0; i < Array.getLength(anArray); i++)
			{
				Integer id = getObjID(obj);
				System.out.println(Array.get(anArray, i).toString());
				System.out.println("refID " + id.toString());
				objList.add(Array.get(anArray, i));
				System.out.println("in objList: " + objList.get(i).toString());
				//objectElem.addContent(new Element("value").setText(Array.get(anArray, i).toString()));
			}
			addObjToDoc(anArray, objectElem);*/
			//objList.clear();
			//objectElem.addContent(new Element("value").setText(classObject.toString()));//Seems like it's null? from debug
		}
		else 
		{
			for(int j = 0; j < Array.getLength(anArray); j++)
			{
				//Integer id = getObjID(Array.get(anArray, j));
				Integer id = getObjID(Array.get(anArray, j));
				System.out.println("id " + id);
				reference.setText(id.toString());
				//objectElem.addContent(new Element("reference").setText(Integer.toString(getObjID(Array.get(anArray, i)))));
				
				for(int k = 0; k < Array.getLength(anArray); k++)
				{
					serialize(Array.get(anArray, k));
				}
				
			}
			temp = reference;
		}
		return temp;
	}
	
	private Element serialField(Field field, Object obj, Class classObject)
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
				System.out.println(id.toString());
				serialize(field.get(obj));
				/*if(field.getType().getName() == "java.lang.String")
				{
					try {
					System.out.println(field.get(obj));
					System.out.println(field.getType().getName());
					System.out.println("true");
					for(char character: ((String) field.get(obj)).toCharArray())
					{
						System.out.println(character);
						charList.add(character);
					}
					objectElem.setAttribute(new Attribute("length", Integer.toString(charList.size())));
					for(int i = 0; i < charList.size(); i++){
						newField.addContent(new Element("value").setText(charList.get(i).toString()));
					}
					//addCharToDoc(objectElem, newField);
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}*/
				
				
				
				
			}
			
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		//objectElem.addContent(newField);
		return newField;
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
