package com.pfizer.utils; 

import com.tgix.printing.LoggerHelper;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ReadProperties
{
	
	/* If a constructor without a file name is used, this file will be used for 
	 * reading the properties 
	 */
	private final String PROPERTY_FILE_NAME = "trt.properties";
    private final String EMAIL_PROPERTY_FILE_NAME = "EmailHandlingProperties.properties";


	FileInputStream fileInputStream = null;
	ResourceBundle resourceBundle = null;

	public ReadProperties()
	{
		try
		{
			InputStream inputStream = this.getClass().getResourceAsStream(PROPERTY_FILE_NAME);
			if(inputStream == null)
			{
					//System.out.println("NOT ABLE TO FIND THE PROPERTIES FILE");
			}
			else
			{
				//System.out.println("FOUND THE PROPERTIES FILE");
			}
			this.resourceBundle= new PropertyResourceBundle (inputStream);
			
		}
		catch (Exception e)
		{
			System.out.println("Excpetion occurred in getting the properties files" + e);
		}

	}
	
	/* This method would specify which file to read from 
	 * Added for dbsync.
	 */
	public ReadProperties(String fileName)
	{
		try
		{
			InputStream inputStream = this.getClass().getResourceAsStream(EMAIL_PROPERTY_FILE_NAME);
			if(inputStream == null)
			{
					//System.out.println("NOT ABLE TO FIND THE PROPERTIES FILE");
                    LoggerHelper.logSystemDebug("NOT ABLE TO FIND THE PROPERTIES FILE");
			}
			else
			{
				//System.out.println("FOUND THE PROPERTIES FILE ####");
                LoggerHelper.logSystemDebug("NOT ABLE TO FIND THE PROPERTIES FILE");
			}
			this.resourceBundle= new PropertyResourceBundle (inputStream);
		}
		catch (Exception e)
		{
			System.out.println( "Excpetion occurred in getting the properties files" + e);
            LoggerHelper.logSystemDebug( "Excpetion occurred in getting the properties files" + e);
		}

	}
    
   


	public String getValue(String propName)
	{
		//System.out.println("Initial String"+propName);
		String propValue = null;
		Enumeration enumer  = resourceBundle.getKeys();
		while (enumer.hasMoreElements())
		{
			String keyName = (String) enumer.nextElement();
          //  System.out.println("Current key name"+keyName);
            LoggerHelper.logSystemDebug("Current key name"+keyName);
			if (keyName.equals(propName))
			{
				//System.out.println("Before getting the value from the file");
                LoggerHelper.logSystemDebug("Before getting the value from the file");
                propValue = resourceBundle.getString(keyName);
               // System.out.println("After getting the value from the file"+propValue);
                LoggerHelper.logSystemDebug("After getting the value from the file"+propValue);
				break;
			}
			else
			{
				propValue = "NO MATCH FOUND";
			}
		}
		return propValue;
	}	
}

