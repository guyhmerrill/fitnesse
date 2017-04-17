package org.xcylite.fitnesse;

import org.w3c.dom.Document;
import fit.Parse;


public class DataCreateFixture extends BaseXMLFixture {
	
	
    public DataCreateFixture() {
        super();
    }
    
    protected void doStaticTable(int rows) {
    	
    	String seedNumber = getText(0, 0);
    	String variableInstanceNumber = getText(1, 0);
        if (variableInstanceNumber == null) variableInstanceNumber = "";
        if (variableInstanceNumber.length() > 0) {
    		variableInstanceNumber = "-" + variableInstanceNumber;
        }
    	
		String randomDigit1 = Integer.toString((int)(Math.random() * 10));
		String randomDigit2 = Integer.toString((int)(Math.random() * 10));
		
		String randomDigit3 = Integer.toString((int)(Math.random() * 10));
		String randomTo999 = randomDigit1 + randomDigit2 + randomDigit3;
		System.out.println("Random Value = " + randomTo999);

		// should be 10-99
		String randomTo12 = Integer.toString((int)(Math.random() * 12) + 1);
		if (randomTo12.length() == 1) randomTo12 = "0" + randomTo12;
		System.out.println("Random Month Value = " + randomTo12);

		String variablePart = randomTo999 + seedNumber;
		
		String out = "<BR/><BR/>";
		out = out + setVariable("TestNumber" + variableInstanceNumber, seedNumber);
		out = out + setVariable("InstanceNumber" + variableInstanceNumber, variableInstanceNumber);
		out = out + setVariable("RandomNumber" + variableInstanceNumber, randomTo999);
		out = out + setVariable("RandomNumber1" + variableInstanceNumber, randomDigit1);
		out = out + setVariable("RandomNumber2" + variableInstanceNumber, randomDigit2);
		out = out + setVariable("RandomNumber2" + variableInstanceNumber, randomDigit3);
		out = out + setVariable("RequestNumber" + variableInstanceNumber, variablePart);
		out = out + setVariable("leID" + variableInstanceNumber, randomDigit3);
		out = out + setVariable("peID" + variableInstanceNumber, randomDigit2);
		out = out + setVariable("CompressedPolicyNumber" + variableInstanceNumber, "HHVB34" + variablePart);
		out = out + setVariable("UncompressedPolicyNumber" + variableInstanceNumber, "HHVB34" + randomDigit1 + " " + randomDigit2 + randomDigit3 + seedNumber);
		out = out + setVariable("FirstName" + variableInstanceNumber, "Mike" + variablePart);
		out = out + setVariable("MiddleName" + variableInstanceNumber, "Z" + variablePart);
		out = out + setVariable("LastName" + variableInstanceNumber, "Johnson" + variablePart);
		out = out + setVariable("OrgName" + variableInstanceNumber, "SomeOrg, LLC" + variablePart);
		out = out + setVariable("SSN" + variableInstanceNumber, variablePart);
		out = out + setVariable("BirthDate" + variableInstanceNumber, "19" + randomDigit1 + randomDigit2 + "-" + randomTo12 + "-" + randomTo12);
		out = out + setVariable("Address" + variableInstanceNumber, variablePart + "West 12th Street Salem, MA 77843");
		out = out + setVariable("AddressStreet1" + variableInstanceNumber, variablePart + "West 12th Street");
		out = out + setVariable("AddressStreet2" + variableInstanceNumber, "");
		out = out + setVariable("AddressStreet3" + variableInstanceNumber, "");
		out = out + setVariable("StreetNumber" + variableInstanceNumber, variablePart);
		out = out + setVariable("StreetName" + variableInstanceNumber, "West 12th Street");
		out = out + setVariable("CityName" + variableInstanceNumber, "Salem");
		out = out + setVariable("StateName" + variableInstanceNumber, "MA");
		out = out + setVariable("StateCode" + variableInstanceNumber, "13");
		out = out + setVariable("ZipCode" + variableInstanceNumber, "77843");
		out = out + setVariable("ZipPlus4" + variableInstanceNumber, "1111");
		out = out + setVariable("Email" + variableInstanceNumber, "sam" + variablePart + "@gmail.com");
		out = out + setVariable("GUID" + variableInstanceNumber, "12345678901" + variablePart);
		out = out + setVariable("HHAccountNumber" + variableInstanceNumber, "???????" + randomTo999 + " " + seedNumber);
		out = out + setVariable("CommercialAccountNumber" + variableInstanceNumber, "???????" + randomTo999 + " " + seedNumber);
        
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
