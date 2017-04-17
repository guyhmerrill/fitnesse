package org.xcylite.fitnesse;

import fit.Parse;
import fitnesse.fixtures.TableFixture;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public abstract class BaseXMLFixture extends TableFixture {

	 protected final String token = "!!";

	    public BaseXMLFixture() {
	        super();
	    }

		protected String setVariable(String name, String value) {
	        VariableMap.getInstance().storeVariable(name, value);
	        return escape(name) + " = " + escape(value)+ "<BR/>";
		}
		
	    // The test table must start in Row #3 - First two rows are reserved for Fixture setup and XML retrieval
	    protected void doStaticTable(int rows) {
	 //   	System.out.println("BaseXMLFixture : doStaticTable ");
	    	
	//    	System.out.println("BaseXMLFixture : retrieveXML - Start ");
	        Document doc = retrieveXML();
	//        System.out.println("BaseXMLFixture : retrieveXML - end ");

	        for (int rowCount = 2; rowCount < rows; rowCount++) {
	            if (!this.blank(rowCount, 0) && !this.blank(rowCount, 1)) {
	                String assertCommand = getText(rowCount, 0);
	                String xpathExpression = getText(rowCount, 1);

	                if ("exists".equalsIgnoreCase(assertCommand)) {
	                    doExistsCommand(rowCount, doc, xpathExpression);
	                } else if ("not exists".equalsIgnoreCase(assertCommand)) {
	                    doNotExistsCommand(rowCount, doc, xpathExpression);
	                } else if ("value".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                    doValueCommand(rowCount, doc, xpathExpression);
	                } else if ("not value".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                    doNotValueCommand(rowCount, doc, xpathExpression);
	                } else if ("passthrough".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                    doPassThroughCommand(rowCount, doc, xpathExpression);
	                } else if ("search".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                    doSearchCommand(rowCount, doc, xpathExpression);
	                } else if ("count".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                    doCountCommand(rowCount, doc, xpathExpression);
	                } else if ("intcompare".equalsIgnoreCase(assertCommand) &&
	                            !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                        doIntCompareCommand(rowCount, doc, xpathExpression);
	                } else if ("doublecompare".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                    doDoubleCompareCommand(rowCount, doc, xpathExpression);
	                }else if ("floatcompare".equalsIgnoreCase(assertCommand) &&
	                    !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                	doFloatCompareCommand(rowCount, doc, xpathExpression);
	                }else if ("stringcompare".equalsIgnoreCase(assertCommand) &&
	                !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                	doStringCompareCommand(rowCount, doc, xpathExpression);
	                }else if ("timestampcompare".equalsIgnoreCase(assertCommand) &&
	                !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                	doTimeStampCompareCommand(rowCount, doc, xpathExpression);}
	                else if ("datecompare".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2) &&!this.blank(rowCount, 3)) {
	                        	doDateCompareCommand(rowCount, doc, xpathExpression);}
	                /* used for schema validation
	                else if ("validation".equalsIgnoreCase(assertCommand) &&
	                        !this.blank(rowCount, 2)) {
	                        	doSchemaValidationCommand(rowCount, doc, xpathExpression);}*/
	                else {
	                    this.wrong(rowCount, 0,
	                        "Unknown XML Fixture command: " + assertCommand);
	                }
	            }
	        }

	        //VariableMap.getInstance().dumpMap();
	    }

	    protected abstract Document retrieveXML();

	    private void doValueCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        try {
	            String expectedValue = getText(rowCount, 2);

	            if (areTokensFound(expectedValue)) {
	                expectedValue = replaceVariables(expectedValue);
	                updateCell(rowCount, 2, expectedValue);
	            }

	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();
	            
	            if (matches == 1) {
	                String actualValue = NamespaceXpathEngine.evaluate(xpathExpression,
	                        doc);

	                if (expectedValue.equals(actualValue)) {
	                    this.right(rowCount, 2);
	                } else {
	                    this.wrong(rowCount, 2, actualValue);
	                }
	            } else if (matches > 1) {
	                this.wrong(rowCount, 1,
	                    "More than one element was found for XPath");
	            } else {
	                this.wrong(rowCount, 1, "Element not found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (Exception e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }

	    private void doNotValueCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        try {
	            String expectedValue = getText(rowCount, 2);

	            if (areTokensFound(expectedValue)) {
	                expectedValue = replaceVariables(expectedValue);
	                updateCell(rowCount, 2, expectedValue);
	            }

	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();

	            if (matches == 1) {
	                String actualValue = NamespaceXpathEngine.evaluate(xpathExpression,
	                        doc);

	                if (!expectedValue.equals(actualValue)) {
	                    this.right(rowCount, 2);
	                } else {
	                    this.wrong(rowCount, 2, actualValue);
	                }
	            } else if (matches > 1) {
	                this.wrong(rowCount, 1,
	                    "More than one element was found for XPath");
	            } else {
	                this.wrong(rowCount, 1, "Element not found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }

	    private void doPassThroughCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        String variableName = getText(rowCount, 2);

	        try {
	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();

	            if (matches == 1) {
	                String actualValue = NamespaceXpathEngine.evaluate(xpathExpression,
	                        doc);

	                if ((actualValue != null) && !actualValue.equals("")) {
	                    VariableMap.getInstance().storeVariable(variableName,
	                        actualValue);
	                    this.updatePassThrough(rowCount, 2, actualValue);
	                    this.right(rowCount, 2);
	                } else {
	                    this.wrong(rowCount, 2,
	                        "No value found for variable '" + variableName + "'");
	                }
	            } else if (matches > 1) {
	                this.wrong(rowCount, 1,
	                    "More than one element was found for XPath");
	            } else {
	                this.wrong(rowCount, 1, "Element not found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }

	    private void doExistsCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        try {
	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();

	            if (matches > 0) {
	                this.right(rowCount, 1);
	            } else {
	                this.wrong(rowCount, 1, "Element not found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (Exception e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }

	    private void doNotExistsCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        try {
	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();

	            if (matches == 0) {
	                this.right(rowCount, 1);
	            } else {
	                this.wrong(rowCount, 1, "Element found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }

	    private void doSearchCommand (int rowCount, Document doc,
	        String xpathExpression) {
	    	
           System.out.println(" xpathExpression " + xpathExpression);
            try {
            	String expectedValue = getText(rowCount, 2);
            	
            	String[] splited = expectedValue.split("\\s");
            	
            	String attrCode = splited[0];
            	String attrVal = splited[0];
            	
            	
            	NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
				NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
				        doc);
				
			       String actualValue = NamespaceXpathEngine.evaluate(xpathExpression,
	                        doc);
			       
			       System.out.println(" actualValue "  + actualValue);
				for (int i = 0; i < nodeList.getLength(); i++) {
					 System.out.println(" i                           : " + i);
					 System.out.println(" nodeList getFirstChild      : " + nodeList.item(i).getFirstChild().toString());
					 System.out.println(" nodeList getFirstChild name : " + nodeList.item(i).getFirstChild().getNodeName());
					 System.out.println(" nodeList getFirstChild text : " + nodeList.item(i).getFirstChild().getTextContent());
					 System.out.println(" nodeList nodename           : " + nodeList.item(i).getNodeName());
					 System.out.println(" nodeList text               : " + nodeList.item(i).getTextContent());
					 System.out.println(" nodeList value              : " + nodeList.item(i).getNodeValue());
					 System.out.println(" nodeList attrvalue          : " + nodeList.item(i).getUserData("attrCode"));
					 System.out.println(" nodeList item local name    : " + nodeList.item(i).getLocalName());
					 System.out.println(" nodeList lastChild          : " + nodeList.item(i).getLastChild().getNodeName());
					 System.out.println(" nodeList getNextSibling     : " + nodeList.item(i).getFirstChild().getNextSibling());
					 System.out.println(" nodeList getParentNode      : " + nodeList.item(i).getFirstChild().getParentNode());
					 
					 Element eElement = (Element) nodeList.item(i);
					 System.out.println(" eElement.getAttribute       : " + eElement.getAttribute("attrCode"));
					 System.out.println(" getTagName			      : " + eElement.getTagName());
					 System.out.println(" Text       			      : " + eElement.getTextContent());
					
				}

				boolean found = false;
				for (int i = 0; i < nodeList.getLength(); i++) {
					
					String lastName =  nodeList.item(i).getLastChild().getNodeName();
					String currentName = null;
					
					Node child = nodeList.item(i).getFirstChild();
					
					System.out.println(" lastName " + lastName);
					
					while (lastName != child.getNodeName()) {
						
						child = child.getNextSibling();
						System.out.println(" next value .... " + child.getNodeValue());
						System.out.println(" next Name .... " + child.getNodeName());
						System.out.println(" next Txt  .... " + child.getTextContent());
						

					}
					
					if (nodeList.item(i).getTextContent().contains(attrCode) && nodeList.item(i).getTextContent().contains(attrVal)) {
						found = true;
					}  
					
				}
				
				if (found) {
					this.right(rowCount, 2);
				} else {
					this.wrong(rowCount, 2);
				}
				 
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    private void doCountCommand(int rowCount, Document doc,
	        String xpathExpression) {
	        int expectedCount = Integer.parseInt(getText(rowCount, 2));

	        try {
	            if (areTokensFound(xpathExpression)) {
	                xpathExpression = replaceVariables(xpathExpression);
	                updateCell(rowCount, 1, xpathExpression);
	            }

	            NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	            NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                    doc);
	            int matches = nodeList.getLength();
	            
	            System.out.println(" " + nodeList.toString());
	            if (matches > 0) {
	                String actualCount = NamespaceXpathEngine.evaluate("count(" +
	                        xpathExpression + ")", doc);

	                if (expectedCount == Integer.parseInt(actualCount)) {
	                    this.right(rowCount, 2);
	                } else {
	                    this.wrong(rowCount, 2, actualCount);
	                }
	            } else {
	                this.wrong(rowCount, 1, "Element not found for XPath");
	            }
	        } catch (PassThroughException pte) {
	            this.wrong(rowCount, 1, pte.getMessage());
	        } catch (TransformerConfigurationException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        } catch (TransformerException e) {
	            this.wrong(rowCount, 1, "Problem with XPath");
	            System.out.println(e);
	        }
	    }
	    //The assertions below are newly added 5-6-09, for information on general use, consult the fitnesse guide on the wiki
	    private void doIntCompareCommand(int rowCount, Document doc, String xpathExpression)
	    {
	    	//grabs the value of the cell and checks to see if it is an integer
	    	String expr = getText(rowCount, 3); 
	    	if(!isNumber(expr,"int")){
	    		this.wrong(rowCount, 3, "Target value is not integer");
	    		return;
	    	}
	    	//if it can be converted to an integer, it does it
	    		int expectedInt = Integer.parseInt(expr);
	    	//grabs the value of the operator from the second cell, <, > or =, anything else throws error
	     String operator =getText(rowCount, 2);
	     try {
	    	 if(areTokensFound(xpathExpression))
	    	 {
	    		 xpathExpression = replaceVariables(xpathExpression);
	    		 updateCell(rowCount, 1, xpathExpression);
	    	 }
	    	 //is used in all assertions to grab the corresponding xpath location
	    	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	         NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                 doc);
	         //if the xpath location exists, it will proceed
	         if (nodeList.getLength() > 0) 
	        {
	        	 //used to grav the value of the xpath in the given document
	        	 expr = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	        	 if(!isNumber(expr,"int")){
	        		 //checks to see if the xpath is an int, if not, shows an error
	        		 this.wrong(rowCount, 1, "Input XPath expression does not evaluate to integer");
	        		 return;
	        	 }
	        	 int actualInt = Integer.parseInt(expr);
	        	 
	        	 //used to determine whether the assertion is true, given the operator, xpath, and value to compare against.
	    	 if(operator.equals("<"))
	    	 {
	    		 if (expectedInt > actualInt)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered is less than or equal to actual");
	    	 }
	    	 else if (operator.equals("="))
	    	 {
	    		 if (expectedInt == actualInt)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered not equal to actual");
	    	 }
	    	 else if (operator.equals(">"))
	    	 {
	    		 if (expectedInt < actualInt)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered is greater than or equal to actual");
	    	 }
	    	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	        }
	         //fails if it doesn't retrieve an xpath value
	     else this.wrong(rowCount, 2, "Failed to retrieve node");
	     }
	      //used for error catching
	    catch (PassThroughException pte) {
	         this.wrong(rowCount, 1, pte.getMessage());
	     } 
	     catch (TransformerConfigurationException e) {
	         this.wrong(rowCount, 1, "Problem with XPath");
	         System.out.println(e);
	     } 
	     catch (TransformerException e) {
	         this.wrong(rowCount, 1, "Problem with XPath");
	         System.out.println(e);
	     } 
	    	
	}
	    private void doDoubleCompareCommand(int rowCount, Document doc, String xpathExpression)
	    {
	    	//many of the steps in this method are very similar to integer compare, where many comments are added.
	    String expr = getText(rowCount, 3); 
		if(!isNumber(expr,"double")){
			this.wrong(rowCount, 3, "Target value is not double");
			return;
		}
		double expectedDouble = Double.parseDouble(expr);
	    
	    String operator =getText(rowCount, 2);
	     try {
	    	 if(areTokensFound(xpathExpression))
	    	 {
	    		 xpathExpression = replaceVariables(xpathExpression);
	    		 updateCell(rowCount, 1, xpathExpression);
	    	 }
	    	 
	    	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	         NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                 doc);
	         
	         if (nodeList.getLength() > 0) 
	        {
	        	 expr = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	        	 if(!isNumber(expr,"int")){
	        		 this.wrong(rowCount, 1, "Input XPath Expression Does not evaluate to a double");
	        		 return;
	        	 }
	        	 double actualDouble = Double.parseDouble(expr);
	        	 
	        	 
	    	 if(operator.equals("<"))
	    	 {
	    		 if (expectedDouble > actualDouble)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered is less than or equal to actual");
	    	 }
	    	 else if (operator.equals("="))
	    	 {
	    		 if (expectedDouble == actualDouble)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered not equal to actual");
	    	 }
	    	 else if (operator.equals(">"))
	    	 {
	    		 if (expectedDouble < actualDouble)
	    		 {this.right(rowCount, 2);}
	    		 else this.wrong(rowCount, 2, "Value entered is greater than or equal to actual");
	    	 }
	    	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	        }
	     else this.wrong(rowCount, 2, "Failed to retrieve node");
	     }
	      
	    catch (PassThroughException pte) {
	         this.wrong(rowCount, 1, pte.getMessage());
	     } 
	     catch (TransformerConfigurationException e) {
	         this.wrong(rowCount, 1, "Problem with XPath");
	         System.out.println(e);
	     } 
	     catch (TransformerException e) {
	         this.wrong(rowCount, 1, "Problem with XPath");
	         System.out.println(e);
	     } 
	     
	    }
	     //many of the steps in float compare are similar to integer compare, please see integer compare comments.
	     private void doFloatCompareCommand(int rowCount, Document doc, String xpathExpression)
	     { String expr = getText(rowCount, 3); 
		if(!isNumber(expr,"float")){
			this.wrong(rowCount, 3, "Target value is not float");
			return;
		}
		float expectedFloat = Float.parseFloat(expr);
		
		String operator =getText(rowCount, 2);
	      try {
	     	 if(areTokensFound(xpathExpression))
	     	 {
	     		 xpathExpression = replaceVariables(xpathExpression);
	     		 updateCell(rowCount, 1, xpathExpression);
	     	 }
	     	 
	     	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	          NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                  doc);
	          
	          if (nodeList.getLength() > 0) 
	         {
	        	  expr = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	         	 if(!isNumber(expr,"int")){
	         		 this.wrong(rowCount, 1, "Input XPath Expression Does not evaluate to float");
	         		 return;
	         	 }
	         	 double actualFloat = Float.parseFloat(expr);
	     	 if(operator.equals("<"))
	     	 {
	     		 if (expectedFloat > actualFloat)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered is less than actual");
	     	 }
	     	 else if (operator.equals("="))
	     	 {
	     		 if (expectedFloat == actualFloat)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered not equal to actual");
	     	 }
	     	 else if (operator.equals(">"))
	     	 {
	     		 if (expectedFloat < actualFloat)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered is greater than actual");
	     	 }
	     	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	         }
	      else this.wrong(rowCount, 2, "Failed to retrieve node");
	      }
	       
	     catch (PassThroughException pte) {
	          this.wrong(rowCount, 1, pte.getMessage());
	      } 
	      catch (TransformerConfigurationException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      catch (TransformerException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	    	
	}
	     private void doStringCompareCommand(int rowCount, Document doc, String xpathExpression)
	     //compares two strings together to get base relationship, for equal, must be exact match, 6.0 != 6
	     //otherwise, very similar to the above, int compare, which is commented thoroughly
	     {String expectedString = getText(rowCount, 3);
	      String operator =getText(rowCount, 2);
	      try {
	     	 if(areTokensFound(xpathExpression))
	     	 {
	     		 xpathExpression = replaceVariables(xpathExpression);
	     		 updateCell(rowCount, 1, xpathExpression);
	     	 }
	     	 
	     	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	          NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                  doc);
	          
	          if (nodeList.getLength() > 0) 
	         {
	         	 String actualString = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	     	 if(operator.equals("<"))
	     	 {
	     		 if (expectedString.compareTo(actualString) > 0)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered is less than actual");
	     	 }
	     	 else if (operator.equals("="))
	     	 {
	     		 if (expectedString.equals(actualString))
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered not equal to actual, =");
	     	 }
	     	 else if (operator.equals(">"))
	     	 {
	     		 if (expectedString.compareTo(actualString) < 0)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Value entered is greater than actual");
	     	 }
	     	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	         }
	      else this.wrong(rowCount, 2, "Failed to retrieve node");
	      }
	       
	     catch (PassThroughException pte) {
	          this.wrong(rowCount, 1, pte.getMessage());
	      } 
	      catch (TransformerConfigurationException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      catch (TransformerException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	    	
	}
	     private void doTimeStampCompareCommand(int rowCount, Document doc, String xpathExpression)
	     //This method is used to compare values in the xml input to the date the wiki user enters.
	     //It takes a variety of formats, with the basis being yyyy-MM-dd
	     //It converts both dates to milliseconds to see which one is larger.
	     //The wiki user can supply > = or < as valid operators to compare against
	     {String tsExpected = getText(rowCount, 3);
	     //receives the value from the third cell and uses it as the expected date
	      String operator =getText(rowCount, 2);
	      //receives the value from the second cell as the operator so it knows how to compare
	      DateFormat formatter; 
	      //used to format the dates
	      Date expectedDate = new Date();
	      
	      //used to convert tsExpected from a string to a date
	      java.util.Date now = null;
	      if (tsExpected.equalsIgnoreCase("now"))
	      {
	    	 formatter =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    	 now = new java.util.Date();
	      }
	      else if (tsExpected.length() == 10)
		  {//checks to see how long the string is so it knows which format the date might be in.
		  formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		  }
		  else if (tsExpected.length() == 23)
		  {
	    	  formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    	  }
		  else if (tsExpected.length() == 22)
		  {
	    	  formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	    	  }
		  else if (tsExpected.length() == 19)
	    	  {
	        	  formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	  }
		  else if (tsExpected.length() == 16)
		  {
	    	  formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	    	  }
		  else { this.wrong(rowCount, 3, "Entered Wiki value is not a valid date format, must be YYYY-MM-DD or YYYY-MM-DD HH:MM:SS");
		  return;
		  }
	           try {//turns tsExpected into a date from a string
	        	   if (now == null)
	        	   {
	        		   expectedDate = (Date)formatter.parse(tsExpected);
	        	   }
	        	   else 
	        	   {
	        		   expectedDate = now;
	        	   }
	           }
	           catch (ParseException e) {
					this.wrong(rowCount, 3, "Please use the YYYY-MM-DD format");
					return;
				}
	      try {
	     	 if(areTokensFound(xpathExpression))
	     	 {
	     		 xpathExpression = replaceVariables(xpathExpression);
	     		 updateCell(rowCount, 1, xpathExpression);
	     	 } 
	     	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	          NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                  doc);
	          
	          if (nodeList.getLength() > 0) 
	         {
	        	  Date actualDate = new Date();
	        	  String tsActual = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	        	  DateFormat formatter2;
	        	  try {
	        	  //Similar to if statements above
	        	  if (tsActual.length() == 10)
	        	  {
	        	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	        	  }
	        	  else if (tsActual.length() == 23)
	        	  {
	            	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	            	  }
	        	  else if (tsActual.length() == 22)
	        	  {
	            	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	            	  }
	        	  else if (tsActual.length() == 19)
	        	  {
	            	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            	  }
	        	  else if (tsActual.length() == 16)
	        	  {
	            	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	            	  }
	        	  else { this.wrong(rowCount, 1, "Input XPath Expression Does not evaluate to a date.");
	        	  return;
	        	  }
	        	  
				
					actualDate = (Date)formatter2.parse(tsActual);
				} catch (ParseException e) {
					this.wrong(rowCount, 1, "Input XPath Expression Does not evaluate to a date.");
				}
				long eDate = expectedDate.getTime();
				long aDate = actualDate.getTime();
	     	 if(operator.equals(">"))
	     	 {
	     		 if (aDate > eDate)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered less than actual");
	     	 }
	     	 else if (operator.equals("="))
	     	 {
	     		 if (eDate == aDate)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered not equal to actual");
	     	 }
	     	 else if (operator.equals("<"))
	     	 {
	     		 if (aDate < eDate)
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered is greater than actual");
	     	 }
	     	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	         }
	      else this.wrong(rowCount, 2, "Failed to retrieve node");
	      }
	       
	     catch (PassThroughException pte) {
	          this.wrong(rowCount, 1, pte.getMessage());
	      } 
	      catch (TransformerConfigurationException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      catch (TransformerException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      
	      }
	     
	     private void doDateCompareCommand(int rowCount, Document doc, String xpathExpression)
	     //The wiki user can supply > = or < as valid operators to compare against
	     {String dateExpected = getText(rowCount, 3);
	     String operator =getText(rowCount, 2);
	     String tDateExpected;
	     if (dateExpected.length() > 9)
		  {
	    	 tDateExpected = dateExpected.substring(0, 10);
		  }
		  else
		  {
			  this.wrong(rowCount, 3, "The date must be in YYYY-MM-DD format");
		  return;
		  }
	     
	     //receives the value from the third cell and uses it as the expected date
	      
	      //receives the value from the second cell as the operator so it knows how to compare
	      DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");; 
	      //used to format the dates
	      Date expectedDate = null;
	      //used to convert tsExpected from a string to a date
		  
	           try {//turns tsExpected into a date from a string
	          expectedDate = (Date)formatter.parse(tDateExpected);
	           }
	           catch (ParseException e) {
					this.wrong(rowCount, 3, "The date must be in YYYY-MM-DD format");
					return;
				}
	      try {
	     	 if(areTokensFound(xpathExpression))
	     	 {
	     		 xpathExpression = replaceVariables(xpathExpression);
	     		 updateCell(rowCount, 1, xpathExpression);
	     	 } 
	     	 NamespaceXpathEngine NamespaceXpathEngine = new NamespaceXpathEngine();
	          NodeList nodeList = NamespaceXpathEngine.getMatchingNodes(xpathExpression,
	                  doc);
	          
	          if (nodeList.getLength() > 0) 
	         {
	        	  Date actualDate = new Date();
	        	  String tsActual = NamespaceXpathEngine.evaluate(xpathExpression, doc);
	        	  String tDateActual;
	        	  DateFormat formatter2;
	        	  tDateActual = tsActual.substring(0,10);
	        	  //similiar to if statements above
	        	  
	        	  formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	        	  
				try{
					actualDate = (Date)formatter2.parse(tDateActual);
				} catch (ParseException e) {
					this.wrong(rowCount, 1, "Input Xpath Expression does not evaluate to a date.");
					return;
				}
	        	
	     	 if(operator.equals("<"))
	     	 {
	     		 if (actualDate.before(expectedDate))
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered less than actual");
	     	 }
	     	 else if (operator.equals("="))
	     	 {
	     		 if (actualDate.equals(expectedDate))
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered not equal to actual");
	     	 }
	     	 else if (operator.equals(">"))
	     	 {
	     		 if (actualDate.after(expectedDate))
	     		 {this.right(rowCount, 2);}
	     		 else this.wrong(rowCount, 2, "Date entered is greater than actual");
	     	 }
	     	 else this.wrong(rowCount, 2, "Please use <, >, or =");
	        	  
	         }
	      else {this.wrong(rowCount, 2, "Failed to retrieve node");}
	          
	      }
	     catch (PassThroughException pte) {
	          this.wrong(rowCount, 1, pte.getMessage());
	      } 
	      catch (TransformerConfigurationException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      catch (TransformerException e) {
	          this.wrong(rowCount, 1, "Problem with XPath");
	          System.out.println(e);
	      } 
	      
	      }
	    
	    private void updatePassThrough(int row, int column, String value) {
	        Parse cell = getCell(row, column);
	        cell.addToTag(" class=\"pass\"");
	        cell.addToBody(" = " + escape(value));
	    }

	    private void updateCell(int row, int column, String value) {
	        Parse cell = getCell(row, column);
	        cell.addToBody(" <HR> " + escape(value));
	    }

	    protected String readFileAsString(String filePath)
	        throws java.io.IOException {
	        System.out.println("Reading file: " + filePath);

	        StringBuffer fileData = new StringBuffer(1000);
	        BufferedReader reader = new BufferedReader(new FileReader(filePath));
	        char[] buf = new char[1024];
	        int numRead = 0;

	        while ((numRead = reader.read(buf)) != -1) {
	            String readData = String.valueOf(buf, 0, numRead);
	            fileData.append(readData);
	            buf = new char[1024];
	        }

	        reader.close();
	        System.out.println("Done reading file...");

	        return fileData.toString();
	    }

	    protected boolean areTokensFound(String input) {
	        if (input.indexOf(token) == -1) {
	            return false;
	        } else {
	            return true;
	        }
	    }

	    protected String replaceVariables(String xmlString)
	        throws PassThroughException {
	        String patternStr = token + "([^!]*)" + token;

	        // Set to false if only the tokens that match the pattern are to be returned.
	        // If true, the text between matching tokens are also returned.
	        boolean returnDelims = true;

	        StringBuffer buffer = new StringBuffer();
	        System.out.println(" In replaceVariables() : " + xmlString);
	        try {
	            // Create the tokenizer
	            RETokenizer tokenizer = new RETokenizer(xmlString, patternStr,
	                    returnDelims);

	            // Get the tokens (and delimiters)
	            while (tokenizer.hasNext()) {
	                if (tokenizer.isNextToken()) {
	                    String variable = (String) tokenizer.next();
	                    System.out.println(" variable " + variable);
	                    variable = variable.substring(token.length(),
	                            variable.length() - token.length());

	                    String value = VariableMap.getInstance().retrieveVariable(variable);

	                    System.out.println(" variable after " + variable); 
	                    if (value == null) {
	                        System.out.println("Required PassThrough - " +
	                            variable +
	                            " - cannot be found from previous test run. Please check variable name...");
	                        throw new PassThroughException(
	                            "Required PassThrough - " + variable +
	                            " - cannot be found from previous test run. Please check variable name...");
	                    } else {
	                        buffer.append(value.trim());
	                    }
	                } else {
	                    buffer.append((String) tokenizer.next());
	                }
	            }
	        } catch (PassThroughException pte) {
	            throw pte;
	        } catch (Exception e) {
	            this.wrong(0, 0, "Tokenizer exception: " + e.getMessage());
	        }
	        System.out.println(" buffer.toString().trim() : " + buffer.toString().trim()); 
	        return buffer.toString().trim();
	    }
	    
	    private boolean isNumber(String input,String type){
	    	
	    	try{
	    		if(type.equals("int")){
	    			Integer.parseInt(input);
	    		}else if (type.equals("float")){
	    			Float.parseFloat(input);
	    		}else if (type.equals("double")){
	    			Double.parseDouble(input);
	    		}
	    		return true;
	    	}catch (NumberFormatException nfe){
	    		return false;
	    	}
	    }
}
