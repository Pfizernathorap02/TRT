    package com.pfizer.webapp.user; 

import com.pfizer.db.Product;
import com.tgix.Utils.Util;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TerritoryFilterForm {
	public static final int TYPE_ALL_FILTER = 0;
	public static final int TYPE_AREA_FILTER = 1;
	public static final int TYPE_REGION_FILTER = 2;
	public static final int TYPE_DISTRICT_FILTER = 3;
    public static final int TYPE_PRODUCT_FILTER = 4;
	
	
    public static final int TYPE_ALL_SALESPOS_FILTER = 01;
    public static final int TYPE_LEVEL1_FILTER = 11;
	public static final int TYPE_LEVEL2_FILTER = 21;
	public static final int TYPE_LEVEL3_FILTER = 31;
    public static final int TYPE_LEVEL4_FILTER = 41;
    public static final int TYPE_LEVEL5_FILTER = 51;    
    public static final int TYPE_LEVEL6_FILTER = 61;
	public static final int TYPE_LEVEL7_FILTER = 71;
	public static final int TYPE_LEVEL8_FILTER = 81;
    public static final int TYPE_LEVEL9_FILTER = 91;
    public static final int TYPE_LEVEL10_FILTER = 101;  
	
	// form field names
	public static final String FIELD_AREA		= "UserFilterForm_area";									
	public static final String FIELD_DISTRICT	= "UserFilterForm_district";									
	public static final String FIELD_REGION		= "UserFilterForm_region";		
	public static final String FIELD_TEAM		= "UserFilterForm_team";		
	public static final String FIELD_PRODUCT	= "UserFilterForm_product";	
								
								
	// form values selected								
	private String area;
	private String region;
	private String district;
	private String team;
    private String product;
	
    private List teamList = new ArrayList();
	
    /* Variables added for RBU changes */
    private List salesOrgList = new ArrayList();
    
    private List firstList = new ArrayList();
    private List secondList = new ArrayList();
    private List thirdList = new ArrayList();
    private List fourthList = new ArrayList();
    private List fifthList = new ArrayList();
    private List sixthList = new ArrayList();
    private List seventhList = new ArrayList();
    private List eighthList = new ArrayList();
    private List ninthList = new ArrayList();
    private List tenthList = new ArrayList();
    
    //private List multipleGeos = new ArrayList();
    
    public static final String FIELD_GEOGRAPHY		= "UserFilterForm_geography";		
	public static final String FIELD_SALESORG		= "UserFilterForm_salesOrg";
    
    public static final String FIELD_LEVEL1		= "UserFilterForm_level1";									
	public static final String FIELD_LEVEL2      = "UserFilterForm_level2";									
	public static final String FIELD_LEVEL3		= "UserFilterForm_level3";		
    public static final String FIELD_LEVEL4		= "UserFilterForm_level4";	
    public static final String FIELD_LEVEL5		= "UserFilterForm_level5";	
    public static final String FIELD_LEVEL6		= "UserFilterForm_level6";									
	public static final String FIELD_LEVEL7      = "UserFilterForm_level7";									
	public static final String FIELD_LEVEL8		= "UserFilterForm_level8";		
    public static final String FIELD_LEVEL9		= "UserFilterForm_level9";	
    public static final String FIELD_LEVEL10		= "UserFilterForm_level10";
    
    public static final String FIELD_SELECTEDGEO    = "UserFilterForm_selectedGeo";
    
     //added for TRT major enhancement 3.6- F1
    public static final String FIELD_CHKSTATUS		= "UserFilterForm_chkStatus";
    public static final String FIELD_FROMDATE		= "UserFilterForm_fromDate";
    public static final String FIELD_TODATE		    = "UserFilterForm_toDate";
   //ends here
    
    private String geography;
    private String salesOrg;
    
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
    private String level6;
    private String level7;
    private String level8;
    private String level9;
    private String level10;
    //added for TRT major enhancement 3.6- F1
    private String chkStatus;
    private String fromDate;
    private String toDate;
    //ends here
    
    private String selectedGeo;
    /* Added for Phase 1 by Meenakshi */
    private List statusList= new ArrayList();
    /* End of addition */	
	
	public TerritoryFilterForm() {
		area = new String();
		region = new String();
		district = new String();
        team = new String();
        
         /* Values for RBU */
        geography=new String();
        salesOrg=new String();
        
        level1 = new String();
        level2 = new String();
        level3 = new String();
        level4 = new String();
        level5 = new String();
        level6 = new String();
        level7 = new String();
        level8 = new String();
        level9 = new String();
        level10 = new String();
        
        selectedGeo=new String();
        
        //added for TRT major enhancement 3.6- F1
        chkStatus = new String();
	    fromDate = new String();
	    toDate = new String();
        //ends here
	}
	
    	
	
	/////////////////////////////////////////////////////////////////////////////
	// Form values
	/////////////////////////////////////////////////////////////////////////////
	
    
    public String getLevel1() {
		return this.level1;
	}
	public void setLevel1( String level1 ) {
		if ( Util.isEmpty( level1 ) ) {
			return;
		}
		this.level1 = level1;
	}
    
    public String getLevel2() {
		return this.level2;
	}
	public void setLevel2( String level2 ) {
		if ( Util.isEmpty( level2 ) ) {
			return;
		}
		this.level2 = level2;
	}
    
    public String getLevel3() {
		return this.level3;
	}
	public void setLevel3( String level3 ) {
		if ( Util.isEmpty( level3 ) ) {
			return;
		}
		this.level3 = level3;
	}
    
    public String getLevel4() {
		return this.level4;
	}
	public void setLevel4( String level4 ) {
		if ( Util.isEmpty( level4 ) ) {
			return;
		}
		this.level4 = level4;
	}
    
    public String getLevel5() {
		return this.level5;
	}
	public void setLevel5( String level5 ) {
		if ( Util.isEmpty( level5 ) ) {
			return;
		}
		this.level5 = level5;
	}
    
    public String getLevel6() {
		return this.level6;
	}
	public void setLevel6( String level6 ) {
		if ( Util.isEmpty( level6 ) ) {
			return;
		}
		this.level6 = level6;
	}
    
     public String getLevel7() {
		return this.level7;
	}
	public void setLevel7( String level7 ) {
		if ( Util.isEmpty( level7 ) ) {
			return;
		}
		this.level7 = level7;
	}
    
     public String getLevel8() {
		return this.level8;
	}
	public void setLevel8( String level8 ) {
		if ( Util.isEmpty( level8 ) ) {
			return;
		}
		this.level8 = level8;
	}
    
     public String getLevel9() {
		return this.level9;
	}
	public void setLevel9( String level9 ) {
		if ( Util.isEmpty( level9 ) ) {
			return;
		}
		this.level9 = level9;
	}
    
     public String getLevel10() {
		return this.level10;
	}
	public void setLevel10( String level10 ) {
		if ( Util.isEmpty( level10 ) ) {
			return;
		}
		this.level10 = level10;
	}
    
     //added for TRT major enhancement 3.6- F1
    public String getChkStatus() {
		return this.chkStatus;
	}
	public void setChkStatus( String v ) {
		
		this. chkStatus = v;
	}

     public String getFromDate() {
		return this.fromDate;
	}
	public void setFromDate( String v ) {
		
		this.fromDate = v;
	}

     public String getToDate() {
		return this.toDate;
	}
	public void setToDate( String v ) {
		
		this. toDate = v;
	}
    //ends here
     public void setFirstList(LabelValueBean labelValBean){
        if(!firstList.contains(labelValBean))
            this.firstList.add(labelValBean);
    }
     public List getFirstList(){
        return this.firstList;
    }
    
    public void setSecondList(LabelValueBean labelValBean){
        if(!secondList.contains(labelValBean))
            this.secondList.add(labelValBean);
    }
    
     public List getSecondList(){
        return this.secondList;
    }
    
    
     public void setThirdList(LabelValueBean labelValBean){   
        this.thirdList.add(labelValBean);
    }
     public List getThirdList(){
        return this.thirdList;
    }
    
     public void setFourthList(LabelValueBean labelValBean){   
        this.fourthList.add(labelValBean);
    }
     public List getFourthList(){
        return this.fourthList;
    }
    
     public void setFifthList(LabelValueBean labelValBean){   
        this.fifthList.add(labelValBean);
    }
     public List getFifthList(){
        return this.fifthList;
    }
    
    public void setSixthList(LabelValueBean labelValBean){   
        this.sixthList.add(labelValBean);
    }
     public List getSixthList(){
        return this.sixthList;
    }
    
    public void setSeventhList(LabelValueBean labelValBean){   
        this.seventhList.add(labelValBean);
    }
     public List getSeventhList(){
        return this.seventhList;
    }
    
     public List getEighthList(){
        return this.eighthList;
    }
    
    public void setEighthList(LabelValueBean labelValBean){   
        this.eighthList.add(labelValBean);
    }
     public List getNinthList(){
        return this.ninthList;
    }
    
    public void setNinthList(LabelValueBean labelValBean){   
        this.ninthList.add(labelValBean);
    }
    
     public List getTenthList(){
        return this.tenthList;
    }
    
     public void setTenthList(LabelValueBean labelValBean){   
        this.tenthList.add(labelValBean);
    }
    
    public String getSalesOrg() {
		return this.salesOrg;
	}
    
	public void setSalesOrg( String salesOrg ) {
		this.salesOrg = salesOrg;
	}
    
     public void setSalesOrgList(LabelValueBean labelValBean){
        this.salesOrgList.add(labelValBean);
    }
     public List getSalesOrgList(){
        return this.salesOrgList;
    }
    
    /* Added for Phase 1 by Meenakshi */
     public void setStatusList(){ 
         if(this.statusList.size()<=0)
         {         
             statusList.clear();
             LabelValueBean statusBean= new LabelValueBean("All","All");
             this.statusList.add(statusBean);
             LabelValueBean statusBean1= new LabelValueBean("Complete","Complete");
             this.statusList.add(statusBean1); 
             /*LabelValueBean statusBean2= new LabelValueBean("Waived","Waived");
             this.statusList.add(statusBean2);*/
             LabelValueBean statusBean3= new LabelValueBean("Assigned","Assigned");
             this.statusList.add(statusBean3); 
             LabelValueBean statusBean4= new LabelValueBean("Not Complete","Not Complete");
             this.statusList.add(statusBean4); 
             LabelValueBean statusBean5= new LabelValueBean("On-Leave","On-Leave");
             this.statusList.add(statusBean5); 
             /*LabelValueBean statusBean6= new LabelValueBean("Cancelled","Cancelled");
             this.statusList.add(statusBean6);*/
         }                       
    }
    
     public List getStatusList(){
        return this.statusList;
    }
    /* End of addition */
    
    public String getSelectedGeo() {
		return this.selectedGeo;
	}
	public void setSelectedGeo( String selectedGeo ) {
		if ( Util.isEmpty( selectedGeo ) ) {
			return;
		}
		this.selectedGeo = selectedGeo;
	}
    
   /*   public List getMultipleGeoList(){
        return this.multipleGeos;
    }
    
    public void setMultipleGeoList(LabelValueBean labelValBean){        
            this.multipleGeos.add(labelValBean);        
    } */
    
    /*Clearing functions */
     public void clearFirstList(){
        if (this.firstList.size()>0)
        {
           this.firstList= new ArrayList();
        }
    }
    
     public void clearSecondList(){
        if (this.secondList.size()>0)
        {
       
           this.secondList= new ArrayList();
        }
    }
    public void clearThirdList(){
        if (this.thirdList.size()>0)
        {
           this.thirdList= new ArrayList();
        }
    }
    public void clearFourthList(){
        if (this.fourthList.size()>0)
        {
           this.fourthList= new ArrayList();
        }
    }
    public void clearFifthList(){
        if (this.fifthList.size()>0)
        {
           //System.out.println("Inside clearSecondGeoList");
           this.fifthList= new ArrayList();
        }
    }
    public void clearSixthList(){
        if (this.sixthList.size()>0)
        {
           this.sixthList= new ArrayList();
        }
    }
    public void clearSeventhList(){
        if (this.seventhList.size()>0)
        {
           this.seventhList= new ArrayList();
        }
    }
    public void clearEighthList(){
        if (this.eighthList.size()>0)
        {    
           this.eighthList= new ArrayList();
        }
    }
     public void clearNinthList(){
        if (this.ninthList.size()>0)
        {    
           this.ninthList= new ArrayList();
        }
    }
     public void clearTenthList(){
        if (this.tenthList.size()>0)
        {    
           this.tenthList= new ArrayList();
        }
    }
    
    /* public void clearMultipleGeos(){
        if (this.multipleGeos.size()>0)
        {    
           this.multipleGeos= new ArrayList();
        }
    }
    */
    
    //ended for RBU
    
    
    
    
	public String getArea() {
		return this.area;
	}
	public void setArea( String area ) {
		if ( Util.isEmpty( area ) ) {
			return;
		}
		this.area = area;
	} 
	
	public String getRegion() {
		return this.region;
	}
	public void setRegion( String region ) {
		if ( Util.isEmpty( region ) ) {
			return;
		}

		this.region = region;
	}
	
	public String getDistrict() {
		return this.district;
	}
	public void setDistrict( String district ) {
		if ( Util.isEmpty( district ) ) {
			return;
		}
		this.district = district;
	}
	
	public void setTeam(String team) {
        this.team = team;
    }
    public String getTeam() {
        return this.team;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    public String getProduct() {
        return this.product;
    }
    
    
	public int getSelectionType() {
		if ( "All".equals( area ) ) {
			return TYPE_ALL_FILTER;
		}
		
		if ( !"All".equals( area ) 
				&& "All".equals( region) ) {
			return TYPE_AREA_FILTER;
		}

		if ( !"All".equals( region) 
				&& "All".equals( district ) ) {
			return TYPE_REGION_FILTER;
		}
		
		return TYPE_DISTRICT_FILTER;		
    
	}
    
    
    public int getNewSelectionType() {
		//return TYPE_DISTRICT_FILTER;	
        
        //added for RBU
       // System.out.print("Inside TerritoryFilterForm");
        if ( "All".equals( level1) ) {
            //System.out.print("Type All");
			return TYPE_ALL_SALESPOS_FILTER;
		}
		
		if ( !"All".equals( level1 ) 
				&& "All".equals( level2) ) {
                    // System.out.print("Type 1");
			return TYPE_LEVEL1_FILTER;
		}

		if ( !"All".equals( level2) 
				&& "All".equals( level3) ) {
                  //  System.out.print("Type 2");
			return TYPE_LEVEL2_FILTER;
		}
        
        if ( !"All".equals( level3) 
				&& "All".equals( level4) ) {
                  //  System.out.print("Type 3");
			return TYPE_LEVEL3_FILTER;
		}
        
        if ( !"All".equals( level4) 
				&& "All".equals( level5) ) {
                  //  System.out.print("Type 4");
			return TYPE_LEVEL4_FILTER;
		}
        
        if ( !"All".equals( level5) 
				&& "All".equals( level6) ) {
                  //  System.out.print("Type 5");
			return TYPE_LEVEL5_FILTER;
		}
        
        if ( !"All".equals(level6) 
				&& "All".equals( level7) ) {
                  //  System.out.print("Type 6");
			return TYPE_LEVEL6_FILTER;
		}
        
        if ( !"All".equals( level7) 
				&& "All".equals( level8) ) {
                 //   System.out.print("Type 7");
			return TYPE_LEVEL7_FILTER;
		}
        
        if ( !"All".equals( level8) 
				&& "All".equals( level9) ) {
                  //  System.out.print("Type 8");
			return TYPE_LEVEL8_FILTER;
		}
        
        if ( !"All".equals( level9) 
				&& "All".equals( level10) ) {
                  //  System.out.print("Type 9");
			return TYPE_LEVEL9_FILTER;
		}
		//System.out.print("Type 10");
		return TYPE_LEVEL10_FILTER;
        //ended for RBU	
    
	}
	
    
    public void setTeamList(LabelValueBean labelValBean){
        this.teamList.add(labelValBean);
    }
    
    public String getTeamDesc() {
        if ( teamList != null && teamList.size() > 0 && this.team != null) {
            for (Iterator it = teamList.iterator(); it.hasNext();) {
                LabelValueBean bean = (LabelValueBean)it.next();
                if (this.team.equals(bean.getValue()) ){
                    return bean.getLabel();
                }
            }
        }
        return "";
    }
    public List getTeamList(){
        return this.teamList;
    }
    
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       area: " + area + "\n" );
		sb.append( "	 region: " + region + "\n" );
		sb.append( "   district: " + district + "\n" );
        sb.append( "   product: " + product + "\n" );
		
		return sb.toString();
	}
    
       
    /* Getter and setter methods added for RBU */
    public String getGeography() {
		return this.geography;
	}
	public void setGeography( String geography ) {
		if ( Util.isEmpty( geography ) ) {
			return;
		}
		this.geography = geography;
	} 

} 
