package net.nypc.gps.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;

//import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Convenience Class to parse XML strings to DOM Document for XML contents
 * retrieval.
 */
public class XMLUtils {
    private static DocumentBuilder parser;
    public static Writer writer;
    static private String indent = "";
    protected static final Log logger = LogFactory.getLog(XMLUtils.class);


    /**
     * @return
     * @throws ParserConfigurationException
     */
    public static DocumentBuilder getDocumentBuilder()
            throws ParserConfigurationException {
        if (parser == null) {
            // JPT: Remove xerces use
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setValidating(false);
            parser = documentBuilderFactory.newDocumentBuilder();
        }

        return parser;
    }
    
     

    /**
     * @param xmlString
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public synchronized static Document parse(String xmlString)
            throws IOException, SAXException, ParserConfigurationException {
        StringReader reader = new StringReader(xmlString);
        InputSource inputSource = new InputSource(reader);
        return getDocumentBuilder().parse(inputSource);
    }
    
     
    /**
     * @return
     * @throws ParserConfigurationException
     */
    public static Document newDocument() throws ParserConfigurationException { 
        return getDocumentBuilder().newDocument(); 
    }

    /**
     * @param stream
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public synchronized static Document parse(InputStream stream)
            throws IOException, SAXException, ParserConfigurationException {
        return getDocumentBuilder().parse(stream);
    }
    
    public synchronized static Document getDocument(InputStream stream, String encoding)
            throws IOException, SAXException, ParserConfigurationException {

            InputSource inputSource = new InputSource(stream);
            inputSource.setEncoding(encoding);
            Document doc = getDocumentBuilder().parse(inputSource);
            final PrefixResolver resolver =
        new PrefixResolverDefault(doc.getDocumentElement());
    NamespaceContext ctx = new NamespaceContext() {
        public String getNamespaceURI(String prefix) {
            return resolver.getNamespaceForPrefix(prefix);
        }

        // Dummy implementation - not used!
        @SuppressWarnings("unchecked")
        public Iterator getPrefixes(String val) {
            return null;
        }

        // Dummy implemenation - not used!
        public String getPrefix(String uri) {
            return null;
        }
    };
    return doc;

    }

    /**
     * @param document
     * @param name
     * @return
     */
    public static String getElementByName(Document document, String name) {
        NodeList nodes = document.getElementsByTagName(name);
        String s = null;
        for (int i=0; i < nodes.getLength() ; i++) {
            Node node = nodes.item(i);
            s = node.getTextContent().trim();
        }
        return s;
     }

    /**
     * @param doc
     * @param expression
     * @return string contents
     */
    public static String getValueWithXpath(Document doc, String expression) {
        Element root = doc.getDocumentElement();
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new MetadataNamespaceContext());
        String result = "";

