package com.pfizer.utils;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.timer.Timer;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

import com.tgix.Utils.Util;
//import weblogic.management.timer.Timer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import weblogic.jndi.Environment;
//import weblogic.management.MBeanHome;
//import weblogic.management.runtime.ServerRuntimeMBean;
import java.sql.CallableStatement;
import java.sql.ResultSetMetaData;
//Webligic Server admin libs
//import weblogic.jndi.Environment;
//import weblogic.management.MBeanHome;
//import weblogic.management.runtime.ServerRuntimeMBean;

public class TMUUploadListener implements ServletContextListener,
		NotificationListener {
	private String file = "TRT_training";
	private String ftpServer = "amrndhw957.amer.pfizer.com";
	private String user = "mergepu";
	private String password = "pumerge01$";// change user and password for local environment
	private String folder = "/mergepu";

	protected static final Log log = LogFactory.getLog(TMUUploadListener.class);
	private Timer timer;
	// private Integer notificationId;
	private static final long PERIOD = Timer.ONE_DAY;
	private ServletContext context = null;
	private Integer notificationId;

	public void handleNotification(Notification arg0, Object arg1) {
		System.out.println("In handle notification method");
		// Check Date Time, off on SAT and SUN
		Calendar cal = Calendar.getInstance();
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if ((dayofweek == Calendar.SATURDAY) || (dayofweek == Calendar.SUNDAY)) {
			log.info("Stop running TMUUploadListener on Saturday and Sunday!");
			return;
		}

		FTPClient tmuFtpClient = new FTPClient();
		try {
			String output = queryData();
			// Don't write output file
			// output="testString";
			if (output == null) {
				return;
			}
			String timestr = null;
			Date date = new Date();
			Format formatter = new SimpleDateFormat("yyMMdd");
			timestr = formatter.format(date);
			String fileName = file.concat(timestr).concat(".txt");
			System.out.println("fileName " + fileName);
			tmuFtpClient.connect(ftpServer);
			System.out.println("ftpServer " + ftpServer);
			tmuFtpClient.login(user, password);
			System.out.println("user " + user);
			System.out.println("password " + password);
			ByteArrayInputStream str = new ByteArrayInputStream(
					output.getBytes());
			tmuFtpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
			tmuFtpClient.changeWorkingDirectory(folder);
			boolean result = tmuFtpClient.storeFile(fileName, str);
			System.out.println("^^^^^^^^^^^^^^^^FTP Result:" + result);
			str.close();

			// Must call completePendingCommand() to finish upload command.
			if (!tmuFtpClient.completePendingCommand()) {
				if (!tmuFtpClient.logout()) {
					if (log.isWarnEnabled()) {
						log.warn("Failed to successfully logout from ftp server! Reply code: "
								+ tmuFtpClient.getReplyCode());
					}
				}
				throw new Exception("File transfer failed!");
			}
			if (!tmuFtpClient.logout()) {
				if (log.isWarnEnabled()) {
					log.warn("Failed to successfully logout from ftp server! Reply code: "
							+ tmuFtpClient.getReplyCode());
				}
			}
			// log.info("TMUUploadListener.handleNotification upload result:"+result);
		} catch (Exception e) {
			log.info("TMUUploadListener.handleNotification Error:"
					+ e.toString());
		} finally {
			try {
				/*
				 * if(tmuFtpClient.isConnected()){ log.info(
				 * "Before disconnect : TMUUploadListener.handleNotification Error:"
				 * );
				 */
				tmuFtpClient.disconnect();
				// }
			} catch (Exception e) {
				log.info("Finally TMUUploadListener.handleNotification Error:"
						+ e.toString());
			}
		}
	}

	private String queryData() throws SQLException, NamingException {
		StringBuffer output = new StringBuffer("");
		String delimiter = "|";
		java.sql.Connection conn = null;
		java.sql.ResultSet rs = null;
		java.sql.Statement stmt = null;
		int resultCount = 0;
		conn = DBUtil.getConn("java:jboss/trt_ds");
		// 1. CHECK ACTIVE STATUS
		stmt = conn.createStatement();
		rs = stmt
				.executeQuery("select count(*) from tr_job_schedule where JOB_NAME='TMUUPLOAD' and JOB_STATUS = 'A'");
		if (rs.next()) {
			int count = rs.getInt(1);
			if (count <= 0) {
				DBUtil.closeDBObjects(conn, rs, stmt);
				return null;
			}
		}
		// 2.CALL STORED PROCEDURE
		CallableStatement proc = conn
				.prepareCall("{ call TMU_DAILY_UPLOAD_PROCESS()}");
		proc.execute();
		// 3. QUERY DATA
		String sql = "SELECT * FROM TMU_UPLOAD_DATA";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnLength = rsmd.getColumnCount();
		for (int i = 1; i <= columnLength; i++) {
			output.append(rsmd.getColumnName(i));
			if (i != columnLength) {
				output.append(delimiter);
			}
		}
		output.append("\n");
		while (rs.next()) {
			resultCount = resultCount + 1;
			for (int i = 1; i <= columnLength; i++) {
				output.append(Util.ifNull(rs.getString(i), ""));
				if (i != columnLength) {
					output.append(delimiter);
				}
			}
			output.append("\n");
		}
		DBUtil.closeDBObjects(conn, rs, stmt);
		if (resultCount == 0) {
			// Don't write output file id there are no data.
			return null;
		} else {
			return output.toString();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		log.info("TMUUploadListener.contextDestroyed");
		/*
		 * if timre is intialized then only stop if else not required to call
		 * stop
		 */
		if (timer != null) {
			timer.stop();
		}
		log.info(">>> TMUUpload timer stopped.");
	}

	public void contextInitialized(ServletContextEvent sce) {
		log.info("TMUUploadListener.contextInitialized");

		/*
		 * //===================== TESTING ===================== context =
		 * sce.getServletContext(); Timer timer = new Timer();
		 * timer.addNotificationListener(this, null, "some handback object");
		 * Date working = new Date(System.currentTimeMillis()+30000);
		 * timer.addNotification("oneDayTimer", "a recurring call",
		 * this,working, PERIOD); timer.start(); System.out.println();
		 * System.out.println("======> Start TMUUpldateListener.java");
		 * System.out.println();
		 * 
		 * //===================================================
		 */

		// Check correct server
		// String doEmail = System.getProperty("doemail");
		/*
		 * String doEmail ="true"; if (!"true".equals(doEmail)) {
		 * log.info("domail...TMUUploadListener.contextInitialized"); return; }
		 */

		// Check correct server
		String jboss_base_dir = System.getProperty("jboss.server.base.dir");
		System.out.println(" Environmnet :: jboss.server.base.dir:: "
				+ jboss_base_dir);

		/* directory for TRT Prod server-1 JVM */
		String prod_1_dir = "trt-prd/trt-prd_server1";

		System.out.println(" Production JVM-1 directory " + prod_1_dir);
		// String tst_dir="trt-tst/trt-tst_server1";
		/*
		 * Below Logic check if its JVM-1 in prod or not if it not prod JVM-1
		 * then no need to proceed to set up job timer So as result TMU Upload
		 * job will be scheduled on only JVM-1, and job will be execute by only
		 * one JVM
		 */
		// sanjeev
		if (!jboss_base_dir.contains(prod_1_dir)) {
			log.info("non prod environment... not required to set up job...TMUUploadListener.contextInitialized");
			return;
		}

		/*
		 * if (!jboss_base_dir.contains(tst_dir)) { log.info(
		 * "non Test environment... not required to set up job...TMUUploadListener.contextInitialized"
		 * ); return; }
		 */
		java.sql.Connection conn = null;
		java.sql.ResultSet rs = null;
		java.sql.PreparedStatement stmt = null;
		boolean blnSuccess = false;
		List dateList = new ArrayList();
		java.util.Date startDate = new java.util.Date();
		try {
			conn = DBUtil.getConn("java:jboss/trt_ds");
			String sql = " select PROCESS_RUN_HOURS,PROCESS_RUN_MINUTES,PROCESS_RUN_SECONDS,AM_PM from tr_job_schedule where JOB_NAME='TMUUPLOAD' ";
			System.out.println("FAKE********TMUUPLOAD JOB Timer Starts at:"
					+ startDate);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					// Instantiating the Timer MBean
					timer = new Timer();
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR,
							new Integer(rs.getString("PROCESS_RUN_HOURS"))
									.intValue());
					calendar.set(Calendar.MINUTE,
							new Integer(rs.getString("PROCESS_RUN_MINUTES"))
									.intValue());
					calendar.set(Calendar.SECOND,
							new Integer(rs.getString("PROCESS_RUN_SECONDS"))
									.intValue());
					String startTimeAmPm = rs.getString("AM_PM");
					System.out.println("AM_PM at:" + startTimeAmPm);
					calendar.set(Calendar.AM_PM,
							startTimeAmPm.equals("AM") ? Calendar.AM
									: Calendar.PM);
					startDate = calendar.getTime();
					System.out.println("time zone  at:"
							+ calendar.getTimeZone());
					dateList.add(startDate);
					timer.addNotificationListener(this, null,
							"some handback object");
					/*
					 * notificationId = timer.addNotification("oneDayTimer",
					 * "a recurring call", this, startDate, PERIOD);
					 */
					notificationId = timer.addNotification("oneDayTimer",
							"a recurring call", this, startDate, PERIOD, 0,
							true);

					timer.start();

					System.out
							.println("REAL*******TMUUPLOAD JOB Timer Starts at:"
									+ startDate);
				}
			}
		} catch (Exception e) {
			log.error("TMUUpload contextInitialized error" + e);
			e.printStackTrace();

		} finally {
			try {
				DBUtil.closeDBObjects(conn, rs, stmt, blnSuccess);
			} catch (Exception e) {
				log.error("TMUUpload: Exception in Closing DB Objects" + e);
			}
		}// end of finally block
	}
}