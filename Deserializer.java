import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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
					
					Class classObject = Class.forName("TempObject");
					
					try {
						Object object = classObject.newInstance();//Implicitly uses the no-constructor?
						Field[] field = classObject.getDeclaredFields();
						
						//This is to get the attribute value of the id
						Element elem = (Element) list.get(i);
						String id = elem.getAttributeValue("id");
						System.out.println(id);
						
						String className = elem.getAttributeValue("class");
						System.out.println(className);
						
						refTable.put(object, Integer.parseInt(id));
						
						List newList = elem.getChildren();
						
						for(int j = 0; j < newList.size(); j++)
						{
							Element fieldElem = (Element) newList.get(j);
							String fieldClass = fieldElem.getAttributeValue("declaringclass");
							
							if(fieldClass == null)
							{
								//skip. most likely an array of primitives
								String fieldName = fieldElem.getAttributeValue("name");
							}
							else
							{
								Class classObj = Class.forName(fieldClass);//Load dynamically?
								String fieldName = fieldElem.getAttributeValue("name");
								
								System.out.println(fieldName);
							}
							
							
							/*if(!field[j].isAccessible())
							{
								field[j].setAccessible(true);
							}
							if(field[j].getType().isArray())
							{
								
							}
							else
							{
								
							}*/
							//System.out.println(field[j].getType().getName());
							//System.out.println(field[j].getName());
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
	
}
