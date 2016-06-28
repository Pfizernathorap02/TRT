package com.pfizer.processor; 



import com.pfizer.db.Attendance;

import com.pfizer.db.Employee;

import com.pfizer.db.PassFail;

import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.UserFilter;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

import java.util.HashMap;

import java.util.Iterator;

import java.util.List;

import java.util.Map;

import java.util.Set;
import org.apache.commons.collections.comparators.ReverseComparator;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



public class OverallProcessor {
	protected static final Log log = LogFactory.getLog( OverallProcessor.class );

	public static final String COLUMN_ONE	= "test1";
	public static final String COLUMN_TWO	= "test2";
	public static final String COLUMN_THREE = "test3";
	public static final String COLUMN_FOUR	= "test4";
	// possible status
	public static final String STATUS_COMPLETE			= "Complete";
	public static final String STATUS_INCOMPLETE		= "Not Complete";
	public static final String STATUS_ON_LEAVE			= "On Leave";

	// chart colors
	public static final Map colorMap;	
	public static final Map testColumnMap;

	static {
		Map tMap = new HashMap();
		tMap.put( STATUS_COMPLETE, AppConst.COLOR_BLUE );
		tMap.put( STATUS_INCOMPLETE, AppConst.COLOR_ORANGE );
		tMap.put( STATUS_ON_LEAVE, AppConst.COLOR_LIME );
		colorMap = Collections.unmodifiableMap(tMap);
		
		Map tmpMap = new HashMap();
		tmpMap.put("FFT Aricept Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Aricept Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Caduet Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Caduet Exam 2",COLUMN_TWO);
		tmpMap.put("FFT CV Exam 1",COLUMN_THREE);
		tmpMap.put("FFT CV Exam 2",COLUMN_FOUR);
		tmpMap.put("FFT Chantix Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Chantix Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Celebrex Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Celebrex Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Exubera Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Exubera Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Detrol LA Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Detrol LA Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Geodon Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Geodon Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Lipitor Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Lipitor Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Lyrica Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Lyrica Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Rebif Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Rebif Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Revatio Exam",COLUMN_ONE);
		tmpMap.put("FFT Spiriva Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Spiriva Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Viagra Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Viagra Exam 2",COLUMN_TWO);
		tmpMap.put("FFT Zyrtec Exam 1",COLUMN_ONE);
		tmpMap.put("FFT Zyrtec Exam 2",COLUMN_TWO);
		testColumnMap = Collections.unmodifiableMap(tmpMap);
	}

		

	private AttendanceProcessor attendance;
	private PassFailProcessor passFail;
	private SceProcessor sce;

	private Map allEmployeeMap = new HashMap();
	private Employee[] employees;

	private UserFilter currentFilter;

	public OverallProcessor( UserFilter filter ) {
		this.currentFilter = new UserFilter();
		this.currentFilter.setFilterForm( new TerritoryFilterForm() );
		this.currentFilter.getFilterForm().setArea( filter.getFilterForm().getArea() );
		this.currentFilter.getFilterForm().setRegion( filter.getFilterForm().getRegion() );
		this.currentFilter.getFilterForm().setDistrict( filter.getFilterForm().getDistrict() );
		this.currentFilter.setProdcut( filter.getProduct() );
	}

	

	public UserFilter getCurrentFilter() {
		return currentFilter;
	}

	

	public boolean isSameFilter( UserFilter filter) {
		TerritoryFilterForm newForm = filter.getFilterForm();
		TerritoryFilterForm currentForm = currentFilter.getFilterForm();

		if ( currentForm == null) {
			return false;
		}

		if (currentForm.getArea() != null &&
				currentForm.getArea().equals( newForm.getArea() ) &&
				currentForm.getRegion() != null &&
				currentForm.getRegion().equals( newForm.getRegion() ) &&
				currentForm.getDistrict() != null &&
				currentForm.getDistrict().equals( newForm.getDistrict() ) &&
				currentFilter.getProduct() != null &&
				currentFilter.getProduct().equals( filter.getProduct() ) ) {
			return true;
		}

		return false;
	}

	public void setAttendanceProcessor( AttendanceProcessor processor ) {
		this.attendance = processor;
	}
	
	public AttendanceProcessor getAttendanceProcessor() {
		return this.attendance;
	}

	public void setPassFailProcessor( PassFailProcessor processor ) {
		this.passFail = processor;
	}

	public PassFailProcessor getPassFailProcessor() {
		return this.passFail;
	}

	public void setSceProcessor( SceProcessor processor ) {
		this.sce = processor;
	}

	public SceProcessor getSceProcessor() {
		return this.sce;
	}

	public void setEmployees( Employee[] emp ) {
		if ( emp != null ) {
			for (int i = 0; i < emp.length; i++ ) {
				allEmployeeMap.put(emp[i].getEmplId(),new OverallResult(emp[i]));
			}
		}
		this.employees = emp;
	}

	public Map getAllEmployeeMap() {
		return allEmployeeMap;
	}

