package com.pfizer.hander; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import sun.security.krb5.internal.bd;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.DelegatedEmp;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.tgix.Utils.LoggerHelper;


public class DelegateHandler

{  protected static final Log log = LogFactory.getLog( DelegateHandler.class );
    public DelegateHandler() {

    }


    public DelegatedEmp[] DelegatedFrList() {
        LoggerHelper.logSystemDebug("TRTtest1");
		String retString = null;
		
        //String sql = "select * from mv_field_employee_rbu";
       
       
        //LoggerHelper.logSystemDebug("TRTtest16 "+ result.get(0) );
        
        
 
        DelegatedEmp[]  ret = null;
        List tempList = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		//Connection conn = null;
        
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        
        try{
           /* Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();   */           
			st = conn.createStatement();            
			st.setFetchSize(5000);
            
            String sql = "select del.delfr as delfr, fromDel.first_name as from_FN, fromDel.last_name as from_LN," + 
                         "del.delto as delto,toDel.first_name as to_FN, toDel.last_name as to_LN from delegate del," +
                         "(select first_name,last_name, emplid from mv_field_employee_rbu rbu where emplid in (select delfr from delegate)) fromDel,"+
                         "(select first_name,last_name, emplid from mv_field_employee_rbu rbu where emplid in (select delto from delegate)) ToDel"+  
                         " where toDel.emplid = del.delto and fromDel.emplid = del.delfr";
                          
                            
            LoggerHelper.logSystemDebug("TRTtest10 ");   
           System.out.println("query"+sql);
            rs = st.executeQuery(sql);
             
             
             while (rs.next()) {
                
                DelegatedEmp curr = new DelegatedEmp();
                LoggerHelper.logSystemDebug("TRTtest29 " );
                curr.setdelfrId(rs.getString("delfr"));
                curr.setdelfr( rs.getString("from_FN") );
                              
                curr.setdelfr( rs.getString("from_LN") );
                
                curr.setdeltoId(rs.getString("delto"));
                curr.setdelto( rs.getString("to_FN") );
                curr.setdelto( rs.getString("to_LN") );  
                  
                 System.out.println("first name" + rs.getString("from_FN") );              
				 tempList.add(curr);	
               		
			}
             
            ret = (DelegatedEmp[])tempList.toArray(new DelegatedEmp[1]);
            LoggerHelper.logSystemDebug("TRTtest34 " );
                           
        }
        catch (Exception e) {
			log.error(e,e);
		}
        finally {
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
         //LoggerHelper.logSystemDebug("TRTtest27 "+ ret.length );
        return ret;	
	}
    


   
    
    public DelegatedEmp[] DelegatedToList() {
        LoggerHelper.logSystemDebug("TRTtest1");
		String retString = null;
		
        //String sql = "select * from mv_field_employee_rbu";
       
       
        //LoggerHelper.logSystemDebug("TRTtest16 "+ result.get(0) );
        
        
 
        DelegatedEmp[]  ret = null;
        
        ResultSet rs = null;
		Statement st = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
        List tempList = new ArrayList();
        
        try{
            /*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();  */            
			st = conn.createStatement();            
			st.setFetchSize(5000);
            
            String sql = "select first_name,last_name " + 
                           "from mv_field_employee_rbu where emplid in (select delto from delegate)"; 
            LoggerHelper.logSystemDebug("TRTtest10 ");   
            LoggerHelper.logSystemDebug(sql);
             rs = st.executeQuery(sql);
             
             
             while (rs.next()) {
                
               DelegatedEmp curr = new DelegatedEmp();
                LoggerHelper.logSystemDebug("TRTtest29 " );
                curr.setdelto( rs.getString("first_name") );
                LoggerHelper.logSystemDebug("TRTtest31 " );
                
                curr.setdelto( rs.getString("last_name") );
                  
                 LoggerHelper.logSystemDebug("TRTtest35 " );              
				 tempList.add(curr);	
               		
			}
             
            ret = (DelegatedEmp[])tempList.toArray(new DelegatedEmp[1]);
            LoggerHelper.logSystemDebug("TRTtest34 " );
                           
        }
        catch (Exception e) {
			log.error(e,e);
		}
        finally {
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
         //LoggerHelper.logSystemDebug("TRTtest27 "+ ret.length );
        return ret;	
	}
    
    public void Insert(String fempid, String toempid ) {
       String retString = null;
		String insertSql = "insert into delegate " + 
                           " values (?,?) ";    
		ResultSet rs = null;
		PreparedStatement statement = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			statement = conn.prepareStatement(insertSql);
			
			statement.setString( 1, fempid );				
			statement.setString( 2, toempid );				
			
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
    
    
    


    
    
    public void Delete(String delfr, String delto ) {
       String retString = null;
		String insertSql = "delete from delegate " + 
                           " where delfr = "+ "'"+ delfr + "'" + "and delto = "+ "'"+ delto + "'" ;
		ResultSet rs = null;
		PreparedStatement statement = null;
		//Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			statement = conn.prepareStatement(insertSql);
			
			//statement.setString( 1, fempid );				
			//statement.setString( 2, toempid );				
			
			statement.executeUpdate();		
            LoggerHelper.logSystemDebug("delete" + insertSql);
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
  


  
   
    public List getDelegatedFromUserList(String empid) {
        LoggerHelper.logSystemDebug("TRTtest1");
		String retString = null;
		String sql = "select delfr " + 
                           "from delegate where delto = "+ "'"+ empid + "'" ; 
        //String sql = "select * from mv_field_employee_rbu";
       
        List result = executeSql2(sql);
	 
        LoggerHelper.logSystemDebug("DelToday" + sql);
        LoggerHelper.logSystemDebug("DelToday2" + result);
        
        //LoggerHelper.logSystemDebug("TRTtest16 "+ result.get(0) );
        return result;  
        
}

  public List executeSql2( String sql ) {
        
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
	List ret = new ArrayList();
	Iterator itr = result.iterator();
	Map m = null;
	while (itr.hasNext()) {
		m = (HashMap)itr.next();		
		ret.addAll(m.values());
	}
        
        return ret;
    }

   public List getEmployeeDetails (String empid) {
      List result = new ArrayList();
      
      
      return result;  
   } 

  
}

