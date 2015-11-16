import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Vector;
/*
 * Melissa Ta, CPSC 501, Assignment 2, UCID# 10110850
 * 
Location: University of Calgary, Alberta, Canada
Created By: Jordan Kidney
Created on:  Oct 23, 2005
Last Updated: Oct 23, 2005
^Used the code provided on D2l made by Jordan Kidney.
 The general set up was used for methods "inspect", "inspectMethod", "inspectFieldClasses", and part of "inspectMethods".
 Every other method was made by me.
 Didn't implement recursion inspection.

*/
public class Inspector {
	
	public void inspect(Object obj, boolean recursive)
    {
	Vector objectsToInspect = new Vector();
	Class ObjClass = obj.getClass();
	
	System.out.println("  inside inspector: " + obj + " (recursive = "+recursive+")");

	//check the class name
	inspectClass(obj, ObjClass, objectsToInspect);
	
	//Check methods a class declares including the exceptions thrown, parameter and return types, and modifiers
	inspectMethods(obj, ObjClass, objectsToInspect);
	
	//Check constructors
	inspectConstructor(obj, ObjClass, objectsToInspect);
	
	//Check fields of the current class
	System.out.println("----Inspecting Fields----");
	inspectFields(obj, ObjClass, objectsToInspect);
	
	//Check all fields in a class that are an object (recursion doesn't work nor was it implemented)
	/*if(recursive == true)
	{
	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
    }*/
    }
	
