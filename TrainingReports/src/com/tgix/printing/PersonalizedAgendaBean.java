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
 * @author Jeevan
 * The Bean will have all the information that we need to substitute in the Personalised Agenda Letter.
 *
 */

public class PersonalizedAgendaBean
{
    private String firstName;
	private String lastName;
	private String  templateBody;
    private String emplId;
    private String serverName;
    
    private String mondayAMProduct;
	private String mondayAMTable;
	private String mondayAMRoom;
	private String mondayAMTrainer;
    private String mondayPMProduct;
    private String mondayPMTable;
    private String mondayPMRoom;
    private String mondayPMTrainer;
    private String tuesdayAMProduct;
    private String tuesdayAMTable;
    private String tuesdayAMRoom;
    private String tuesdayAMTrainer;
    private String tuesdayPMProduct;
    private String tuesdayPMTable;
    private String tuesdayPMRoom;
    private String tuesdayPMTrainer;
    private String wednesdayAMProduct;
    private String wednesdayAMTable;
    private String wednesdayAMRoom;
    private String wednesdayAMTrainer;
    private String wednesdayPMProduct;
    private String wednesdayPMTable;
    private String wednesdayPMRoom;
    private String wednesdayPMTrainer;
    private String thursdayAMProduct;
    private String thursdayAMTable;
    private String thursdayAMRoom;
    private String thursdayAMTrainer;
    private String thursdayPMProduct;
    private String thursdayPMTable;
    private String thursdayPMRoom;
    private String thursdayPMTrainer;
    private String fridayAMProduct;
    private String fridayAMTable;
    private String fridayAMRoom;
    private String fridayAMTrainer;
    private String fridayPMProduct;
    private String fridayPMTable;
    private String fridayPMRoom;
    private String fridayPMTrainer;
    private String mondayAMStartTime;
	private String mondayAMEndTime;
	private String mondayPMStartTime;
	private String mondayPMEndTime;
	private String tuesdayAMStartTime;
	private String tuesdayAMEndTime;
	private String tuesdayPMStartTime;
	private String tuesdayPMEndTime;
	private String wednesdayAMStartTime;
	private String wednesdayAMEndTime;
	private String wednesdayPMStartTime;
	private String wednesdayPMEndTime;
	private String thursdayAMStartTime;
	private String thursdayAMEndTime;
	private String thursdayPMStartTime;
	private String thursdayPMEndTime;
	private String fridayAMStartTime;
	private String fridayAMEndTime;
	private String fridayPMStartTime;
	private String fridayPMEndTime;
        
        private String weekStartDate;
	    private String weekEndDate;
        private String year;
        private VelocityConvertor velocityConvertor=new VelocityConvertor();
        
	    
		public String getWeekStartDate() {
			return weekStartDate;
		}
		public void setWeekStartDate(String weekStartDate) {
			this.weekStartDate = weekStartDate;
		}
		public String getWeekEndDate() {
			return weekEndDate;
		}
		public void setWeekEndDate(String weekEndDate) {
			this.weekEndDate = weekEndDate;
		}
		public String getMondayAMProduct() {
			return mondayAMProduct;
		}
		public void setMondayAMProduct(String mondayAMProduct) {
			this.mondayAMProduct = mondayAMProduct;
		}
		
		public String getMondayAMTable() {
			return mondayAMTable;
		}
		public void setMondayAMTable(String mondayAMTable) {
			this.mondayAMTable = mondayAMTable;
		}
		public String getMondayAMRoom() {
			return mondayAMRoom;
		}
		public void setMondayAMRoom(String mondayAMRoom) {
			this.mondayAMRoom = mondayAMRoom;
		}
		public String getMondayAMTrainer() {
			return mondayAMTrainer;
		}
		public void setMondayAMTrainer(String mondayAMTrainer) {
			this.mondayAMTrainer = mondayAMTrainer;
		}
		public String getMondayPMProduct() {
			return mondayPMProduct;
		}
		public void setMondayPMProduct(String mondayPMProduct) {
			this.mondayPMProduct = mondayPMProduct;
		}
		
		public String getMondayPMTable() {
			return mondayPMTable;
		}
		public void setMondayPMTable(String mondayPMTable) {
			this.mondayPMTable = mondayPMTable;
		}
		public String getMondayPMRoom() {
			return mondayPMRoom;
		}
		public void setMondayPMRoom(String mondayPMRoom) {
			this.mondayPMRoom = mondayPMRoom;
		}
		public String getMondayPMTrainer() {
			return mondayPMTrainer;
		}
		public void setMondayPMTrainer(String mondayPMTrainer) {
			this.mondayPMTrainer = mondayPMTrainer;
		}
		public String getTuesdayAMProduct() {
			return tuesdayAMProduct;
		}
		public void setTuesdayAMProduct(String tuesdayAMProduct) {
			this.tuesdayAMProduct = tuesdayAMProduct;
		}
		
