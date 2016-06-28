package com.pfizer.processor; 

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

public class PassFailProcessor {
	protected static final Log log = LogFactory.getLog( PassFailProcessor.class );
	


	// chart colors
	public static final Map colorMap;	
	static {
		Map tMap = new HashMap();
		tMap.put( PassFail.CONST_TEST_NOT_TAKEN, AppConst.COLOR_ORANGE );
		tMap.put( PassFail.CONST_TEST_PASS, AppConst.COLOR_BLUE );
		tMap.put( PassFail.CONST_TEST_FAIL, AppConst.COLOR_GREEN );
		colorMap = Collections.unmodifiableMap(tMap);
	}
		
	// this list holds employees that missed at least 1 exam,
	// at this point you don't know which exam is missed.
	private List notTakenList = new ArrayList();
	
	// it's possible that a product has 1 or more exams
	// this map will hold each exam and it's result in a TestResult obejct.
	private Map exams = new HashMap();
	
	public  PassFailProcessor(PassFail[] result, Map allEmp) {
		String examName = "";
		TestResult testResult;
		if ( result != null && result.length > 0 ) {
			for ( int i = 0; i < result.length; i ++ ) {
				try {
					examName = result[i].getExamName(); 
					if ( PassFail.CONST_TEST_NOT_TAKEN.equals( result[i].getStatus() ) ) {
						notTakenList.add(result[i]);			
							
					} else {				
						// now handle exams that an employee def took.
						if ( exams.get(examName) != null ) {
							testResult = (TestResult)exams.get(examName);
						} else {
							testResult = new TestResult( examName );					
							exams.put(examName,testResult);
						}
						
						OverallResult or = (OverallResult)allEmp.get( result[i].getEmplId() );					
						if ( PassFail.CONST_TEST_PASS.equals( result[i].getStatus() ) ) {
							testResult.addPass(result[i]);							
							or.put((String)OverallProcessor.testColumnMap.get(examName),result[i].getScore());
						}
		
						if ( PassFail.CONST_TEST_FAIL.equals( result[i].getStatus() ) ) {
							testResult.addFail(result[i]);					
							or.put((String)OverallProcessor.testColumnMap.get(examName),result[i].getScore());
						}
	
					}
				} catch (Exception e) {
					//log.info("Score:" +result[i].getScore() + ":" + result[i].getEmplId() + "examName" + examName);
					//(String)OverallProcessor.testColumnMap.get(examName),result[i].getScore()
					log.info("Test:" + (OverallResult)allEmp.get( result[i].getEmplId() ));
					log.error(e,e);
				}				
			}
		}
		
		// as stated above the notTakenList is only a list of people that did not
		// take at least one of exams.  It is possible that they took 1 of 2 exams.
		// add the entire notTakenList to each TestResult object, it will take care of
		// the list and will only add people that miss that instance of the exam.
		Set keys = exams.keySet();
		String tmpKey;
		TestResult tmpResult;
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			tmpKey = (String)it.next();
			tmpResult = (TestResult)exams.get(tmpKey);
			tmpResult.addNotTakenList(notTakenList);
		}
	}
	
	public String getPassedByTestEmployeeId( String testName, String employeeId ) {
		TestResult testResult = (TestResult)exams.get( testName );
		
		if ( testResult != null ) {
			if ( testResult.getPassedList().containsKey( employeeId ) ) {
				return PassFail.CONST_TEST_PASS;
			}
			if ( testResult.getFaillist().containsKey( employeeId ) ) {
				return PassFail.CONST_TEST_FAIL;
			}
		} 
		
		return PassFail.CONST_TEST_NOT_TAKEN;
	}
	public String getTestScoreEmployeeId( String testName, String employeeId ) {
		TestResult testResult = (TestResult)exams.get( testName );
		
		if ( testResult != null ) {
			if ( testResult.getPassedList().containsKey( employeeId ) ) {
				PassFail pf = (PassFail)testResult.getPassedList().get( employeeId );
				return "" + pf.getScore();
			}
			if ( testResult.getFaillist().containsKey( employeeId ) ) {
				PassFail pf = (PassFail)testResult.getFaillist().get( employeeId );
				return "" + pf.getScore();
			}
		} 
		
		return PassFail.CONST_TEST_NOT_TAKEN;
	}

	public String getPassedByTestHtmlStrEmployeeId( String testName, String employeeId ) {
		TestResult testResult = (TestResult)exams.get( testName );
		
		if ( testResult != null ) {
			if ( testResult.getPassedList().containsKey( employeeId ) ) {
				return "&ge; 80%";
			}
			if ( testResult.getFaillist().containsKey( employeeId ) ) {
				return "&lt 80%";
			}
		} 
		
		return PassFail.CONST_TEST_NOT_TAKEN;
	}

	public boolean checkStatusByEmployeeId( String exam, String status, String id ) {
		TestResult testResult = (TestResult)exams.get( exam );
		
		if ( testResult != null ) {
			if ( PassFail.CONST_TEST_PASS.equals( status) ) {
				return testResult.getPassedList().containsKey( id );
			}
			if ( PassFail.CONST_TEST_FAIL.equals( status) ) {
				return testResult.getFaillist().containsKey( id );
			}
			if ( PassFail.CONST_TEST_NOT_TAKEN.equals( status) ) {
				return testResult.getNotTakenList().containsKey( id );
			}
		}		
		return false;
	}
		
	public Map getExams() {
		return this.exams;
	}
	

} 
