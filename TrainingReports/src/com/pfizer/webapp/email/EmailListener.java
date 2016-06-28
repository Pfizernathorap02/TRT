package com.pfizer.webapp.email; 

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.timer.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.BatchJob;
import com.pfizer.db.Employee;
import com.pfizer.db.Product;
import com.pfizer.hander.AuditHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;

public class EmailListener implements ServletContextListener, NotificationListener { 
	protected static final Log log = LogFactory.getLog( EmailListener.class );
	private Timer timer;
	private Integer notificationId;
	private static final long PERIOD = 60*Timer.ONE_MINUTE;
	public static boolean runFlag = true;
	
    public void handleNotification(Notification arg0, Object arg1) {


		log.info("EmailListener.handleNotification");
		
		try {
			
			boolean flag = false;
			flag = checkEmailRunJob();
			
			if (flag) {
				System.out.println("time to run");
				runEmails();
				AuditHandler au = new AuditHandler();
				BatchJob job = au.getEmailBatchJob();
				job.setLastRun( new Date() );
				job.setStatus(BatchJob.STATUS_ACTIVE);
				au.updateBatchJob(job);
				System.out.println("Done sending all emails");
			} else {
				System.out.println("Not running email job.");
			}
			
		} catch (Exception e) {
			log.error(e,e);
		}
	}

	private boolean checkEmailRunJob() {
		
		// check correct server
		String doEmail = System.getProperty("doemail");
		
		if ("true".equals(doEmail)) {
			System.out.println("Correct Email Server");
		} else {
			System.out.println("Not Email Server Server");
			return false;
		}
		
		
		AuditHandler au = new AuditHandler();
		BatchJob job = au.getEmailBatchJob();
		log.info(job);
		try {
			log.debug(java.net.InetAddress.getLocalHost().getHostName());
		} catch (Exception e) {
			log.error(e,e);
		}	
		if ( job != null ) {
			// check if force run is set
			log.info("Job Status:" + job.getStatus());
			if (BatchJob.STATUS_FORCE.equals(job.getStatus()) ) {
				job.setStatus(BatchJob.STATUS_RUNNING);
				job.setComment("Starting forced job.");
				au.updateBatchJob( job );
				return true;
			}
			// check if job is active
			if ("A".equals(job.getStatus()) ) {
				Calendar cal = Calendar.getInstance();
				// check if it's Wednsday
				int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
				if ( dayofweek == Calendar.WEDNESDAY ) {
					log.info("It's WEDNESDAY");
					int hrofday = cal.get(Calendar.HOUR_OF_DAY);
					log.info("Hour of day:" + hrofday);
					
					// check 2hr gap
					if (hrofday == 2 || hrofday ==3) {
						log.info("time to run");
						// check if last run date was atleast 5 days ago.
						Calendar lastBatchJob = Calendar.getInstance();
						lastBatchJob.setTime(job.getLastRun());
						Date today = new Date();
						long diff = cal.getTimeInMillis() - lastBatchJob.getTimeInMillis();
						double days = diff/(24*60*60*1000);
						log.info("difference:" + days);
						if ( days > 6 ) {
							log.info("long enough");
							job.setStatus(BatchJob.STATUS_RUNNING);
							job.setLastRun( new Date() );
							job.setComment("Starting weekly job.");
							au.updateBatchJob( job );
							return true;
						} else {
							System.out.println("Ran earlier.");
						}
					} else {
						System.out.println("It's Not in the hr of 2am or 3am");								
					}
				} else {
					System.out.println("It's Not WEDNESDAY");			
				} 
			} 
		}		
		
		return false;
	}
		
	private void sendEmail(String msg,String to, String rmcc, String armcc) {
		try {
			if ( to.equals(rmcc) ) {
				rmcc = armcc;
				armcc = null;
			}

			String[] email_to = new String[1];
			email_to[0] = to;
			String[] email_cc = null;
			String[] email_bcc = new String[1];
			email_bcc[0] = "joe@tgix.com";
			
			if (!Util.isEmpty(rmcc) && !Util.isEmpty(armcc) ){
				email_cc = new String[2];
				email_cc[0] = rmcc;
				email_cc[1] = armcc;
			} else if (!Util.isEmpty(rmcc)) {
				email_cc = new String[1];
				email_cc[0] = rmcc;				
			} else {
				email_cc = new String[0];
			}
			
			String subject = "ACTION REQUIRED: FFT Training Report";
			//String sending = "to:" + to + "\nRm cc:" + rmcc+ "\nArm cc:" + armcc;
			//System.out.println(sending);
			MailUtil.sendMessage("TrainingLogistics@pfizer.com",email_to,email_cc,email_bcc,subject,msg,"text/html","trMailSession");
			//MailUtil.sendMessage("TrainingLogistics@pfizer.com",to,rmcc,armcc,"joe@tgix.com",subject,msg,"text/html","trMailSession");
		} catch (Exception e) {
			log.error(e,e);
		}
	}
	
