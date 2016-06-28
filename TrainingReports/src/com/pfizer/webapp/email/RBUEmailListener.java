package com.pfizer.webapp.email;

import com.pfizer.db.BatchJob;
import com.pfizer.db.Employee;
import com.pfizer.db.Product;
import com.pfizer.db.RBUEmailReminder;
import com.pfizer.db.RBUEmailReminderHistory;
import com.pfizer.hander.AuditHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.RBUEmployeeHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.processor.AttendanceProcessor;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.JNDIUtil;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.MailUtil;
//import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import weblogic.management.timer.Timer;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.Session;
import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*import weblogic.common.T3Connection;
import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.internal.BootStrap;
import weblogic.management.runtime.ServerRuntimeMBean;*/
import javax.management.timer.Timer;

public class RBUEmailListener implements ServletContextListener, NotificationListener {
	protected static final Log log = LogFactory.getLog( EmailListener.class );
	private Timer timer;
	private Integer notificationId;
	//private static final long PERIOD = 60*Timer.ONE_MINUTE;
   // private static final long PERIOD = Timer.ONE_MINUTE;
	public static boolean runFlag = true;

    public void handleNotification(Notification arg0, Object arg1) {
		log.info("RBUEmailListener.handleNotification");
        System.out.println("RBUEmailListener.handleNotification");
		try {
			boolean flag = false;
            //boolean flag = true;
			flag = checkEmailRunJob();
            String serverName = getServerName();
            String sServerName =null;// BootStrap.getServerName();
           // System.out.println("Host name ########### " + java.net.InetAddress.getLocalHost().getHostName());
			if (flag) {
                AuditHandler au = new AuditHandler();
				System.out.println("time to run");
                String hostName = java.net.InetAddress.getLocalHost().getHostName();
                HashMap map = au.getEnvParameters();
                String env = "";
                String host = "";
                for(Iterator iter=map.keySet().iterator();iter.hasNext();){
                 env = iter.next().toString();
                 host = (String)map.get(env);
                 System.out.println("Host >>>>>>>> " + host + "Env" + env) ;
                }
                //System.out.println("Host name for yes flag #################  " + hostName);
                if(env.equals("INT") && hostName.equalsIgnoreCase(host)){
                System.out.println("Host name for yes flag #################  " + hostName);                    
                // Integration /Dev
                    runEmails();
                }
                else if(env.equals("STG") && hostName.equalsIgnoreCase(host)){
                System.out.println("Host name for yes flag #################  " + hostName);                    
                // Integration /Dev
                    runEmails();
                }
                else if(env.equals("PROD") && hostName.equalsIgnoreCase(host)){
                System.out.println("Host name for yes flag #################  " + hostName);                    
                // Integration /Dev
                    runEmails();
                }
                else{
                    System.out.println("Incorrect configuration present. So email jobs are not run. Env >>> " + env  + "Host name >>>> " + host);                    
                }
              //  if(databaseURL.indexOf("TSP") > 0){
                // Production
                 //   runEmails();
              //  }
              //  if(databaseURL.indexOf("TSD") > 0){
                    // Staging
               //     runEmails();
              //  }
                // Update the job status to active
                au.updateRBUEmailReminderJob(RBUEmailReminder.STATUS_ACTIVE);
				System.out.println("Done sending all emails");
			} else {
				System.out.println("Not running email job.");
			}

		} catch (Exception e) {
			log.error(e,e);
		}
	}

