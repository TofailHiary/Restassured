package Utils;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.transform.*;


public class XmlHandler {
	
	/**
	 * Updates the values of XML elements within a document based on a provided map of updates.
	 * Each key in the map corresponds to an XML element's tag name, and the associated value is the new text content to set.
	 *
	 * @param document The XML Document object to update.
	 * @param updates A map containing the element tag names and their new text content values.
	 */

	
	




	/**
	 * Recursively searches through the XML Document for an element with the specified tag name and retrieves its text content.
	 * If multiple elements with the same tag name exist, it returns the text content of the first occurrence.
	 *
	 * @param node The starting Node (element) from where to begin the search.
	 * @param keyToRead The tag name of the XML element to retrieve the text from.
	 * @return The text content of the found element, or null if no matching element is found.
	 */
	
	public static String readValueForKey(Node node, String keyToRead) {
	    if (node.getNodeName().equals(keyToRead)) {
	        return node.getTextContent();
	    } else if (node.hasChildNodes()) {
	        NodeList nodeList = node.getChildNodes();
	        for (int i = 0; i < nodeList.getLength(); i++) {
	            String value = readValueForKey(nodeList.item(i), keyToRead);
	            if (value != null) {
	                return value;
	            }
	        }
	    }
	    return null; // key not found
	}

	/**
	 * Parses an XML file and returns a Document object that represents the entire XML document.
	 * The file is located within the resources folder using its filename.
	 *
	 * @param filename The name of the XML file to parse.
	 * @return A Document object representing the parsed XML file.
	 * @throws URISyntaxException If the file URI is not formatted correctly.
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If any parse errors occur.
	 * @throws ParserConfigurationException If a DocumentBuilder cannot be created which satisfies the configuration requested.
	 */
	public static Document getXmlDocument(String filename) throws Exception {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    Path filePath = Paths.get(ClassLoader.getSystemResource(filename).toURI());
	    System.out.println(filePath);

	    Document document = builder.parse(Files.newInputStream(filePath));
	    return document;
	}

	public static String prepareXmlPayload(String xmlFileName, Map<String, String> updates) throws Exception {
	    // Read the XML file and parse it into a Document
	    String xmlContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(xmlFileName).toURI()))); // No change
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

	    // Convert String to InputStream directly
	    InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)); // Simplified

	    // Now parse the InputStream
	    Document doc = dBuilder.parse(xmlInputStream);

	    // Update the Document with the updates map
	    if (updates!=null) {
	    updateDocumentWithMap(doc, updates); // Delegate logic to a separate method for better organization
	    }
	    // Convert the updated Document back to a string
	    // Note: You still need to implement the convertDocumentToString method according to your project setup

	    return convertDocumentToString(doc);
	}
	
	private static void updateDocumentWithMap(Document doc, Map<String, String> updates) throws Exception {
	    for (Entry<String, String> entry : updates.entrySet()) {
	        NodeList nodes = doc.getElementsByTagName(entry.getKey());
	        if (nodes.getLength() > 0) {
	            for (int i = 0; i < nodes.getLength(); i++) { // Update all matching elements
	                Element element = (Element) nodes.item(i);
	                element.setTextContent(entry.getValue().toString());
	            }
	        }
	    }
	}

	public static String convertDocumentToString(Document doc) throws Exception {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "no"); // We will handle indentation manually
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	    StringWriter writer = new StringWriter();
	    transformer.transform(new DOMSource(doc), new StreamResult(writer));
	    String unformattedXml = writer.toString();

	    // Manual formatting
	    return formatXml(unformattedXml);
	}
	
	private static String formatXml(String xml) {
	    try {
	        Source xmlInput = new StreamSource(new java.io.StringReader(xml));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", 4);
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // Simple exception handling, consider logging or rethrowing in real code
	    }
	}
	
	 public static String mapToXmlString(Map<String, Object> dataMap) throws Exception {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.newDocument();

	        // Root element
	        Element rootElement = doc.createElement("Pet");
	        doc.appendChild(rootElement);

	        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
	            if (entry.getValue() instanceof List) {
	                // Handle lists (e.g., photoUrls, tags)
	                Element listElement = doc.createElement(entry.getKey());
	                for (Object item : (List<?>) entry.getValue()) {
	                    if (item instanceof Map) {
	                        // Recursively handle nested maps (e.g., Category, Tag)
	                        @SuppressWarnings("unchecked")
							Element child = mapToXmlElement(doc, (Map<String, Object>) item, entry.getKey().substring(0, entry.getKey().length() - 1)); // Singular form
	                        listElement.appendChild(child);
	                    } else {
	                        Element child = doc.createElement("photoUrl"); // Assuming list items are simple strings
	                        child.appendChild(doc.createTextNode(item.toString()));
	                        listElement.appendChild(child);
	                    }
	                }
	                rootElement.appendChild(listElement);
	            } else {
	                // Simple elements
	                Element element = doc.createElement(entry.getKey());
	                element.appendChild(doc.createTextNode(entry.getValue().toString()));
	                rootElement.appendChild(element);
	            }
	        }

	        return convertDocumentToString(doc);
	    }
	 
	    private static Element mapToXmlElement(Document doc, Map<String, Object> map, String elementName) {
	        Element element = doc.createElement(elementName);
	        map.forEach((key, value) -> {
	            Element child = doc.createElement(key);
	            child.appendChild(doc.createTextNode(value.toString()));
	            element.appendChild(child);
	        });
	        return element;
	    }
	    

}