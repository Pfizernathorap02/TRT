package com.pfizer.webapp.wc.components.chart;



import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.UserFilter;

import com.tgix.wc.WebComponent;



public final class ToviazLaunchChartHeaderWc extends WebComponent {

	private final String product;

	private final String bu;

	private final String rbu;

	private final int numTrainees;

	private String productCd="";

    // default value for the jsp page.
    private String jsp = AppConst.JSP_LOC + "/components/chart/toviazLaunchChartHeader.jsp";




	public ToviazLaunchChartHeaderWc(String bu, String rbu, int numTrainess) {

//		this.product = product;
this.product  = "";

		this.bu = bu;

		this.rbu = rbu;

		this.numTrainees = numTrainess;

	}

    public String getJsp() {

		return jsp;

	}

    public void setJsp( String jsp ) {
        this.jsp = jsp;
    }


	public String getProductCode() {

		return this.productCd;

	}



	public String getProduct() {

		return product;

	}

	public String getBu() {

		return bu;

	}

	public String getRbu() {

		return rbu;

	}

	public int getNumTrainees() {

		return numTrainees;

	}

	public void setupChildren() {}



}

