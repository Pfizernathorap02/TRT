package com.pfizer.hander; 

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.utils.JdbcConnectionUtil;

public class CourseHandler { 
    
    protected static final Log log = LogFactory.getLog( CourseHandler.class );
    private String dca_status="Approved";
    private String dca_operation="Delete";
    private String dca_TRAINING_REQ_FUTURE="Not Required";
    
    public CourseHandler(){
    }
    
    /**
     * This method deletes all the enteries from Deleted_Course_Assignment for the Given Emplid and Product CD
     */
    public void delFromDeletedCourseAssig(String emplid,String productCd){
        StringBuffer  sqlBuffer = new StringBuffer();
        sqlBuffer.append("DELETE FROM DELETED_COURSE_ASSIGNMENT DCA");
        sqlBuffer.append(" WHERE DCA.TRAINEE_SSN = (SELECT SSN");
        sqlBuffer.append(" FROM   V_NEW_FIELD_EMPLOYEE");
        sqlBuffer.append(" WHERE  V_NEW_FIELD_EMPLOYEE.EMPLID = '"+emplid+"')");
        sqlBuffer.append(" AND DCA.COURSE_ID IN (SELECT COURSE_ID");
        sqlBuffer.append(" FROM   COURSE");
        sqlBuffer.append(" WHERE  UPPER(COURSE.PRODUCT_CD) = UPPER('"+productCd+"'))");
        
         ResultSet rs = null;
		Statement st = null;
		//Connection conn = null;
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executin Statement To Delete From Deleted Course Assignment"+sqlBuffer.toString());
            st.executeUpdate(sqlBuffer.toString());
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
        
    }
    
    //Delete from COURSE_ASSIGNMENT
    public void delFromCourseAssign(String emplid,String productCd){
        
        String sql="DELETE FROM COURSE_ASSIGNMENT CA"+
                    " WHERE  CA.COURSE_ID IN (SELECT COURSE_ID"+
                    "        FROM   COURSE"+
                    "        WHERE  UPPER(COURSE.PRODUCT_CD) = UPPER('"+productCd+"'))"+
                    "        AND CA.TRAINEE_SSN = (SELECT SSN"+
                    "                         FROM   V_NEW_FIELD_EMPLOYEE"+
                    "                         WHERE  V_NEW_FIELD_EMPLOYEE.EMPLID = '"+emplid+"')";
                    
        ResultSet rs = null;
		Statement st = null;
	//	Connection conn = null;
				/* Infosys - Weblogic to Jboss migration changes start here */
				Connection conn = JdbcConnectionUtil.getJdbcConnection();
				/* Infosys - Weblogic to Jboss migration changes end here */
		try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To Delete From Course Assignment"+sql);
            st.executeUpdate(sql);
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
    }//end of Method
    
    
    
    
    //Insert into the Deleted Course Assignment 
    public boolean insertInDelCourseAssign(String emplid,String productCd,String userID,String reason){
        boolean success=false;
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO DELETED_COURSE_ASSIGNMENT");
        sqlBuffer.append("            (TRAINEE_SSN,");
        sqlBuffer.append("             COURSE_ID,");
        sqlBuffer.append("             GENDER,");
        sqlBuffer.append("             ROLE,");
        sqlBuffer.append("             USER_ID,");
        sqlBuffer.append("             STATUS,");
        sqlBuffer.append("             OPERATION,");
        sqlBuffer.append("             TRAINING_REQ_FUTURE,");
        sqlBuffer.append("             LAST_UPDATED_BY,");
        sqlBuffer.append("             TIME_STAMP,");
        sqlBuffer.append("             DELETE_REASON)");
        sqlBuffer.append(" SELECT MFE.NATIONAL_ID,");
        sqlBuffer.append("        C.COURSE_ID,");
        sqlBuffer.append("        MFE.SEX,");
        sqlBuffer.append("        MFE.TERRITORY_ROLE_CD,");
        sqlBuffer.append("        '-2' USER_ID,");
        sqlBuffer.append("        'Approved' STATUS,");
        sqlBuffer.append("        'Delete' OPERATION,");
        sqlBuffer.append("        'Not Required' TRAINING_REQ_FUTURE,");
        sqlBuffer.append("        '"+userID+"' LAST_UPDATED_BY,");
        sqlBuffer.append("        SYSDATE TIME_STAMP,");
        sqlBuffer.append("        '"+reason+"' DELETE_REASON");
        sqlBuffer.append(" FROM   MV_FIELD_EMPLOYEE MFE,");
        sqlBuffer.append("        COURSE C");
        sqlBuffer.append(" WHERE  MFE.EMPLID = '"+emplid+"'");
        sqlBuffer.append("        AND UPPER(C.PRODUCT_CD) = UPPER('"+productCd+"')");
        sqlBuffer.append("        AND C.COURSE_ID > 200");
        
        Statement st = null;
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To insertInDelCourseAssign"+sqlBuffer.toString());
            st.executeUpdate(sqlBuffer.toString());
            success=true;
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
        return success;
    }
   
