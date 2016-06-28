package com.pfizer.webapp.user; 



import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;

import java.util.List;



public class UserTerritory {

	public static final int TYPE_DISTRICT	= 31;

	public static final int TYPE_REGION		= 21;

	public static final int TYPE_AREA		= 11;

	public static final int TYPE_NATIONAL	= 01;

	public static final int TYPE_UNKNOWN	= -1;

    /* Added for RBU changes */
    public static final int TYPE_LEVEL1 = 11;
    public static final int TYPE_LEVEL2 = 21;
    public static final int TYPE_LEVEL3 = 31;
    public static final int TYPE_LEVEL4 = 41;
    public static final int TYPE_LEVEL5 = 51;
    public static final int TYPE_LEVEL6 = 61;
    public static final int TYPE_LEVEL7 = 71;
    public static final int TYPE_LEVEL8 = 81;
    public static final int TYPE_LEVEL9 = 91;
    public static final int TYPE_LEVEL10 = 101;
      
	private List areas = new ArrayList();
	
	private int type;
	/* Added for RBU changes */
    private ArrayList completeSalesPositionList = null;
	private HashMap salesPositionHashMap = null;
	private HashMap salesPositionDescIdHashMap = null;
	private int salesPositionHierarchyLevel=0;
    private ArrayList salesGroupList=null;
    private ArrayList firstDropdown = null;

	public UserTerritory() {}

	
	public int getType() {

		if (areas.size() == 0) {
			return TYPE_UNKNOWN;
		}
		if (areas.size() > 1) {
			return TYPE_NATIONAL;
		}

		if ( ( (Area)areas.get( 0 ) ).getRegions().size() > 1 ) {

				return TYPE_AREA;
		}
		if ( ( (Area)areas.get( 0 ) ).getRegions().size() == 1 &&

				( (Region)( (Area)areas.get( 0 ) ).getRegions().get(0) ).getDistricts().size() > 1) {					

				return TYPE_REGION;
		}
		if ( ( (Area)areas.get( 0 ) ).getRegions().size() == 1 &&

				( (Region)( (Area)areas.get( 0 ) ).getRegions().get(0) ).getDistricts().size() == 1) {					

				return TYPE_DISTRICT;
		}
		return TYPE_UNKNOWN;
	}
    
    public int getNewType() {
        
		if(firstDropdown!=null)
        {
            if(firstDropdown.size() >= 1) {
                return TYPE_LEVEL1;  
            }
        }
		return TYPE_UNKNOWN;

	}

	/**
	 * Adds an area object to it's list. 
	 */
	public void addArea( Area area ) {
		Area tmp;
		if ( areas.contains( area ) ) {
			tmp = (Area)areas.get( areas.indexOf( area ) );
			tmp.addRegions(area.getRegions());
		} else {
			areas.add( area );
		}
	}
	/**
	 * for types other than national
	 */
	public Area getArea() {
		if (areas.size() == 1) {
			return (Area)areas.get(0);
		}
		return null;
	}

