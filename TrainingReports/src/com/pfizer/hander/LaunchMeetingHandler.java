package com.pfizer.hander;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
//import sun.security.krb5.internal.bd;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.LaunchMeetingDetails;
import com.pfizer.db.P2LTrainingTrackPhaseRelation;
import com.pfizer.db.P2lActivityAction;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lEmployeeStatus;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;

public class LaunchMeetingHandler {
    protected static final Log log = LogFactory.getLog( P2lHandler.class );

    public LaunchMeetingHandler() {

    }

    
    public void insertPie(String name, String trackId) {
		String retString = null;
		String insertSql = "insert into LAUNCH_MEETING_DETAILS " +
                        " (TRACK_PHASE_ID, PHASE_NUMBER, track_id) " +
                        " values (P2L_TRACK_PHASE_ID_SEQ.nextval,?,?) ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, name );
			statement.setString( 2, trackId );

			statement.executeUpdate();
		} catch (Exception e) {
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
	}
    
   
    public String insertTrack(String name) {
        List result = DBUtil.executeSql("Select P2L_TRACK_ID_SEQ.nextval as nextid from dual",AppConst.APP_DATASOURCE);
		Map map = (Map)result.get(0);

        String retString = null;
		String insertSql = "insert into LAUNCH_MEETING " +
                        " (TRACK_ID, track_label, track_type,do_overall) " +
                        " values (?,?,'custom','N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
	//	Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);
            String nextValue = "LAUNCH_" + (BigDecimal)map.get("NEXTID");
			statement.setString( 1, nextValue );
            statement.setString(2, name);

			statement.executeUpdate();
            return 	nextValue;
		} catch (Exception e) {
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

        return "";
	}
    
 

