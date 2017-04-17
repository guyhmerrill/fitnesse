package org.xcylite.fitnesse;

import java.util.Date;
import java.util.Hashtable;
import java.sql.Timestamp;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import fit.ColumnFixture;

public class MQConnectionFixture extends ColumnFixture {
	
	public static String qmName = ""; 
	public static String channel = "";
	public static String host = "";
	public static String port = "";

	private static MQQueueManager mqQueueManager;

	public static MQQueueManager getQueueManager() throws Exception {

		int retrycount=0;
		if ((mqQueueManager != null) && ( mqQueueManager.isOpen())) {
			System.out.println("Debug:: log:: Using Existing connection");
			return mqQueueManager;
		}

		if (!"".equals(qmName)) throw new Exception("Server Connections not implemented -- qmName must be blank");

		mqQueueManager = newMqConnection(retrycount);	
		return mqQueueManager;
	}

	private static MQQueueManager newMqConnection(int retrycount) {
		Date today = new Date();
		Timestamp currentTS = new java.sql.Timestamp(today.getTime());
		System.out.println("today ::"+today);
		System.out.println("currentTS ::"+currentTS.toString());

		System.out.println(" port : " + port);
		System.out.println(" host : " + host);
		System.out.println(" channel : " + channel);
		System.out.println(" qmName : " + qmName);
		Hashtable connectionProperties = new Hashtable();
		connectionProperties.put(MQC.HOST_NAME_PROPERTY, host);
		connectionProperties.put(MQC.CHANNEL_PROPERTY, channel);
		connectionProperties.put(MQC.PORT_PROPERTY, Integer.valueOf(port));

		System.out.println(" qmName " + qmName);
		try{
			mqQueueManager = new MQQueueManager(qmName, connectionProperties);
			System.out.println("DEBUG::INFO::New connection is created to the queue manager ");
		}
		catch(MQException mex)
		{
			System.out.println(" MQException : " + mex);
			if(mex.reasonCode==2009)
			{
				try {            		
					if(retrycount<5){
						retrycount++;
						Thread.sleep(30000L);
						System.out.println("Retrying queue manager connection :: "+ retrycount);
						newMqConnection(retrycount);
					}
					else{
						System.out.println("An MQ error occurred while creating connection to the queue manager : Completion code " + mex.completionCode + 
								" Reason code" + mex.reasonCode);

					}

				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			System.out.println(" Exception : " + e);
			
		}

		return mqQueueManager;
	}

	/**
	 * Put this in the finally of the caller
	 */
	public static void closeQueueManager() throws MQException {
		if (mqQueueManager != null) {
			if (mqQueueManager.isOpen()) {
				mqQueueManager.disconnect();
				mqQueueManager.close();
				System.out.println("Debug:: log:: Close queue manager.Connection is closed");
			}
			mqQueueManager = null;

		}
	}
}