	/**
	 * Only for type national
	 */ 
	public List getAreas() {
		return areas;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator it = areas.iterator(); it.hasNext(); ) {
			sb.append((Area)it.next());
		}
		return sb.toString();	
	}
    
    /* Added for RBU changes */
    
    public ArrayList getCompleteSalesPositionList() {
		return completeSalesPositionList;
	}
	public void setCompleteSalesPositionList(ArrayList completeSalesPositionList) {
		this.completeSalesPositionList = completeSalesPositionList;
	}
	
	public HashMap getSalesPositionHashMap() {
		return salesPositionHashMap;
	}
	public void setSalesPositionHashMap(HashMap salesPositionHashMap) {
		this.salesPositionHashMap = salesPositionHashMap;
	}
	
	public HashMap getSalesPositionDescIdHashMap() {
		return salesPositionDescIdHashMap;
	}
	public void setSalesPositionDescIdHashMap(HashMap salesPositionDescIdHashMap) {
		this.salesPositionDescIdHashMap = salesPositionDescIdHashMap;
	}
	
	public int getSalesPositionHierarchyLevel() {
		return salesPositionHierarchyLevel;
	}
	public void setSalesPositionHierarchyLevel(int salesPositionHierarchyLevel) {
		this.salesPositionHierarchyLevel = salesPositionHierarchyLevel;
        System.out.println("User Territory:Number of levels"+salesPositionHierarchyLevel);
	}
	
    public ArrayList getSalesGroupList() {
		return salesGroupList;
	}
	public void setSalesGroupList(ArrayList salesGroupList) {
		this.salesGroupList = salesGroupList;
	}
        
    public ArrayList getFirstDropdown() {
		return firstDropdown;
	}
	public void setFirstDropdown(ArrayList firstDropdown) {
		this.firstDropdown = firstDropdown;
	}
    /**
	 * This function returns the ArrayList of Geography Descriptions 
	 * for the given Geography Description from the HashMap
	 * @param parentGeoDesc
	 * @return ArrayList
	 */
	public ArrayList getDropdownSalesPositionDesc(String parentId) {
		
		ArrayList salesPosHierarchy = null;
		salesPosHierarchy = new ArrayList();
		ArrayList salesPosDescs = null;
		salesPosDescs = new ArrayList();
      
		try 
		{		
            if(salesPositionHashMap.containsKey((Object)parentId)){
				salesPosHierarchy = (ArrayList)salesPositionHashMap.get(parentId);
				int totCount = salesPosHierarchy.size();				
              				
				/*for(int i = 0;i<totCount;i++)
				{
					LabelValueBean labelValueBean;
                    labelValueBean=(LabelValueBean)(geoHierarchy.get(i));
                          
					if(!geographyDescs.contains(labelValueBean))
                    {
						geographyDescs.add(labelValueBean);
                    }
				}	*/
				salesPosDescs = salesPosHierarchy;          
			}		
			return salesPosDescs;
		}
        catch(Exception e){
			e.printStackTrace();
		}
		return salesPosDescs;
	}
    
     /**
	 * This function creates the sub query to get the employees under the logged in user's sales position id 
	 * which fall under its hierarchy 
	 * @param geographyDesc
	 */
    public String getAllSalesPosition(String salesPositionId) {
        
        //String geographyId = (String)this.getGeographyDescIdHashMap().get(geographyDesc);
       // System.out.println("SalesPositionId for Report Genaration :"+salesPositionId);
        String buildSalesPosCriteria = null;
        //buildGeoCriteria = " and e.emplid in (select distinct emplid from mv_geography_rbu x CONNECT BY PRIOR geography_id=x.parent_geography_id and geography_id <> parent_geography_id START WITH parent_geography_id = '"+geographyId+"') ";       
        //buildSalesPosCriteria = " and e.emplid in (select distinct emplid from MV_SALESPOSITION_RBU x CONNECT BY PRIOR sales_position_id=x.REPORTS_TO_SALES_POSITION_ID and sales_position_id <> REPORTS_TO_SALES_POSITION_ID START WITH REPORTS_TO_SALES_POSITION_ID = '"+salesPositionId+"') ";
        buildSalesPosCriteria = " and e.emplid in (select distinct emplid from MV_SALESPOSITION_RBU x CONNECT BY PRIOR sales_position_id=x.REPORTS_TO_SALES_POSITION_ID and sales_position_id <> REPORTS_TO_SALES_POSITION_ID START WITH REPORTS_TO_SALES_POSITION_ID = '"+salesPositionId+"' " +
                                " union select distinct emplid from mv_field_employee_rbu x where SALES_POSITION_ID = '"+salesPositionId+"') ";
        //System.out.println("SalesPositionId build criteria for Report Genaration :"+buildSalesPosCriteria);	
		//String to be appended to the SQL for Report Generation
		return buildSalesPosCriteria;
	}
    
    /**
 *	This function creates the append condition for report generation
 *  when a Sales Group is selected.
 *  If Sales Group selected is All, it returns NULL
 *  @param salesGroup
 *  @return String: buildSalesCriteria
 */
	public String getAllSalesGroup(String salesGroup) {
		//String to be appended to the Query for report generation
		String salesCondition = " and e.GROUP_CD in ";
		String salesQuery = null; 
		String buildSalesCriteria = null;
		try
		{
			salesQuery = "('";
			if(salesGroup == "ALL" || salesGroup.equalsIgnoreCase("all") || salesGroup.equalsIgnoreCase("")){
				buildSalesCriteria = " ";
			}
			else{
				buildSalesCriteria = salesCondition+salesQuery+salesGroup+"')";
			}
            //System.out.print("SalesCondition :"+buildSalesCriteria);
			return buildSalesCriteria;
		}catch(Exception e){
			e.printStackTrace();
		}
		//String to be appended to the SQL for Report Generation
		return buildSalesCriteria;
	}

	

} 

