package com.pfizer.hander; 

import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.GeneralSessionEmployee;
import com.pfizer.db.RBUDBConfig;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.db.RBUEnrollmentException;
import com.pfizer.db.RBUSearchBean;
import com.pfizer.db.TrainingScheduleByTrack;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdminReportHandler {
	protected static final Log log = LogFactory.getLog( AdminReportHandler.class );
	
	public EmpReport[] getTrainingSchedule(String event) {
        
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String view = "V_PWRA_TR_SCHEDULE_REPORT";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_TR_SCHEDULE_REPORT";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_TR_SCHEDULE_REPORT";
        }
        String sql = "SELECT " +
                        "product_cd, " +
                        "product_desc, " +
                        "trunc(loa_start_time) as startDate , " +
                        "trunc(loa_end_time) as endDate , " +
                        "team_cd, " +
                        "team_desc, " +
                        "count(distinct trainee_ssn) as cnt, " +
                        "(select count(distinct trainee_ssn) as cnt from " + view + ") as totalCnt " +
                    "FROM " + view + " " +
                    "GROUP BY product_cd, product_desc, trunc(loa_start_time), trunc(loa_end_time), team_cd, team_desc " +
                    "ORDER BY product_desc, startDate, endDate, team_desc";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection(); */ 
			List tempList = new ArrayList();	
            List tempGeneralList = new ArrayList();	
					
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
            while (rs.next()) {
                curr = new EmpReport();
                
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));
                curr.setTeamCode(rs.getString("team_cd"));
                curr.setTeamDesc(rs.getString("team_desc"));
                curr.setCount(new Integer(rs.getInt("cnt")));               
                curr.setTotalCount(new Integer(rs.getInt("totalCnt")));
                
                if (curr.getProductCode().toUpperCase().equals("PLCA"))
                    tempGeneralList.add(curr);
                else
                    tempList.add(curr); 
			}
            
            ret = new EmpReport[tempList.size() + tempGeneralList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
            }
            for (int i=tempList.size(); i<tempList.size()+tempGeneralList.size(); i++) {
                ret[i] = (EmpReport)tempGeneralList.get(i-tempList.size());
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
    
    public List getRBUTrainingScheduleByTrack() {
        
        List tracks = new ArrayList();
		
		ResultSet rs = null;
        ResultSet rs1 = null;
		PreparedStatement st = null;
        PreparedStatement st1 = null;
		//Connection conn = null;
        Connection conn = JdbcConnectionUtil.getJdbcConnection();

        String sql = "SELECT * from V_RBU_PRODUCT_CLASS_BY_TRACK ";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection(); */ 
			List tempList = new ArrayList();	
            List tempGeneralList = new ArrayList();	
					
			st = conn.prepareCall(sql);			
			
            rs = st.executeQuery();
            
            TrainingScheduleByTrack curr=null;
            while (rs.next()) {
                curr = new TrainingScheduleByTrack();
                
                curr.setTrack_id(rs.getInt("TRACK_ID"));
                curr.setTrack_desc(rs.getString("TRACK_DESC"));
                curr.setProductCode(rs.getString("product_cd"));
                
                curr.setClass_id(rs.getString("CLASS_ID"));
                curr.setStart_date(rs.getDate("START_DATE"));
                curr.setEnd_date(rs.getDate("END_DATE"));     
                curr.setProductDesc(rs.getString("product_desc"));
                st1 = conn.prepareCall("SELECT RL.ROLE_GROUP, COUNT(CA.EMPLID) cnt "
                    + "FROM RBU_CLASS_ASSIGNMENT CA, " + RBUDBConfig.VIEW_FUTURE_ALIGNMENT + " AL, RBU_FUTURE_ROLE_MAP RL "
                    + "WHERE CA.EMPLID = AL.EMPLID AND AL.TERRITORY_ROLE_CD = RL.TERRITORY_ROLE_CD AND CLASS_ID = " + curr.getClass_id()
                    + " AND TRACK_ID = " + curr.getTrack_id()
                    + " GROUP BY RL.ROLE_GROUP");			
			
                rs1 = st1.executeQuery();      
                Map tmp = new HashMap();
                int total = 0;
                while (rs1.next()){
                   int rolecnt = rs1.getInt("cnt");
                   tmp.put(rs1.getString("ROLE_GROUP"), new Integer(rolecnt));
                   total = total + rolecnt;
                }            
                curr.setRolegroupcount(tmp);
                curr.setTraineecount(total);

                tracks.add(curr); 
			}


		} catch (Exception e) {
			log.error(e,e);
            e.printStackTrace();
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
            if ( rs1 != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st1 != null) {
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
        
		return tracks;	
    }    
    
    public List getRBUEnrollmentExcepitons() {
        
        List exceptions = new ArrayList();
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

        String sql = "SELECT e.EMPLID, FIRST_NAME, LAST_NAME, EXCEPTION_DATE, EXCEPTION_REASON, PRODUCTS_NEEDED FROM RBU_ENROLLMENT_EXCEPTIONS e, " + RBUDBConfig.VIEW_CURRENT_SNAPSHOT 
                    + " mv where e.emplid = mv.emplid AND e.enrollment_id = (select max(enrollment_id ) from RBU_ENROLLMENT_E_HEADER) ORDER BY EXCEPTION_DATE DESC";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */					
			st = conn.prepareCall(sql);			
			rs = st.executeQuery();
            
            
            RBUEnrollmentException curr;
            while (rs.next()) {
                curr = new RBUEnrollmentException();
                curr.setEmplId(rs.getString("EMPLID"));
                curr.setFirstname(rs.getString("FIRST_NAME"));
                curr.setLastname(rs.getString("LAST_NAME"));
                curr.setExceptionDate(rs.getDate("EXCEPTION_DATE"));
                curr.setReason(rs.getString("EXCEPTION_REASON"));
                curr.setproducts(rs.getString("PRODUCTS_NEEDED"));

                exceptions.add(curr); 
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
        
		return exceptions;	
    }   
    
    public EmpReport[] getRBUTrainingSchedule() {
        
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

        String sql = "SELECT class_id, product_cd, product_desc, trunc(START_DATE) as startDate ,trunc(END_DATE) as endDate , guest_count, trainee_count, totalcnt, MANAGER_COUNT " +
                        "FROM V_RBU_TR_SCHEDULE_SUMMARY " +
                        //why group by needed - shannon
                       // "GROUP BY product_cd, product_desc, start_Date, end_Date, class_id, guest_count, trainee_count, totalcnt " 
                       " ORDER BY product_desc, start_Date, end_Date";
        System.out.println(sql);
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
            List tempGeneralList = new ArrayList();	
					
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
            while (rs.next()) {
                curr = new EmpReport();
                curr.setCourseId(new Integer(rs.getString("class_id")));
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));            
                curr.setCount(new Integer(rs.getInt("trainee_count")));
                curr.setGuestCount(rs.getInt("guest_count"));
                curr.setTotalCount(new Integer(rs.getInt("totalCnt")));
                curr.setManager_count(new Integer(rs.getInt("MANAGER_COUNT")));

                tempList.add(curr); 
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
            }

		} catch (Exception e) {
			log.error(e,e);
            e.printStackTrace();
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
    public EmpReport[] getRBUTrainingScheduleEmplList(ClassFilterForm form) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String whereClause = "";
        String productCd = form.getProduct();        
        String strStartDate = form.getStartDate();
        String strEndDate = form.getEndDate();
         //added for rbu training schedule by track report 
        String classes = form.getClasses();
        if(classes != null && classes.length() >1){
            whereClause = "WHERE class_id in(" + classes + ") ";
        }
        else if(productCd == null || productCd.equals("")) {
            // Grand Total
        }

        else {
            // Row
            whereClause = "WHERE product_cd = '" + productCd + "' ";
            if (strStartDate != null && !strStartDate.equals("")) {
                whereClause += " AND trunc(START_DATE) = to_date('" + strStartDate + "','mm/dd/yyyy') ";                                         
            }
           
            if (strEndDate != null && !strEndDate.equals("")) {
                whereClause += " AND trunc(end_DATE) = to_date('" + strEndDate + "','mm/dd/yyyy') ";                         
            }
            
            if(form.getIfmanager() != null && !form.getIfmanager().equals("")){
                whereClause += " AND ismanager = '" + form.getIfmanager()+ "'";  
            }

        }
        String sql = "SELECT distinct product_cd, product_desc, START_DATE as startDate , END_DATE as endDate , " +
                        "EMPLID,first_name, last_name, role_cd, address1, address2, city, state, zip, area_cd, " +
                        "area_desc, region_cd, region_desc, district_id, district_desc, territory_id, (select distinct email_address from V_RBU_Live_Feed mv where mv.emplid = v.reports_to_emplid) reports_to_emplid, " +
                        "field_active, empl_status, BU, futurerbu " +                        
                        "FROM  V_RBU_TRAINING_SCHEDULE_REPORT v " + whereClause +                                        
                        "ORDER BY product_desc, startDate, endDate, first_name, last_name";
        System.out.println(sql);
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();            	
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                curr = new EmpReport();                
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));
                curr.setEmplId(rs.getString("EMPLID"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setRole(rs.getString("role_cd"));
                curr.setShipAdd1(rs.getString("address1"));
                curr.setShipAdd2(rs.getString("address2"));
                curr.setShipCity(rs.getString("city"));
                curr.setShipState(rs.getString("state"));
                curr.setShipZip(rs.getString("zip"));
                curr.setAreaDesc(rs.getString("area_desc"));
                curr.setRegionDesc(rs.getString("region_desc"));
                curr.setDistrictDesc(rs.getString("district_desc"));
                curr.setTerritoryId(rs.getString("territory_id"));
                curr.setReportsToEmplid(rs.getString("reports_to_emplid"));
                curr.setEmplStatus(rs.getString("empl_status"));
                curr.setFieldActive(rs.getString("field_active"));      
                curr.setFutureBU(rs.getString("BU"));                         
                curr.setFutureRBU(rs.getString("futurerbu"));  
                
                tempList.add(curr);
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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
     
    public EmpReport[] getRBUTrainingScheduleEmplListRBU(ClassFilterForm form) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String whereClause = "";

         //added for rbu training schedule by track report 
        String classes = form.getClasses();
        String track = form.getTrack();
        String sql = "";
        
        if(form.getRolegroup() !=null && form.getRolegroup().length()>0) {
            sql = "SELECT distinct product_cd, product_desc, START_DATE as startDate , END_DATE as endDate , " +
                        "EMPLID,first_name, last_name, role_cd, address1, address2, city, state, zip, area_cd, " +
                        "area_desc, region_cd, region_desc, district_id, district_desc, territory_id, (select distinct email_address from V_RBU_Live_Feed mv where mv.emplid = v.reports_to_emplid) reports_to_emplid , " +
                        "field_active, empl_status, BU, futurerbu " +                        
                        " FROM V_RBU_TRAINING_SCHEDULE_REPORT v, RBU_FUTURE_ROLE_MAP rp " 
                        + " WHERE v.role_cd = rp.territory_role_cd AND v.class_id = " + classes 
                        + " and rp.role_group = '" + form.getRolegroup() + "' AND v.track_id = '" + track                                        
                        + "' ORDER BY product_desc, startDate, endDate, first_name, last_name";    
        } else{
            sql = "SELECT distinct product_cd, product_desc, START_DATE as startDate , END_DATE as endDate , " +
                        "EMPLID,first_name, last_name, role_cd, address1, address2, city, state, zip, area_cd, " +
                        "area_desc, region_cd, region_desc, district_id, district_desc, territory_id, (select distinct email_address from V_RBU_Live_Feed mv where mv.emplid = v.reports_to_emplid) reports_to_emplid, " +
                        "field_active, empl_status, BU, futurerbu " +                        
                        "FROM  V_RBU_TRAINING_SCHEDULE_REPORT v WHERE class_id = " + classes   +   " and v.track_id='" + track +                                 
                        "' ORDER BY product_desc, startDate, endDate, first_name, last_name";    
        }  

        System.out.println(sql);
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();            	
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                curr = new EmpReport();                
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));
                curr.setEmplId(rs.getString("EMPLID"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setRole(rs.getString("role_cd"));
                curr.setShipAdd1(rs.getString("address1"));
                curr.setShipAdd2(rs.getString("address2"));
                curr.setShipCity(rs.getString("city"));
                curr.setShipState(rs.getString("state"));
                curr.setShipZip(rs.getString("zip"));
                curr.setAreaDesc(rs.getString("area_desc"));
                curr.setRegionDesc(rs.getString("region_desc"));
                curr.setDistrictDesc(rs.getString("district_desc"));
                curr.setTerritoryId(rs.getString("territory_id"));
                curr.setReportsToEmplid(rs.getString("reports_to_emplid"));
                curr.setEmplStatus(rs.getString("empl_status"));
                curr.setFieldActive(rs.getString("field_active"));      
                curr.setFutureBU(rs.getString("BU"));                         
                curr.setFutureRBU(rs.getString("futurerbu")); 
                    
                tempList.add(curr);
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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
    public EmpReport[] getEnrollmentEmplList(String event,ClassFilterForm form){
        EmpReport[] output = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        EmpReport curr=null;
        List tempList = new ArrayList();
        String sql = " SELECT distinct product_cd, product_desc, trunc(loa_start_time) as startDate , trunc(loa_end_time) as endDate,";
        sql = sql+" trainee_ssn, first_name, last_name, cluster_cd, cluster_desc, team_cd, team_desc, role_cd, address1, address2,";
        sql = sql+" city, state, zip, area_cd, area_desc, region_cd, region_desc, district_id, district_desc, territory_id,";
        sql = sql+" reports_to_emplid, field_active, empl_status";        
        sql = sql+" ,FMOH.DATEORDERED TSMDATE,TWA.EMDATE EMDATE,PPFL.P2LDATE P2LDATE";        
        sql = sql+" FROM";
        sql = sql+" (SELECT DISTINCT(CA.TRAINEE_SSN) AS TSSN FROM COURSE_ASSIGNMENT CA, COURSE C ";
        sql = sql+" WHERE TO_CHAR(CA.ENROLL_DATE,'mm/dd/yyyy') = ?";        
        sql = sql+" AND C.COURSE_ID = CA.COURSE_ID AND C.PRODUCT_CD=?) ENROLL ,";        
        if(event.equalsIgnoreCase(AppConst.EVENT_PDF)){
            sql = sql+" V_PWRA_TR_SCHEDULE_REPORT VEMP ";     
        }else{
            sql = sql+" V_SPF_TR_SCHEDULE_REPORT VEMP ";
        }
        sql = sql+" ,(SELECT  PERSON_ID,MAX (DATEORDERED) DATEORDERED FROM V_FFT_MATERIAL_ORDER_HISTORY WHERE SOURCE_ORDER_ID LIKE 'PDF%' GROUP BY PERSON_ID)  FMOH";
        sql = sql+" ,(SELECT USER_ID,MAX(ACTION_DATE) EMDATE FROM TR_WEBSITE_AUDIT WHERE ACTION = 'pdfemail' GROUP BY USER_ID) TWA";
        sql = sql+" ,(SELECT EMPLID,MAX(FEED_DATE) P2LDATE FROM PWRA_P2L_FEED_LOG GROUP BY EMPLID) PPFL ";
        sql = sql+" WHERE VEMP.TRAINEE_SSN = ENROLL.TSSN AND PRODUCT_CD=?";
        sql = sql+" AND VEMP.TRAINEE_SSN=FMOH.PERSON_ID(+)";         
        sql = sql+" AND VEMP.TRAINEE_SSN=TWA.USER_ID(+)";         
        sql = sql+" AND VEMP.TRAINEE_SSN=PPFL.EMPLID(+)";         
        sql = sql+" ORDER BY PRODUCT_DESC, STARTDATE, ENDDATE, FIRST_NAME, LAST_NAME";                

        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();  */
            st = conn.prepareStatement(sql);            
            st.setString(1,form.getEnrollmentDate());
            st.setString(2,form.getProduct());
            st.setString(3,form.getProduct());
            ResultSet rs = st.executeQuery();            
            while(rs.next()){
                curr = new EmpReport();                
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));
                curr.setEmplId(rs.getString("trainee_ssn"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setClusterCode(rs.getString("cluster_cd"));                
                curr.setClusterDesc(rs.getString("cluster_desc"));                                
                curr.setTeamCode(rs.getString("team_cd"));
                curr.setTeamDesc(rs.getString("team_desc"));
                curr.setRole(rs.getString("role_cd"));
                curr.setShipAdd1(rs.getString("address1"));
                curr.setShipAdd2(rs.getString("address2"));
                curr.setShipCity(rs.getString("city"));
                curr.setShipState(rs.getString("state"));
                curr.setShipZip(rs.getString("zip"));
                curr.setAreaDesc(rs.getString("area_desc"));
                curr.setRegionDesc(rs.getString("region_desc"));
                curr.setDistrictDesc(rs.getString("district_desc"));
                curr.setTerritoryId(rs.getString("territory_id"));
                curr.setReportsToEmplid(rs.getString("reports_to_emplid"));
                curr.setEmplStatus(rs.getString("empl_status"));
                curr.setFieldActive(rs.getString("field_active"));               
                curr.setTRMShipmentDate(rs.getDate("TSMDATE"));
                curr.setEmailInvitationDate(rs.getDate("EMDATE"));
                curr.setP2LregistrationDate(rs.getDate("P2LDATE"));
                tempList.add(curr);          
            } 
            rs.close();            
            output = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                output[i] = (EmpReport)tempList.get(i);
            }
		} catch (Exception e) {          
            e.printStackTrace();
			log.error(e,e);
		} finally {
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
        return output;
    }

    public EmpReport getEnrollmentSummaryReportTotal(String event){
        EmpReport curr = new EmpReport();
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();	
	
        String sql = " SELECT COUNT(DISTINCT CA.TRAINEE_SSN) CNTTOTAL,";
        sql = sql+" COUNT(DISTINCT FMOH.PERSON_ID) SMCNTTOTAL ,COUNT(DISTINCT TWA.USER_ID) EMCNTTOTAL ,";
        sql = sql+" COUNT(DISTINCT PPFL.EMPLID) P2LCNTTOTAL ";
        sql = sql+" FROM COURSE_ASSIGNMENT CA, COURSE C, PRODUCT_CODE_MAP PCM ,";
        sql = sql+" (SELECT PERSON_ID FROM V_FFT_MATERIAL_ORDER_HISTORY WHERE SOURCE_ORDER_ID LIKE 'PDF%') FMOH ,";
        sql = sql+" (SELECT DISTINCT USER_ID FROM TR_WEBSITE_AUDIT WHERE ACTION = 'pdfemail') TWA,";
        sql = sql+" PWRA_P2L_FEED_LOG PPFL ";
        sql = sql+" WHERE C.COURSE_ID = CA.COURSE_ID";
        sql = sql+" AND CA.TRAINEE_SSN=PPFL.EMPLID(+) AND C.PRODUCT_CD=PCM.PRODUCT_CD(+)";
        sql = sql+" AND CA.TRAINEE_SSN=FMOH.PERSON_ID(+) AND CA.TRAINEE_SSN=TWA.USER_ID(+)";
        sql = sql+" AND CA.TRAINEE_SSN=PPFL.EMPLID(+)";
        if(event.equals(AppConst.EVENT_PDF)){
            sql = sql+" AND (CA.COURSE_ID BETWEEN 300 AND 399)";                            
        }else{
            sql = sql+" AND (CA.COURSE_ID BETWEEN 400 AND 499)";                                
        }            

        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();  */
            
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                curr = new EmpReport();          
                curr.setCount(new Integer(rs.getInt("CNTTOTAL")));      
                curr.setTRMShipmentCount(new Integer(rs.getInt("SMCNTTOTAL")));      
                curr.setEmailInvitationCount(new Integer(rs.getInt("EMCNTTOTAL")));      
                curr.setP2LregistrationCount(new Integer(rs.getInt("P2LCNTTOTAL")));      
            }
            rs.close();            
		} catch (Exception e) {
             e.printStackTrace();
			log.error(e,e);
		} finally {
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
        return curr;
    }
    
    public EmpReport[] getEnrollmentSummaryReport(String event){
        EmpReport[] output = null;
		PreparedStatement st = null;
	//	Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        EmpReport curr=null;
        List tempList = new ArrayList();
        String sql = " SELECT (CASE WHEN C.PRODUCT_CD = 'PLCA' THEN 'General Session' ELSE PCM.PRODUCT_DESC END) AS PRODUCT_DESC,";
        sql = sql+" TRUNC(CA.ENROLL_DATE) EDATE,C.PRODUCT_CD CODE,COUNT(DISTINCT CA.TRAINEE_SSN) CNT,COUNT(DISTINCT FMOH.PERSON_ID) SMCNT";
        sql = sql+" ,COUNT(DISTINCT TWA.USER_ID) EMCNT,COUNT(DISTINCT PPFL.EMPLID) P2LCNT";        
        sql = sql+" FROM COURSE_ASSIGNMENT CA, COURSE C, PRODUCT_CODE_MAP PCM";        
        sql = sql+" ,(SELECT PERSON_ID FROM V_FFT_MATERIAL_ORDER_HISTORY WHERE SOURCE_ORDER_ID LIKE 'PDF%') FMOH";
        sql = sql+" ,(SELECT DISTINCT USER_ID FROM TR_WEBSITE_AUDIT WHERE ACTION = 'pdfemail') TWA, PWRA_P2L_FEED_LOG PPFL";        
        sql = sql+" WHERE C.COURSE_ID = CA.COURSE_ID";
        sql = sql+" AND CA.TRAINEE_SSN=PPFL.EMPLID(+)";        
        sql = sql+" AND C.PRODUCT_CD=PCM.PRODUCT_CD(+)";                
        sql = sql+" AND CA.TRAINEE_SSN=FMOH.PERSON_ID(+)";
        sql = sql+" AND CA.TRAINEE_SSN=TWA.USER_ID(+)";                
        sql = sql+" AND CA.TRAINEE_SSN=PPFL.EMPLID(+)";    
        if(event.equals(AppConst.EVENT_PDF)){
            sql = sql+" AND (CA.COURSE_ID BETWEEN 300 AND 399)";                            
        }else{
            sql = sql+" AND (CA.COURSE_ID BETWEEN 400 AND 499)";                                
        }            
        sql = sql+" GROUP BY TRUNC(CA.ENROLL_DATE),C.PRODUCT_CD,PCM.PRODUCT_DESC";        
        sql = sql+" ORDER BY PCM.PRODUCT_DESC";     
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();  */
            
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                curr = new EmpReport();          
                curr.setProductCode(rs.getString("CODE"));       
                curr.setProductDesc(rs.getString("PRODUCT_DESC"));
                curr.setStartDate(rs.getDate("EDATE"));
                curr.setCount(new Integer(rs.getInt("CNT")));      
                curr.setTRMShipmentCount(new Integer(rs.getInt("SMCNT")));      
                curr.setEmailInvitationCount(new Integer(rs.getInt("EMCNT")));      
                curr.setP2LregistrationCount(new Integer(rs.getInt("P2LCNT")));      
                tempList.add(curr);          
            }
            rs.close();            
            output = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                output[i] = (EmpReport)tempList.get(i);
            }
		} catch (Exception e) {             
			log.error(e,e);
		} finally {
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
        return output;
    }

    public EnrollChangeReport[] getEnrollmentChangeReport(String event){
        EnrollChangeReport[] output = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        EnrollChangeReport curr = null;
        List tempList = new ArrayList();
        String sql = AppConst.EVENT_SPF;
        
        if(event.equalsIgnoreCase(AppConst.EVENT_PDF))
          //This has to be changed once view for PDF is available.
          sql = "SELECT v.emplid as emplid, v.FIRST_NAME as first_name, v.LAST_NAME as last_name , V.EMAIL_ADDRESS as email_address, V.PRODUCT_CD as product_cd , V.OPERATION as operation, V.REASON as reason, V.OPERATION_DATE as operation_date  FROM v_pwra_enrollment_change v order by v.EMPLID";

        if(event.equalsIgnoreCase(AppConst.EVENT_SPF))
          sql = "SELECT v.emplid as emplid, v.FIRST_NAME as first_name, v.LAST_NAME as last_name , V.EMAIL_ADDRESS as email_address, V.PRODUCT_CD as product_cd , V.OPERATION as operation, V.REASON as reason, V.OPERATION_DATE as operation_date  FROM v_spf_enrollment_change v order by v.EMPLID";

        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection(); */ 
            
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                curr = new EnrollChangeReport();          
                curr.setEmplId(rs.getString("emplid"));       
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setEmailAddress(rs.getString("email_address"));
                curr.setProductCd(rs.getString("product_cd"));
                curr.setOperation(rs.getString("operation"));
                curr.setReason(rs.getString("reason"));                                                
                curr.setOperationDate(rs.getDate("operation_date"));
                tempList.add(curr);          
            }
            rs.close();            
            output = new EnrollChangeReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                output[i] = (EnrollChangeReport)tempList.get(i);
            }
		} catch (Exception e) {             
			log.error(e,e);
		} finally {
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
        return output;
    }
    
     public RBUEnrollChangeReport[] getRBUEnrollmentChangeReport(){
        RBUEnrollChangeReport[] output = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        RBUEnrollChangeReport curr = null;
        List tempList = new ArrayList();
        String sql = "SELECT v.emplid as emplid, v.FIRST_NAME as first_name, v.LAST_NAME as last_name , V.EMAIL_ADDRESS as email_address, "
                    + "v.action_date, v.updated_by, v.update_reason, v.action, V.PRODUCT_DESC FROM V_RBU_ENROLLMENT_CHANGE v order by v.EMPLID";

        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();  */
            
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                curr = new RBUEnrollChangeReport();          
                curr.setEmplId(rs.getString("emplid"));       
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setEmailAddress(rs.getString("email_address"));
                curr.setProductCd(rs.getString("product_desc"));
                curr.setOperation(rs.getString("action"));
                curr.setChanged_by(rs.getString("updated_by"));
                curr.setReason(rs.getString("update_reason"));                                                
                curr.setOperationDate(rs.getDate("action_date"));
               
                tempList.add(curr);          
            }
            rs.close();            
            output = new RBUEnrollChangeReport[tempList.size()];
            
            for (int i=0; i<tempList.size(); i++) {
                output[i] = (RBUEnrollChangeReport)tempList.get(i);
            }
		} catch (Exception e) {             
			log.error(e,e);

		} finally {
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
        return output;
    }
    public EmpReport[] getTrainingScheduleEmplList(String event, ClassFilterForm form) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		
        String view = "V_PWRA_TR_SCHEDULE_REPORT";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_TR_SCHEDULE_REPORT";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_TR_SCHEDULE_REPORT";
        }
        
        String whereClause = "";
        String productCd = form.getProduct();
        String teamCd = form.getTeamCd();
        String strStartDate = form.getStartDate();
        String strEndDate = form.getEndDate();
        
        if (productCd == null || productCd.equals("")) {
            // Grand Total
        }
        else if (teamCd == null || teamCd.equals("")) {
            // Product Total 
            whereClause = "WHERE product_cd = '" + productCd + "' ";           
        }
        else {
            // Row
            whereClause = "WHERE product_cd = '" + productCd + "' " +
                          "AND team_cd = '" + teamCd + "' ";
            if (strStartDate != null && !strStartDate.equals("")) {
                whereClause += " AND trunc(loa_start_time) = to_date('" + strStartDate + "','mm/dd/yyyy') ";                                         
            }
            else {
                whereClause += " AND loa_start_time is null ";
            }
            if (strEndDate != null && !strEndDate.equals("")) {
                whereClause += " AND trunc(loa_end_time) = to_date('" + strEndDate + "','mm/dd/yyyy') ";                         
            }
            else {
                whereClause += " AND loa_end_time is null ";
            }
        }
        String sql = "SELECT distinct " +
                        "product_cd, " +
                        "product_desc, " +
                        "trunc(loa_start_time) as startDate , " +
                        "trunc(loa_end_time) as endDate , " +
                        "trainee_ssn, " +
                        "first_name, " +
                        "last_name, " +
                        "cluster_cd, " +
                        "cluster_desc, " +                        
                        "team_cd, " +
                        "team_desc, " +
                        "role_cd, " +
                        "address1, " +
                        "address2, " +
                        "city, " +
                        "state, " +
                        "zip, " +
                        "area_cd, " +
                        "area_desc, " +
                        "region_cd, " +
                        "region_desc, " +
                        "district_id, " +
                        "district_desc, " +
                        "territory_id, " +
                        "reports_to_emplid, " +
                        "field_active, " +
                        "empl_status " +                        
                    "FROM " + view + " " + whereClause +                                        
                    "ORDER BY product_desc, startDate, endDate, first_name, last_name";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();            	
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                curr = new EmpReport();                
                curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));
                curr.setEmplId(rs.getString("trainee_ssn"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setClusterCode(rs.getString("cluster_cd"));                
                curr.setClusterDesc(rs.getString("cluster_desc"));                                
                curr.setTeamCode(rs.getString("team_cd"));
                curr.setTeamDesc(rs.getString("team_desc"));
                curr.setRole(rs.getString("role_cd"));
                curr.setShipAdd1(rs.getString("address1"));
                curr.setShipAdd2(rs.getString("address2"));
                curr.setShipCity(rs.getString("city"));
                curr.setShipState(rs.getString("state"));
                curr.setShipZip(rs.getString("zip"));
                curr.setAreaDesc(rs.getString("area_desc"));
                curr.setRegionDesc(rs.getString("region_desc"));
                curr.setDistrictDesc(rs.getString("district_desc"));
                curr.setTerritoryId(rs.getString("territory_id"));
                curr.setReportsToEmplid(rs.getString("reports_to_emplid"));
                curr.setEmplStatus(rs.getString("empl_status"));
                curr.setFieldActive(rs.getString("field_active"));               
                
                
                tempList.add(curr);
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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
        public VarianceReportBean[] getRBUVarianceReport() {
        
        //initilize to avoid null pointer - shannon
        VarianceReportBean[] ret = new VarianceReportBean[0];
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

        
        String sql = "SELECT EMPLID, FIRST_NAME, LAST_NAME,CURRENT_ROLE, CURRENT_PRODUCTS, CREDITS, FUTURE_BU, FUTURE_ROLE,FUTURE_PRODUCTS, REQUIRED_PRODUCTS, futurerbu " +
                    "FROM V_RBU_VARIANCE_REPORT ORDER BY first_name asc, last_name asc";
        System.out.println("variance size " + sql);
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
            
            st = conn.prepareCall(sql);			
			
			
            rs = st.executeQuery();
            
            VarianceReportBean curr=null;
			while (rs.next()) {
             
                curr = new VarianceReportBean();
                
                curr.setEmplID(rs.getString("emplid"));
                   System.out.println("variance  " + curr.getEmplID());
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));                

                curr.setPreRole(rs.getString("CURRENT_role"));
                curr.setPreProduct(rs.getString("CURRENT_products"));
                if(rs.getString("CREDITS")!=null){
                    curr.setCredits(rs.getString("CREDITS"));
                }
                
                curr.setFutureBU(rs.getString("FUTURE_BU"));
                curr.setPostRole(rs.getString("FUTURE_role"));
                curr.setPostProduct(rs.getString("FUTURE_products"));
                curr.setRequiredProducts(rs.getString("required_products"));
                curr.setFutureRBU(rs.getString("FUTURERBU"));
                tempList.add(curr);
			}
            
            ret = new VarianceReportBean[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (VarianceReportBean)tempList.get(i);
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
    
    
    private String replaceSpecialCharacters(String name){
        
        String replaceName = "";
        if(name.indexOf("'") != -1){
            replaceName = name.replaceAll("'", "''");
        }
        
        return replaceName;
    }
    
    
    public RBUSearchBean[] getRBUSearchResults(EmplSearchForm form) {
        
        //initilize to avoid null pointer - shannon
        RBUSearchBean[] ret = new RBUSearchBean[0];
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        boolean append = false;
        String firstName = form.getFname();
        String lastName =  form.getLname();
        System.out.println("Last name >>>>>>>>>>>> " + lastName);
        if(firstName.indexOf("'") != -1){
            firstName = replaceSpecialCharacters(firstName);
        }
         if(lastName.indexOf("'") != -1){
            lastName = replaceSpecialCharacters(lastName);
        }
        System.out.println("Last name after replace>>>>>>>>>>>> " + lastName);
        
        // Changes made for the search issue Jeevan
        // Upper case comparison of First Name and Last Name to the DB values done - 01/28/2009 - Arpit Dhir
        StringBuffer wherec = new StringBuffer(" where ");
        if(form.getFname() != null && form.getFname().length() >0){
            wherec.append(" upper(first_name) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(last_name) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        else if(form.getLname() != null && form.getLname().length() >0){
            wherec.append(" upper(first_name) LIKE '" + firstName.toUpperCase() + "%' ");
            wherec.append(" and upper(last_name) LIKE '" + lastName.toUpperCase() + "%' ");
            append = true;
        }
        if(form.getEmplid() !=null && form.getEmplid().length()>0 ){
            if(append){
                wherec.append("and  EMPLID = '" + form.getEmplid() + "' ");
            }
            else{
                wherec.append("EMPLID = '" + form.getEmplid() + "' ");
            }
            append = true;
        }
        
         if(form.getTerrId() !=null && form.getTerrId().length()>0 ){
            if(append){
                wherec.append(" and sales_position = '" + form.getTerrId() + "' ");
            }   
        
            else{
                wherec.append("sales_position = '" + form.getTerrId() + "' ");
            }
        }
        if (wherec.toString().equals(" where ")){
            wherec.setLength(0);
        }
        
        
        String sql = "SELECT EMPLID, FIRST_NAME, LAST_NAME, Employee_Status, Active, Future_Role, Sales_Position,current_products,future_products, required_products " +
                    " FROM V_RBU_SEARCH " + wherec.toString() + " ORDER BY first_name asc, last_name asc";
                    
        System.out.println("Query for search ############## " + sql);
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection(); */ 
			List tempList = new ArrayList();	
            
            st = conn.prepareCall(sql);			
			
			
            rs = st.executeQuery();
            
            RBUSearchBean curr=null;
			while (rs.next()) {
                curr = new RBUSearchBean();
                
                curr.setEmplID(rs.getString("emplid"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));     
                    
                curr.setEmployee_Status(rs.getString("Employee_Status"));
                curr.setActive(rs.getString("Active"));
                curr.setFuture_Role(rs.getString("Future_Role"));
                curr.setSales_Position(rs.getString("Sales_Position"));
                curr.setCurrent_products(rs.getString("current_products"));
                curr.setPostProduct(rs.getString("FUTURE_products"));
                curr.setRequiredProducts(rs.getString("required_products"));
                
                tempList.add(curr);
			}
            
            ret = new RBUSearchBean[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (RBUSearchBean)tempList.get(i);
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
    
    
    
    public VarianceReportBean[] getVarianceReport(String event) {
        
        //initilize to avoid null pointer - shannon
        VarianceReportBean[] ret = new VarianceReportBean[0];
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String view = "v_pwra_variance_report";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "v_pwra_variance_report";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "v_spf_variance_report";
        }else if (AppConst.EVENT_RBU.equalsIgnoreCase(event)) {
            view = "v_rbu_variance_report";
            return ret;
            //todo - return 0 lengh array for testing before view is setup - shannnon
        }
        
        String sql = "SELECT " +
                        "emplid, " +
                        "first_name, " +
                        "last_name, " +
                        "pre_cluster, " +
                        "pre_team, " +
                        "pre_role, " +
                        "pre_products, " +
                        "post_cluster, " +
                        "post_team, " +
                        "post_role, " +
                        "post_products, " +
                        "required_products " +
                    "FROM " + view + " " +
                    "ORDER BY first_name asc, last_name asc";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
            
            st = conn.prepareCall(sql);			
			
			
            rs = st.executeQuery();
            
            VarianceReportBean curr=null;
			while (rs.next()) {
                curr = new VarianceReportBean();
                
                curr.setEmplID(rs.getString("emplid"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setPreCluster(rs.getString("pre_cluster"));
                curr.setPreTeam(rs.getString("pre_team"));
                curr.setPreRole(rs.getString("pre_role"));
                curr.setPreProduct(rs.getString("pre_products"));
                curr.setPostCluster(rs.getString("post_cluster"));
                curr.setPostTeam(rs.getString("post_team"));
                curr.setPostRole(rs.getString("post_role"));
                curr.setPostProduct(rs.getString("post_products"));
                curr.setRequiredProducts(rs.getString("required_products"));
                
                tempList.add(curr);
			}
            
            ret = new VarianceReportBean[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (VarianceReportBean)tempList.get(i);
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
    
    public EmpReport[] getPersonalizedAgendaReport(String event) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        List listDates = null;
        Date trainingDate = null;
        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        
        String view = "V_PWRA_TR_SCHEDULE_REPORT";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_TR_SCHEDULE_REPORT";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_TR_SCHEDULE_REPORT";
        }
		
        //String whereClause = "WHERE loa_start_time is not null and loa_end_time is not null ";
        String whereClause = "";
        String sql = "SELECT distinct " +
                        "product_cd, " +
                        "product_desc, " +
                        "trunc(loa_start_time) as startDate, " +
                        "trunc(loa_end_time) as endDate, " +
                        "trainee_ssn, " +
                        "first_name, " +
                        "last_name, " +
                        "preferred_name, " +
                        "cluster_cd, " +
                        "cluster_desc, " +                        
                        "team_cd, " +
                        "team_desc, " +
                        "role_cd " +                        
                    "FROM " + view + " " +
                    whereClause +
                    "ORDER BY first_name, last_name, product_desc, startDate, endDate";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
			st = conn.prepareCall(sql);			
			
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                startDate = rs.getDate("startDate");
                endDate = rs.getDate("endDate");
                
                if (endDate == null) {
                    endDate = startDate;
                }
                else if (startDate == null) {
                    startDate = endDate;
                }                
                
                    
                if (startDate != null) {
                    listDates = Util.getDatesBetween(new java.util.Date(startDate.getTime()),new java.util.Date(endDate.getTime()));
                }
                else {
                    listDates = new ArrayList();
                    //listDates.add(null);
                }
                for (int i=0; i<listDates.size(); i++) {
                    trainingDate = (Date)listDates.get(i);
                                    
                    curr = new EmpReport();                        
                    curr.setProductCode(rs.getString("product_cd"));
                    curr.setProductDesc(rs.getString("product_desc"));                        
                    curr.setTrainingDate(trainingDate);
                    curr.setEmplId(rs.getString("trainee_ssn"));
                    curr.setFirstName(rs.getString("first_name"));
                    curr.setLastName(rs.getString("last_name"));
                    curr.setPreferredName(rs.getString("preferred_name"));
                    curr.setClusterCode(rs.getString("cluster_cd"));                
                    curr.setClusterDesc(rs.getString("cluster_desc"));                                
                    curr.setTeamCode(rs.getString("team_cd"));
                    curr.setTeamDesc(rs.getString("team_desc"));
                    curr.setRole(rs.getString("role_cd"));
                    
                    tempList.add(curr);
                }
                
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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
    
    public EmpReport[] getClassRosterReport(String event, ClassFilterForm form) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		
        
        String view = "V_PWRA_CLASS_ROSTER";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_CLASS_ROSTER";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_CLASS_ROSTER";
        }
        
        String whereClause = "";
        
        String classroom = form.getClassRoom();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        
        String productCode = form.getProduct();
        
        whereClause = "NVL(loa_start_time, loa_end_time) is not null ";
        if (!"All".equalsIgnoreCase(form.getTrainingDate())) {
            if (whereClause.equals("")) {   
                whereClause += "to_date('" +  form.getTrainingDate() + "','mm/dd/yyyy') " +
                            "between trunc(loa_start_time) and trunc(loa_end_time) ";
            }
            else {
                whereClause += "AND to_date('" +  form.getTrainingDate() + "','mm/dd/yyyy') " +
                            "between trunc(loa_start_time) and trunc(loa_end_time) ";
            }
        }
        
        if (!"All".equalsIgnoreCase(classroom)) {
            if (whereClause.equals("")) {                
                whereClause += "classroom = '" + classroom + "' ";
            }
            else {
                whereClause += "AND classroom = '" + classroom + "' ";
            }
        }
        
        if (!"All".equalsIgnoreCase(productCode)) {
            if (whereClause.equals("")) {                
                whereClause += "product_cd = '" + productCode + "' ";
            }
            else {
                whereClause += "AND product_cd = '" + productCode + "' ";
            }
        }
        if (!whereClause.equals("")) { 
            whereClause = "WHERE " + whereClause;
        }
        String sql = "SELECT distinct " +
                        /*"product_cd, " +
                        "product_desc, " +
                        "course_id, " +
                        "course_description, " +
                        "trunc(loa_start_time) as startDate , " +
                        "trunc(loa_end_time) as endDate , " +*/
                        "trainee_ssn, " +
                        "first_name, " +
                        "last_name, " +
                        "preferred_name, " +
                        "cluster_cd, " +
                        "cluster_desc, " +                        
                        "team_cd, " +
                        "team_desc, " +
                        "role_cd, " +
                        "address1, " +
                        "address2, " +
                        "city, " +
                        "state, " +
                        "zip, " +
                        "area_cd, " +
                        "area_desc, " +
                        "region_cd, " +
                        "region_desc, " +
                        "district_id, " +
                        "district_desc, " +
                        "territory_id, " +
                        "reports_to_emplid, " +
                        "field_active, " +
                        "empl_status " +                        
                    "FROM " + view + " " + whereClause +                                        
                    "ORDER BY first_name, last_name";        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                curr = new EmpReport();
                
                /*curr.setProductCode(rs.getString("product_cd"));
                curr.setProductDesc(rs.getString("product_desc"));
                curr.setCourseId(new Integer(rs.getInt("course_id")));
                curr.setCourseDesc(rs.getString("course_description"));
                curr.setStartDate(rs.getDate("startDate"));
                curr.setEndDate(rs.getDate("endDate"));*/
                curr.setEmplId(rs.getString("trainee_ssn"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setPreferredName(rs.getString("preferred_name"));
                curr.setClusterCode(rs.getString("cluster_cd"));                
                curr.setClusterDesc(rs.getString("cluster_desc"));                                
                curr.setTeamCode(rs.getString("team_cd"));
                curr.setTeamDesc(rs.getString("team_desc"));
                curr.setRole(rs.getString("role_cd"));
                curr.setShipAdd1(rs.getString("address1"));
                curr.setShipAdd2(rs.getString("address2"));
                curr.setShipCity(rs.getString("city"));
                curr.setShipState(rs.getString("state"));
                curr.setShipZip(rs.getString("zip"));
                curr.setAreaDesc(rs.getString("area_desc"));
                curr.setRegionDesc(rs.getString("region_desc"));
                curr.setDistrictDesc(rs.getString("district_desc"));
                curr.setTerritoryId(rs.getString("territory_id"));
                curr.setReportsToEmplid(rs.getString("reports_to_emplid"));
                curr.setEmplStatus(rs.getString("empl_status"));
                curr.setFieldActive(rs.getString("field_active"));               
                
                
                tempList.add(curr);
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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

    public ClassRosterBean[] getClassData(String event, String productCode) {
        ClassRosterBean[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        
        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
		
        String view = "V_PWRA_CLASS_ROSTER";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_CLASS_ROSTER";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_CLASS_ROSTER";
        }
        
        String whereClause = "";
        if (productCode != null && !"".equals(productCode)) {
            whereClause = "WHERE product_cd = '" + productCode + "'";
        }
        String sql = "SELECT distinct " +
                        "classroom, " +
                        "trunc(loa_start_time) as startDate, " +
                        "trunc(loa_end_time) as endDate, " +
                        "product_cd, " +
                        "product_desc " +                      
                    "FROM " + view + " " +
                    whereClause +                      
                    "ORDER BY classroom, startDate, product_desc ";
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
			st = conn.prepareCall(sql);			
			
			
            rs = st.executeQuery();
            
            ClassRosterBean curr=null;
			while (rs.next()) {
                startDate = rs.getDate("startDate");
                endDate = rs.getDate("endDate");
                
                if (endDate == null) {
                    endDate = startDate;
                }
                else if (startDate == null) {
                    startDate = endDate;
                }  
                    
                if (startDate != null) { 
                    curr = new ClassRosterBean();   
                    
                    curr.setClassroom(rs.getString("classroom"));  
                    curr.setStartDate(new Date(startDate.getTime()));
                    curr.setEndDate(new Date(endDate.getTime()));
                    curr.setProductCode(rs.getString("product_cd"));
                    curr.setProductDesc(rs.getString("product_desc"));                                           
                    
                    tempList.add(curr);
                }
                    
            }
			
            
            ret = new ClassRosterBean[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {                
                ret[i] = (ClassRosterBean)tempList.get(i);
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
    
    
    public EmpReport[] getAttendanceReport(String event, ClassFilterForm form) {
        EmpReport[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String view = "V_PWRA_ATTENDANCE";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "V_PWRA_ATTENDANCE";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "V_SPF_ATTENDANCE";
        }
        
        String whereClause = "";
        
        String classroom = form.getClassRoom();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        
        String productCode = form.getProduct();
        
        
        if (!"All".equalsIgnoreCase(form.getTrainingDate())) {
            whereClause = "to_date('" +  form.getTrainingDate() + "','mm/dd/yyyy') " +
                        "between trunc(loa_start_time) and trunc(loa_end_time) ";
        }
        
        if (!"All".equalsIgnoreCase(classroom)) {
            if (whereClause.equals("")) {                
                whereClause += "classroom = '" + classroom + "' ";
            }
            else {
                whereClause += "AND classroom = '" + classroom + "' ";
            }
        }
        
        if (!"All".equalsIgnoreCase(productCode)) {
            if (whereClause.equals("")) {                
                whereClause += "product_cd = '" + productCode + "' ";
            }
            else {
                whereClause += "AND product_cd = '" + productCode + "' ";
            }
        }
        if (!whereClause.equals("")) { 
            whereClause = "WHERE " + whereClause;
        }
        String sql = "SELECT distinct " +
                        "trainee_ssn, " +
                        "first_name, " +
                        "last_name, " +
                        "preferred_name, " +
                        "cluster_cd, " +
                        "cluster_desc, " +                        
                        "team_cd, " +
                        "team_desc, " +
                        "role_cd, " +
                        "status " +
                    "FROM " + view + " " + whereClause +                                        
                    "ORDER BY first_name, last_name";       
        
        try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection(); */ 
			List tempList = new ArrayList();	
			st = conn.prepareCall(sql);			
			//st.setFetchSize(5000);
			
            rs = st.executeQuery();
            
            EmpReport curr=null;
			while (rs.next()) {
                curr = new EmpReport();
                
                curr.setEmplId(rs.getString("trainee_ssn"));
                curr.setFirstName(rs.getString("first_name"));
                curr.setLastName(rs.getString("last_name"));
                curr.setPreferredName(rs.getString("preferred_name"));
                curr.setClusterCode(rs.getString("cluster_cd"));                
                curr.setClusterDesc(rs.getString("cluster_desc"));                                
                curr.setTeamCode(rs.getString("team_cd"));
                curr.setTeamDesc(rs.getString("team_desc"));
                curr.setRole(rs.getString("role_cd"));
                curr.setAttendanceStatus(rs.getString("status"));
                                
                tempList.add(curr);
			}
            
            ret = new EmpReport[tempList.size()];
            for (int i=0; i<tempList.size(); i++) {
                ret[i] = (EmpReport)tempList.get(i);
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
    
    
    public GeneralSessionEmployee[] getGeneralSessionEmployees(String event) { 
        GeneralSessionEmployee[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String thisSection="";
        String[] params = new String[0];
        
        String view = "";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            view = "300 AND 399";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            view = "400 AND 499";
        }
       
       
        String sql = "SELECT distinct " +
                        "PD.TEAM_DESC, " +
                        "PD.LAST_NAME, " +
                        "PD.FIRST_NAME, " +
                        "PD.ROLE_CD, " +
                        "PD.DISTRICT_DESC, " +
                        "PD.EMPLID, " +
                        "PD.COURSE_ID, " +
                        "PD.START_DATE, " +
                        "decode(PA.STATUS, null, '', '','P', 'P','P') as ATTENDED " +
                     "from " +
                    "(SELECT ca.trainee_ssn AS emplid, ca.course_id, c.start_date, ra.team_desc, fe.last_name, fe.first_name, ra.role_cd, ra.district_desc FROM " +
                    "course_assignment  ca, " +
                    "course c, " +
                    "MV_FIELD_EMPLOYEE FE, " +
                    "v_realignment ra " +
                    "WHERE ca.trainee_ssn = fe.emplid " +
                    "AND ca.trainee_ssn = ra.emplid " +
                    "AND ca.course_id = c.course_id " +
                    "AND c.course_id BETWEEN " + view + " " +
                    "AND c.product_cd = 'PLCA') " +
                    "PD left outer join PWRA_SPF_PLC_ATTENDANCE PA " +
                    "on PA.EMPLID = PD.EMPLID and PA.COURSE_ID = PD.COURSE_ID " +
                    "order by PD.FIRST_NAME";
		
		try {
           /* Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */
			List tempList = new ArrayList();	
			st = conn.prepareCall(sql);						
			
            rs = st.executeQuery();
            
            GeneralSessionEmployee curr=null;
			while (rs.next()) {
                curr = new GeneralSessionEmployee();
                
                curr.setTeamCode(rs.getString("TEAM_DESC"));
                curr.setLastName(rs.getString("LAST_NAME"));
                curr.setFirstName(rs.getString("FIRST_NAME"));
                curr.setRole(rs.getString("ROLE_CD"));
                curr.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                curr.setEmplId(rs.getString("EMPLID"));                
                curr.setAttended(rs.getString("ATTENDED"));                                
                curr.setCourseId(new Integer(rs.getInt("COURSE_ID")));
                curr.setStartDate(rs.getDate("START_DATE"));
                                
                tempList.add(curr);
			}
            
            ret = new GeneralSessionEmployee[tempList.size()];
			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (GeneralSessionEmployee)tempList.get(j);			
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
    
    public void batchUpdateAttendance(String event, HashMap hashUpdate, String sUser)
    {
		ResultSet rs = null;
		PreparedStatement st = null;
		//Connection conn = null;
        Statement batch = null;
        Connection conn = JdbcConnectionUtil.getJdbcConnection();
        String course_id = "";
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            course_id = "300";
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            course_id = "400";
        }
        
		try 
        {
		 /* Context ctx = new InitialContext();		  
		  DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
          conn =   ds.getConnection(); */ 
			       
          Iterator iterDiff= hashUpdate.entrySet().iterator();
           
          final String sSQLInsertAttendanceTempl =   "insert into pwra_spf_plc_attendance values('InsertEmplID','InsertCourseID','Status', SYSDATE,'InsertLastUpdBy')";
          final String sSQLdDeleteAttendanceTempl = "delete pwra_spf_plc_attendance where emplid = 'DeleteEmplID' and course_id = 'DeleteCourseID'";            
            
          batch = conn.createStatement();        
          while(iterDiff.hasNext())
          {
              Map.Entry entry    = (Map.Entry)iterDiff.next();
              //Employee_Course is the key and the Attendance is the value
              //String sEmplID = (String)entry.getKey();
              String sEmplID_CourseID = (String)entry.getKey();
              String sEmplID = sEmplID_CourseID.substring(0,sEmplID_CourseID.indexOf("_"));
              course_id = sEmplID_CourseID.substring(sEmplID_CourseID.indexOf("_") + 1);
              String sAttendance = (String)entry.getValue();
              String sSQLInsertAttendance = sSQLInsertAttendanceTempl;
              String sSQLdDeleteAttendance = sSQLdDeleteAttendanceTempl;
              if(sAttendance.equalsIgnoreCase("P"))
              {
                    //Insert into table
                  sSQLInsertAttendance = sSQLInsertAttendance.replaceAll("InsertEmplID", sEmplID);
                  sSQLInsertAttendance = sSQLInsertAttendance.replaceAll("InsertCourseID", course_id);
                  sSQLInsertAttendance = sSQLInsertAttendance.replaceAll("InsertLastUpdBy", sUser);
                  sSQLInsertAttendance = sSQLInsertAttendance.replaceAll("Status", "P");                    
                  batch.addBatch(sSQLInsertAttendance);
              }else{
                  sSQLdDeleteAttendance = sSQLdDeleteAttendance.replaceAll("DeleteEmplID", sEmplID);
                  sSQLdDeleteAttendance = sSQLdDeleteAttendance.replaceAll("DeleteCourseID", course_id);
                  batch.addBatch(sSQLdDeleteAttendance);
              }
          }
          
          int[] results  = batch.executeBatch();
       }
       catch(SQLException e)
       {
         e.printStackTrace();         
       }
     
       finally {
			if ( rs != null) 
            {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( batch != null) 
            {
				try {
					batch.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) 
            {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
       }
    }
} 