    //Insert into the Deleted Course Assignment 
   /* public boolean insertCancelInDelCourseAssign(String emplid,String sCourseID,String sReason){
        boolean success=false;
        StringBuffer sqlBuffer = new StringBuffer();
        
        sqlBuffer.append(" INSERT INTO deleted_course_assignment(trainee_ssn, ");
        sqlBuffer.append(" course_id,gender,role,operation,time_stamp, delete_reason, user_id, status) ");
        sqlBuffer.append(" SELECT ");
        sqlBuffer.append("'" + emplid +  "', ");
        sqlBuffer.append(sCourseID + " , ");
        sqlBuffer.append("gender , role, 'Delete' , SYSDATE , ");
        sqlBuffer.append("'" + sReason + "'  ,");
        sqlBuffer.append("'-2'" + ",  ");
        sqlBuffer.append(" 'Approved' ");
        sqlBuffer.append(" FROM course_assignment WHERE trainee_ssn =  ");
        sqlBuffer.append("'" + emplid  + "' ");
        sqlBuffer.append(" AND course_id = ");
        sqlBuffer.append("'" + sCourseID + "'");
                
        Statement st = null;
		Connection conn = null;
        try {
			Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To insertCancelInDelCourseAssign"+sqlBuffer.toString());
            st.executeUpdate(sqlBuffer.toString());
            success=true;
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
        return success;
    }    

    */
    // Infosys Migrated code changes starts here
    public boolean insertCancelInDelCourseAssign(String emplid,String sCourseID,String sReason){
        boolean success=false;
        StringBuffer sqlBuffer = new StringBuffer();
       // Connection conn = JdbcConnectionUtil.getJdbcConnection();
        sqlBuffer.append(" INSERT INTO deleted_course_assignment(trainee_ssn, ");
        sqlBuffer.append(" course_id,gender,role,operation,time_stamp, delete_reason, user_id, status) ");
        sqlBuffer.append(" SELECT ");
        sqlBuffer.append("'" + emplid +  "', ");
        sqlBuffer.append(sCourseID + " , ");
        sqlBuffer.append("gender , role, 'Delete' , SYSDATE , ");
        sqlBuffer.append("'" + sReason + "'  ,");
        sqlBuffer.append("'-2'" + ",  ");
        sqlBuffer.append(" 'Approved' ");
        sqlBuffer.append(" FROM course_assignment WHERE trainee_ssn =  ");
        sqlBuffer.append("'" + emplid  + "' ");
        sqlBuffer.append(" AND course_id = ");
        sqlBuffer.append("'" + sCourseID + "'");
                
        Statement st = null;
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To insertCancelInDelCourseAssign"+sqlBuffer.toString());
            st.executeUpdate(sqlBuffer.toString());
            success=true;
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
        return success;
    }    

    
    
    
    
    
    
    
    
    
    
    
    //Insert Into Course_Assignment for recovered Training
    public boolean insertRecoverInCourseAssign(String emplid,String sCourseID,String sReason){
        boolean success=false;
        StringBuffer sqlBuffer = new StringBuffer();
        
        sqlBuffer.append(" INSERT INTO course_assignment(trainee_ssn, ");
        sqlBuffer.append(" course_id,gender,role, user_id, enroll_date) ");
        sqlBuffer.append(" SELECT ");
        sqlBuffer.append("'" + emplid +  "', ");
        sqlBuffer.append(sCourseID + " , ");
        sqlBuffer.append("gender , role,  ");        
        sqlBuffer.append(" '-2', ");
        sqlBuffer.append(" SYSDATE ");
        sqlBuffer.append(" FROM deleted_course_assignment WHERE trainee_ssn =  ");
        sqlBuffer.append("'" + emplid  + "' ");
        sqlBuffer.append(" AND course_id = ");
        sqlBuffer.append("'" + sCourseID + "'");
                
        Statement st = null;
		/*Connection conn = null;*/
        /* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection(); */ 
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To insertCancelInDelCourseAssign"+sqlBuffer.toString());
            st.executeUpdate(sqlBuffer.toString());
            success=true;
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
        return success;
    }    

    
    //Delete from COURSE_ASSIGNMENT
   /* public void delCancelFromCourseAssign(String emplid,String sCourseID){
        
        String sql= " DELETE FROM COURSE_ASSIGNMENT CA "+
                    " WHERE  CA.COURSE_ID = '" + sCourseID  + "' " + 
                    "and CA.trainee_ssn = '" + emplid + "' ";
                    
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        try {
			Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To Delete From Course Assignment"+sql);
            st.executeUpdate(sql);
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
    }//end of Method
   */ 
    //Infosys migrated code changes starts here
 public void delCancelFromCourseAssign(String emplid,String sCourseID){
        
        String sql= " DELETE FROM COURSE_ASSIGNMENT CA "+
                    " WHERE  CA.COURSE_ID = '" + sCourseID  + "' " + 
                    "and CA.trainee_ssn = '" + emplid + "' ";
                    
        ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To Delete From Course Assignment"+sql);
            st.executeUpdate(sql);
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
    }//end of Method
   
    
    //Delete from DELETED_COURSE_ASSIGNMENT
    public void delRecoverFromDeletedCourseAssign(String emplid,String sCourseID){
        
        String sql= " DELETE FROM DELETED_COURSE_ASSIGNMENT CA "+
                    " WHERE  CA.COURSE_ID = '" + sCourseID  + "' " + 
                    "and CA.trainee_ssn = '" + emplid + "' ";
                    
        ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/  
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To Delete From Course Assignment"+sql);
            st.executeUpdate(sql);
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
    }//end of Method
    
    
    
    public List getAllExemptionReasons(){
        List list=new ArrayList();
        String sql="select reason from exemption_reasons order by reason";
         ResultSet rs = null;
		Statement st = null;
	/*	Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
        try {
			/*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection(); */ 
			st = conn.createStatement();
			st.setFetchSize(5000);
            log.info("Executing Statement To getAllExemptionReasons"+sql);
            rs=st.executeQuery(sql);
            while(rs.next()){
                list.add(rs.getString("reason"));
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
        return list;
    }
    
    
    public void refreshDB(){
       /*Connection conn = null;*/
    	/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
     try{
        /*Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
         conn =   ds.getConnection();  */
        CallableStatement proc = conn.prepareCall("{ call Refresh_mv_training_required()}");     
        proc.executeUpdate();
        
     } catch (Exception e) {
			log.error(e,e);
		}
    finally {
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}//end of the Method 
        
    
    
    
    
} 
