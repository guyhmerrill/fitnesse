package org.xcylite.fitnesse;

public class PassThroughException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PassThroughException() {
        super();
    }

    /**
     * @param string
     */
    public PassThroughException(String string)  {
        super(string);
    }

}
