package cas;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class XMLParser.
 */
public class XMLParser {

	/**
	 * Instantiates a new XML parser.
	 */
	public XMLParser() {

	}

	/**
	 * Gets the dom element.
	 *
	 * @param xml the xml
	 * @requires xml must be a valid xml file.
	 * @modifies 
	 * @ensures a new DOM document is parsed from xml
	 * @return the dom element
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Document getDomElement(File xml)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(xml);
		return doc;
	}

	/**
	 * Gets the element value.
	 *
	 * @param node the node
	 * @requires 
	 * @modifies 
	 * @ensures 
	 * @return the element value
	 */
	public static final String getElementValue(Node node) {

		Node child;

		if (node != null) {
			if (node.hasChildNodes()) {
				for (child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	/**
	 * Gets the value.
	 *
	 * @param item the item
	 * @param str the str
	 * @requires 
	 * @modifies 
	 * @ensures 
	 * @return the value
	 */
	public static String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return getElementValue(n.item(0));
	}
}
