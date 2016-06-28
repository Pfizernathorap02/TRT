package com.pfizer.webapp.wc.templates; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.global.EmptyPageWc;
import com.pfizer.webapp.wc.global.FooterWc;
import com.pfizer.webapp.wc.global.HeaderAdminReportsWc;
import com.pfizer.webapp.wc.global.HeaderAdminToolsWc;
import com.pfizer.webapp.wc.global.HeaderAdminWc;
import com.pfizer.webapp.wc.global.HeaderControlPanelWc;
import com.pfizer.webapp.wc.global.HeaderDashBoardReportsWc;
import com.pfizer.webapp.wc.global.HeaderEmployeeDetailPageEmailWc;
import com.pfizer.webapp.wc.global.HeaderEmployeeDetailPageWc;
import com.pfizer.webapp.wc.global.HeaderFailureReportWc;
import com.pfizer.webapp.wc.global.HeaderGNSMSEARCHWc;
import com.pfizer.webapp.wc.global.HeaderGNSMWc;
import com.pfizer.webapp.wc.global.HeaderLaunchMeetingReportsWc;
import com.pfizer.webapp.wc.global.HeaderMSEPISEARCHWc;
import com.pfizer.webapp.wc.global.HeaderMSEPIWc;
import com.pfizer.webapp.wc.global.HeaderP4Wc;
import com.pfizer.webapp.wc.global.HeaderPHRWc;
import com.pfizer.webapp.wc.global.HeaderPLCWc;
import com.pfizer.webapp.wc.global.HeaderPOAWc;
import com.pfizer.webapp.wc.global.HeaderPWRAWc;
import com.pfizer.webapp.wc.global.HeaderRBUSearchWc;
import com.pfizer.webapp.wc.global.HeaderSEARCHWc;
import com.pfizer.webapp.wc.global.HeaderSPFHSWc;
import com.pfizer.webapp.wc.global.HeaderSPFPLCWc;
//import com.pfizer.webapp.wc.global.HeaderShipmentLetterWc;
import com.pfizer.webapp.wc.global.HeaderTSHTSEARCHWc;
import com.pfizer.webapp.wc.global.HeaderToviazLaunchEDPWc;
import com.pfizer.webapp.wc.global.HeaderToviazLaunchReportsWc;
import com.pfizer.webapp.wc.global.HeaderUnAuthorizedRoleWc;
import com.pfizer.webapp.wc.global.HeaderVRSSEARCHWc;
import com.pfizer.webapp.wc.global.HeaderWc;
import com.pfizer.webapp.wc.global.UserBarWc;
import com.pfizer.webapp.wc.global.HeaderVRSWc;
import com.pfizer.webapp.wc.global.RBUUserBarEmailWc;
import com.pfizer.webapp.wc.global.RBUUserBarWc;
import com.pfizer.webapp.wc.global.ToviazLaunchUserBarWc;
import com.tgix.wc.WebComponent;
import com.tgix.wc.WebPageComponent;

public class MainTemplateWpc extends SuperWebPageComponents { 
	
	protected WebComponent header; 
	protected WebComponent userBar; 
	protected WebComponent main;
    protected String reportType;
    protected String onLoad = "";
	
