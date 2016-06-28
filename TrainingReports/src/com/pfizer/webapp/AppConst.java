package com.pfizer.webapp; 

import java.awt.Color;

public class AppConst {
	public static final String APP_ROOT="/TrainingReports";
	public static final String RESOURCE_LOC=  "/TrainingReports/resources"; 
	public static final String CSS_LOC= RESOURCE_LOC+"/css/"; 
	public static final String JAVASCRIPT_LOC= RESOURCE_LOC+"/js/";
	
	public static final String WEB_COMPONENT_LOC="/WEB-INF/jsp/components"; 										//"/WEB-INF/jsp/components"; 
	public static final String JSP_LOC="/WEB-INF/jsp";							//"/WEB-INF/jsp"; 
	public static final String IMAGE_DIR = RESOURCE_LOC + "/images";
	
	public static final int CHART_WIDTH = 250;
    public static final int CHART_WIDTH_WITH_LINK = 500;
	public static final int CHART_HEIGHT = 170;
	
	public static final Color COLOR_BLUE = new Color( 0, 113, 189 );
	public static final Color COLOR_GREEN = new Color( 74, 162, 8 );
	public static final Color COLOR_LIME = new Color( 255, 243, 156 );
	public static final Color COLOR_ORANGE = new Color( 214, 101, 0 );
	public static final Color COLOR_CYAN = new Color( 0, 255, 255 );
												 
	public static final String APP_DATASOURCE = "java:jboss/trt_ds";
    
    public static final String APP_EFT_DATASOURCE = "eftds";
    
    public static final String EVENT_PDF = "PDF";
    public static final String EVENT_SPF = "SPF";
    public static final String EVENT_RBU = "RBU";
    public static final String EVENT_P4 = "P4";
    public static final String EVENT_PSCPTEDP = "PSCPTEDP";
    public static final String EVENT_PSCPT = "PSCPT";
    
    public static final String EVENTID_FFT = "1";    
    public static final String EVENTID_PDF = "2";    
    public static final String EVENTID_SPF = "3";    
    public static final String EVENTID_RBU = "4";
} 
