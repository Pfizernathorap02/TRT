package com.pfizer.hander; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pfizer.dao.TransactionDB;
import com.pfizer.db.ForecastReport;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.db.P2lTrack;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.webapp.AppConst;
//import weblogic.webservice.tools.pagegen.result;

public class MoveCopyHandler 
{ 
    
    public TransactionDB trDb=new TransactionDB();
    
    protected static final Log log = LogFactory.getLog(ForecastFilterHandler.class );
    
String sectionListSql = "select training_report_id section_id,training_report_label section_label"+
                        " from training_report where parent is null and delete_flag='N' order by training_report_label asc";

String groupListSql = "select training_report_id group_id,training_report_label group_label"+
                        " from training_report where track_id LIKE 'GROUP%' and delete_flag='N' and parent is not null";

String sectionGroupSql = "select t.parent section_id,t.training_report_id group_id,t.training_report_label group_label from" +
                        " training_report t,(select training_report_id, TRAINING_REPORT_LABEL from"+
                        " training_report where parent is null and delete_flag='N') p"+
                        " where t.track_id LIKE 'GROUP%' and t.delete_flag='N' and t.parent = p.training_report_id"+
                        " group by t.parent,t.training_report_id,t.training_report_label order by t.parent,t.training_report_label asc";

String trainingReportSql = "select training_report_id ID,track_id trackid,training_report_label label from training_report where training_report_id=";

String trainingReportIdSql = "select training_report_id ID from training_report  where track_id=";

String trainingChildReportIdSql = "select training_report_id ID from training_report where parent=";



    public MoveCopyHandler() {

    }
    
    public List getSectionList() {
       List result = executeSql2(sectionListSql);
        return result;
    }
    
    public List getGroupList() {
       List result = executeSql2(groupListSql);
        return result;
    }
    
    public List getSectionGroupList() {
       List result = executeSql2(sectionGroupSql);
        return result;
    }
       
    public List executeSql2( String sql ) {
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        return result;
    }
    