		public String getTuesdayAMTable() {
			return tuesdayAMTable;
		}
		public void setTuesdayAMTable(String tuesdayAMTable) {
			this.tuesdayAMTable = tuesdayAMTable;
		}
		public String getTuesdayAMRoom() {
			return tuesdayAMRoom;
		}
		public void setTuesdayAMRoom(String tuesdayAMRoom) {
			this.tuesdayAMRoom = tuesdayAMRoom;
		}
		public String getTuesdayAMTrainer() {
			return tuesdayAMTrainer;
		}
		public void setTuesdayAMTrainer(String tuesdayAMTrainer) {
			this.tuesdayAMTrainer = tuesdayAMTrainer;
		}
		public String getTuesdayPMProduct() {
			return tuesdayPMProduct;
		}
		public void setTuesdayPMProduct(String tuesdayPMProduct) {
			this.tuesdayPMProduct = tuesdayPMProduct;
		}
		
		public String getTuesdayPMTable() {
			return tuesdayPMTable;
		}
		public void setTuesdayPMTable(String tuesdayPMTable) {
			this.tuesdayPMTable = tuesdayPMTable;
		}
		public String getTuesdayPMRoom() {
			return tuesdayPMRoom;
		}
		public void setTuesdayPMRoom(String tuesdayPMRoom) {
			this.tuesdayPMRoom = tuesdayPMRoom;
		}
		public String getTuesdayPMTrainer() {
			return tuesdayPMTrainer;
		}
		public void setTuesdayPMTrainer(String tuesdayPMTrainer) {
			this.tuesdayPMTrainer = tuesdayPMTrainer;
		}
		public String getWednesdayAMProduct() {
			return wednesdayAMProduct;
		}
		public void setWednesdayAMProduct(String wednesdayAMProduct) {
			this.wednesdayAMProduct = wednesdayAMProduct;
		}
		
		public String getWednesdayAMTable() {
			return wednesdayAMTable;
		}
		public void setWednesdayAMTable(String wednesdayAMTable) {
			this.wednesdayAMTable = wednesdayAMTable;
		}
		public String getWednesdayAMRoom() {
			return wednesdayAMRoom;
		}
		public void setWednesdayAMRoom(String wednesdayAMRoom) {
			this.wednesdayAMRoom = wednesdayAMRoom;
		}
		public String getWednesdayAMTrainer() {
			return wednesdayAMTrainer;
		}
		public void setWednesdayAMTrainer(String wednesdayAMTrainer) {
			this.wednesdayAMTrainer = wednesdayAMTrainer;
		}
		public String getWednesdayPMProduct() {
			return wednesdayPMProduct;
		}
		public void setWednesdayPMProduct(String wednesdayPMProduct) {
			this.wednesdayPMProduct = wednesdayPMProduct;
		}
		
		public String getWednesdayPMTable() {
			return wednesdayPMTable;
		}
		public void setWednesdayPMTable(String wednesdayPMTable) {
			this.wednesdayPMTable = wednesdayPMTable;
		}
		public String getWednesdayPMRoom() {
			return wednesdayPMRoom;
		}
		public void setWednesdayPMRoom(String wednesdayPMRoom) {
			this.wednesdayPMRoom = wednesdayPMRoom;
		}
		public String getWednesdayPMTrainer() {
			return wednesdayPMTrainer;
		}
		public void setWednesdayPMTrainer(String wednesdayPMTrainer) {
			this.wednesdayPMTrainer = wednesdayPMTrainer;
		}
		public String getThursdayAMProduct() {
			return thursdayAMProduct;
		}
		public void setThursdayAMProduct(String thursdayAMProduct) {
			this.thursdayAMProduct = thursdayAMProduct;
		}
		
		public String getThursdayAMTable() {
			return thursdayAMTable;
		}
		public void setThursdayAMTable(String thursdayAMTable) {
			this.thursdayAMTable = thursdayAMTable;
		}
		public String getThursdayAMRoom() {
			return thursdayAMRoom;
		}
		public void setThursdayAMRoom(String thursdayAMRoom) {
			this.thursdayAMRoom = thursdayAMRoom;
		}
		public String getThursdayAMTrainer() {
			return thursdayAMTrainer;
		}
		public void setThursdayAMTrainer(String thursdayAMTrainer) {
			this.thursdayAMTrainer = thursdayAMTrainer;
		}
		public String getThursdayPMProduct() {
			return thursdayPMProduct;
		}
		public void setThursdayPMProduct(String thursdayPMProduct) {
			this.thursdayPMProduct = thursdayPMProduct;
		}
		