    public List getActivityTree(String id) {
        String sql = " select activityname, activity_pk,m.ACTIVITY_CODE, level " +
                        " from mv_usp_activity_hierarchy m " +
                        " start with activity_pk = " +  id + " and rel_type='Parent' " +
                        " connect by prior activity_pk = prntactfk" ;
        List result = executeSql2(sql);
        return result;
    }
    public List getActivityTreeByName(String name) {
        String sql = " select activityname, activity_pk,m.ACTIVITY_CODE, level " +
                        " from mv_usp_activity_hierarchy m " +
                        " start with upper(activityname) like '%" +  name + "%' and rel_type='Parent' and activity_code is not null" +
                        " connect by prior activity_pk = prntactfk" ;
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    public List getActivityTreeByCode(String code) {
        String sql = " select activityname, activity_pk,m.ACTIVITY_CODE, level " +
                        " from mv_usp_activity_hierarchy m " +
                        " start with activity_code ='" +  code + "' and rel_type='Parent' and activity_code is not null" +
                        " connect by prior activity_pk = prntactfk" ;
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    
   
    public void updateTrackPhase(String value, String field, String id) {
		String retString = null;
        String fieldstr = "";
        //Connection conn = null;
        Connection conn = JdbcConnectionUtil.getJdbcConnection();
        ResultSet rs = null;
		PreparedStatement statement = null;
        try{
           /* Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
            if ("ROOT_ACTIVITY_ID".equals(field)) {
                fieldstr = " ROOT_ACTIVITY_ID=? ";
            }
            if ("ALT_ACTIVITY_ID".equals(field)) {
                fieldstr = " ALT_ACTIVITY_ID=? ";
            }
            if ("ALT_ACTIVITY_ID1".equals(field)) {
                fieldstr = " ALT_ACTIVITY_ID1=? ";
            }
            // Before updating get the current value of activity id
            String selectSQL = "select " + field + ", TRACK_ID from launch_meeting_details where track_phase_id = ?";
            System.out.println("###################### Select sql " + selectSQL);
            statement = conn.prepareStatement(selectSQL);
            statement.setBigDecimal( 1, new BigDecimal(value) );
            String activityId = "";
            String trackId = "";
            rs = statement.executeQuery();
            while(rs.next()){
                activityId = (new Integer(rs.getInt(field))).toString();
                trackId = rs.getString("TRACK_ID");
            }
            System.out.println("Track Id here is ###### " + trackId + "activityId " + activityId);
            // Update the mapping table 
            String insertSql = "update  launch_meeting_details set " +
                            fieldstr +
                            "   where track_phase_id = ? ";
     		log.info(insertSql + "\nid: " + id + "\nvalue:" + value);
     		statement = null;
			statement = conn.prepareStatement(insertSql);

			statement.setBigDecimal( 1, new BigDecimal(id) );
			statement.setBigDecimal( 2, new BigDecimal(value) );

			int num = statement.executeUpdate();
            System.out.println("update:" + num);
            updateTrackActivityMapping(trackId,activityId,id,value);

        } catch (Exception e) {
            e.printStackTrace();
			log.error(e,e);
		} finally {
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
    }
    
    public void updateTrackActivityMapping(String trackId, String currentActivity, String futureActivity, String trackPhaseId){
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            // Check wehther there is a mapping for this track-activity if yes then update else insert
            String selectQuery = "SELECT ACTIVITY_ID FROM TRACK_ACTIVITY_MAPPING where TRACK_ID = ? and ACTIVITY_ID=?";
            statement = conn.prepareStatement(selectQuery);
            statement.setString( 1, trackId);
            statement.setBigDecimal( 2, new BigDecimal(currentActivity));
            rs = statement.executeQuery();
            boolean recordExists  = false;
            while(rs.next()){
                recordExists = true;
            }
            
            if(recordExists){
                //update records
                String updateQuery = "UPDATE TRACK_ACTIVITY_MAPPING SET  ACTIVITY_ID = ? where TRACK_ID = ? and ACTIVITY_ID = ?";
                statement = conn.prepareStatement(updateQuery);
                statement.setBigDecimal( 1, new BigDecimal(futureActivity));
                statement.setString( 2, trackId);
                statement.setBigDecimal( 3, new BigDecimal(currentActivity));
                System.out.println("Updated the record ##########################" + updateQuery + "FA " + futureActivity + "Track " + trackId + "CA " + currentActivity );
                statement.execute();
                
            }
            else{
                // Insert records
                String insertQuery = "INSERT INTO TRACK_ACTIVITY_MAPPING(ACTIVITY_ID, TRACK_ID, TRACK_PHASE_ID) VALUES(?,?,?)";
                statement = conn.prepareStatement(insertQuery);
                 statement.setBigDecimal( 1, new BigDecimal(futureActivity));
                statement.setString( 2, trackId);
                statement.setBigDecimal( 3, new BigDecimal(trackPhaseId));
                statement.execute();
               System.out.println("Inserted the record ##########################" + insertQuery + "FA " + futureActivity + "Track " + trackId + "CA " + currentActivity );
            }
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
			log.error(e,e);
		} finally {
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
        
        
        
    }
    
    
    public void deleteTrackPhase(String id) {
        String sql = " delete from launch_meeting_details where track_phase_id = " + id;
        // Also delete the mapping
        System.out.println("update:" + DBUtil.executeUpdate(sql,AppConst.APP_DATASOURCE));
        
        String deleteMapping = "delete from TRACK_ACTIVITY_MAPPING where track_phase_id = " + id;
        System.out.println("Delete from track mapping :" + DBUtil.executeUpdate(deleteMapping,AppConst.APP_DATASOURCE));
    }
    public void updateTrackPhase(LaunchMeetingDetails phase) {
		String retString = null;
        String fieldstr = "";
        System.out.println("Step1");
		String insertSql = "update  launch_meeting_details set " +
                        " PHASE_NUMBER = ?, " +
                        " sort_order = ?," +
                        " is_attendance = ?, " +
                        " is_overall = ? " +
                        "   where track_phase_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
		log.info(insertSql + "\nid: " + phase.getTrackPhaseId() );
		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			conn = JdbcConnectionUtil.getJdbcConnection();
			/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, phase.getPhaseNumber() );
            BigDecimal db = new BigDecimal(0);
            if ( !Util.isEmpty(phase.getSortorder())) {
                try {
                    statement.setBigDecimal( 2, new BigDecimal(phase.getSortorder()) );
                } catch (Exception e) {
                    statement.setBigDecimal( 2, null );
                }
            }   else {
                    statement.setBigDecimal( 2, null );

            }
            System.out.println("In DB update##### " + (phase.getAttendance()?"Yes":"No"));
            statement.setString( 3, phase.getAttendance()?"Yes":"No" );
            statement.setString( 4, phase.getOverall()?"Yes":"No" );

			statement.setBigDecimal( 5, new BigDecimal(phase.getTrackPhaseId()) );
        System.out.println("Step2");

			int num = statement.executeUpdate();
            System.out.println("Step3");
            System.out.println("update:" + num);

        } catch (Exception e) {
            System.out.println("Error >>>>>>>> " + e.getMessage());
            e.printStackTrace();
			log.error(e,e);
		} finally {
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
    }

    public void updateTrack(LaunchMeeting track) {
		String retString = null;
		String insertSql = "update  LAUNCH_MEETING set " +
                        "   track_label=?, " +
                        "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			conn = JdbcConnectionUtil.getJdbcConnection();
			/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, track.getTrackLabel() );
			//statement.setString( 2, track.getDoComplete()?"Y":"N" );
			statement.setString( 2, track.getTrackId() );

			int num = statement.executeUpdate();
            System.out.println("update:" + num);

        } catch (Exception e) {
			log.error(e,e);
		} finally {
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
    }
    public boolean insertTrainingReports(LaunchMeeting track, String menuId) {

        String retString = null;
		String insertSql = "insert into  training_report  " +
                        "   (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) " +
                        "   values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			conn = JdbcConnectionUtil.getJdbcConnection();
			/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, track.getTrackLabel() );
			/*Infosys - Weblogic to Jboss Migrations changes start here*/
			/*statement.setString( 2, "/TrainingReports/LaunchMeeting/begin.do?track=" + track.getTrackId() );*/
			statement.setString( 2, "/TrainingReports/LaunchMeeting/begin?track=" + track.getTrackId() );
			/*Infosys - Weblogic to Jboss Migrations changes end here*/
			statement.setBigDecimal( 3, new BigDecimal(menuId) );
			statement.setString( 4, track.getTrackId() );

			log.info(insertSql);
			int num = statement.executeUpdate();
            if ( num > 0 ) {
                return true;
            }

        } catch (Exception e) {
			log.error(e,e);
		} finally {
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
        return false;
    }

    
    public void updateTrainingReports(LaunchMeeting track) {
		String retString = null;
		String insertSql = "update  training_report set " +
                        "   training_report_label=? " +
                        "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			conn = JdbcConnectionUtil.getJdbcConnection();
			/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, track.getTrackLabel() );
			statement.setString( 2, track.getTrackId() );

			int num = statement.executeUpdate();
            System.out.println("update:" + num);

        } catch (Exception e) {
			log.error(e,e);
		} finally {
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
    }

    public Map getSpecialCodes() {
        Map ret = new HashMap();

        String sql = "Select * from p2l_sce_special_codes";

        List result = executeSql2(sql);

        for ( Iterator it = result.iterator();it.hasNext();) {
            Map curr = (Map)it.next();
            if(curr.get("SCE_EVENT_ID")!=null)
            ret.put((String)curr.get("COURSE_CODE"),((BigDecimal)curr.get("SCE_EVENT_ID")).toString());
        }
        return ret;
    }
    public List getAllCompleteTracks() {
        List ret = new ArrayList();

        String sql = "Select track_id as trackid, track_label as trackLabel from LAUNCH_MEETING  order by track_label";

        List result = executeSql2(sql);
        // only expect 1
        LaunchMeeting track = null;
        if ( result != null && result.size() > 0 ) {

            for ( Iterator it = result.iterator();it.hasNext();) {
                boolean addTrack = false;
                Map tmp = (Map)it.next();
                track = new LaunchMeeting();
                FormUtil.loadObject(tmp,track,true);
                track = getTrack(track.getTrackId());
                List newPhases = new ArrayList();
                for ( Iterator b = track.getPhases().iterator(); b.hasNext();) {
                    LaunchMeetingDetails phase = (LaunchMeetingDetails)b.next();
                    phase.setTrack(track);

                    List act = getActivities(phase.getRootActivityId());

                    phase.setActivities(act);
                    if ( act.size() > 0 ) {
                        newPhases.add(phase);
                        addTrack = true;
                    }
                }
                track.setPhases(newPhases);
                if ( addTrack ) {
                    System.out.println("Asize:" + track.getTrackLabel());
                    ret.add(track);
                }
            }

        }
        //System.out.println("Done");
        return ret;
    }

    public List getActivities( String activityId ) {
        List ret = new ArrayList();
        String sql = "select distinct activityname from " +
                        " (select mah.* " +
                        " from mv_usp_activity_hierarchy mah " +
                        " start with activity_pk = " +  activityId +
                        " connect by prior activity_pk = prntactfk) sub1 " +
                        " where activityname like '%(Bucket)%' and ACTIVITY_CODE is not null order by activityname ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }

    public LaunchMeeting getTrack(String trackId) {
        String sql = "Select track_id as trackid, track_label as trackLabel, track_type as trackType, do_overall as doOverall from LAUNCH_MEETING where track_id='" + trackId + "'";

        List result = executeSql2(sql);
        // only expect 1
        LaunchMeeting track = null;
        if ( result != null && result.size() > 0 ) {
            track = new LaunchMeeting();
            FormUtil.loadObject((Map)result.get(0),track,true);
            sql = "select a.sort_order as sortorder,m3.code as coursecodealt1, m3.activityname as activitynamealt1, m2.code as coursecodealt, "+ 
                  " m2.activityname as activitynamealt,m.code as coursecode, m.activityname, a.track_id as trackid,"+ 
                  " a.do_prerequisite as prerequisite,a.phase_number as phasenumber,"+
                  " a.track_phase_id trackPhaseId , a.root_activity_id as rootactivityid, "+
                  " a.alt_activity_id as altactivityid, a.alt_activity_id1 as altactivityid1, a.is_attendance as attendance, a.is_overall as overall " +
                    " from launch_meeting_details a, mv_usp_activity_master m,mv_usp_activity_master m2, mv_usp_activity_master m3 " +
                    " where  a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.alt_activity_id1 = m3.activity_pk(+) and a.track_id='" + trackId + "' order by sort_order, a.phase_number";
            result = executeSql2(sql);
            log.info(sql);
            if ( result != null && result.size() > 0 ) {
                List phases = new ArrayList();
                for (Iterator it = result.iterator(); it.hasNext();) {
                    LaunchMeetingDetails phase = new LaunchMeetingDetails();
                    FormUtil.loadObject((Map)it.next(),phase,true);
                    System.out.println("######################### >>> " + phase.getActivitynamealt1());
                    phase.setTrack(track);
                    //log.debug(phase);
                    phases.add(phase);
                }
                track.setPhases(phases);
            }
        }
        return track;
    }
    
    public LaunchMeeting getTrackForOverAllPie(String trackId) {
        String sql = "Select track_id as trackid, track_label as trackLabel, track_type as trackType, do_overall as doOverall from LAUNCH_MEETING where track_id='" + trackId + "'";

        List result = executeSql2(sql);
        // only expect 1
        LaunchMeeting track = null;
        if ( result != null && result.size() > 0 ) {
            track = new LaunchMeeting();
            FormUtil.loadObject((Map)result.get(0),track,true);
            sql = "select a.sort_order as sortorder,m3.code as coursecodealt1, m3.activityname as activitynamealt1, m2.code as coursecodealt, "+ 
                  " m2.activityname as activitynamealt,m.code as coursecode, m.activityname, a.track_id as trackid,"+ 
                  " a.do_prerequisite as prerequisite,a.phase_number as phasenumber, "+
                  " a.track_phase_id trackPhaseId , a.root_activity_id as rootactivityid, "+
                  " a.alt_activity_id as altactivityid, a.alt_activity_id1 as altactivityid1, a.is_attendance as attendance, a.is_overall as overall " +
                  " from launch_meeting_details a, mv_usp_activity_master m,mv_usp_activity_master m2, mv_usp_activity_master m3 " +
                  " where  a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) "+ 
                  " and a.alt_activity_id1 = m3.activity_pk(+) and a.track_id='" + trackId + "' and a.IS_OVERALL <> 'Yes' "+ 
                  " order by sort_order, a.phase_number";
            result = executeSql2(sql);
            log.info(sql);
            if ( result != null && result.size() > 0 ) {
                List phases = new ArrayList();
                for (Iterator it = result.iterator(); it.hasNext();) {
                    LaunchMeetingDetails phase = new LaunchMeetingDetails();
                    FormUtil.loadObject((Map)it.next(),phase,true);
                    System.out.println("######################### >>> " + phase.getActivitynamealt1());
                    phase.setTrack(track);
                    //log.debug(phase);
                    phases.add(phase);
                }
                track.setPhases(phases);
            }
        }
        return track;
    }

    public LaunchMeetingDetails getTrackPhase(String activityId, String trackId) {
        String sql =    " select    " +
                            " a.track_id as trackid, " +
                            " m.code as coursecode, " +
                            " a.sort_order as sortorder, " +
                            " m.activityname, " +
                            " m2.code as coursecodealt, " +
                            " m2.activityname as activitynamealt, " +
                            " m3.code as coursecodealt1, " +
                            " m3.activityname as activitynamealt1, " +
                            " a.phase_number as phasenumber, " +
                            " a.do_prerequisite as prerequisite, " +
                            " a.root_activity_id as rootactivityid, " +
                            " a.track_phase_id as trackPhaseId, " +
                            " a.alt_activity_id as altactivityid, " +
                            " a.alt_activity_id1 as altactivityid1, " +
                            " b.track_label as trackLabel " +
                        " from launch_meeting_details a, launch_meeting b, mv_usp_activity_master m,mv_usp_activity_master m2,mv_usp_activity_master m3 " +
                        " where a.alt_activity_id = m2.activity_pk(+) and  a.alt_activity_id1 = m3.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id = b.track_id and a.root_activity_id=" + activityId + " and a.track_id = '" + trackId + "'";

        List result = executeSql2(sql);
        log.info(sql);
        LaunchMeetingDetails phase = null;
        if ( result != null && result.size() > 0 ) {
            phase = new LaunchMeetingDetails();
            FormUtil.loadObject((Map)result.get(0),phase,true);
        }
        return phase;
    }
    
    public void updateToviazLaunchAttComplete(String emplid, String actionBy, String trackId){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for attendance 
        HashMap result = getP2LCourseCode(trackId, "Attendance");
        String  p2lCourseCode = "";
        String activityId = "";
        for(Iterator iter=result.keySet().iterator();iter.hasNext();){
                 activityId = iter.next().toString();
                 p2lCourseCode = (String)result.get(activityId);
        }
        System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        System.out.println("#############Course code for attendance is  " + p2lCourseCode + "Completion Date " + completionDate + "GUID ########## " + guid) ;
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	 conn = JdbcConnectionUtil.getJdbcConnection();
        	 /*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            	
            String sql = "INSERT INTO LAUNCH_MEETING_ATTENDANCE( EMPLID, LAUNCH_MEETING_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES, ACTION_BY,TRACK_ID,ACTIVITY_ID,ACTIVITY_TYPE)" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCode + "', null,'" + completionDate + "',null,null,4,sysdate,'" + guid + "',null,null, '" + actionBy + "', '"+trackId+"', "+activityId+", 'ATT') ";
            System.out.println(sql);
            st.execute(sql);
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
    
    
     private String getTodaysDate(){
        
        String today = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        today = dateFormat.format(now);
        return today;
    }
    
    public HashMap getP2LCourseCode(String trackId, String type){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        HashMap result = new HashMap();
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            String sql = "";
            if(type.equals("Attendance")){	
                sql= "SELECT a.CODE as CODE, b.ROOT_ACTIVITY_ID as ACTIVITY_ID from MV_USP_ACTIVITY_MASTER a, LAUNCH_MEETING_DETAILS b  where b.TRACK_ID = '"+trackId+"'"+
                              " and b.ROOT_ACTIVITY_ID = a.ACTIVITY_PK and b.IS_ATTENDANCE='Yes' and b.IS_OVERALL='No'";
            }
            if(type.equals("Manager")){	
                sql= "SELECT a.CODE as CODE, b.ALT_ACTIVITY_ID as ACTIVITY_ID from MV_USP_ACTIVITY_MASTER a, LAUNCH_MEETING_DETAILS b  where b.TRACK_ID = '"+trackId+"'"+
                              " and b.ALT_ACTIVITY_ID = a.ACTIVITY_PK and b.IS_ATTENDANCE='Yes' and b.IS_OVERALL='No'";
            }
            if(type.equals("Compliance")){	
                sql= "SELECT a.CODE as CODE, b.ALT_ACTIVITY_ID1 as ACTIVITY_ID from MV_USP_ACTIVITY_MASTER a, LAUNCH_MEETING_DETAILS b  where b.TRACK_ID = '"+trackId+"'"+
                              " and b.ALT_ACTIVITY_ID1 = a.ACTIVITY_PK and b.IS_ATTENDANCE='Yes' and b.IS_OVERALL='No'";
            }
            System.out.println("############## for P2lCoursecode" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String code  = rs.getString("CODE");
                
                String  activityId = new Long(rs.getLong("ACTIVITY_ID")).toString(); 
                result.put(activityId, code);
                
            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        return result;
        
    }
    
     public void updateRegistrationForBreezeCompliance(String emplid, String actionBy, String trackId){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for has attended 
        String p2lCourseCodeCompliance = "";
        HashMap result = getP2LCourseCode(trackId, "Compliance");
        String activityId = "";
        for(Iterator iter=result.keySet().iterator();iter.hasNext();){
                 activityId = iter.next().toString();
                 p2lCourseCodeCompliance = (String)result.get(activityId);
        }
      //  System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	 conn = JdbcConnectionUtil.getJdbcConnection();
            st = conn.createStatement();
            String sql3 =" INSERT INTO LAUNCH_MEETING_ATTENDANCE( EMPLID, LAUNCH_MEETING_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES, ACTION_BY,TRACK_ID,ACTIVITY_ID,ACTIVITY_TYPE)" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCodeCompliance + "','" + completionDate + "',null,null,null,0,sysdate,'" + guid + "',null,null,'" + actionBy + "', '"+trackId+"', '"+activityId+"', null) ";
            System.out.println(sql3);
            st.execute(sql3);
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
    
    public void updateToviazLaunchManagerCertification(String emplid, String actionBy, String trackId){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for has attended 
        String p2lCourseCodePostLaunch = "";
        HashMap result = getP2LCourseCode(trackId, "Manager");
        String activityId = "";
        for(Iterator iter=result.keySet().iterator();iter.hasNext();){
                 activityId = iter.next().toString();
                 p2lCourseCodePostLaunch = (String)result.get(activityId);
        }
      //  System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        System.out.println("#############Course code for attendance is  " + p2lCourseCodePostLaunch + "Completion Date " + completionDate + "GUID ########## " + guid) ;
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	 conn = JdbcConnectionUtil.getJdbcConnection();
        	 /*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            String sql2 =" INSERT INTO LAUNCH_MEETING_ATTENDANCE( EMPLID, LAUNCH_MEETING_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES, ACTION_BY,TRACK_ID,ACTIVITY_ID,ACTIVITY_TYPE)" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCodePostLaunch + "', null,'" + completionDate + "',null,null,4,sysdate,'" + guid + "',null,null,'" + actionBy + "', '"+trackId+"',' "+activityId+"',null) ";
            System.out.println(sql2);
            st.execute(sql2);
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
    
    public String getEmplNumber(String emplId){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();	
            String sql = "SELECT GUID from V_RBU_LIVE_FEED where EMPLID= '" + emplId + "'";
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("GUID");
            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        return result;
        
    }
    
    public LaunchMeetingDetails getTrackPhaseForList(String phaseNumber, String trackId) {
        String sql =    " select    " +
                            " a.track_id as trackid, " +
                            " m.code as coursecode, " +
                            " a.sort_order as sortorder, " +
                            " m.activityname, " +
                            " m2.code as coursecodealt, " +
                            " m2.activityname as activitynamealt, " +
                            " m3.code as coursecodealt1, " +
                            " m3.activityname as activitynamealt1, " +
                            " a.phase_number as phasenumber, " +
                            " a.do_prerequisite as prerequisite, " +
                            " a.root_activity_id as rootactivityid, " +
                            " a.track_phase_id as trackPhaseId, " +
                            " a.IS_ATTENDANCE as attendance, " +
                            " a.IS_OVERALL as overall, " +
                            " a.alt_activity_id as altactivityid, " +
                            " a.alt_activity_id1 as altactivityid1, " +
                            " b.track_label as trackLabel " +
                        " from launch_meeting_details a, launch_meeting b, mv_usp_activity_master m,mv_usp_activity_master m2,mv_usp_activity_master m3 " +
                        " where a.alt_activity_id = m2.activity_pk(+) and  a.alt_activity_id1 = m3.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id = b.track_id and a.phase_number='" + phaseNumber + "' and a.track_id = '" + trackId + "'";

        List result = executeSql2(sql);
        System.out.println("############### getTrackPhaseForList " + sql); 
        log.info(sql);
        LaunchMeetingDetails phase = null;
        if ( result != null && result.size() > 0 ) {
            phase = new LaunchMeetingDetails();
            FormUtil.loadObject((Map)result.get(0),phase,true);
        }
        return phase;
    }
    
    public LaunchMeetingDetails getTrackPhaseById(String Id) {
        String sql =    " select    " +
                            " a.track_id as trackid, " +
                            " m2.code as coursecodealt, " +
                            " a.sort_order as sortorder, " +
                            " m2.activityname as activitynamealt, " +
                            " m3.activityname as activitynamealt1, " +
                            " m3.code as coursecodealt1, " +
                            " m.code as coursecode, " +
                            " m.activityname, " +
                            " a.phase_number as phasenumber, " +
                            " a.do_prerequisite as prerequisite, " +
                            " a.root_activity_id as rootactivityid, " +
                            " a.track_phase_id as trackPhaseId, " +
                            " a.alt_activity_id as altactivityid, " +
                            " a.alt_activity_id1 as altactivityid1, " +
                            " b.track_label as trackLabel " +
                        " from launch_meeting_details a, launch_meeting b, mv_usp_activity_master m, mv_usp_activity_master m2, mv_usp_activity_master m3 " +
                        " where a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.alt_activity_id = m3.activity_pk(+) and a.track_id = b.track_id and a.track_phase_id=" + Id + "";

        List result = executeSql2(sql);
        log.info(sql);
        LaunchMeetingDetails phase = null;
        if ( result != null && result.size() > 0 ) {
            phase = new LaunchMeetingDetails();
            FormUtil.loadObject((Map)result.get(0),phase,true);
        }
        return phase;
    }

    public String getActivityNameById(String activityId) {
        String sql =    " select    activityname from mv_usp_activity_master where activity_pk=" +activityId;

        List result = executeSql2(sql);
        if ( result != null && result.size() > 0 ) {
            Map map = (Map)result.get(0);
            return (String)map.get("ACTIVITYNAME");
        }
        return "";
    }

    public Employee[] getCompleteByNode(String node, UserFilter uFilter, String altNode, boolean isdetail ) {
        String nodes = "";
        if (!Util.isEmpty(altNode)) {
            nodes = node + "," + altNode;
        } else {
            nodes = node;
        }
        String sql = " and e.guid IN " +
                     " (select distinct emp_no " +
                     "  from mv_usp_completed a " +
                     "  where (a.ACTIVITYFK in " +
                     "   (select activity_pk " +
                     "	from mv_usp_activity_hierarchy " +
                     "	start with activity_pk in (" + nodes + ")  " +
                     "	connect by prior activity_pk = prntactfk and level < 2))   and a.STATUS = 'Complete') ";

        return executeSql( sql, uFilter ,isdetail);
    }

    public List getComplete(String node, UserFilter uFilter, String altNode, boolean isdetail ) {
        List total = new ArrayList();
        String sql1 = "select emp_no, activityfk, status from mv_usp_completed c where c.ACTIVITYFK in (select m.ACTIVITY_PK from mv_usp_activity_master m where m.PRNTACTFK=" + node + ")"  ;
        List nodes = executeSql2(sql1);
        if ( nodes != null && nodes.size() > 0) {
            total.addAll(nodes);
        }
        sql1 = "select emp_no, activityfk, status from mv_usp_completed c where c.ACTIVITYFK =" + altNode   ;
        nodes = executeSql2(sql1);
        if ( nodes != null && nodes.size() > 0) {
            total.addAll(nodes);
        }
        return total;
    }

    public boolean checkCompleteAndRegistered(String node, String guid ) {

        String sql = " select distinct emp_no " +
                     "  from V_USP_ACTIVITY_STATUS a " +
                     "  where (a.ACTIVITY_pk in " +
                     "   (select activity_pk " +
                     "	from mv_usp_activity_hierarchy " +
                     "	start with activity_pk = " + node + " " +
                     "	connect by prior activity_pk = prntactfk and rel_type <> 'Subscription' ))  " +
                     "  and a.emp_no = '" + guid + "'";

         List res = executeSql2( sql );
         log.info(sql);
         if ( res.size() > 0 )
            return true;

         sql = " select distinct emp_no " +
                     "  from mv_usp_registered a " +
                     "  where (a.ACTIVITY_PK in " +
                     "   (select activity_pk " +
                     "	from mv_usp_activity_hierarchy " +
                     "	start with activity_pk = " + node + " " +
                     "	connect by prior activity_pk = prntactfk ))  " +
                     "  and a.emp_no = '" + guid + "'";

        res = executeSql2( sql );
        if ( res.size() > 0 )
            return true;

        return false;
    }

    public Employee[] getRegistered(String node, UserFilter uFilter, boolean isdetail, String altNode ) {
        String nodes = "";
        if (!Util.isEmpty(altNode)) {
            nodes = node + "," + altNode;
        } else {
            nodes = node;
        }
        String sql = " and e.guid IN " +
                     " ( select distinct emp_no " +
                     "  from mv_usp_registered a " +
                     "  where a.status=0 and a.ACTIVITY_pk in " +
                     "   (select activity_pk " +
                     "	from mv_usp_activity_hierarchy " +
                     "	start with activity_pk in  (" + nodes + ")   " +
                     "	connect by prior activity_pk = prntactfk)) " ;

        return executeSql( sql, uFilter ,isdetail);

    }

    public Employee[] getWorldByNode(String node, UserFilter uFilter, String altNode, boolean isdetail, String emplid ) {
        String nodes = "";
        if (!Util.isEmpty(altNode)) {
            nodes = node + "," + altNode;
        } else {
            nodes = node;
        }
        String sql = " and e.guid IN " +
                                " (select  emp_no " +
                        "           from (  select activity_pk, activityname, emp_no,'Registered' " +
                        "                       from mv_usp_registered " +
                                        "   union " +
                        "                   select activity_pk, activityname, emp_no,'Assigned' " +
                        "                       from mv_usp_assigned  " +
                                        "   union " +
                        "                   select ACTIVITYFK, activityname, emp_no,'Completed' " +
                        "                       from mv_usp_completed) a " +
                        "           where a.activity_pk in  " +
                        "               (select activity_pk  " +
                        "                   from mv_usp_activity_hierarchy " +
                        "                   start with activity_pk in (" + nodes  + ") " +
                        "                   connect by prior activity_pk = prntactfk)) ";


        //List totalList = executeSql( sql );
        if ( !Util.isEmpty(emplid)) {
            sql = " and e.emplid = '" + emplid + "' " + sql;
        }
        return executeSql( sql,uFilter,isdetail  );
    }

    private String buildCriteria(UserFilter uFilter) {
        TerritoryFilterForm form = uFilter.getFilterForm();
		StringBuffer criteria = new StringBuffer();
        criteria.append( " and e.emplid = e.emplid ");
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            criteria.append(" AND e.new_team_cd='"+form.getTeam()+"' ");
        }

        if (!uFilter.isTsrOrAdmin()) {
        	criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			//if (uFilter.isAdmin()) {
			//} else {
			//	criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
			//}
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
//			if (uFilter.isAdmin()) {
//			} else {
//				criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
//			}
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
//			if (uFilter.isAdmin()) {
//			} else {
//				criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
//			}
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
//			if (uFilter.isAdmin()) {
//			} else {
//				criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
//			}
		}


        return criteria.toString();
    }
    public int getCompleteById() {
        return 0;
    }
    public int getRegisteredById() {
        return 0;
    }
    public int getAssingedById() {
        return 0;
    }
    public Collection getOveralStatus(LaunchMeeting track, UserFilter uFilter, boolean isDetail) {
        String nodes = track.getAllNodesDelimit();

        List result = new ArrayList();
        String sqlAlt =             " e.last_name as lastName, " +
                                    " e.team_cd as teamCode, " +
									" e.cluster_cd as clusterCode, " +
									" e.email_address as email, " +
									" e.territory_role_cd as role, " +
									" e.district_desc as districtDesc, " +
									" e.preferred_name as preferredName, ";


        if ( !isDetail ) {
            sqlAlt = "";
        }
        String sql =    "select distinct emp_no,E.EMPLID, " +
                        sqlAlt +
                                " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status " +
                        "from   (		select activity_pk, emp_no, status  " +
                        "                   from V_USP_ACTIVITY_STATUS  where activity_pk in (" + nodes + ")) a, v_new_field_employee e " +
                        " where a.emp_no = e.guid ";

         //if ( onlyRms ) {
         //   sql = sql + " and e.territory_role_cd = 'RM' ";
         //   System.out.println("hello world");
         //}

        sql = sql + buildCriteria(uFilter);
        log.info(sql);
        Timer timer = new Timer();
        List temp = executeSql2(sql);
        Map master = new HashMap();
        P2lEmployeeStatus pStatus;
        for (int i=0;i < temp.size(); i++) {
            Employee emp = new Employee();
            Map map = (Map)temp.get(i);
            emp.setGuid( Util.toEmpty((String)map.get("EMP_NO")) );
            emp.setEmplId( (String)map.get("EMPLID") );
            emp.setEmail( Util.toEmpty((String)map.get("EMAIL")) );
            emp.setEmployeeStatus((String)map.get("EMPL_STATUS"));
            emp.setPreferredName((String)map.get("preferredName".toUpperCase()) );
            emp.setFirstName((String)map.get("preferredName".toUpperCase()) );
            emp.setLastName((String)map.get("lastName".toUpperCase()) );
            emp.setDistrictDesc((String)map.get("districtDesc".toUpperCase()));
            emp.setTeamCode((String)map.get("teamCode".toUpperCase()));
            emp.setClusterCode((String)map.get("clusterCode".toUpperCase()));
            emp.setRole((String)map.get("role".toUpperCase()));
            pStatus = new P2lEmployeeStatus(emp,(String)map.get("STATUS"),"Overall");
            pStatus.setStatus((String)map.get("STATUS"));

            if ( master.get( emp.getGuid()) == null) {
                master.put(emp.getGuid(),pStatus);
            } else {
                P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus)master.get( emp.getGuid());
                if (pStatusTemp.getStatus().equals("Complete")) {
                    master.put(emp.getGuid(),pStatus);
                }
                if (pStatusTemp.getStatus().equals("Registered")) {
                    continue;
                }
                if (pStatusTemp.getStatus().equals("Waived")) {
                    if ("Complete".equals(pStatus.getStatus())
                        || "Assigned".equals(pStatus.getStatus())
                        || "Registered".equals(pStatus.getStatus()) ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }

                    continue;
                }
                if (pStatusTemp.getStatus().equals("Assigned")) {
                    if ("Complete".equals(pStatus.getStatus())
                        || "Waived".equals(pStatus.getStatus())
                        || "Registered".equals(pStatus.getStatus()) ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                    continue;
                }

            }
        }

        return master.values();


    }
    public Collection getPhaseStatus(UserTerritory ut, LaunchMeetingDetails phase, UserFilter uFilter, boolean isDetail,String selectedBU, String selectedRBU) {
        return getPhaseStatus(ut,phase, uFilter, isDetail, "",selectedBU, selectedRBU);
    }
    public Collection getPhaseStatus(UserTerritory ut, LaunchMeetingDetails phase, UserFilter uFilter, boolean isDetail, String otherNodes, String selectedBU, String selectedRBU) {
        String nodes = "";
        String completeNodes = "";
        boolean doFulltree = false;
        ReadProperties props = new ReadProperties();  
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() + otherNodes;
                completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
                completeNodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
                completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId()  + otherNodes;
                completeNodes = phase.getRootActivityId()  + otherNodes;
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1() + otherNodes;
                completeNodes =  phase.getAlttActivityId()  + "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1() + otherNodes;
                completeNodes = phase.getAlttActivityId1() + otherNodes;
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId()  + otherNodes;
                completeNodes = phase.getAlttActivityId()  + otherNodes;
            }
        }
        if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && Util.isEmpty(phase.getRootActivityId())){
            nodes = "''";
            completeNodes = "''";
        }
        if ( phase.getPrerequisite() )  {

            List tmplist = getPrerequisite(phase.getRootActivityId(),phase);
            if ( tmplist != null && tmplist.size() > 0 ) {
                for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
                    Map item = (Map)itr.next();
                    BigDecimal tnode = (BigDecimal)item.get("preqactfk".toUpperCase());
                    //P2lTrackPhase tmpPhase = new P2lTrackPhase();
                    if(tnode!=null)
                    nodes = nodes + "," + tnode.toString();
                }
            }
        }
        List result = new ArrayList();
        /*Modified for RBU changes */        
        String sqlAlt = props.getValue("sqlAlt");  
                                          
        String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, null as completedate  " +
                            " from mv_usp_pending  " +
                            " union  ";


        if ( !isDetail ) {
            sqlAlt = "";
        }
        String sql =    " select distinct emp_no,E.EMPLID, completedate, " +
                        " e.last_name as lastName,  e.last_name as mlastName, e.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, "+
                        " e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, " +
                        " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status " +
                        " from   (select activity_pk, emp_no,'Registered' as status, null as completedate  " +
                        " from mv_usp_registered  " +
                        " union  " +
                        " select activity_pk, emp_no,'Assigned' as status, null as completedate  " +
                        " from mv_usp_assigned  " +
                        " union  " +
        				" select ACTIVITYFK, emp_no,'RegisteredC' as status, null as completedate  " +
                        " from mv_usp_completed  " +
                        " union  ";
                        
        String orderByClause = "order by e.last_name";
                        
              /*  if (phase.getApprovalStatus()) {
                    sql = sql + pendingSql;
                }
                */
                String subScriptionClause = " and rel_type <> 'Subscription' ";
                if ("CPT".equals(phase.getTrack().getTrackType()))  {
                    subScriptionClause = " ";
                }
                /* The following condition is for Admin users to see all the training records else
                    see only those records of the employee having reports to as the logged in user's employee id */
                if(uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin())
                {
                    sql = sql +		" select ACTIVITYFK, emp_no,'Waived' as status, null as completedate  " +
                                    " from mv_usp_completed c where c.status='Waived' and c.activityfk in (" + completeNodes +")   " +
                                    " union  " +
                                    " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  " +
                                    " from mv_usp_completed c where c.status='Complete' and c.activityfk in (" + completeNodes +") ) a,  " +
                    /* Added for RBU changes */                
                                    " (select distinct emplid,reports_to_emplid, GUID,empl_status,last_name, preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc from mv_field_employee_rbu) e "+
                    /* End of addition */               
                                    " where a.activity_pk in  " +
                                    " (select activity_pk  " +
                                    " from mv_usp_activity_hierarchy  " +
                                    " start with activity_pk in (" + nodes +  ")  " +
                                    " connect by prior activity_pk = prntactfk " + subScriptionClause + ") " +
                                    " and a.emp_no=e.guid ";
                }
                else
                {
                    sql = sql +		" select ACTIVITYFK, emp_no,'Waived' as status, null as completedate  " +
                                    " from mv_usp_completed c where c.status='Waived' and c.activityfk in (" + completeNodes +")   " +
                                    " union  " +
                                    " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  " +
                                    " from mv_usp_completed c where c.status='Complete' and c.activityfk in (" + completeNodes +") ) a,  " +
                     //        
                    /* Added for RBU changes */                
                                    " (SELECT (select distinct emplid "+
                                    " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"+ 
                                    " emplid, GUID, empl_status, LEVEL org_level,last_name, preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "+
                                    " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("+ emplid+ ")) e "+
                    /* End of addition */               
                                    " where a.activity_pk in  " +
                                    " (select activity_pk  " +
                                    " from mv_usp_activity_hierarchy  " +
                                    " start with activity_pk in (" + nodes +  ")  " +
                                    " connect by prior activity_pk = prntactfk " + subScriptionClause + ") " +
                                    " and a.emp_no=e.guid ";
                }
        
        System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
        //added for RBU
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
         condQuery=condQuery+ " AND e.BU='"+selectedBU+"'";
        }
        if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
         condQuery=condQuery+ " AND e.RBU_DESC='"+selectedRBU+"' ";
        }
        sql = sql+condQuery + orderByClause;
        /*if(uFilter.isRefresh()==true)
        { 
            sql = sql + buildCriteria(ut,uFilter) + orderByClause;
        }
        else
        {
            sql=sql + orderByClause;
        }*/
        //ended for RBU
        System.out.println("Final SQL---\n"+sql);
        Timer timer = new Timer();
        List temp = executeSql2(sql);
        Map master = new HashMap();
        P2lEmployeeStatus pStatus;
        for (int i=0;i < temp.size(); i++) {
            Employee emp = new Employee();
            Map map = (Map)temp.get(i);
            emp.setManagerFname( Util.toEmpty((String)map.get("mpreferredName".toUpperCase())) );
            emp.setManagerLname( Util.toEmpty((String)map.get("mlastName".toUpperCase())) );
            emp.setRegionDesc( Util.toEmpty((String)map.get("regionDesc".toUpperCase())) );
            emp.setAreaDesc( Util.toEmpty((String)map.get("areaDesc".toUpperCase())) );
            emp.setGuid( Util.toEmpty((String)map.get("EMP_NO")));
            emp.setEmplId( (String)map.get("EMPLID") );
            emp.setEmail( Util.toEmpty((String)map.get("EMAIL")) );
            emp.setEmployeeStatus((String)map.get("EMPL_STATUS"));
            emp.setPreferredName((String)map.get("preferredName".toUpperCase()) );
            emp.setFirstName((String)map.get("preferredName".toUpperCase()) );
            emp.setLastName((String)map.get("lastName".toUpperCase()) );
            emp.setDistrictDesc((String)map.get("districtDesc".toUpperCase()));
            emp.setTeamCode((String)map.get("teamCode".toUpperCase()));
            emp.setClusterCode((String)map.get("clusterCode".toUpperCase()));
            emp.setRole((String)map.get("role".toUpperCase()));
            pStatus = new P2lEmployeeStatus(emp,(String)map.get("STATUS"),phase.getPhaseNumber());
            pStatus.setStatus((String)map.get("STATUS"));

            Object obj = map.get("completedate".toUpperCase());
            if(obj!=null){
                pStatus.setCompleteDate(new java.sql.Date(((java.util.Date)obj).getTime()));
            }else{
                pStatus.setCompleteDate(null);
            }

            if ( master.get( emp.getGuid()) == null) {
                master.put(emp.getGuid(),pStatus);
            } else {
                P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus)master.get( emp.getGuid());
                if ( pStatusTemp.getStatus().equals("RegisteredC") ) {
                    if ( pStatus.getStatus().equals("Registered") ) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Assigned") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Registered") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Pending") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Waived") ) {
                    if (pStatus.getStatus().equals("Complete")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Assigned") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Registered")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("RegisteredC") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                }
//                if ( pStatusTemp.getStatus().equals("Registered")
//                        || pStatusTemp.getStatus().equals("RegisteredC")
//                        || pStatusTemp.getStatus().equals("Complete")
//                        || pStatusTemp.getStatus().equals("Exempt")
//                        || pStatusTemp.getStatus().equals("Pending")) {
//                    if (pStatus.getStatus().equals("Exempt") || pStatus.getStatus().equals("Complete") ) {
//                        master.put(emp.getGuid(),pStatus);
//                    }
//                    if (pStatusTemp.getStatus().equals("RegisteredC") && pStatus.getStatus().equals("Registered") ) {
//                        master.put(emp.getGuid(),pStatus);
//                        System.out.println("found registeredC");
//                   }
//                    continue;
//                } else {
//                    master.put(emp.getGuid(),pStatus);
//                }
            }
        }

        return master.values();
    }



 public Collection getPhaseStatusForAttendance(UserTerritory ut, LaunchMeetingDetails phase, UserFilter uFilter, boolean isDetail, String otherNodes) {
        String nodes = "";
        String completeNodes = "";
        boolean doFulltree = false;
        ReadProperties props = new ReadProperties();  
        String emplid=uFilter.getEmployeeId();
        List result = new ArrayList();
        /*Modified for RBU changes */  
        if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() + otherNodes;
        //        completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
          //      completeNodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
            //    completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId()  + otherNodes;
              //  completeNodes = phase.getRootActivityId()  + otherNodes;
            }      
        
        String sql =    " select distinct e.emp_no as EMP_NO,E.EMPLID as EMPLID,  " +
                        " e.last_name as lastName,  e.first_name as firstName,  "+
                        " e.email_address as email, e.role_cd as role,  " +
                        " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status, " +
                        " e.attendance as status, e.reports_to_emplid as reports_to_emplid"+
                        " from V_LAUNCH_MEETING_STATUS e where e.activity_id in ("+nodes+") ";
                                    
           if(!uFilter.isAdmin() && !uFilter.isTsrAdmin() && !uFilter.isTsrOrAdmin())
         {
            sql = sql + " and e.REPORTS_TO_EMPLID = '"+emplid+"'";                                      
         }             
                        
        String orderByClause = " order by e.last_name";
                        
        
        System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
        //added for RBU
        if(uFilter.isRefresh()==true)
        { 
            sql = sql + buildCriteria(ut,uFilter) + orderByClause;
        }
        else
        {
            sql=sql + orderByClause;
        }
        //ended for RBU
        System.out.println("Final SQL---\n"+sql);
        Timer timer = new Timer();
        List temp = executeSql2(sql);
        Map master = new HashMap();
        P2lEmployeeStatus pStatus;
        for (int i=0;i < temp.size(); i++) {
            Employee emp = new Employee();
            Map map = (Map)temp.get(i);
            //emp.setManagerFname( Util.toEmpty((String)map.get("mpreferredName".toUpperCase())) );
            //emp.setManagerLname( Util.toEmpty((String)map.get("mlastName".toUpperCase())) );
            //emp.setRegionDesc( Util.toEmpty((String)map.get("regionDesc".toUpperCase())) );
            //emp.setAreaDesc( Util.toEmpty((String)map.get("areaDesc".toUpperCase())) );
            emp.setGuid( Util.toEmpty((String)map.get("EMP_NO")));
            emp.setEmplId( (String)map.get("EMPLID") );
            emp.setEmail( Util.toEmpty((String)map.get("EMAIL")) );
            emp.setEmployeeStatus((String)map.get("EMPL_STATUS"));
            //emp.setPreferredName((String)map.get("preferredName".toUpperCase()) );
            emp.setFirstName((String)map.get("firstName".toUpperCase()) );
            emp.setLastName((String)map.get("lastName".toUpperCase()) );
            //emp.setDistrictDesc((String)map.get("districtDesc".toUpperCase()));
            //emp.setTeamCode((String)map.get("teamCode".toUpperCase()));
            //emp.setClusterCode((String)map.get("clusterCode".toUpperCase()));
            emp.setRole((String)map.get("role".toUpperCase()));
            pStatus = new P2lEmployeeStatus(emp,(String)map.get("STATUS"),phase.getPhaseNumber());
            pStatus.setStatus((String)map.get("STATUS"));

            Object obj = map.get("completedate".toUpperCase());
            if(obj!=null){
                pStatus.setCompleteDate(new java.sql.Date(((java.util.Date)obj).getTime()));
            }else{
                pStatus.setCompleteDate(null);
            }

            if ( master.get( emp.getGuid()) == null) {
                master.put(emp.getGuid(),pStatus);
            } else {
                P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus)master.get( emp.getGuid());
                if ( pStatusTemp.getStatus().equals("RegisteredC") ) {
                    if ( pStatus.getStatus().equals("Registered") ) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Assigned") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Registered") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Pending") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Waived") ) {
                    if (pStatus.getStatus().equals("Complete")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Assigned") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Registered")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("RegisteredC") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                }
