package com.pfizer.webapp.wc.components.admin; 

import com.pfizer.webapp.AppConst;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;
import com.pfizer.db.GAProdCourse;
import com.pfizer.db.GAProduct; 

public class GapAnalysisAdminWc extends WebComponent { 
    private GAProduct[] products;
    private GAProdCourse[] pcMap;
    private String selectedProduct = "";
    private String courseCode = "";
    private String message = "";
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
    public void setSelectedProduct(String sProduct)
    {
        this.selectedProduct = sProduct;
    }
    
     public void setCourseCode(String cCode)
    {
        this.courseCode = cCode;
    }
    
    public String getSelectedProduct()
    {
       return this.selectedProduct;
    } 
     public String getCourseCode()
    {
       return this.courseCode;
    } 
    public GapAnalysisAdminWc() {
	}

    public void setProducts(GAProduct[] p ) {
        this.products = p;
    }		
    public GAProduct[] getProducts() {
        return this.products;
    }    
    
    public void setPcMap(GAProdCourse[] m ) {
        this.pcMap = m;
    }		
    public GAProdCourse[] getPcMap() {
        return this.pcMap;
    }

    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admin/gapAnalysisAdmin.jsp";
	}  
	public void setupChildren() {}

	public List getProductDisplayList() {
		List ret = new ArrayList();
		for(int i=0;i<products.length;i++) {
			ret.add(new LabelValueBean(products[i].getProduct(),products[i].getProduct()));
		}
		return ret;
	}
}