	private void runEmails() {
		ServiceFactory factory = Service.getServiceFactory();
		EmployeeHandler eHandler = factory.getEmployeeHandler();
		
		// get all DM to check if they need emails sent to them
		Employee[] dmList = eHandler.getEmployeeByRole( "'DM'" );
		generateUserEmail(dmList,false,null);
		log.info("Finished with DM emails.");

		log.info("Doing RM emails now.");
		// RM without DM logic
		TerritoryHandler th = factory.getTerritoryHandler();
		List regionNoDm = th.getRegionNoDm();
		for (Iterator it = regionNoDm.iterator(); it.hasNext(); ) {
			Map curr = (Map)it.next();
			log.info((String)curr.get("REGION_CD"));
			UserFilter uFilter = new UserFilter();
			TerritoryFilterForm tff = new TerritoryFilterForm();
			uFilter.setClusterCode((String)curr.get("CLUSTER_CD"));
			tff.setArea((String)curr.get("AREA_CD"));
			tff.setRegion((String)curr.get("REGION_CD"));
			tff.setDistrict((String)curr.get("DISTRICT_ID"));
			uFilter.setFilterForm(tff);
			Employee emp = getRegionManager(null,uFilter,eHandler);
			Employee[] rmList = new Employee[1];
			rmList[0] = emp;
			// RM logic will only work 1 RM at a time
			generateUserEmail(rmList,true,tff);		
		}
				
	}
	
	private void generateUserEmail(Employee[] dmList, boolean regionManager, TerritoryFilterForm rmForm) {
		ServiceFactory factory = Service.getServiceFactory();
		UserFilter uFilter;
		UserSession uSession;
		TerritoryFilterForm tff = null;
		User user;
		int numList = dmList.length;
		//if (numList > 10) {
		//	numList = 10;
		//}
		
		for ( int i=0; i < numList; i++) {
			
			// if there is an error with 1 user, log and move on.
			try {			
				uFilter = new UserFilter();
				uSession = new UserSession();
				user = uSession.getUser(dmList[i].getEmplId());
				
				// if in region manager mode, use the TerritoryFilerFrom being passed
				// otherwise use the default DM filter.
				if (!regionManager) {
					tff = uSession.getNewTerritoryFilterForm();
				} else {
					tff = rmForm;
				}
				
				// setting User cluster code used for filters.
				uFilter.setClusterCode( user.getCluster() );
				
				// Array Of products to check for.
				List productEmail = new ArrayList();			
				for ( Iterator it = user.getProducts().iterator(); it.hasNext(); )	{
					Product prod = (Product)it.next();
					uFilter.setProdcut( prod.getProductCode() );
					uFilter.setClusterCode( user.getCluster() ) ;
					uFilter.setFilterForm( tff );
					
					ReportBuilder rb = new ReportBuilder(factory);
					OverallProcessor op = rb.getOverallProcessor(uFilter);
					int total = op.getTotalEmployees();
					int passed = op.getOverallPassedCount();
					int onleavecount = op.getOverallOnLeaveCount();
					// add onleave to pass so they don't get emailed.
					passed = passed + onleavecount;
							
					// total employee's must equal # or else someone must be not complete.
					if (total > passed) {
						productEmail.add(prod);
						log.info( "Must email this person with product code:" + prod.getProductCode());
					} else {
						log.info( "Do not have to email this person with product code:" + prod.getProductCode() );
						if (onleavecount > 0) {
							log.info( "\n\n\nDo not have to email this person with product code because of Onleave:" + prod.getProductCode() + " user ID:" + user.getEmplid());						
						}
					}			
				}
				
				// need to set the RM's employee district record, normally they are 
				// empty
				if (regionManager) {
					dmList[0].setDistrictId(tff.getDistrict());
				}
				
				generateEmailByProductUser(productEmail,user,dmList[i],uFilter);

			} catch (Exception e) {
				log.error(e,e);				
			}
		}	
	}
	
