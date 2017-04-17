package org.xcylite.fitnesse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.w3c.dom.Document;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQC;


public class MQXMLFixture extends BaseXMLFixture {

	public MQXMLFixture() {
		super();
	}
	
	protected void doStaticTable(int rows) {
		System.out.println(" MQXMLFixture.doStaticTable ");
		System.out.println(" rows " + rows);
		String queueName = getText(0, 0);
		String xmlFile = getText(1, 0);
		if(putToQueue(xmlFile, queueName))
			right(0, 0);
	}

	protected Document retrieveXML() {

		System.out.println(" MQXMLFixture.retrieveXML ");
		Document doc = null;		
		
		return doc;
	}
	
	private boolean putToQueue(String xmlFile, String queueName) {
		System.out.println(" MQXMLFixture.putToQueue");
		
		System.out.println(" xmlFile " + xmlFile);
		System.out.println(" queueName " + queueName);
		
		
		boolean putWorked = false;
				
		MQQueueManager qm;
		try {
			File messagefile = new File(xmlFile);
			String messagestring = getFileAsString(messagefile);
			qm = MQConnectionFixture.getQueueManager();
			MQQueue q = qm.accessQueue(queueName, MQC.MQOO_OUTPUT);
			MQMessage message = new MQMessage();
			message.format = MQC.MQFMT_STRING;
			message.writeString(messagestring);
			q.put(message);
			putWorked = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return putWorked;
	}

	private final String getFileAsString(final File file) throws IOException {
		final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		final byte [] bytes = new byte[(int) file.length()];
		bis.read(bytes);
		bis.close();
		return new String(bytes);
	}
	
}