	private  boolean checkEmailRunJob() {

		AuditHandler au = new AuditHandler();
        RBUEmailReminder job = au.getEmailBatchJobForRBU();
        // Get the historyId to check whether this is the first run
        boolean firstRun = false;
        long historyId = au.getEmailHistoryId();
        if(historyId == 0){
            firstRun = true;
        }
		log.info(job);
		try {
                System.out.println("Subject ########### " + job.getSubject());
                log.debug(java.net.InetAddress.getLocalHost().getHostName());
            
            if ( job != null ) {
                // check if force run is set
                log.info("Job Status:" + job.getStatus());
                System.out.println("Job Status:" + job.getStatus());
                // check if job is active
                if (RBUEmailReminder.STATUS_ACTIVE.equals(job.getStatus())) {
                    System.out.println("Job Status is active");
                    Calendar now = Calendar.getInstance();
                    // check if the job was run before max days
                    Date today = now.getTime();
                    //Date today = new Date();
                    Date startDate = job.getStartDate();
                    Calendar start = Calendar.getInstance();
                    start.setTime(startDate);
                    System.out.println("############## Date start Date" + getDisplayDate(startDate) + " flag " + firstRun  + "today " + getDisplayDate(today));
                   // System.out.println("############## Date start Date" + start.getTimeInMillis() + " flag "  + "today " + cal.getTimeInMillis());
                    if(firstRun){
                        if(now.get(Calendar.DAY_OF_MONTH) == start.get(Calendar.DAY_OF_MONTH) 
                        && now.get(Calendar.MONTH) == start.get(Calendar.MONTH)
                        && now.get(Calendar.YEAR) == start.get(Calendar.YEAR)
                        && now.get(Calendar.HOUR_OF_DAY) == start.get(Calendar.HOUR_OF_DAY)
                        ){
                            System.out.println("true for start date ####################");
                            // Update the job status to running
                            au.updateRBUEmailReminderJob(RBUEmailReminder.STATUS_RUN);
                            return true;
                        }
                    }
                    else{
                        // Convert days to minutes
                        double frequencyDays = job.getDays()*60*24;
                        int minutesToAdd =  new Integer((int) round(frequencyDays,1)).intValue();
                        System.out.println("Minutes >>>>>>>>>>>>>>>>>>" + minutesToAdd);
                        // Get the  latest run date from the history table
                        Date lastRun = au.getLatestRunDate();
                        System.out.println("Last run ############### " + lastRun.toString());
                        //Date formattedDate = (new SimpleDateFormat("MM/dd/yyyy HH:mm aaa")).parse(lastRun);
                        // Add frequencyDays to this
                        Calendar future = Calendar.getInstance();
                        Date addedDate = addDaysToDate(lastRun, minutesToAdd);
                        future.setTime(addedDate);
//                        System.out.println("Date matches current date  " + getDisplayDate(today));
  //                      System.out.println("Date matches current date + days  " + getDisplayDate(addedDate));
                       System.out.println("Day " + now.get(Calendar.DAY_OF_MONTH) + "Month " + now.get(Calendar.MONTH) + "Year " + now.get(Calendar.YEAR) + "Hour " + now.get(Calendar.HOUR_OF_DAY));
                       System.out.println("Future Day " + future.get(Calendar.DAY_OF_MONTH) + "Month " + future.get(Calendar.MONTH) + "Year " + future.get(Calendar.YEAR) + "Hour " + future.get(Calendar.HOUR_OF_DAY));
                       // if(today.compareTo(newDate) == 0){
                         //if(getDisplayDate(today).equals(getDisplayDate(addedDate))){
                        if(now.get(Calendar.DAY_OF_MONTH) == future.get(Calendar.DAY_OF_MONTH) 
                        && now.get(Calendar.MONTH) == future.get(Calendar.MONTH)
                        && now.get(Calendar.YEAR) == future.get(Calendar.YEAR)
                        && now.get(Calendar.HOUR_OF_DAY) == future.get(Calendar.HOUR_OF_DAY)
                        ){      
                            System.out.println("Returned true ##############");
                            // Update the job status to running
                            au.updateRBUEmailReminderJob(RBUEmailReminder.STATUS_RUN);
                            return true;
                        }
                        else{
                            System.out.println("The job was run with in the frequency of days. Its not time to run again.");
                        }
                    }
                    
                }
                
                
            }
            else{
                System.out.println("The job is null !!!!.");
            }
        } catch (Exception e) {
			log.error(e,e);
		}

		return false;
	}
    
    public static double round(double d, int decimalPlace){
		    BigDecimal bd = new BigDecimal(Double.toString(d));
		    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		    return bd.doubleValue();
		  }
    
