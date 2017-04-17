package org.xcylite.fitnesse;

import fitnesse.fixtures.TableFixture;

public class SleepFixture extends TableFixture {

	protected void doStaticTable(int arg0) {
		// TODO Auto-generated method stub
		try {
			for(int rowCount=0; rowCount < arg0; rowCount++) {
				int seconds = getInt(rowCount,0);
				Integer displaySeconds = new Integer(seconds);
				System.out.println("Sleeping for " + displaySeconds.toString());
				Thread.sleep(seconds * 1000);
				System.out.println("Slept for " + displaySeconds.toString());
				this.right(rowCount,0);
			}

		} catch (Exception e) {
			this.wrong(arg0 , 0, e.getMessage());
			System.out.println(e);
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}

	}

}
