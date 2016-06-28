package com.tgix.printing; 

import java.util.Map;

public class PrintingConstants 
{    
    //Lets KEEP THE SYSTEM/PROCESS ID as 3
    public static final String Weblogic_User_ID="3";
    //Constants Needed For  Printing:
     public static final String Printing_Template_Directory="/TemplateFiles";
     //Name of the File where we have HTML Code , Velocity will parse this File for gernation Invitations
     public static final String templateFileName="InvitationLetter";
     public static final String templateFileNamePromotion="PWRAInvitationTemplate_Promotion";
     public static final String templateFileNameLateral="PWRAInvitationTemplate_Lateral";
     public static final String templateFileNameDM="PWRAInvitationTemplate_DM";
     public static final String templateFileNameAircept="PWRAInvitationTemplate_Aircept";
     public static final String templateFileNameSteerePromotion="SteereInvitationTemplate_Promotion";
     public static final String templateFileNameSteereLateral="SteereInvitationTemplate_Lateral";
     public static final String templateFileNameSteereAltLateral="SteereInvitationTemplate_AltaLateral";
     public static final String templateFileNameSteereUpjohnLateral="SteereInvitationTemplate_UpjohnLateral";
     public static final String templateFileNamePowerPHRLateral="PowerInvitationTemplate_PHRLateral";

     public static final String emailtemplateFileNamePromotion="emailPWRAInvitationTemplate_Promotion";
     public static final String emailtemplateFileNameLateral="emailPWRAInvitationTemplate_Lateral";
     public static final String emailtemplateFileNameDM="emailPWRAInvitationTemplate_DM";
     public static final String emailtemplateFileNameAircept="emailPWRAInvitationTemplate_Aircept";
     public static final String emailtemplateFileNameSteereLateral="emailSteereInvitationTemplate_Lateral";
     public static final String emailtemplateFileNameSteerePromotion="emailSteereInvitationTemplate_Promotion";
     public static final String emailtemplateFileNameSteereAltaLateral="emailSteereInvitationTemplate_AltaLateral";
     public static final String emailtemplateFileNameSteereUpjohnLateral="emailSteereInvitationTemplate_UpjohnLateral";
     public static final String emailtemplateFileNamePowerPHR="emailPowerInvitationTemplate_PHRLateral";
     public static final String emailtemplateFileNamePDFGeodon="emailPDFGeodon";
     

     
     public static final String Printing_Label_Directory="TemplateFiles/CreatedLabels";
     //We will add OrderID to it, when generating file ,INVXXX.html will be the file 
     public static final String INVITATION_OUTPUT_FILE = "INV"; 
     
     public static final String EMAIL_DELIMITER = ",";
     //Date Formats
     public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
     public static final String INVITATION_DATE_FORMAT = "EEEEE, MMMM d, yyyy";
     public static final String EXTENDED_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
     
     //Inviatation Template Constants
     public static final String INV_FIRST_NAME = "firstName";
     public static final String INV_LAST_NAME = "lastName";
     public static final String INV_PREF_NAME = "prefName";
     public static final String INV_SHIP_ADD1 = "shipAdd1";
     public static final String INV_SHIP_ADD2 = "shipAdd2";
     public static final String INV_SHIP_CITY = "shipCity";
     public static final String INV_SHIP_STATE = "shipState";
     public static final String INV_SHIP_ZIP = "shipZip";
     public static final String INV_ORDER_NM = "orderNumber";
     public static final String INV_DATE = "currentDate";
     public static final String INV_ALL_MATERIALS = "materials";
     public static final String INV_CLUSTER_CD = "clusterCd";
     public static final String INV_PEDCLOSE_DATE = "pedCloseDate";
     public static final String INV_PRODUCTS = "products";
     public static final String INV_STARTDATE = "startdate";
     public static final String INV_ENDDATE   = "enddate";
     public static final String INV_MEETINGNUMBER   = "meetingnumber";
     public static final String INV_TEAM   = "teamdesc";
     public static final String ORDER_ID= "OrderID";
     public static final String TEAM_DESC= "teamdesc";
     public static final String IMAGE_URL = "imageurl";
     
     
     
