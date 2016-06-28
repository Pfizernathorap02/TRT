package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.tgix.wc.WebComponent;
import java.util.List;
import java.util.ArrayList;

public class ChartListWc extends WebComponent { 
	public final static int LAYOUT_2COL = 1;
    private List charts = new ArrayList();
	private int trainees = 0;
	private int layout = 0;
    
	public ChartListWc( List charts ) {
		this.charts = charts;

	}
	public void setLayout(int layout) {
        this.layout = layout;
    }
    public String getJsp() {
        
        if ( layout == LAYOUT_2COL ) {
    		return AppConst.JSP_LOC + "/components/chart/chartListAlt.jsp";        
        }
		return AppConst.JSP_LOC + "/components/chart/chartList.jsp";
	}
	
    
    public void setTrainees(int trainees){
        this.trainees=trainees;
    }
    
    public int getTrainees(){
        return this.trainees;
    }
    
	public List getChartList() {
		return charts;
	}
	
	public void setupChildren() {
		// Only need to add one of these objects to the list of children.
		if (charts.size() > 0) {
			children.add( charts.get(0) );
		}
	}	

} 
