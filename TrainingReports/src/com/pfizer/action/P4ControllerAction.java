package com.pfizer.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;


import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.actionForm.P4ClassRoomReportForm;
import com.pfizer.actionForm.P4traineetablemapForm;
import com.pfizer.db.RBUClassRosterBean;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.P4Handler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.components.report.P4ClassRoomReportWc;
import com.pfizer.webapp.wc.components.report.P4ClassRosterReportWc;
import com.pfizer.webapp.wc.components.report.P4SceReportWc;
import com.pfizer.webapp.wc.components.report.P4TraineeTableMapWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.printing.TrainingWeeks;
import com.tgix.wc.WebPageComponent;

public class P4ControllerAction  extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	
	TransactionDB trDb= new TransactionDB();
	protected static final Log log = LogFactory.getLog(AdminAction.class );
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private P4traineetablemapForm p4traineetablemapForm=new P4traineetablemapForm();
	private P4ClassRoomReportForm p4ClassRoomReportForm=new P4ClassRoomReportForm();
	
	public P4traineetablemapForm getP4traineetablemapForm() {
		return p4traineetablemapForm;
	}

	public void setP4traineetablemapForm(P4traineetablemapForm p4traineetablemapForm) {
		this.p4traineetablemapForm = p4traineetablemapForm;
	}

	public P4ClassRoomReportForm getP4ClassRoomReportForm() {
		return p4ClassRoomReportForm;
	}

	public void setP4ClassRoomReportForm(P4ClassRoomReportForm p4ClassRoomReportForm) {
		this.p4ClassRoomReportForm = p4ClassRoomReportForm;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public HttpServletResponse getResponse() {
		return response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
		
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpSession getSession() {
		return request.getSession();
	}

	
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	/* public static class P4ClassRoomReportForm extends ActionSupport
	    {
	        private List weeks = new ArrayList();

	        private String week_id = "0";
	        private String wave_id = "0";

	        public void setWeek_id(String week_id)
	        {
	            this.week_id = week_id;
	        }

	        public String getWeek_id()
	        {
	            return this.week_id;
	        }
	        public void setWave_id(String wave_id)
	        {
	            this.wave_id = wave_id;
	        }

	        public String getWave_id()
	        {
	            return this.wave_id;
	        }

	        public void setWeeks(List weeks)
	        {
	            this.weeks = weeks;
	        }

	        public List getWeeks()
	        {
	            // For data binding to be able to post data back, complex types and
	            // arrays must be initialized to be non-null.
	            if(this.weeks == null)
	            {
	                this.weeks = new ArrayList();
	            }

	            return this.weeks;
	        }
	    }*/
	    
	    
	        /**
	     * FormData get and set methods may be overwritten by the Form Bean editor.
	     */
	  /*  public static class P4traineetablemapForm extends ActionSupport
	    {
	        private String room_id;

	        private String week_id;
	        private String wave_id;

	        private String class_id;

	        private String table_id;

	        private String room;

	        private String product;

	        private String day;

	        public void setDay(String day)
	        {
	            this.day = day;
	        }

	        public String getDay()
	        {
	            // For data binding to be able to post data back, complex types and
	            // arrays must be initialized to be non-null. This type doesn't have
	            // a default constructor, so Workshop cannot initialize it for you.

	            // TODO: Initialize day if it is null.
	            //if(this.day == null)
	            //{
	            //    this.day = new Date(?);
	            //}

	            return this.day;
	        }

	        public void setProduct(String product)
	        {
	            this.product = product;
	        }

	        public String getProduct()
	        {
	            return this.product;
	        }

	        public void setRoom(String room)
	        {
	            this.room = room;
	        }

	        public String getRoom()
	        {
	            return this.room;
	        }

	        public void setTable_id(String table_id)
	        {
	            this.table_id = table_id;
	        }

	        public String getTable_id()
	        {
	            return this.table_id;
	        }

	        public void setClass_id(String class_id)
	        {
	            this.class_id = class_id;
	        }

	        public String getClass_id()
	        {
	            return this.class_id;
	        }

	        public void setWeek_id(String week_id)
	        {
	            this.week_id = week_id;
	        }

	        public String getWeek_id()
	        {
	            return this.week_id;
	        }
	        public void setWave_id(String wave_id)
	        {
	            this.wave_id = wave_id;
	        }

	        public String getWave_id()
	        {
	            return this.wave_id;
	        }

	        public void setRoom_id(String room_id)
	        {
	            this.room_id = room_id;
	        }

	        public String getRoom_id()
	        {
	            return this.room_id;
	        }
	    }
	    */
	
	    
	    public String begin()
	    {
	        try{
	    	return new String("success");
	        }
	        catch (Exception e) {
	        	Global.getError(getRequest(),e);
	        	return new String("failure");
	        	}

	    }
	    
	 public String displayClassRosterReport()
	    {
	        try{   
	        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        HttpServletRequest req = this.getRequest();
	        UserSession uSession;        
			uSession = UserSession.getUserSession(getRequest());        
	        AppQueryStrings qStrings = new AppQueryStrings();        
			User user;
	        uSession = UserSession.getUserSession(getRequest());
	        String firstTime = "";
	        if(req.getParameter("firstTime") != null){
	            firstTime = (String)req.getParameter("firstTime");
	        }
	        // Training week array to be displayed in the drop down.
	        TrainingWeeks[] trainingWeekArayDropDown;
	        String query = "SELECT distinct wave_id as week_id, ('Wave ' || wave_id ) as week_name from V_P4_class_roster_report order by wave_id asc";
	        trainingWeekArayDropDown = trDb.getTrainingWeeksP4(query);
	        if(firstTime.equals("true") ){
	            TrainingWeeks[] trainingWeekAray;
	            String sql = "SELECT distinct wave_id as week_id, ('Wave ' || wave_id ) as week_name from V_P4_class_roster_report order by wave_id asc";
	            trainingWeekAray = trDb.getTrainingWeeksP4(sql);
	            String  WaveId = "";
	            WaveId = req.getParameter("WaveId")==null?"":req.getParameter("WaveId");
	            RBUClassRosterBean[] rbuClassRosterBeanArray;
	            StringBuffer sbr = new StringBuffer();
	            sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY HH24:MI:SS') as startDate, emplid, first_name as firstName, last_name as lastName,  is_trainer as isTrainer,");
	           /* sbr.append(" room_name as roomName, table_id as tableNumber FROM V_P4_class_roster_report");*/
	            sbr.append(" room_name as roomName, table_id as tableNumber FROM V_P4_class_roster_report");
	            sbr.append(" WHERE wave_id='"+WaveId+"'   ORDER BY start_date, product_desc,  table_id");
	            System.out.println("Query ############### " + sbr.toString());
	            rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr.toString());
	            String displayResult = "Y";
	    
	            P4ClassRosterReportWc main = new P4ClassRosterReportWc(rbuClassRosterBeanArray, AppConst.EVENT_RBU, trainingWeekArayDropDown, displayResult);
	            MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_RBU,"P4REPORTROSTER" );  
	            page.setMain(main);
	            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);	
	            if(getSession().getAttribute("WaveId") != null){
	                getSession().removeAttribute("WaveId");
	            }
	            getSession().setAttribute("WaveId", WaveId);	
	        } else {
	            TrainingWeeks[] trainingWeekAray;
	            String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY HH24:MI:SS') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
	            trainingWeekAray = trDb.getTrainingWeeks(sql);
	            String WaveId = req.getParameter("WaveId")==null?"":req.getParameter("WaveId");
	            System.out.println("WaveId from request ##################" + WaveId);
	            String downloadExcel = req.getParameter("downloadExcel")==null?"":req.getParameter("downloadExcel");
	            RBUClassRosterBean[] rbuClassRosterBeanArray;
	            StringBuffer sbr = new StringBuffer();
	            P4ClassRosterReportWc main = null;
	            String displayResult = "Y";
	            if (!"true".equalsIgnoreCase(downloadExcel)) {	
	                sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY HH24:MI:SS') as startDate, emplid, first_name as firstName, last_name as lastName,  is_trainer as isTrainer,");
	                sbr.append(" room_name as roomName, table_id as tableNumber FROM V_P4_class_roster_report");
	                sbr.append(" WHERE wave_id ='"+WaveId+"'   ORDER BY start_date, product_desc,  table_id");
	                rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr.toString());
	                main = new P4ClassRosterReportWc(rbuClassRosterBeanArray, AppConst.EVENT_RBU, trainingWeekArayDropDown, displayResult);
	            }
	            if ("true".equalsIgnoreCase(downloadExcel)) {	
	                String employeeNumber = "";
	                String output = "";
	                if(this.getRequest().getParameter("orderNumber") != null){
	                    employeeNumber = this.getRequest().getParameter("orderNumber");
	                }
	                if(employeeNumber != null && !employeeNumber.equals("") && employeeNumber.length() > 0){
	                     StringTokenizer token = new StringTokenizer(employeeNumber, ",");
	                    
	                    while(token.hasMoreTokens()){
	                         output =output +  "'" + token.nextToken() + "'";
	                        output = output+ ",";
	                    }
	                    output = output.substring(0, output.length() - 1);
	                 }
	                sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY HH24:MI:SS') as startDate, emplid, first_name as firstName, last_name as lastName, is_trainer as isTrainer,");
	               /* sbr.append(" room_name as roomName, table_id as tableNumber FROM V_P4_class_roster_report");*/
	                sbr.append(" room_name as roomName,table_id as tableNumber FROM V_P4_class_roster_report");
	                sbr.append(" WHERE wave_id='"+WaveId+"'");
	                if(output != null && !output.equals("") && output.length() > 0){
	                    sbr.append(" and emplid in (" + output.trim() + ")");
	                }
	                sbr.append(" ORDER BY start_date, product_desc,  table_id");
	                System.out.println("Query in download excel ######## " + sbr.toString());
	                rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr.toString());
	                main = new P4ClassRosterReportWc(rbuClassRosterBeanArray, AppConst.EVENT_RBU, trainingWeekAray, displayResult);		
	                WebPageComponent page;
	                page = new BlankTemplateWpc(main);
	                getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
	                // getRequest().setAttribute("exceldownload","true"); 
	                
	                getResponse().setHeader("content-disposition","attachment;filename=\"P4-ClassRosterReport.xls\"");
	                getResponse().setContentType("application/vnd.ms-excel");	
	                getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
	                getResponse().setHeader("Pragma","public");		
	                return new String("successXls");
	           }
	            MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_RBU,"P4REPORTROSTER" );  
	            page.setMain(main);
	            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);	
	            if(getSession().getAttribute("WaveId") != null){
	                getSession().removeAttribute("WaveId");
	            }
	            getSession().setAttribute("WaveId", WaveId);	
	        } 
	        return new String("success");
	        }
	        catch (Exception e) {
	        	Global.getError(getRequest(),e);
	        	return new String("failure");
	        	}

	    }
	
	// public String P4ClassRoomReport(P4ClassRoomReportForm form)
	 public String P4ClassRoomReport()
	    {
			try{
		    UserSession uSession;        
			uSession = UserSession.getUserSession(getRequest());        
	        AppQueryStrings qStrings = new AppQueryStrings();        
			User user;
			
	        uSession = UserSession.getUserSession(getRequest());
			
			if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
				getRequest().getSession().invalidate();		
				//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
				//uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser( qStrings.getEmplid() );			
			} else {
				uSession = UserSession.getUserSession(getRequest()); 
				user = uSession.getUser();				
			}

	       if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        String downloadExcel = getRequest().getParameter("downloadExcel");
	        
	 
	        
	        RBUSHandler handler = new RBUSHandler();          
	        P4Handler p4handler = new P4Handler();          
	        
	        String waveweek = getRequest().getParameter("waveweek");
	        String[] wavekeesarr = null;
	        
	        if (waveweek != null) {
	             wavekeesarr = waveweek.split("-");
	        }
	        String week_id = null;
	        String wave_id = "1";
	        if (wavekeesarr != null) {
	            wave_id = wavekeesarr[0];
	            week_id = wavekeesarr[1];
	        } else {
	            if (getRequest().getParameter("wave_id") != null && getRequest().getParameter("week_id") != null) {
	                wave_id = getRequest().getParameter("wave_id");
	                week_id = getRequest().getParameter("week_id");          
	            }
	        }        
//	        if(week_id == null) {
	 //           P4TrainingWeek week = p4handler.getCurrentWeek();
	  //          if(week !=null) {
	  //              week_id = week.getWeek_id();
	  //              wave_id = "1";
	  //          }else{
	  //              week_id = "0";
	  //          }
	  //      }
	        
	        List rooms = new ArrayList();
	        List weeks = p4handler.getClassWeeks();
	       System.out.println(wave_id+"idd");
	       System.out.println(weeks+"gfjghjgj");
	        if(week_id !=null){
	            rooms = p4handler.getClassRooms(week_id,wave_id);
	        }
	        p4ClassRoomReportForm.setWeeks(weeks);
	        p4ClassRoomReportForm.setWeek_id(week_id);
	        p4ClassRoomReportForm.setWave_id(wave_id);
	       
	        getRequest().setAttribute("p4ClassRoomReportForm", p4ClassRoomReportForm);
	        
	        P4ClassRoomReportWc main = new P4ClassRoomReportWc(rooms, AppConst.EVENTID_RBU);       
	        main.setP4ClassRoomReportForm(p4ClassRoomReportForm);               
	                
	        if ("true".equalsIgnoreCase(downloadExcel)) {			
				WebPageComponent page;
	            page = new BlankTemplateWpc(main);
	            
	            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
	            getRequest().setAttribute("exceldownload","true"); 
	                                    
	            getResponse().addHeader("content-disposition","attachment;filename=\"P4 Class Room Report.xls\"");
				
				getResponse().setContentType("application/vnd.ms-excel");	
				getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
				getResponse().setHeader("Pragma","public");		
	            
	            return new String("successXls");
	        }
	        
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_P4,"P4REPORT" );  
	        
	        page.setMain(main);
	        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
	        
	        return new String("success");  
			}
			catch (Exception e) {
				Global.getError(getRequest(),e);
				return new String("failure");
				}

	    
	    }
	 
	 private Date addDaysToDate(Date date, int daysToAdd) {
	        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	        Calendar now = Calendar.getInstance();
	        now.setTime(date);
	        now.add(Calendar.DAY_OF_MONTH, daysToAdd);
	        return now.getTime();
	    }
	 
	
	 
