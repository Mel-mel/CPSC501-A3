import org.jdom2.Document;

public class ObjectCreator {

	private static TempObject obj = new TempObject();
	private static TempObject obj2 = new TempObject();
	
	public static void main(String[] args)
	{
		Serializer serial = new Serializer();
		Document docs = serial.serialize(obj);
		serial.writeXMLFile(docs);
		//serial.serialize(obj2);
	}
}
