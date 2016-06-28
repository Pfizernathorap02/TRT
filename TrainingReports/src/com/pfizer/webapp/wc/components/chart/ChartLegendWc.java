package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.tgix.wc.WebComponent;



public class ChartLegendWc extends WebComponent { 

    public static int LAYOUT_DEFAULT = 0;
    
	public static int LAYOUT_ATTENDANCE = 1;

	public static int LAYOUT_PASS_FAIL = 2;

	public static int LAYOUT_SCE = 3;

	public static int LAYOUT_OVERALL = 4;
    
    public static int LAYOUT_POA = 5;
    
    public static int LAYOUT_PDFHS = 6;
    
    public static int LAYOUT_PLC = 7;
    
    public static int LAYOUT_SPFPLC = 8;
    
    public static int LAYOUT_PHASE = 9;
    
    public static int LAYOUT_PHASE_PENDING = 10;
    
    public static int LAYOUT_GNSM = 11;
    
    public static int LAYOUT_MSEPI = 12;
    
    /* Added for Vista Rx Spiriva enhancement 
    */
    public static int LAYOUT_VRS=13;
    
    public static int LAYOUT_RBU=14;
    
    
    public static int LAYOUT_TOVIAZ_LAUNCH=15;
    
    public static int LAYOUT_TOVIAZ_LAUNCH_EXEC=16;
    
	//private	PieChart pieChart;

	//private String description;

	//private boolean showDescription = true;

	private int layout = 1;

	private String examName;
    private String key;
	

	public ChartLegendWc( int layout ) {

		this.layout = layout;

	}
    
    
    public ChartLegendWc( int layout ,String key) {

		this.layout = layout;
        setExamName(key);
        this.key = key;
	}
     public String getKey() {
        return key;
    }   

    public String getJsp() {

		if ( layout == LAYOUT_ATTENDANCE ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendAttendance.jsp";		

		}

		if ( layout == LAYOUT_PASS_FAIL ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendPassFail.jsp";		

		}

		if ( layout == LAYOUT_SCE ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendSce.jsp";		

		}
        
        
        if ( layout == LAYOUT_POA ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallPOA.jsp";		

		}

        if ( layout == LAYOUT_PDFHS ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallPDFHS.jsp";		

		}
        if ( layout == LAYOUT_RBU ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallRBU.jsp";		

		}
        
         if ( layout == LAYOUT_TOVIAZ_LAUNCH ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallToviazLaunch.jsp";		

		}
        
        if ( layout == LAYOUT_TOVIAZ_LAUNCH_EXEC ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallToviazLaunchExec.jsp";		

		}
        
        if ( layout == LAYOUT_PLC ) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendOverallPLC.jsp";		
		}

        if ( layout == LAYOUT_DEFAULT ) {
		   return AppConst.JSP_LOC + "/components/chart/chartLegendOverall.jsp";
		}
        
        if ( layout == LAYOUT_SPFPLC ) {
		   return AppConst.JSP_LOC + "/components/chart/chartLegendOverallSPFPLC.jsp";
		}

        if ( layout == LAYOUT_PHASE) {
			return AppConst.JSP_LOC + "/components/chart/phasereports/chartLegendPhase.jsp";		
		}
        if ( layout == LAYOUT_PHASE_PENDING) {

			return AppConst.JSP_LOC + "/components/chart/phasereports/chartLegendPhasePending.jsp";		
		}
        if ( layout == LAYOUT_GNSM) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendGNSM.jsp";		
		}
        if ( layout == LAYOUT_MSEPI) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendMSEPI.jsp";		
		}
        /* Added for Vista Rx Spiriva enhancement */
        
        if ( layout == LAYOUT_VRS) {

			return AppConst.JSP_LOC + "/components/chart/chartLegendVRS.jsp";		
		}
                
		return AppConst.JSP_LOC + "/components/chart/chartLegendOverall.jsp";

	}

	public void setExamName( String name ) {

		this.examName = name;

	}

	public String getExamName() {

		return this.examName;

	}

	public void setupChildren() {}	

} 

