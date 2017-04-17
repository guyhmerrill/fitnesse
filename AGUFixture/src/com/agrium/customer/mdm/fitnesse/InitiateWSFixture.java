package com.agrium.customer.mdm.fitnesse;

import org.w3c.dom.Document;
import org.xcylite.fitnesse.BaseXMLFixture;

public class InitiateWSFixture extends BaseXMLFixture{

	
	
	public InitiateWSFixture() {
		super();
	}
	
	
	@Override
	protected Document retrieveXML() {
		
		System.out.println("InitiateWSFixture : retrieveXML ");
		String input_Url = getText(0, 0);
		String soapAction = getText(0, 1);
		String trustAll = getText(0, 2);
		
		Document doc = null;
		String response = null;
		
		
		return null;
	}

}