//                if ( pStatusTemp.getStatus().equals("Registered")
//                        || pStatusTemp.getStatus().equals("RegisteredC")
//                        || pStatusTemp.getStatus().equals("Complete")
//                        || pStatusTemp.getStatus().equals("Exempt")
//                        || pStatusTemp.getStatus().equals("Pending")) {
//                    if (pStatus.getStatus().equals("Exempt") || pStatus.getStatus().equals("Complete") ) {
//                        master.put(emp.getGuid(),pStatus);
//                    }
//                    if (pStatusTemp.getStatus().equals("RegisteredC") && pStatus.getStatus().equals("Registered") ) {
//                        master.put(emp.getGuid(),pStatus);
//                        System.out.println("found registeredC");
//                   }
//                    continue;
//                } else {
//                    master.put(emp.getGuid(),pStatus);
//                }
            }
        }

        return master.values();
    }

    public HashMap getAttendanceCount(UserTerritory ut, LaunchMeetingDetails phase, UserFilter uFilter, boolean isDetail, String otherNodes) {
        HashMap result  = new HashMap();
        String emplid=uFilter.getEmployeeId();
        String nodes = "";
        
        if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() + otherNodes;
        //        completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
          //      completeNodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1() + otherNodes;
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
            //    completeNodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()  + otherNodes;
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId()  + otherNodes;
              //  completeNodes = phase.getRootActivityId()  + otherNodes;
            }
        String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Complete' and data.activity_id in(" + nodes +") ";
         if(!uFilter.isAdmin() || !uFilter.isTsrAdmin() || !uFilter.isTsrOrAdmin())
         {
            sqlQuery1 = sqlQuery1 + " and data.REPORTS_TO_EMPLID = '"+emplid+"'";                                      
         }
         if(uFilter.isRefresh()==true)
        { 
            sqlQuery1 = sqlQuery1 + buildCriteria(ut,uFilter);
        }
         String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Not Complete' and data.activity_id in(" + nodes +") ";
         if(!uFilter.isAdmin() || !uFilter.isTsrAdmin() || !uFilter.isTsrOrAdmin())
         {
            sqlQuery2 = sqlQuery2 + " and data.REPORTS_TO_EMPLID = '"+emplid+"'";                                      
         }
          if(uFilter.isRefresh()==true)
        { 
            sqlQuery2 = sqlQuery2 + buildCriteria(ut,uFilter);
        }
         String sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'On Leave' and data.activity_id in(" + nodes +") ";
         if(!uFilter.isAdmin() || !uFilter.isTsrAdmin() || !uFilter.isTsrOrAdmin())
         {
            sqlQuery3 = sqlQuery3 + " and data.REPORTS_TO_EMPLID = '"+emplid+"'";                                      
         }
          if(uFilter.isRefresh()==true)
        { 
            sqlQuery3 = sqlQuery3 + buildCriteria(ut,uFilter);
        }
         
         String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
         System.out.println("Final Query for attendance ################## " + finalQuery);
        return executeStatement(finalQuery);
    }
    
    
    private HashMap executeStatement(String query){

         HashMap result = new HashMap();
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;

         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
        	 conn = JdbcConnectionUtil.getJdbcConnection();
        	 /*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.prepareCall(query);
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                String status = rs.getString("STATUS");
                int total = rs.getInt("TOTAL");
                result.put(status, new Integer(total));
            }
        }catch(Exception e){
                log.error(e,e);
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
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
        return result;
        }

/**
     * Function overloading for generating the query condition for Report Generation
     */
    private String buildCriteria(UserTerritory ut, UserFilter uFilter) {
        TerritoryFilterForm form = uFilter.getFilterForm();
		StringBuffer criteria = new StringBuffer();
        
        //Query condition to be appended for Geography Selection
        if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_ALL_SALESPOS_FILTER ) {
            criteria.append(" ");
        } 
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL1_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel1()));
        } 
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL2_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel2()));
        } 
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL3_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel3()));
        } 
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL4_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel4()));
        } 
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL5_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel5()));
        }        
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL6_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel6()));
        }
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL7_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel7()));
        }
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL8_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel8()));
        }
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL9_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel9()));
        }
        else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL10_FILTER ) {
            criteria.append(ut.getAllSalesPosition(form.getLevel10()));
        }
        
        //System.out.print("Sales Org selected"+form.getSalesOrg());
        //Query condition to be appended for Geography Selection
        criteria.append(ut.getAllSalesGroup(form.getSalesOrg()));
       /* if(form.getSalesorg() == null || form.getSalesOrg() == "ALL" || form.getSalesOrg().equalsIgnoreCase("all")){
            criteria.append(" ");
        }
        else {
            criteria.append(ut.getAllSalesGroup(form.getSalesOrg()));
        }   */     
        //combined condition for Geography and Sales Organization
        return criteria.toString();
    }

    public Collection getPhaseStatusOverallForLaunchMeeting(UserTerritory ut,LaunchMeetingDetails details, UserFilter uFilter, boolean isDetail, String otherNodes) {
        String nodes = "";
        String completeNodes = "";
        boolean doFulltree = false;
        ReadProperties props = new ReadProperties();
        String emplid=uFilter.getEmployeeId();
        // Get all the activities for this track to comput Complete/Not complete statuses
        LaunchMeeting meeting = getTrackForOverAllPie(details.getTrackId());                
        List phases = new ArrayList();
        phases = meeting.getPhases();
        Iterator iter = phases.iterator();
        while(iter.hasNext()){
            LaunchMeetingDetails phase = (LaunchMeetingDetails)iter.next();
            if(!Util.isEmpty(phase.getRootActivityId())){
                System.out.println("############ Inside if Root id is not empty ");
                System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
                if(!nodes.equals("")){
                    nodes = nodes + ",";
                }
                if(!completeNodes.equals("")){
                    completeNodes = completeNodes + ",";
                }
                if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes = nodes + phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
                    completeNodes = completeNodes + phase.getRootActivityId() + "," + phase.getAlttActivityId()  + "," + phase.getAlttActivityId1();
                }
                 if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes = nodes + phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
                    completeNodes = completeNodes + phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
                }
                 if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes = nodes + phase.getRootActivityId() + "," + phase.getAlttActivityId();
                    completeNodes = completeNodes + phase.getRootActivityId() + "," + phase.getAlttActivityId();
                }
                if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                    nodes = nodes + phase.getRootActivityId();
                    completeNodes = completeNodes +  phase.getRootActivityId();
                }
            }
            else{
                System.out.println("############ Inside else  Root id is  empty ");
                if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes =  nodes + phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
                    completeNodes =  completeNodes + phase.getAlttActivityId()  + "," + phase.getAlttActivityId1();
                }
                 if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes = nodes + phase.getAlttActivityId1();
                    completeNodes = completeNodes + phase.getAlttActivityId1();
                }
                 if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                    nodes = nodes + phase.getAlttActivityId();
                    completeNodes = completeNodes + phase.getAlttActivityId();
                }
            }
            nodes = nodes + otherNodes;
            completeNodes = completeNodes + otherNodes;
            if ( phase.getPrerequisite() )  {
    
                List tmplist = getPrerequisite(phase.getRootActivityId(),phase);
                if ( tmplist != null && tmplist.size() > 0 ) {
                    for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
                        Map item = (Map)itr.next();
                        BigDecimal tnode = (BigDecimal)item.get("preqactfk".toUpperCase());
                        //P2lTrackPhase tmpPhase = new P2lTrackPhase();
                       // nodes = nodes + "," + tnode.toString();
                        if(tnode!=null)
                        nodes = nodes  + tnode.toString();
                    }
                }
            }
        }
        
         List result = new ArrayList();
        String sql =    "select distinct emp_no,E.EMPLID, " +
                       " e.last_name as lastName, " +
                        " e.team_cd as teamCode, " +
                        " e.cluster_cd as clusterCode, " +
                        " e.email_address as email, " +
                        " e.territory_role_cd as role, " +
                        " e.district_desc as districtDesc, " +
                        " e.preferred_name as preferredName, " +
                        " a.status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status " +
                        " from(select activity_pk, emp_no, a.status  " +
                        " from V_USP_ACTIVITY_STATUS  where activity_pk in (" + nodes + ")) a, v_new_field_employee e " + 
                        " where a.emp_no = e.guid ";
        
        
        System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
        String orderByClause= "";
        //added for RBU
        if(uFilter.isRefresh()==true)
        { 
            sql = sql + buildCriteria(ut,uFilter) + orderByClause;
        }
        else
        {
            sql=sql + orderByClause;
        }
        //ended for RBU
        System.out.println("Final SQL---\n"+sql);
        Timer timer = new Timer();
        List temp = executeSql2(sql);
        Map master = new HashMap();
        P2lEmployeeStatus pStatus;
        for (int i=0;i < temp.size(); i++) {
            Employee emp = new Employee();
            Map map = (Map)temp.get(i);
            emp.setManagerFname( Util.toEmpty((String)map.get("mpreferredName".toUpperCase())) );
            emp.setManagerLname( Util.toEmpty((String)map.get("mlastName".toUpperCase())) );
            emp.setRegionDesc( Util.toEmpty((String)map.get("regionDesc".toUpperCase())) );
            emp.setAreaDesc( Util.toEmpty((String)map.get("areaDesc".toUpperCase())) );
            emp.setGuid( Util.toEmpty((String)map.get("EMP_NO")));
            emp.setEmplId( (String)map.get("EMPLID") );
            emp.setEmail( Util.toEmpty((String)map.get("EMAIL")) );
            emp.setEmployeeStatus((String)map.get("EMPL_STATUS"));
            emp.setPreferredName((String)map.get("preferredName".toUpperCase()) );
            emp.setFirstName((String)map.get("preferredName".toUpperCase()) );
            emp.setLastName((String)map.get("lastName".toUpperCase()) );
            emp.setDistrictDesc((String)map.get("districtDesc".toUpperCase()));
            emp.setTeamCode((String)map.get("teamCode".toUpperCase()));
            emp.setClusterCode((String)map.get("clusterCode".toUpperCase()));
            emp.setRole((String)map.get("role".toUpperCase()));
            pStatus = new P2lEmployeeStatus(emp,(String)map.get("STATUS"),details.getPhaseNumber());
            pStatus.setStatus((String)map.get("STATUS"));

            Object obj = map.get("completedate".toUpperCase());
            if(obj!=null){
                pStatus.setCompleteDate(new java.sql.Date(((java.util.Date)obj).getTime()));
            }else{
                pStatus.setCompleteDate(null);
            }

            if ( master.get( emp.getGuid()) == null) {
                master.put(emp.getGuid(),pStatus);
            } else {
                P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus)master.get( emp.getGuid());
                if ( pStatusTemp.getStatus().equals("RegisteredC") ) {
                    if ( pStatus.getStatus().equals("Registered") ) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Assigned") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Registered") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Pending") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Waived") ) {
                    if (pStatus.getStatus().equals("Complete")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                }
                if ( pStatusTemp.getStatus().equals("Assigned") ) {
                    if ( pStatus.getStatus().equals("Complete")
                        || pStatus.getStatus().equals("Registered")
                        || pStatus.getStatus().equals("Pending")
                        || pStatus.getStatus().equals("Waived")) {
                        master.put(emp.getGuid(),pStatus);
                    }
                    if ( pStatus.getStatus().equals("RegisteredC") ) {
                        pStatus.setStatus("Registered");
                        master.put(emp.getGuid(),pStatus);
                    }
                }
            }
        }

        return master.values();
    }

    public Employee[] search(String nodes, UserSession uSession, EmplSearchForm form ) {

        String sql = " and e.guid IN " +
                                " (select  emp_no " +
                        "           from (  select activity_pk, activityname, emp_no,'Registered' " +
                        "                       from mv_usp_registered " +
                                        "   union " +
                        "                   select activity_pk, activityname, emp_no,'Assigned' " +
                        "                       from mv_usp_assigned  " +
                                        "   union " +
                        "                   select ACTIVITYFK, activityname, emp_no,'Completed' " +
                        "                       from mv_usp_completed) a " +
                        "           where a.activity_pk in  " +
                        "               (select activity_pk  " +
                        "                   from mv_usp_activity_hierarchy " +
                        "                   start with activity_pk in (" + nodes + ")" +
                        "                   connect by prior activity_pk = prntactfk)) ";



        if ( !Util.isEmpty(form.getEmplid()) ) {
            sql = " and e.emplid = '" + form.getEmplid().trim() + "' " + sql ;
        } else if ( !Util.isEmpty(form.getTerrId()) ) {
            sql = " and e.SALES_POSITION_ID = '" + form.getSalesposId() + "' " + sql ;
        } else {
            if (Util.isEmpty(form.getFname()) && Util.isEmpty(form.getLname())) {
                return null;
            }
            String fsql = "";
            String lsql = "";
            if ( !Util.isEmpty(form.getFname()) ) {
                   fsql =  " and (upper(e.FIRST_NAME) like '%" + form.getFname().toUpperCase() + "%' or upper(e.preferred_name) like '%" + form.getFname().toUpperCase() + "%' )";
            }
            if ( !Util.isEmpty(form.getLname()) ) {
                   lsql =  " and upper(e.LAST_NAME) like '%" + form.getLname().toUpperCase() + "%' ";
            }
            sql = fsql + lsql + sql;
        }

        //sql = sql + buildCriteria(uSession.getUserFilter());
        System.out.println(sql);
        return executeSql3( sql,uSession  );
    }


    public List getPedScores(Map activityIds, String guid) {
        List result = new ArrayList();
        String ids = "";

        for (Iterator it = activityIds.keySet().iterator(); it.hasNext(); ) {
            if (Util.isEmpty(ids)) {
                ids = (String)it.next();
            } else {
                ids = ids + "," + (String)it.next();
            }
        }
        String sql =    " Select    * from sv_course_compl a where a.activity_pk in (" + ids + ") " +
 //        String sql =    " Select * from vt_course_compl a where a.activity_pk in (" + ids + ") " +
                        " and a.emp_no = '" + guid + "'";
        result = executeSql2(sql);

        return result;
    }
    
    public List getPedExamsList(String empGuid, String trackId){
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        List result = new ArrayList();
        String sql = "SELECT m.ACTIVITYNAME as COURSENAME, m.CODE as ACTIVITYCODE, DECODE(s.STATUS, 'Registered', 'Not Complete','Waived', 'Complete', s.STATUS) as status , d.is_attendance as ATTENDANCE, d.is_overall as OVERALL  from MV_USP_ACTIVITY_STATUS s,"+
                     " MV_USP_ACTIVITY_MASTER m, LAUNCH_MEETING_DETAILS d  where  d.track_id = '"+trackId+"' and " +
                     " d.root_activity_id = m.activity_pk and d.root_activity_id = s.activity_pk " + 
                     " and s.emp_no = '"+empGuid+"' " ;
                     
        System.out.println("Query for ped exam list ################# " + sql);
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            List processedCodes = new ArrayList();
            while(rs.next()){
                LaunchMeetingTrainingStatus launch = new LaunchMeetingTrainingStatus();
                if(rs.getString("ATTENDANCE") == null && rs.getString("OVERALL") == null){
                String courseName  = rs.getString("COURSENAME");
                String activityCode = rs.getString("ACTIVITYCODE");
                String status = rs.getString("STATUS");
                launch.setCourseName(courseName);
                launch.setCourseCode(activityCode);
                launch.setStatus(status);
                if(!processedCodes.contains(activityCode)){
                    result.add(launch);
                    processedCodes.add(activityCode);
                }
              }

            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        
        
        return result;
    }
    
    
    public boolean isAttendanceDefined(String trackId){
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        boolean result = false;
        String sql = "SELECT TRACK_ID from LAUNCH_MEETING_DETAILS where track_id = '"+trackId+"' and IS_ATTENDANCE='Yes'";
        System.out.println("Query for ped exam list ################# " + sql);
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = true;
            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        
        
        return result;
    }
    
    public String getOverallStatus(String trackId, String emplid){
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        String sql = "SELECT OVERALL from V_LAUNCH_MEETING_STATUS where track_id = '"+trackId+"' and emplid='"+emplid+"'";
        System.out.println("Query for overall status ################# " + sql);
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("OVERALL");
                break;
            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        
        
        return result;
    }
    
    public String getComplianceStatus(String empNumber, LaunchMeetingDetails details){
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        String sql = "SELECT 'Complete' as status  from mv_usp_completed mv, launch_meeting_details d "+
                     " where mv.activityfk = d.alt_activity_id1  and d.track_id = '"+details.getTrackId()+"' and d.is_attendance='Yes'";
        System.out.println("Query for getComplianceStatus ################# " + sql);
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = "Completed";
            }
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        
        
        return result;
    }
    
    public List getAttendanceList(String empGuid, String trackId){
        List result = new ArrayList();
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String sql = "SELECT m.ACTIVITYNAME as COURSENAME, m.CODE as ACTIVITYCODE, decode(s.STATUS, 4, 'Completed', 0,'Not Complete', s.status) as status from LAUNCH_MEETING_ATTENDANCE s,"+
                     " MV_USP_ACTIVITY_MASTER m, LAUNCH_MEETING_DETAILS d  where  d.track_id = '"+trackId+"' and " +
                     " d.root_activity_id = m.activity_pk and d.root_activity_id = s.activity_id  and d.track_id = s.track_id and d.is_attendance = 'Yes' and " + 
                     " d.is_overall = 'No' and s.emp_number = '"+empGuid+"' " ;
        
        System.out.println("Query for atendance list ################# " + sql);
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            // Results for Attendance code
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                LaunchMeetingTrainingStatus launch = new LaunchMeetingTrainingStatus();
                String courseName  = rs.getString("COURSENAME");
                String activityCode = rs.getString("ACTIVITYCODE");
                String status = rs.getString("STATUS");
                launch.setCourseName("Attendance_Code");
                launch.setCourseCode(activityCode);
                launch.setStatus(status);
                result.add(launch);
            }
            
            st = null;
            rs = null;
            sql = "";
            sql = "SELECT m.ACTIVITYNAME as COURSENAME, m.CODE as ACTIVITYCODE, decode(s.STATUS, 4, 'Completed', 0,'Not Complete', s.status) as status from LAUNCH_MEETING_ATTENDANCE s,"+
                     " MV_USP_ACTIVITY_MASTER m, LAUNCH_MEETING_DETAILS d  where  d.track_id = '"+trackId+"' and " +
                     " d.alt_activity_id = m.activity_pk and d.alt_activity_id = s.activity_id and d.is_attendance = 'Yes' and " + 
                     " d.is_overall = 'No' and s.emp_number = '"+empGuid+"' " ;
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                LaunchMeetingTrainingStatus launch = new LaunchMeetingTrainingStatus();
                String courseName  = rs.getString("COURSENAME");
                String activityCode = rs.getString("ACTIVITYCODE");
                String status = rs.getString("STATUS");
                launch.setCourseName("Manager_Training_Code");
                launch.setCourseCode(activityCode);
                launch.setStatus(status);
                result.add(launch);
            }
            st = null;
            rs = null;
            sql = "";
            sql = "SELECT m.ACTIVITYNAME as COURSENAME, m.CODE as ACTIVITYCODE, decode(s.STATUS, 4, 'Completed', 0,'Not Complete', s.status) as status from LAUNCH_MEETING_ATTENDANCE s,"+
                     " MV_USP_ACTIVITY_MASTER m, LAUNCH_MEETING_DETAILS d  where  d.track_id = '"+trackId+"' and " +
                     " d.alt_activity_id1 = m.activity_pk and d.alt_activity_id1 = s.activity_id and d.is_attendance = 'Yes' and " + 
                     " d.is_overall = 'No' and s.emp_number = '"+empGuid+"' " ;
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                LaunchMeetingTrainingStatus launch = new LaunchMeetingTrainingStatus();
                String courseName  = rs.getString("COURSENAME");
                String activityCode = rs.getString("ACTIVITYCODE");
                String status = rs.getString("STATUS");
                launch.setCourseName("Compliance_Presentation_Code");
                launch.setCourseCode(activityCode);
                launch.setStatus(status);
                result.add(launch);
            }               
        	
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        return result;
        
    }
    

    public List getPhaseDetail(String employeeId , LaunchMeetingDetails phase ) {
        String nodes = "";
        String altNode = phase.getAlttActivityId();
        String activitypk = phase.getRootActivityId();
        String altNode1 = phase.getAlttActivityId1();
        System.out.println("activitypk >>>>> " + activitypk + "altNode >>>> " + altNode + "altNode1 >>>>>  " + altNode1 + "Is Attendance >>>>>>>> " + phase.getAttendance());
        if(!phase.getAttendance()){
            if (!Util.isEmpty(altNode)) {
                if ( checkCompleteAndRegistered( activitypk, employeeId ) ) {
                    System.out.println("got it");
                    nodes = activitypk ;
                } else {
                    nodes = altNode;
                }
            } else {
                nodes = activitypk;
            }
        }
        else{
            if (!Util.isEmpty(altNode) && !Util.isEmpty(altNode1)) {
                System.out.println("Both are not null ###################");
                    nodes = altNode + "," + altNode1;
            } else {
                    if (!Util.isEmpty(altNode)) {
                        if ( checkCompleteAndRegistered( activitypk, employeeId ) ) {
                            System.out.println("got it");
                            nodes = activitypk ;
                        } else {
                            nodes = altNode;
                        }
                } else {
                    nodes = activitypk;
                }
            }    
        }
        System.out.println("Nodes here is ################### " + nodes);
        String sql = "select hi.activity_pk, hi.prntactfk, hi.mname, hi.mlevel, hi.rel_type, hi.ACTLABEL_NAME, hi.activityname, " +
                       //" select hi.*, " +
                       /*Modified the query to change the 'Exempt' status to 'Waived' */
                        " action.action_check, " +
                        "   attemp.CURRENTATTEMPTIND, " +
                        " attemp.EStatus, " +
                        "   m.code, attemp.enddt, " +
                        "   r.reg_check, " +
                        "   rq.assign_check, " +
                        "   attemp.att_check as has_attempt, "+
                        "   r.reg_check as has_registered, " +
                        "   rq.assign_check as has_assigned, "+
                        "   com.com_check as has_completed, "+
                        "   attemp.score, " +
                        "   success, " +
                        "   decode(pend.activityfk,null,'no','yes') as pending_check,"+
                        "   CompletionStatus " +
                        " from  " +
                        "   ( select rownum as myrow,activity_pk,prntactfk,lpad(' ', level*4,'-') as mname,  level as mlevel, m.REL_TYPE, m.ACTLABEL_NAME, activityname  " +
                        "   from mv_USP_ACTIVITY_HIERARCHY m " +
                        "	start with activity_pk in  (" + nodes  + ") " +
                        "	connect by prior activity_pk = prntactfk  and level < 9 ) hi " +
                        "   , mv_usp_activity_master m " +
                        "   , (select activityfk from mv_usp_pending where emp_no='" + employeeId + "') pend " +
                        "	, ( select distinct att.enddt, att.ACTIVITYFK,att.CURRENTATTEMPTIND , 'true' as att_check, score, decode(success,1,'Pass',0,'Fail') as success, decode(CompletionStatus,1,'Complete',0,'Not Complete') as CompletionStatus, mc.status as EStatus  from MV_USP_ATTEMPT att " +
                    	"				   	 			   			 , mv_p2l_tblemp emp, mv_usp_completed mc    " +
						"									where emp.EMP_NO = '" + employeeId + "' and att.EMPFK = emp.EMP_PK and att.empfk=mc.empfk and mc.empfk=emp.emp_pk and att.activityfk=mc.activityfk ) attemp " +
                        "   , ( select distinct reg.ACTIVITY_pK , 'true' as reg_check, score   from mv_usp_registered reg, mv_p2l_tblemp emp " +
                        "                                   where emp.EMP_NO = '" + employeeId + "' and reg.EMPFK = emp.EMP_PK  ) r " +
                        "   , ( select distinct act.ACTIVITY_id , 'Complete' as action_check from p2l_activity_action act " +
                        "                                   where act.empl_guid = '" + employeeId + "' ) action " +
                        "   , ( select distinct COM.ACTIVITYfK , 'true' as com_check, score   from mv_usp_completed com, mv_p2l_tblemp emp " +
                        "                                   where emp.EMP_NO = '" + employeeId + "' and com.EMPFK = emp.EMP_PK  ) com " +
						"   , ( SELECT DISTINCT ma.ACTIVITY_PK , 'true' AS assign_check" +
                        "                   FROM mv_usp_assigned ma, mv_p2l_tblemp emp " +
						"									WHERE emp.emp_no = '" + employeeId + "' and ma.EMPFK = emp.EMP_PK )  rq "+
                        "   where " +
                        "	 hi.activity_pk = attemp.ACTIVITYFK(+) " +
                        "       and m.ACTIVITY_PK  = hi.activity_pk " +
                        "       and hi.activity_pk = r.ACTIVITY_pK(+) " +
                        "       and hi.activity_pk = com.ACTIVITYfK(+) " +
                        "       and hi.activity_pk = pend.ACTIVITYFK(+) " +
                        "       and hi.activity_pk = action.ACTIVITY_id(+) " +
                        "       and hi.activity_pk = rq.ACTIVITY_PK(+) order by hi.myrow";

        List result = executeSql2(sql);
        log.info(sql);
        System.out.println("Query to get the edp details ################## " + sql);
        HashMap tempMap = new HashMap();
        P2lActivityStatus emp = null;
        Iterator it = result.iterator();
        List ret = new ArrayList();
        P2lActivityStatus curr = new P2lActivityStatus((HashMap)it.next());
        ret.add(curr);
        System.out.println("Activityid outside loop for training detail ################# " + curr.getActivityId());
        tempMap.put(new Integer(curr.getActivityId()), curr);
        for (int i=0; it.hasNext();) {
            P2lActivityStatus next = new P2lActivityStatus((HashMap)it.next());
            if ( tempMap.get( new Integer(next.getActivityId()) ) == null ) {
                System.out.println("Activityid inside loop for training detail ################# " + next.getActivityId());
                tempMap.put(new Integer(next.getActivityId()), next);
                ret.add(next);
            }
           /* if ( next.getParentid() != 0 ) {
                P2lActivityStatus tmp = (P2lActivityStatus)tempMap.get(new Integer(next.getParentid()));
                if (tmp != null) {
                    // Selvam 9-june-2008 : Some of the course in P2L registered in Fullfilment level, This produces duplicate
                    // entry in employee detail page. The following logic will avoid that duplicate.
                    if (next.isFullfilment() == true && next.isRegistered()== true){
                        continue;
                    }

                    tmp.addChild( next );

                }
            } */
        }
       /* if ( phase.getPrerequisite() && !nodes.equals(altNode))  {
            System.out.println("Loop for getPrerequisite ####################################");

            List tmplist = getPrerequisite(activitypk,phase);
            if ( tmplist != null && tmplist.size() > 0 ) {
                for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
                    Map item = (Map)itr.next();
                    BigDecimal tnode = (BigDecimal)item.get("preqactfk".toUpperCase());
                    LaunchMeetingDetails tmpPhase = new LaunchMeetingDetails();
                    tmpPhase.setRootActivityId(tnode.toString());
                    P2lActivityStatus tmp = getPhaseDetail(employeeId,tmpPhase);
                    tmp.setRaiselevel(1);
                    curr.addChild(tmp);
                    ret.add(curr);
                }
            }
        }*/
        return ret;
    }

    public List getPrerequisite( String activitypk,LaunchMeetingDetails phase) {

        String sql =    "select distinct preqactfk from MV_P2L_TBL_TMX_ActPreq m where m.ACTIVITYFK in " +
                        " (select activity_pk " +
                        " from mv_usp_activity_hierarchy  " +
                        " start with activity_pk = " + activitypk +
                        " connect by prior activity_pk = prntactfk)";
        List result = executeSql2(sql);
        //status.getKids().clear();
        return result;
    }

    public void getKids(Iterator it,P2lActivityStatus emp) {
        if (it.hasNext()) {
            HashMap map = (HashMap)it.next();
            P2lActivityStatus tmp = new P2lActivityStatus(map);
            if (tmp.getLevel() == (emp.getLevel() + 1) ) {
                emp.addChild(tmp);
                if (it.hasNext()) {
                    getKids(it,tmp);
                }
            }
        }
    }

    public Employee[] executeSql( String sql, UserFilter uFilter, boolean isDetail ) {

        EmployeeHandler eh = new EmployeeHandler();
        Employee[] result = eh.getEmployees(uFilter.getFilterForm(),uFilter, sql, isDetail);
        //List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        if ( result == null ) {
            return null;
        }
        return result;
    }

    public Employee[] executeSql3( String sql, UserSession uSession ) {

        EmployeeHandler eh = new EmployeeHandler();
        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();
        //form.setTeam("All");
        form.setSalesOrg("All");
        Employee[] result = eh.getEmployees(form,uSession.getUserFilter(), sql, true);
        //List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        if ( result == null ) {
            return null;
        }
        return result;
    }

    public List executeSql2( String sql ) {

        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);

        return result;
    }

    public void insertComplete(UserSession uSession, String activityId, Employee emp) {
        P2lActivityAction action = new P2lActivityAction();
        action.setActiontype("Complete");
        action.setActivity_id(activityId);
        if ( uSession.isAdmin() ) {
            action.setByEmplId(uSession.getOrignalUser().getId());
        } else {
            action.setByEmplId(uSession.getUser().getId());
        }
        action.setEmplid(emp.getEmplId());
        action.setGuid(emp.getGuid());
        action.setSubDate( new Date());

        insertaction(action);
    }

    public void deleteComplete(UserSession uSession, String activityId, Employee emp) {
        P2lActivityAction action = new P2lActivityAction();
        action.setActiontype("UnComplete");
        action.setActivity_id(activityId);
        if ( uSession.isAdmin() ) {
            action.setByEmplId(uSession.getOrignalUser().getId());
        } else {
            action.setByEmplId(uSession.getUser().getId());
        }
        action.setEmplid(emp.getEmplId());
        action.setGuid(emp.getGuid());
        action.setSubDate( new Date());

        deleteAction(action);
    }
    private void insertaction(P2lActivityAction action) {
		String retString = null;
		String insertSql = "insert into p2l_activity_action " +
                        " (action_id, empl_id, activity_id, action_type, by_empl_id, sub_date, empl_guid, status ) " +
                        " values (P2L_ACTIVITY_ACTION_SEQ.nextval,?,?,?,?,sysdate,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, action.getEmplid() );
			statement.setString( 2, action.getActivity_id() );
			statement.setString( 3, action.getActiontype() );
			statement.setString( 4, action.getByEmplId() );
            statement.setString(5, action.getGuid() );

			statement.executeUpdate();
		} catch (Exception e) {
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
	}
    private void deleteAction(P2lActivityAction action) {
		String retString = null;
		String insertSql = "update  p2l_activity_action set " +
                        "   empl_id=?, activity_id=?, action_type=?, by_empl_id=?, sub_date=sysdate, empl_guid=?, status='D'  " +
                        "   where activity_id=" + action.getActivity_id() + " and empl_id='" + action.getEmplid() + "'";
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);

			statement.setString( 1, action.getEmplid() );
			statement.setString( 2, action.getActivity_id() );
			statement.setString( 3, action.getActiontype() );
			statement.setString( 4, action.getByEmplId() );
            statement.setString(5, action.getGuid() );

			int num = statement.executeUpdate();
            System.out.println("update:" + num);
		} catch (Exception e) {
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
	}

    public List getEmployeeActivity( CourseSearchForm form ) {
		String sqlQuery =   " SELECT VNFE.LAST_NAME, VNFE.EMPLID, MUAHM.ACTIVITY_PK, MUAHM.COMPLETE_STATUS ,VNFE.FIRST_NAME, VNFE.TERRITORY_ROLE_CD, VNFE.TEAM_CD,MUAHM.ACTIVITYNAME,VNFE.EMAIL_ADDRESS, M.CODE " +
                            " FROM V_USP_BATCH_COMPLETE MUAHM, V_NEW_FIELD_EMPLOYEE VNFE, MV_USP_ACTIVITY_MASTER M " +
                            " WHERE MUAHM.activityname =  '" + form.getActivity() + "' " +
                            " AND MUAHM.EMP_NO = VNFE.GUID " +
                            " AND m.code is not null " +
                            " AND M.ACTIVITY_PK = MUAHM.ACTIVITY_PK AND (MUAHM.COMPLETE_STATUS in ('N','C') or MUAHM.COMPLETE_STATUS is null) " +
                            " AND M.ACTIVITY_PK IN ( " +
                            "   select activity_pk " +
                            "    from mv_usp_activity_hierarchy  " +
                            "    start with activity_pk = " + form.getPhase() +
                            "    connect by prior activity_pk = prntactfk)" +
                            " and vnfe.guid in ( select emp_no from v_usp_activity_status vs where vs.activity_pk in (select activity_pk from mv_usp_activity_hierarchy start with activity_pk = " + form.getPhase() + " connect by prior activity_pk = prntactfk and rel_type <> 'Subscription' ) )" +
                            "  order by  VNFE.LAST_NAME ";

        log.info(sqlQuery);
        List result = executeSql2(sqlQuery);

        return result;
    }

    public TreeMap getActivityByPhase(String phaseID){
        TreeMap output = new TreeMap();
		String sqlQuery =   " SELECT ACTIVITY_CODE,ACTIVITYNAME FROM  " +
                            " (SELECT ACTIVITY_CODE,ACTIVITYNAME,ACTLABEL_NAME FROM MV_USP_ACTIVITY_HIERARCHY " +
                            " START WITH ACTIVITY_PK = "+phaseID+" CONNECT BY PRIOR ACTIVITY_PK = PRNTACTFK) " +
                            " WHERE ACTLABEL_NAME = 'ILT Session' ORDER BY ACTIVITYNAME ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
			pstmt = conn.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			while(rs.next()){
                output.put(rs.getString("ACTIVITY_CODE"),rs.getString("ACTIVITYNAME"));
            }
            rs.close();
            pstmt.close();
            conn.close();
		} catch (Exception e) {
			log.error(e,e);
		} finally {
			}
			if ( pstmt != null) {
				try {
					pstmt.close();
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
        return output;
    }
    public Vector getAllTrainingPhaseTrack(){
        Vector output = new Vector();
		String sqlQuery =   " SELECT PT.TRACK_ID,PT.TRACK_LABEL,PTP.PHASE_NUMBER,PTP.ROOT_ACTIVITY_ID " +
                            " FROM P2L_TRACK_PHASE PTP,P2L_TRACK PT " +
                            " WHERE PTP.TRACK_ID=PT.TRACK_ID and pt.track_type='phase'" +
                            " ORDER BY PT.TRACK_LABEL,PTP.PHASE_NUMBER ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
            P2LTrainingTrackPhaseRelation record = null;
            String tempKey = "";
            String trackID = "";
            /*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
			pstmt = conn.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			while(rs.next()){
                trackID = rs.getString("TRACK_ID");
                if(!tempKey.equals(trackID)){
                    record = new P2LTrainingTrackPhaseRelation();
                    record.setTrackID(trackID);
                    record.setTrackLabel(rs.getString("TRACK_LABEL"));
                    record.getRootActivityID().addElement(rs.getString("ROOT_ACTIVITY_ID"));
                    record.getPhaseNumber().addElement(rs.getString("PHASE_NUMBER"));
                    output.addElement(record);
                }else{
                    record.getRootActivityID().addElement(rs.getString("ROOT_ACTIVITY_ID"));
                    record.getPhaseNumber().addElement(rs.getString("PHASE_NUMBER"));
                }
                tempKey = trackID;
            }
            rs.close();
            pstmt.close();
            conn.close();
		} catch (Exception e) {
			log.error(e,e);
		} finally {

			if ( pstmt != null) {
				try {
					pstmt.close();
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
        return output;
    }
    
    
    public RBUChartBean[] getLaunchMeetingExamChart(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU,String selectedRBU, String emplidAccess){
            
        String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
            
            
            System.out.println("In getLaunchMeetingExamChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND mv.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND mv.SALES_GROUP='"+selectedRBU+"' ";
            }
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   MV_LAUNCH_MEETING_PED_DATA data, MV_FIELD_EMPLOYEE_RBU mv"+
                             " WHERE  data.ACTIVITY_ID in("+nodes+") and data.STATUS = 'C' and data.emp_no = mv.guid";
              if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                           
                             
             String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   MV_LAUNCH_MEETING_PED_DATA data, MV_FIELD_EMPLOYEE_RBU mv"+
                             " WHERE  data.status ='NC'  and data.ACTIVITY_ID in("+nodes+") and  data.emp_no = mv.guid ";
               if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery2 = sqlQuery2 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                               
            String sqlQuery3="";
            
            sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   MV_LAUNCH_MEETING_PED_DATA data, MV_FIELD_EMPLOYEE_RBU mv"+
                             " WHERE  data.status ='L'  and data.ACTIVITY_ID in("+nodes+") and data.emp_no = mv.guid ";
               if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery3 = sqlQuery3 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            System.out.println("Query for LaunchMeeting exam:"+finalQuery.toString());
            return executeStatementLaunchMeeting(finalQuery);
        }
        
        public RBUChartBean[] getLaunchMeetingAttendanceChart(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU,String selectedRBU,String trackId, String emplidAccess){
            
        String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
            System.out.println("In getLaunchMeetingExamChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.TRACK_ID in('"+trackId+"') and data.ATTENDANCE in('Complete')";
                if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }             
             
             String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             " COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.ATTENDANCE in('Not Complete')  and data.TRACK_ID in('"+trackId+"')";
              if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery2 = sqlQuery2 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                              
            String sqlQuery3="";
            
            sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.ATTENDANCE in('On Leave')  and data.TRACK_ID in('"+trackId+"')";
              if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery3 = sqlQuery3 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                             
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            System.out.println("Query for LaunchMeeting exam:"+finalQuery.toString());
            return executeStatementLaunchMeeting(finalQuery);
        }
        
        public RBUChartBean[] getLaunchMeetingOverallChart(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU,String selectedRBU, String trackId, String emplidAccess){
            
         String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
            System.out.println("In getLaunchMeetingExamChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.TRACK_ID in('"+trackId+"') and data.OVERALL in('Complete') ";
                if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }              
             String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             " COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.OVERALL in('Not Complete')  and data.TRACK_ID in('"+trackId+"')";
              if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery2 = sqlQuery2 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                               
            String sqlQuery3="";
            
            sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMP_NO) TOTAL"+
                             " FROM   V_LAUNCH_MEETING_STATUS data"+
                             " WHERE  data.OVERALL in('On Leave')  and data.TRACK_ID in('"+trackId+"')";
               if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery3 = sqlQuery3 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                  
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            System.out.println("Query for LaunchMeeting exam:"+finalQuery.toString());
            return executeStatementLaunchMeeting(finalQuery);
        }
        
      
        private RBUChartBean[] executeStatementLaunchMeeting(String query){

         RBUChartBean[] rbuChartBean=null;
         RBUChartBean thisPOAChartBean;
         List tempList = new ArrayList();
         ResultSet rs = null;
         PreparedStatement st = null;
		 //Connection conn = null;
         Connection conn = JdbcConnectionUtil.getJdbcConnection();
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
            st = conn.prepareCall(query);
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                thisPOAChartBean=new RBUChartBean();
                thisPOAChartBean.setStatus(rs.getString("STATUS"));
                thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                tempList.add(thisPOAChartBean);
            }
            rbuChartBean=new RBUChartBean[tempList.size()];
            for(int i=0;i<tempList.size();i++){
                rbuChartBean[i]=(RBUChartBean)tempList.get(i);
            }
        }catch(Exception e){
                log.error(e,e);
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
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
        return rbuChartBean;
        } 
        
      
        
        public Employee[] getLaunchMeetingExamEmployees(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU, String selectedRBU, String section, String emplidAccess){
		String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
        
        Employee[] ret = null;
        String thisSection="";
        String type =  "Exam";
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="NC";
            courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="C";
            courseStatus="Completed";
        }

        System.out.println("###################" + thisSection);
          String sqlQuery = "";
          String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND mv.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND mv.sales_group='"+selectedRBU+"' ";
            } 
            String orderQuery=" ORDER BY mv.LAST_NAME"; 
          
         if(thisSection.equals("C")){
            
            
            String sqlQuery1 =   " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.sales_group as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Complete' as status, mv1.EMAIL_ADDRESS as REPORTSTOEMAIL, ped.TEST_SCORE as TEST_SCORE  "+
                             " from MV_FIELD_EMPLOYEE_RBU mv,MV_FIELD_EMPLOYEE_RBU mv1, MV_LAUNCH_MEETING_PED_DATA ped "+
                             " where ped.activity_id in("+nodes+") and ped.STATUS ='C' and " +
                             " mv.reports_to_emplid = mv1.emplid(+)  and ped.EMP_NO = mv.guid";           
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                              
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          } 
        else if(thisSection.equals("NC")){
           String sqlQuery1 =  " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.sales_group as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Not Complete' as status, mv1.EMAIL_ADDRESS as REPORTSTOEMAIL, ped.TEST_SCORE as TEST_SCORE   "+
                             " from MV_FIELD_EMPLOYEE_RBU mv,MV_FIELD_EMPLOYEE_RBU mv1, MV_LAUNCH_MEETING_PED_DATA ped "+
                             " where  ped.activity_id in("+nodes+") and ped.STATUS ='NC' and" +
                             " mv.reports_to_emplid = mv1.emplid(+)  and ped.EMP_NO = mv.guid";           
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          
        }
        else if(thisSection.equals("L")){
           String sqlQuery1 =  " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.sales_group as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'On Leave' as status, mv1.EMAIL_ADDRESS as REPORTSTOEMAIL, ped.TEST_SCORE as TEST_SCORE   "+
                             " from MV_FIELD_EMPLOYEE_RBU mv,MV_FIELD_EMPLOYEE_RBU mv1,  MV_LAUNCH_MEETING_PED_DATA ped "+
                             " where  ped.activity_id in("+nodes+") and ped.STATUS = 'L' and" +
                             " mv.reports_to_emplid = mv1.emplid(+)  and ped.EMP_NO = mv.guid";           
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                               
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
        }
        
            System.out.println("Query to get specific employees >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchEmployees(sqlQuery,type) ;
    }
    
    public Employee[] getLaunchMeetingAttendanceEmployees(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU, String selectedRBU, String section,String trackId, String emplidAccess){
		String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
        
        Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="NC";
            courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="C";
            courseStatus="Completed";
        }

        System.out.println("###################" + thisSection);
          String sqlQuery = "";
          String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND mv.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND mv.rbu_desc='"+selectedRBU+"' ";
            } 
            String orderQuery=" ORDER BY mv.LAST_NAME"; 
          
         if(thisSection.equals("C")){
            
            
            String sqlQuery1 =   " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Complete' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.ATTENDANCE = 'Complete'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                               
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          } 
        else if(thisSection.equals("NC")){
           String sqlQuery1 =  " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Not Complete' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.ATTENDANCE = 'Not Complete'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          
        }
        else if(thisSection.equals("L")){
           String sqlQuery1 = " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'On Leave' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.ATTENDANCE = 'On Leave'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
        }
        
            System.out.println("Query to get specific employees Attendance >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchEmployees(sqlQuery, "") ;
    }
    
    public Employee[] getLaunchMeetingOverallEmployees(User user,LaunchMeetingDetails phase,UserFilter uFilter, String selectedBU, String selectedRBU, String section,String trackId, String emplidAccess){
		String nodes = "";
        boolean doFulltree = false;
        String emplid=uFilter.getEmployeeId();
        if(!Util.isEmpty(phase.getRootActivityId())){
            System.out.println("############ Inside if Root id is not empty ");
            System.out.println("############ Root id  " + phase.getRootActivityId() + "Alt Id " + phase.getAlttActivityId() + "Alt1 " + phase.getAlttActivityId1());
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId() + "," + phase.getAlttActivityId1() ;
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() +  "," + phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId();
            }
            if(Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()) && !Util.isEmpty(phase.getRootActivityId())) {
                nodes = phase.getRootActivityId();
            }
        }
        else{
            System.out.println("############ Inside else  Root id is  empty ");
            if (!Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes =  phase.getAlttActivityId() + "," + phase.getAlttActivityId1();
            }
             if (Util.isEmpty(phase.getAlttActivityId()) && !Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId1();
            }
             if (!Util.isEmpty(phase.getAlttActivityId()) && Util.isEmpty(phase.getAlttActivityId1()))  {
                nodes = phase.getAlttActivityId();
            }
        }
        
        Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="NC";
            courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="C";
            courseStatus="Completed";
        }

        System.out.println("###################" + thisSection);
          String sqlQuery = "";
          String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND mv.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND mv.rbu_desc='"+selectedRBU+"' ";
            } 
            String orderQuery=" ORDER BY mv.LAST_NAME"; 
          
         if(thisSection.equals("C")){
            
            
            String sqlQuery1 =   " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Complete' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.OVERALL = 'Complete'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                             
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          } 
        else if(thisSection.equals("NC")){
           String sqlQuery1 =  " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'Not Complete' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.OVERALL = 'Not Complete'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                               
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          
        }
        else if(thisSection.equals("L")){
           String sqlQuery1 = " SELECT  mv.LAST_NAME LASTNAME,"+
                             " mv.FIRST_NAME FIRSTNAME,"+
                             " mv.EMPLID EMPLID, "+
                             " mv.EMAIL_ADDRESS as EMAIL, mv.bu as bu, mv.rbu_desc as rbu,"+
                           //  " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                               "'On Leave' as status, mv.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_LAUNCH_MEETING_STATUS mv"+
                             " where mv.track_id in('"+trackId+"') and mv.OVERALL = 'On Leave'";
            if(emplidAccess != null && !emplidAccess.equals("")){
	               sqlQuery1 = sqlQuery1 + " and mv.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplidAccess+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
        }
        
            System.out.println("Query to get specific employees Overall >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchEmployees(sqlQuery,"") ;
    }
    
    /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getToviazLaunchEmployees( String sql, String type) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		Connection conn = null;
    System.out.println("In getToviazLaunchEmployees ##############################");
		try {
			HashMap empHashMap=new LinkedHashMap();
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			conn = JdbcConnectionUtil.getJdbcConnection();
			/*Infosys - Weblogic to Jboss migration changes end here*/
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			rs = st.executeQuery();
            String emplid="";
            Employee curr = null;
            List examScores = null;
            boolean first = false;
			while (rs.next()) {
                emplid=rs.getString("EMPLID");
                 if(empHashMap.containsKey(emplid)){
                    curr=(Employee)empHashMap.get(emplid);
                }else{
                    curr = new Employee();
                }
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setEmail(rs.getString("EMAIL") );
                curr.setFutureBU(rs.getString("bu".toUpperCase()));
                curr.setFutureRBU(rs.getString("rbu".toUpperCase()));
                if(type.equals("Exam")){
                    curr.setPed1(rs.getString("TEST_SCORE"));
                }
                // Get the reports to email for the employee
                String email = "";
                if(rs.getString("REPORTSTOEMAIL") != null){
                    email = rs.getString("REPORTSTOEMAIL");
                }
                curr.setReportsToEmail(email);
                curr.setEmplId(emplid);
               empHashMap.put(emplid,curr);
			}

			String emplidTemp="";
            System.out.println("HashMap Size here is for employees for a particular product "+empHashMap.size());
            ret = new Employee[empHashMap.size()];
            int c=0;
            for(Iterator iter=empHashMap.keySet().iterator();iter.hasNext();){
            emplidTemp = iter.next().toString();
            Employee employee = (Employee)empHashMap.get(emplidTemp);
            ret[c++]=employee;
           }

		} catch (Exception e) {
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
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
		return ret;
	}
    
    public String getNTIdExistance(String emplid){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
            st = conn.createStatement();	
            String sql = "select EMPLID from V_RBU_LIVE_FEED where emplid ='"+emplid+"'";
            System.out.println("Query togetNTIdExistance from live feed ##############   " + sql);
            rs = st.executeQuery(sql);
            boolean found = false;
            while(rs.next()){
                found = true;
                 result = rs.getString("EMPLID");
            }
           if(!found){
                // Check in the user access
                sql = "select EMPLID from USER_ACCESS where emplid ='"+emplid+"'";
                System.out.println("Query togetNTIdExistance from user access ##############   " + sql);
                 rs = st.executeQuery(sql);
                 while(rs.next()){
                 found = true;
                 result = "";
            }
           } 
           if(!found){
                // The user is not present anywhere !!!
                result = "NF";
           }
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
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
        return result;
        
    }  
}
