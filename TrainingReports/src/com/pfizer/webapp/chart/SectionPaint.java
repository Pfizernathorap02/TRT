package com.pfizer.webapp.chart; 



import java.awt.Color;



public class SectionPaint {

	public static final String KEY_ATTENED		= "Yes";

	public static final String KEY_UNSCHEDULED	= "Absent / Not Trained";

	public static final String KEY_SCHEDULED	= "Scheduled";



	public static final String KEY_FAIL			= "Failed";

	public static final String KEY_NOTTAKEN		= "Not taken";

	public static final String KEY_PASS			= "Passed";

	

	public SectionPaint() {

	}

	

	public static Color getColor(Comparable key) {

		

		if (KEY_SCHEDULED.equals(key) 

			|| KEY_FAIL.equals(key)) {

			return new Color(214,101,0);

		} else if ( KEY_UNSCHEDULED.equals(key) 

			|| KEY_NOTTAKEN.equals(key)) {

			return new Color(140,199,57);

		} else if ( KEY_ATTENED.equals(key) 

			|| KEY_PASS.equals(key)) {

			return new Color(0,113,189);

		}

		

		return Color.WHITE;

	}

} 

