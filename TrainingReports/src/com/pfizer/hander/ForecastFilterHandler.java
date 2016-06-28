package com.pfizer.hander; 

import com.pfizer.db.ForecastReport;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
//import weblogic.webservice.tools.pagegen.result;




public class ForecastFilterHandler 
{ 
        protected static final Log log = LogFactory.getLog(ForecastFilterHandler.class );

    public ForecastFilterHandler () {

    }
public boolean insertTrainingReports(ForecastReport track, String menuId) {
        
        
        List result = DBUtil.executeSql("Select TRAINING_REPORT_ID_SEQ.nextval as nextid from dual",AppConst.APP_DATASOURCE);
        Map map = (Map)result.get(0);

              
        String insertSql = "insert into  training_report (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) " +
                        "values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        
        try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);
        
            statement.setString(1,track.getTrackLabel());
            statement.setString(2,"/TrainingReports/ForecastReport/begin.do?track="+track.getTrackId());
            statement.setBigDecimal( 3, new BigDecimal(menuId) );
		    statement.setString( 4, track.getTrackId() );
     
            log.debug("forecast Final SQL---\n"+insertSql);
			log.info(insertSql);
           
			int num = statement.executeUpdate();
            if(num>0)
            {
                return true;
            }
        }
        catch(Exception e)
        {
            log.error(e,e);
        }
        finally {
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

public void updateTrainingReports(ForecastReport track) {
		String retString = null;
		String insertSql = "update  training_report set " +
                        "   training_report_label=? " +
                        "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
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
public String insertTrack(String name){
        
        
        
        List result = DBUtil.executeSql("Select FORECAST_TRACK_ID_SEQ.nextval as nextforecastid from dual",AppConst.APP_DATASOURCE);
        Map map = (Map)result.get(0);

        String retString = null;

        String insertSql = " insert into FORECAST_TRACK " + 
                           " (TRACK_ID,TRACK_LABEL,TRACK_TYPE) "+
                           " values(?,?,'Forecast') ";
        ResultSet rs=null;
        PreparedStatement statement = null;
		/*Connection conn = null;*/
        String nextValue=null;
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try{                    
        /*Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn = ds.getConnection();*/
        statement  = conn.prepareStatement(insertSql);
        
        nextValue = "FORECAST_" + (BigDecimal)map.get("NEXTFORECASTID");
       // System.out.println("nextValue = "+nextValue);
         
        //statement.setBigDecimal( 1, (BigDecimal)map.get("NEXTFORECASTID") );
        statement.setString(1, nextValue);				
        statement.setString(2, name);
    
        System.out.println(statement);
        log.debug("forecast Final SQL---\n"+insertSql);
        
		statement.executeUpdate();
       
    
        return 	nextValue;
		} 
        catch (Exception e) {
			log.error(e,e);
		} 
        finally {
           
                if ( rs != null) {
                    try {
                        rs.close();
                        } 
                    catch ( Exception e2) {
                        log.error(e2,e2);
                    }
                }
                if ( statement != null) {
                    try {
                        statement.close();
                    } 
                    catch ( Exception e2) {
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
         
        return 	"";
	}

    public List getForecastOptionalFields(String trackID){
        System.out.println("In getForecastOptionalFields="+trackID);
        String sql = " select * from Forecast_optional_fields where track_id = '"+trackID+"'" ;
        System.out.println("SQL == "+sql);
        List result = executeSql2(sql);
        return result;
        
    }
    public List executeSql2( String sql ) {

        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        return result;
    }
    
     public boolean insertForecastOptionalFields(ForecastReport track) {
                      
        String insertSql = "insert into  Forecast_optional_fields (track_id, Gender,Manager_email,Source,Promotion_Date,Hire_Date, Employee_ID,GUID, Geography_Descr,Regional_Office_state, Products) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?) ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */ 
        try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);
        
            statement.setString(1,track.getTrackId());
            statement.setString(2,track.getGender()?"Yes":"No");
            statement.setString(3,track.getManagerEmail()?"Yes":"No");
            statement.setString(4,track.getSource()?"Yes":"No");
            statement.setString(5,track.getPromDate()?"Yes":"No");
            statement.setString(6,track.getHirDate()?"Yes":"No");
            statement.setString(7,track.getEmployeeId()?"Yes":"No");
            statement.setString(8,track.getGuId()?"Yes":"No");
            statement.setString(9,track.getGeographyDesc()?"Yes":"No");
            statement.setString(10,track.getRegionalOfficeState()?"Yes":"No");
            statement.setString(11,track.getProducts()?"Yes":"No");
     
            log.debug("forecast Final SQL---\n"+insertSql);
			log.info(insertSql);
           
			int num = statement.executeUpdate();
            if(num>0)
            {
                return true;
            }
        }
        catch(Exception e)
        {
            log.error(e,e);
            return false;
        }
        finally {
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
        return true;
    }

public int updateForecastOptionalFields(ForecastReport track) {
		String retString = null;
		String insertSql = "update  Forecast_optional_fields set " +
                        "   Gender=? " +
                        ",Manager_email=? " +
                        ",Source=? " +
                        ",Promotion_Date=? " +
                        ",Hire_Date=? " +
                        ", Employee_ID=? " +
                        ",GUID=? " +
                        ", Geography_Descr=? " +
                        ",Regional_Office_state=? " +
                        ", Products=? " +
                        "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        int num=0;
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

            statement.setString(1,track.getGender()?"Yes":"No");
            statement.setString(2,track.getManagerEmail()?"Yes":"No");
            statement.setString(3,track.getSource()?"Yes":"No");
            statement.setString(4,track.getPromDate()?"Yes":"No");
            statement.setString(5,track.getHirDate()?"Yes":"No");
            statement.setString(6,track.getEmployeeId()?"Yes":"No");
            statement.setString(7,track.getGuId()?"Yes":"No");
            statement.setString(8,track.getGeographyDesc()?"Yes":"No");
            statement.setString(9,track.getRegionalOfficeState()?"Yes":"No");
            statement.setString(10,track.getProducts()?"Yes":"No");
            statement.setString(11,track.getTrackId());
            
            System.out.println("track.getGender()="+track.getGender());
            
			num = statement.executeUpdate();
            System.out.println("update:" + num);
            
            
        } catch (Exception e) {
			log.error(e,e);
            return 0;
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
        return num;
    }
    
   //////////start--merge for admin config of forecast/////////// 
    public List getAllRoleCode() {
	List ret = new ArrayList();
        String sql = " select distinct role_cd,role_desc from mv_field_employee_rbu where role_cd is not null order by role_desc";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
   public List getForecastFilterData(String trackId){
        List courses= new ArrayList();
        System.out.println("getForecastFilterData="+trackId);
        String sql = "select FORECAST_REPORT_ID,FORECAST_REPORT_LABEL, role_cd,to_char(start_date,'mm/dd/yyyy') as start_date, "
                +"to_char(end_date,'mm/dd/yyyy') as end_date, hire_or_promotion_date, "
                +" duration,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES, NOT_REGISTERED_COURSES from Forecast_filter_criteria where forecast_report_ID = '"+trackId+"'";
        List result = executeSql2(sql);
        System.out.println("getForecastFilterData"+sql);
        System.out.println("db results from getForecastFilterData = "+result.toString());
        return result;

    }
    
    public int updateFilterData(HttpSession session){
      
        System.out.println("in updateFilterData");
        System.out.println(" role_cd = "+session.getAttribute("ROLE_CD"));//role);
        System.out.println("in startDate ="+session.getAttribute("START_DATE"));
        System.out.println("in endDate="+session.getAttribute("END_DATE"));
        System.out.println("hire_or_promotion_date="+session.getAttribute("hire_or_promotion_date"));
        System.out.println("in duration="+session.getAttribute("DURATION"));
        System.out.println("in trackId="+session.getAttribute("trackID"));
        
        String sql="update forecast_filter_criteria set "
                    +"role_cd=? "
                    +",start_date=to_date(? ,'mm/dd/yyyy') "
                    +",end_date=to_date(? ,'mm/dd/yyyy') "
                      +",hire_or_promotion_date=? "
                    +",duration=? "
                    +",COMPLETED_COURSES=? "
                    +",NOT_COMPLETED_COURSES=? "
                    +",REGISTERED_COURSES=? "
                    +",NOT_REGISTERED_COURSES=? "
                    +"where forecast_report_id=? ";
        PreparedStatement statement = null;
	/*	Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        int num=0;
        try{
           /* Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(sql);
            statement.setString(1,(String)session.getAttribute("ROLE_CD"));
            statement.setString(2,(String)session.getAttribute("START_DATE"));
            statement.setString(3,(String)session.getAttribute("END_DATE"));
            statement.setString(4,(String)session.getAttribute("HIRE_OR_PROMOTION_DATE"));
            statement.setString(5,(String)session.getAttribute("DURATION"));
            statement.setString(6,(String)session.getAttribute("Completed_id"));
            statement.setString(7,(String)session.getAttribute("NotCompleted_id"));
            statement.setString(8,(String)session.getAttribute("Registered_id"));
            statement.setString(9,(String)session.getAttribute("NotRegistered_id"));
            statement.setString(10,(String)session.getAttribute("trackID"));
           num = statement.executeUpdate();
            
        }
        catch(Exception e){
            log.error(e,e);
            e.printStackTrace();
            return 0;
        }
        finally{
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
        return num;
    }
   public boolean insertFilterData(HttpSession session){
         
        String sql="insert into forecast_filter_criteria "+
                    "(FORECAST_REPORT_ID,FORECAST_REPORT_LABEL,ROLE_CD,START_DATE,END_DATE,HIRE_OR_PROMOTION_DATE,DURATION,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES, NOT_REGISTERED_COURSES)"
                    +"values(?,?,?,to_date(? ,'mm/dd/yyyy'),to_date(? ,'mm/dd/yyyy'),?,?,?,?,?,?)";
        PreparedStatement statement = null;
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        System.out.println("tempStatus = "+session);
        
        
        try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(sql);
              statement.setString(1,(String)session.getAttribute("trackID"));
              statement.setString(2,(String)session.getAttribute("trackName"));
              statement.setString(3,(String)session.getAttribute("ROLE_CD"));
            statement.setString(4,(String)session.getAttribute("START_DATE"));
            statement.setString(5,(String)session.getAttribute("END_DATE"));
            statement.setString(6,(String)session.getAttribute("HIRE_OR_PROMOTION_DATE"));
            statement.setString(7,(String)session.getAttribute("DURATION"));
            statement.setString(8,(String)session.getAttribute("Completed_id"));
            statement.setString(9,(String)session.getAttribute("NotCompleted_id"));
            statement.setString(10,(String)session.getAttribute("Registered_id"));
            statement.setString(11,(String)session.getAttribute("NotRegistered_id"));
            
            statement.executeUpdate();
            
        }
        catch(Exception e){
            log.error(e,e);
            e.printStackTrace();
            return false;
        }
        finally{
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
        System.out.println("insertFilterData updated");
        return true;
    }
    
    public List getCourseCompleted(String name){
        String uName=name.toUpperCase();
        String sql="select distinct activityname,activityfk as activityid from mv_usp_completed "
                    +"where upper(activityname) like '%"+uName+"%'";
        List result=executeSql2(sql);
        log.info(sql);
        return result;
    }
    public List getCourseRegistered(String name){
        String uName=name.toUpperCase();
        String sql="select distinct activityname,activity_pk as activityid from mv_usp_registered "
                    +"where upper(activityname) like '%"+uName+"%'";
        List result=executeSql2(sql);
        log.info(sql);
        return result;
    }
    public List getCourseNotCompleted(String name){
        String uName=name.toUpperCase();
        String sql="select distinct activityname,activityfk as activityid from mv_usp_pending "
                    +"where upper(activityname) like '%"+uName+"%'";
        List result=executeSql2(sql);
        log.info(sql);
        return result;
    }
    public List getCourseNotRegistered(String name){
        String uName=name.toUpperCase();
        String sql="select distinct activityname,activity_pk as activityid from mv_usp_assigned "
                    +"where upper(activityname) like '%"+uName+"%'";
        List result=executeSql2(sql);
        log.info(sql);
        return result;
    }
    // not reg is assigned
    // not comp is pending
    
    
    public HashMap getActivityIDAndDesc(String Id){
        HashMap activityDataMap=new HashMap();
            HashMap idActivityMap=new HashMap();
        if(Id!=null){
            String idStr = Id.replaceAll("AND",",");
            idStr= idStr.replaceAll(", OR ,",",");
            System.out.println("idStr="+idStr);
            String sql = "Select distinct ACTIVITYNAME, ACTIVITY_PK from mv_usp_activity_hierarchy where activity_pk in ("+ idStr +")";
            System.out.println("sql="+sql);
            List result=executeSql2(sql);
            
                if(result.size()!=0){
                for(int i = 0; i<result.size(); i++){
                    activityDataMap=(HashMap)result.get(i);
                    idActivityMap.put(activityDataMap.get("ACTIVITY_PK").toString(),(String)activityDataMap.get("ACTIVITYNAME"));
                   //System.out.println("activity map = "+activityDataMap.toString());
                 //System.out.println("Activity Name= "+(String)activityDataMap.get("ACTIVITYNAME"));
                 //System.out.println("Activity Id= "+activityDataMap.get("ACTIVITY_PK").toString());//2809387
        
                 //String key = activityDataMap.get("ACTIVITY_PK").toString();
                 //System.out.println("Activity value = "+idActivityMap.get(key));
                 //System.out.println("Activity value 2  = "+activityDataMap.get(activityDataMap.get("ACTIVITY_PK")));
                }
                }
        
            idActivityMap.put("OR","OR");
            idActivityMap.put("AND","AND");
        System.out.println("result of getActivityIDAndDesc= "+result.toString());
        }
        return idActivityMap;
    }
//////////end--merge for admin config of forecast/////////// 
} 
