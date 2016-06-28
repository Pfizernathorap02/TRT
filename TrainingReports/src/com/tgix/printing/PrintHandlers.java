package com.tgix.printing; 

import com.pfizer.db.P4TrainingWave;
import com.pfizer.db.RBUTrainingWeek;
import com.pfizer.webapp.AppConst;
import java.sql.CallableStatement;
import javax.naming.Context;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class PrintHandlers 
{ 
    protected static final Log log = LogFactory.getLog( PrintHandlers.class );
    private static final DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
    private static final DateFormat dateFormatEmail = new SimpleDateFormat("MM/dd/yyyy");
    
    public static void insertEmailDispatch(EmailInfoBean emailInfo, String sLogAction) {
		String retString = null;
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
		
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
		/*	String updateUserSql = "insert into tr_website_audit " +
						" (audit_id, action, user_id, action_date) " +
						" values (tr_website_audit_seq.nextval, '" + sLogAction + "', ?, sysdate) ";*/
		String updateUserSql = "insert into RBU_EMAIL_AUDIT " +
						" (EMPLID, CLASS_ID,  ACTION, ACTION_DATE) " +
						" values ('" + emailInfo.getEmplID()  + "','" + emailInfo.getClassId()+ "', '" + sLogAction + "', sysdate) ";
			statement = conn.prepareStatement(updateUserSql);
			log.info(updateUserSql);			
           // statement.setString(1,emailInfo.getEmplID());
            //statement.setString(2,emailInfo.getProducts());
           // statement.setString(3,emailInfo.getClassDates());
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
    
    /**
     * This method processes the shipment  order on click of the Place Shipment order link.
     */
    public static void callTRMOrderProcess()
    {
      Connection conn = null;
      try
      {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  
        // Changes made for RBU Shipment
        CallableStatement proc = conn.prepareCall("{ call rbu_trm_shipment_process()}");     
        proc.executeUpdate();        
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
      
      }
      if ( conn != null) {
      try 
      {
		conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
	  }
    }//end of the Method 
    /**
     * Method added for RBU Shipment. This method gets the class start date for a particular employee and 
     * product. 
     */
    public static String getClassStartDate(String emplId, String productDesc){
        Date classDate = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        String status = "";
        String result = "";
        String productDescription = "";
		
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			StringBuffer selectStmt = new StringBuffer();
            // This is added because these products are actually having 2 sub products each in
            // CLASS_ASSIGNMENT so to get  the actual class date for these sun products.
            if(productDesc.equals("Toviaz") || productDesc.equals("Revatio") || productDesc.equals("Aricept") ||
            productDesc.equals("Geodon") || productDesc.equals("Lyrica") ){
                selectStmt.append("SELECT START_DATE as START_DATE, PLC_TRAINING_REQUIRED as STATUS, PRODUCT_DESC as PRODUCT_DESC from V_RBU_CLASS_ASSIGNMENT rbu");
                selectStmt.append(" where  rbu.emplid='"+emplId+"' ");
                selectStmt.append(" and  rbu.PRODUCT_DESC like'%"+productDesc+"%' ");
            }
            else{
                selectStmt.append("SELECT START_DATE as START_DATE, PLC_TRAINING_REQUIRED as STATUS , PRODUCT_DESC as PRODUCT_DESC from V_RBU_CLASS_ASSIGNMENT rbu");
                selectStmt.append(" where  rbu.emplid='"+emplId+"' ");
                selectStmt.append(" and  rbu.PRODUCT_DESC ='"+productDesc+"' ");
            }
            statement = conn.prepareStatement(selectStmt.toString());
			log.info(selectStmt.toString());			
						
			rs = statement.executeQuery();
            if(rs != null && rs.next()){
                classDate = rs.getDate(1);
                status = rs.getString(2);
                productDescription = rs.getString(3);
            } 
            if(status != null && status.equals("Y")){
                if(classDate != null){
                    result = dateFormat.format(classDate);
                }
            }
            else{
                result = status;
            }
            System.out.println("Product description from input  >>" + productDesc + "From DB >> " + productDescription) ;
            if(productDescription.equalsIgnoreCase("OAB Toviaz")){
                productDesc = "OAB Toviaz";
            }
            if(productDescription.equalsIgnoreCase("HS/L Toviaz")){
                productDesc = "Toviaz";
            }  
            return productDesc + " - " + result;
            
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
        
        return result;
    } 
    
     /**
     * Method added for RBU Shipment. This method gets the BU for a employee from the future allignment. 
     */
    public static String getBUForEmployee(String emplId){
        Date classDate = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        String result = "";
		
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			StringBuffer selectStmt = new StringBuffer();
            
                selectStmt.append("SELECT BU as BU  from V_RBU_FUTURE_ALIGNMENT rbu");
                selectStmt.append(" where  rbu.emplid='"+emplId+"' ");
            statement = conn.prepareStatement(selectStmt.toString());
			log.info(selectStmt.toString());			
						
			rs = statement.executeQuery();
            if(rs != null && rs.next()){
                result = rs.getString(1);
            } 
            return result;
            
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
        
        return result;
    } 
    
    
     public static P4TrainingWave getClassDatesForWeekP4(String weekId){
        Date classDate = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        P4TrainingWave week = new P4TrainingWave();
		
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			StringBuffer selectStmt = new StringBuffer();
            
                selectStmt.append("SELECT START_DATE,END_DATE  from RBU_TRAINING_WEEKS");
                selectStmt.append(" where  week_id='"+weekId+"' ");
            statement = conn.prepareStatement(selectStmt.toString());
			log.info(selectStmt.toString());			
						
			rs = statement.executeQuery();
            if(rs != null && rs.next()){
                week.setStart_date(rs.getDate(1));
                week.setEnd_date(rs.getDate(2));
            } 
            week.setWeek_id(weekId);
            return week;
            
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
        
        return week;
    } 
    
    public static RBUTrainingWeek getClassDatesForWeek(String weekId){
        Date classDate = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        RBUTrainingWeek week = new RBUTrainingWeek();
		
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			StringBuffer selectStmt = new StringBuffer();
            
                selectStmt.append("SELECT START_DATE,END_DATE  from RBU_TRAINING_WEEKS");
                selectStmt.append(" where  week_id='"+weekId+"' ");
            statement = conn.prepareStatement(selectStmt.toString());
			log.info(selectStmt.toString());			
						
			rs = statement.executeQuery();
            if(rs != null && rs.next()){
                week.setStart_date(rs.getDate(1));
                week.setEnd_date(rs.getDate(2));
            } 
            week.setWeek_id(weekId);
            return week;
            
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
        
        return week;
    } 

    public static List getClassDates(){
        Date classDate = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        List result = new ArrayList();
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			StringBuffer selectStmt = new StringBuffer();
            
                selectStmt.append("SELECT START_DATE ,END_DATE, WEEK_ID  from RBU_TRAINING_WEEKS order by week_id asc");
            statement = conn.prepareStatement(selectStmt.toString());
			log.info(selectStmt.toString());			
						
			rs = statement.executeQuery();
            if(rs != null && rs.next()){
                RBUTrainingWeek week = new RBUTrainingWeek();
                week.setStart_date(rs.getDate(1));
                week.setEnd_date(rs.getDate(2));
                 week.setWeek_id(rs.getString(3));
                 result.add(week);
            } 
           
            return result;
            
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
        
        return result;
    } 
    
    
    public static String getEmailSent(EmailInfoBean infoBean){
        Date result = null;
        ResultSet rs1 = null;
		PreparedStatement statement1 = null;
        Connection conn = null;
        String sendFlag = "Y";
        try{
          // Check whether the record exists in the table for emplid, product and class date combination
          Context ctx = new InitialContext();
          DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
	      conn =   ds.getConnection();  
          StringBuffer str =  new StringBuffer();
          str.append("SELECT emplid from RBU_EMAIL_AUDIT where emplid = '"+infoBean.getEmplID()+"' ");
          str.append(" and CLASS_ID ='"+infoBean.getClassId()+"'");
          //System.out.println("Query for getting the status " + str.toString());
          statement1 = conn.prepareStatement(str.toString());
          rs1 = statement1.executeQuery();
          sendFlag = "Y";
          if(rs1 != null && rs1.next()){
                sendFlag = "N";
          }
          
        } catch (Exception e) {
            e.printStackTrace();
			log.error(e,e);
		} finally {
			
            if ( rs1 != null) {
				try {
					rs1.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
            if ( statement1 != null) {
				try {
					statement1.close();
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
        return sendFlag;
        
    }
    
      

} 
