package com.tgix.printing; 

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class EmailInfoBean 
{ 
    private String emplID;
    private String tnCode;
    private List allTNCodes=new ArrayList();
    private List allProducts=new ArrayList();    
    private String trmOrderNumber;
    private String clusterCD;
    private String products;
    private String materials;
    private String firstName;
    private String lastName;
    private String shipadd1;
    private String shipadd2;
    private String ShipCity;
    private String shipstate;
    private String shipzip;
    private String areaCode;
    private String homePhone;
	private String roleCD;
	private VelocityConvertor velocityConvertor=new VelocityConvertor();
	public void setShipadd1(String shipadd1) {
		this.shipadd1 = shipadd1;
	}

	public void setShipadd2(String shipadd2) {
		this.shipadd2 = shipadd2;
	}

	private String countryCode;
    private String groupID;
    private Date orderDate;
    private String ordernumber;
    private String team;
    private String type;
    private Date  startdate;
    private Date enddate;
    private String meetingnumber;
    private String emailid;
     private String serverName;
   private String classId;  
  
    
     
   public String getClassId() {
    	return classId;
    }
    
    public void setClassId(String classId) {
	this.classId = classId;
    }
    
    
     public String getServerName() {
    	return serverName;
    }
    
    public void setServerName(String serverName) {
	this.serverName = serverName;
    }

    public String getEmailID() {
		return emailid;
	}
	public void setEmailID(String param) {
		this.emailid = param;
	}

    public String getMeetingNumber() {
		return meetingnumber;
	}
	public void setMeetingNumber(String param) {
		this.meetingnumber = param;
	}

    public String getTeam() {
		return team;
	}
	public void setTeam(String param) {
		this.team = param;
	}

    public Date getStartDate() {
		return startdate;
	}
	
    public void setStartDate(Date param) {
		this.startdate = param;
	}

    public Date getEndDate() {
		return enddate;
	}
	
    public void setEndDate(Date param) {
		this.enddate = param;
	}

    public String getOrderNumber() {
		return ordernumber;
	}
    
	public void setOrderNumber(String OrderNumber) {
		this.ordernumber = OrderNumber;
	}

    public String getType() {
		return type;
	}
	public void setType(String Type) {
		this.type = type;
	}
        
    public String getShipAdd1() {
		return shipadd1;
	}
	public void getShipAdd1(String shipadd1) {
		this.shipadd1 = shipadd1;
	}

    public String getShipAdd2() {
		return shipadd2;
	}
	public void getShipAdd2(String shipadd2) {
		this.shipadd2 = shipadd2;
    }

	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getShipCity() {
		return ShipCity;
	}
	public void setShipCity(String ShipCity) {
		this.ShipCity = ShipCity;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getShipState() {
		return shipstate;
	}
	public void setShipState(String shipstate) {
		this.shipstate = shipstate;
	}
	public String getShipZip() {
		return shipzip;
	}
	public void setShipZip(String shipzip) {
		this.shipzip = shipzip;
	}
       
    public String getEmplID() {
        return emplID;
    }
    public void setEmplID(String emplID) {
        this.emplID = emplID;
    }
    
    public String getTnCode() {
        return tnCode;
    }
    public void setTnCode(String tnCode) {
        this.tnCode = tnCode;
    }
    
    public String getClusterCD() {
	return clusterCD;
	}
	public void setClusterCD(String clusterCD) {
	this.clusterCD = clusterCD;
	}
        
    public String getProducts() {
			return products;
    }
	public void setProducts(String products) {
			this.products = products;
	}

    public String getMaterials() {
			return materials;
    }
	public void setMaterials(String materials) {
			this.materials = materials;
	}
  
      public List getAllProducts(){
        return allProducts;
    }
        
    public void addProducts(String products){
        this.allProducts.add(products);
    }   
  
      public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date sOrderDate) {
        this.orderDate = sOrderDate;
    }
    
    public String getEmailTemplate()
    {
      InputStream is = null;  
      BufferedReader  bf = null;      
      
      String sTemplateFolder = velocityConvertor.getTemplateFolder(PrintingConstants.env_type);
      
      String sContent = "", sTemplateFile = "";
      // Changes made for RBU Sales Training by Jeevan
      // There will be  template based on the BU of the employee 
      sTemplateFile = "";
      
       String bu = "";
        bu = PrintHandlers.getBUForEmployee(getEmplID());
        if(bu.equals("Primary Care")){
            sTemplateFile = "EmailInvitation_PrimaryCare_Template";
        }
        else if(bu.equals("Specialty")){
            sTemplateFile = "EmailInvitation_Specialty_Template";
        }
      
      /*if(type!= null && type.equalsIgnoreCase("P"))
         sTemplateFile = PrintingConstants.emailtemplateFileNamePromotion;
      else
      if(type!= null && type.equalsIgnoreCase("L"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameLateral;
      else
      if(type!= null && type.equalsIgnoreCase("D"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameDM;
      else
      if(type!= null && type.equalsIgnoreCase("A"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameAircept;
      else
      if(type!= null && type.equalsIgnoreCase("SL"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameSteereLateral;
      else
      if(type!= null && type.equalsIgnoreCase("SP"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameSteerePromotion;
      else
      if(type!= null && type.equalsIgnoreCase("SHA"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameSteereAltaLateral;
      else
      if(type!= null && type.equalsIgnoreCase("SHU"))
        sTemplateFile = PrintingConstants.emailtemplateFileNameSteereUpjohnLateral;
      else      
      if(type!= null && type.equalsIgnoreCase("H"))
        sTemplateFile = PrintingConstants.emailtemplateFileNamePowerPHR;        
      else      
      if(type!= null && type.equalsIgnoreCase("GEOD"))
        sTemplateFile = PrintingConstants.emailtemplateFileNamePDFGeodon;
      else            
        return null;
        */
      
      String sTemplatePath = sTemplateFolder + File.separator + sTemplateFile +".html";
      LoggerHelper.logSystemDebug("Path of the sTemplatePath in EmailInfoBean>>>>>>>>>>>>"+ sTemplatePath);
      String serverName = this.serverName;
      // Apply this only if the its on pfizer server
     
     /*   if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_INT  + sTemplatePath;
        }
        else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_STG  + sTemplatePath;
        }
        else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_PROD  + sTemplatePath;
        }*/
      if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_INT  + sTemplatePath;
            }
       // else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
         else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_STG  + sTemplatePath;
            }
             //else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
             else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_PROD  + sTemplatePath;
            }
      LoggerHelper.logSystemDebug("Path of the sTemplatePath after applying server name in Email info>>>>>>>>>>>>"+ sTemplatePath);
      System.out.println("Path of the sTemplatePath after applying server name in Email info>>>>>>>>>>>>"+ sTemplatePath);
      String templateBody="";
      try
      {
         bf  = new BufferedReader(new FileReader(sTemplatePath));        
       }
       catch(Exception e)
       {
         LoggerHelper.logSystemError("Class:EmailInfoBean Method:prepareTemplateBody--Couldn't locate the Email Template in prepareTemplateBody ",e);
         return "";
       }
       
       
       StringBuffer sbr =new StringBuffer();
       StringWriter mailMessage = new StringWriter();
          
       String strSM="";
       try
       {
          while((strSM = bf.readLine())!=null){
          sbr.append(strSM+" ");
       }
       bf.close();  
       }
       catch(Exception e) 
       {
          LoggerHelper.logSystemError("Class:EmailInfoBean Method:prepareTemplateBody--Error Reading Email temaplte Body ",e);
          return null;
       }
                 
       sContent =sbr.toString();
          
       if(sContent == null)
         return "";                            
       
       return sContent ;
    }  
} 
