package org.xcylite.fitnesse;

import java.util.HashMap;

public class VariableMap {

	private static VariableMap map = null;
	private HashMap storageMap = null;

	private VariableMap() {
		storageMap = new HashMap(20);
	}

	public static VariableMap getInstance() {
		if (map == null) {
			map = new VariableMap();
		}

		return map;
	}

	public void storeVariable(String variable, String value) {
		storageMap.put(variable, value);
	}

	public String retrieveVariable(String variable) {
		System.out.println("storageMap size" + storageMap.size());
		System.out.println("map " + map.toString());
		return (String) storageMap.get(variable);
	}

	public void clear(){
		if(storageMap!=null){
			storageMap.clear();
		}
	}
}