	public List getAllEmployeeList() {
		List retList = new ArrayList();
		Set keys = allEmployeeMap.keySet();
		int passedCount = 0;

		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			OverallResult or = (OverallResult)allEmployeeMap.get( key );
			retList.add(or);
		}
		Collections.sort(retList,OverallResult.byTerritory);
		return retList;
	}

	public List getAllEmployeeList( ORSortBy sortBy ) {
		List retList = new ArrayList();
		Set keys = allEmployeeMap.keySet();
		int passedCount = 0;

		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			OverallResult or = (OverallResult)allEmployeeMap.get( key );
			retList.add(or);
		}
		
		Comparator comp = null;
		
		if ("district".equals( sortBy.getField() ) ) {
			comp = OverallResult.byDistrict;
		} else if ("lname".equals( sortBy.getField() ) ) {
			comp = OverallResult.byLname;
			
		} else if ("fname".equals( sortBy.getField() ) ) {
			comp = OverallResult.byFname;
			
		} else if ("role".equals( sortBy.getField() ) ) {
			comp = OverallResult.byRole;
			
		} else if ("empid".equals( sortBy.getField() ) ) {
			comp = OverallResult.byEmplid;			
		} else if ("team".equals( sortBy.getField() ) ) {
			comp = OverallResult.byTeam;
		} else if ("attendance".equals( sortBy.getField() ) ) {
			comp = OverallResult.byAttendance;
		} else if ("sce".equals( sortBy.getField() ) ) {
			comp = OverallResult.bySce;
		} else if ("overall".equals( sortBy.getField() ) ) {
			comp = OverallResult.byOverall;
		} else if ("test1".equals( sortBy.getField() ) ) {
			comp = OverallResult.byTest1;
		} else if ("test2".equals( sortBy.getField() ) ) {
			comp = OverallResult.byTest2;
		} else if ("test3".equals( sortBy.getField() ) ) {
			comp = OverallResult.byTest3;
		} else if ("test4".equals( sortBy.getField() ) ) {
			comp = OverallResult.byTest4;
		} else {
			comp = OverallResult.byTerritory;
		}
		
		if ("reverse".equals(sortBy.getDirection() ) ) {
			Collections.sort(retList,new ReverseComparator(comp));				
		}  else {
			Collections.sort(retList,comp);	
		}	
		return retList;		
	}
	

	public int getTotalEmployees() {
		return allEmployeeMap.size();
	}

	

	public int getOverallPassedCount() {
		Set keys = allEmployeeMap.keySet();

		int passedCount = 0;
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			OverallResult or = (OverallResult)allEmployeeMap.get( key );
			if ( or.isPassed() ) {
				passedCount = passedCount + 1;
			}
		}

		return passedCount;
	}

	public void validate() {
		validateAttendance();
		validateSec();
		validateTest();
	}
	
	public int getOverallOnLeaveCount() {
		Set keys = allEmployeeMap.keySet();

		int onLeaveCount = 0;
		String status = null;
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			OverallResult or = (OverallResult)allEmployeeMap.get( key );
			if ( or.isOnLeave() ) {
				onLeaveCount = onLeaveCount + 1;
			}
		}

		return onLeaveCount;
	}
	
	

	private void validateAttendance() {
		// attendance not required
		if ( attendance == null) {
			return;
		}
		
		// get all that doesn't pass this test
		Map failMap = new HashMap();
		failMap.putAll( attendance.getAbsentMap() );
		failMap.putAll( attendance.getOnLeaveMap() );
		failMap.putAll( attendance.getScheduledMap() );
		Set keys = failMap.keySet();

		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();			
			OverallResult or = (OverallResult)allEmployeeMap.get( key );
			if ( or != null ) {
				or.setAttendanceFlag( false );
			} else {
				log.equals("Missing Employee in validateAttendance:" + key);
			}
		}
	}

	/**
	 * If users is DC or NI, they passed
	 */
	private void validateSec() {
		// sce not required
		if ( sce == null) {
			return;
		}
		
		Map failList = new HashMap();
		failList.putAll( sce.getNullMap() );
		failList.putAll(sce.getUnacceptableMap());
		
		Set keys = failList.keySet();

		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();			
			OverallResult or = (OverallResult)allEmployeeMap.get( key );

			if ( or != null ) {
				or.setPassedSceFlag( false );
			} else {
				log.equals("Missing Employee in validateSec:" + key);
			}
		}		

	}

	

	private void validateTest() {
		Set empKey = allEmployeeMap.keySet();
		OverallResult or; 
		String empId;

		for ( Iterator it = empKey.iterator(); it.hasNext(); ) {
			empId = (String)it.next();
			or = (OverallResult)allEmployeeMap.get( empId );
			or.setPassedTestFlag( true );

			Set examNames = passFail.getExams().keySet();
			String name;
			boolean tmpFlag = false;

			for ( Iterator itb = examNames.iterator(); itb.hasNext(); ) {
				name = (String)itb.next();

				tmpFlag = passFail.checkStatusByEmployeeId(  name , PassFail.CONST_TEST_NOT_TAKEN , empId );

				if ( tmpFlag ) {
					or.setPassedTestFlag( false );
				}
			}		
		}		
	}

	

	public void checkAttendance() {
		Map tmp = attendance.getYesMap();
		tmp.putAll(attendance.getAbsentMap());
		tmp.putAll(attendance.getScheduledMap());

		Set keys = tmp.keySet();
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String)it.next();

			if ( !allEmployeeMap.containsKey( key ) ) {
				log.error("\nWho is this:" + key + "\n");
			}
		}
	}

} 

