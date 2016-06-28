package com.pfizer.hander; 

import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.tgix.html.FormUtil;
import com.tgix.printing.LoggerHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;











// Added fro TRT Major Enhancement 3.6 (Management Summary Report)
import java.sql.*;
public class ManagementFilterHandler 
{ 
    protected static final Log log = LogFactory.getLog(ManagementFilterHandler.class );
   
    public ManagementFilterHandler(){
     //   System.out.println("Inside mgt reports");
    }
   
    public List getQueryResult(String sql)
    {
        System.out.println("Management report Query=="+sql);
        List result = executeSql(sql);//retrieve columns in a order
      //  System.out.println("result in query"+result);
        log.info(sql);
        return result;
    }
    public List getFilterType(String trackId)
   {
        String sql = " Select distinct filter_type from management_code_desc where filter_id='"+trackId+"'";
        List result = executeSql2(sql);
        return result;
   } 
   
   public List getActivityIdList(String trackId)
   {
        String sql = " select filter_code from management_code_desc where filter_type = 'activityfk' and filter_id='"+trackId+"' and filter_code is not null ";
        List result = executeSql2(sql);
        return result;
   }
   public List getCourseCodeList(String trackId)
   {
        String sql = " select filter_description from management_code_desc where filter_type = 'activityfk' and filter_id='"+trackId+"' ";
        List result = executeSql2(sql);
        return result;
   }
   public List getSelectedCourseCodeList(String trackId,String activityId)
   {
       List result=new ArrayList();
        if(activityId!=null){
        String sql = " select filter_code,filter_description from management_code_desc where filter_type = 'activityfk' and filter_code in ("+ activityId +") and filter_id='"+trackId+"' ";
        result = executeSql2(sql);
        System.out.println("SQL"+sql);
      //  System.out.println("Result :"+ result);
        }
        return result;
   }
   