     /**
     * This method adds days to a given date
     */
    private Date addDaysToDate(Date date, int daysToAdd) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, daysToAdd);
        return now.getTime();
    }

    private String getDisplayDate(Date date){
     DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aaa");
     String ret = "";
    ret = sdf.format(date);
    return ret;
   }
   
    private String getDisplayDateWithSecs(Date date){
     DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aaa");
     String ret = "";
    ret = sdf.format(date);
    return ret;
   }
   
   
    private String getDisplayDateSimple(Date date){
     DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
     String ret = "";
    ret = sdf.format(date);
    return ret;
   }
	private void sendEmail(String msg,String to) {
		try {
			
			
             ReadProperties readProperties = new ReadProperties("EmailHandlingProperties.propeties");
            String email_bcc = "";
            String cc = "";
            //String subject = "ACTION REQUIRED: PSCPT Training Report";
            String subject = "";
            AuditHandler au = new AuditHandler();
            HashMap map = au.getEnvParameters();
            String env = "";
            for(Iterator iter=map.keySet().iterator();iter.hasNext();){
            env = iter.next().toString();
            System.out.println("Host >>>>>>>> " + "Env" + env) ;
            }
            if(env.equalsIgnoreCase("INT")){
            // Integration /Dev
                to = readProperties.getValue("EMAIL_REMINDER_NOT_COMPLETE_TO_DEV_INT_STG");
                cc = readProperties.getValue("EMAIL_REMINDER_NOT_COMPLETE_CC_DEV_INT_STG");
                // For testing 
                //to = "arpit@tgix.com";
               //to = "jeevan@tgix.com";
               // email_bcc = "rbu.trt@tgix.com";
            }
             if(env.equalsIgnoreCase("PROD")){
                // Production
                email_bcc = readProperties.getValue("EMAIL_REMINDER_NOT_COMPLETE_BCC_PROD");
            }
             if(env.equalsIgnoreCase("STG")){
                // Staging
                to = readProperties.getValue("EMAIL_REMINDER_NOT_COMPLETE_TO_DEV_INT_STG");
                cc = readProperties.getValue("EMAIL_REMINDER_NOT_COMPLETE_CC_DEV_INT_STG");
            }
            RBUEmailReminder email = au.getEmailBatchJobForRBU();
            subject = email.getSubject();
            System.out.println("Email value here is  ############## TO " + to + "Email BCC " + email_bcc);
            MailUtil.sendMessage("TrainingLogistics@pfizer.com",to,cc,email_bcc,subject,msg,"text/html","trMailSession");
			//MailUtil.sendMessage("TrainingLogistics@pfizer.com",to,rmcc,armcc,"joe@tgix.com",subject,msg,"text/html","trMailSession");
		} catch (Exception e) {
			log.error(e,e);
		}
	}

	public  void runEmails() {
		ServiceFactory factory = Service.getServiceFactory();
		RBUEmployeeHandler eHandler = factory.getRBUEmployeeHandler();
        UserSession uSession;
        HashMap reportToEmployees = eHandler.getEmployeesForEmail();
        AuditHandler au = new AuditHandler();
        long historyId = au.getEmailHistoryId() + 1;
       int counter = 0;
        try{
            // Iterate over the hash map and create a list of employees that is not completed
            for(Iterator iter=reportToEmployees.keySet().iterator();iter.hasNext();){
                counter++;
                String emplidTemp = iter.next().toString();
                String reportsToEmail = "";
                System.out.println("########### Reports to emplid in scheduler" + emplidTemp);
                //LoggerHelper.logSystemDebug("########### Reports to emplid in scheduler" + emplidTemp);
                List notCompleteEmployees = new ArrayList();
                List employeesNotCompeted = (List)reportToEmployees.get(emplidTemp);
                System.out.println("List of emplyees #### " + employeesNotCompeted.size());
                Iterator listIter = employeesNotCompeted.iterator();
                while(listIter.hasNext()){
                    Employee currentEmployee = (Employee)listIter.next();
                    String emplid = currentEmployee.getEmplId();
                    reportsToEmail = currentEmployee.getReportsToEmail();
                        notCompleteEmployees.add(currentEmployee);
                }
                 Thread.sleep(100);
                // Insert the history records
                insertHistoryEmailReminder(notCompleteEmployees, emplidTemp, historyId);
                 generateEmailByProductUser(notCompleteEmployees,reportsToEmail);
                System.out.println("Total processed records ########## " + counter);
            }
        }catch(Exception ex){
        }
        
	}

	private void generateEmailByProductUser(List incompleteEmployees, String reportsToEmail) {
		ServiceFactory factory = Service.getServiceFactory();
		EmployeeHandler eHandler = factory.getEmployeeHandler();
        AuditHandler au = new AuditHandler();
        String link = "";
         HashMap map = au.getEnvParameters();
          String env = "";
          for(Iterator iter=map.keySet().iterator();iter.hasNext();){
          env = iter.next().toString();
           System.out.println("Host >>>>>>>> " + "Env" + env) ;
          }
           // System.out.println("Size ###### " + user.getEmplid());
            if ( incompleteEmployees.size() > 0 ) {
    
                // prepare district url
                StringBuffer sb = new StringBuffer();
                for (Iterator it = incompleteEmployees.iterator(); it.hasNext(); ) {
                    Employee emp = (Employee)it.next();
                    String emplid = emp.getEmplId();
                 //   System.out.println("Emplid ###### " + emplid);
                    if(env.equalsIgnoreCase("INT")){
                        // Integration
                       link = "<br>" + "<a href='http://trt-tst.pfizer.com/TrainingReports/overview/getEmployeeDetails?emplid=" + emplid +  "'>" + emp.getLastName() +", "+ emp.getFirstName() + "</a>";
                    }
                    if(env.equalsIgnoreCase("PROD")){
                        // Production
                       link = "<br>" + "<a href='http://trt.pfizer.com/TrainingReports/overview/getEmployeeDetails?emplid=" + emplid +  "'>" + emp.getLastName() +", "+ emp.getFirstName() + "</a>";
                    }
                    if(env.equalsIgnoreCase("STG")){
                        // Staging
                       link = "<br>" + "<a href='http://trt-stg.pfizer.com/TrainingReports/overview/getEmployeeDetails?emplid=" + emplid +  "'>" + emp.getLastName() +", "+ emp.getFirstName() + "</a>";
                    }
                //	sb.append("<br>" + "<a href='http://trt.pfizer.com/TrainingReports/overview/listreport.do?type=overall&section=Not%20Complete&productCode=" + productCode + "&email=true</a>");
                    sb.append(link);
              //      System.out.println("Link ################## " + link);
                   
                }
                //System.out.println("Creating the message ############## " + sb.toString());
                String sendMessage =  topMessage + sb.toString() + bottomMessage;
                //System.out.println("sendMessage  ###### " + sendMessage);
                //sendEmail(sendMessage,"jeevan@tgix.com");
                sendEmail(sendMessage,reportsToEmail);
               // au.insertEmailAudit(user.getEmplid());
             //  System.out.println("sent emails to this user:" + user.getEmplid());
            } else {
                System.out.println("no need to send emails to this user:" + reportsToEmail);
            }

	}
    private void insertHistoryEmailReminder(List incompleteEmployees, String emplid, long historyId){
        AuditHandler au = new AuditHandler();
        RBUEmailReminderHistory history = null;
        for (Iterator it = incompleteEmployees.iterator(); it.hasNext(); ) {
                history = new RBUEmailReminderHistory();
                Employee emp = (Employee)it.next();
                String reportsEmplid = emp.getEmplId();
                history.setReportsEmplId(reportsEmplid);
                String managerEmplid = emplid;
                history.setManagerEmplId(managerEmplid);
                history.setJobId(1);
                history.setHistoryId(historyId);
                //System.out.println("Last record processed ############## " + reportsEmplid + " for manager  " + managerEmplid);
                au.insertRBUEmailReminderHistory(history);
        }       
    }
	
    public void contextDestroyed(ServletContextEvent sce) {
		log.info("RBUEmailListener.contextDestroyed");
		try {
            timer.stop();
            timer.removeNotification(notificationId);
            log.info(">>> timer stopped.");
		} catch (InstanceNotFoundException e) {
           log.error(e,e);
		}
    }

    public void contextInitialized(ServletContextEvent sce) {
		log.info("RBUEmailListener.contextInitialized");

		// Instantiating the Timer MBean
		timer = new Timer();
        RBUSHandler handler = new RBUSHandler();
        AuditHandler au = new AuditHandler();
        // RBUEmailReminder job = au.getEmailBatchJobForRBU();
        // Get the historyId to check whether this is the first run
        boolean firstRun = false;
       // long historyId = au.getEmailHistoryId();
       // if(historyId == 0){
         //   firstRun = true;
       // }
       /* Date timerTriggerAt = new Date();
        long minutesToAdd = 60;
        if(job != null){
            if(firstRun){
                Date triggerDate = job.getStartDate();
                timerTriggerAt = triggerDate;
            }
            else{
                    Date lastRun = au.getLatestRunDate();
                    if(lastRun != null){
                        timerTriggerAt = lastRun;
                    }
            }
            double frequencyDays = job.getDays()*60*24;
            minutesToAdd =  new Long((long) round(frequencyDays,1)).longValue();
        }
        System.out.println("Minutes >>>> " + minutesToAdd);
        */
        //long PERIOD = minutesToAdd*Timer.ONE_MINUTE;
        long PERIOD = 60*Timer.ONE_MINUTE;
        System.out.println("Period *************** " + PERIOD);
		// Registering this class as a listener
		timer.addNotificationListener(this, null, "some handback object");

		// Adding the notification to the Timer and assigning the
		// ID that the Timer returns to a variable
		Date timerTriggerAt = new Date((new Date()).getTime() + 5000L);
		notificationId = timer.addNotification("oneMinuteTimer",
						 "a recurring call", this,
						 timerTriggerAt, PERIOD);
		timer.start();
        System.out.println(">>> timer started." +timerTriggerAt);
		log.info(">>> timer started.");
	}


	/*private static  String getServerName(){
		String serverName="";
		MBeanHome home = null;
		String url = "t3://localhost:8252";
		String username = "weblogic";
		String password = "weblogic";
		ServerRuntimeMBean serverRuntime = null;
		Set mbeanSet = null;
		Iterator mbeanIterator = null;
		try {
			Environment env = new Environment();
			env.setProviderUrl(url);
			env.setSecurityPrincipal(username);
			env.setSecurityCredentials(password);
			Context ctx = env.getInitialContext();
			home = (MBeanHome) ctx.lookup(MBeanHome.ADMIN_JNDI_NAME);
        } catch (Exception e){
           log.error(e,e);
        } try {
           mbeanSet = home.getMBeansByType("ServerRuntime");
		   mbeanIterator = mbeanSet.iterator();
           serverRuntime = (ServerRuntimeMBean)mbeanIterator.next();
		} catch (Exception e) {
			log.error(e,e);
		}

		if(serverRuntime!=null )serverName=serverRuntime.getName();

		return serverName;
   }*/
	
    private static String getServerName(){
         String serverName = "";   
         try{   
            
             Hashtable env = new Hashtable();  
             env.put(Context.PROVIDER_URL,"t3://localhost:7001");  
             env.put(Context.INITIAL_CONTEXT_FACTORY  
                ,"weblogic.jndi.WLInitialContextFactory");  
            env.put(Context.SECURITY_CREDENTIALS,"weblogic");  
            env.put(Context.SECURITY_PRINCIPAL,"weblogic");  
            Context ctx = new InitialContext(env);  
            /*MBeanHome home = (MBeanHome)ctx.lookup(MBeanHome.ADMIN_JNDI_NAME);  
            Set set = home.getMBeansByType("ServerRuntime");  
            Iterator itr = set.iterator();  
            while(itr.hasNext())  
            {  
                ServerRuntimeMBean mbean = (ServerRuntimeMBean) itr.next(); 
                serverName =  mbean.getName().toString();
                System.out.println(mbean.getName().toString());  
            }*/
         }
         catch(Exception e){
            log.error(e,e);
         }
         
         return serverName;
   
    }

	
	private String topMessage = "<html> " +
        " " +
        "<head> " +
        "<title>Dear Manager, </title> " +
        "<style> " +
        "<!-- " +
        " /* Font Definitions */ " +
        " @font-face " +
        "	{font-family:Wingdings; " +
        "	panose-1:5 0 0 0 0 0 0 0 0 0;} " +
        "@font-face " +
        "	{font-family:Tahoma; " +
        "	panose-1:2 11 6 4 3 5 4 4 2 4;} " +
        "@font-face " +
        "	{font-family:Palatino; " +
        "	panose-1:0 0 0 0 0 0 0 0 0 0;} " +
        " /* Style Definitions */ " +
        " p.MsoNormal, li.MsoNormal, div.MsoNormal " +
        "	{margin:0in; " +
        "	margin-bottom:.0001pt; " +
        "	font-size:12.0pt; " +
        "	font-family:'Times New Roman';} " +
        " p.MsoNormalItalic, li.MsoNormalItalic, div.MsoNormalItalic " +
        "	{margin:0in; " +
        "	margin-bottom:.0001pt; " +
        "	font-size:12.0pt; " +
        "	font-style:italic; " +
        "	font-family:'Times New Roman';} " +
        "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate " +
        "	{margin:0in; " +
        "	margin-bottom:.0001pt; " +
        "	font-size:8.0pt; " +
        "	font-family:Tahoma;} " +
        "span.EmailStyle15 " +
        "	{font-family:Arial; " +
        "	color:windowtext;} " +
        "ins " +
        "	{text-decoration:none;} " +
        "span.msoIns " +
        "	{text-decoration:underline;} " +
        "span.msoDel " +
        "	{text-decoration:line-through; " +
        "	color:red;} " +
        "@page Section1 " +
        "	{size:8.5in 11.0in; " +
        "	margin:1.0in 1.25in 1.0in 1.25in;} " +
        "div.Section1 " +
        "	{page:Section1;} " +
        " /* List Definitions */ " +
        " ol " +
        "	{margin-bottom:0in;} " +
        "ul " +
        "	{margin-bottom:0in;} " +
        "--> " +
        "</style> " +
        " " +
        "</head> " +
        " " +
        "<body lang=EN-US> " +
        "<div class=Section1> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>Dear Manager, </span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>&nbsp;</span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>This is a follow-up to " +
        "the </span><span style='font-family:Palatino'>RBU Training Reports e-mail that " +
        "was sent earlier.  The report has identified that you have colleagues that still " +
        "require specific action to complete RBU Product  Training.  The requirements for " +
        "completing RBU Product training include: </span></p> " +
        " " +
        "<p class=MsoNormal>&nbsp;</p> " +
        "<p class=MsoNormal style='margin-left:.5in;text-indent:-.5in'><span " +
        "style='font-family:Symbol'>&nbsp;&nbsp;&nbsp;·<span style='font:7.0pt 'Times New Roman''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
        "</span></span><span style='font-family:Palatino'>Completion of all applicable Pedagogue Exams (*)</span></p> " +
        " " +
        "<p class=MsoNormal style='margin-left:.5in;text-indent:-.5in'><span " +
        "style='font-family:Symbol'>&nbsp;&nbsp;&nbsp;·<span style='font:7.0pt 'Times New Roman''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
        "</span></span><span style='font-family:Palatino'>Completion of  Sales Call Evaluation, if applicable</span></p> " +
        " " +
        "<p class=MsoNormal>&nbsp;</p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>Please click on the link(s) " +
        "below to be directed to the specific details.</span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>&nbsp;</span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>Please note that you will " +
        "continue to receive reminder e-mails until all of your colleague's status is logged " +
        "as 'complete'.</span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>&nbsp;</span></p> " +
        " " +
        "<p class=MsoNormal><span style='font-family:Palatino'>";
        
        
        String bottomMessage = "</p> " +
        " " +
        "<p class=MsoNormal>&nbsp;</p> " +
        
        "<p class=MsoNormalItalic><span style='font-family:Palatino'>(*) Please note that as the  " +
        "immediate supervisor, you are responsible for providing coaching to those individuals " +
        "who received an exam score of 80 or less." +
        "</span></p> " +
        "<p class=MsoNormal>&nbsp;</p> " +
        
        "<p class=MsoNormal><span style='font-family:Palatino'>Note: Individuals who return from Short Term Disability or reassigned to new products by their manager between May 1st to June 1st should have their manager contact <a href=\"mailto:mark.yniguez@pfizer.com\">Mark Yniguez</a> for Lateral Product Training.  The manager should include the following in their e-mail to Mark:  1)  Name of colleague 2) Their role and 3)  Which products they need to be trained on.</span></p>" +
        
        "<p class=MsoNormal>&nbsp;</p> " +
 
 
        "<p class=MsoNormal>Instructions for individuals returning from STD after June 1st will be sent out soon.</p> " +
        "<p class=MsoNormal>&nbsp;</p> " +
        
        "<p class=MsoNormal>Questions?</p> " +
        
        "<p class=MsoNormal>&nbsp;</p> " +
        "<p class=MsoNormal>&nbsp;&nbsp;Please contact:</p> " +
        
        "<p class=MsoNormal>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"mailto:traininglogistics@pfizer.com\">traininglogistics@pfizer.com</a></p> " +
         
        "<p class=MsoNormal>&nbsp;&nbsp;&nbsp;&nbsp;(866) 4LD-2WIN</p> " + 
       
        " " +
        
        "</div> " +
        " " +
        "</body> " +
        " " +
        "</html> ";

}
