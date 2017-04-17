package org.xcylite.fitnesse;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SoapXMLFixture extends BaseXMLFixture {

	public SoapXMLFixture() {
		super();
	}

	public Document retrieveXML() {
		System.out.println("SoapXMLFixture : retrieveXML ");
		String input_Url = getText(0, 0);
		String soapAction = getText(0, 1);
		String trustAll = getText(0, 2);
		
		System.out.println("Retrieving XML: inputUrl=" + input_Url +
				", SOAPAction=" + soapAction);
		Document doc = null;
		String response = null;
		
		try {
			SoapFitnesseAdapter adapter = new SoapFitnesseAdapter(input_Url,
					soapAction, trustAll );
			String xmlString = readFileAsString(getText(1, 0));
			xmlString = replaceVariables(xmlString);
			response = adapter.makeRequest(xmlString);
			Reader xmlStream = new StringReader(response);
			InputSource inputXMLStream = new InputSource(xmlStream);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			System.out.println("Begin building XML Document");
			factory.setIgnoringElementContentWhitespace(true);
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(inputXMLStream);
			xmlStream.close();
			System.out.println("Done building XML Document...");
		} 

		catch (PassThroughException pte) {
			System.out.println(" PassThroughException ");
			this.wrong(0, 0, "PassThrough problem: " + pte.toString());
		}  catch (ParserConfigurationException e) {
			System.out.println(" ParserConfigurationException ");
			this.wrong(0, 0, "Parser Config problem:" + e.toString());
			System.out.println(e);
		} catch (SAXException e) {
			System.out.println(" SAXException " + e
					);
			this.wrong(0, 0, "Parser problem:" + e.toString());
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(" IOException ");
			this.wrong(0, 0, "IO problem: " + e.toString());
			System.out.println(e);
		} catch(Exception e){
			System.out.println(" Exception ");
			this.wrong(0, 0, "Unknown Exception" + e.getStackTrace());        	
		}

		return doc;
	}
}