	public MainTemplateWpc(User user, String pageId) {		
		super();
		//setLoginRequired( true );
        
		userBar = new UserBarWc(user);
		header =  new HeaderWc( pageId );
        
		cssFiles.add(AppConst.CSS_LOC + "trainning.css");
	}
    
    
    public MainTemplateWpc(User user, String pageId,String reportType) {		
		super();
		//setLoginRequired( true );
        if(reportType.equalsIgnoreCase("")){
		userBar = new UserBarWc(user);
		header =  new HeaderWc( pageId );
        }
        if(reportType.equalsIgnoreCase("POA")){            
        userBar = new UserBarWc(user);
		header =  new HeaderPOAWc( pageId );
        }
        if(reportType.equalsIgnoreCase("PDFHS")){            
        userBar = new UserBarWc(user);
		header =  new HeaderPWRAWc( pageId );
        }
        if(reportType.equalsIgnoreCase("PDFRA")){            
        userBar = new UserBarWc(user);
		header =  new HeaderPWRAWc( pageId );
        }
        
        if(reportType.equalsIgnoreCase("RBUREPORT")){            
            userBar = new RBUUserBarWc(user);
            header =  new HeaderAdminReportsWc(pageId);
        }
        if(reportType.equalsIgnoreCase("P4REPORT")){            
            userBar = new EmptyPageWc();
            header =  new HeaderP4Wc(pageId);
            ((HeaderP4Wc)header).setCustomHeader("<div id=header_title>P4 Training - Classroom Grid Report</div>");
            
        }
        if(reportType.equalsIgnoreCase("P4REPORTROSTER")){            
            userBar = new EmptyPageWc();
            header =  new HeaderP4Wc(pageId);
            ((HeaderP4Wc)header).setCustomHeader("<div id=header_title>P4 Training - Class Roster Report</div>");
            
        }
        if(reportType.equalsIgnoreCase("P4SCEREPORT")){            
            userBar = new EmptyPageWc();
            header =  new HeaderP4Wc(pageId);
            ((HeaderP4Wc)header).setCustomHeader("<div id=header_title>P4 Training - SCE Report</div>");
            
        }
        
        if(reportType.equalsIgnoreCase("DASHBOARDREPORT")){            
            userBar = new RBUUserBarWc(user);
            header =  new HeaderDashBoardReportsWc(pageId);
        }
        if(reportType.equalsIgnoreCase("TOVIAZLAUNCH")){            
            userBar = new ToviazLaunchUserBarWc(user);
            header =  new HeaderToviazLaunchReportsWc(pageId);
        }
        if(reportType.equalsIgnoreCase("LAUNCHMEETING")){            
            userBar = new ToviazLaunchUserBarWc(user);
            header =  new HeaderLaunchMeetingReportsWc(pageId);
        }
        
        if(reportType.equalsIgnoreCase("TOVIAZLAUNCHEXEC")){            
            userBar = new ToviazLaunchUserBarWc(user);
            header =  new HeaderToviazLaunchReportsWc(pageId);
        }
        
         if(reportType.equalsIgnoreCase("PSCPTEDP")){            
            userBar = new RBUUserBarWc(user);
            header =  new HeaderEmployeeDetailPageWc(pageId);
        }
         if(reportType.equalsIgnoreCase("PSCPTEDP_EMAIL")){            
            userBar = new RBUUserBarEmailWc(user);
            header =  new HeaderEmployeeDetailPageEmailWc(pageId);
        }
         
         if(reportType.equalsIgnoreCase("TLEDP")){            
            userBar = new ToviazLaunchUserBarWc(user);
            header =  new HeaderToviazLaunchEDPWc(pageId);
        } 

        if(reportType.equalsIgnoreCase("PSCPT")){            
            userBar = new RBUUserBarWc(user);
            header =  new HeaderControlPanelWc(pageId);
        }
        if(reportType.equalsIgnoreCase("UNAUTHORIZED")){ 
            userBar = new UserBarWc(user);           
            header =  new HeaderUnAuthorizedRoleWc(pageId);
        }
            
       /* if(reportType.equalsIgnoreCase("SHIPLETTER")){            
            userBar = new RBUUserBarWc(user);
            header =  new HeaderShipmentLetterWc(pageId);
        }*/

        if(reportType.equalsIgnoreCase("PDFHSREPORT")){            
        userBar = new UserBarWc(user);
		header =  new HeaderAdminReportsWc( pageId );
        }

        if(reportType.equalsIgnoreCase("PDFHSTOOLS")){            
        userBar = new UserBarWc(user);
		header =  new HeaderAdminToolsWc( pageId );
        }
                        
        if(reportType.equalsIgnoreCase("PLC")){            
        userBar = new UserBarWc(user);
		header =  new HeaderPLCWc( pageId );
        }
                
        if(reportType.equalsIgnoreCase("PHR")){            

        userBar = new UserBarWc(user);
		header =  new HeaderPHRWc( pageId );
        }        
        if(reportType.equalsIgnoreCase("SPFPLC")){            
            userBar = new UserBarWc(user);
            header =  new HeaderSPFPLCWc( pageId );
        }
                
        if(reportType.equalsIgnoreCase("SPFREPORT")){            
        userBar = new UserBarWc(user);
		header =  new HeaderAdminReportsWc( pageId );
        }        
        
        if(reportType.equalsIgnoreCase("SPFTOOLS")){            
        userBar = new UserBarWc(user);
		header =  new HeaderAdminToolsWc( pageId );
        }                
        
        if(reportType.equalsIgnoreCase("SPF")){            
        userBar = new UserBarWc(user);
		header =  new HeaderSPFHSWc( pageId );
        }
        
        if(reportType.equalsIgnoreCase("SEARCH")){            
        userBar = new UserBarWc(user);
		header =  new HeaderSEARCHWc( pageId );        
        } 

        if(reportType.equalsIgnoreCase("GNSMSEARCH")){            
        userBar = new UserBarWc(user);
		header =  new HeaderGNSMSEARCHWc( pageId );        
        } 
        
        if(reportType.equalsIgnoreCase("RBUSEARCH")){            
            userBar = new RBUUserBarWc(user);
            //userBar = new UserBarWc(user);
            header =  new HeaderRBUSearchWc( pageId );        
        } 
                
        if(reportType.equalsIgnoreCase("ADMIN")){            
        userBar = new UserBarWc(user);
		header =  new HeaderAdminWc( pageId );        
        }                 
        
        if(reportType.equalsIgnoreCase("GNSM")){            
            userBar = new UserBarWc(user);
            header =  new HeaderGNSMWc( pageId );
        }
        
        if(reportType.equalsIgnoreCase("MSEPI")){            
            userBar = new UserBarWc(user);
            header =  new HeaderMSEPIWc( pageId );
        }
        
        if(reportType.equalsIgnoreCase("MSEPISEARCH")){            
            userBar = new UserBarWc(user);
            header =  new HeaderMSEPISEARCHWc( pageId );
        }

        if(reportType.equalsIgnoreCase("TSHTSEARCH")){            
        userBar = new UserBarWc(user);
		header =  new HeaderTSHTSEARCHWc( pageId );        
        } 

        if(reportType.equalsIgnoreCase("TSHTSEARCHDETAIL")){            
        userBar = new UserBarWc(user);
		header =  new HeaderTSHTSEARCHWc( pageId );        
        } 

        if(reportType.equalsIgnoreCase("FAILUREREPORT")){            
        userBar = new UserBarWc(user);
		header =  new HeaderFailureReportWc( pageId );        
        }
        
        /* Added for Vista Rx Spiriva enhancement
         * Author:Meenakshi
         * Date: 14-Sep-2008
        */
        if(reportType.equalsIgnoreCase("VRS")){            
        userBar = new UserBarWc(user);
        System.out.println("\nThe PAGE ID for Report type =VRS"+pageId);
		header =  new HeaderVRSWc( pageId );        
        }
        
        if(reportType.equalsIgnoreCase("VRSSEARCH")){            
        userBar = new UserBarWc(user);
        System.out.println("\nThe PAGE ID for Report type =VRSSEARCH"+pageId);
		header =  new HeaderVRSSEARCHWc( pageId );        
        } 
        /* End of modification */
    
        
		cssFiles.add(AppConst.CSS_LOC + "trainning.css");
	}
    
    
	public void setupChildren() {
		children.add( header );
		children.add( main );
	}
	public void setPageId( String id ) {
		((HeaderWc)header).setPageId( id );
	}
	public void setShowNav( boolean flag) {
		((HeaderWc)header).setShowNav( flag );
	}
	public WebComponent getHeader() {
		return header;
	}
	public WebComponent getUserBar() {
		return userBar;
	}	
    public void setHeader( WebComponent header ){
        this.header = header;
    }
    public void setUserBar( WebComponent userBar ) {
        this.userBar = userBar;
    }	
	public void setMain( WebComponent main ) {
		this.main = main;

	}
	public WebComponent getMain() {
		return main;
	}

	public String getOnLoad() {
        return onLoad;
    }
    public void setOnLoad(String onload) {
        this.onLoad = onload;
    }
    public String getJsp()  {
        System.out.println("from maintemplate = " + AppConst.JSP_LOC + "/templates/mainTemplate.jsp");
		return AppConst.JSP_LOC + "/templates/mainTemplate.jsp";
	}
	
	
} 
