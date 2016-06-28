package com.pfizer.webapp.user; 



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;

import java.util.List;



public class GeographyDetails {
	
	private ArrayList completeGeographyList = null;
	private HashMap geographyHashMap = null;
	private HashMap geographyDescIdHashMap = null;
	private int geographyHierarchyLevel=0;

	
	public ArrayList getCompleteGeographyList() {
		return completeGeographyList;
	}
	public void setCompleteGeographyList(ArrayList completeGeographyList) {
		this.completeGeographyList = completeGeographyList;
	}
	
	public HashMap getGeographyHashMap() {
		return geographyHashMap;
	}
	public void setGeographyHashMap(HashMap geographyHashMap) {
		this.geographyHashMap = geographyHashMap;
	}
	
	public HashMap getGeographyDescIdHashMap() {
		return geographyDescIdHashMap;
	}
	public void setGeographyDescIdHashMap(HashMap geographyDescIdHashMap) {
		this.geographyDescIdHashMap = geographyDescIdHashMap;
	}
	
	public int getGeographyHierarchyLevel() {
		return geographyHierarchyLevel;
	}
	public void setGeographyHierarchyLevel(int geographyHierarchyLevel) {
		this.geographyHierarchyLevel = geographyHierarchyLevel;
	}
	
}