

package com.tgix.Utils;



import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



public class Timer {

	private static final int SECOND = 1000;

	

	private long start;

    private long last;

	private static Log log = LogFactory.getLog(Timer.class);

	

    public Timer() {

		this.start = System.currentTimeMillis();

		this.last = start;

		

    }

    

    

    public long getFromLast() {

        long retVal = System.currentTimeMillis() - last;

        last = System.currentTimeMillis();

        return retVal;     

    }

    

	

	

	public long getFromLastInSeconds() {

		return getFromLast() / SECOND;  

    }

	

	

	

	public void reset() {

    	if(log.isDebugEnabled()) log.debug("reset()");

        start = System.currentTimeMillis();

        last = start;

    }

    public long getFromStart() {

        long retVal = System.currentTimeMillis() - start;

        return retVal;

    }

	

	

	

	public long getFromStartInSeconds() {

		return getFromStart() / SECOND;

    }

}

