package com.pfizer.webapp.wc.components; 

import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.db.MenuList;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;
// Start : Newly Added file for TRT enhancement
public class ManagementSummaryReportWc  extends WebComponent 
{ 
    private ManagementSummaryReport track;
    private MenuList menuItem;
    private String errorMsg="";
    private List reportList;
    private List reportDataList;
    public static final int LAYOUT_XLS=1;
     public int layout;
    
    public void setMenu( MenuList menuItem ) {
        this.menuItem = menuItem;
    }
    public MenuList getMenu() {
        return this.menuItem;
    }

    public void setErrorMsg( String msg ) {
        this.errorMsg = msg;
    }
    public String getErrorMsg() {
        return this.errorMsg;
    }
    public void setTrack(ManagementSummaryReport track ) {
        this.track = track;
    }
    public ManagementSummaryReport getTrack() {
        return this.track;
    }


    public String getJsp()
    {
        if(layout==LAYOUT_XLS)
       {
        return AppConst.JSP_LOC+"/components/ManagementSummaryReportXls.jsp";
       }
       else
       {
       return AppConst.JSP_LOC + "/components/ManagementSummaryReport.jsp";
       }
    }
    public void setupChildren() {}
   
   
    public void setReportList(List reportList){
        this.reportList = reportList;
    }
    public List getReportList(){
        return this.reportList;
    }
   
    public void setReportDataList(List reportList){
        this.reportDataList = reportList;
    }
    public List getReportDataList(){
        return this.reportDataList;
    }
    public void setLayout(int layout)
    {
        this.layout=layout;
    }
    public int getLayout()
    {
        return this.layout;
    }
}//End of addition 
