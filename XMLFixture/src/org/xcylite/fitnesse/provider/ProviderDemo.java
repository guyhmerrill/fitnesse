package org.xcylite.fitnesse.provider;

import org.w3c.dom.Document;
import org.xcylite.fitnesse.BaseXMLFixture;

import fit.Parse;

public class ProviderDemo  extends BaseXMLFixture {

    public ProviderDemo() {
        super();
    }
    
    protected void doStaticTable(int rows) {
    	
    	String seedNumber = getText(0, 0);
    	String variableInstanceNumber = getText(1, 0);
    	String instanceLetter = getText(2, 0);
    	
    	String random3Digit = Integer.toString((int)(Math.random() * 1000));
    	String random6Digit = Integer.toString((int)(Math.random() * 1000000));
    	String random1Digit = Integer.toString((int)(Math.random() * 10));
    	String random5Digit = Integer.toString((int)(Math.random() * 100000));
    	
    	if (random1Digit.equalsIgnoreCase("0") || random1Digit.equalsIgnoreCase("1")) {
    		random1Digit = "4";
    	}
    	
		String out = " ";
		out = out + setVariable("firstName" + variableInstanceNumber, providerNames.getProviderFirstName().toUpperCase());
		out = out + setVariable("lastName" + variableInstanceNumber, providerNames.getProviderLastName().toUpperCase());
		out = out + setVariable("areaCode" + variableInstanceNumber, random3Digit);
		out = out + setVariable("phoneBody" + variableInstanceNumber, random1Digit + random6Digit);
		out = out + setVariable("zipCode" + variableInstanceNumber, random5Digit);
		out = out + setVariable("zipPlus4" + variableInstanceNumber, random5Digit.substring(1));
		
        Parse cell = getCell(1, 0);
        cell.addToTag(" class=\"pass\"");
        cell.addToBody(out);
    	
    }

	@Override
	protected Document retrieveXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
