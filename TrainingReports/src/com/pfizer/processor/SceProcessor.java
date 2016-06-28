package com.pfizer.processor; 

import com.pfizer.db.Attendance;
import com.pfizer.db.PassFail;
import com.pfizer.db.Sce;
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

public class SceProcessor {
	protected static final Log log = LogFactory.getLog( SceProcessor.class );
	
	
	// chart colors
	public static final Map colorMap;	
	static {
		Map tMap = new HashMap();
		tMap.put( Sce.STATUS_DC, AppConst.COLOR_BLUE );
		tMap.put( Sce.STATUS_UN, AppConst.COLOR_GREEN );
		tMap.put( Sce.STATUS_NOT_COMPLETE, AppConst.COLOR_ORANGE );
		tMap.put( Sce.STATUS_NI, AppConst.COLOR_LIME );
		colorMap = Collections.unmodifiableMap(tMap);
	}
	
	private Map dcMap = new HashMap();
	private Map niMap = new HashMap();
	private Map unMap = new HashMap();
	private Map nullMap = new HashMap();
	
	
		
	public SceProcessor(Sce[] result, Map allemp) {
		if ( result != null && result.length > 0 ) {
			for ( int i = 0; i < result.length; i ++ ) {
				OverallResult or = (OverallResult)allemp.get( result[i].getEmplid() );
				or.put("sce",result[i].getRating());
				
				if ( Sce.STATUS_DC.equals( result[i].getRating() ) ){
					dcMap.put( result[i].getEmplid(),result[i] );
				} else if ( Sce.STATUS_NI.equals( result[i].getRating() ) ) {
					niMap.put( result[i].getEmplid(),result[i] );					
				} else if ( Sce.STATUS_UN.equals( result[i].getRating() ) ) {
					unMap.put( result[i].getEmplid(),result[i] );				
				} else if ( Sce.STATUS_NOT_COMPLETE.equals( result[i].getRating() ) ) {
					nullMap.put( result[i].getEmplid(),result[i] );				
				} else {
					log.error("Problem adding this dce obj:" + result[i]);
				}		
			}
		}		
	}
	
	public int getCompetanceCount() {
		return dcMap.size();
	}
	public int getNeedsImprovementCount() {
		return niMap.size();
	}
	public int getUnacceptableCount() {
		return unMap.size();	
	}
	public int getNullCount() {
		return nullMap.size();	
	}
	
	public Map getComppetenceMap() {
		return dcMap;
	}
	public Map getNeedsImprovementMap() {
		return niMap;
	}
	public Map getUnacceptableMap() {
		return unMap;
	}
	public Map getNullMap() {
		return nullMap;
	}

	public String getSceStatusByEmployeeId( String id ) {
		if ( dcMap.containsKey( id ) ) {
			return "DC";
		}
		if ( niMap.containsKey( id ) ) {
			return "NI";
		}
		if ( unMap.containsKey( id ) ) {
			return "UN";
		}
		if ( nullMap.containsKey( id ) ) {
			return Sce.STATUS_NOT_COMPLETE;
		}
		return Sce.STATUS_NOT_COMPLETE;
	}
	
	public boolean checkStatusByEmployeeId( String status, String id ) {
		if ( Sce.STATUS_DC.equals( status) ) {
			return dcMap.containsKey( id );
		}
		if ( Sce.STATUS_NI.equals( status ) ) {
			return niMap.containsKey( id );
		}
		if ( Sce.STATUS_UN.equals( status ) ) {
			return unMap.containsKey( id );
		}
		if ( Sce.STATUS_NOT_COMPLETE.equals( status ) ) {
			return nullMap.containsKey( id );
		}
		
		return false;
	}	
} 
