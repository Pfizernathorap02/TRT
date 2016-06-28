package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.tgix.wc.WebComponent;
import java.util.List;
import java.util.ArrayList;

public class POAIndexWc extends WebComponent { 
	private WebComponent headerWc;
	private WebComponent chartListWc;
	private WebComponent rightBarWc;
	
	private int numTrainees = 0;

	public POAIndexWc(WebComponent chartList, WebComponent rightBar) {
		this.chartListWc = chartList;
		
		this.rightBarWc = rightBar;
		
	}
	
	public int getNumTrainess() {
		return numTrainees;
	}
	public void setupChildren() {
		children.add( headerWc );
		children.add( chartListWc );
		children.add( rightBarWc );
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/chart/poaIndex.jsp";
	}

	public WebComponent getHeader() {
		return headerWc;
	}
	
	public WebComponent getChartList() {
		return chartListWc;
	}	
	
	public WebComponent getRightBar() {
		return rightBarWc;
	}
} 
