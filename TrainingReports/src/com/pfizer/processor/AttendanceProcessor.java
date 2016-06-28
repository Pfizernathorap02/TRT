package com.pfizer.processor; 

import com.pfizer.db.Attendance;
import com.pfizer.db.PassFail;
import com.pfizer.webapp.AppConst;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AttendanceProcessor {
	protected static final Log log = LogFactory.getLog( AttendanceProcessor.class );
	
	// possible status
	public static final String STATUS_ATTENED		= "Attended";
	public static final String STATUS_UNSCHEDULED	= "Absent: Training Needed";
	public static final String STATUS_SCHEDULED		= "Transitional Training";
	public static final String STATUS_REGIONAL		= "Regional Training";
	public static final String STATUS_ONLEAVE		= "On Leave";
	
	// chart colors
	public static final Map colorMap;	
	static {
		Map tMap = new HashMap();
		tMap.put( STATUS_ATTENED, AppConst.COLOR_BLUE );
		tMap.put( STATUS_UNSCHEDULED, AppConst.COLOR_GREEN );
		tMap.put( STATUS_SCHEDULED, AppConst.COLOR_ORANGE );
		tMap.put( STATUS_ONLEAVE, AppConst.COLOR_LIME );
		tMap.put( STATUS_REGIONAL, AppConst.COLOR_CYAN );
		colorMap = Collections.unmodifiableMap(tMap);
	}
	
	private Map yesMap;
	private Map absentMap;
	private Map scheduledMap;
	private Map onLeaveMap;
	private Map regionalMap;
	
	
		
	public AttendanceProcessor(Attendance[] result, Map allEmp, SceProcessor scep ) {
		yesMap = new HashMap();
		absentMap = new HashMap();
		scheduledMap = new HashMap();
		onLeaveMap = new HashMap();
		regionalMap = new HashMap();
		
		if ( result != null && result.length > 0 ) {
			for ( int i = 0; i < result.length; i ++ ) {
				OverallResult or = (OverallResult)allEmp.get( result[i].getEmplid() );
				if ( STATUS_ATTENED.equals( result[i].getStatus() ) ){
					yesMap.put( result[i].getEmplid(),result[i] );
					or.put("attendance",STATUS_ATTENED);
				} else if ( STATUS_UNSCHEDULED.equals( result[i].getStatus() ) ) {
					absentMap.put( result[i].getEmplid(),result[i] );					
					or.put("attendance",STATUS_UNSCHEDULED);
				} else if ( STATUS_SCHEDULED.equals( result[i].getStatus() ) ) {
					//log.info("\n\nSpecial Handling of Transitional Training people");
					// this logic is here to handle those people in Transitional Training
					if (scep != null) {
						if (scep.getComppetenceMap().containsKey(result[i].getEmplid())
							|| scep.getNeedsImprovementMap().containsKey(result[i].getEmplid()) ) {
							yesMap.put( result[i].getEmplid(),result[i] );
							or.put("attendance",STATUS_ATTENED);
							//log.info("\n\nSpecial Handling of Transitional Training people: Completed SCE:" + result[i].getEmplid());
						} else {
							//log.info("\n\nSpecial Handling of Transitional Training people: Not Completed SCE");
							scheduledMap.put( result[i].getEmplid(),result[i] );
							or.put("attendance",STATUS_SCHEDULED);
						}		
					} else {
						scheduledMap.put( result[i].getEmplid(),result[i] );		
						or.put("attendance",STATUS_SCHEDULED);
					}	
						
				} else if ( STATUS_ONLEAVE.equals( result[i].getStatus() ) ) {
					onLeaveMap.put( result[i].getEmplid(),result[i] );	
					or.put("attendance",STATUS_ONLEAVE);			
				} else if ( STATUS_REGIONAL.equals( result[i].getStatus() ) ) {
					//log.info("\n\nSpecial Handling of Regional Training people");
					if (scep != null) {
						if (scep.getComppetenceMap().containsKey(result[i].getEmplid())
							|| scep.getNeedsImprovementMap().containsKey(result[i].getEmplid()) ) {						
							//log.info("\n\nSpecial Handling of Regional Training people: Completed SCE" + result[i].getEmplid());
							yesMap.put( result[i].getEmplid(),result[i] );
							or.put("attendance",STATUS_ATTENED);			
						} else {
							//log.info("\n\nSpecial Handling of Regional Training people: Not Completed SCE");
							regionalMap.put( result[i].getEmplid(),result[i] );
							or.put("attendance",STATUS_REGIONAL);			
						}		
					} else {
						regionalMap.put( result[i].getEmplid(),result[i] );		
						or.put("attendance",STATUS_REGIONAL);			
					}	
				} else {
					log.error("Problem adding this attendance obj:" + result[i]);
				}		
			}
		}		
	}
	
	public int getYesCount() {
		return yesMap.size();
	}
	public int getAbsentCount() {
		return absentMap.size();
	}
	public int getScheduledCount() {
		return scheduledMap.size();	
	}
	public int getOnLeaveCount() {
		return onLeaveMap.size();	
	}
	public int getRegionalCount() {
		return regionalMap.size();	
	}
	
	public Map getYesMap() {
		return yesMap;
	}
	public Map getAbsentMap() {
		return absentMap;
	}
	public Map getScheduledMap() {
		return scheduledMap;
	}
	public Map getOnLeaveMap() {
		return onLeaveMap;
	}
	public Map getRegionalMap() {
		return regionalMap;
	}
	public String getAttendanceStatusByEmployeeId( String id ) {
		if ( yesMap.containsKey( id ) ) {
			return STATUS_ATTENED;
		}
		if ( absentMap.containsKey( id ) ) {
			return "Absent";
		}
		if ( scheduledMap.containsKey( id ) ) {
			return STATUS_SCHEDULED;
		}
		if ( onLeaveMap.containsKey( id ) ) {
			return STATUS_ONLEAVE;
		}
		if ( regionalMap.containsKey( id ) ) {
			return STATUS_REGIONAL;
		}
		return "";
	}
	
	public boolean checkStatusByEmployeeId( String status, String id ) {
		if ( STATUS_ATTENED.equals( status) ) {
			return yesMap.containsKey( id );
		}
		if ( STATUS_UNSCHEDULED.equals( status ) ) {
			return absentMap.containsKey( id );
		}
		if ( STATUS_SCHEDULED.equals( status ) ) {
			return scheduledMap.containsKey( id );
		}
		if ( STATUS_ONLEAVE.equals( status ) ) {
			return onLeaveMap.containsKey( id );
		}
		if ( STATUS_REGIONAL.equals( status ) ) {
			return regionalMap.containsKey( id );
		}
		
		return false;
	}
} 
