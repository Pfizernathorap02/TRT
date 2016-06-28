package com.tgix.printing; 


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jboss.system.server.ServerConfig;

import com.tgix.Utils.PrintUtils;
//import weblogic.management.internal.BootStrap;
public class VelocityConvertor implements ServerConfig
{ 
	 
    private static final DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
  
    
    
 

	/**
     * This Method will Create Invitation Letter for the Employee in the Format:INV022963.html 
     * where INV is a Constant followed by EMPLID
     */
    public static void createLabel(String txt,String employeeID, String serverName){
            try {
                String sLabelFolder = VelocityConvertor.getLabelFolder(PrintingConstants.env_type);
                /*if(serverName != null && serverName.indexOf("pfizer.com") != -1){
                  //  sLabelFolder =  "http://" + serverName + sLabelFolder;
                  sLabelFolder = PrintingConstants.APPLICATION_PATH  + sLabelFolder;
                }*/
            /*    if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_INT  + sLabelFolder;
                }
                else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_STG  + sLabelFolder;
                }
                else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_PROD  + sLabelFolder;
                } */
           // if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
                 if(serverName != null && serverName.indexOf("trt-tst.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_INT  + sLabelFolder;
            }
       // else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
//            else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
                 else if(serverName != null && serverName.indexOf("trt-stg.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_STG  + sLabelFolder;
            }
             else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
             //else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_PROD  + sLabelFolder;
            }
            
               // DebugOutput(sLabelFolder);
                //String sLabelFolder = "TrainingReports" + File.separator +
                  //             "PrintHome" + File.separator + "Templates" + File.separator + "CreatedLabels";
                File labelFile;                
                labelFile=new File(sLabelFolder + File.separator + PrintingConstants.INVITATION_OUTPUT_FILE + employeeID.trim()+".html");
                LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel Success in Creating Invitation HTML "+labelFile.toURL());
                String path=labelFile.getPath();
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                HttpURLConnection connection = (HttpURLConnection)urlConnection;
                String sName=System.getProperty(SERVER_NAME);
                Socket socket = new Socket(sName, 17200);  
                //BufferedWriter bf1 = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                BufferedWriter bf1 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//                BufferedWriter bf1 = new BufferedWriter(new FileWriter(labelFile));                    
                bf1.write(txt);
                bf1.flush();
                bf1.close();
               }catch (Exception e){
                    LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel-- EXCEPTION in Creating Invitation HTML "+e);
                    e.printStackTrace();
                }
    }
    
    public static void createLabelForAgenda(String txt,String employeeID, String serverName){
            try {
                String sLabelFolder = VelocityConvertor.getLabelFolder(PrintingConstants.env_type);
                
                
                /*if(serverName != null && serverName.indexOf("pfizer.com") != -1){
                  //  sLabelFolder =  "http://" + serverName + sLabelFolder;
                  sLabelFolder = PrintingConstants.APPLICATION_PATH  + sLabelFolder;
                }*/
            /*    if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_INT  + sLabelFolder;
                }
                else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_STG  + sLabelFolder;
                }
                else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
                    sLabelFolder = PrintingConstants.APPLICATION_PATH_PROD  + sLabelFolder;
                } */
           // if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
//                 if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
                if(serverName != null && serverName.indexOf("trt-tst.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_INT  + sLabelFolder;
            }
       // else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
            //else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
                else if(serverName != null && serverName.indexOf("trt-stg.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_STG  + sLabelFolder;
            }
            else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
//             else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
            sLabelFolder = PrintingConstants.APPLICATION_PATH_PROD  + sLabelFolder;
            }
            
            System.out.println("Label folder >>>>>>>>>>>" + sLabelFolder);
               // DebugOutput(sLabelFolder);
                //String sLabelFolder = "TrainingReports" + File.separator +
                  //             "PrintHome" + File.separator + "Templates" + File.separator + "CreatedLabels";
                File labelFile;                
                labelFile=new File(sLabelFolder + File.separator + PersonalizedAgendaConstants.PERSONALIZED_AGENDA + employeeID.trim()+".html");
                LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel Success in Creating Invitation HTML "+labelFile.toURL());
                String path=labelFile.getPath();
                URL url = new URL(path);
                String sName=System.getProperty(SERVER_NAME);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                HttpURLConnection connection = (HttpURLConnection)urlConnection;
               
                /*String sName=System.getProperty(SERVER_NAME);
                System.setProperty("http.proxyHost", sName);
                System.setProperty("http.proxyPort", "17200");
                */
                Socket socket = new Socket(sName, 17200);  
                //BufferedWriter bf1 = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                BufferedWriter bf1 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                BufferedWriter bf1 = new BufferedWriter(new FileWriter(labelFile));                    
                bf1.write(txt);
                bf1.flush();
                bf1.close();
               }catch (Exception e){
                    LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel-- EXCEPTION in Creating Invitation HTML "+e);
                    e.printStackTrace();
                }
    }
    
    public static void DebugOutput(String sOutput)
    {
        try
        {          
          SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");             
          Date now = new Date();
          String sFinalOutput  = format.format(now);          
          
          BufferedWriter bf = new BufferedWriter(new FileWriter(new File("c:\\output.txt")));
          bf.write(sFinalOutput + '\n');
          bf.flush();
          bf.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static String getTemplateFolder(String sEnv)
    {
    	 String sDeploymentRoot = getDeploymentRoot(sEnv);
    	 System.out.println("Deployment Root : " + sDeploymentRoot);
         String sTemplateRoot = sDeploymentRoot + File.separator + "TrainingReports" + File.separator + 
                                "PrintHome" + File.separator + "Templates" ;
        // String sTemplateRoot = "/TrainingReports/PrintHome/Templates/CreatedLabels";
         return sTemplateRoot;    
    }
    
    public static String getLabelFolder(String sEnv)
    {
        String sDeploymentRoot = getDeploymentRoot(sEnv);
        String sTemplateRoot = sDeploymentRoot + File.separator + "TrainingReports" + File.separator + 
                               "PrintHome" + File.separator + "Templates" + File.separator + "CreatedLabels";
       // String sTemplateRoot = "/TrainingReports/PrintHome/Templates/CreatedLabels";
        return sTemplateRoot;                
    }
    
   /* public static String getDeploymentRoot(String sEnv)
    {
       String sAppRoot="";        
       
       try
       {
         String sServerDir = "";
         String sDevelopmentSubServerFolder = ".wlnotdelete";
         String sNonDevelopmentSubServerFolder = "stage";
         // Changes made for RBU Shipment
         String sAppName = "TrainingReports";
         TomcatService ts= new TomcatService();
         //File file =null;// BootStrap.getConfigFile();
         String file = ts.getConfigFile();
        // File file=new File(path);
         if(file != null) 
         {
            
           String sConfigFilePath = file;					//file.getCanonicalPath();
           System.out.println("Reative path >>> " +  sConfigFilePath + "Absolute path " + file);
           //String sServerName =null;// BootStrap.getServerName();
           String sServerName = System.getProperty("jboss.server.name");  
           String sConfileDir = sConfigFilePath.substring(0,sConfigFilePath.lastIndexOf("\\"));
           sServerDir = sConfileDir + File.separator + sServerName;
          LoggerHelper.logSystemDebug("Path of the sServerDir >>>>>>>>>>>>>>>>>>>>>> "+ sServerDir + "sServerName >>>> " + sServerName);          
           if(sEnv.equalsIgnoreCase(PrintingConstants.env_local))
           {
              sAppRoot = sServerDir + File.separator + sDevelopmentSubServerFolder + File.separator + sAppName;              
           }else{
              sAppRoot = sServerDir + File.separator + sNonDevelopmentSubServerFolder + File.separator + sAppName;
           }           
         }
        LoggerHelper.logSystemDebug("Path of the sAppRoot >>>>>>>>>>>>>>>>>>>>> "+ sAppRoot);     
       }
       catch(Exception e)
       {
         e.printStackTrace();
       }
         
       return sAppRoot;
    }
    

   */ 

    public static String getDeploymentRoot(String sEnv)
    {
       String sAppRoot="";        
       
       try
       {    	   
         String sServerDir = "";
         String sDevelopmentSubServerFolder = ".wlnotdelete";
         String sNonDevelopmentSubServerFolder = "stage";
         // Changes made for RBU Shipment
         String sAppName = "TrainingReportTool";
         
         File file = new File(SERVER_CONFIG_URL);
         if(file != null) 
         {
            
           String sConfigFilePath = file.getCanonicalPath();
           System.out.println("Reative path >>> " +  sConfigFilePath + "Absolute path " + file.getAbsolutePath());
           String serName=System.getProperty(SERVER_NAME);
           String sServerName = "http://"+System.getProperty(SERVER_NAME)+":17200";
           if(serName.equalsIgnoreCase("amrndhl275")){
        	   sServerName="http://trt-dev.pfizer.com";
           }
           else  if(serName.equalsIgnoreCase("amrndhl279")||serName.equalsIgnoreCase("amrndhl280")){
        	   sServerName="http://trt-tst.pfizer.com";
           }
           else if(serName.equalsIgnoreCase("amrndhl285")||serName.equalsIgnoreCase("amrndhl286")){
        	   sServerName="http://trt-stg.pfizer.com";
           }
           else if(serName.equalsIgnoreCase("amrndhl289")||serName.equalsIgnoreCase("amrndhl290")){
        	   sServerName="http://trt.pfizer.com";
           }
           
           System.out.println("server name is "+ sServerName);
          // String sConfileDir = sConfigFilePath.substring(0,sConfigFilePath.lastIndexOf("\\"));
           String sConfileDir = sConfigFilePath.substring(0,sConfigFilePath.lastIndexOf("/"));
           System.out.println("s ConfileDir "+ sConfileDir);
           //sServerDir = sConfileDir + File.separator + sServerName;
           sServerDir = System.getProperty(SERVER_HOME_DIR);
           System.out.println("server Server Directory is "+ sServerDir);
          LoggerHelper.logSystemDebug("Path of the sServerDir >>>>>>>>>>>>>>>>>>>>>> "+ sServerDir + "sServerName >>>> " + sServerName);          
           if(sEnv.equalsIgnoreCase(PrintingConstants.env_local))
           {
              sAppRoot = sServerDir + File.separator + sDevelopmentSubServerFolder + File.separator + sAppName;              
           }else{
              sAppRoot = sServerDir + File.separator + sNonDevelopmentSubServerFolder + File.separator + sAppName;
           }  
           sAppRoot=sServerName;
         }
        LoggerHelper.logSystemDebug("Path of the sAppRoot >>>>>>>>>>>>>>>>>>>>> "+ sAppRoot);     
        System.out.println("DeploymentRoot Path is "+sAppRoot);
        
       
       }
       catch(Exception e)
       {
         e.printStackTrace();
       }
         
       return sAppRoot;
    }
 
    

    
    /**
     * This Method will Create Invitation Letter for the Employee in the Format:INV022963.html 
     * where INV is a Constant followed by EMPLID
     */
    /*public static void createLabel(String txt,String employeeID){
            try {
                File file = BootStrap.getConfigFile();
                String path = file.getCanonicalPath();
                path = path.substring(0,path.lastIndexOf("\\"));
                File labelFile;
               //File labelFile=new File(path+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+employeeID.trim()+".html");
               if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_staging)){
                labelFile=new File(PrintingConstants.path_for_staging+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+employeeID.trim()+".html");
               }else if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_prod)){
                labelFile=new File(PrintingConstants.path_for_prod+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+employeeID.trim()+".html");
               }
               else{
                labelFile=new File(PrintingConstants.path_for_local+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+employeeID.trim()+".html");
               }
                LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel Success in Creating Invitation HTML "+labelFile.toURL());
                BufferedWriter bf1 = new BufferedWriter(new FileWriter(labelFile));                    
                bf1.write(txt);
                bf1.flush();
                bf1.close();
               }catch (Exception e){
                    LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:createLabel-- EXCEPTION in Creating Invitation HTML "+e);
                    e.printStackTrace();
                }
    }*/
    
   
   /**
    * This method returns the URL of the File.
    * Here we pass the EMPLID and it looks for the URL 
    */ 
   
  /* public static URL getURLoftheFile(String emplid) {
    String path="";
    URL fileURL=null;
    try{
        //File file = null;//BootStrap.getConfigFile();
    	TomcatService ts= new TomcatService();
        String file = ts.getConfigFile();
        //path = file.getCanonicalPath();
        path=file;
        path = path.substring(0,path.lastIndexOf("\\"));
        File labelFile=new File(path+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+emplid.trim()+".html");
        LoggerHelper.logSystemDebug("URL For the File"+labelFile.toURL());
        fileURL=labelFile.toURL();
        }catch(Exception e){
        LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:getURLoftheFile-- Error in the Path"+e);
    }
         return fileURL;
   }
       */
    
    
	public static URI getURLoftheFile(String emplid) {
        String path="";
        URI fileURL=null;
        try{
            File file = new File(SERVER_CONFIG_URL);	//BootStrap.getConfigFile();
            path = file.getCanonicalPath();
            path = path.substring(0,path.lastIndexOf("\\"));
            File labelFile=new File(path+"\\"+PrintingConstants.Printing_Label_Directory+"\\"+PrintingConstants.INVITATION_OUTPUT_FILE+emplid.trim()+".html");
            LoggerHelper.logSystemDebug("URL For the File"+labelFile.toURI());
            fileURL=labelFile.toURI();
            }catch(Exception e){
            LoggerHelper.logSystemDebug("Class:Velocity Convertor Method:getURLoftheFile-- Error in the Path"+e);
        }
             return fileURL;
       }
     
    
    
    
    /**
    * This method is responsible for Generating Invitations amd Substituting Values in the Invitation Template.
    */
   public static void generateInvitations(InvitationLetterBean invLetterBean){
    String  msg="";
    StringWriter mailMessage = new StringWriter();
    String logTag="";
    List templList = new ArrayList();
    List materialList = new ArrayList();
    String ship2;
    try{
        if(invLetterBean!=null){
        invLetterBean.prepareTemplateBody(); 
       /* first, we init the runtime engine.  Defaults are fine. */
        Velocity.init();
        /* lets make a Context and put data into it */
        VelocityContext context = new VelocityContext();
        /*Now We will Substitute the Values here on the Context */
        
        Date dateStart = invLetterBean.getStartDate();
        if(dateStart != null)
        {
          Date dateDayBeforeStart = PrintUtils.addDays(dateStart, -1);
          String startDate = PrintUtils.getPedCloseDateFormatted(dateDayBeforeStart);
          context.put(PrintingConstants.INV_STARTDATE, startDate);
        }
        
        //Temporary              
        context.put("type",PrintUtils.ifNullNBSP(invLetterBean.getType()));                      
                      
        context.put(PrintingConstants.INV_PREF_NAME,PrintUtils.ifNullNBSP(invLetterBean.getFirstName()));
        context.put(PrintingConstants.INV_FIRST_NAME,PrintUtils.ifNullNBSP(invLetterBean.getFirstName()));
        context.put(PrintingConstants.INV_LAST_NAME,PrintUtils.ifNullNBSP(invLetterBean.getLastName()));
        context.put(PrintingConstants.INV_ORDER_NM,PrintUtils.ifNullNBSP(invLetterBean.getOrderNumber()));        
        context.put(PrintingConstants.INV_DATE,(new SimpleDateFormat("MMMM d, yyyy")).format(new Date(System.currentTimeMillis())));
        context.put(PrintingConstants.INV_SHIP_ADD1,PrintUtils.ifNullNBSP(invLetterBean.getShipAdd1()));
        context.put(PrintingConstants.ORDER_ID,PrintUtils.ifNullNBSP(invLetterBean.getOrderNumber()));
        //We have to Display Ship2 Address on NEXT Line ;
        if(invLetterBean.getShipAdd2()!=null && !invLetterBean.getShipAdd2().trim().equalsIgnoreCase("")){
           ship2="<br>"+invLetterBean.getShipAdd2() ;
        }else{ship2="&nbsp;";}
        context.put(PrintingConstants.INV_SHIP_ADD2,PrintUtils.ifNullNBSP(ship2));
        context.put(PrintingConstants.INV_SHIP_CITY,PrintUtils.ifNullNBSP(invLetterBean.getShipCity()));
        context.put(PrintingConstants.INV_SHIP_STATE,PrintUtils.ifNullNBSP(invLetterBean.getShipState()));
        context.put(PrintingConstants.INV_SHIP_ZIP,PrintUtils.ifNullNBSP(invLetterBean.getShipZip()));
        context.put(PrintingConstants.TEAM_DESC,PrintUtils.ifNullNBSP(invLetterBean.getTeam()));
          
        
        if(invLetterBean.getProducts()!=null)
        {                
          //Get Product Array from Comma Delimited Product    
          String sProducts = invLetterBean.getProducts();
          if(sProducts == null) sProducts = "";
          StringTokenizer st = new StringTokenizer(sProducts, ",");
          if(st != null)
          {          
            while(st.hasMoreTokens())
            {
              String sProduct = st.nextToken();
              sProduct = sProduct.trim();
              // Changes made for RBU Shipment 
              // GEt the class start date for this employee and product
              String result = "";
              result = PrintHandlers.getClassStartDate(invLetterBean.getEmplId(), sProduct);
              //sProduct = sProduct + " - " + classDate;
              sProduct = result;
              templList.add(sProduct);
            }
          }                
          //Get Product Array from Comma Delimited Product                    
          String sMaterials = invLetterBean.getMaterials();
          if(sMaterials == null) sMaterials = "";
          st = new StringTokenizer(sMaterials, ",");
          if(st != null)
          {            
            while(st.hasMoreTokens())
            {
              String sMaterial = st.nextToken();
              sMaterial = sMaterial.trim();
              materialList.add(sMaterial);
            }
          }

          Collections.sort(templList);
          context.put(PrintingConstants.INV_PRODUCTS,templList);
          context.put(PrintingConstants.INV_ALL_MATERIALS,materialList);        
        }
        
        if(Velocity.evaluate(context,mailMessage,logTag,invLetterBean.getTemplateBody()))  
        LoggerHelper.logSystemDebug("Class:VelocityConvertor  Method:generateInvitations-- Invitation Prepared for  Trainee *****---"+mailMessage);;
        /*After The Message has been Created We will Create Invitation Letters and Store it. */
        createLabel(mailMessage.toString(),invLetterBean.getOrderNumber(), invLetterBean.getServerName());
        }else{
        LoggerHelper.logSystemDebug("Class:VelocityConvertor Method:generateInvitations-- ,InvitationLetterBean is NULL");
        }
    }catch(Exception e){
        e.printStackTrace();
        LoggerHelper.logSystemDebug("Exception Class:VelocityConvertor Method:generateInvitations--"+e);
    }
   }
   
  public static void generatePersonalizedAgenda(PersonalizedAgendaBean personalizedAgendaBean){
    String  msg="";
    StringWriter mailMessage = new StringWriter();
    String logTag="";
    List mondayAMList = new ArrayList();
    List mondayPMList = new ArrayList();
    List tuesdayAMList = new ArrayList();
    List tuesdayPMList = new ArrayList();
    List wednesdayAMList = new ArrayList();
    List wednesdayPMList = new ArrayList();
    List thursdayAMList = new ArrayList();
    List thursdayPMList = new ArrayList();
    List fridayAMList = new ArrayList();
    List fridayPMList = new ArrayList();
    String currentYear = "2009";
    try{
        if(personalizedAgendaBean!=null){
        personalizedAgendaBean.prepareTemplateBody(); 
       /* first, we init the runtime engine.  Defaults are fine. */
        Velocity.init();
        /* lets make a Context and put data into it */
        VelocityContext context = new VelocityContext();
        /*Now We will Substitute the Values here on the Context */
        
        // Get the first name and last name
        context.put(PersonalizedAgendaConstants.FIRST_NAME,PrintUtils.ifNullNBSP(personalizedAgendaBean.getFirstName()));
        context.put(PersonalizedAgendaConstants.LAST_NAME,PrintUtils.ifNullNBSP(personalizedAgendaBean.getLastName()));
        context.put(PersonalizedAgendaConstants.EMPLI_ID,PrintUtils.ifNullNBSP(personalizedAgendaBean.getEmplId()));
        
        // Get the week start and end date              
        context.put(PersonalizedAgendaConstants.WEEK_START_DATE,PrintUtils.ifNullNBSP(personalizedAgendaBean.getWeekStartDate()));
        context.put(PersonalizedAgendaConstants.WEEK_END_DATE,PrintUtils.ifNullNBSP(personalizedAgendaBean.getWeekEndDate()));
        context.put(PersonalizedAgendaConstants.YEAR,currentYear);
        
        // Add the MOndayAM contents
        if(personalizedAgendaBean.getMondayAMProduct() != null && !personalizedAgendaBean.getMondayAMProduct().equals("")){
            mondayAMList.add(personalizedAgendaBean.getMondayAMProduct());
            mondayAMList.add(personalizedAgendaBean.getMondayAMStartTime()+ " - " + personalizedAgendaBean.getMondayAMEndTime());
            mondayAMList.add(personalizedAgendaBean.getMondayAMRoom());
            mondayAMList.add(personalizedAgendaBean.getMondayAMTable());
            mondayAMList.add(personalizedAgendaBean.getMondayAMTrainer());
            
        }
       // Add the MOndayPM contents
       if(personalizedAgendaBean.getMondayPMProduct() != null && !personalizedAgendaBean.getMondayPMProduct().equals("")){
            mondayPMList.add(personalizedAgendaBean.getMondayPMProduct());
            mondayPMList.add(personalizedAgendaBean.getMondayPMStartTime()+ " - " + personalizedAgendaBean.getMondayPMEndTime());
            mondayPMList.add(personalizedAgendaBean.getMondayPMRoom());
            mondayPMList.add(personalizedAgendaBean.getMondayPMTable());
            mondayPMList.add(personalizedAgendaBean.getMondayPMTrainer());
            
        }
          // Add the tuesdayAM contents
         if(personalizedAgendaBean.getTuesdayAMProduct() != null && !personalizedAgendaBean.getTuesdayAMProduct().equals("")){
             tuesdayAMList.add(personalizedAgendaBean.getTuesdayAMProduct());
             tuesdayAMList.add(personalizedAgendaBean.getTuesdayAMStartTime()+ " - " + personalizedAgendaBean.getTuesdayAMEndTime());
             tuesdayAMList.add(personalizedAgendaBean.getTuesdayAMRoom());
             tuesdayAMList.add(personalizedAgendaBean.getTuesdayAMTable());
             tuesdayAMList.add(personalizedAgendaBean.getTuesdayAMTrainer());
             
         }
        // Add the tuesdayPM contents
        if(personalizedAgendaBean.getTuesdayPMProduct() != null && !personalizedAgendaBean.getTuesdayPMProduct().equals("")){
             tuesdayPMList.add(personalizedAgendaBean.getTuesdayPMProduct());
             tuesdayPMList.add(personalizedAgendaBean.getTuesdayPMStartTime()+ " - " + personalizedAgendaBean.getTuesdayPMEndTime());
             tuesdayPMList.add(personalizedAgendaBean.getTuesdayPMRoom());
             tuesdayPMList.add(personalizedAgendaBean.getTuesdayPMTable());
             tuesdayPMList.add(personalizedAgendaBean.getTuesdayPMTrainer());
             
        }
        
        // Add the wednesdayAM contents
         if(personalizedAgendaBean.getWednesdayAMProduct() != null && !personalizedAgendaBean.getWednesdayAMProduct().equals("")){
             wednesdayAMList.add(personalizedAgendaBean.getWednesdayAMProduct());
             wednesdayAMList.add(personalizedAgendaBean.getWednesdayAMStartTime()+ " - " + personalizedAgendaBean.getWednesdayAMEndTime());
             wednesdayAMList.add(personalizedAgendaBean.getWednesdayAMRoom());
             wednesdayAMList.add(personalizedAgendaBean.getWednesdayAMTable());
             wednesdayAMList.add(personalizedAgendaBean.getWednesdayAMTrainer());
             
         }
        // Add the wednesdayPM contents
        if(personalizedAgendaBean.getWednesdayPMProduct() != null && !personalizedAgendaBean.getWednesdayPMProduct().equals("")){
             wednesdayPMList.add(personalizedAgendaBean.getWednesdayPMProduct());
             wednesdayPMList.add(personalizedAgendaBean.getWednesdayPMStartTime()+ " - " + personalizedAgendaBean.getWednesdayPMEndTime());
             wednesdayPMList.add(personalizedAgendaBean.getWednesdayPMRoom());
             wednesdayPMList.add(personalizedAgendaBean.getWednesdayPMTable());
             wednesdayPMList.add(personalizedAgendaBean.getWednesdayPMTrainer());
             
        }
        
          // Add the thursdayAM contents
         if(personalizedAgendaBean.getThursdayAMProduct() != null && !personalizedAgendaBean.getThursdayAMProduct().equals("")){
             thursdayAMList.add(personalizedAgendaBean.getThursdayAMProduct());
             thursdayAMList.add(personalizedAgendaBean.getThursdayAMStartTime()+ " - " + personalizedAgendaBean.getThursdayAMEndTime());
             thursdayAMList.add(personalizedAgendaBean.getThursdayAMRoom());
             thursdayAMList.add(personalizedAgendaBean.getThursdayAMTable());
             thursdayAMList.add(personalizedAgendaBean.getThursdayAMTrainer());
             
         }
        // Add the thursdayPM contents
        if(personalizedAgendaBean.getThursdayPMProduct() != null && !personalizedAgendaBean.getThursdayPMProduct().equals("")){
             thursdayPMList.add(personalizedAgendaBean.getThursdayPMProduct());
             thursdayPMList.add(personalizedAgendaBean.getThursdayPMStartTime()+ " - " + personalizedAgendaBean.getThursdayPMEndTime());
             thursdayPMList.add(personalizedAgendaBean.getThursdayPMRoom());
             thursdayPMList.add(personalizedAgendaBean.getThursdayPMTable());
             thursdayPMList.add(personalizedAgendaBean.getThursdayPMTrainer());
             
        }
        
          // Add the fridayAM contents
         if(personalizedAgendaBean.getFridayAMProduct() != null && !personalizedAgendaBean.getFridayAMProduct().equals("")){
             fridayAMList.add(personalizedAgendaBean.getFridayAMProduct());
             fridayAMList.add(personalizedAgendaBean.getFridayAMStartTime()+ " - " + personalizedAgendaBean.getFridayAMEndTime());
             fridayAMList.add(personalizedAgendaBean.getFridayAMRoom());
             fridayAMList.add(personalizedAgendaBean.getFridayAMTable());
             fridayAMList.add(personalizedAgendaBean.getFridayAMTrainer());
             
         }
        // Add the fridayPM contents
        if(personalizedAgendaBean.getFridayPMProduct() != null && !personalizedAgendaBean.getFridayPMProduct().equals("")){
             fridayPMList.add(personalizedAgendaBean.getFridayPMProduct());
             fridayPMList.add(personalizedAgendaBean.getFridayPMStartTime()+ " - " + personalizedAgendaBean.getFridayPMEndTime());
             fridayPMList.add(personalizedAgendaBean.getFridayPMRoom());
             fridayPMList.add(personalizedAgendaBean.getFridayPMTable());
             fridayPMList.add(personalizedAgendaBean.getFridayPMTrainer());
             
        }
        context.put(PersonalizedAgendaConstants.MONDAY_AM, mondayAMList);
        context.put(PersonalizedAgendaConstants.MONDAY_PM, mondayPMList);
        context.put(PersonalizedAgendaConstants.TUESDAY_AM, tuesdayAMList);
        context.put(PersonalizedAgendaConstants.TUESDAY_PM, tuesdayPMList);
        context.put(PersonalizedAgendaConstants.WEDNESDAY_AM, wednesdayAMList);
        context.put(PersonalizedAgendaConstants.WEDNESDAY_PM, wednesdayPMList);
         context.put(PersonalizedAgendaConstants.THURSDAY_AM, thursdayAMList);
        context.put(PersonalizedAgendaConstants.THURSDAY_PM, thursdayPMList);
         context.put(PersonalizedAgendaConstants.FRIDAY_AM, fridayAMList);
        context.put(PersonalizedAgendaConstants.FRIDAY_PM, fridayPMList);
        
        if(Velocity.evaluate(context,mailMessage,logTag,personalizedAgendaBean.getTemplateBody()))  
        LoggerHelper.logSystemDebug("Class:VelocityConvertor  Method:generateInvitations-- Invitation Prepared for  Trainee *****---"+mailMessage);;
        /*After The Message has been Created We will Create Invitation Letters and Store it. */
        createLabelForAgenda(mailMessage.toString(),personalizedAgendaBean.getEmplId(), personalizedAgendaBean.getServerName());
        }else{
        LoggerHelper.logSystemDebug("Class:VelocityConvertor Method:generateInvitations-- ,InvitationLetterBean is NULL");
        }
    }catch(Exception e){
        e.printStackTrace();
        LoggerHelper.logSystemDebug("Exception Class:VelocityConvertor Method:generateInvitations--"+e);
    }
   }   
   
  private static String paddString(String org, int size) {
    
        String tmp = "";
        
        if (org != null && org.length() < size) {
            tmp = org + "<br>";
        } else {
            tmp = org;
        }
        return tmp;
  }
  
  private static String toEmpty(String tmp) {
    if (tmp == null) {
        return " ";
    }
    return tmp;
  }
  
  private static String getDateString(String start, String end) {
    return "";
  }
  public static void generatePersonalizedAgenda(PersonalizedAgendaBeanP4 personalizedAgendaBean){
    String  msg="";
    StringWriter mailMessage = new StringWriter();
    String logTag="";
    List mondaySession1List = new ArrayList();
    List tuesdaySession1List = new ArrayList();
    List wednesdaySession1List = new ArrayList();
    List thursdaySession1List = new ArrayList();
    List fridaySession1List = new ArrayList();

    List mondaySession2List = new ArrayList();
    List tuesdaySession2List = new ArrayList();
    List wednesdaySession2List = new ArrayList();
    List thursdaySession2List = new ArrayList();
    List fridaySession2List = new ArrayList();
    
    List mondaySession3List = new ArrayList();
    List tuesdaySession3List = new ArrayList();
    List wednesdaySession3List = new ArrayList();
    List thursdaySession3List = new ArrayList();
    List fridaySession3List = new ArrayList();

    List mondaySession4List = new ArrayList();
    List tuesdaySession4List = new ArrayList();
    List wednesdaySession4List = new ArrayList();
    List thursdaySession4List = new ArrayList();
    List fridaySession4List = new ArrayList();

    String currentYear = "2009";
    try{
        if(personalizedAgendaBean!=null){
        personalizedAgendaBean.prepareTemplateBody(); 
       /* first, we init the runtime engine.  Defaults are fine. */
        Velocity.init();
        /* lets make a Context and put data into it */
        VelocityContext context = new VelocityContext();
        /*Now We will Substitute the Values here on the Context */
        
        // Get the first name and last name
        context.put(PersonalizedAgendaConstantsP4.FIRST_NAME,PrintUtils.ifNullNBSP(personalizedAgendaBean.getFirstName()));
        context.put(PersonalizedAgendaConstantsP4.LAST_NAME,PrintUtils.ifNullNBSP(personalizedAgendaBean.getLastName()));
        context.put(PersonalizedAgendaConstantsP4.EMPLI_ID,PrintUtils.ifNullNBSP(personalizedAgendaBean.getEmplId()));
        context.put(PersonalizedAgendaConstantsP4.WEEK_NAME,PrintUtils.ifNullNBSP(personalizedAgendaBean.getWeek_Name()));
        
        /*
        // Get the week start and end date              
        context.put(PersonalizedAgendaConstants.WEEK_START_DATE,PrintUtils.ifNullNBSP(personalizedAgendaBean.getWeekStartDate()));
        context.put(PersonalizedAgendaConstants.WEEK_END_DATE,PrintUtils.ifNullNBSP(personalizedAgendaBean.getWeekEndDate()));
        context.put(PersonalizedAgendaConstants.YEAR,currentYear);
        */
        
        
        // Add the MOndayAM contents
        if(personalizedAgendaBean.getMondaySession1Product() != null && !personalizedAgendaBean.getMondaySession1Product().equals("")){
            mondaySession1List.add(paddString(toEmpty(personalizedAgendaBean.getMondaySession1Product()),20));
            mondaySession1List.add(personalizedAgendaBean.getMondaySession1StartTime()+ " - " + personalizedAgendaBean.getMondaySession1EndTime());
            mondaySession1List.add(personalizedAgendaBean.getMondaySession1Room());
            mondaySession1List.add(toEmpty(personalizedAgendaBean.getMondaySession1Table()));
            
        }
        if(personalizedAgendaBean.getTuesdaySession1Product() != null && !personalizedAgendaBean.getTuesdaySession1Product().equals("")){
            tuesdaySession1List.add(paddString(toEmpty(personalizedAgendaBean.getTuesdaySession1Product()),20));
            tuesdaySession1List.add(personalizedAgendaBean.getTuesdaySession1StartTime() + " - " + personalizedAgendaBean.getTuesdaySession1EndTime());
            tuesdaySession1List.add(personalizedAgendaBean.getTuesdaySession1Room());
            tuesdaySession1List.add(toEmpty(personalizedAgendaBean.getTuesdaySession1Table()));            
        }
        if(personalizedAgendaBean.getWednesdaySession1Product() != null && !personalizedAgendaBean.getWednesdaySession1Product().equals("")){
            wednesdaySession1List.add(paddString(toEmpty(personalizedAgendaBean.getWednesdaySession1Product()),20));
            wednesdaySession1List.add(personalizedAgendaBean.getWednesdaySession1StartTime()+ " - " + personalizedAgendaBean.getWednesdaySession1EndTime());
            wednesdaySession1List.add(personalizedAgendaBean.getWednesdaySession1Room());
            wednesdaySession1List.add(toEmpty(personalizedAgendaBean.getWednesdaySession1Table()));            
        }
        if(personalizedAgendaBean.getThursdaySession1Product() != null && !personalizedAgendaBean.getThursdaySession1Product().equals("")){
            thursdaySession1List.add(paddString(personalizedAgendaBean.getThursdaySession1Product(),20));
            thursdaySession1List.add(personalizedAgendaBean.getThursdaySession1StartTime()+ " - " + personalizedAgendaBean.getThursdaySession1EndTime());
            thursdaySession1List.add(personalizedAgendaBean.getThursdaySession1Room());
            thursdaySession1List.add(toEmpty(personalizedAgendaBean.getThursdaySession1Table()));            
        }
        if(personalizedAgendaBean.getFridaySession1Product() != null && !personalizedAgendaBean.getFridaySession1Product().equals("")){
            fridaySession1List.add(paddString(personalizedAgendaBean.getFridaySession1Product(),20));
            fridaySession1List.add(personalizedAgendaBean.getFridaySession1StartTime()+ " - " + personalizedAgendaBean.getFridaySession1EndTime());
            fridaySession1List.add(personalizedAgendaBean.getFridaySession1Room());
            fridaySession1List.add(toEmpty(personalizedAgendaBean.getThursdaySession1Table()));             
        }
        
        
        
        
        
        // Session2
        if(personalizedAgendaBean.getMondaySession2Product() != null && !personalizedAgendaBean.getMondaySession2Product().equals("")){
            mondaySession2List.add(paddString(toEmpty(personalizedAgendaBean.getMondaySession2Product()),20));
            mondaySession2List.add(personalizedAgendaBean.getMondaySession2StartTime()+ " - " + personalizedAgendaBean.getMondaySession2EndTime());
            mondaySession2List.add(personalizedAgendaBean.getMondaySession2Room());
            mondaySession2List.add(toEmpty(personalizedAgendaBean.getMondaySession2Table()));
            
        }
    
        if(personalizedAgendaBean.getTuesdaySession2Product() != null && !personalizedAgendaBean.getTuesdaySession2Product().equals("")){
            tuesdaySession2List.add(paddString(toEmpty(personalizedAgendaBean.getTuesdaySession2Product()),20));
            tuesdaySession2List.add(personalizedAgendaBean.getTuesdaySession2StartTime()+ " - " + personalizedAgendaBean.getTuesdaySession2EndTime());
            tuesdaySession2List.add(personalizedAgendaBean.getTuesdaySession2Room());
            tuesdaySession2List.add(toEmpty(personalizedAgendaBean.getTuesdaySession2Table()));            
        }
        if(personalizedAgendaBean.getWednesdaySession2Product() != null && !personalizedAgendaBean.getWednesdaySession2Product().equals("")){
            wednesdaySession2List.add(paddString(toEmpty(personalizedAgendaBean.getWednesdaySession2Product()),20));
            wednesdaySession2List.add(personalizedAgendaBean.getWednesdaySession2StartTime()+ " - " +personalizedAgendaBean.getWednesdaySession2EndTime());
            wednesdaySession2List.add(toEmpty(personalizedAgendaBean.getWednesdaySession2Room()));
            wednesdaySession2List.add(toEmpty(personalizedAgendaBean.getWednesdaySession2Table()));            
        }
        if(personalizedAgendaBean.getThursdaySession2Product() != null && !personalizedAgendaBean.getThursdaySession2Product().equals("")){
            thursdaySession2List.add(paddString(toEmpty(personalizedAgendaBean.getThursdaySession2Product()),20));
            thursdaySession2List.add(personalizedAgendaBean.getThursdaySession2StartTime()+ " - " + personalizedAgendaBean.getThursdaySession2EndTime());
            thursdaySession2List.add(personalizedAgendaBean.getThursdaySession2Room());
            thursdaySession2List.add(toEmpty(personalizedAgendaBean.getThursdaySession2Table()));            
        }
        if(personalizedAgendaBean.getFridaySession2Product() != null && !personalizedAgendaBean.getFridaySession2Product().equals("")){
            fridaySession2List.add(paddString(toEmpty(personalizedAgendaBean.getFridaySession2Product()),20));
            fridaySession2List.add(personalizedAgendaBean.getFridaySession2StartTime() + " - " +  personalizedAgendaBean.getFridaySession2EndTime());
            fridaySession2List.add(personalizedAgendaBean.getFridaySession2Room());
            fridaySession2List.add(toEmpty(personalizedAgendaBean.getFridaySession2Table()));            
        }


        if(personalizedAgendaBean.getMondaySession3Product() != null && !personalizedAgendaBean.getMondaySession3Product().equals("")){
            mondaySession3List.add(paddString(toEmpty(personalizedAgendaBean.getMondaySession3Product()),20));
            mondaySession3List.add(personalizedAgendaBean.getMondaySession3StartTime()+ " - " + personalizedAgendaBean.getMondaySession3EndTime());
            mondaySession3List.add(personalizedAgendaBean.getMondaySession3Room());
            mondaySession3List.add(toEmpty(personalizedAgendaBean.getMondaySession3Table()));
            
        }
    
        if(personalizedAgendaBean.getTuesdaySession3Product() != null && !personalizedAgendaBean.getTuesdaySession3Product().equals("")){
            tuesdaySession3List.add(paddString(toEmpty(personalizedAgendaBean.getTuesdaySession3Product()),20));
            tuesdaySession3List.add(personalizedAgendaBean.getTuesdaySession3StartTime()+ " - " + personalizedAgendaBean.getTuesdaySession3EndTime());
            tuesdaySession3List.add(personalizedAgendaBean.getTuesdaySession3Room());
            tuesdaySession3List.add(toEmpty(personalizedAgendaBean.getTuesdaySession3Table()));            
        }
        if(personalizedAgendaBean.getWednesdaySession3Product() != null && !personalizedAgendaBean.getWednesdaySession3Product().equals("")){
            wednesdaySession3List.add(paddString(toEmpty(personalizedAgendaBean.getWednesdaySession3Product()),20));
            wednesdaySession3List.add(personalizedAgendaBean.getWednesdaySession3StartTime()+ " - " +personalizedAgendaBean.getWednesdaySession3EndTime());
            wednesdaySession3List.add(toEmpty(personalizedAgendaBean.getWednesdaySession3Room()));
            wednesdaySession3List.add(toEmpty(personalizedAgendaBean.getWednesdaySession3Table()));            
        }
        if(personalizedAgendaBean.getThursdaySession3Product() != null && !personalizedAgendaBean.getThursdaySession3Product().equals("")){
            thursdaySession3List.add(paddString(toEmpty(personalizedAgendaBean.getThursdaySession3Product()),20));
            thursdaySession3List.add(personalizedAgendaBean.getThursdaySession3StartTime()+ " - " + personalizedAgendaBean.getThursdaySession3EndTime());
            thursdaySession3List.add(personalizedAgendaBean.getThursdaySession3Room());
            thursdaySession3List.add(toEmpty(personalizedAgendaBean.getThursdaySession3Table()));            
        }
        if(personalizedAgendaBean.getFridaySession3Product() != null && !personalizedAgendaBean.getFridaySession3Product().equals("")){
            fridaySession3List.add(paddString(toEmpty(personalizedAgendaBean.getFridaySession3Product()),20));
            fridaySession3List.add(personalizedAgendaBean.getFridaySession3StartTime() + " - " +  personalizedAgendaBean.getFridaySession3EndTime());
            fridaySession3List.add(personalizedAgendaBean.getFridaySession3Room());
            fridaySession3List.add(toEmpty(personalizedAgendaBean.getFridaySession3Table()));            
        }


        if(personalizedAgendaBean.getMondaySession4Product() != null && !personalizedAgendaBean.getMondaySession4Product().equals("")){
            mondaySession4List.add(paddString(toEmpty(personalizedAgendaBean.getMondaySession4Product()),20));
            mondaySession4List.add(personalizedAgendaBean.getMondaySession4StartTime()+ " - " + personalizedAgendaBean.getMondaySession4EndTime());
            mondaySession4List.add(personalizedAgendaBean.getMondaySession4Room());
            mondaySession4List.add(toEmpty(personalizedAgendaBean.getMondaySession4Table()));
            
        }

        if(personalizedAgendaBean.getTuesdaySession4Product() != null && !personalizedAgendaBean.getTuesdaySession4Product().equals("")){
            tuesdaySession4List.add(paddString(toEmpty(personalizedAgendaBean.getTuesdaySession4Product()),20));
            tuesdaySession4List.add(personalizedAgendaBean.getTuesdaySession4StartTime()+ " - " + personalizedAgendaBean.getTuesdaySession4EndTime());
            tuesdaySession4List.add(personalizedAgendaBean.getTuesdaySession4Room());
            tuesdaySession4List.add(toEmpty(personalizedAgendaBean.getTuesdaySession4Table()));            
        }
        if(personalizedAgendaBean.getWednesdaySession4Product() != null && !personalizedAgendaBean.getWednesdaySession4Product().equals("")){
            wednesdaySession4List.add(paddString(toEmpty(personalizedAgendaBean.getWednesdaySession4Product()),20));
            wednesdaySession4List.add(personalizedAgendaBean.getWednesdaySession4StartTime()+ " - " +personalizedAgendaBean.getWednesdaySession4EndTime());
            wednesdaySession4List.add(toEmpty(personalizedAgendaBean.getWednesdaySession4Room()));
            wednesdaySession4List.add(toEmpty(personalizedAgendaBean.getWednesdaySession4Table()));            
        }
        if(personalizedAgendaBean.getThursdaySession4Product() != null && !personalizedAgendaBean.getThursdaySession4Product().equals("")){
            thursdaySession4List.add(paddString(toEmpty(personalizedAgendaBean.getThursdaySession4Product()),20));
            thursdaySession4List.add(personalizedAgendaBean.getThursdaySession4StartTime()+ " - " + personalizedAgendaBean.getThursdaySession4EndTime());
            thursdaySession4List.add(personalizedAgendaBean.getThursdaySession4Room());
            thursdaySession4List.add(toEmpty(personalizedAgendaBean.getThursdaySession4Table()));            
        }
        if(personalizedAgendaBean.getFridaySession4Product() != null && !personalizedAgendaBean.getFridaySession4Product().equals("")){
            fridaySession4List.add(paddString(toEmpty(personalizedAgendaBean.getFridaySession4Product()),20));
            fridaySession4List.add(personalizedAgendaBean.getFridaySession4StartTime() + " - " +  personalizedAgendaBean.getFridaySession4EndTime());
            fridaySession4List.add(personalizedAgendaBean.getFridaySession4Room());
            fridaySession4List.add(toEmpty(personalizedAgendaBean.getFridaySession4Table()));            
        }
        
        context.put(PersonalizedAgendaConstantsP4.MONDAY_SESSION1, mondaySession1List);
        context.put(PersonalizedAgendaConstantsP4.TUESDAY_SESSION1, tuesdaySession1List);
        context.put(PersonalizedAgendaConstantsP4.WEDNESDAY_SESSION1, wednesdaySession1List);
        context.put(PersonalizedAgendaConstantsP4.THURSDAY_SESSION1, thursdaySession1List);
        context.put(PersonalizedAgendaConstantsP4.FRIDAY_SESSION1, fridaySession1List);

        context.put(PersonalizedAgendaConstantsP4.MONDAY_SESSION2, mondaySession2List);
        context.put(PersonalizedAgendaConstantsP4.TUESDAY_SESSION2, tuesdaySession2List);
        context.put(PersonalizedAgendaConstantsP4.WEDNESDAY_SESSION2, wednesdaySession2List);
        context.put(PersonalizedAgendaConstantsP4.THURSDAY_SESSION2, thursdaySession2List);
        context.put(PersonalizedAgendaConstantsP4.FRIDAY_SESSION2, fridaySession2List);

        context.put(PersonalizedAgendaConstantsP4.MONDAY_SESSION3, mondaySession3List);
        context.put(PersonalizedAgendaConstantsP4.TUESDAY_SESSION3, tuesdaySession3List);
        context.put(PersonalizedAgendaConstantsP4.WEDNESDAY_SESSION3, wednesdaySession3List);
        context.put(PersonalizedAgendaConstantsP4.THURSDAY_SESSION3, thursdaySession3List);
        context.put(PersonalizedAgendaConstantsP4.FRIDAY_SESSION3, fridaySession3List);

        context.put(PersonalizedAgendaConstantsP4.MONDAY_SESSION4, mondaySession4List);
        context.put(PersonalizedAgendaConstantsP4.TUESDAY_SESSION4, tuesdaySession4List);
        context.put(PersonalizedAgendaConstantsP4.WEDNESDAY_SESSION4, wednesdaySession4List);
        context.put(PersonalizedAgendaConstantsP4.THURSDAY_SESSION4, thursdaySession4List);
        context.put(PersonalizedAgendaConstantsP4.FRIDAY_SESSION4, fridaySession4List);
        
        if(Velocity.evaluate(context,mailMessage,logTag,personalizedAgendaBean.getTemplateBody()))  
        LoggerHelper.logSystemDebug("Class:VelocityConvertor  Method:generateInvitations-- Invitation Prepared for  Trainee *****---"+mailMessage);;
        /*After The Message has been Created We will Create Invitation Letters and Store it. */
        createLabelForAgenda(mailMessage.toString(),personalizedAgendaBean.getEmplId()+personalizedAgendaBean.getWeek_Name(), personalizedAgendaBean.getServerName());
       // createLabelForAgenda(mailMessage.toString(),personalizedAgendaBean.getEmplId()+personalizedAgendaBean.getWeek_Name(),"C:/temp/pdf/" );

        }else{
        LoggerHelper.logSystemDebug("Class:VelocityConvertor Method:generateInvitations-- ,InvitationLetterBean is NULL");
        }
    }catch(Exception e){
        e.printStackTrace();
        LoggerHelper.logSystemDebug("Exception Class:VelocityConvertor Method:generateInvitations--"+e);
    }
   }
   
   
   private static String getTrainerStatus(String current){
    String result = "";
    if(current != null && current.equals("Y")){
        result = "Yes";
    }
    else{
        result = "No";
    }
    return result;
    
   }
   
   
   
       /**
    * This method is responsible for Generating Invitations amd Substituting Values in the Invitation Template.
    */
   public static String generateInvitations(EmailInfoBean emailInfoBean)
   {
    String sTemplate = "";
    
    String  msg="";
    StringWriter mailMessage = new StringWriter();
    String logTag="";
    List templList = new ArrayList();
    List materialList = new ArrayList();
    String ship2;
    try
    {
        if(emailInfoBean!=null)
        {
          sTemplate = emailInfoBean.getEmailTemplate();
          
          if(sTemplate == null)
          {
            LoggerHelper.logSystemDebug("Please check template code, Null Email Content returned for:" + emailInfoBean.getEmailID());
            return null;
          }
          
          LoggerHelper.logSystemDebug("The tempate that would be applied for :" + emailInfoBean.getEmailID() + "is >> " + sTemplate);
           /* first, we init the runtime engine.  Defaults are fine. */
            Velocity.init();
            /* lets make a Context and put data into it */
            VelocityContext context = new VelocityContext();
            /*Now We will Substitute the Values here on the Context */
            context.put(PrintingConstants.INV_PREF_NAME,PrintUtils.ifNullNBSP(emailInfoBean.getFirstName()));
            context.put(PrintingConstants.INV_FIRST_NAME,PrintUtils.ifNullNBSP(emailInfoBean.getFirstName()));
            context.put(PrintingConstants.INV_LAST_NAME,PrintUtils.ifNullNBSP(emailInfoBean.getLastName()));
            context.put(PrintingConstants.INV_ORDER_NM,PrintUtils.ifNullNBSP(emailInfoBean.getOrderNumber()));
            context.put(PrintingConstants.INV_DATE,(new SimpleDateFormat("MMMM d, yyyy")).format(new Date(System.currentTimeMillis())));
            context.put(PrintingConstants.INV_SHIP_ADD1,PrintUtils.ifNullNBSP(emailInfoBean.getShipAdd1()));
            context.put(PrintingConstants.ORDER_ID,PrintUtils.ifNullNBSP(emailInfoBean.getOrderNumber()));
            //We have to Display Ship2 Address on NEXT Line ;
            if(emailInfoBean.getShipAdd2()!=null && !emailInfoBean.getShipAdd2().trim().equalsIgnoreCase("")){
               ship2="<br>"+emailInfoBean.getShipAdd2() ;
            }else{ship2="&nbsp;";}
            context.put(PrintingConstants.INV_SHIP_ADD2,PrintUtils.ifNullNBSP(ship2));
            context.put(PrintingConstants.INV_SHIP_CITY,PrintUtils.ifNullNBSP(emailInfoBean.getShipCity()));
            context.put(PrintingConstants.INV_SHIP_STATE,PrintUtils.ifNullNBSP(emailInfoBean.getShipState()));
            context.put(PrintingConstants.INV_SHIP_ZIP,PrintUtils.ifNullNBSP(emailInfoBean.getShipZip()));
            //context.put(PrintingConstants.TEAM_DESC,PrintUtils.ifNullNBSP(emailInfoBean.getTeam()));
            
            if(emailInfoBean.getProducts()!=null)
            {                
              //Get Product Array from Comma Delimited Product    
              String sProducts = emailInfoBean.getProducts();
              StringTokenizer st = new StringTokenizer(sProducts, ",");
              while(st.hasMoreTokens())
              {
                String sProduct = st.nextToken();
                sProduct = sProduct.trim();
                // Changes made by Jeevan for RBU Shipment
                String result = "";
                result = PrintHandlers.getClassStartDate(emailInfoBean.getEmplID(), sProduct);
                //sProduct = sProduct + " - " + classDate;
                sProduct = result;
                templList.add(sProduct);
              }
            }        

            if(emailInfoBean.getMaterials()!=null)
            {
              //Get Product Array from Comma Delimited Product                    
              String sMaterials = emailInfoBean.getMaterials();
              StringTokenizer st = new StringTokenizer(sMaterials, ",");
              while(st.hasMoreTokens())
              {
                String sMaterial = st.nextToken();
                sMaterial = sMaterial.trim();
                materialList.add(sMaterial);
              }
            }
            
          String startDate = "";
          Date dateStart = emailInfoBean.getStartDate();
          if(dateStart != null)
          {
            Date dateDayBeforeStart = PrintUtils.addDays(dateStart, -1);
            startDate = PrintUtils.getPedCloseDateFormatted(dateDayBeforeStart);
          }
          
          String endDate = "";
          Date dateEnd = emailInfoBean.getEndDate();
          if(dateEnd != null)
          {
            endDate = PrintUtils.getPedCloseDateFormatted(dateEnd);            
          }
          
         // String sMeetingNumber = emailInfoBean.getMeetingNumber();
          String sTeamDesc = emailInfoBean.getTeam();
          
          
          Collections.sort(templList);
          context.put(PrintingConstants.INV_PRODUCTS,templList);
          context.put(PrintingConstants.INV_ALL_MATERIALS,materialList);        
          context.put(PrintingConstants.INV_STARTDATE, startDate);
          context.put(PrintingConstants.INV_ENDDATE, endDate);
          //context.put(PrintingConstants.INV_MEETINGNUMBER, sMeetingNumber);
         // context.put(PrintingConstants.INV_TEAM, sTeamDesc);
          if(emailInfoBean.getServerName() != null && emailInfoBean.getServerName().indexOf("pfizer.com") != -1){
            context.put(PrintingConstants.IMAGE_URL, "http:://" + emailInfoBean.getServerName() + "/TrainingReports/PrintHome/pfizer_logo.gif");
          }
          else{
             context.put(PrintingConstants.IMAGE_URL, "http://localhost:8619/TrainingReports/PrintHome/pfizer_logo.gif");
          }
          /*
          if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_local))
             context.put(PrintingConstants.IMAGE_URL, "http://localhost:7001/TrainingReports/PrintHome/pfizer_logo.gif");
          else
          if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_staging))
            context.put(PrintingConstants.IMAGE_URL, "http:://trstg.pfizer.com/TrainingReports/PrintHome/pfizer_logo.gif");
          else
            context.put(PrintingConstants.IMAGE_URL, "http:://trt.pfizer.com/TrainingReports/PrintHome/pfizer_logo.gif");              
            */
            
            if(Velocity.evaluate(context,mailMessage,logTag,sTemplate))
            {
              LoggerHelper.logSystemDebug("Class:VelocityConvertor  Method:generateEmailInvitations-- Invitation Prepared for  Trainee ---"+ emailInfoBean.getEmailID() + "---" + mailMessage);;
            }else{
              LoggerHelper.logSystemDebug("Class:VelocityConvertor Method:generateEmailInvitations-- ,EmailInfoBean is NULL");
            }
           
      }//if
      return mailMessage.toString();
    }
    catch(Exception e)
    {
        e.printStackTrace();
        LoggerHelper.logSystemDebug("Exception Class:VelocityConvertor Method:generateEmailInvitations--"+e);
        return "";
    }
   }
   
   
   
  public static String readFromURL(String url){
    StringBuffer contentBuffer=new StringBuffer();
    String inputLine;
    try{
    URL urlHTML = new URL(url);
    URLConnection urlConnect = urlHTML.openConnection();
    urlConnect.setDoOutput(true);
    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
    while ((inputLine = in.readLine()) != null)contentBuffer.append(inputLine);
    }catch(Exception e){
        LoggerHelper.logSystemDebug("Class:VelocityConvertor Method:readFromURL-- ,Error in Reading from the URL-"+url+"--"+e);
        e.printStackTrace();
    }
    return  contentBuffer.toString();


  }

@Override
public boolean getBlockingShutdown() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean getExitOnShutdown() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public File getHomeDir() {
	// TODO Auto-generated method stub
	return new File(HOME_DIR);
}

@Override
public URL getHomeURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(HOME_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public URL getLibraryURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(LIBRARY_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public URL getPatchURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(PATCH_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public boolean getRequireJBossURLStreamHandlerFactory() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public String getRootDeploymentFilename() {
	// TODO Auto-generated method stub
	return ROOT_DEPLOYMENT_FILENAME;
}

@Override
public File getServerBaseDir() {
	// TODO Auto-generated method stub
	return new File(SERVER_BASE_DIR);
}

@Override
public URL getServerBaseURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(SERVER_BASE_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public URL getServerConfigURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(SERVER_CONFIG_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public File getServerDataDir() {
	// TODO Auto-generated method stub
	return new File(SERVER_DATA_DIR);
}

@Override
public File getServerHomeDir() {
	// TODO Auto-generated method stub
	return new File(SERVER_HOME_DIR);
}

@Override
public URL getServerHomeURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(SERVER_HOME_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public URL getServerLibraryURL() {
	// TODO Auto-generated method stub
	URL url=null;
	try {
		url= new URL(SERVER_LIBRARY_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return url;
}

@Override
public String getServerName() {
	// TODO Auto-generated method stub
	return SERVER_NAME;
}

@Override
public File getServerTempDir() {
	// TODO Auto-generated method stub
	return new File(SERVER_TEMP_DIR);
}

@Override
public void setBlockingShutdown(boolean arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void setExitOnShutdown(boolean arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void setRequireJBossURLStreamHandlerFactory(boolean arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void setRootDeploymentFilename(String arg0) {
	// TODO Auto-generated method stub
	
}


  
    
} //end of the Class
