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


public class RBUTravelFeedHandler 

{ 
    private static final Log log = LogFactory.getLog(RBUTravelFeedHandler.class );
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
        //get current set
        //get log set
        //calculation new set
        //create file
        //email 
        //clean up log and dump file data to log
        getNewList();
        /*
        //getLatestList();      
        boolean inserted = false;
        //Step1. track changes, new employee or employee with track changes will be inserted
        for(Iterator cIter= cList.iterator(); cIter.hasNext();){
             RBUTravelFeed datafromc = (RBUTravelFeed) cIter.next();
             
             inserted = false;
             for(Iterator logi= lList.iterator(); logi.hasNext();){
                RBUTravelFeed datafromlog = (RBUTravelFeed) logi.next();
                if (datafromlog.getEmplid().equals(datafromc.getEmplid())){
                    if(!(datafromlog.getTracks().equals(datafromc.getTracks()) || 
                        !(datafromlog.getGttracks().equals(datafromc.getGttracks())))){
                        newList.add(datafromc);
                        System.out.println("employee with track change " + datafromc.getEmplid()); 
                        inserted = true;
                    }
                    //same track 
                    inserted = true;
                }

             }
             if(!inserted){
               newList.add(datafromc);
             }
        }
        //Step2. If a person is completely dropped from training after initially being enrolled, include an entry for that person with null for tracks column
        List tmplist = new ArrayList();
        for(Iterator logi= lList.iterator(); logi.hasNext();){
            RBUTravelFeed datafromlog = (RBUTravelFeed) logi.next();
            if(!cList.contains(datafromlog)){
                datafromlog.setTracks("");
                datafromlog.setGttracks("");
                tmplist.add(fullload(datafromlog));
            }
        }
        if(tmplist.size()>0){
            newList.addAll(tmplist);
        }
        */
        
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
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
   		Connection conn = JdbcConnectionUtil.getJdbcConnection();
   		/* Infosys - Weblogic to Jboss migration changes end here */
            