        try {
            result = ((String) xpath.evaluate(expression, root)).trim();
            return result;
        } catch (XPathExpressionException e) {
            logger.error("XPathExpressionException ", e);
            return null;
        }
    }

    /**
     * @param doc (either a Document or a Node)
     * @param expression
     * @return string contents
     */
    public static String getStringWithXpath(Object obj, String expression) {
       Object root = null;
       if (obj instanceof Document) {
          Document doc = (Document) obj;
          root = doc.getDocumentElement();
       } else {
          root = (Node) obj;
       }

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new MetadataNamespaceContext());
        String result = "";

        try {
            result = ((String) xpath.evaluate(expression, root, XPathConstants.STRING)).trim();
            return result;
        } catch (XPathExpressionException e) {
            logger.error("XPathExpressionException ", e);
            return null;
        }
    }

    /**
     * @param doc (either a Document or a Node)
     * @param expression
     * @return string contents
     */
    public static Node getNodeWithXpath(Object obj, String expression) {
       Object root = null;
       if (obj instanceof Document) {
          Document doc = (Document) obj;
          root = doc.getDocumentElement();
       } else {
          root = (Node) obj;
       }

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new MetadataNamespaceContext());
        Node result = null;

        try {
            //result = ((Node) xpath.evaluate(expression, root, XPathConstants.NODE));
            result = (Node) xpath.compile(expression).evaluate(root, XPathConstants.NODE);
            return result;
        } catch (XPathExpressionException e) {
            logger.error("XPathExpressionException ", e);
            return null;
        }
    }

    /**
     * @param doc (either a Document or a Node)
     * @param expression
     * @return string contents
     */
    public static NodeList getNodeSetWithXpath(Object obj, String expression) {
       Object root = null;
       if (obj instanceof Document) {
          Document doc = (Document) obj;
          root = doc.getDocumentElement();
       } else {
          root = (Node) obj;
       }

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new MetadataNamespaceContext());
        NodeList result = null;

        try {
            result = ((NodeList) xpath.evaluate(expression, root, XPathConstants.NODESET));
            return result;
        } catch (XPathExpressionException e) {
            logger.error("XPathExpressionException ", e);
            return null;
        }
    }

    /*public static void serializeDoc(Document doc) throws IOException {
       XMLSerializer serializer = new XMLSerializer();
       serializer.setOutputByteStream(System.out);
       serializer.serialize(doc);
    }*/

    public static void displayNodeInfo(Node node) {
       switch (node.getNodeType()) {
       case Node.DOCUMENT_NODE:
         System.out.println("Document Node ");
         break;
       case Node.ELEMENT_NODE:
         System.out.println("Element Node: "+ node.getNodeName());
         break;
       case Node.TEXT_NODE:
         System.out.println("Text Node: "+ node.getNodeName());
         break;
       case Node.CDATA_SECTION_NODE:
         System.out.println("CDATA Section Node: ");
         break;
       case Node.COMMENT_NODE:
         System.out.println("Comment Node ");
         break;
       case Node.PROCESSING_INSTRUCTION_NODE:
         System.out.println("Processing Instruction Node ");
         break;
       case Node.ENTITY_REFERENCE_NODE:
         System.out.println("Entity Reference Node ");
         break;
       case Node.DOCUMENT_TYPE_NODE:
         System.out.println("Document Type Node ");
         break;
       }
    }

    public static void serializeNode(Node node) throws IOException {
       if (writer == null) writer = new BufferedWriter(new OutputStreamWriter(System.out));

       switch (node.getNodeType()) {
       case Node.DOCUMENT_NODE:
         Document doc = (Document) node;
         writer.write("<?xml version=\"");
         writer.write(doc.getXmlVersion());
         writer.write("\" encoding=\"UTF-8\" standalone=\"");
         if (doc.getXmlStandalone())
           writer.write("yes");
         else
           writer.write("no");
         writer.write("\"?>\n");

         NodeList nodes = node.getChildNodes();
         if (nodes != null)
           for (int i = 0; i < nodes.getLength(); i++)
             serializeNode(nodes.item(i));
         break;
       case Node.ELEMENT_NODE:
         String name = node.getNodeName();
         writer.write("<" + name);
         NamedNodeMap attributes = node.getAttributes();
         for (int i = 0; i < attributes.getLength(); i++) {
           Node current = attributes.item(i);
           writer.write(" " + current.getNodeName() + "=\"");
           print(current.getNodeValue());
           writer.write("\"");
         }
         writer.write(">");

         NodeList children = node.getChildNodes();
         if (children != null) {
           //if ((children.item(0) != null) && (children.item(0).getNodeType() == Node.ELEMENT_NODE))
           //  writer.write("\n");

           for (int i = 0; i < children.getLength(); i++)
             serializeNode(children.item(i));
           if ((children.item(0) != null)
               && (children.item(children.getLength() - 1).getNodeType() == Node.ELEMENT_NODE))
             writer.write("");
         }

         writer.write("</" + name + ">");
         break;
       case Node.TEXT_NODE:
         print(node.getNodeValue());
         break;
       case Node.CDATA_SECTION_NODE:
         writer.write("CDATA");
         print(node.getNodeValue());
         writer.write("");
         break;
       case Node.COMMENT_NODE:
         writer.write("<!-- " + node.getNodeValue() + " -->\n");
         break;
       case Node.PROCESSING_INSTRUCTION_NODE:
         writer.write("<?" + node.getNodeName() + " " + node.getNodeValue() + "?>\n");
         break;
       case Node.ENTITY_REFERENCE_NODE:
         writer.write("&" + node.getNodeName() + ";");
         break;
       case Node.DOCUMENT_TYPE_NODE:
         DocumentType docType = (DocumentType) node;
         String publicId = docType.getPublicId();
         String systemId = docType.getSystemId();
         String internalSubset = docType.getInternalSubset();
         writer.write("<!DOCTYPE " + docType.getName());
         if (publicId != null)
           writer.write(" PUBLIC \"" + publicId + "\" ");
         else
           writer.write(" SYSTEM ");
         writer.write("\"" + systemId + "\"");
         if (internalSubset != null)
           writer.write(" [" + internalSubset + "]");
         writer.write(">\n");
         break;
       }
       writer.flush();
     }

    /**
     * @param xml
     */
    public static void prettyPrint(String xml) {
        Source xmlInput = new StreamSource(new StringReader(xml));
        StreamResult xmlOutput = new StreamResult(new StringWriter());
        Transformer transformer = null;
       try {
          transformer = TransformerFactory.newInstance().newTransformer();
       } catch (TransformerConfigurationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       } catch (TransformerFactoryConfigurationError e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
        //transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try {
          transformer.transform(xmlInput, xmlOutput);
       } catch (TransformerException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
        String formattedxml=xmlOutput.getWriter().toString();
        System.out.println(formattedxml);

     }

    /**
     * @param xml
     */
    public static String prettyPrintToString(String xml) {
        Source xmlInput = new StreamSource(new StringReader(xml));
        StreamResult xmlOutput = new StreamResult(new StringWriter());
        Transformer transformer = null;
       try {
          transformer = TransformerFactory.newInstance().newTransformer();
       } catch (TransformerConfigurationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       } catch (TransformerFactoryConfigurationError e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
        //transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try {
          transformer.transform(xmlInput, xmlOutput);
       } catch (TransformerException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
        String formattedxml=xmlOutput.getWriter().toString();
        return formattedxml;

     }
	private static void print(String s) throws IOException {
		if (s == null)
			return;
		for (int i = 0, len = s.length(); i < len; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				writer.write("&lt;");
				break;
			case '>':
				writer.write("&gt;");
				break;
			case '&':
				writer.write("&amp;");
				break;
			case '\r':
				writer.write("&#xD;");
				break;
			default:
				writer.write(c);
			}
		}
	}

}
