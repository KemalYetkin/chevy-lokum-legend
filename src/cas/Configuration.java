package cas;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import engines.GameEngine;
import occupiers.Lokum;
import occupiers.SquareOccupier;
import occupiers.SquareOccupierFactory;

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
			// TODO Auto-generated catch block
			return false;			
		}
	}	
	
}
