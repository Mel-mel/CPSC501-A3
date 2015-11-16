import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
/*
 * Melissa Ta, CPSC 501, Assignment 3, UCID# 10110850
 */
public class ObjectCreator {

	private static TempObject obj;
	
	public static void main(String[] args)
	{
		makeObj();
		Serializer serial = new Serializer();
		Document docs = serial.serialize(obj);
		Inspector inspector = new Inspector();
		/*Element object = new Element("object");
		for(int i = 0; i < serial.getObjList().size(); i++)
		{
			System.out.println("objects in this list: " + serial.getObjList().get(i).toString());
			object.addContent(new Element("value").setText(serial.getObjList().get(i).toString()));
		}
		docs.getRootElement().addContent(object);*/
		serial.writeXMLFile(docs);
		
		Deserializer deserial = new Deserializer();
		inspector.inspect(deserial.deserialize(docs), true);
	}
	
	private static void makeObj()
	{
		Scanner input = new Scanner(System.in);
		
		
		System.out.println("========OBJECT CREATOR=========");
		System.out.println("What kind of objects do you want to make?\n");
		System.out.println("Please type in the number to make a predefined object\n");
		System.out.println("Simple Object, 1");
		System.out.println("Object with references to objects, 2");
		System.out.println("Object with array of primitives, 3");
		System.out.println("Object with array of references, 4");
		//System.out.println("Object using collection instance, 5");
		
		System.out.print("Enter your selection: ");
		String type = input.nextLine();
		
		if(type.equals(""))
		{
			System.out.println("Error, please type a single digit to determine the object to be made.");
			System.exit(0);
		}
		else
		{
			if(type.equals("1"))
			{
				System.out.println("Creating a TempObject with value of 45.0 and 7");
				
				obj = new TempObject(45.0, 7);
			}
			else if(type.equals("2"))
			{
				System.out.println("Making a 'Random' object with int 2 and int 6 in TempObject.");
				
				obj = new TempObject(new Random(2, 6));
			}
			else if(type.equals("3"))
			{
				System.out.println("Making array of primitives in TempObject");
				obj = new TempObject(5, 3, 66, 9);
			}
			else if(type.equals("4"))
			{
				System.out.println("Making array of references in TempObject");
				obj = new TempObject(new Random(1, 2), new Random(3, 4));
			}
			//else if(type.equals("5"))
			//{
			//	System.out.println("Making object using collection instance");
			//}
			else {
				System.out.println("Error. Incorrect selection entered.");
				System.exit(0);
			}
		}
	}
}
