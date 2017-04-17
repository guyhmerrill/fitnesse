package org.xcylite.fitnesse.provider;

import org.w3c.dom.Document;
import org.xcylite.fitnesse.BaseXMLFixture;
import org.xcylite.fitnesse.VariableMap;

import fit.Parse;

public class ProviderID extends BaseXMLFixture {

    public ProviderID() {
        super();
    }
    
    protected void doStaticTable(int rows) {
    	
    	String seedNumber = getText(0, 0);
    	String variableInstanceNumber = getText(1, 0);
    	String instanceLetter = getText(2, 0);
    	System.out.println(" seedNumber : " + seedNumber);
    	System.out.println(" variableInstanceNumber : " + variableInstanceNumber);
    	System.out.println(" instanceLetter : " + instanceLetter);
    	
        if (variableInstanceNumber == null) variableInstanceNumber = "";
        if (variableInstanceNumber.length() > 0) {
    		variableInstanceNumber = "-" + variableInstanceNumber;
        }
        if (instanceLetter == null) instanceLetter = "";
        
        String random9Digit = Integer.toString((int)(Math.random() * 1000000000));
        String random5Digit = Integer.toString((int)(Math.random() * 100000));
        String random7Digit = Integer.toString((int)(Math.random() * 10000000)); 
        String random7Digit2 = Integer.toString((int)(Math.random() * 10000000));
    	
		String out = " ";
		out = out + setVariable("TestNumber" + variableInstanceNumber, seedNumber);
		out = out + setVariable("InstanceNumber" + variableInstanceNumber, variableInstanceNumber);
		out = out + setVariable("InstanceLetter" + instanceLetter, instanceLetter);
		out = out + setVariable("leID" + variableInstanceNumber, random7Digit);
		out = out + setVariable("peID" + variableInstanceNumber, random7Digit2);
		out = out + setVariable("DEANumber" + variableInstanceNumber, random9Digit);
		out = out + setVariable("NPINumber" + variableInstanceNumber, random5Digit + random5Digit);
		out = out + setVariable("SourceID" +   variableInstanceNumber, instanceLetter.concat(random7Digit));		
    	
        Parse cell = getCell(1, 0);
        cell.addToTag(" class=\"pass\"");
        cell.addToBody(out);
    }
    
//	private String setVariable(String name, String value) {
//        VariableMap.getInstance().storeVariable(name, value);
//        return escape(name) + " = " + escape(value)+ "<BR/>";
//	}

	@Override
	protected Document retrieveXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
