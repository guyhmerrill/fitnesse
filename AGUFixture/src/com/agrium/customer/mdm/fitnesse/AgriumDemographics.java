package com.agrium.customer.mdm.fitnesse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;
import org.xcylite.fitnesse.BaseXMLFixture;
import fit.Parse;

public class AgriumDemographics extends BaseXMLFixture{
	
    public AgriumDemographics() {
        super();
    }
    
protected void doStaticTable(int rows) {
    	
    	String seedNumber = getText(0, 0);
    	String variableInstanceNumber = getText(1, 0);
    	String instanceLetter = getText(2, 0);
    	
    	String random3Digit = Integer.toString((int)(Math.random() * 1000));
    	String random4Digit = Integer.toString((int)(Math.random() * 10000));
    	String random6Digit = Integer.toString((int)(Math.random() * 1000000));
    	String random1Digit = Integer.toString((int)(Math.random() * 10));
    	String random5Digit = Integer.toString((int)(Math.random() * 100000));
    	
    	if (random1Digit.equalsIgnoreCase("0") || random1Digit.equalsIgnoreCase("1")) {
    		random1Digit = "4";
    	}
    	
		String out = " ";
		out = out + setVariable("companyName" + variableInstanceNumber, AgriumNames.getAgriumCompanyName().toUpperCase());
		out = out + setVariable("StreetName" + variableInstanceNumber, random4Digit + " " +  getStreetName().toUpperCase());
		out = out + setVariable("areaCode" + variableInstanceNumber, random3Digit);
		out = out + setVariable("phoneBody" + variableInstanceNumber, random1Digit + random6Digit);
		out = out + setVariable("fullPhone" + variableInstanceNumber, random3Digit + random1Digit + random6Digit);
		out = out + setVariable("zipCode" + variableInstanceNumber, random5Digit);
		out = out + setVariable("zipPlus4" + variableInstanceNumber, random5Digit.substring(1));
		
        Parse cell = getCell(1, 0);
        cell.addToTag(" class=\"pass\"");
        cell.addToBody(out);
    	
    }

