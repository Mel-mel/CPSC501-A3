import static org.junit.Assert.*;

import org.jdom2.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class A3Test {

	private static Object simple, objref, arrprim;
	private Serializer serial = new Serializer();
	private Deserializer deserial = new Deserializer();
	@Before
	public void setup()
	{
		simple = new TempObject();
		objref = new TempObject(new Random(66, 36));
		arrprim = new TempObject(1, 2, 3, 4);
	}

	@Test
	public void testSerializer()
	{
		Document doc = serial.serialize(simple);
		assertNotEquals(0, doc);
		
		doc = serial.serialize(objref);
		assertNotEquals(0, doc);
	}
	@Test
	public void testWriteXmlFile()
	{
		Document docs = serial.serialize(objref);
		serial.writeXMLFile(docs);
	}
	@Test
	public void testDeserializer()
	{
		Document docs = serial.serialize(simple);
		Object obj = deserial.deserialize(docs);
		
		assertNotEquals(null, obj);
		
		docs = serial.serialize(arrprim);
		obj = deserial.deserialize(docs);
		assertNotEquals(objref, arrprim);
	}
	
	@After
	public void teardown()
	{
		simple = null;
		objref = null;
		arrprim = null;
	}
}