     //Location in Local and Staging Env. where it will be creating Labels
     public static final String path_for_local="D:/bea/workshopdomains/domains/EFT_FFT/EFT_FFT_Server/wlnotdelete/EFTApplication/EFTApplicationWeb/jsp";
     public static final String path_for_staging="D:/bea/workshopdomains/domains/EFT_FFT/EFT_FFT_Server/.wlnotdelete/EFTApplication/EFTApplicationWeb/jsp";
    // public static final String path_for_prod="D:/bea/workshop/domains/eft/eftadmin/.wlnotdelete/EFTApplication/EFTApplicationWeb/jsp";
    //Selvam update for cluster 2-26-2007
    public static final String path_for_prod="D:/bea/workshop/domains/eft/eftadmin/.wlnotdelete/EFTApplication/EFTApplicationWeb/jsp";          
    public static final String env_local="Local";
    public static final String env_staging="Dev";
    public static final String env_prod="Prod";
    public static  String env_type = env_prod;   
    
    // This will be different for different instances
    // For INT
    //public static String APPLICATION_PATH_INT = "/app/user_projects/gpr814d22/wls1_814d222/stage/TRTProd";
    public static String APPLICATION_PATH_INT = "/app/jbshome/jboss61domains/trt-tst";
    
    // For STG
    //public static String APPLICATION_PATH_STG = "/app/user_projects/dm814s12/wls5_814s122/stage/TRTProd";   
    public static String APPLICATION_PATH_STG = "/app/jbshome/jboss61domains/trt-stg";
    
    // For PROD
   // public static String APPLICATION_PATH_PROD = "/app/user_projects/dm814p42/wls4_814p422/stage/TRTProd";
    public static String APPLICATION_PATH_PROD = "/app/jbshome/jboss61domains/trt-prd";
   
    // For INT
    //public static String APPLICATION_PATH_JSP_INT = "http://wlsdev1.pfizer.com:8222";
      //public static String APPLICATION_PATH_JSP_INT = "http://trint.pfizer.com";
    public static String APPLICATION_PATH_JSP_INT = "http://amrndhl279:17200";
    
    // For STG
    //public static String APPLICATION_PATH_JSP_STG = "http://wlsstg5.pfizer.com:8122";
  //public static String APPLICATION_PATH_JSP_STG = "http://trstg.pfizer.com";  
    public static String APPLICATION_PATH_JSP_STG = "http://amrndhl286:17200";
    
    // For PROD
    //public static String APPLICATION_PATH_JSP_PROD = "http://wlsprd4.pfizer.com:8422"; 
   // public static String APPLICATION_PATH_JSP_PROD = "http://trt.pfizer.com";
    public static String APPLICATION_PATH_JSP_PROD = "http://amrndhl289:17200";
    
   // This will change for each local instance
    //public static String APPLICATION_PATH_DEV = "C:/bea/weblogic81/samples/domains/workshop/cgServer/stage/TRTProd";       
    public static String APPLICATION_PATH_DEV = "/app/jbshome/jboss61domains/trt-dev";
    
   // public static String APPLICATION_PATH_JSP_DEV = "http://amrnwlw058:8619";
    public static String APPLICATION_PATH_JSP_DEV = "http://amrndhl275:17200";
     //For Address Validations
     public static final String ILLEGAL_ADDRESS_TOKEN = "box";
     public static final int PHONE_NUMBER_FIELD_LENGTH = 10; 
     public static final int ZIP_CODE_LENGTH = 5; 
     public static final String POBOX_Pattern ="[p]\\s*[\\.]?\\s*[o]\\s*[\\.]?\\s*box";
     public static final String ADDRESS1_VALIDATION="Address1 cannot be blank";
     public static final String ADDRESS1_POBOX_VALIDATION="Address1 contains P.O. BOX";
     public static final String ADDRESS1_LENGTH_VALIDATION="Address1 length is greater than 35";
     public static final String ADDRESS2_LENGTH_VALIDATION="Address2 length is greater than 35";
     public static final String ADDRESS2_POBOX_VALIDATION="Address2 contains P.O. BOX";
     public static final String CITY_VALIDATION="City cannot be blank";
     public static final String SATE_VALIDATION="State cannot be blank";
     public static final String PHONE_NUMBER_VALIDATION="Phone Number should be of 10 Digits";
     public static final String ZIP_VALIDATION="ZIP Code cannot be blank";
     public static final String ZIP_SIZE_VALIDATION="ZIP Code should be of 5 Digits";
     
     
     //EMAIL JNDI NAME:
     public static final String EMAIL_JNDI_NAME="EFTMailSession";
     
     //EMAIL Template That will be going to Pedagogue Reminder
     public static final String PED_EMAIL_NAME="PedEmail";
    public static final String PED_EMAIL_FROM="traininglogistics@pfizer.com";
    // public static final String PED_EMAIL_FROM="amit@tgix.com";
     public static final String PED_EMAIL_SUBJECT="Pedagogue Reminder Email";
     public static long  THREAD_SLEEP_MILL_SECONDS=15; 
} 
