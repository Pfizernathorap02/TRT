package com.tgix.printing; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author amit
 * The Bean will have all the information that we need to substitute in the Invitation Letter.
 *
 */

public class InvitationLetterBean 
{     
    private String ordernumber;
    private String firstName;
	private String lastName;
	private String materialDesc;
	private String shipAdd1;
	private String shipAdd2;
	private String shipCity;
	private String shipCountry;
    private String materials;
	private String shipState;
    private String shipZip;
    private Date   orderDate;
    private String products;
    private String team;    
    private String  templateBody;
    private String type;
    private Date startdate;
    // Changes made for RBU Shipment by Jeevan
    private String emplId;
    private VelocityConvertor velocityConvertor=new VelocityConvertor();
    private String serverName;
    
     public String getServerName() {
    	return serverName;
    }
    
    public void setServerName(String serverName) {
	this.serverName = serverName;
    }
    
     public String getEmplId() {
    	return emplId;
    }
    
    public void setEmplId(String emplid) {
	this.emplId = emplid;
    }
    
    public Date getStartDate() {
    	return startdate;
    }
    public void setStartDate(Date param) {
	this.startdate = param;
    }

    public Date getOrderDate() {
    	return orderDate;
    }
    public void setOrderDate(Date pOrderDate) {
	this.orderDate = pOrderDate;
    }

    public String  getTeam() {
    	return team;
    }
    
    public String getType() {
    	return type;
    }
    
    public void setType(String type) {
	this.type = type;
    }
    
    
    public void setTeam(String pTeam) {
	this.team = pTeam;
    }


    public String getOrderNumber() {
    	return ordernumber;
    }
    
    public void setOrderNumber(String pOrderNumber) {
	this.ordernumber = pOrderNumber;
    }
        
	public String getShipZip() {
		return shipZip;
	}

	public void setShipZip(String pShipZip) {
		this.shipZip = pShipZip;
	}    

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMaterialDesc() {
		return materialDesc;
	}
	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}
	public String getShipAdd1() {
		return shipAdd1;
	}
	public void setShipAdd1(String shipAdd1) {
		this.shipAdd1 = shipAdd1;
	}
	public String getShipAdd2() {
		return shipAdd2;
	}
	public void setShipAdd2(String shipAdd2) {
		this.shipAdd2 = shipAdd2;
	}
	public String getShipCity() {
		return shipCity;
	}
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}
	public String getShipCountry() {
		return shipCountry;
	}
	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
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
       	
    public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}
	
	
    public void prepareTemplateBody(){
       InputStream is = null;
       String templateBody="";
       try{
        // Hard code the file name
        // Changes made for RBU Shipment
         String sTemplateFileName = "";
         /*String sTrainingType = getType();
         sTrainingType = "SHU";
         if(sTrainingType == null){
            // There is no training type currently in RBU
            //throw new Exception("Invalid Training Type");         
         }*/
        
        String bu = "";
        bu = PrintHandlers.getBUForEmployee(getEmplId());
        if(bu.equals("Primary Care")){
            sTemplateFileName = "ShipmentLetter_PrimaryCare_Template";
        }
        else if(bu.equals("Specialty")){
            sTemplateFileName = "ShipmentLetter_Specialty_Template";
        }
         // Get the BU type from Furute Alignment to det the type of letter template
         String sTemplateFolder = velocityConvertor.getTemplateFolder(PrintingConstants.env_type);
        
        LoggerHelper.logSystemDebug("Path of the template folder >>>>>>>>>>> "+ sTemplateFolder);          
        /* if(sTrainingType.equalsIgnoreCase("P"))
         {
            sTemplateFileName = PrintingConstants.templateFileNamePromotion;                    
         }else{
         if(sTrainingType.equalsIgnoreCase("L"))
         {
            sTemplateFileName = PrintingConstants.templateFileNameLateral;            
         }else{
         if(sTrainingType.equalsIgnoreCase("D"))
         {
            sTemplateFileName = PrintingConstants.templateFileNameDM;
         }else{
         if(sTrainingType.equalsIgnoreCase("A"))
         {                        
            sTemplateFileName = PrintingConstants.templateFileNameAircept;
         }else{
         if(sTrainingType.equalsIgnoreCase("SP"))
         {                        
            sTemplateFileName = PrintingConstants.templateFileNameSteerePromotion;
         }else{
         if(sTrainingType.equalsIgnoreCase("SL"))
         {                        
            sTemplateFileName = PrintingConstants.templateFileNameSteereLateral;            
         }else{            
         if(sTrainingType.equalsIgnoreCase("SHA"))
         {                        
            sTemplateFileName = PrintingConstants.templateFileNameSteereAltLateral;
         }else{                
         if(sTrainingType.equalsIgnoreCase("SHU"))
         {                        
            sTemplateFileName = PrintingConstants.templateFileNameSteereUpjohnLateral;
         }else{                                                
         if(sTrainingType.equalsIgnoreCase("H"))
         {                         
            sTemplateFileName = PrintingConstants.templateFileNamePowerPHRLateral;             
         }else{                                                
           throw new Exception("Training Type Flag Not Set for printing template");         
         }}}}}}}}}
         */
                                  
          
          //is = getClass().getResourceAsStream(sTemplateFolder + File.separator + sTemplateFileName +".html");
          String sTemplatePath = sTemplateFolder + File.separator + sTemplateFileName +".html";
          LoggerHelper.logSystemDebug("Path of the sTemplatePath >>>>>>>>>>>>"+ sTemplatePath);
          String serverName = this.serverName;
          LoggerHelper.logSystemDebug("Server Name >>>>>>>>>>>>"+ serverName);
          // Apply this only if the its on pfizer server
          //if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
          // if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
          if(serverName != null && serverName.indexOf("trt-tst.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_INT  + sTemplatePath;
            }
       // else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
//         else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt-stg.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_STG  + sTemplatePath;
            }
             //else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
//             else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_PROD  + sTemplatePath;
            }
          
         //String sTemplatePath = "/TrainingReports/WebContent/PrintHome/Templates" + File.separator + sTemplateFileName +".html";
          LoggerHelper.logSystemDebug("Path of the sTemplatePath after applying server name>>>>>>>>>>>>"+ sTemplatePath);
          URL url = new URL(sTemplatePath);
          URLConnection urlConnection = url.openConnection();
          HttpURLConnection connection = (HttpURLConnection)urlConnection;;
          BufferedReader  bf  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          
          //BufferedReader  bf  = new BufferedReader(new FileReader(sTemplatePath));
                   
          StringBuffer sbr =new StringBuffer();
          StringWriter mailMessage = new StringWriter();
          
          String strSM="";
          try
          {
            while((strSM = bf.readLine())!=null)
            {
              sbr.append(strSM+" ");
            }
            bf.close();            
            this.templateBody=sbr.toString();                    
          }catch(Exception e){
          LoggerHelper.logSystemError("Class:InvitationLetterBean Method:prepareTemplateBody--Couldn't locate the Invitation Template in prepareTemplateBody ",e);
          }
          //BufferedReader   bf = new BufferedReader(new InputStreamReader(is));
          
          
          
          }catch(Exception e) {LoggerHelper.logSystemError("Class:InvitationLetterBean Method:prepareTemplateBody--Error Reading Email temaplte Body ",e);}
          
          }

    public String getTemplateBody(){
        return this.templateBody;
    }
} 