	//Recursively inspects field classes and prints out content of a field (doesnt work properly)
	private void inspectFieldClasses(Object obj, Class classObject, Vector objsToInspect, boolean recursive)
	{
		if(objsToInspect.size() > 0)
		{
			Field aField = classObject.getDeclaredFields()[0];
			aField.setAccessible(true);
			Enumeration num = objsToInspect.elements();
			for (int i = 0; i < objsToInspect.size(); i++)
			{
				System.out.println(i);
			}
			boolean bool = num.hasMoreElements();
			while (bool = true)
			{
				Field field = (Field) num.nextElement();
				field.setAccessible(true);
				System.out.print("\n----Checking contents of " + field.getName() + "----");
				try {
					inspect(field.get(obj), recursive);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//Inspect a class to get its declaring name, the superclass that classObject belongs to, and
	//the names of the interfaces that are in classObject.
	private void inspectClass(Object obj, Class classObject, Vector objsToInspect)
    {
    	//Get name of declaring class
    	String className = obj.getClass().getName();
    	System.out.println("Name of declaring class: " + className);
    	
    	//Get name of super class
    	Class<?> superName = classObject.getSuperclass();
    	System.out.println("Name of super class: " + superName);
    	
    	//Get name of interfaces of class that implements them and print them out
    	Class[] interfaceNames = classObject.getInterfaces();
    	for(Class classes : interfaceNames)
    	{
    		System.out.println("Name of interfaces: " + interfaceNames);
    	}
    }
	
	//Inspects all methods within a classObject for each method name, any exception and parameter types,
	//its return type, and its modifier. After getting those values it prints them out through a for loop.
	private void inspectMethods(Object obj, Class classObject, Vector objsToInspect)
    {
		//Get all declared methods
		System.out.println("\n----Inspecting Methods----");
    	Method classMethods[] = classObject.getDeclaredMethods();
    	
    	//Loop through array of methods
    	for (int i = 0; i < classMethods.length; i++)
    	{
    		System.out.println("Name of methods: " + classMethods[i].getName());
    		
    		//Get exception and parameter and return type as well as the modifier
    		Class<?>[] exceptions = classMethods[i].getExceptionTypes();
    		Class<?>[] parameters = classMethods[i].getParameterTypes();
    		Class returnType = classMethods[i].getReturnType();
    		int mods = classMethods[i].getModifiers();
    		String modifier = Modifier.toString(mods);
    		
    		//Print out all 4 requested values
    		for (int k = 0; k < exceptions.length; k++)
    		{
    			System.out.println("Exception type for " + classMethods[i].getName() + ": " + exceptions[k]);
    			System.out.println("Parameter type for " + classMethods[i].getName() + ": " + parameters[k]);
    			
    		}
    		System.out.println("Return type for " + classMethods[i].getName() + ": " + returnType);
			System.out.println("Modifiers for " + classMethods[i].getName() + ": " + modifier + "\n");
    	}
    }
	
	//Inspect constructor in classObject to get any constructors in a class. Prints out it's name, modifiers,
	//and parameter types if any.
	private void inspectConstructor(Object obj, Class classObject, Vector objsToInspect)
	{
		//Get array of constructors in classObject
		System.out.println("\n----Inspecting constructor----");
		Constructor[] constructors = classObject.getDeclaredConstructors();
		
		for (int i = 0; i < constructors.length; i++)
		{
			//Print out name of constructor
			System.out.println("Constructor: " + constructors[i].getName());
			
			//Get modifiers
			int mods = constructors[i].getModifiers();
			String modifier = Modifier.toString(mods);
			
			//Get parameter types
			Class<?>[] constructParameters = constructors[i].getParameterTypes();
			
			//Print out list of parameter types and modifier
			for (int j = 0; j < constructParameters.length; j++)
			{
				System.out.println("Constructor parameter types: " + constructParameters[j].getName());
			}
			System.out.println("Modifiers for " + classObject.getName() + ": " + modifier + "\n");
		}
	}
	
	//Inspect array of fields within classObject to get it's name, type, and modifiers then print it
	private void inspectFields(Object obj, Class classObject, Vector objsToInspect)
	{
		//Get field modifiers, type, and name
		Field[] fields = classObject.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			//Get access to private fields
			fields[i].setAccessible(true);
			
			//Get modifier
			int mods = fields[i].getModifiers();
			String modifier = Modifier.toString(mods);
			
			//Get type for fields[i]. This is for checking
			//if a field is an array.
			Object object = fields[i].getType();
			
			//Calling print methods to print out information on a field
			printFieldContent(modifier, object, fields, i);
			
			//If field is an array and its type is not a primitive then 
			//print out its content along with its length, name, and 
			//component type.
			Class aClass = fields[i].getType();
			if((!fields[i].getType().isPrimitive()) && (aClass.isArray() == true))
			{
				printArrayContent(obj, classObject, aClass, fields, i);
			}
		}
		
		//Getting field values
		for (int i = 0; i < fields.length; i++)
		{
			//Get one field at a time and make any private field accessible
			Field oneField = classObject.getDeclaredFields()[i];
			oneField.setAccessible(true);
			
			//Print value of field and its hash code when recursive is false
			printFieldValues(obj, classObject, objsToInspect, oneField, fields, i);
			
		}
	}
	
	//Simply prints the fields value and it's 
	private void printFieldValues(Object obj, Class classObject, Vector objsToInspect, Field oneField, Field[] fields, int i)
	{
		try {
			//Making vector contain elements of objects
			//objsToInspect.addElement(fields[i]);
			System.out.println("\nField value of " + fields[i].getName() + ": " + oneField.get(obj));
			
			//If a field is not a primitive, then print out object hash code instead
			if(!fields[i].getType().isPrimitive())
			{
				System.out.println("Field hash code of " + fields[i].getName() + ": " + obj.hashCode());
			}
			
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		if(classObject.getSuperclass() != null)
		{
			inspectFields(obj, classObject.getSuperclass(), objsToInspect);
		}
	}
	
	//Print out field values for modifier, the type of a field and its name respectively.
	private void printFieldContent(String modifier, Object object, Field[] fields, int i)
	{
		System.out.println("Name of modifier: " + modifier);
		System.out.println("Name of type: " + object);
		System.out.println("Name of field: " + fields[i].getName() + "\n");
	}
	
	//Prints out contents of array. Displays its length, its name, the component type, and
	//all of its content
	private void printArrayContent(Object obj, Class classObject, Class aClass, Field[] fields, int i)
	{
		Object arrayObject;
		try {
			arrayObject = fields[i].get(obj);
			int arrLength = Array.getLength(arrayObject);
			System.out.println("Name of array: " + fields[i].getName());
			System.out.println("Component type of array: " + aClass.getComponentType().getName());
			System.out.println("Length of array: " + arrLength);
			for (int j = 0; j < arrLength; j++)
			{
				System.out.println("Contents of array:" + Array.get(arrayObject, j));
			}
			System.out.println(" ");
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
