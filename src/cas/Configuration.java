package cas;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public abstract class Configuration {		

	protected Configuration(){
	}	

	/**
	 * Validate.
	 *
	 * @param xsdFile the xsd file
	 * @param xmlFile the xml file
	 * @requires xsdFile and xmlFile should not be null. 
	 * 			 xsdFile should be a xsd format, xmlFile should be a xml format.
	 * @modifies  
	 * @ensures 
	 * @return true, if successful
	 */
	public static boolean validate(File xsdFile, File xmlFile){
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema;			
			schema = schemaFactory.newSchema(xsdFile);			
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			return true;
		} catch (SAXException | IOException e) {
			return false;			
		}
	}		
}