		public String getThursdayPMTable() {
			return thursdayPMTable;
		}
		public void setThursdayPMTable(String thursdayPMTable) {
			this.thursdayPMTable = thursdayPMTable;
		}
		public String getThursdayPMRoom() {
			return thursdayPMRoom;
		}
		public void setThursdayPMRoom(String thursdayPMRoom) {
			this.thursdayPMRoom = thursdayPMRoom;
		}
		public String getThursdayPMTrainer() {
			return thursdayPMTrainer;
		}
		public void setThursdayPMTrainer(String thursdayPMTrainer) {
			this.thursdayPMTrainer = thursdayPMTrainer;
		}
		public String getFridayAMProduct() {
			return fridayAMProduct;
		}
		public void setFridayAMProduct(String fridayAMProduct) {
			this.fridayAMProduct = fridayAMProduct;
		}
		
		public String getFridayAMTable() {
			return fridayAMTable;
		}
		public void setFridayAMTable(String fridayAMTable) {
			this.fridayAMTable = fridayAMTable;
		}
		public String getFridayAMRoom() {
			return fridayAMRoom;
		}
		public void setFridayAMRoom(String fridayAMRoom) {
			this.fridayAMRoom = fridayAMRoom;
		}
		public String getFridayAMTrainer() {
			return fridayAMTrainer;
		}
		public void setFridayAMTrainer(String fridayAMTrainer) {
			this.fridayAMTrainer = fridayAMTrainer;
		}
		public String getFridayPMProduct() {
			return fridayPMProduct;
		}
		public void setFridayPMProduct(String fridayPMProduct) {
			this.fridayPMProduct = fridayPMProduct;
		}
		
		public String getFridayPMTable() {
			return fridayPMTable;
		}
		public void setFridayPMTable(String fridayPMTable) {
			this.fridayPMTable = fridayPMTable;
		}
		public String getFridayPMRoom() {
			return fridayPMRoom;
		}
		public void setFridayPMRoom(String fridayPMRoom) {
			this.fridayPMRoom = fridayPMRoom;
		}
		public String getFridayPMTrainer() {
			return fridayPMTrainer;
		}
		public void setFridayPMTrainer(String fridayPMTrainer) {
			this.fridayPMTrainer = fridayPMTrainer;
		}

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
    