    public List getRoleCodes(){
        System.out.println("Inside rolecodes");
        List ret = new ArrayList();
        String sql = "  Select distinct role_cd, role_desc from mv_field_employee_rbu where role_cd is not null and sales_position_type_cd ='PFE'  and role_desc is not null order by role_desc asc ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    
    public List getSalesOrg()
    {
        System.out.println("Inside sales org");
        List ret =new ArrayList();
        String sql = " Select distinct sales_group,group_cd from mv_field_employee_rbu where group_cd is not null and sales_position_type_cd ='PFE' and sales_group is not null order by sales_group asc ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }  
    
    public List getBusinessUnit()
    {
        System.out.println("Inside Bu");
        List ret =new ArrayList();
        String sql = " Select distinct bu from mv_field_employee_rbu where bu is not null and sales_position_type_cd ='PFE' order by bu asc ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    
    public List getCourseCodes()
    {
        List ret =new ArrayList();
     //   String sql = " Select distinct activityfk,activityName from mv_usp_completed ";
        String sql = " Select distinct activity_pk,activityName from mv_usp_activity_hierarchy ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    
    public List getSelectedManagementFilter(String trackId)
    {
     //   System.out.println(trackId+"+inside mangt filter criteria");
        List ret= new ArrayList();
        String sql = " Select filter_id,report_label,sales_org,gender,to_char(hire_start_date,'mm/dd/yyyy') as hire_start_date,to_char(hire_end_date,'mm/dd/yyyy') as hire_end_date,course_code,to_char(training_completion_start_date,'mm/dd/yyyy') as training_completion_start_date, to_char(training_completion_end_date,'mm/dd/yyyy') as training_completion_end_date, to_char(training_reg_start_date,'mm/dd/yyyy') as training_reg_start_date, to_char(training_reg_end_date,'mm/dd/yyyy') as training_reg_end_date, business_unit, role_code,first_group_by, second_group_by, third_group_by,fourth_group_by,fifth_group_by,sixth_group_by from Management_filter_criteria where filter_id= '"+trackId+"'";
//        String sql = " Select * from Management_filter_criteria where filter_id= '"+trackId+"'";
     /*   String sql = " Select FILTER_ID,REPORT_LABEL,HIRE_START_DATE,HIRE_END_DATE,"+
        "TRAINING_COMPLETION_START_DATE,TRAINING_COMPLETION_END_DATE,TRAINING_REG_START_DATE,"+
        "TRAINING_REG_END_DATE,FIRST_GROUP_BY,SECOND_GROUP_BY,THIRD_GROUP_BY,FOURTH_GROUP_BY,"+
        "FIFTH_GROUP_BY from Management_filter_criteria where filter_id= '"+trackId+"'"; */
        List result = executeSql2(sql);
        log.info(sql);
        return result;
    }
    
    
    public ManagementSummaryReport getTrack(String trackId) {
        System.out.println(trackId);
        
        String sql = "Select track_id as trackid, track_label as trackLabel, track_type as trackType from management_track where track_id='" + trackId + "'";
    //   String sql = "Select track_id as trackid, training_report_label as trackLabel, track_type as trackType from management_track where track_id='" + trackId + "'";
        List result = executeSql2(sql);
        
        ManagementSummaryReport track = null;
        if ( result != null && result.size() > 0 ) {
            track = new ManagementSummaryReport();
            FormUtil.loadObject((Map)result.get(0),track,true);
          
           for (Iterator it = result.iterator(); it.hasNext();){
               Map res = (Map)it.next();
               String str = (String) res.get("TRACKID"); 
             //  System.out.println("+++"+str);
             //  System.out.println((String)res.get("TRACKLABEL"));
               track.setTrackId((String)res.get("TRACKID"));
               track.setTrackLabel((String)res.get("TRACKLABEL"));
           }
        }
      return track;
    }
    
    public void insertManagementFilterCriteria(ManagementSummaryReport track)
    {
/*        String insertSql = " insert into Management_Filter_criteria( Filter_Id, Report_Label, Sales_Org, Gender, Hire_Start_date, Hire_End_date, Course_code, " +
                     " Training_Completion_Start_date, Training_Completion_End_date, Training_Reg_Start_date, Training_Reg_End_date, "+
                     " Business_unit, Role_Code) values(Management_Filter_id_seq.nextval,?,?,?,to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),?,to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),?,?) "; */
         System.out.println("inside insert");
        
        String insertSql = " insert into management_filter_criteria( filter_id, report_label, sales_org, gender, hire_start_date, hire_end_date, course_code, " +
                     " training_completion_start_date, training_completion_end_date, training_reg_start_date, training_reg_end_date, "+
                     " business_unit, role_code,first_group_by,second_group_by,third_group_by,fourth_group_by,fifth_group_by,sixth_group_by) values(?,?,?,?,to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),?,to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),to_date(?,'mm/dd/yyyy'),?,?,?,?,?,?,?,?) ";
        
        ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        
        try{
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);
           
            System.out.println(track.getHireStartDate()+"Hire date");
           
            statement.setString(1,track.getTrackId());
            statement.setString(2,track.getTrackLabel());
            statement.setString(3,track.getSalesOrg());
            statement.setString(4,track.getGender());
            statement.setString(5,track.getHireStartDate().toString());
            statement.setString(6,track.getHireEndDate().toString());
            statement.setString(7,track.getCourseCode());
            statement.setString(8,track.getTrainingCompletionStartdate().toString());
            statement.setString(9,track.getTrainingCompletionEndDate().toString());
            statement.setString(10,track.getTrainingRegistrationStartDate().toString());
            statement.setString(11,track.getTrainingRegistrationEndDate().toString());
            statement.setString(12,track.getBusinessUnit());
            statement.setString(13,track.getRoleCode());
            statement.setString(14,track.getGroupBy1());
            statement.setString(15,track.getGroupBy2());
            statement.setString(16,track.getGroupBy3());
            statement.setString(17,track.getGroupBy4());
            statement.setString(18,track.getGroupBy5());
            statement.setString(19,track.getGroupBy6());
            
            log.info(insertSql);
			int num = statement.executeUpdate();
           /* if ( num > 0 ) {
                return true;
            }*/
           } 
        catch(Exception e){
            log.error(e,e);
        }
        finally{
                if ( statement != null){
                 try {
                     statement.close();
                     } catch ( Exception e2){
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
//        return false;
    }
    
    public List executeSql2( String sql ) {
        
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        
        return result;
    }
    
    public void updateManagementFilterCriteria(ManagementSummaryReport track)
    {    
       
        
        String insertSql = "update management_filter_criteria set report_label= ? ,business_unit = ? , role_code = ? ,sales_org = ? , gender = ? ,course_code = ? ,hire_start_date = to_date(?,'mm/dd/yyyy') ,hire_end_date = to_date(?,'mm/dd/yyyy') ,training_completion_start_date = to_date(?,'mm/dd/yyyy') ,training_completion_end_date = to_date(?,'mm/dd/yyyy') ,training_reg_start_date = to_date(?,'mm/dd/yyyy') ,training_reg_end_date = to_date(?,'mm/dd/yyyy') ,first_group_by = ? ,second_group_by = ?  ,third_group_by = ?  ,fourth_group_by = ? ,fifth_group_by = ?,sixth_group_by = ?  where filter_id = ?";
       // " where filter_id = ? ";
    
       
        //String insertSql = "update management_filter_criteria set sales_org = ?  where filter_id = ? ";
       
        ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        
        try{
            
            System.out.println("sales org value"+track.getSalesOrg());
            /*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
            conn = JdbcConnectionUtil.getJdbcConnection();
            /*Infosys - Weblogic to Jboss migration changes end here*/
			statement = conn.prepareStatement(insertSql);
           
       /*     System.out.println("gender"+track.getGender());
            System.out.println("getHireStartDate"+track.getHireStartDate());
            System.out.println("getHireEndDate"+track.getHireEndDate());
             System.out.println("getcompEndDate"+track.getTrainingCompletionEndDate());
            System.out.println("getCourseCode"+track.getCourseCode());
            System.out.println("getGroupBy1"+track.getGroupBy1());
            System.out.println("getGroupBy2"+track.getGroupBy2());
            System.out.println("getGroupBy3"+track.getGroupBy3());
            System.out.println("getGroupBy4"+track.getGroupBy4());
            System.out.println("getTrackId"+track.getTrackId()); */
            
  //         statement.setString(1,track.getTrackLabel());
          statement.setString(1,track.getTrackLabel());
            
            statement.setString(2,track.getBusinessUnit() );
            statement.setString(3,track.getRoleCode() );
            statement.setString(4,track.getSalesOrg() );
            //statement.setString( 2,track.getTrackId() );
            statement.setString( 5,track.getGender() );
            
            statement.setString( 6,track.getCourseCode() );
            statement.setString( 7,track.getHireStartDate() );
            statement.setString( 8,track.getHireEndDate() );
            statement.setString( 9,track.getTrainingCompletionStartdate() );
            statement.setString( 10,track.getTrainingCompletionEndDate() );
            statement.setString( 11,track.getTrainingRegistrationStartDate() );
            statement.setString( 12,track.getTrainingRegistrationEndDate() );
            statement.setString( 13,track.getGroupBy1() );
            statement.setString( 14,track.getGroupBy2() );
            statement.setString( 15,track.getGroupBy3() );
            statement.setString( 16,track.getGroupBy4() );
            statement.setString( 17,track.getGroupBy5() );
            statement.setString( 18,track.getGroupBy6() );
            statement.setString( 19,track.getTrackId() );
            
            
            
            
            
            LoggerHelper.logSystemDebug("query"+insertSql );
			int num = statement.executeUpdate();
            
            
           if ( num > 0 ) {
                System.out.println("updation query"+insertSql);
            }
           } 
        catch(Exception e){
            log.error(e,e);
            e.printStackTrace();
            System.out.println("In exception");
        }
        finally{
                if ( statement != null){
                 try {
                     statement.close();
                     } catch ( Exception e2){
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
    
    //added method to retrieve columns in a order
    public List executeSql( String sql ) {
        
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE,"ordered");
        
        return result;
    }
  
   public String insertTrack(String name){
       
        List result = DBUtil.executeSql("Select MANAGEMENT_TRACK_ID_SEQ.nextval as nextmanagementid from dual",AppConst.APP_DATASOURCE);
        Map map = (Map)result.get(0);

        String retString = null;

        String insertSql = " insert into MANAGEMENT_TRACK " + 
                           " (TRACK_ID,TRACK_LABEL,TRACK_TYPE) "+
                           " values(?,?,'Management') ";
        ResultSet rs=null;
        PreparedStatement statement = null;
		Connection conn = null;
        String nextValue=null;
        
        try{                    
	        /*Infosys - Weblogic to Jboss migration changes start here*/
	        /*Context ctx = new InitialContext();
	        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
	        conn = ds.getConnection();*/
	        conn = JdbcConnectionUtil.getJdbcConnection();
	        /*Infosys - Weblogic to Jboss migration changes end here*/
	        statement  = conn.prepareStatement(insertSql);
	        
	        nextValue = "MANAGEMENT_" + (BigDecimal)map.get("NEXTMANAGEMENTID");
	        System.out.println(nextValue);
	           
	        statement.setString(1, nextValue);				
	        statement.setString(2, name);
	    
	        System.out.println(statement);
	        LoggerHelper.logSystemDebug("forecast Final SQL---\n"+insertSql);
	        
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

  public boolean insertTrainingReports(ManagementSummaryReport track, String menuId) {
        
        System.out.println("Inside insertTraininngreports of mgt summary"+menuId);
        
        List result = DBUtil.executeSql("Select TRAINING_REPORT_ID_SEQ.nextval as nextid from dual",AppConst.APP_DATASOURCE);
        Map map = (Map)result.get(0);

              
        String insertSql = "insert into  training_report (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) " +
                        "values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
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
        
            statement.setString(1,track.getTrackLabel());
            System.out.println(track.getTrackLabel()+"Label" );
            statement.setString(2,"/TrainingReports/ManagementSummaryReport/begin?track="+track.getTrackId());
            statement.setBigDecimal( 3, new BigDecimal(menuId) );
		    statement.setString( 4, track.getTrackId() );
     
            LoggerHelper.logSystemDebug("forecast Final SQL---\n"+insertSql);
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
    // End
      public void insertManagementCodeAndDescrpn(String trackId, String filter_type, String filter_code, String code_colName){
        String deleteSql = "delete from management_code_desc where filter_id ='"+trackId+ "' and filter_type ='"+filter_type+"'"; 
            // execute deleteSql
        ResultSet drs = null;
		Statement st = null;
		Connection dconn = null;
		ArrayList ret = new ArrayList();
		try {
			/*Infosys - Weblogic to Jboss migration changes start here*/
			/*Context ctx = new InitialContext();


			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			dconn =   ds.getConnection();*/
			dconn = JdbcConnectionUtil.getJdbcConnection();
			st = dconn.createStatement();
			st.setFetchSize(2000);
			
			
			int num = st.executeUpdate(deleteSql);
            if(num >0)
            {
                System.out.println("deleted");
            }
			
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			if ( drs != null) {
				try {
					drs.close();
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
			if ( dconn != null) {
				try {
					dconn.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
		}
                
                
             
             String insertSql, innerSql;
             String[] selectedValues= filter_code.split(",");
             for(int i=0; i< selectedValues.length; i++) {
               // insertSql = "insert into management_code_desc values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                //+"',(select "+filter_type+" from mv_field_employee_rbu where "+group_cd = 'RBUWE')+")";
              //  insertSql = "insert into management_code_desc values (?,?,?,(select distinct ? from mv_field_employee_rbu where ?='?'))";
              
              if(!filter_type.equals("activityfk")){
                insertSql = "insert into management_code_desc values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                "',(select distinct "+filter_type+" from mv_field_employee_rbu where "+code_colName+" = '"+selectedValues[i]+"'))";                 
              } else {
                insertSql = "insert into management_code_desc values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                "',(select distinct activityname from mv_usp_activity_hierarchy where "+code_colName+" = '"+selectedValues[i]+"'))";                 
              }
               
                 
                 
             //   System.out.println("insertSql=="+insertSql);
                // execute insert stmt.
                
                ResultSet rs = null;
		        PreparedStatement statement = null;
		        Connection conn = null;
                
             try{
            	 /*Infosys - Weblogic to Jboss migration changes start here*/
		            /*Context ctx = new InitialContext();
		        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
					conn =   ds.getConnection();*/
            	 	conn = JdbcConnectionUtil.getJdbcConnection();
            	 	/*Infosys - Weblogic to Jboss migration changes end here*/
					statement = conn.prepareStatement(insertSql);
		           
		           // System.out.println(track.getHireStartDate()+"Hire date");
		           
		            //statement.setString(1,track.getTrackLabel());
		      /*           statement.setString(1,trackId);
		                 statement.setString(2, filter_type);
		                 statement.setString(3, selectedValues[i]);
		                 statement.setString(4, filter_type);
		                 statement.setString(5,code_colName);
		                 statement.setString(6,selectedValues[i]);*/
		            
		            log.info(insertSql);
					int num = statement.executeUpdate();
		           /* if ( num > 0 ) {
		                return true;
		            }*/
           } 
        catch(Exception e){
            log.error(e,e);
        }
        finally{
                if ( statement != null){
                 try {
                     statement.close();
                     } catch ( Exception e2){
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
                       // String selOrg = selectedValues[i]; 
            
       }
    
 } 
