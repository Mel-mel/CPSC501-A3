import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
/*
 * Melissa Ta, CPSC 501, Assignment 3, UCID# 10110850
 */
public class Deserializer {

	private Object obj;
	HashMap<Object, Integer> refTable = new HashMap<Object, Integer>();

	public Object deserialize(Document docs)
	{
		SAXBuilder saxbuild = new SAXBuilder();
		File xml = new File("d:\\xmlFile.xml");


		try {
			Document document = saxbuild.build(xml);
			Element root = document.getRootElement();
			List list = root.getChildren("object");

			for(int i = 0; i < list.size(); i++)
			{
				try {

					Element element = (Element) list.get(i);
					String className = element.getAttributeValue("class");

					Class classObject = Class.forName(className);

					try {
						
						//Instantiating objects that are either arrays or just simple objects
						//Check for arrays first, else it's just an non-array object
						if(classObject.isArray())
						{
							String length = element.getAttributeValue("length");
							int arrLength = Integer.parseInt(length);
							Class type = classObject.getComponentType();
							obj = Array.newInstance(type, arrLength);
							
							String id = element.getAttributeValue("id");
							refTable.put(obj, Integer.parseInt(id));
						}
						else
						{
							obj = classObject.newInstance();
							
							//This is to get the attribute value of the id
							String id = element.getAttributeValue("id");
							refTable.put(obj, Integer.parseInt(id));
							
							
							List newList = element.getChildren();
							System.out.println(newList.size());
							for(int j = 0; j < newList.size(); j++)
							{
								Element fieldElem = (Element) newList.get(j);
								String fieldClass = fieldElem.getAttributeValue("declaringclass");
								System.out.println(fieldClass);
								Class classObj = Class.forName(fieldClass);//Load dynamically?
								String fieldName = fieldElem.getAttributeValue("name");

								if(fieldClass == null)
								{
									//skip. most likely an array of primitives
									//String fieldName = fieldElem.getAttributeValue("name");
								}
								else
								{

									try {
										Field initfield = classObj.getDeclaredField(fieldName);
							
										if(!initfield.isAccessible())
										{
											initfield.setAccessible(true);
										}
										String xmlField = fieldElem.getValue().trim();
										//Get the unique id number of reference
										System.out.println(fieldElem.getName());
										if(fieldElem.getChildren().get(0).getName().equals("reference"))
										{
											Integer idNum = refTable.get(obj);
											System.out.println("idnum " + idNum);
										}
										else
										{
											//Initializing and setting field values
											initfield.set(obj, makeWrapper(xmlField, initfield));
										}
									} catch (NoSuchFieldException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SecurityException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					//System.out.println(classObject.getName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return obj;
	}


	//Method to make a wrapper object and returns it. Considers all primitive types
	private Object makeWrapper(String xmlFieldstr, Field field)
	{
		if(field.getType().getName().equals("double") == true || field.getType().getName().equals("Double") == true)
		{
			return Double.parseDouble(xmlFieldstr);
		}
		else if(field.getType().getName().equals("float") == true || field.getType().getName().equals("Float") == true)
		{
			return Float.parseFloat(xmlFieldstr);
		}
		else if(field.getType().getName().equals("int") == true || field.getType().getName().equals("Integer") == true)
		{
			return Integer.parseInt(xmlFieldstr);
		}
		else if(field.getType().getName().equals("long") == true || field.getType().getName().equals("Long") == true)
		{
			return Long.parseLong(xmlFieldstr);
		}
		else if(field.getType().getName().equals("short") == true || field.getType().getName().equals("Short") == true)
		{
			return Short.parseShort(xmlFieldstr);
		}
		else if(field.getType().getName().equals("boolean") == true || field.getType().getName().equals("Boolean") == true)
		{
			return Boolean.parseBoolean(xmlFieldstr);
		}
		else if (field.getType().getName().equals("byte") == true || field.getType().getName().equals("Byte") == true)
		{
			return Byte.parseByte(xmlFieldstr);
		}
		else if(field.getType().getName().equals("char") == true || field.getType().getName().equals("Char") == true)
		{
			return xmlFieldstr.toCharArray();
		}

		return null;
	}
}
