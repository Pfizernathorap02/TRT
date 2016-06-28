package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.tgix.wc.WebComponent;
import java.util.List;

public class ChartDetailWc extends WebComponent { 
	public static int LAYOUT_ALT = 2;
    
    public static int LAYOUT_MAIN = 1;
    public static int LAYOUT_EMPTY = 3;
    public static int LAYOUT_NO_DATA = 4;
	
	private	PieChart pieChart;
	private String description;
	private boolean showDescription = false;
	private WebComponent legendWc;
    private String linkIdentifier = "";
    private List links;
    
	private int layout = 1;
	
    //Added for major enhancement 3.6 - F1
    private String p2lPhaseChartURL;
	//ends here
	public ChartDetailWc(PieChart chart, String description,WebComponent legend) {
		this.pieChart = chart;
		this.description = description;
		this.legendWc = legend;
    
	}
    
    public ChartDetailWc(PieChart chart, String description,WebComponent legend, String linkIdentifier, List links) {
		this.pieChart = chart;
		this.description = description;
		this.legendWc = legend;
        this.linkIdentifier = linkIdentifier;
        this.links = links;
    
	}
   //Added for major enhancement 3.6 - F1
    
    public String getP2lPhaseChartURL() {
		return p2lPhaseChartURL;
	}
    
     //LoggerHelper.logSystemDebug("inside chartdetail ");
     
    public void setP2lPhaseChartURL(String value){
		p2lPhaseChartURL = value;
	}
    // ends here
    
    public void setChart(PieChart chart) {
        this.pieChart = chart;
    }
    public ChartDetailWc() {
        this.legendWc = new ChartLegendWc(0);
    }
    public String getJsp() {
		if ( layout == LAYOUT_ALT ) {
			return AppConst.JSP_LOC + "/components/chart/chartDetailAlt.jsp";	
		}
		if ( layout == LAYOUT_EMPTY ) {
			return AppConst.JSP_LOC + "/global/emptypage.jsp";
		}
		if ( layout == LAYOUT_NO_DATA ) {
			return AppConst.JSP_LOC + "/components/chart/chartDetailNoData.jsp";
		}
        if(this.linkIdentifier != null &&  this.linkIdentifier.equals("showLink")){
            return AppConst.JSP_LOC + "/components/chart/chartDetailRBU.jsp";
        }
        /* Added for Sustainiability links */
         else if(this.linkIdentifier != null &&  this.linkIdentifier.equals("showReportLink") &&  this.links!=null)
        {
            return AppConst.JSP_LOC + "/components/chart/chartDetail.jsp";
        }        
        else{
            return AppConst.JSP_LOC + "/components/chart/chartDetail.jsp";
        }
	}
	
	public void setLayout( int layout ) {
		this.layout = layout;
	}
	
    public List getLinks(){
        return links;
    }
    
	public PieChart getChart() {
		return pieChart;
	}
	public String getDescription() {
		return description;
	}
    public String getLinkIdentifier() {
		return linkIdentifier;
	}
	public WebComponent getLegend() {
		return legendWc;
	}
	
	public boolean isShowDescription() {
		return showDescription;
	}
	public void setShowDescription( boolean flag ) {
		this.showDescription = flag;
	}
	
	public void setupChildren() {
		children.add( legendWc );
	}	
} 
