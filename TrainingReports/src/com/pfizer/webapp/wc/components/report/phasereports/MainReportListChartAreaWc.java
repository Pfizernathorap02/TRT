package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class MainReportListChartAreaWc extends WebComponent {
    private WebComponent chart;
    
	public MainReportListChartAreaWc(WebComponent chart) {
		super();
        this.chart = chart;
	}
    
    public WebComponent getChart() {
        return chart;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListChartArea.jsp";
	}
	public void setupChildren() {} 
} 