         try{
           /* Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
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
                tmpStr.append(Util.toEmpty(data.getEmplid()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getFirstName()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getLastName()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getSex()));
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getEmail()));                
                tmpStr.append("\",\"");
               // tmpStr.append(Util.toEmpty(data.getFutre_role()));                
              //  tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getMaster_flag()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getTrainer_flag()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManager_flag()));                
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getTracks()));   
                //CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL,CURRENT_AREA,CURRENT_REGION,CURRENT_CLUSTER,CURRENT_TEAM
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getRole()));        
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getManagername()));        
                tmpStr.append("\",\"");
                tmpStr.append(Util.toEmpty(data.getSupervisorEmail()));        
                //removed per requirement change
             //   tmpStr.append("\",\"");
               // tmpStr.append(Util.toEmpty(data.getAreaDesc()));        
             //   tmpStr.append("\",\"");
            //    tmpStr.append(Util.toEmpty(data.getRegionDesc()));        
             //   tmpStr.append("\",\"");
             //   tmpStr.append(Util.toEmpty(data.getClusterCode()));        
             //   tmpStr.append("\",\"");
            //    tmpStr.append(Util.toEmpty(data.getTeamCode())); 
                tmpStr.append("\"\r\n");             
                entries[index] = tmpStr.toString();
                 
               // System.out.println("index " +index + " " + entries[index]);   
                tmpStr.setLength(0);
                index ++;
            }
            System.out.println("new list size " + newList.size()); 
          /*  File tfile;
            
            String p2lFolder = getP2LFolder(PrintingConstants.env_type);
                //String p2lFolder = getP2LFolder(PrintingConstants.env_local);
                //p2lFile=new File(p2lFolder + File.separator + "TRT_P2L_Registration_" + formatFileName.format(new Date()) + ".txt");    
            
            //filename = p2lFolder + File.separator + "travel_feed_" + formatFileName.format(new Date()) + ".csv";
            filename = p2lFolder + File.separator + "travel_feed_" + formatFileName.format(new Date()) + ".txt";            
            // This is added to get the complete path on different servers
             if(requestURL != null && requestURL.indexOf("trint.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_INT  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("trstg.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_STG  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("trt.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_PROD  + filename;
            }
             LoggerHelper.logSystemDebug("Travel feed file name :" + filename + "Entries >>>  " + entries.toString());
              System.out.println("Travel feed file name :" + filename + "Entries >>>  " + entries.toString());
             
            System.out.println(filename);
            tfile=new File(filename);
              
           BufferedWriter bf1  = new BufferedWriter(new FileWriter(tfile));
            bf1.write("EMPLID,FIRST_NAME,LAST_NAME,SEX,EMAIL_ADDRESS,FUTURE_ROLE,MASTERS_FLAG,TRAINER_FLAG,MANAGER_FLAG,TRACKS,CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL,CURRENT_AREA,CURRENT_REGION,CURRENT_CLUSTER,CURRENT_TEAM\r\n");
                for (int i=0; i<entries.length; i++) {                
                    bf1.write(entries[i]);
                    bf1.write("\r\n");
                }            
                    
            bf1.flush();
            bf1.close();
          */  
       // }catch(IOException e){
       //         e.printStackTrace();
      //  }

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
        
        
        /*   SELECT MAX(FEED_ID) + 1 INTO TMP FROM RBU_TRAVEL_FEED_HEADER;
   INSERT INTO  RBU_TRAVEL_FEED_HEADER (FEED_GENERATED_TIMESTAMP,NUMBER_OF_RECOREDS_SENT, FEED_ID )
   		  VALUES(SYSDATE, NUMBEROFRECORDS, TMP); 
   INSERT INTO RBU_TRAVEL_FEED_DETAILS
   		  (FEED_ID,  EMPLID,  TRACKS,  GT_TRACKS,  MASTERS_FLAG,  TRAINER_FLAG,  MANAGER_FLAG)
  		  VALUES(TMP, EMPLID, TRACKS, GT_TRACKS,MASTERS_FLAG,TRAINER_FLAG, MANAGER_FLAG);*/    
        
        getDumpList();
        
        StringBuffer sql = new StringBuffer("");
            
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection(); */ 
            st = conn.createStatement();

            rs = st.executeQuery("select MAX(FEED_ID) + 1  feedid from RBU_TRAVEL_FEED_HEADER ");
            if(rs.next()){
                feedid = rs.getInt("feedid");
            }
            

            st.executeQuery("INSERT INTO RBU_TRAVEL_FEED_HEADER (FEED_GENERATED_TIMESTAMP,NUMBER_OF_RECORDS_SENT, FEED_ID )"
                + " VALUES(SYSDATE," + dumpList.size() + ", "+ feedid + " )  ");
            
            System.out.println("dump size + " + dumpList.size());
            for(Iterator iter = dumpList.iterator(); iter.hasNext();){
                 RBUTravelFeed data = (RBUTravelFeed) iter.next();
                 sql.append("INSERT INTO RBU_TRAVEL_FEED_DETAILS(FEED_ID,  EMPLID,  TRACKS,  GT_TRACKS,  MASTERS_FLAG,  TRAINER_FLAG,  MANAGER_FLAG) values (");
                 sql.append(feedid+ ", '" + data.getEmplid() + "', ");
                 if( data.getTracks() == null){
                    sql.append("NULL, ");
                 }else{
                    sql.append("'" + data.getTracks() + "', ");
                 }
                 if(data.getGttracks() == null){
                    sql.append("NULL, ");
                 }else{
                     sql.append("'" + data.getGttracks() + "', ");
                 }
                 if(data.getMaster_flag() == null){
                    sql.append("NULL, ");
                 }else{
                     sql.append("'" + data.getMaster_flag() + "', ");
                 }
                 if(data.getTrainer_flag() == null){
                    sql.append("NULL, ");
                 }else{
                     sql.append("'" + data.getTrainer_flag() + "', ");
                 }
                 if(data.getManager_flag() == null){
                    sql.append("NULL) ");
                 }else{
                     sql.append("'" + data.getManager_flag() + "') ");
                 }

                st.executeQuery(sql.toString());
                sql.setLength(0);
            }         

            st.close();
            conn.close();

        }catch(Exception e){
                e.printStackTrace();
                log.error(sql, e);
                System.out.println(sql);
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
	/*	Connection conn = null;*/      
        /* Infosys - Weblogic to Jboss migration changes start here */
   		Connection conn = JdbcConnectionUtil.getJdbcConnection();
   		/* Infosys - Weblogic to Jboss migration changes end here */     
         try{
           /* Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
            st = conn.createStatement();
            rs = st.executeQuery("select * from V_RBU_TRAVEL_FEED_DIFF");
            // CURRENT_ROLE, AREA_DESC, 
 //REGION_DESC, CLUSTER_CD, TEAM_CD, CURRENT_SUPERVISOR, CURRENT_SUPERVISOR_EMAIL
 /*CREATE OR REPLACE VIEW V_RBU_TRAVEL_FEED_DIFF
(EMPLID, FIRST_NAME, LAST_NAME, SEX, EMAIL_ADDRESS, 
 MASTERS_FLAG, TRAINER_FLAG, MANAGER_FLAG, TRACKS, CURRENT_ROLE, 
 CURRENT_SUPERVISOR, CURRENT_SUPERVISOR_EMAIL)
 */
			while (rs.next()) {    
                
                RBUTravelFeed data = new RBUTravelFeed();
                data.setEmail(rs.getString("email_address"));
                data.setEmplid(rs.getString("emplid"));
                data.setFirstName(rs.getString("first_name"));
                data.setLastName(rs.getString("last_name"));
                //data.setFutre_role(rs.getString("FUTURE_ROLE"));
                data.setSex(rs.getString("SEX"));
                data.setManager_flag(rs.getString("MANAGER_FLAG"));
                data.setMaster_flag(rs.getString("MASTERS_FLAG"));
                data.setTrainer_flag(rs.getString("TRAINER_FLAG"));
                data.setRole(rs.getString("CURRENT_ROLE"));
                //data.setAreaDesc(rs.getString("AREA_DESC"));
                //data.setRegionDesc(rs.getString("REGION_DESC"));
                //data.setClusterCode(rs.getString("CLUSTER_CD"));
                
                //data.setTeamCode(rs.getString("TEAM_CD"));               
               
                
                data.setManagername(rs.getString("CURRENT_SUPERVISOR"));
                data.setSupervisorEmail(rs.getString("CURRENT_SUPERVISOR_EMAIL"));

                
                
                if(rs.getString("TRACKS")!=null){
                    data.setTracks(rs.getString("TRACKS").replaceAll(" ", ""));
                }
               // if(rs.getString("GT_TRACKS")!=null){
               //     data.setGttracks(rs.getString("GT_TRACKS"));
                    //data.setTrainer_flag("Y");
              //  }else{
                    //data.setTrainer_flag("N");
              //  }
                
                
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
	/*	Connection conn = null;*/      
        /* Infosys - Weblogic to Jboss migration changes start here */
   		Connection conn = JdbcConnectionUtil.getJdbcConnection();
   		/* Infosys - Weblogic to Jboss migration changes end here */     
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
            st = conn.createStatement();;
            rs = st.executeQuery("select m.emplid, m.tracks, m.gt_tracks, m.masters_fg as masters_flag, "
                                + "  DECODE(GT_TRACKS,NULL,'N','Y') AS trainer_flag, rm.ismanager AS manager_flag " 
                                + " from v_rbu_travel_feed m, v_rbu_future_alignment ra, rbu_future_role_map rm "
                                + " where m.emplid=ra.emplid (+) AND ra.territory_role_cd=rm.territory_role_cd (+) ");
            
			while (rs.next()) {    
                RBUTravelFeed data = new RBUTravelFeed();
               // data.setEmail(rs.getString("email_address"));
                data.setEmplid(Util.toEmpty(rs.getString("emplid")));
                data.setMaster_flag(rs.getString("MASTERS_FLAG"));
              /*  data.setFirstName(rs.getString("first_name"));
                data.setLastName(rs.getString("last_name"));
                data.setSex(rs.getString("SEX"));
                data.setFutre_role(rs.getString("FUTURE_ROLE"));
                data.setManager_flag(rs.getString("MANAGER_FLAG"));
                data.setMaster_flag(rs.getString("MASTERS_FLAG"));
                
                data.setRole(rs.getString("CURRENT_ROLE"));
                data.setAreaDesc(rs.getString("AREA_DESC"));
                data.setRegionDesc(rs.getString("REGION_DESC"));
                data.setClusterCode(rs.getString("CLUSTER_CD"));
                data.setTeamCode(rs.getString("TEAM_CD"));
                data.setManagername(rs.getString("CURRENT_SUPERVISOR"));
                data.setSupervisorEmail(rs.getString("CURRENT_SUPERVISOR_EMAIL"));
                */


               // if(rs.getString("TRACKS")!=null){
                data.setTracks(rs.getString("TRACKS"));
               // }
                data.setGttracks(rs.getString("GT_TRACKS"));
                data.setTrainer_flag(rs.getString("trainer_flag"));
                data.setManager_flag(rs.getString("MANAGER_FLAG"));

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
             TO = readProperties.getValue("EMAIL_TRAVEL_FEED_TO_PROD");
             String bcc1 = readProperties.getValue("EMAIL_TRAVEL_FEED_REGISTRATION_BCC_PROD1");
//           //  String bcc2 = readProperties.getValue("EMAIL_TRAVEL_FEED_REGISTRATION_BCC_PROD2");
             BCC = bcc1;
             CC = readProperties.getValue("EMAIL_TRAVEL_FEED_CC_PROD");
             
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
        
        message.setSubject("PSCPT Travel Feed");
        // bf1.write("EMPLID,FIRST_NAME,LAST_NAME,SEX,EMAIL_ADDRESS,FUTURE_ROLE,MASTERS_FLAG,TRAINER_FLAG,MANAGER_FLAG,TRACKS,CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL,CURRENT_AREA,CURRENT_REGION,CURRENT_CLUSTER,CURRENT_TEAM\r\n");
        StringBuffer tmp = new StringBuffer("EMPLID,FIRST_NAME,LAST_NAME,SEX,EMAIL_ADDRESS,MASTERS_FLAG,TRAINER_FLAG,MANAGER_FLAG,TRACKS,CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL\r\n");
        for (int i = 0 ;i <input.length;i ++){
            tmp.append(input[i]);
        }
        
        String filename = "";
           ByteArrayInputStream str = new ByteArrayInputStream((tmp.toString()).getBytes()); 
            File tfile;
            String p2lFolder = getP2LFolder(PrintingConstants.env_type);
            filename =  p2lFolder + File.separator + "travel_feed_" + formatFileName.format(new Date()) + ".csv";   
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
        
        System.out.println(filename + " is generated ");
            
         String fileAttachment = filename;
    
    
        // create the message part 
        MimeBodyPart messageBodyPart = new MimeBodyPart();
    
        //fill message
        messageBodyPart.setText("Most recent travel feed for PSCPT.");
    
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
