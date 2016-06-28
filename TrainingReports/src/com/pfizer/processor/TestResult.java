package com.pfizer.processor; 



import com.pfizer.db.PassFail;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.HashSet;

import java.util.Iterator;

import java.util.List;

import java.util.Map;

import java.util.Set;



public class TestResult {

	private String examName;

	

	private Map passList = new HashMap();

	private Map failList = new HashMap();

	private Map notTakenList = new HashMap();

	

	public TestResult( String examName ) {

		this.examName = examName;

	}

	

	public void addPass( PassFail pf ) {

		passList.put( pf.getEmplId(), pf );

	} 

	public void addFail( PassFail pf ) {

		failList.put( pf.getEmplId(), pf );

	} 

	

	public int getPassCount() {

		return passList.size();

	}

	public int getFailCount() {

		return failList.size();

	}

	public int getNotTakenCount() {

		return notTakenList.size();

	}

	

	public Map getPassedList() {

		return passList;

	}

	public Map getFaillist() {

		return failList;

	}

	public Map getNotTakenList() {

		return notTakenList;

	}

		

	public void addNotTakenList(List notTaken) {

		PassFail temp;

		for (Iterator it = notTaken.iterator(); it.hasNext(); ) {

			temp = (PassFail)it.next();

			if (!passList.containsKey( temp.getEmplId() ) 

				&& !failList.containsKey( temp.getEmplId() ) ) {

				notTakenList.put( temp.getEmplId(), temp );

			}

		}

	}

	

	

} 

