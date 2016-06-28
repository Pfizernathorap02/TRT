package com.pfizer.db; 

import java.util.Date;

public class GapAnalysisEntry 
{ 
   
    
    private String empID="";
    private String firstName="";
    private String lastName="";
    private String emailAddr="";
    private String rolecd="";
    private String salesOrg="";
    private String mngrFirstName="";
    private String mngrLastName="";
    private String mngrEmail="";
    private String productName="";
   private String status="";
   // private Date statusDate;
   
   //added by Amit B for Gap report enhancement
   
   private String guID="";
   private Date statusDate=null;
    public String salesOrgCd;

    
    public void setGuID(String guID)
    {
        this.guID=guID;
    }
    public String getGuID()
    {
        return this.guID;
    }
    
    public void setStatusDate(Date statusDate)
    {
        this.statusDate=statusDate;
    }
    public Date getstatusDate()
    {
        return this.statusDate;
    }
    
    
    public void setSalesOrgCd(String salesOrgCd)
    {
        this.salesOrgCd=salesOrgCd;
    }
    public String getSalesOrgCd()
    {
        return this.salesOrgCd;
    }
    
    // End of addition by Amit B.
    
    
    public void setEmpID(String empID)
    {
        this.empID=empID;
    }
    public String getEmpID()
    {
        return this.empID;
    }
    public void setFirstName(String fName)
    {
        this.firstName=fName;
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public void setLastName(String lName)
    {
        this.lastName=lName;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public void setEmailAddr(String emailAddr)
    {
        this.emailAddr=emailAddr;
    }
    public String getEmailAddr()
    {
        return this.emailAddr;
    }
    public void setRolecd(String rolecd)
    {
        this.rolecd=rolecd;
    }
    public String getRolecd()
    {
        return this.rolecd;
    }
    public void setSalesOrg(String salesOrg)
    {
     this.salesOrg=salesOrg;   
    }
    public String getSalesOrg()
    {
        return this.salesOrg;
    }
    public void setMngrFirstName(String mngrFirstName)
    {
        this.mngrFirstName=mngrFirstName;
    }
    public String getMngrFirstName()
    {
        return this.mngrFirstName;
    }
    public void setMngrLastName(String mngrLastName)
    {
        this.mngrLastName=mngrLastName;
    }
    public String getMngrLastName()
    {
        return this.mngrLastName;
    }
    public void setMngrEmail(String mngrEmail)
    {
        this.mngrEmail=mngrEmail;
    }
    public String getMngrEmail()
    {
        return this.mngrEmail;
    }
    public void setProductName(String productName)
    {
        this.productName=productName;
    }
    public String getProductName()
    {
        return this.productName;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getStatus()
    {
        return this.status;
    }
      
    public String toString()
    {
     StringBuffer sb=new StringBuffer();
        sb.append("\n"+this.getClass().getName()+"\n");
        sb.append( "     empID:"+empID+"\n");
        sb.append( "     guID:"+guID+"\n");
        sb.append( "    firstName: " + firstName + "\n" );
		sb.append( "     lastName: " + lastName + "\n" );
		sb.append( "     emailAddr: " + emailAddr + "\n" );
		sb.append( "     rolecd: " + rolecd + "\n" );
		sb.append( "     salesOrg: " + salesOrg + "\n" );
		sb.append( "     Managers firstName: " + mngrFirstName + "\n" );
		sb.append( "     Managers lastName: " + mngrLastName + "\n" );
		sb.append( "     Managers email: " + mngrEmail + "\n" );
		sb.append( "     product: " + productName + "\n" );
		sb.append( "     status: " + status + "\n" );
		sb.append( "     statusDate: " + statusDate + "\n" );

     return sb.toString(); 
    }
} 