    public String getMondayAMStartTime() {
		return mondayAMStartTime;
	}
	public void setMondayAMStartTime(String mondayAMStartTime) {
		this.mondayAMStartTime = mondayAMStartTime;
	}
	public String getMondayAMEndTime() {
		return mondayAMEndTime;
	}
	public void setMondayAMEndTime(String mondayAMEndTime) {
		this.mondayAMEndTime = mondayAMEndTime;
	}
	public String getMondayPMStartTime() {
		return mondayPMStartTime;
	}
	public void setMondayPMStartTime(String mondayPMStartTime) {
		this.mondayPMStartTime = mondayPMStartTime;
	}
	public String getMondayPMEndTime() {
		return mondayPMEndTime;
	}
	public void setMondayPMEndTime(String mondayPMEndTime) {
		this.mondayPMEndTime = mondayPMEndTime;
	}
	public String getTuesdayAMStartTime() {
		return tuesdayAMStartTime;
	}
	public void setTuesdayAMStartTime(String tuesdayAMStartTime) {
		this.tuesdayAMStartTime = tuesdayAMStartTime;
	}
	public String getTuesdayAMEndTime() {
		return tuesdayAMEndTime;
	}
	public void setTuesdayAMEndTime(String tuesdayAMEndTime) {
		this.tuesdayAMEndTime = tuesdayAMEndTime;
	}
	public String getTuesdayPMStartTime() {
		return tuesdayPMStartTime;
	}
	public void setTuesdayPMStartTime(String tuesdayPMStartTime) {
		this.tuesdayPMStartTime = tuesdayPMStartTime;
	}
	public String getTuesdayPMEndTime() {
		return tuesdayPMEndTime;
	}
	public void setTuesdayPMEndTime(String tuesdayPMEndTime) {
		this.tuesdayPMEndTime = tuesdayPMEndTime;
	}
	public String getWednesdayAMStartTime() {
		return wednesdayAMStartTime;
	}
	public void setWednesdayAMStartTime(String wednesdayAMStartTime) {
		this.wednesdayAMStartTime = wednesdayAMStartTime;
	}
	public String getWednesdayAMEndTime() {
		return wednesdayAMEndTime;
	}
	public void setWednesdayAMEndTime(String wednesdayAMEndTime) {
		this.wednesdayAMEndTime = wednesdayAMEndTime;
	}
	public String getWednesdayPMStartTime() {
		return wednesdayPMStartTime;
	}
	public void setWednesdayPMStartTime(String wednesdayPMStartTime) {
		this.wednesdayPMStartTime = wednesdayPMStartTime;
	}
	public String getWednesdayPMEndTime() {
		return wednesdayPMEndTime;
	}
	public void setWednesdayPMEndTime(String wednesdayPMEndTime) {
		this.wednesdayPMEndTime = wednesdayPMEndTime;
	}
	public String getThursdayAMStartTime() {
		return thursdayAMStartTime;
	}
	public void setThursdayAMStartTime(String thursdayAMStartTime) {
		this.thursdayAMStartTime = thursdayAMStartTime;
	}
	public String getThursdayAMEndTime() {
		return thursdayAMEndTime;
	}
	public void setThursdayAMEndTime(String thursdayAMEndTime) {
		this.thursdayAMEndTime = thursdayAMEndTime;
	}
	public String getThursdayPMStartTime() {
		return thursdayPMStartTime;
	}
	public void setThursdayPMStartTime(String thursdayPMStartTime) {
		this.thursdayPMStartTime = thursdayPMStartTime;
	}
	public String getThursdayPMEndTime() {
		return thursdayPMEndTime;
	}
	public void setThursdayPMEndTime(String thursdayPMEndTime) {
		this.thursdayPMEndTime = thursdayPMEndTime;
	}
	public String getFridayAMStartTime() {
		return fridayAMStartTime;
	}
	public void setFridayAMStartTime(String fridayAMStartTime) {
		this.fridayAMStartTime = fridayAMStartTime;
	}
	public String getFridayAMEndTime() {
		return fridayAMEndTime;
	}
	public void setFridayAMEndTime(String fridayAMEndTime) {
		this.fridayAMEndTime = fridayAMEndTime;
	}
	public String getFridayPMStartTime() {
		return fridayPMStartTime;
	}
	public void setFridayPMStartTime(String fridayPMStartTime) {
		this.fridayPMStartTime = fridayPMStartTime;
	}
	public String getFridayPMEndTime() {
		return fridayPMEndTime;
	}
	public void setFridayPMEndTime(String fridayPMEndTime) {
		this.fridayPMEndTime = fridayPMEndTime;
	}
    
    
	public void prepareTemplateBody(){
       InputStream is = null;
       String templateBody="";
       try{
         String sTemplateFileName = "Personalized_Agenda_Template";
         String sTemplateFolder = velocityConvertor.getTemplateFolder(PrintingConstants.env_type);
            LoggerHelper.logSystemDebug("Path of the template folder >>>>>>>>>>> "+ sTemplateFolder);
          String sTemplatePath = sTemplateFolder + File.separator + sTemplateFileName +".html";
         //   String sTemplatePath = "/TrainingReports/WebContent/PrintHome/Templates" + File.separator + sTemplateFileName +".html";
          LoggerHelper.logSystemDebug("Path of the sTemplatePath >>>>>>>>>>>>"+ sTemplatePath);
          String serverName = this.serverName;
          LoggerHelper.logSystemDebug("Server Name >>>>>>>>>>>>"+ serverName);
           //if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
          if(serverName != null && serverName.indexOf("trt-tst.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_INT  + sTemplatePath;
            }
         //else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt-stg.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_STG  + sTemplatePath;
            }
             //else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_PROD  + sTemplatePath;
            }
           
          LoggerHelper.logSystemDebug("Path of the sTemplatePath after applying server name>>>>>>>>>>>>"+ sTemplatePath);
          URL url = new URL(sTemplatePath);
          URLConnection urlConnection = url.openConnection();
          HttpURLConnection connection = (HttpURLConnection)urlConnection;;
          BufferedReader  bf  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         // BufferedReader  bf  = new BufferedReader(new FileReader(sTemplatePath));
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
          LoggerHelper.logSystemError("Class:PersonalizedAgendaBean Method:prepareTemplateBody--Couldn't locate the Invitation Template in prepareTemplateBody ",e);
          }
          }catch(Exception e) {LoggerHelper.logSystemError("Class:PersonalizedAgendaBean Method:prepareTemplateBody--Error Reading Email temaplte Body ",e);}

          }

    public String getTemplateBody(){
        return this.templateBody;
    }
}
