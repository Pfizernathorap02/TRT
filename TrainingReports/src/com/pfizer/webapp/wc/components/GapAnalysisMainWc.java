package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;
import java.util.Map;

public class GapAnalysisMainWc extends WebComponent
{ 
    public Map Result;
    public String productDesc[];
    public String deploymentPackageId[];
    public int layout;
    public String duration;
    public String deploymentId;
    public String errorMsg=null;
    public static final int LAYOUT_XLS=1;
    
    public String selectedProdDesc[]; //added by Amit B
    public String roleCds[]; //added by Amit B
    public String selectedRoleCds[]; //Added by Amit B
    public String salesOrgCds[]; //added by Amit B
     public String selectedSalesOrgCds[]; //added by Amit B
     public Map salesOrgCdDesc; //added by Amit B
    
     public GapAnalysisMainWc()
    {
       
    }
    public GapAnalysisMainWc(Map r)
    {
        this.Result=r;
    }
    public String getJsp()
    {
       if(layout==LAYOUT_XLS)
       {
        return AppConst.JSP_LOC+"/components/gapAnalysisMainXls.jsp";
       }
       else
       {
        return AppConst.JSP_LOC+"/components/gapAnalysisMain.jsp";
       }
        
    }
    public void setResult(Map r)
    {
        this.Result=r;
    }
    public Map getResult()
    {
        return this.Result;
    }
    public void setProductDesc(String[] productDesc)
    {
        this.productDesc=productDesc;
    }
    public String[] getProductDesc()
    {
        return this.productDesc;
    }
    
    
    // start added by Amit B
        
     public void setSalesOrgCdDesc(Map salesOrgCdDesc)
    {
        this.salesOrgCdDesc=salesOrgCdDesc;
    }
    public Map getSalesOrgCdDesc()
    {
        return this.salesOrgCdDesc;
    }
    
    
     public void setSelectedProdDesc(String[] selectedProdDesc)
    {
        this.selectedProdDesc=selectedProdDesc;
    }
    public String[] getSelectedProdDesc()
    {
        return this.selectedProdDesc;
    }
    
    
    
     public void setSelectedRoleCds(String[] selectedRoleCds)
    {
        this.selectedRoleCds=selectedRoleCds;
    }
    public String[] getSelectedRoleCds()
    {
        return this.selectedRoleCds;
    }
    
    
      public void setSalesOrgCds(String[] salesOrgCds)
    {
        this.salesOrgCds=salesOrgCds;
    }
    public String[] getSalesOrgCds()
    {
        return this.salesOrgCds;
    }
    
    
     public void setRoleCds(String[] roleCds)
    {
        this.roleCds=roleCds;
    }
    public String[] getRoleCds()
    {
        return this.roleCds;
    }
    
     public void setSelectedSalesOrgCds(String[] selectedSalesOrgCds)
    {
        this.selectedSalesOrgCds=selectedSalesOrgCds;
    }
    public String[] getSelectedSalesOrgCds()
    {
        return this.selectedSalesOrgCds;
    }
    
    
    //End addn by Amit B
    
    public void setdeploymentPackageId(String[] deploymentPackageId)
    {
        this.deploymentPackageId=deploymentPackageId;
    }
    public String[] getdeploymentPackageId()
    {
        return this.deploymentPackageId;
    }
    
    public void setLayout(int layout)
    {
        this.layout=layout;
    }
    public int getLayout()
    {
        return this.layout;
    }
    
     public void setDuration(String duration)
    {
        this.duration=duration;
    }
    public String getDuration()
    {
        return this.duration;
    }
    public void setDeploymentId(String deploymentId)
    {
        this.deploymentId=deploymentId;
    }
    public String getDeploymentId()
    {
        return this.deploymentId;
    }
    
    public void setErrorMsg(String error)
    {
        this.errorMsg=error;   
    }
    public String getErrorMsg()
    {
        return this.errorMsg;
    }

    public void setupChildren()
    {
    }
} 