	private String getStreetName() {
		
		String streetName;
		
		List<String> streetNameMap = setUpStreetName();
		int randomIndex = new Random().nextInt(streetNameMap.size());
		
		
		streetName = streetNameMap.get(randomIndex);
		
		return streetName;
		
	}
    
	
	private  List<String>  setUpStreetName() { 
	
		
		List<String> streetList = new ArrayList<String>();
		
		streetList.add("BETHEL RD");
		streetList.add("OAK ST");
		streetList.add("ELM ST");
		streetList.add("Main ST");
		streetList.add("Church ST");
		streetList.add("Main ST North");
		streetList.add("Main ST South");
		streetList.add("High ST");
		streetList.add("Elm ST");
		streetList.add("Main ST West");
		streetList.add("Park AVE");
		streetList.add("Washington ST");
		streetList.add("Main ST East");
		streetList.add("Walnut ST");
		streetList.add("2nd ST");
		streetList.add("Chestnut ST");
		streetList.add("Maple AVE");
		streetList.add("BRD ST");
		streetList.add("Maple ST");
		streetList.add("Center ST");
		streetList.add("Oak ST");
		streetList.add("River RD");
		streetList.add("Pine ST");
		streetList.add("Water ST");
		streetList.add("Market ST");
		streetList.add("South ST");
		streetList.add("Union ST");
		streetList.add("3rd ST");
		streetList.add("Park ST");
		streetList.add("CT ST");
		streetList.add("North ST");
		streetList.add("Washington AVE");
		streetList.add("Cherry ST");
		streetList.add("Highland AVE");
		streetList.add("Prospect ST");
		streetList.add("Spring ST");
		streetList.add("Mill ST");
		streetList.add("School ST");
		streetList.add("4th ST");
		streetList.add("Central AVE");
		streetList.add("Franklin ST");
		streetList.add("1st ST");
		streetList.add("Front ST");
		streetList.add("West ST");
		streetList.add("State ST");
		streetList.add("Cedar ST");
		streetList.add("Jefferson ST");
		streetList.add("Bridge ST");
		streetList.add("Madison AVE");
		streetList.add("Locust ST");
		streetList.add("Park Place");
		streetList.add("5th ST");
		streetList.add("Grove ST");
		streetList.add("Jackson ST");
		streetList.add("Pennsylvania AVE");
		streetList.add("Meadow LN");
		streetList.add("Pearl ST");
		streetList.add("Pleasant ST");
		streetList.add("Spruce ST");
		streetList.add("Adams ST");
		streetList.add("Ridge RD");
		streetList.add("Academy ST");
		streetList.add("Dogwood Drive");
		streetList.add("Lincoln ST");
		streetList.add("Madison ST");
		streetList.add("East ST");
		streetList.add("Jefferson AVE");
		streetList.add("11th ST");
		streetList.add("4th ST West");
		streetList.add("7th ST");
		streetList.add("Lincoln AVE");
		streetList.add("River ST");
		streetList.add("Summit AVE");
		streetList.add("12th ST");
		streetList.add("2nd AVE");
		streetList.add("3rd ST West");
		streetList.add("5th AVE");
		streetList.add("9th ST");
		streetList.add("Cherry LN");
		streetList.add("Route 1");
		streetList.add("Virginia AVE");
		streetList.add("6th ST");
		streetList.add("Charles ST");
		streetList.add("Elizabeth ST");
		streetList.add("Hill ST");
		streetList.add("Woodland Drive");
		streetList.add("10th ST");
		streetList.add("Church RD");
		streetList.add("Delaware AVE");
		streetList.add("Fairway Drive");
		streetList.add("Green ST");
		streetList.add("Hickory LN");
		streetList.add("Liberty ST");
		streetList.add("Route 30");
		streetList.add("2nd ST West");
		streetList.add("BRDway");
		streetList.add("Brookside Drive");
		streetList.add("Monroe ST");
		streetList.add("Vine ST");
		streetList.add("Winding Way");
		streetList.add("13th ST");
		streetList.add("1st AVE");
		streetList.add("3rd AVE");
		streetList.add("College ST");
		streetList.add("Colonial Drive");
		streetList.add("Prospect AVE");
		streetList.add("Sunset Drive");
		streetList.add("Valley RD");
		streetList.add("6th ST West");
		streetList.add("Division ST");
		streetList.add("Hillside AVE");
		streetList.add("Mill RD");
		streetList.add("New ST");
		streetList.add("4th AVE");
		streetList.add("5th ST North");
		streetList.add("8th ST");
		streetList.add("Clinton ST");
		streetList.add("Harrison ST");
		streetList.add("Lake ST");
		streetList.add("Laurel ST");
		streetList.add("Oak LN");
		streetList.add("Primrose LN");
		streetList.add("RailRD AVE");
		streetList.add("RailRD ST");
		streetList.add("Riverside Drive");
		streetList.add("Route 32");
		streetList.add("Route 6");
		streetList.add("Sherwood Drive");
		streetList.add("4th ST North");
		streetList.add("7th AVE");
		streetList.add("Beech ST");
		streetList.add("Buckingham Drive");
		streetList.add("Dogwood LN");
		streetList.add("Hillcrest Drive");
		streetList.add("Lafayette AVE");
		streetList.add("Penn ST");
		streetList.add("Willow ST");
		streetList.add("5th ST West");
		streetList.add("6th AVE");
		streetList.add("Circle Drive");
		streetList.add("Durham RD");
		streetList.add("Front ST North");
		streetList.add("Grant ST");
		streetList.add("Laurel LN");
		streetList.add("Mulberry ST");
		streetList.add("Park Drive");
		streetList.add("Poplar ST");
		streetList.add("Ridge AVE");
		streetList.add("Route 29");
		streetList.add("Walnut AVE");
		streetList.add("Warren ST");
		streetList.add("Williams ST");
		streetList.add("2nd ST East");
		streetList.add("6th ST North");
		streetList.add("7th ST East");
		streetList.add("Berkshire Drive");
		streetList.add("Cambridge Drive");
		streetList.add("Cedar LN");
		streetList.add("Clark ST");
		streetList.add("Elm AVE");
		streetList.add("Essex CT");
		streetList.add("Fulton ST");
		streetList.add("George ST");
		streetList.add("Heather LN");
		streetList.add("Highland Drive");
		streetList.add("John ST");
		streetList.add("King ST");
		streetList.add("Lakeview Drive");
		streetList.add("Linden ST");
		streetList.add("Route 10");
		streetList.add("Summit ST");
		streetList.add("Surrey LN");
		streetList.add("Arch ST");
		streetList.add("Cambridge CT");
		streetList.add("Canal ST");
		streetList.add("College AVE");
		streetList.add("Country Club Drive");
		streetList.add("Franklin AVE");
		streetList.add("Grant AVE");
		streetList.add("Hamilton ST");
		streetList.add("Hillside Drive");
		streetList.add("Holly Drive");
		streetList.add("James ST");
		streetList.add("Lafayette ST");
		streetList.add("Lake AVE");
		streetList.add("Magnolia Drive");
		streetList.add("Maple LN");
		streetList.add("Myrtle AVE");
		streetList.add("Olive ST");
		streetList.add("Oxford CT");
		streetList.add("Smith ST");
		streetList.add("William ST");
		streetList.add("Wood ST");
		streetList.add("Woodland AVE");
		streetList.add("York RD");
		streetList.add("3rd ST East");
		streetList.add("6th ST East");
		streetList.add("8th ST East");
		streetList.add("8th ST West");
		streetList.add("Ann ST");
		streetList.add("Aspen CT");
		streetList.add("Bank ST");
		streetList.add("Canterbury CT");
		streetList.add("Carriage Drive");
		streetList.add("Colonial AVE");
		streetList.add("Columbia ST");
		streetList.add("Creek RD");
		streetList.add("Deerfield Drive");
		streetList.add("Euclid AVE");
		streetList.add("Fairview AVE");
		streetList.add("Forest Drive");
		streetList.add("Garden ST");
		streetList.add("Glenwood AVE");
		streetList.add("Hilltop RD");
		streetList.add("Lilac LN");
		streetList.add("Linden AVE");
		streetList.add("Locust LN");
		streetList.add("Madison CT");
		streetList.add("Magnolia AVE");
		streetList.add("Oak AVE");
		streetList.add("Orange ST");
		streetList.add("Route 9");
		streetList.add("Shady LN");
		streetList.add("Skyline Drive");
		streetList.add("Sycamore Drive");
		streetList.add("Willow LN");
		streetList.add("Windsor CT");
		streetList.add("12th ST East");
		streetList.add("2nd ST North");
		streetList.add("3rd ST North");
		streetList.add("5th ST South");
		streetList.add("9th ST West");
		streetList.add("Arlington AVE");
		streetList.add("Belmont AVE");
		streetList.add("Cardinal Drive");
		streetList.add("Cedar AVE");
		streetList.add("Chestnut AVE");
		streetList.add("Devon RD");
		streetList.add("Garfield AVE");
		streetList.add("Grand AVE");
		streetList.add("Grove AVE");
		streetList.add("Heritage Drive");
		streetList.add("Magnolia CT");
		streetList.add("Montgomery AVE");
		streetList.add("Orchard ST");
		streetList.add("Race ST");
		streetList.add("Rosewood Drive");
		streetList.add("Route 11");
		streetList.add("Route 20");
		streetList.add("Valley View Drive");
		streetList.add("Village Drive");
		streetList.add("Wall ST");
		streetList.add("4th ST East");
		streetList.add("4th ST South");
		streetList.add("7th ST West");
		streetList.add("Beechwood Drive");
		streetList.add("Brown ST");
		streetList.add("Canterbury Drive");
		streetList.add("Canterbury RD");
		streetList.add("Church ST North");
		streetList.add("Clay ST");
		streetList.add("Cleveland ST");
		streetList.add("Clover LN");
		streetList.add("Cottage ST");
		streetList.add("East AVE");
		streetList.add("Evergreen Drive");
		streetList.add("Fawn LN");
		streetList.add("Henry ST");
		streetList.add("Hickory ST");
		streetList.add("Howard ST");
		streetList.add("Laurel Drive");
		streetList.add("Magnolia ST");
		streetList.add("Manor Drive");
		streetList.add("Mechanic ST");
		streetList.add("Overlook Drive");
		streetList.add("Redwood Drive");
		streetList.add("Route 4");
		streetList.add("Route 41");
		streetList.add("Route 70");
		streetList.add("ST RD");
		streetList.add("Sunset AVE");
		streetList.add("Valley View RD");
		streetList.add("Warren AVE");
		streetList.add("WOOD BARK RD");


		
		return streetList;
	}

	@Override
	protected Document retrieveXML() {
		// TODO Auto-generated method stub
		return null;
	}

}
