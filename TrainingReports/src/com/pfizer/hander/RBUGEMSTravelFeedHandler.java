package com.pfizer.hander; 

import com.pfizer.db.RBUTravelFeed;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.AppConst;
import com.tgix.Utils.JNDIUtil;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;
import com.tgix.printing.PrintingConstants;
import com.tgix.printing.VelocityConvertor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RBUGEMSTravelFeedHandler 

{ 
    private static final Log log = LogFactory.getLog(RBUGEMSTravelFeedHandler.class );
    //Emails should go 'To' gems@pfizer.com and bcc'ed to rbu.trt@tgix.com
    
    
    private  String BCC= "";
    //private static final String TO= "gems@pfizer.com";
    private   String TO= "";
    private static final String FROM= "traininglogistics@pfizer.com";
    
   // private  List cList = new ArrayList();
    private  List dumpList = new ArrayList();
    private  List newList = new ArrayList();
    private  int feedid;
    
    SimpleDateFormat formatFileName = new SimpleDateFormat("MM-dd-yyyy-S"); 
    
    public String generateCSV(String requestURL){
        String result = "";

        getNewList();

        
        if(newList.size()>0){
        //email
            
            if(!postMail(createFile(requestURL), requestURL)){
                result = "Unable to send out email.";
                return result;
            }
            dumplog();
           
            result = "Travel Feed has been generated successfully.  ";
        }else{
            result = "There are no differences in the travel feed from the previous version.";
        }
        
        return result;
    }
    
    private RBUTravelFeed fullload(RBUTravelFeed datafromlog){
        RBUTravelFeed data = new RBUTravelFeed();
        ResultSet rs = null;
        Statement st = null;
	/*	Connection conn = null;*/      
        /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */
  		
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection(); */ 
            st = conn.createStatement();
            rs = st.executeQuery("select * from V_RBU_TRAVEL_FEED_CURRENT where EMPLID = '" + datafromlog.getEmplid() + "'" );

			while (rs.next()) {    
              
                data.setEmail(rs.getString("email_address"));
                data.setEmplid(rs.getString("emplid"));
                data.setFirstName(rs.getString("first_name"));
                data.setLastName(rs.getString("last_name"));
                //data.setFutre_role(rs.getString("FUTURE_ROLE"));
                data.setSex(rs.getString("SEX"));
                //data.setManager_flag(rs.getString("MANAGER_FLAG"));
                data.setMaster_flag(rs.getString("MASTERS_FG"));
                
                data.setRole(rs.getString("CURRENT_ROLE"));
                data.setAreaDesc(rs.getString("AREA_DESC"));
                data.setRegionDesc(rs.getString("REGION_DESC"));
                data.setClusterCode(rs.getString("CLUSTER_CD"));
                
                data.setTeamCode(rs.getString("TEAM_CD"));               
               
                
                data.setManagername(rs.getString("CURRENT_SUPERVISOR"));
                data.setSupervisorEmail(rs.getString("CURRENT_SUPERVISOR_EMAIL"));
 
			}    
            
            rs = st.executeQuery("select * from V_RBU_TRAVEL_FEED_FUTURE where EMPLID = '" + datafromlog.getEmplid() + "'");

			while (rs.next()) {    
                data.setFutre_role(rs.getString("FUTURE_ROLE"));
                data.setManager_flag(rs.getString("MANAGER_FLAG")); 
			}    
            
            
            data.setTrainer_flag(datafromlog.getTrainer_flag());
            data.setTracks(datafromlog.getTracks());
            data.setGttracks(datafromlog.getGttracks());
            st.close();
            conn.close();
            
            

        }catch(Exception e){
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		} 
        return data;
        
    }
    
    
    
    private String[] createFile(String requestURL){
        String filename = "";
       // try{
            String[] entries = new String[newList.size()];
            
            int index = 0;
            StringBuffer tmpStr = new StringBuffer("");
            for(Iterator i = newList.iterator(); i.hasNext();){
                RBUTravelFeed data = (RBUTravelFeed) i.next();
                tmpStr.append("\"");
                tmpStr.append(Util.toEmpty(data.getOperation()));
                 tmpStr.append("\",\"");
                 tmpStr.append(Util.toEmpty(data.getFirstName()));
                tmpStr.append("\",\"");
                 tmpStr.append(Util.toEmpty(data.getLastName()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getEmplid()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getEmail()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getAreaCd()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getAreaDesc()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getClusterDesc()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getToviazIdentifier()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getState()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getMappedRole()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getSex()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getMaster_flag()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManagerSalesPositionId()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManagerLastName()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManagerFirstName()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManagerEmailAddress()));                
                //tmpStr.append(Util.toEmpty(data.getSupervisorEmail()));        LAST ONE
                tmpStr.append("\"\r\n");             
                entries[index] = tmpStr.toString();
                 
               // System.out.println("index " +index + " " + entries[index]);   
                tmpStr.setLength(0);
                index ++;
            }
            //System.out.println("new list size " + newList.size()); 


            return entries;
    }

    private String getP2LFolder(String sEnv)
    {
        String sDeploymentRoot = VelocityConvertor.getDeploymentRoot(sEnv);
        String sTemplateRoot = sDeploymentRoot + File.separator + "TrainingReports" + File.separator +
                               "TravelFeedFiles" + File.separator + "CreatedFiles";
        //System.out.println("sTemplateRoot:" + sTemplateRoot);
        return sTemplateRoot;                
    }
    
    private void dumplog(){
        
        ResultSet rs = null;
        Statement st = null;
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */
            
        
        getDumpList();
        
        StringBuffer sql = new StringBuffer("");
            
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();  */
            st = conn.createStatement();

            rs = st.executeQuery("select MAX(FEED_ID) + 1  feedid from RBU_TOVIAZ_FEED_HEADER ");
            if(rs.next()){
                feedid = rs.getInt("feedid");
            }
            

            st.executeQuery("INSERT INTO RBU_TOVIAZ_FEED_HEADER (FEED_ID,NUM_RECORDS,FEED_DATE)"
                + " VALUES("+ feedid + "," + dumpList.size() + ", SYSDATE)  ");
            
            //System.out.println("dump size + " + dumpList.size());
            for(Iterator iter = dumpList.iterator(); iter.hasNext();){
                 RBUTravelFeed data = (RBUTravelFeed) iter.next();
                 sql.append("INSERT INTO RBU_TOVIAZ_FEED_DETAILS(FEED_ID, EMPLID,  TOVIAZ_IDENTIFIER,AREA_CD,AREA_DESC) values (");
                 sql.append(feedid+ ", '" + data.getEmplid() + "', ");
                 if( data.getToviazIdentifier() == null){
                    sql.append("NULL, ");
                 }else{
                    sql.append("'" + data.getToviazIdentifier() + "', ");
                 }
                 if( data.getAreaCd() == null){
                    sql.append("NULL, ");
                 }else{
                    sql.append("'" + data.getAreaCd() + "', ");
                 }
               
                if(data.getAreaDesc() == null){
                    sql.append("NULL) ");
                 }else{
                     sql.append("'" + data.getAreaDesc() + "') ");
                 }

                st.executeQuery(sql.toString());
                sql.setLength(0);
            }         

            st.close();
            conn.close();

        }catch(Exception e){
                e.printStackTrace();
                log.error(sql, e);
                //System.out.println(sql);
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}   
        
    }
    private void getNewList(){
        
        ResultSet rs = null;
        Statement st = null;
		/*Connection conn = null;*/      
        /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */  
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
            st = conn.createStatement();
            rs = st.executeQuery("select * from V_RBU_GEMS_DATA_FEED_DIFF");
		while (rs.next()) {    
                
                RBUTravelFeed data = new RBUTravelFeed();
                data.setOperation(rs.getString("OPERATION"));
                data.setFirstName(rs.getString("FIRST_NAME"));
                data.setLastName(rs.getString("LAST_NAME"));
                data.setEmplid(rs.getString("EMPLID"));
                data.setEmail(rs.getString("EMAIL_ADDRESS"));
                data.setAreaCd(rs.getString("AREA_CD"));
                data.setAreaDesc(rs.getString("AREA_DESC"));
                data.setClusterDesc(rs.getString("CLUSTER_DESC"));
                data.setToviazIdentifier(rs.getString("TOVIAZ_IDENTIFIER"));
                data.setState(rs.getString("STATE"));
                data.setMappedRole(rs.getString("MAPPED_ROLE"));
                data.setSex(rs.getString("GENDER"));
                data.setMaster_flag(rs.getString("MASTERS_FLAG"));
                data.setManagerSalesPositionId(rs.getString("MANAGER_SALES_POSITION_ID"));
                data.setManagerLastName(rs.getString("MANAGER_LAST_NAME"));
                data.setManagerFirstName(rs.getString("MANAGER_FIRST_NAME"));
                data.setManagerEmailAddress(rs.getString("MANAGER_EMAIL_ADDRESS"));
                //if(rs.getString("TRACKS")!=null){
                  //  data.setTracks(rs.getString("TRACKS").replaceAll(" ", ""));
               // }
                
                newList.add(data);
			}    
            

        }catch(Exception e){
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}   
        
    }
    
    void getDumpList(){        
        ResultSet rs = null;
        Statement st = null;
		/*Connection conn = null;*/      
        /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */  
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection(); */ 
            st = conn.createStatement();
            rs = st.executeQuery("SELECT  EMPLID, TOVIAZ_IDENTIFIER, AREA_CD, AREA_DESC from V_RBU_GEMS_DATA_FEED ");
            /*rs = st.executeQuery("select m.emplid, m.tracks, m.gt_tracks, m.masters_fg as masters_flag, "
                                + "  DECODE(GT_TRACKS,NULL,'N','Y') AS trainer_flag, rm.ismanager AS manager_flag " 
                                + " from v_rbu_travel_feed m, v_rbu_future_alignment ra, rbu_future_role_map rm "
                                + " where m.emplid=ra.emplid (+) AND ra.territory_role_cd=rm.territory_role_cd (+) ");*/
            
			while (rs.next()) {    
                RBUTravelFeed data = new RBUTravelFeed();
                data.setEmplid(Util.toEmpty(rs.getString("EMPLID")));
                data.setToviazIdentifier(rs.getString("TOVIAZ_IDENTIFIER"));
                data.setAreaCd(rs.getString("AREA_CD"));
                data.setAreaDesc(rs.getString("AREA_DESC"));
                dumpList.add(data);
			}    
            

        }catch(Exception e){
                
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}   
        
    }
    
    private boolean postMail(  String[] input, String requestURL)
    {

      String CC="";
      LoggerHelper.logSystemDebug("#### Request URL #####" + requestURL);
       boolean ifsucceed = false;
       try{
        Session session = (Session)JNDIUtil.lookupS("trMailSession");
        session.setDebug(false);
        ReadProperties readProperties = new ReadProperties("EmailHandlingProperties.propeties");
          if(requestURL != null && requestURL.indexOf("wlsdev1.pfizer.com") != -1
              || requestURL != null && requestURL.indexOf("wlsstg5.pfizer.com") != -1
          || requestURL != null && requestURL.indexOf("localhost") != -1){
            
           LoggerHelper.logSystemDebug("#### Request URL IF for INT/DEV/STG for Travel feed email sending #####");
            String value = readProperties.getValue("EMAIL_TRAVEL_FEED_TO_DEV_INT_STG");
            TO = value; 
            BCC = "jeevan@tgix.com";
            CC=readProperties.getValue("EMAIL_TRAVEL_FEED_CC_DEV_INT_STG");
            
          }
         else if(requestURL != null && requestURL.indexOf("wlsprd4.pfizer.com") != -1){
            
            LoggerHelper.logSystemDebug("#### Request URL ELSE IF for PROD for Travel feed email sending #####");
             TO = readProperties.getValue("EMAIL_TRAVEL_FEED_GEMS_TO_PROD");
             String bcc1 = readProperties.getValue("EMAIL_TRAVEL_FEED_REGISTRATION_BCC_PROD1");
//           //  String bcc2 = readProperties.getValue("EMAIL_TRAVEL_FEED_REGISTRATION_BCC_PROD2");
             BCC = bcc1;
             // Currently CC is commented out.
             //CC = readProperties.getValue("EMAIL_TRAVEL_FEED_GEMS_CC_PROD");
             CC = null;
             
          }
        
        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        if(CC != null){
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(CC));   
        } 
        if(BCC != null){
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(BCC));
        }
        
        message.setSubject("Toviaz Launch Travel Feed");
        // bf1.write("EMPLID,FIRST_NAME,LAST_NAME,SEX,EMAIL_ADDRESS,FUTURE_ROLE,MASTERS_FLAG,TRAINER_FLAG,MANAGER_FLAG,TRACKS,CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL,CURRENT_AREA,CURRENT_REGION,CURRENT_CLUSTER,CURRENT_TEAM\r\n");
        StringBuffer tmp = new StringBuffer("OPERATION,FIRST_NAME,LAST_NAME,EMPLID,EMAIL_ADDRESS,AREA_CD,AREA_DESC,CLUSTER_DESC,TOVIAZ_IDENTIFIER,STATE,MAPPED_ROLE,GENDER,MASTERS_FLAG,MANAGER_SALES_POSITION_ID,MANAGER_LAST_NAME,MANAGER_FIRST_NAME,MANAGER_EMAIL_ADDRESS\r\n");
        for (int i = 0 ;i <input.length;i ++){
            tmp.append(input[i]);
        }
        
        String filename = "";
           ByteArrayInputStream str = new ByteArrayInputStream((tmp.toString()).getBytes()); 
            File tfile;
            String p2lFolder = getP2LFolder(PrintingConstants.env_type);
            filename =  p2lFolder + File.separator + "GEMS_travel_feed_" + formatFileName.format(new Date()) + ".csv";   
            // This is added to get the complete path on different servers
             if(requestURL != null && requestURL.indexOf("wlsdev1.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_INT  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("wlsstg5.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_STG  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("wlsprd4.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_PROD  + filename;
            }
            LoggerHelper.logSystemDebug("File name in RBU Travel Feed ##### " + filename);
            //System.out.println("########## File name " + filename);
            //tfile=new File(filename);
            
            FileOutputStream fos = new FileOutputStream(filename);
            int data;
            while((data=str.read())!=-1)
            {
            char ch = (char)data;
            fos.write(ch);
            }
            fos.flush();
            fos.close();
            
        LoggerHelper.logSystemDebug(filename + " is generated ");
        
        //System.out.println(filename + " is generated ");
            
         String fileAttachment = filename;
    
    
        // create the message part 
        MimeBodyPart messageBodyPart = new MimeBodyPart();
    
        //fill message
        messageBodyPart.setText("Most recent travel feed for Toviaz Launch.");
    
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
    
        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        javax.activation.DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment);
        multipart.addBodyPart(messageBodyPart);
    
        // Put parts in message
        message.setContent(multipart);
    
        // Send the message
        Transport.send( message );
        ifsucceed = true;
       }catch(Exception e){
                e.printStackTrace();
                ifsucceed = false;
       }
        
        return ifsucceed;

    }
    
    

} 
