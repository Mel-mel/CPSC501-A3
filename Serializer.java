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
	
	//Serial an object. Takes object turns it into an xml file. Creates an id number to identity each object.
	//Each object will be serialized and any fields in it will be serialized as well. If those fields are 
	//objects themselves then they will be recursively go through this method again to check any of its
	//fields and serialize those if any.
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
		
		//Check if the classObject is an array
		if(classObject.isArray())
		{
			objectElem.setAttribute(new Attribute("length", Integer.toString(Array.getLength(obj))));
			for(int i = 0; i < Array.getLength(obj); i++)
			{
				objectElem.addContent(serialObject(obj, classObject, i));
			}
			
		}
		//When the classObject is not an array
		else
		{
			Field[] list = classObject.getDeclaredFields();
			for (int i = 0; i < list.length; i++)
			{	
				objectElem.addContent(serialField(list[i], obj, classObject));
			}
		}
		
		//Add content of an object to docs
		docs.getRootElement().addContent(objectElem);
		return docs;
	}
	
	//Serialize an object, add content to the doc if it's a value or a reference.
	//Returns added content afterwards.
	private Element serialObject(Object obj, Class classObject, int i)
	{
		Object anArray = obj;
		Element value = new Element("value");
		Element reference = new Element("reference");
		Element temp = null;
		
		//When the object is some primitive, add content under "value"
		if(classObject.getComponentType().isPrimitive())
		{
			System.out.println(classObject.getComponentType());
			
			value.setText(Array.get(anArray, i).toString());
				
			temp = value;
			
		}
		//When object is not a primitive, add reference id under "reference" since it's most likely an object.
		//The object already has a unique id number
		else 
		{
			for(int j = 0; j < Array.getLength(anArray); j++)
			{
				Integer id = getObjID(Array.get(anArray, j));
				reference.setText(id.toString());
				
				for(int k = 0; k < Array.getLength(anArray); k++)
				{
					serialize(Array.get(anArray, k));
				}
				

			}
			temp = reference;
		}
		return temp;
	}
	
	//Serialize fields with it's name and declaring class. If it's a primitive then just set it's value
	//to "value". If it's an object, then get the reference id of object and set content to "reference"
	private Element serialField(Field field, Object obj, Class classObject)
	{
		Element newField = new Element("field");
		try {
			if(!field.isAccessible())
			{
				field.setAccessible(true);
			}
			//Setting attributes for xml document
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
				
				//This part below was an attempt to work with a 'String'. Doesnt work though
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
		
		return newField;
	}
	
	//A method to write an .xml file into storage space. Mostly likely on the D drive
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
			System.out.println(id);
		}
		else
		{
			referenceMap.put(obj, id);
			referenceID++;
		}
		System.out.println("method id" + id);
		return id;
	}
}
