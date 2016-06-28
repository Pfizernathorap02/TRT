package com.pfizer.hander;

import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.PDFHS.PDFHSChartBean;
import com.pfizer.webapp.wc.RBU.RBUChartBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RBUPieChartHandler
{
    	protected static final Log log = LogFactory.getLog( RBUPieChartHandler.class );


        public RBUPieChartHandler(){
        }

        public RBUChartBean[] getFilteredRBUChart(String productCd,String selectedBU,String selectedRBU, String emplid){
            
            //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND future.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.RBU_DESC='"+selectedRBU+"' ";
            }
            
           String sqlQuery1=" SELECT 'Complete' STATUS,"+
                             "        COUNT(DISTINCT ped.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS ped, V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.STATUS = 'C' "+
                             " AND ped.PRODUCT_CD = '"+productCd+"' " + 
                             " AND ped.emplid = future.emplid(+)";
                             
             if(emplid != null && !emplid.equals("")){
	               sqlQuery1 = sqlQuery1 + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
             }                 
                  String sqlQuery2=" SELECT 'InComplete' STATUS,"+
                             "        COUNT(DISTINCT ped.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS ped, V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.STATUS = 'NC' "+
                             " AND ped.PRODUCT_CD = '"+productCd+"' " +
                             " AND ped.emplid = future.emplid(+)";            
                if(emplid != null && !emplid.equals("")){
	               sqlQuery2 = sqlQuery2 + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
             }              
                                       
            String sqlQuery3 = "";                 
            String additionalCond = "";
            if(emplid != null && !emplid.equals("")){
	               additionalCond = additionalCond + " and ped.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
             } 
            if(condQuery != null && !condQuery.equals("")){
            sqlQuery3=      
            // " Select sub.STATUS, sum(sub.TOTAL) as TOTAL from "+
              //               " ( "+
            
                             " SELECT 'OnLeave' STATUS,"+
                             "        COUNT(DISTINCT ped.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS ped, V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.STATUS = 'L' AND ped.PRODUCT_CD = '"+productCd+"' " + 
                             " AND ped.emplid = future.emplid(+)" + 
                             " "+additionalCond+""+
                             " "+condQuery+"";
                /*
                             " union"+
                             " SELECT 'OnLeave' STATUS,COUNT(DISTINCT ped.EMPLID) TOTAL from V_rbu_training_required ped, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed mv where "+
                             " ped.emplid = future.emplid(+) and ped.emplid not in (select emplid from V_RBU_DASHBOARD_STATUS) and ped.required_product= '"+productCd+"'  and ped.emplid = mv.emplid(+) and mv.empl_status in ('L','P') "+
                             " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' " + 
                             " "+additionalCond+""+
                             " "+condQuery+" "+ 
                             " )"+
                             " Sub"+
                             " group by sub.STATUS";*/
                
        }
        else if(condQuery != null && condQuery.equals("")){
            sqlQuery3= 
            //      " Select sub.STATUS, sum(sub.TOTAL) as TOTAL from "+
              //               " ( "+
            
                             " SELECT 'OnLeave' STATUS,"+
                             "        COUNT(DISTINCT ped.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS ped, V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.OVERALL_STATUS = 'L'  AND ped.PRODUCT_CD = '"+productCd+"' " + 
                             " AND ped.emplid = future.emplid(+)" +
                             " "+additionalCond+"";
                
                /*             " union"+
                             " SELECT 'OnLeave' STATUS,COUNT(DISTINCT ped.EMPLID) TOTAL from V_rbu_training_required ped, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed mv where "+
                             " ped.emplid = future.emplid(+) and ped.emplid not in (select emplid from V_RBU_DASHBOARD_STATUS) and ped.required_product= '"+productCd+"' and ped.emplid = mv.emplid(+) and mv.empl_status in ('L','P')"+
                             " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' " + 
                             " "+additionalCond+""+
                             " )"+
                             " Sub"+
                             " group by sub.STATUS";             */
        }                    
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for PDFHS Handler with filter:"+finalQuery.toString());
            //System.out.println("Query to get trainee list ##### " + finalQuery.toString());
            return executeStatement(finalQuery);
        }
        
        public RBUChartBean[] getFilteredRBUOverallChart(String productCd,String selectedProduct,String selectedBU,String selectedRBU, String emplid){
            
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND future.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND future.RBU_DESC='"+selectedRBU+"' ";
            }
            
             String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS data,V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE data.emplid = future.emplid(+) and  data.OVERALL_STATUS = 'C' " ;
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }
            String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS data,V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE data.emplid = future.emplid(+) and  data.OVERALL_STATUS = 'NC' " ;
              if(emplid != null && !emplid.equals("")){
                    sqlQuery2 = sqlQuery2 + " and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }   

            String sqlQuery3="";
            if(condQuery != null && !condQuery.equals("")){
           String addionalCon = "";
          if(emplid != null && !emplid.equals("")){
                addionalCon = addionalCon + " and rbu.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";
          }
                  sqlQuery3 =
                  //  " Select sub.STATUS, sum(sub.TOTAL) as TOTAL from"+
                    //         " ( "+
                             
                             " SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT rbu.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS rbu,V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE rbu.emplid = future.emplid(+) and  rbu.OVERALL_STATUS = 'L' " +
                             " "+addionalCon+""+
                             " "+condQuery+"";
                      
                      //       " union"+
                        //     " SELECT 'OnLeave' STATUS,COUNT(DISTINCT rbu.EMPLID) TOTAL from V_rbu_training_required rbu, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed mv  where "+
                          //   " rbu.emplid not in(select emplid from V_RBU_DASHBOARD_STATUS) and "+
                            // " rbu.emplid = future.emplid(+) and rbu.emplid = mv.emplid(+) and mv.empl_status in ('L','P') " + 
                         //    " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' " + 
                         //    " "+addionalCon+""+
                         //   " "+condQuery+" "+ 
                         //    " )"+
                         //    " Sub"+
                         //    " group by sub.STATUS";
            }
            else if(condQuery != null && condQuery.equals("")){
                
                 String addionalCon = "";
          if(emplid != null && !emplid.equals("")){
                addionalCon = addionalCon + " and rbu.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";
          }
                  sqlQuery3 = 
                  // " Select sub.STATUS, sum(sub.TOTAL) as TOTAL from"+
                    //         " ( "+
                             " SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT rbu.EMPLID) TOTAL"+
                             " FROM   V_RBU_DASHBOARD_STATUS rbu,V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE rbu.emplid = future.emplid(+) and  rbu.OVERALL_STATUS = 'L' " +
                             " "+addionalCon+"";
                      
                      /*       " union"+
                             " SELECT 'OnLeave' STATUS,COUNT(DISTINCT rbu.EMPLID) TOTAL from V_rbu_training_required rbu, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed mv where "+
                             " rbu.emplid not in(select emplid from MV_RBU_PED_SCE_DATA) and " +
                             " rbu.emplid = future.emplid(+) and rbu.emplid = mv.emplid(+) and mv.empl_status in ('L','P') " + 
                             " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' " + 
                             " "+addionalCon+""+
                             " )"+
                             " Sub"+
                             " group by sub.STATUS";
                          */   
            }                               
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Overall pie chart:"+finalQuery.toString());
            //System.out.println("Query for Overall pie chart:"+finalQuery.toString());
            return executeStatement(finalQuery);
        } 

    public RBUChartBean[] getFilteredToviazLaunchAttendanceChart(String productCd,String selectedProduct,String selectedBU,String selectedRBU, String emplid){
            
            
            //System.out.println("In getFilteredToviazLaunchOverallChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Complete' ";
            if(emplid != null && !emplid.equals("")){
	               sqlQuery1 = sqlQuery1 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                 
            String sqlQuery2 = "";
              sqlQuery2 =    "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Not Complete' ";
             if(emplid != null && !emplid.equals("")){
	               sqlQuery2 = sqlQuery2 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            } 
            String sqlQuery3="";
           sqlQuery3 =  "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'On Leave' ";
            if(emplid != null && !emplid.equals("")){
	               sqlQuery3 = sqlQuery3 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            //System.out.println("Query for Overall pie chart:"+finalQuery.toString());
            return executeStatement(finalQuery);
        } 


   public RBUChartBean[] getFilteredToviazLaunchAttendanceChartExecs(String productCd,String selectedProduct,String selectedBU,String selectedRBU, String emplid){
            
            
            //System.out.println("In getFilteredToviazLaunchAttendanceChartExecs #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_TOVIAZ_LAUNCH_EXEC_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Complete' ";
            if(emplid != null && !emplid.equals("")){
	               sqlQuery1 = sqlQuery1 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                 
            String sqlQuery2 = "";
              sqlQuery2 =    "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_TOVIAZ_LAUNCH_EXEC_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'Not Complete' ";
             if(emplid != null && !emplid.equals("")){
	               sqlQuery2 = sqlQuery2 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            } 
            String sqlQuery3="";
           sqlQuery3 =  "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   V_TOVIAZ_LAUNCH_EXEC_STATUS data"+
                             " WHERE  data.ATTENDANCE = 'On Leave' ";
            if(emplid != null && !emplid.equals("")){
	               sqlQuery3 = sqlQuery3 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            //System.out.println("Query for Overall pie chart:"+finalQuery.toString());
            return executeStatement(finalQuery);
        }      


    public RBUChartBean[] getFilteredToviazLaunchSpecificChart(String examType,String selectedProduct,String selectedBU,String selectedRBU, String emplid){
            
            
            //System.out.println("In getFilteredToviazLaunchSpecificChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data."+examType+" not in ('Not Complete', 'On Leave', 'N/A') ";
               if(emplid != null && !emplid.equals("")){
	               sqlQuery1 = sqlQuery1 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                
                             
             String sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data."+examType+" = 'Not Complete' ";
             if(emplid != null && !emplid.equals("")){
	               sqlQuery2 = sqlQuery2 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                              
            String sqlQuery3="";
            
             sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data."+examType+" ='On Leave' ";
            if(emplid != null && !emplid.equals("")){
	               sqlQuery3 = sqlQuery3 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                              
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            //System.out.println("Query for Overall pie chart:"+finalQuery.toString());
            return executeStatement(finalQuery);
        } 
        
        
        public RBUChartBean[] getFilteredToviazLaunchOverAllChart(String selectedBU,String selectedRBU, String emplid){
            
            
            //System.out.println("In getFilteredToviazLaunchOverAllChart #####################");
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU) && !selectedBU.equals("")){
             condQuery=condQuery+ " AND data.BU='"+selectedBU+"'";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU) && !selectedRBU.equals("")){
             condQuery=condQuery+ " AND data.RBU_DESC='"+selectedRBU+"' ";
            }
            
             String sqlQuery1 = "SELECT 'Complete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.OVERALL = 'Complete' ";                
             if(emplid != null && !emplid.equals("")){
	               sqlQuery1 = sqlQuery1 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }               
                             
            String sqlQuery2 = "";
            sqlQuery2 = "SELECT 'InComplete' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.OVERALL = 'Not Complete' "; 
              if(emplid != null && !emplid.equals("")){
	               sqlQuery2 = sqlQuery2 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                 
            
            String sqlQuery3="";
          sqlQuery3 = "SELECT 'OnLeave' STATUS ,"+
                             "        COUNT(DISTINCT data.EMPLID) TOTAL"+
                             " FROM   MV_TOVIAZ_LAUNCH_STATUS data"+
                             " WHERE  data.OVERALL = 'On Leave' ";                                
             if(emplid != null && !emplid.equals("")){
	               sqlQuery3 = sqlQuery3 + "and data.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                                              
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for Attendance pie chart:"+finalQuery.toString());
            System.out.println("Query for Overall pie chart:"+finalQuery.toString());
            return executeStatement(finalQuery);
        } 

      /* public int  getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd) {
        String sqlQuery1="SELECT COUNT(DISTINCT EMPLID) COUNT"+
                             " FROM   V_PWRA_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS IN ('L',"+
                             "                           'I',"+
                             "                           'P')" ;



        //ConditionQuery
        String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(teamCd)  || "".equalsIgnoreCase(teamCd.trim()) ){
             condQuery=condQuery+ "AND TEAM_CD='"+teamCd+"' ";
            }

            if(!"ALL".equalsIgnoreCase(regionCd)){
             condQuery=condQuery+ "AND REGION_CD='"+regionCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(districtId)){
             condQuery=condQuery+ "AND DISTRICT_ID='"+districtId+"' ";
            }

            sqlQuery1=sqlQuery1+condQuery;
            return getCount(sqlQuery1);

        }*/

         public int  getOverAllTotalCount(String productCd,String selectedBU,String selectedRBU) {
            
         String sqlQuery1 = "";   
        if(productCd.equalsIgnoreCase("OVERALL")){    
         sqlQuery1="SELECT COUNT(DISTINCT ped.EMPLID) COUNT"+
                             " FROM   V_RBU_PED_SCE_DATA ped, V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.STATUS IN ('NC',"+
                             "                           'C',"+
                             "                           'L')" ;

        
        }
        else{
            sqlQuery1="SELECT COUNT(DISTINCT ped.EMPLID) COUNT"+
                             " FROM   V_RBU_PED_SCE_DATA ped , V_RBU_FUTURE_ALIGNMENT future"+
                             " WHERE  ped.STATUS IN ('NC',"+
                             "                           'C',"+
                             "                           'L')"+
                             " AND ped.PRODUCT_CD = '"+productCd+"' ";
        }
        //ConditionQuery
        String condQuery="";
            if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND future.BU='"+selectedBU+"' and future.emplid = ped.emplid";
            }
            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.RBU_DESC='"+selectedRBU+"' and future.emplid = ped.emplid ";
            }
            sqlQuery1=sqlQuery1+condQuery;
            return getCount(sqlQuery1);

        }

        private int getCount(String query){
         int total=0;
         ResultSet rs = null;
         PreparedStatement st = null;
		/* Connection conn = null;*/
         /* Infosys - Weblogic to Jboss migration changes start here */
   		Connection conn = JdbcConnectionUtil.getJdbcConnection();
   		/* Infosys - Weblogic to Jboss migration changes end here */

         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
            st = conn.prepareCall(query);
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                total=rs.getInt("COUNT");

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
           return total;
           }

        private RBUChartBean[] executeStatement(String query){

         RBUChartBean[] rbuChartBean=null;
         RBUChartBean thisPOAChartBean;
         List tempList = new ArrayList();
         ResultSet rs = null;
         PreparedStatement st = null;
		/* Connection conn = null;*/
         /* Infosys - Weblogic to Jboss migration changes start here */
   		Connection conn = JdbcConnectionUtil.getJdbcConnection();
   		/* Infosys - Weblogic to Jboss migration changes end here */
         try{
           /* Context ctx = new InitialContext();
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

}
