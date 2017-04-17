package org.xcylite.fitnesse;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.NodeIteratorImpl;
import org.custommonkey.xmlunit.*;
import javax.xml.transform.TransformerFactory;

public class NamespaceXpathEngine implements XSLTConstants {
	

    /**
     * What every XSL transform needs
     * @return
     */
    private StringBuffer getXSLTBase() {
        return new StringBuffer(XML_DECLARATION);
    }

    /**
     * @param select an xpath syntax <code>select</code> expression
     * @param doc the document being evaluated against. Used to look up the namespaces.
     * @return the <code>copy-of</code> transformation
     */
    private String getCopyTransformation(String select, Document doc) {
        return getXSLTBase().append(getXsltStart(select, doc))
            .append("<xsl:preserve-space elements=\"*\"/>")
            .append("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\"/>")
            .append("<xsl:template match=\"/\">")
            .append("<xpathResult>")
            .append("<xsl:apply-templates select=\"").append(select)
                .append("\" mode=\"result\"/>")
            .append("</xpathResult>")
            .append("</xsl:template>")
            .append("<xsl:template match=\"*\" mode=\"result\">")
            .append("  <xsl:copy-of select=\".\"/>")
            .append("</xsl:template>")
            .append("</xsl:stylesheet>")
            .toString();
    }

    /**
     * @param select an xpath syntax <code>select</code> expression
     * @param doc the document being evaluated. Used to look up the namespaces.
     * @return the <code>value-of</code> transformation
     */
    private String getValueTransformation(String select, Document doc) {
        return getXSLTBase().append(getXsltStart(select, doc))
            .append("<xsl:output method=\"text\"/>")
            .append("<xsl:template match=\"/\">")
            .append("  <xsl:value-of select=\"").append(select).append("\"/>")
            .append("</xsl:template>")
            .append("</xsl:stylesheet>")
            .toString();
    }

    /**
     * Adds the beginning tag for the XSLT by incorporating any namespace prefixs found
     * in the XPath into the tag. The NamespaceURI is retrieved from the document being
     * evaluated against.
     * 
     * @param select the Xpath being evaluated.
     * @param doc the Document being evaluated against.
     * @return the opening tag of the XSLT
     */
    private String getXsltStart(String select, Document doc) {
    	StringBuffer start = new StringBuffer("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\" ");
    	
    	// this pattern finds each prefix - assuming pattern /<prefix>:<element>/
        String patternStr = "/([^/:]*):";

        // Set to false if only the tokens that match the pattern are to be returned.
        // If true, the text between matching tokens is also returned.
        boolean returnDelims = false;

        try {
            // Create the tokenizer
            RETokenizer tokenizer = new RETokenizer(select, patternStr,
                    returnDelims);

            // Get the tokens (and delimiters)
            Map prefixFound = new HashMap();
            while (tokenizer.hasNext()) {
            	String token = (String) tokenizer.next();
            	String prefix = token.substring(1,token.length()-1);
            	if ( ! prefixFound.containsKey(prefix)) {
            		String namespace = findNamespace(prefix, doc);
            		start.append("xmlns:"+prefix+"=\""+namespace+"\" ");
            		prefixFound.put(prefix, null);
            	}
            }
        } catch (Exception e) {
        	// Let the XLST fail if the RETokenizer fails
        }
            
        start.append(">");
    	return start.toString();
    }
    
    /**
     * Used to find the Namespace given a prefix. Will search through the Document
     * looking for the prefix since the Namespace used in the XPath may be defined
     * farther down in the document.
     * 
     * @param prefix the Prefix being looked for.
     * @param doc the Document being search through.
     * @return the NamespaceURI for the given prefix
     */
    private String findNamespace(String prefix, Document doc) {
    	String namespace = null;

		NodeIteratorImpl iterator =
			(NodeIteratorImpl) ((DocumentImpl) doc).createNodeIterator(doc, Node.ELEMENT_NODE, null);

    	Node node = doc;
    	while ((node = iterator.nextNode()) != null)  {
    		namespace = node.lookupNamespaceURI(prefix);
    		if (namespace != null ) {
    			break;
    		}
    	}
    	
    	return namespace;
    }
    
    
    /**
     * Perform the actual transformation work required
     * @param xslt
     * @param document
     * @param result
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private void performTransform(String xslt, Document document, Result result)
    throws TransformerConfigurationException, TransformerException {
    	System.out.println(" xslt " + xslt + " result  " + result.toString() );
        StreamSource source = new StreamSource(new StringReader(xslt));
        String docAsString = getStringFromDocument(document);
        InputSource saxInputSource = new InputSource(new StringReader(docAsString));
        SAXSource saxSource = new SAXSource(saxInputSource);
        Transformer transformer =
            XMLUnit.getTransformerFactory().newTransformer(source);
//        transformer.transform(new DOMSource(document), result); Commented it out by sontis1 to make transformation work in  both UI and command line
        transformer.transform(saxSource, result);        
    }

    /**
     * Testable method to execute the copy-of transform and return the root
     * node of the resulting Document.
     * @param select
     * @param document
     * @return the root node of the Document created by the copy-of transform.
     * @throws TransformerException
     */
    protected Node getXPathResultNode(String select, Document document)
    throws TransformerException {
        return getXPathResultAsDocument(select, document).getDocumentElement();
    }

    /**
     * Execute the copy-of transform and return the resulting Document.
     * Used for XMLTestCase comparison
     * @param select
     * @param document
     * @return the Document created by the copy-of transform.
     * @throws TransformerException
     */
    protected Document getXPathResultAsDocument(String select, Document document)
    throws TransformerException {
        DOMResult result = new DOMResult();
        performTransform(getCopyTransformation(select, document), document, result);
        return (Document) result.getNode();
    }

    /**
     * Execute the specified xpath syntax <code>select</code> expression
     * on the specified document and return the list of nodes (could have
     * length zero) that match
     * @param select
     * @param document
     * @return list of matching nodes
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public NodeList getMatchingNodes(String select, Document document)
    throws TransformerException, TransformerConfigurationException {
        return getXPathResultNode(select, document).getChildNodes();
    }

    /**
     * Evaluate the result of executing the specified xpath syntax
     * <code>select</code> expression on the specified document
     * @param select
     * @param document
     * @return evaluated result
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public String evaluate(String select, Document document)
    throws TransformerException, TransformerConfigurationException {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        performTransform(getValueTransformation(select, document), document, result);
        return writer.toString();
    }
    
    /**
     * Transforms Document as a String
     * @author SONTIS1
     *
     * 
     * 
     */
    public String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }    
    }

}