    public void copyReport(String reportId,String parentId)
    {
       List result = executeSql2(trainingReportSql + "'" +reportId+ "'");
       String reportTrackId = "";
       String newTrackId =  "";
         System.out.println("List size"+result.size());
        if (result != null && result.size() > 0)
         {
             HashMap sectionDataMap=(HashMap)result.get(0);
             if(sectionDataMap.get("TRACKID")!=null)
            reportTrackId = sectionDataMap.get("TRACKID").toString();
             System.out.println("Child " + reportId + "childTrack " + reportTrackId );          
         }
         String groupTrackId = copy(reportId,parentId);
        if(reportTrackId.startsWith("GROUP")){
            String groupId = "";
             System.out.println("Inside Group Copy");
             List result2 = executeSql2(trainingReportIdSql + "'" +groupTrackId+ "'");
             if (result2 != null && result2.size() > 0)
            {
                HashMap sectionDataMap=(HashMap)result2.get(0);
                if(sectionDataMap.get("ID")!=null)
                groupId = sectionDataMap.get("ID").toString();
                System.out.println("Group ID " + groupId);  
            }
            if (groupId.trim().length() > 0)
            {
                List result3 = executeSql2(trainingChildReportIdSql + "'" +reportId+ "'");
                System.out.println(trainingChildReportIdSql + "'" +reportId+ "'");
                if (result3 != null && result3.size() > 0)
                {
                     System.out.println("result3.size() "+result3.size());
                    for(int i=0;i<result3.size();i++)
                    {
                        HashMap sectionDataMap=(HashMap)result3.get(i);
                        if(sectionDataMap.get("ID")!=null)
                        reportId = sectionDataMap.get("ID").toString();
                       System.out.println("adding child "+reportId +" to " + groupId+ " group " );
                        copy(reportId,groupId);
                    }
                }
            }
        }
    }
    
    
    public String copy(String reportId,String parentId)
    {
             List result = executeSql2(trainingReportSql + "'" +reportId+ "'");
             P2lHandler p2l = new P2lHandler();
             P2lTrack track = new P2lTrack();
             ManagementFilterHandler mtHandler = new ManagementFilterHandler();
             ForecastFilterHandler fcHandler = new ForecastFilterHandler();
             LaunchMeetingHandler lHandler = new LaunchMeetingHandler();
            String reportTrackId = "";
            String reportLabel = ""; 
             String newTrackId =  "";
             System.out.println("List size"+result.size());
             if (result != null && result.size() > 0)
             {
                 HashMap sectionDataMap=(HashMap)result.get(0);
                 if(sectionDataMap.get("TRACKID")!=null)
                reportTrackId = sectionDataMap.get("TRACKID").toString();
                 if(sectionDataMap.get("LABEL")!=null)
                reportLabel = sectionDataMap.get("LABEL").toString(); 
                System.out.println("Child " + reportId + "childTrack " + reportTrackId + " childLabel" +reportLabel);          
             }

            if(!reportTrackId.startsWith("LAUNCH") && !reportTrackId.startsWith("MANAGEMENT") && !reportTrackId.startsWith("FORECAST") && !reportTrackId.startsWith("GROUP")){
               System.out.println("Inside General Copy");
               track.setTrackLabel(reportLabel);
                newTrackId = p2l.insertTrack(track.getTrackLabel()) + ""; // for regular
                track.setTrackId(newTrackId + "");
                 System.out.println("new track id ="+newTrackId+ " original track id" + reportTrackId);
                p2l.insertTrainingReports(track,parentId);
                copyP2LTrackPhase(track.getTrackId(),reportTrackId);
            } // Added for Management report
            else if(reportTrackId.startsWith("MANAGEMENT")){
                    System.out.println("(track.getTrackLabel()=" + reportLabel);
                    newTrackId = mtHandler.insertTrack(reportLabel);
                    System.out.println(reportLabel +" Inside mgt summary");
                    ManagementSummaryReport report = new ManagementSummaryReport();
                    System.out.println("Track Id ################# " + newTrackId);
                    report.setTrackId(newTrackId);
                    report.setTrackLabel(reportLabel);
                    mtHandler.insertTrainingReports(report,parentId);
                    copyManagementFilter(newTrackId,reportTrackId);
                    copyManagementCodeDesc(newTrackId,reportTrackId);
                    
            } else if(reportTrackId.startsWith("FORECAST")){
                System.out.println("Inside Forecast Copy");
                 newTrackId = fcHandler.insertTrack(reportLabel);
                 ForecastReport report = new ForecastReport();
                System.out.println("Track Id ################# " + newTrackId);
                report.setTrackId(newTrackId);
                System.out.println("report.getTrackId(trackId)=" + report.getTrackId());
                report.setTracklabel(reportLabel);
                System.out.println("Track label " + reportLabel);
                fcHandler.insertTrainingReports(report,parentId);
                 copyForecastFilter(newTrackId,reportTrackId);
                            //trDb.deleteMenuByID(getRequest().getParameter("delID"));
               /* trDb.deleteReportByIDForForecastReport(getRequest().getParameter("delIDTrack"));
                trDb.deleteReportFromForecastFilterCriteria(getRequest().getParameter("delIDTrack"));*/
            }
            else if(reportTrackId.startsWith("LAUNCH")){
                 System.out.println("Inside Launch Copy");
                  LaunchMeeting meeting = new LaunchMeeting();
                    newTrackId = lHandler.insertTrack(reportLabel);
                    System.out.println("Track Id #################" + newTrackId);
                    meeting.setTrackId(newTrackId + "");
                    meeting.setTrackLabel(reportLabel);
                    lHandler.insertTrainingReports(meeting, parentId);
                 copyLaunchMeetingDetails(newTrackId,reportTrackId);
                 copyLaunchMeetingAttendance(newTrackId,reportTrackId);
                 copyTrackActivityMapping(newTrackId,reportTrackId);
                 
                //trDb.deleteMenuByID(getRequest().getParameter("delID"));
              /*  trDb.deleteReportByIDForLaunchMeeting(getRequest().getParameter("delIDTrack"));
                trDb.deleteTrackPhaseByIDForLaunchMeeting(getRequest().getParameter("delIDTrack"));
                trDb.deleteTrackActivityMappingForLaunchMeeting(getRequest().getParameter("delIDTrack"));
                trDb.deleteLaunchMeetingAttendance(getRequest().getParameter("delIDTrack"));          */
            }
            else if(reportTrackId.startsWith("GROUP")){
                 System.out.println("Inside Group Copy");
               track.setTrackLabel(reportLabel);
                newTrackId = p2l.insertTrack(track.getTrackLabel()) + ""; // for regular
                newTrackId = "GROUP_" + newTrackId;
                track.setTrackId(newTrackId);
                 System.out.println("new track id ="+newTrackId+ " original track id" + reportTrackId);
                p2l.insertTrainingReports(track,parentId);   
            }
            return newTrackId;
    }
    
    
    /*    public void copyP2LTrackPhase(String newTrackId,String oldTrackId) {
        String retString = null;
		String insertSql = "INSERT INTO p2l_track_phase(track_phase_id, track_id, phase_number, root_activity_id,alt_activity_id, do_prerequisite, report_approval_status,sort_order, do_assigned, do_exempt)"+
                            " SELECT p2l_track_phase_id_seq.NEXTVAL, '"+newTrackId+"', phase_number, root_activity_id, alt_activity_id, do_prerequisite,report_approval_status, sort_order, do_assigned, do_exempt FROM p2l_track_phase WHERE track_id = '"+oldTrackId+"'";
        
                           
        System.out.println("copy mgmt query : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    */
      

