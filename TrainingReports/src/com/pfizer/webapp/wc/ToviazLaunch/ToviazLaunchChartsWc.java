package com.pfizer.webapp.wc.ToviazLaunch;

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.rbu.ProductDataBean;
import com.tgix.wc.WebComponent;

public class ToviazLaunchChartsWc extends WebComponent {
    WebComponent chart = null;
    WebComponent territorySelect;
     WebComponent headerWc;
     ProductDataBean[] productDataBean;
     FutureAllignmentBuDataBean[] buDataBean;
     FutureAllignmentRBUDataBean[] rbuDataBean;
     String firstRequest;
     String selectedProduct;
     String selectedBu;
     String selectedRbu;


    public ToviazLaunchChartsWc(TerritoryFilterForm form, User user, WebComponent headerWc) {
        super();
        this.headerWc = headerWc;
        AreaRegionDistJsWc dynamicJs = new AreaRegionDistJsWc( user.getUserTerritory(), form );

		dynamicJavascripts.add( dynamicJs );

        territorySelect = new TerritorySelectWc( form, user.getUserTerritory(), "/TrainingReports/PWRA/getFilteredChart" );

        ((TerritorySelectWc)territorySelect).setShowTeam(true);


        javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");




    }
     public ToviazLaunchChartsWc(User user, WebComponent headerWc) {
        super();
        this.headerWc = headerWc;
    }


    public String getJsp()
    {
        return "/WEB-INF/jsp/ToviazLaunch/ToviazLaunchCharts.jsp";
    }

    public WebComponent getTerritoryForm() {
        return territorySelect;
    }

    public WebComponent getWebComponent() {
        return chart;
    }

    public void setWebComponent(WebComponent wc) {
        this.chart = wc;
    }

    public WebComponent getHeader() {
		return headerWc;
	}

    public void setupChildren()    {
        children.add( headerWc );
    }

    public ProductDataBean[] getProductDataBean(){
        return productDataBean;
    }

    public void setProductDataBean(ProductDataBean[] dataBean){
        this.productDataBean = dataBean;
    }

     public FutureAllignmentBuDataBean[] getFutureAllignmentBuDataBean(){
        return buDataBean;
    }

    public void setFutureAllignmentBuDataBean(FutureAllignmentBuDataBean[] dataBean){
        this.buDataBean = dataBean;
    }

     public FutureAllignmentRBUDataBean[] getFutureAllignmentRBUDataBean(){
        return rbuDataBean;
    }

    public void setFutureAllignmentRBUDataBean(FutureAllignmentRBUDataBean[] dataBean){
        this.rbuDataBean = dataBean;
    }

    public String getFirstRequest(){
        return firstRequest;
    }

    public void setFirstRequest(String firstRequest){
        this.firstRequest = firstRequest;
    }

    public String getSelectedBu(){
        return selectedBu;
    }

    public void setSelectedBu(String bu){
        this.selectedBu = bu;
    }

    public String getSelectedRbu(){
        return selectedRbu;
    }

    public void setSelectedRbu(String rbu){
        this.selectedRbu = rbu;
    }

    public String getSelectedProduct(){
        return this.selectedProduct;
    }

    public void setSelectedProduct(String selectedProduct){
        this.selectedProduct = selectedProduct;

    }

}
