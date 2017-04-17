package com.agrium.customer.mdm.fitnesse;

import org.w3c.dom.Document;
import org.xcylite.fitnesse.BaseXMLFixture;

import fit.Parse;

public class AgriumID extends BaseXMLFixture{

	
	
    public AgriumID() {
        super();
    } 
    
   protected void doStaticTable(int rows) {
    	
    	String seedNumber = getText(0, 0);
    	String variableInstanceNumber = getText(1, 0);
    	String instanceLetter = getText(2, 0);
 //   	System.out.println(" seedNumber : " + seedNumber);
 //  	System.out.println(" variableInstanceNumber : " + variableInstanceNumber);
 //   	System.out.println(" instanceLetter : " + instanceLetter);
    	
        if (variableInstanceNumber == null) variableInstanceNumber = "";
        if (variableInstanceNumber.length() > 0) {
    		variableInstanceNumber = "-" + variableInstanceNumber;
        }
        if (instanceLetter == null) instanceLetter = "";
        
        String random9Digit = Integer.toString((int)(Math.random() * 1000000000));
        String random4Digit = Integer.toString((int)(Math.random() * 10000));
        String random7Digit = Integer.toString((int)(Math.random() * 10000000)); 
    	
		String out = " ";
		out = out + setVariable("TestNumber" + variableInstanceNumber, seedNumber);
		out = out + setVariable("SourceID" + variableInstanceNumber, random7Digit);
		out = out + setVariable("TaxID" + variableInstanceNumber, random9Digit);
		out = out + setVariable("houseHoldID" + variableInstanceNumber, random4Digit);
    	
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
