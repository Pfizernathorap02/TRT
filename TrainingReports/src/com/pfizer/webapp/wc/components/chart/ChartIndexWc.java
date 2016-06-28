package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.tgix.wc.WebComponent;
import java.util.List;
import java.util.ArrayList;

public class ChartIndexWc extends WebComponent { 
	private WebComponent headerWc;
	private WebComponent chartListWc;
	private WebComponent rightBarWc;
	
	private int numTrainees = 0;

	public ChartIndexWc(WebComponent header, WebComponent chartList, WebComponent rightBar, int numTrainees) {
		this.chartListWc = chartList;
		this.headerWc = header;
		this.rightBarWc = rightBar;
		this.numTrainees = numTrainees;
	}
	public ChartIndexWc(WebComponent header, WebComponent chartList, WebComponent rightBar) {
		this.chartListWc = chartList;
		this.headerWc = header;
		this.rightBarWc = rightBar;
		this.numTrainees = 1;
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
		return AppConst.JSP_LOC + "/components/chart/chartIndex.jsp";
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
