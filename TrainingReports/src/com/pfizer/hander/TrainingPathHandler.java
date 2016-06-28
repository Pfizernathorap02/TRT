package com.pfizer.hander; 

import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TrainingPathHandler 
{ 
    protected static final Log log = LogFactory.getLog(TrainingPathHandler.class );
    
    public void insertTrainingPathConfig(int trackId, String filter_type, String filter_code, String code_colName){
     //   String deleteSql = " delete from training_path_config where config_id='"+trackId+"' "; 
            // execute deleteSql
        ResultSet drs = null;
		Statement st = null;
		/*Connection dconn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */	
   		Connection dconn = JdbcConnectionUtil.getJdbcConnection(); 		
   		/* Infosys - Weblogic to Jboss migration changes end here */ 
   		
		ArrayList ret = new ArrayList();
		try {
			/*Context ctx = new InitialContext();


			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			dconn =   ds.getConnection();  */
			st = dconn.createStatement();
			st.setFetchSize(2000);
			
			
		/*	int num = st.executeUpdate(deleteSql);
            if(num >0)
            {
                System.out.println("deleted");
            } */
			
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
                
                
             
             String insertSql="";
             String innerSql="";
             String[] selectedValues= filter_code.split(",");
             String[] selectedAlias = code_colName.split(",");
             for(int i=0; i< selectedValues.length; i++) {
               
              if(filter_type.equals("activityfk")){
                insertSql = "insert into training_path_config(config_id,filter_type,code,description,course_order) values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                "',(select distinct activityname from mv_usp_activity_hierarchy where "+code_colName+" = '"+selectedValues[i]+"'),'"+i+"')";                 
              }else if (filter_type.equals("alias")) 
              {
                insertSql = "insert into training_path_config(config_id,filter_type,code,description,course_order) values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                "','"+ selectedAlias[i] +"','"+i+"')";     
                
                System.out.println("insert query for alias" + insertSql);            
              } 
              else 
              {
                insertSql = "insert into training_path_config(config_id,filter_type,code,description) values ('"+trackId+"','"+filter_type+"', '"+selectedValues[i]+
                "',(select distinct "+filter_type+" from mv_field_employee_rbu where "+code_colName+" = '"+selectedValues[i]+"'))";                 
              } 
              
                             
             //    System.out.println("Insert SQL=="+insertSql);
                 
                  
                ResultSet rs = null;
		        PreparedStatement statement = null;
		      /*  Connection conn = null;*/
		        /* Infosys - Weblogic to Jboss migration changes start here */	
		   		Connection conn = JdbcConnectionUtil.getJdbcConnection(); 		
		   		/* Infosys - Weblogic to Jboss migration changes end here */   
                try{
            /*Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);
           
               log.info(insertSql);
			int num = statement.executeUpdate();
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
                    
            
       }
        
       public void updateTrainingPathConfig(int trackId){
        String deleteSql = " delete from training_path_config where config_id='"+trackId+"' "; 
            // execute deleteSql
        ResultSet drs = null;
		Statement st = null;
		/*Connection dconn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */	
   		Connection dconn = JdbcConnectionUtil.getJdbcConnection(); 		
   		/* Infosys - Weblogic to Jboss migration changes end here */ 
		ArrayList ret = new ArrayList();
		try {
			/*Context ctx = new InitialContext();


			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			dconn =   ds.getConnection();  */
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
           
       } 
       public List getSelectedTrainingConfiguraton(int configid){
            String sql = " select * from training_path_config where config_id='"+configid+"' "; 
            List result = executeSql2(sql);
            log.info(sql);
            return result;
       }
       
      public List getAllTrainingPaths(){
            String sql = " select distinct config_id,filter_type,code,description,course_order from training_path_config group by config_id,filter_type,code,description,course_order order by config_id,course_order asc ";
          //    String sql = " select * from training_path_config group by config_id,code order by course_order ";
            List result = executeSql2(sql);
            log.info(sql);
            return result;
      }
        public List getConfigList(){
            String sql = " select distinct(config_id) from training_path_config order by config_id asc ";
            List result = executeSql2(sql);
            log.info(sql);
            return result;
      }  
       public void  deleteConfiguration(String id){
            String sql = " delete from training_path_config where config_id='"+id+"' ";  
         //   String deleteSql = "delete from management_code_desc where filter_id ='"+trackId+ "' and filter_type ='"+filter_type+"'"; 
            // execute deleteSql
        ResultSet drs = null;
		Statement st = null;
		/*Connection dconn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */	
   		Connection dconn = JdbcConnectionUtil.getJdbcConnection(); 		
   		/* Infosys - Weblogic to Jboss migration changes end here */ 
   		
		ArrayList ret = new ArrayList();
		try {
			/*Context ctx = new InitialContext();


			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			dconn =   ds.getConnection();  */
			st = dconn.createStatement();
			st.setFetchSize(2000);
			
			
			int num = st.executeUpdate(sql);
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
            //return result;
       }
       public List getRoleCodes(){
        System.out.println("Inside rolecodes");
        List ret = new ArrayList();
        String sql = "  Select distinct role_cd, role_desc from mv_field_employee_rbu where role_cd is not null and is_hq_user=0 and role_desc is not null order by role_desc asc ";
        List result = executeSql2(sql);
        log.info(sql);
        return result;
      }
    
        public List getSalesOrg()
        {
            System.out.println("Inside sales org");
            List ret =new ArrayList();
            String sql = " Select distinct sales_group,group_cd from mv_field_employee_rbu where group_cd is not null and sales_group is not null and is_hq_user=0 order by sales_group asc ";
            List result = executeSql2(sql);
            log.info(sql);
            return result;
        }  
        
        public List getBusinessUnit()
        {
            System.out.println("Inside Bu");
            List ret =new ArrayList();
            String sql = " Select distinct bu from mv_field_employee_rbu where bu is not null and is_hq_user=0 order by bu asc ";
            List result = executeSql2(sql);
            log.info(sql);
            return result;
        }
        
        public List validateTrainingPaths(String org,String bu,String roles){
        //   String[] selectedValues = org.split(","); 
        //   String[] selectedBu = bu.split(",");
           String sql="";
       //    System.out.println("sales org=="+org); 
           String salesOrg = org.replaceAll(",","','");
           String businessUnit = bu.replaceAll(",","','");
           String roleCodes = roles.replaceAll(",","','");
           
           System.out.println("sales org=="+salesOrg); 
       
                sql = " select distinct bu_id.config_id from  "+
                            " (select distinct config_id from training_path_config where filter_type = 'bu' and code IN('"+businessUnit+"')) bu_id, " +
                            " (select distinct config_id from training_path_config where filter_type = 'sales_group' and code IN('"+salesOrg+"')) sg_id, "+
                            " (select distinct config_id from training_path_config where filter_type = 'role_desc' and code IN ('"+roleCodes+"')) role_id "+
                            " where bu_id.config_id = sg_id.config_id " +
                            " and bu_id.config_id = role_id.config_id " +
                            " and sg_id.config_id = role_id.config_id";
        //    }        
            System.out.println("SQL---"+sql);            
           List result = executeSql2(sql);
           log.info(sql);
          
           return result;
        }
        public List validateEditTrainingPaths(String org,String bu,String roles,int trackId){
        //   String[] selectedValues = org.split(","); 
        //   String[] selectedBu = bu.split(",");
           String sql="";
       //    System.out.println("sales org=="+org); 
           String salesOrg = org.replaceAll(",","','");
           String businessUnit = bu.replaceAll(",","','");
           String roleCodes = roles.replaceAll(",","','");
           
           System.out.println("sales org=="+salesOrg); 
       
                sql = " select distinct bu_id.config_id from  "+
                            " (select distinct config_id from training_path_config where filter_type = 'bu' and code IN('"+businessUnit+"')) bu_id, " +
                            " (select distinct config_id from training_path_config where filter_type = 'sales_group' and code IN('"+salesOrg+"')) sg_id, "+
                            " (select distinct config_id from training_path_config where filter_type = 'role_desc' and code IN ('"+roleCodes+"')) role_id "+
                            " where bu_id.config_id = sg_id.config_id " +
                            " and bu_id.config_id = role_id.config_id " +
                            " and sg_id.config_id = role_id.config_id and bu_id.config_id <> '"+trackId+"' ";
        //    }        
            System.out.println("SQL---"+sql);            
           List result = executeSql2(sql);
           log.info(sql);
          
           return result;
        }
     /*   public List validateTrainingPathsBackup(String org,String bu){
          // private StringBuffer selectClause1;
           String[] selectedValues = org.split(","); 
           String[] selectedBu = bu.split(",");
           String sql="";
           
            for(int i=0; i< selectedValues.length; i++) {
                sql = " select distinct bu_id.config_id from  "+
                            " (select distinct config_id from training_path_config where filter_type = 'bu' and code = 'Oncology') bu_id, " +
                            " (select distinct config_id from training_path_config where filter_type = 'sales_group' and code = '"+selectedValues[i]+"') sg_id, "+
                            " (select distinct config_id from training_path_config where filter_type = 'role_desc' and code = 'AM' OR code = 'ASD') role_id "+
                            " where bu_id.config_id = sg_id.config_id " +
                            " and bu_id.config_id = role_id.config_id " +
                            " and sg_id.config_id = role_id.config_id";
            }        
            System.out.println("SQL---"+sql);            
           List result = executeSql2(sql);
           log.info(sql);
          
           return result;
        } */
        public List executeSql( String sql ) {
           List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE,"ordered");
           return result;
        }
        public List executeSql2( String sql ) {
            List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
            return result;
        }
  
} 