	private void generateEmailByProductUser(List productEmail, User user, Employee employee, UserFilter uFilter) {
		ServiceFactory factory = Service.getServiceFactory();
		EmployeeHandler eHandler = factory.getEmployeeHandler();
				
		if ( productEmail.size() > 0 ) {
			
			// prepare district url
			StringBuffer sb = new StringBuffer();
			String territoryString = "&UserFilterForm_area=" + Util.toEmpty(employee.getAreaCd());
			territoryString += "&UserFilterForm_region=" + Util.toEmpty(employee.getRegionCd());
			territoryString += "&UserFilterForm_district=" + Util.toEmpty(employee.getDistrictId());				
			for (Iterator it = productEmail.iterator(); it.hasNext(); ) {
				Product currProd = (Product)it.next();
				sb.append("<br>" + "<a href='http://trt.pfizer.com/TrainingReports/overview/listReport?type=overall&section=Not%20Complete&productCode=" + currProd.getProductCode() + "&email=true" + territoryString + "'>" + currProd.getProductDesc() + "</a>");
			}
			
			String sendMessage =  topMessage + sb.toString() + bottomMessage;
			// region manager
			Employee regionManager = getRegionManager(user,uFilter,eHandler);

			// assistant region manager
			Employee AregionManager = getARegionManager(user,uFilter,eHandler);
			AuditHandler au = new AuditHandler();
			if ( AregionManager != null && regionManager != null) {
				sendEmail(sendMessage,user.getEmail(),regionManager.getEmail(), AregionManager.getEmail());
				au.insertEmailAudit(user.getEmplid());
				if (!user.getEmplid().equals(regionManager.getEmplId())) {
					au.insertEmailAudit(regionManager.getEmplId());
				}
				au.insertEmailAudit(AregionManager.getEmplId());
			} else if ( regionManager != null) { 
				sendEmail(sendMessage,user.getEmail(),regionManager.getEmail(),"");
				au.insertEmailAudit(user.getEmplid());
				if (!user.getEmplid().equals(regionManager.getEmplId())) {
					au.insertEmailAudit(regionManager.getEmplId());
				}
			} else {
				sendEmail(sendMessage,user.getEmail(),"","");			
				au.insertEmailAudit(user.getEmplid());
			}				
			System.out.println("sent emails to this user:" + user.getEmplid());
		} else {
			System.out.println("no need to send emails to this user:" + user.getEmplid());
		}
	
	}
	
	private Employee getRegionManager(User user, UserFilter uFilter,EmployeeHandler eHandler) {
		Employee rmanager = eHandler.getRegionManager(uFilter);
		if ( rmanager != null ) {
			return rmanager;
		}
		return null;
	}
	private Employee getARegionManager(User user, UserFilter uFilter,EmployeeHandler eHandler) {
		Employee rmanager = eHandler.getARManager(uFilter);
		if ( rmanager != null ) {
			return rmanager;
		}
		return null;
	}
    public void contextDestroyed(ServletContextEvent sce) {
		log.info("EmailListener.contextDestroyed");
		try {
            timer.stop();
            timer.removeNotification(notificationId);
            log.info(">>> timer stopped.");
		} catch (InstanceNotFoundException e) {
           log.error(e,e);
		}
    }

    public void contextInitialized(ServletContextEvent sce) {
		log.info("EmailListener.contextInitialized");
		
		// Instantiating the Timer MBean
		timer = new Timer();

		// Registering this class as a listener
		timer.addNotificationListener(this, null, "some handback object");

		// Adding the notification to the Timer and assigning the
		// ID that the Timer returns to a variable
		Date timerTriggerAt = new Date((new Date()).getTime() + 5000L);
		notificationId = timer.addNotification("oneMinuteTimer",
						 "a recurring call", this, 
						 timerTriggerAt, PERIOD);
		timer.start();
		log.info(">>> timer started.");               
	}
	
/*
	private static  String getServerName(){
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
   } 

	*/
	private String topMessage = "<html> " +
" " +
"<head> " +
"<title>Dear District Manager, </title> " +
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
"the </span><span style='font-family:Palatino'>FFT Training Reports e-mail that " +
"was sent earlier.  The report has identified that you have colleagues that still " +
"require specific action to complete FFT Product Training.  The requirements for " +
"completing FFT Product Training include: </span></p> " +
" " +
"<p class=MsoNormal style='margin-left:.5in;text-indent:-.5in'><span " +
"style='font-family:Symbol'>&nbsp;&nbsp;&nbsp;·<span style='font:7.0pt 'Times New Roman''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
"</span></span><span style='font-family:Palatino'>Completion of Pedagogue Exams</span></p> " +
" " +
"<p class=MsoNormal style='margin-left:.5in;text-indent:-.5in'><span " +
"style='font-family:Symbol'>&nbsp;&nbsp;&nbsp;·<span style='font:7.0pt 'Times New Roman''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
"</span></span><span style='font-family:Palatino'>Attendance at Training </span></p> " +
" " +
"<p class=MsoNormal style='margin-left:.5in;text-indent:-.5in'><span " +
"style='font-family:Symbol'>&nbsp;&nbsp;&nbsp;·<span style='font:7.0pt 'Times New Roman''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
"</span></span><span style='font-family:Palatino'>Sales Call Evaluation (SCE)</span></p> " +
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
" " +
"</div> " +
" " +
"</body> " +
" " +
"</html> ";

} 
