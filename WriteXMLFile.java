import java.io.FileWriter;
import java.io.IOException;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class WriteXMLFile {

	
	public static void main(String[] args)
	{
		try
		{
			Element serialized = new Element("serialized");
			Document docs = new Document(serialized);
			//docs.setRootElement(serialized);
			
			//Creating an object
			Element object = new Element("object");
			object.setAttribute(new Attribute("class", "Zoo"));
			object.setAttribute(new Attribute("id", "0"));
			
			docs.getRootElement().addContent(object);
			
			//Creating some fields
			Element field = new Element("field");
			field.setAttribute(new Attribute("name", "city"));
			field.setAttribute(new Attribute("declaringclass", "Zoo"));
			
			//If field is a primitive
			field.addContent(new Element("value").setText("23.7"));
			
			//If field is an object reference, then add the ref and loop back to the top at 'object'
			field.addContent(new Element("reference").setText("5"));
			
			docs.getRootElement().addContent(field);
			
			//If object reference is an array with non object elements, need to make new object attribute thing
			Element objArray = new Element("object");
			objArray.setAttribute(new Attribute("class", "[C"));
			objArray.setAttribute(new Attribute("id", "8"));
			objArray.setAttribute(new Attribute("length", "5"));
			
			objArray.addContent(new Element("value").setText("s"));
			objArray.addContent(new Element("value").setText("m"));
			objArray.addContent(new Element("value").setText("i"));
			objArray.addContent(new Element("value").setText("t"));
			objArray.addContent(new Element("value").setText("h"));
			
			docs.getRootElement().addContent(objArray);
			
			new XMLOutputter().output(docs, System.out);
			XMLOutputter outputs = new XMLOutputter();
			
			//On personal computer, xml file is located right on the D drive.
			outputs.setFormat(Format.getPrettyFormat());
			outputs.output(docs, new FileWriter("d:\\xmlFile.xml"));
			//outputs.output(docs, new FileWriter(System.getProperty("user.dir")));
			
			System.out.println("File made and saved.");
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