    //Infosys Coding Modification Starts Here
    public void copyP2LTrackPhase(String newTrackId,String oldTrackId) {
        String retString = null;
		String insertSql = "INSERT INTO p2l_track_phase(track_phase_id, track_id, phase_number, root_activity_id,alt_activity_id, do_prerequisite, report_approval_status,sort_order, do_assigned, do_exempt)"+
                            " SELECT p2l_track_phase_id_seq.NEXTVAL, '"+newTrackId+"', phase_number, root_activity_id, alt_activity_id, do_prerequisite,report_approval_status, sort_order, do_assigned, do_exempt FROM p2l_track_phase WHERE track_id = '"+oldTrackId+"'";
        
                           
        System.out.println("copy mgmt query : " + insertSql);                           
        Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx= session.beginTransaction();
			Query query= session.createSQLQuery(insertSql);
			query.executeUpdate();
			tx.commit();
            return ;
		} 
		
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("copyP2LTrackPhase(MoveCopyHandler) Hibernatate Exception");
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
        return;
	}

    //Infosys Coding Modification Ends Here
    
    public void copyManagementCodeDesc(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO MANAGEMENT_CODE_DESC (FILTER_ID, FILTER_TYPE, FILTER_CODE, FILTER_DESCRIPTION)"+
                            " SELECT '"+newTrackId+"',FILTER_TYPE, FILTER_CODE,FILTER_DESCRIPTION FROM MANAGEMENT_CODE_DESC WHERE FILTER_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
    public void copyManagementFilter(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO MANAGEMENT_FILTER_CRITERIA (FILTER_ID,REPORT_LABEL,SALES_ORG,GENDER,HIRE_START_DATE,HIRE_END_DATE,COURSE_CODE,TRAINING_COMPLETION_START_DATE,TRAINING_COMPLETION_END_DATE,TRAINING_REG_START_DATE,TRAINING_REG_END_DATE,BUSINESS_UNIT,ROLE_CODE,FIRST_GROUP_BY,SECOND_GROUP_BY,THIRD_GROUP_BY,FOURTH_GROUP_BY,FIFTH_GROUP_BY,SIXTH_GROUP_BY)"+
                            " SELECT '"+newTrackId+"',REPORT_LABEL,SALES_ORG,GENDER,HIRE_START_DATE,HIRE_END_DATE,COURSE_CODE,TRAINING_COMPLETION_START_DATE,TRAINING_COMPLETION_END_DATE,TRAINING_REG_START_DATE,TRAINING_REG_END_DATE,BUSINESS_UNIT,ROLE_CODE,FIRST_GROUP_BY,SECOND_GROUP_BY,THIRD_GROUP_BY,FOURTH_GROUP_BY,FIFTH_GROUP_BY,SIXTH_GROUP_BY FROM MANAGEMENT_FILTER_CRITERIA WHERE FILTER_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
     public void copyForecastFilter(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO FORECAST_FILTER_CRITERIA (FORECAST_REPORT_ID,FORECAST_REPORT_LABEL,ROLE_CD,START_DATE,END_DATE,HIRE_OR_PROMOTION_DATE,DURATION,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES,NOT_REGISTERED_COURSES)"+
                            " SELECT '"+newTrackId+"',FORECAST_REPORT_LABEL,ROLE_CD,START_DATE,END_DATE,HIRE_OR_PROMOTION_DATE,DURATION,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES,NOT_REGISTERED_COURSES FROM FORECAST_FILTER_CRITERIA WHERE FORECAST_REPORT_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
    
     public void copyLaunchMeetingDetails(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO LAUNCH_MEETING_DETAILS (TRACK_PHASE_ID,TRACK_ID,PHASE_NUMBER,ROOT_ACTIVITY_ID,ALT_ACTIVITY_ID,ALT_ACTIVITY_ID1,DO_PREREQUISITE,SORT_ORDER,IS_ATTENDANCE,IS_OVERALL)"+
                            " SELECT TRACK_PHASE_ID,'"+newTrackId+"',PHASE_NUMBER,ROOT_ACTIVITY_ID,ALT_ACTIVITY_ID,ALT_ACTIVITY_ID1,DO_PREREQUISITE,SORT_ORDER,IS_ATTENDANCE,IS_OVERALL FROM LAUNCH_MEETING_DETAILS WHERE TRACK_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
    
    public void copyLaunchMeetingAttendance(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO LAUNCH_MEETING_ATTENDANCE (EMPLID,LAUNCH_MEETING_CODE,REGISTRATION_DATE,COMPLETION_DATE,SCORE,PASSED,STATUS,CREATED_DATE,EMP_NUMBER,CANCELLATION_DATE,NOTES,ACTION_BY,TRACK_ID,ACTIVITY_ID,ACTIVITY_TYPE)"+
                            " SELECT EMPLID,LAUNCH_MEETING_CODE,REGISTRATION_DATE,COMPLETION_DATE,SCORE,PASSED,STATUS,CREATED_DATE,EMP_NUMBER,CANCELLATION_DATE,NOTES,ACTION_BY,'"+newTrackId+"',ACTIVITY_ID,ACTIVITY_TYPE FROM LAUNCH_MEETING_ATTENDANCE WHERE TRACK_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
        public void copyTrackActivityMapping(String newTrackId,String oldTrackId) {
        String retString = null;
		
        String insertSql = "INSERT INTO TRACK_ACTIVITY_MAPPING (ACTIVITY_ID,TRACK_ID,TRACK_PHASE_ID)"+
                            " SELECT ACTIVITY_ID,'"+newTrackId+"',TRACK_PHASE_ID FROM TRACK_ACTIVITY_MAPPING WHERE TRACK_ID = '"+oldTrackId+"'";                   
        
       // System.out.println("insertSql : " + insertSql);                           
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(insertSql);
			statement.executeUpdate();
            return ;
		} catch (Exception e) {
            System.out.println("Insert statement exception" + e);
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( statement != null) {
				try {
					statement.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}

        return;
	}
    
} 