//	 public String p4traineetablemap(P4traineetablemapForm form)
	 public String p4traineetablemap()
	    {
	        try{
		    UserSession uSession;        
			uSession = UserSession.getUserSession(getRequest());        
	        AppQueryStrings qStrings = new AppQueryStrings();        
			User user;
			
	        uSession = UserSession.getUserSession(getRequest());
			
			if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
				getRequest().getSession().invalidate();		
				//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
				//uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser( qStrings.getEmplid() );			
			} else {
				uSession = UserSession.getUserSession(getRequest()); 
				user = uSession.getUser();				
			}

	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        String day = getRequest().getParameter("day");
	        String product = getRequest().getParameter("product");
	        String room = getRequest().getParameter("room");
	        String room_id = getRequest().getParameter("room_id");
	        String table_id = getRequest().getParameter("table_id");
	        String class_id = getRequest().getParameter("class_id");
	        String week_id =  getRequest().getParameter("week_id");
	        String wave_id =  getRequest().getParameter("wave_id");
	        
	        
	        p4traineetablemapForm.setDay(day);
	        p4traineetablemapForm.setProduct(product);
	        p4traineetablemapForm.setRoom(room);
	        p4traineetablemapForm.setTable_id(table_id);
	        p4traineetablemapForm.setClass_id(class_id);
	        p4traineetablemapForm.setWeek_id(week_id);
	        p4traineetablemapForm.setRoom_id(room_id);
	        p4traineetablemapForm.setWave_id(wave_id);
	        
	        P4Handler p4handler = new P4Handler();    
	        RBUSHandler handler = new RBUSHandler();  
	 //       List ts = handler.getEmpListByTalbe(class_id, table_id);
	        List tsnew = p4handler.getEmpListByTable(class_id, table_id, room_id, week_id, wave_id);
	        
	   //     List guest = handler.getGuestListByTable(class_id, table_id);
	        List guestnew = p4handler.getGuestListByTable(class_id, table_id);
	        	
	        
	        P4TraineeTableMapWc main = new P4TraineeTableMapWc(tsnew, guestnew, AppConst.EVENTID_RBU);  
	         
	        main.setAvailableTables(p4handler.getTables(class_id));
	        main.setAvailableRooms(p4handler.getRooms(class_id));
	        main.setP4traineetablemapForm(p4traineetablemapForm);
	        
	        //main.setRBUClassRoomReportForm(form);               
	        
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_RBU,"P4REPORT" ); 
	        //BlankTemplateWpc page = new BlankTemplateWpc(main); 
	        page.setMain(main);
	        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
	       			
	        return new String("success");
	        }
	        catch (Exception e) {
	        	Global.getError(getRequest(),e);
	        	return new String("failure");
	        	}

	    }
	 
	  public String p4traineedtableupdate()
	    {
		    try{
	        UserSession uSession;        
			uSession = UserSession.getUserSession(getRequest());        
	        AppQueryStrings qStrings = new AppQueryStrings();        
			User user;
			
	        uSession = UserSession.getUserSession(getRequest());
			
			if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
				getRequest().getSession().invalidate();		
				getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
				user = uSession.getUser( qStrings.getEmplid() );			
			} else {
				uSession = UserSession.getUserSession(getRequest()); 
				user = uSession.getUser();				
			}

	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        String class_id = getRequest().getParameter("class_id");
	        String room_id = getRequest().getParameter("room_id");
	        String table_id = getRequest().getParameter("table_id");
	        String wave_id = getRequest().getParameter("wave_id");
	        String week_id = getRequest().getParameter("week_id");
	        
	        String tts = getRequest().getParameter("emplist");
	        String gts = getRequest().getParameter("guestlist");
	        
	        String [] glist;
	        String [] tlist;
	        
	        Map gmap = new HashMap();
	        Map tmap = new HashMap();
	        
	        
	        if(tts != null){
	           tlist = tts.split(",");
	           for ( int i = 0; i< tlist.length; i++){
	                tmap.put(tlist[i], getRequest().getParameter("t" + tlist[i]));
	           }
	        }
	        if(gts != null){
	           glist = gts.split(",");
	           
	           for ( int i = 0; i< glist.length; i++){
	                gmap.put(glist[i], getRequest().getParameter("g" + glist[i]));
	           }
	        }
	        //to do - empty user
	        P4Handler p4handler = new P4Handler();    

	        RBUSHandler handler = new RBUSHandler();
	        p4handler.updateTable(gmap, tmap, class_id, user.getId(), room_id, table_id);

	        getRequest().setAttribute("wave_id",wave_id);
	        getRequest().setAttribute("week_id",week_id);
	        /*return new String("success");*/
	      return new String ("success");
		    }
		    catch (Exception e) {
		    	Global.getError(getRequest(),e);
		    	return new String("failure");
		    	}

	    }
	  	
	    public String p4scereport()
	    {
	       try{
	    	UserSession uSession;        
			uSession = UserSession.getUserSession(getRequest());        
	        AppQueryStrings qStrings = new AppQueryStrings();        
			User user;
			
	        uSession = UserSession.getUserSession(getRequest());
			
			if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
				getRequest().getSession().invalidate();		
				//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
				//uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser( qStrings.getEmplid() );			
			} else {
				uSession = UserSession.getUserSession(getRequest()); 
				user = uSession.getUser();				
			}

	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        String examdate = getRequest().getParameter("examdate");
	        String examname = getRequest().getParameter("examname");
	        boolean fetchflag = true; 
	        if (examdate == null) {
	            examdate = "all";
	            fetchflag = false;
	        }
	        if (examname == null) {
	            examname = "all";
	        }
	        
	        P4Handler p4handler = new P4Handler();    
	        List scedates = p4handler.getSceDates(); 
	        List sceexamnames = p4handler.getSceExamnames(); 
	        List sceMap = p4handler.getSceMap();
	        List report = p4handler.getSceReport(examdate,examname);
	        
	        P4SceReportWc main = new P4SceReportWc();
	        main.setAllClassnames(sceexamnames);
	        main.setAllDates(scedates);
	        main.setAllDateClassMap(sceMap);
	        main.setSelectedDate(examdate);
	        main.setSelectedExam(examname);
	        if (fetchflag) {
	            main.setReportList(report);
	        } else {
	            main.setReportList(new ArrayList());
	        }
	        
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_P4,"P4SCEREPORT" ); 
	        page.setMain(main);
	        
	        String downloadExcel = getRequest().getParameter("downloadExcel");
	        
	        if ("true".equalsIgnoreCase(downloadExcel)) {	
	            main.setExcelDownload(true);		
				WebPageComponent page2;
	            page2 = new BlankTemplateWpc(main);
	            
	            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page2 );
	            getRequest().setAttribute("exceldownload","true"); 
	                                    
	            getResponse().addHeader("content-disposition","attachment;filename=\"P4 Class Room Report.xls\"");
				
				getResponse().setContentType("application/vnd.ms-excel");	
				getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
				getResponse().setHeader("Pragma","public");		
	            
	            return new String("successXls"); 
	        } else {
	            page.setOnLoad("dropInit();");
	        }

	        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
	       			
	        return new String("success");
	       }
	       catch (Exception e) {
	    	   Global.getError(getRequest(),e);
	    	   return new String("failure");
	    	   }

	    }


		


	}
