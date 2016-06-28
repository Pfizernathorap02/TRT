package com.pfizer.hander;

import com.pfizer.db.EmpSearch;
import com.pfizer.db.EmpSearchGNSM;
import com.pfizer.db.EmpSearchPDFHS;
import com.pfizer.db.EmpSearchPOA;
import com.pfizer.db.EmpSearchTSHT;
import com.pfizer.db.Employee;
import com.pfizer.db.GeneralSessionEmployee;
import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.db.TrainingOrder;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmplSearchTSHTForm;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.printing.EmployeeBean;
import com.tgix.printing.LoggerHelper;

//import db.TrDB;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RBUEmployeeHandler {
	protected static final Log log = LogFactory.getLog( RBUEmployeeHandler.class );

	private String productEmployeSql =
								" select 	 " +
									" e.emplid as emplId, " +
									" e.area_cd as areaCd, " +
									" e.promotion_date as promoDate, " +
									" e.effective_hire_date as hireDate, " +
									" e.sex as gender, " +
									" e.email_address as email, " +
									" e.reports_to_emplid as reportsToEmplid," +
                                    " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ," +
									" e.area_desc as areaDesc, " +
									" e.region_cd as regionCd, " +
									" e.region_desc as regionDesc, " +
									" e.district_id as districtId, " +
									" e.district_desc as districtDesc, " +
									" e.territory_id as territoryId, " +
									" e.territory_role_cd as role, " +
									" e.team_cd as teamCode, " +
									" e.cluster_cd as clusterCode, " +
									" e.last_name as lastName, " +
									" e.first_name as firstName, " +
									" e.middle_name as middleName, " +
									" e.preferred_name as preferredName " +
								" from " +
									" v_new_field_employee e, " +
									" v_rbu_training_required p " +
								" where  " +
									" p.emplid = e.emplid ";

    	private String searchEmployeSql =
								" select  " +
									" e.emplid as emplId, " +
									" p.product_cd as productCd, " +
									" e.last_name as lastName, " +
									" e.first_name as firstName, " +
									" e.middle_name as middleName, " +
									" e.preferred_name as preferredName , " +
                                    " TRAINING_REQUIRED as trainingNeed ,"  +
                                    " e.TERRITORY_ROLE_CD as roleCD, "+
                                    " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ," +
                                    " to_char(sub1.issued_on,'MM/DD/YYYY') exam_issued_on, "+
                                    " TO_CHAR(SUB2.MATERIALDATEORDERED,'MM/DD/YYYY') MATERIALDATEORDERED ,"+
                                    " e.TERRITORY_ID as territoryId  "+
                                    " from " +
									" v_new_field_employee e, " +
									" mv_fft_products_training p , " +
                                    "	(	select distinct product_cd,emplid,trunc(issued_on) issued_on  "+
                                    "		from "+
                                    "			FFT_PRODUCT_PEDAGOGUE_MAP pedmap," +
									"			fft_pedagogue_test_history fpt " +
                                    "		where " +
									"			pedmap.set_id=fpt.SET_ID ) sub1, " +
                                    "	(	SELECT DISTINCT MAX(TRUNC(DATEORDERED)) MATERIALDATEORDERED,FCR.PRODUCT_CD ,PERSON_ID AS EMPLID "+
                                    "		FROM " +
									"			FFT_MATERIAL_ORDER_HISTORY FMO," +
									"			FFT_CLUSTER_ROLE_PROD_TM FCR  " +
                                    "		WHERE " +
									"			FMO.INV_ID=FCR.TRAINING_MATERIAL_ID "+
                                    "			AND PRODUCT_CD IS NOT NULL  " +
									"		GROUP BY " +
									"			PRODUCT_CD ,PERSON_ID ) SUB2 "+
								" where  " +
									" p.emplid = e.emplid " +
									" and p.emplid=sub1.emplid(+) "+
                                    " AND p.EMPLID=SUB2.EMPLID(+) "+
                                    " AND SUB2.PRODUCT_CD(+) = P.PRODUCT_CD"+
                                    " and sub1.product_cd(+)=p.product_cd " ;

	private String	employeSql =
								" select distinct	 " +
									" e.emplid as emplId, " +
									" e.guid as guid, " +
									" e.promotion_date as promoDate, " +
									" e.effective_hire_date as hireDate, " +
									" e.sex as gender, " +
									" e.geo_type_desc as geoType, " +
									" e.email_address as email, " +
									" e.reports_to_emplid as reportsToEmplid," +
                                    " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ," +
									" e.area_cd as areaCd, " +
									" e.area_desc as areaDesc, " +
									" e.region_cd as regionCd, " +
									" e.region_desc as regionDesc, " +
									" e.district_id as districtId, " +
									" e.district_desc as districtDesc, " +
									" e.territory_id as territoryId, " +
									" e.territory_role_cd as role, " +
									" e.team_cd as teamCode, " +
									" e.cluster_cd as clusterCode, " +
									" e.last_name as lastName, " +
									" e.first_name as firstName, " +
									" e.middle_name as middleName, " +
									" e.preferred_name as preferredName " +
								" from " +
									" v_new_field_employee e " +
								" where   ";

	private String	basicemployeSql =
								" select distinct	 " +
									" e.emplid as emplId, " +
									" e.guid as guid, " +
                                    " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE " +
								" from " +
									" v_new_field_employee e " +
								" where   ";


    private String searchPOAEmployeeSql =
                                " select " +
                                " TO_CHAR(e.DATE_CREATED,'MM/DD/YY') as completedDate, " +
                                " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, " +
                                " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, " +
                                " e.emplid as emplId, " +
                                " e.product_cd as productCd," +
                                " e.last_name as lastName, " +
                                " e.first_name as firstName,"+
                                " e.middle_name as middleName,"+
                                " e.preferred_name as preferredName , "+
                                    " e.TERRITORY_ROLE_CD as roleCD," +
                                    " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"+
                                " e.TERRITORY_ID as territoryId"+
                                " from v_powers_midpoa1_data e ";


    /*private String searchPDFHSEmployeeSql =
                                " select distinct" +
                                " TO_CHAR(e.EXAM_TAKEN_DATE,'MM/DD/YY') as completedDate, " +
                                " TO_CHAR(eplc.EXAM_TAKEN_DATE,'MM/DD/YY') as completedDatePLC, " +
                                " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, " +
                                " DECODE(eplc.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, " +
                                " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, " +
                                " DECODE(eplc.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, " +
                                " e.emplid as emplId, " +
                                " e.product_cd as productCd," +
                                " e.last_name as lastName, " +
                                " e.first_name as firstName,"+
                                //" e.middle_name as middleName,"+
                                //" e.preferred_name as preferredName , "+
                                //" e.TERRITORY_ROLE_CD as roleCD," +
                                " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"+
                                " e.TERRITORY_ID as territoryId"+
                                " from v_pwra_hs_data_overall e inner join v_pwra_plc_data_overall eplc on " +
                                " e.emplid = eplc.emplid and " +
                                " e.product_cd = eplc.product_cd and " +
                                " e.last_name = eplc.last_name and " +
                                " e.first_name = eplc.first_name and " +
                                " e.territory_id = eplc.territory_id ";*/


    private String searchPDFHSEmployeeSql = "select distinct " +
                                " decode(hs.status,'I','', hs.exam_taken_date) as completedDate, " +
                                " decode(e.status,'I','', e.exam_taken_date) as completedDatePLC, " +
                                " DECODE(hs.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, " +
                                " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, " +
                                " DECODE(hs.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, " +
                                " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, " +
                                " e.emplid as emplId, " +
                                " (CASE WHEN e.product_cd = 'PLCA' THEN 'General Session' ELSE e.product_cd END) AS productCd, " +
                                " e.last_name as lastName, " +
                                " e.first_name as firstName,"+
                                " e.ROLE_CD as roleCD," +
                                " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"+
                                " e.TERRITORY_ID as territoryId" +
                                " from (select sub1.emplid,sub1.product_cd,sub1.status,OAL.overall_status, " +
                                " OAL.last_name, OAL.first_name, OAL.role_cd, OAL.empl_status, OAL.territory_id, OAL.area_cd, OAL.region_cd, " +
                                " OAL.district_id, OAL.cluster_cd, OAL.exam_taken_date from " +
                                " (select comp_list.*,'P' as status from (select distinct emplid,product_cd from v_pwra_plc_data_overall " +
                                " minus " +
                                " select distinct emplid,product_cd from v_pwra_plc_data_overall where status = 'I' or status = 'L')comp_list " +
                                " union " +
                                " select incomp_list.*, 'I' as status from (select distinct emplid,product_cd from v_pwra_plc_data_overall where status = 'I' or status = 'L')incomp_list)sub1," +
                                " (select emplid,product_cd,overall_status,last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd,max(exam_taken_date) as exam_taken_date " +
                                " from v_pwra_plc_data_overall group by emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd)OAL " +
                                " where sub1.emplid = OAL.emplid AND sub1.product_cd = OAL.product_cd)e, v_pwra_hs_data_overall hs " +
                                " where e.emplid = hs.emplid(+) and " +
                                " e.product_cd = hs.product_cd(+) ";


     private String searchSPFHSEmployeeSql = "select distinct " +
                                " null as completedDate, " +
                                " decode(sub1.status,'I','', e.exam_taken_date) as completedDatePLC, " +
                                " null as examStatus, " +
                                " DECODE(sub1.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, " +
                                " null as overallExamStatus, " +
                                " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, " +
                                " e.emplid as emplId, " +
                                " (CASE WHEN e.product_cd = 'PLCA' THEN 'General Session' ELSE e.product_cd END) AS productCd, " +
                                " e.last_name as lastName, " +
                                " e.first_name as firstName,"+
                                " e.ROLE_CD as roleCD," +
                                " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"+
                                " e.TERRITORY_ID as territoryId" +
                                " FROM " +
                                " (SELECT comp_list.*,'P' AS status FROM " +
                                " (SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall " +
                                " MINUS " +
                                " SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall WHERE status = 'I' or status = 'L')comp_list " +
                                " UNION " +
                                " SELECT incomp_list.*, 'I' AS status FROM (SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall WHERE status = 'I' or status = 'L') incomp_list) " +
                                " sub1, " +
                                " (SELECT emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd, MAX(exam_taken_date) AS exam_taken_date " +
                                " FROM v_spf_plc_data_overall GROUP BY emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd) e  " +
                                " WHERE sub1.emplid = e.emplid AND " +
                                " sub1.product_cd = e.product_cd ";


   private String employeeOrderByProdSql= " SELECT DISTINCT TO_CHAR(MAX(TRUNC (dateordered)),'MM/DD/YYYY') materialdateordered, "+
                                       " substr(max(source_order_id),4) as orderid "+
                                       " FROM v_rbu_material_order_history fmo, "+
                                       " fft_cluster_role_prod_tm fcr "+
                                       " WHERE fmo.inv_id = fcr.training_material_id "+
                                       " AND product_cd =? " +
                                       " AND person_id=? "+
                                       " GROUP BY product_cd, person_id "
                                       ;

	public RBUEmployeeHandler() {
	}

   
    
	public Employee getEmployeeById( String id ) {
        //System.out.print("Inside the getEmployeeById method");
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[1];
		criteria.append(" e.emplid = ? ");

		String sql = employeSql + criteria.toString();
        //System.out.print(sql);
		params[0] = id;

		Employee[] tmp = getEmployees ( sql, params );

		if ( tmp != null && tmp.length >= 1 ) {
			employee = tmp[0];
		} else {
			return null;
		}

		return  employee ;
	}


    public String getExemptionReason(String emplid,String product){


        String retString = null;

				ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
			/*Context ctx = new InitialContext();*/

			Timer timer = new Timer();

			/*DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			String var1 = "SELECT DISTINCT EXEMPTION_REASON"+
                          " FROM   MV_FFT_PRODUCTS_TRAINING"+
                          " WHERE  TRAINING_REQUIRED = 'Exempted'"+
                          "        AND UPPER(PRODUCT_CD) = UPPER('"+product+"')"+
                          "        AND EMPLID = '"+emplid+"'" ;


			log.info("The Exemption Query is**"+var1);

			rs = st.executeQuery(var1);

			if ( rs.next() ) {
				retString = rs.getString("EXEMPTION_REASON");
                log.info("The Exemption reason is**"+retString);
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

		return retString;


    }


public EmpSearchTSHT[] getTSHTEmployees( EmplSearchTSHTForm eform, UserSession uSession) {

        EmpSearchTSHT[] ret  = null;
		String sql = "SELECT DISTINCT EMPLID,LAST_NAME,FIRST_NAME ";
        sql = sql+" FROM V_TS_LEGACY_DATA e  ";

        StringBuffer criteria = new StringBuffer();

        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

        boolean firstWhere = true;



		//if(!Util.isEmpty(eform.getFname())||!Util.isEmpty( eform.getLname())){
            if ( !Util.isEmpty( eform.getEmplid()) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append(" e.emplid='"+eform.getEmplid().trim()+"'");
            }
            if ( !Util.isEmpty( eform.getFname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
            }
            if ( !Util.isEmpty( eform.getLname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
            }
            if ( !Util.isEmpty( eform.getCourseName() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " upper(e.course_name) like '" + eform.getCourseName().toUpperCase().trim()  + "%' " );
            }
        //}else if(!Util.isEmpty(eform.getEmplid())){
        //}

		criteria.append( " order by e.last_name,e.first_name " );

        sql = sql+criteria;

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );
        //System.out.println("Search SQL FOR THE EMPLOYEE:" +sql);

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

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
                EmpSearchTSHT e = new EmpSearchTSHT();
                //e.setTeamCode(rs.getString("TEAM_DESC"));
                //e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
                e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
                //e.setRole(rs.getString("ROLE_CD"));
                e.setEmplId(rs.getString("EMPLID"));
                //e.setActivityName(rs.getString("TRT_COURSE_NAME"));
                tempList.add(e);
			}

			ret = new EmpSearchTSHT[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchTSHT)tempList.get(j);
			}

		} catch (Exception e) {
            e.printStackTrace();
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

public EmpSearchTSHT[] getTSHTEmployeeDetail( String emplId, UserSession uSession) {

        EmpSearchTSHT[] ret  = null;
		String sql = "SELECT EMPLID,LAST_NAME,FIRST_NAME,HIRE_DATE,COURSE_CODE,COURSE_NAME, ";
        sql = sql+"COMPLETION_DATE,SCORE,NOTES,EMPL_STATUS,FIELD_ACTIVE,TERRITORY_ROLE_CD, ";
        sql = sql+"COURSE_STATUS,CREDITS ";
        sql = sql+" FROM V_TS_LEGACY_DATA where emplid='" + emplId + "' ORDER BY COURSE_CODE";

        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );

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

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
                EmpSearchTSHT e = new EmpSearchTSHT();
                //e.setTeamCode(rs.getString("TEAM_DESC"));
                //e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                e.setEmplId(rs.getString("EMPLID"));
                e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
                e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
                e.setHireDate(rs.getDate("HIRE_DATE"));
                e.setCourceCode(rs.getString("COURSE_CODE".toUpperCase()));
                e.setCourceName(rs.getString("COURSE_NAME".toUpperCase()));
                e.setCompletionDate(rs.getDate("COMPLETION_DATE"));

                e.setEmployeeStatus(rs.getString("EMPL_STATUS".toUpperCase()));
                e.setFieldActive(rs.getString("FIELD_ACTIVE".toUpperCase()));
                e.setScores(rs.getString("SCORE".toUpperCase()));
                e.setNotes(rs.getString("NOTES".toUpperCase()));
                e.setHrStatus(rs.getString("EMPL_STATUS".toUpperCase()));

                e.setRole(rs.getString("TERRITORY_ROLE_CD"));
                e.setCourseStatus(rs.getString("COURSE_STATUS"));
                e.setCredits(rs.getString("CREDITS"));


                tempList.add(e);
			}

			ret = new EmpSearchTSHT[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchTSHT)tempList.get(j);
			}

		} catch (Exception e) {
            e.printStackTrace();
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




	public String getEmployeeImage ( String id ) {
		String retString = null;

				ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
			/*Context ctx = new InitialContext();*/

			Timer timer = new Timer();

		/*	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			String sql =
					" select " +
						"  photo_file_name " +
					" from  " +
						" employee_pics " +
					 " where  " +
						" emplid = '" + id + "'";

			rs = st.executeQuery(sql);

			if ( rs.next() ) {
				retString = rs.getString("photo_file_name".toUpperCase());
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

		return retString;
	}

	public Employee getAreaManager( UserFilter uFilter ) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ( "All".equals( uFilter.getFilterForm().getArea() ) ) {
			return null;
		}
		String[] params = new String[1];

		criteria.append(" e.territory_role_cd = 'VP' ");
		criteria.append(" and e.area_cd = ? ");
		if ( !uFilter.isAdmin() ) {
			params = new String[2];
			criteria.append(" and e.cluster_cd = ? ");
			params[1] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getArea();




		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees ( sql,params );

		if ( tmp != null && tmp.length >= 1 ) {
			employee = tmp[0];
		} else {
			return null;
		}

		return  employee ;
	}
	public Employee getRegionManager( UserFilter uFilter ) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ( "All".equals( uFilter.getFilterForm().getArea() ) ) {
			return null;
		}
		if ( "All".equals( uFilter.getFilterForm().getRegion() ) ) {
			return null;
		}

		criteria.append("  e.territory_role_cd = 'RM' ");
		criteria.append(" and e.region_cd = ? ");
		criteria.append(" and e.area_cd = ? ");

		String[] params = new String[2];
		if ( !uFilter.isAdmin() ) {
			params = new String[3];
			criteria.append(" and e.cluster_cd = ? ");
			params[2] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getRegion();
		params[1] = uFilter.getFilterForm().getArea();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees ( sql, params );

		if ( tmp != null && tmp.length >= 1 ) {
			employee = tmp[0];
		} else {
			return null;
		}

		return  employee ;
	}
	public Employee getARManager( UserFilter uFilter ) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ( "All".equals( uFilter.getFilterForm().getArea() ) ) {
			return null;
		}
		if ( "All".equals( uFilter.getFilterForm().getRegion() ) ) {
			return null;
		}

		String[] params = new String[3];
		criteria.append(" e.cluster_cd = ? ");
		params[0] = uFilter.getClusterCode();
		criteria.append(" and  e.territory_role_cd = 'ARM' ");
		criteria.append(" and e.region_cd = ? ");
		params[1] = uFilter.getFilterForm().getRegion();
		criteria.append(" and e.area_cd = ? ");
		params[2] = uFilter.getFilterForm().getArea();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees ( sql, params );

		if ( tmp != null && tmp.length >= 1 ) {
			employee = tmp[0];
		} else {
			return null;
		}

		return  employee ;
	}
	public Employee getDistrictManager( UserFilter uFilter ) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ( "All".equals( uFilter.getFilterForm().getArea() ) ) {
			return null;
		}
		if ( "All".equals( uFilter.getFilterForm().getRegion() ) ) {
			return null;
		}
		if ( "All".equals( uFilter.getFilterForm().getDistrict() ) ) {
			return null;
		}

		criteria.append("  e.territory_role_cd = 'DM' ");
		criteria.append(" and e.area_cd = ? ");
		criteria.append(" and e.region_cd = ? ");
		criteria.append(" and e.district_id = ? ");

		String[] params = new String[3];
		if ( !uFilter.isAdmin() ) {
			criteria.append(" and e.cluster_cd = ? ");
			params = new String[4];
			params[3] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getArea();
		params[1] = uFilter.getFilterForm().getRegion();
		params[2] = uFilter.getFilterForm().getDistrict();


		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees ( sql, params );

		if ( tmp != null && tmp.length >= 1 ) {
			employee = tmp[0];
		} else {
			return null;
		}

		return  employee ;
	}

    public TrainingOrder empTrainingMaterialByProd(String emplid,String productFilter){
        TrainingOrder thisTrainingOrder=new TrainingOrder();
        ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
        try{
           /* Context ctx=new InitialContext();
			DataSource ds=(DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			st=conn.prepareStatement(employeeOrderByProdSql);
            st.setString(2,emplid.trim());
            st.setString(1,productFilter.trim().toUpperCase());
            log.info("SQL FOR EMPTRAININGMATRIAL"+employeeOrderByProdSql);
            rs=st.executeQuery();
            String trmID;
            String shipmentDate;
             while(rs.next()){
                trmID=rs.getString("orderid")==null?"":rs.getString("orderid");
                shipmentDate=rs.getString("materialdateordered")==null?"":rs.getString("materialdateordered");
                thisTrainingOrder.setTrmId(trmID);
                thisTrainingOrder.setOrderDate(shipmentDate);
             }
        }catch(Exception e){
            //System.out.println("Error in empTrainingMaterialByProd"+e);
            e.printStackTrace();
            log.error(e);
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
        return thisTrainingOrder;
    }//end of the Method

	public Employee[] getEmployees( UserFilter uFilter ) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[0];
		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			criteria.append(" and p.product_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[1];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[2];
				params[1] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
			criteria.append(" and p.product_cd = ? ");
	 		criteria.append(" and e.area_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[2];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[3];
				params[2] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
			criteria.append(" and p.product_cd = ? ");
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[3];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[4];
				params[3] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
			criteria.append(" and p.product_cd = ? ");
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
	 		criteria.append(" and e.district_id = ? ");
			if (uFilter.isAdmin()) {
				params = new String[4];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[5];
				params[4] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();
			params[3] = form.getDistrict();
		}

		String sql = productEmployeSql + criteria.toString();

		//log.info(sql);
		return getEmployees ( sql,params ) ;
	}

      	/**
	 * Searches users by name, will filter against employees that are managed
	 * by the user.
	 */
	public EmpSearchPOA[] getPOAEmployeesByName( EmplSearchForm eform, UserSession uSession ) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
		for (Iterator it = uSession.getUser().getProducts().iterator(); it.hasNext(); ) {
			Product prod = (Product)it.next();
			productCodes.add( prod.getProductCode() );
		}

		String productStr = Util.delimit(productCodes,"','");
        productStr = "MIDPOA1"; // Hotcoded for MID POA Module
		TerritoryFilterForm form = uSession.getUserFilterForm();

		criteria.append(" where e.product_cd in ('" + productStr + "') ");

		if ( !Util.isEmpty( eform.getFname() ) ) {
			criteria.append( " and (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%' or  upper(e.preferred_name) like '" +  eform.getFname().toUpperCase().trim() + "%' )" );
		}
		if ( !Util.isEmpty( eform.getLname() ) ) {
			criteria.append( " and upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%'" );
		}
        if(!Util.isEmpty(eform.getEmplid())){
            criteria.append("and e.emplid='"+eform.getEmplid().trim()+"'");
        }
        if(!Util.isEmpty(eform.getTerrId())){
            criteria.append("and e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
		}
		if ( !uSession.getUser().isAdmin() ) {
			criteria.append(" and e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}
		criteria.append( " order by e.emplid " );
		String sql = searchPOAEmployeeSql + criteria.toString();

        //System.out.println("@@@@ SQL" + sql);

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );
		return getPOAEmployeesSearch ( sql ) ;
	}

      	/**
	 * Searches users by name, will filter against employees that are managed
	 * by the user.
	 */
	public EmpSearchPDFHS[] getPDFHSEmployeesByName( EmplSearchForm eform, UserSession uSession, String event ) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
        boolean hasProducts = false;
		for (Iterator it = uSession.getUser().getProducts().iterator(); it.hasNext(); ) {
            hasProducts = true;
			Product prod = (Product)it.next();
			productCodes.add( prod.getProductCode() );
		}

		String productStr = Util.delimit(productCodes,"','");
		TerritoryFilterForm form = uSession.getUserFilterForm();

        if (hasProducts) {
            productStr = productStr + "','PLCA";
        }
        criteria.append(" and e.product_cd in ('" + productStr + "') ");

		if ( !Util.isEmpty( eform.getFname() ) ) {
			criteria.append( " and (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
		}
		if ( !Util.isEmpty( eform.getLname() ) ) {
			criteria.append( " and upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
		}
        if(!Util.isEmpty(eform.getEmplid())){
            criteria.append("and e.emplid='"+eform.getEmplid().trim()+"'");
        }
        if(!Util.isEmpty(eform.getTerrId())){
            criteria.append("and e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
		}
		if ( !uSession.getUser().isAdmin() ) {
			criteria.append(" and e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}


        /*criteria.append(" and eplc.product_cd in ('" + productStr + "')");

		if ( !Util.isEmpty( eform.getFname() ) ) {
			criteria.append( " and (upper(eplc.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
		}
		if ( !Util.isEmpty( eform.getLname() ) ) {
			criteria.append( " and upper(eplc.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
		}
        if(!Util.isEmpty(eform.getEmplid())){
            criteria.append("and eplc.emplid='"+eform.getEmplid().trim()+"'");
        }
        if(!Util.isEmpty(eform.getTerrId())){
            criteria.append("and eplc.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and eplc.region_cd = '" + form.getRegion() + "' ");
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and eplc.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and eplc.district_id = '" + form.getDistrict() + "' ");
		}
		if ( !uSession.getUser().isAdmin() ) {
			criteria.append(" and eplc.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}*/





		criteria.append( " order by e.emplid " );
        String sql = "";
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            sql = searchPDFHSEmployeeSql + criteria.toString();
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            sql = searchSPFHSEmployeeSql + criteria.toString();
        }

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );
        ////System.out.println("Search SQL FOR THE EMPLOYEE---"+ sql );

		return getPDFHSEmployeesSearch ( sql ) ;
	}


	public EmpSearchGNSM[] getGNSMEmployees( EmplSearchForm eform, UserSession uSession) {

        EmpSearchGNSM[] ret  = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
        sql = sql+" ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
        sql = sql+" ,DECODE(OVERALL_STATUS,'I','Registered','P','Complete','L','On-Leave')  OVERALL_STATUS ";
        sql = sql+" FROM V_GNSM_DATA_OVERALL e ";

        StringBuffer criteria = new StringBuffer();

        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

        boolean firstWhere = true;

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
            firstWhere = false;
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
            firstWhere = false;
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
            firstWhere = false;
		}

		if(!Util.isEmpty(eform.getFname())||!Util.isEmpty( eform.getLname())){
            if ( !Util.isEmpty( eform.getFname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
            }
            if ( !Util.isEmpty( eform.getLname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
            }
        }else if(!Util.isEmpty(eform.getEmplid())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.emplid='"+eform.getEmplid().trim()+"'");
        }else if(!Util.isEmpty(eform.getTerrId())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( !uSession.getUser().isAdmin() ) {
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}
		criteria.append( " order by e.emplid,TRT_COURSE_NAME " );

        sql = sql+criteria;

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );

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

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
                EmpSearchGNSM e = new EmpSearchGNSM();
                e.setTeamCode(rs.getString("TEAM_DESC"));
                e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
                e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
                e.setRole(rs.getString("ROLE_CD"));
                e.setEmplId(rs.getString("EMPLID"));
                e.setActivityName(rs.getString("TRT_COURSE_NAME"));
                e.setActivityStatus(rs.getString("STATUS"));
                e.setOverallStatus(rs.getString("OVERALL_STATUS"));
                tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchGNSM)tempList.get(j);
			}

		} catch (Exception e) {
            e.printStackTrace();
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

	public EmpSearchGNSM[] getMSEPIEmployees( EmplSearchForm eform, UserSession uSession) {

        EmpSearchGNSM[] ret  = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
        sql = sql+" ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
        sql = sql+" FROM V_MSEPI_NSM_DATA e ";

        StringBuffer criteria = new StringBuffer();

        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

        boolean firstWhere = true;

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
            firstWhere = false;
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
            firstWhere = false;
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
            firstWhere = false;
		}

		if(!Util.isEmpty(eform.getFname())||!Util.isEmpty( eform.getLname())){
            if ( !Util.isEmpty( eform.getFname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
            }
            if ( !Util.isEmpty( eform.getLname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
            }
        }else if(!Util.isEmpty(eform.getEmplid())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.emplid='"+eform.getEmplid().trim()+"'");
        }else if(!Util.isEmpty(eform.getTerrId())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( !uSession.getUser().isAdmin() ) {
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}
		criteria.append( " order by e.emplid,TRT_COURSE_NAME " );

        sql = sql+criteria;

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );

		ResultSet rs = null;
		Statement st = null;
	/*	Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
                EmpSearchGNSM e = new EmpSearchGNSM();
                e.setTeamCode(rs.getString("TEAM_DESC"));
                e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
                e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
                e.setRole(rs.getString("ROLE_CD"));
                e.setEmplId(rs.getString("EMPLID"));
                e.setActivityName(rs.getString("TRT_COURSE_NAME"));
                e.setActivityStatus(rs.getString("STATUS"));
                tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchGNSM)tempList.get(j);
			}

		} catch (Exception e) {
            e.printStackTrace();
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

    public Employee[] getPOAEmployees(UserFilter uFilter,String productCD,String section){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";

        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="I";
            courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="P";
            courseStatus="Completed";
        }

        String sqlQuery= " SELECT VP.DISTRICT_DESC DISTRICTDESC,"+
                         " FTM.TEAM_SHORT_DESC  TEAMCODE,"+
                         " VP.LAST_NAME LASTNAME,"+
                         " VP.FIRST_NAME FIRSTNAME,"+
                         " VP.TERRITORY_ROLE_CD ROLE,"+
                         " VP.EMAIL_ADDRESS as email, "+
                         " VP.EMPLID EMPLID,"+
                         " '"+courseStatus+"' coursestatus"+
                         " FROM   V_POWERS_MIDPOA1_DATA VP ,MV_TEAM_CODE_MAP ftm " +
                         " WHERE  VP.PRODUCT_CD = 'MIDPOA1'"+
                         " AND STATUS='"+thisSection+"' " +
                         " AND FTM.TEAM_CD=VP.TEAM_CD ";


             //ConditionQuery
            String condQuery="";


            if(!"ALL".equalsIgnoreCase(form.getArea())){
             condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
            }

            if(!"ALL".equalsIgnoreCase(form.getTeam())){
             condQuery=condQuery+ " AND VP.TEAM_CD='"+form.getTeam()+"' ";
            }

            if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
            }

            String orderQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

            sqlQuery=sqlQuery+condQuery+orderQuery;



          //System.out.println("Query Here is in EmployeeHandler for POA--"+sqlQuery);

           return getPOAEmployees ( sqlQuery,params ) ;



    }

/*
 *  Additional paramter added to avoid method name conflicts
*/
    public Employee[] getRBUEmployees(UserFilter uFilter,String productCD,String section, String selectedBU, String selectedRBU, String dummy, String emplid){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(productCD.equalsIgnoreCase("Aricept PC")) productCD="ARCPPC";
        if(productCD.equalsIgnoreCase("Aricept SM")) productCD="ARCPSM";
        if(productCD.equalsIgnoreCase("Caduet")) productCD="CADT";
        if(productCD.equalsIgnoreCase("Chantix")) productCD="CHTX";
        if(productCD.equalsIgnoreCase("Celebrex")) productCD="CLBR";
        if(productCD.equalsIgnoreCase("Eraxis")) productCD="ERXS";
        if(productCD.equalsIgnoreCase("Geodon PC")) productCD="GEODPC";
        if(productCD.equalsIgnoreCase("Geodon SM")) productCD="GEODSM";
        if(productCD.equalsIgnoreCase("HS/L Toviaz")) productCD="HSLTOVZ";
        if(productCD.equalsIgnoreCase("Lipitor")) productCD="LPTR";
        if(productCD.equalsIgnoreCase("Lyrica PC")) productCD="LYRCPC";
        if(productCD.equalsIgnoreCase("Lyrica SM")) productCD="LYRCSM";
        if(productCD.equalsIgnoreCase("OAB Toviaz")) productCD="OABTOVZ";
        if(productCD.equalsIgnoreCase("Selzentry")) productCD="SLZN";
        if(productCD.equalsIgnoreCase("Spiriva")) productCD="SPRV";
        if(productCD.equalsIgnoreCase("Vfend")) productCD="VFEN";
        if(productCD.equalsIgnoreCase("Viagra")) productCD="VIAG";
        if(productCD.equalsIgnoreCase("Zyvox")) productCD="ZYVX";
        
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
        String sqlQuery = "";
        if(thisSection.equals("C")){
            String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
         condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
            }

            String orderQuery=" ORDER BY FE.LAST_NAME";
            sqlQuery= " SELECT  FE.LAST_NAME LASTNAME,"+
                         " FE.FIRST_NAME FIRSTNAME,"+
                         " VP.EMPLID EMPLID, "+
                         " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu,"+
                         " VP. PRODUCT_CD as PRODUCT_CD,"+
                         " data.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, data.TEST_SCORE as TEST_SCORE," +
                         " '"+courseStatus+"' coursestatus"+
                         " FROM   MV_RBU_PED_SCE_DATA data, V_RBU_Live_Feed fe, v_rbu_future_alignment future, V_RBU_DASHBOARD_STATUS VP " +
                         " WHERE vp.status='C' and VP.PRODUCT_CD = '"+productCD+"' and  vp.emplid = fe.emplid(+)  and future.emplid(+) = vp.emplid and  vp.EMPLID = data.EMPLID(+) and vp.PRODUCT_CD = data.PRODUCT_CD(+)" ;
                       //  " and 0 = (select count(*) from  MV_RBU_PED_SCE_DATA  where emplid = vp.EMPLID and product_cd = vp.PRODUCT_CD and status in('NC','L'))";
             if(emplid != null && !emplid.equals("")){
	                 sqlQuery = sqlQuery + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                         
        sqlQuery =  sqlQuery + condQuery +  orderQuery;  
          } 
        else if(thisSection.equals("NC")){
            
            String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND future.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
            }

            String orderQuery=" ORDER BY FE.LAST_NAME ";
          /* String sqlQuery1= " SELECT FE.LAST_NAME LASTNAME,"+
                 " FE.FIRST_NAME FIRSTNAME,"+
                 " VP.EMPLID EMPLID, "+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,VP.PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE," +
                 " VP.EMPLID EMPLID,"+
                 " '"+courseStatus+"' coursestatus"+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe  " +
                 " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='"+thisSection+"' " + 
                 " AND vp.emplid = future.emplid(+)" +
                 " AND fe.emplid(+) = vp.emplid";*/
          String sqlQuery1= " SELECT DISTINCT FE.LAST_NAME LASTNAME,"+
                 " FE.FIRST_NAME FIRSTNAME,"+
                 " VP.EMPLID EMPLID, "+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,VP.PRODUCT_CD,"+
                 " data.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, data.TEST_SCORE as TEST_SCORE," +
//                 " VP.EMPLID EMPLID,"+
                 " '"+courseStatus+"' coursestatus"+
                 " FROM   V_RBU_DASHBOARD_STATUS VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe, MV_RBU_PED_SCE_DATA data  " +
                 " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='"+thisSection+"' " + 
                 " AND vp.emplid = future.emplid(+)" +
                 " AND fe.emplid(+) = vp.emplid and  vp.EMPLID = data.EMPLID(+) and vp.PRODUCT_CD = data.PRODUCT_CD(+)" ;
          
          if(emplid != null && !emplid.equals("")){
	             sqlQuery1 = sqlQuery1 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }        
                 
            sqlQuery1 =  sqlQuery1 + condQuery + orderQuery;  
             /* String sqlQuery2  = " UNION"+
                 " SELECT  FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,VP.EMPLID EMPLID,"+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu,"+
                 " VP. PRODUCT_CD as PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE,VP.EMPLID EMPLID,"+
                 " 'Complete' as  status "+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                 " WHERE  vp.emplid = fe.emplid(+)  and future.emplid(+) = vp.emplid and VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='C' "+
                 " and (select count(*) from  MV_RBU_PED_SCE_DATA  where emplid = vp.EMPLID  and PRODUCT_CD = '"+productCD+"' and status in('NC')) > 0";	
        if(emplid != null && !emplid.equals("")){
	            sqlQuery2 = sqlQuery2 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
        } 
                                    
        sqlQuery2 =  sqlQuery2 + condQuery +  orderQuery;  */
         /*String sqlQuery2 = " UNION"+
                            " SELECT distinct FE.LAST_NAME LASTNAME,FE.FIRST_NAME FIRSTNAME,"+
                            " VP.EMPLID EMPLID,fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,"+
                            " VP.PRODUCT_CD, null as EXAM_DISPLAY_NAME, null as TEST_SCORE,"+
                            " '"+courseStatus+"' coursestatus FROM   V_RBU_DASHBOARD_STATUS VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe"+   
                            " WHERE  VP.PRODUCT_CD = '"+productCD+"' AND VP.STATUS='"+thisSection+"' "+
                            " AND vp.emplid = future.emplid(+) AND fe.emplid(+) = vp.emplid"+
                            " and vp.emplid not in(select vp.emplid from V_RBU_DASHBOARD_STATUS VP,"+
                            " MV_RBU_PED_SCE_DATA data where VP.PRODUCT_CD = '"+productCD+"' AND VP.STATUS='"+thisSection+"'"+  
                            " AND vp.EMPLID = data.EMPLID(+) and data.PRODUCT_CD = vp.PRODUCT_CD)";    
        if(emplid != null && !emplid.equals("")){
	            sqlQuery2 = sqlQuery2 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
        } 
       
       sqlQuery2 =  sqlQuery2 + condQuery +  orderQuery;      
        sqlQuery =   sqlQuery1 + sqlQuery2;*/
       sqlQuery =   sqlQuery1;          
        }
        else if(thisSection.equals("L")){
            
            String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
            }

            String orderQuery=" ORDER BY 1";
            
            String sqlQuery1= " SELECT FE.LAST_NAME LASTNAME,"+
                 " FE.FIRST_NAME FIRSTNAME,"+
                 " VP.EMPLID EMPLID, "+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,VP.PRODUCT_CD,"+
                 " data.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, data.TEST_SCORE as TEST_SCORE," +
                 //" VP.EMPLID EMPLID,"+
                 " '"+courseStatus+"' coursestatus"+
                 " FROM   V_RBU_DASHBOARD_STATUS VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe,MV_RBU_PED_SCE_DATA data  " +
                 " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND vp.STATUS='"+thisSection+"' " + 
                 " AND vp.emplid = future.emplid(+)" +
                 " AND fe.emplid(+) = vp.emplid and  vp.EMPLID = data.EMPLID(+) and vp.PRODUCT_CD = data.PRODUCT_CD(+)";
              if(emplid != null && !emplid.equals("")){
	                   sqlQuery1 = sqlQuery1 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }    
            sqlQuery1 =  sqlQuery1 + condQuery + orderQuery; 
            
            /* String sqlQuery1= " SELECT FE.LAST_NAME LASTNAME,"+
                 " FE.FIRST_NAME FIRSTNAME,"+
                 " VP.EMPLID EMPLID, "+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,VP.PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE," +
                 " VP.EMPLID EMPLID,"+
                 " '"+courseStatus+"' coursestatus"+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe  " +
                 " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND STATUS='"+thisSection+"' " + 
                 " AND vp.emplid = future.emplid(+)" +
                 " AND fe.emplid(+) = vp.emplid";
              if(emplid != null && !emplid.equals("")){
	                   sqlQuery1 = sqlQuery1 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }    
            sqlQuery1 =  sqlQuery1 + condQuery ;  
           
            String sqlQuery3  = " UNION"+
                 " SELECT  FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,VP.EMPLID EMPLID,"+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu,"+
                 " VP. PRODUCT_CD as PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE,VP.EMPLID EMPLID,"+
                 " 'Complete' as  status "+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                 " WHERE  vp.emplid = fe.emplid(+)  and future.emplid(+) = vp.emplid and VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='C' "+
                 " and (select count(*) from  MV_RBU_PED_SCE_DATA  where emplid = vp.EMPLID  and PRODUCT_CD = '"+productCD+"' and status ='L') > 0";	
        if(emplid != null && !emplid.equals("")){
	             sqlQuery3 = sqlQuery3 + " and vp.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                             
        sqlQuery3 =  sqlQuery3 + condQuery + orderQuery;  
             
          /*String sqlQuery2 = " UNION" + 
                             " SELECT FE.LAST_NAME LASTNAME,FE.FIRST_NAME FIRSTNAME, rbu.EMPLID EMPLID,"+
                             " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu, rbu.REQUIRED_PRODUCT PRODUCT_CD,"+
                             " null as EXAM_DISPLAY_NAME, null as TEST_SCORE,"+
                             " rbu.EMPLID EMPLID, '"+courseStatus+"' coursestatus"+
                             " FROM   V_RBU_TRAINING_REQUIRED rbu, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe "+
                             " WHERE  rbu.REQUIRED_PRODUCT = '"+productCD+"' AND rbu.emplid = future.emplid(+)"+
                             " and rbu.emplid = fe.emplid(+) and rbu.emplid not in (select emplid from MV_RBU_PED_SCE_DATA) and fe.empl_status in ('L','P') " + 
                             " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' ";
            if(emplid != null && !emplid.equals("")){
	              sqlQuery2= sqlQuery2+ " and rbu.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                            
             sqlQuery2 =  sqlQuery2 + condQuery + orderQuery ; */   
             sqlQuery = sqlQuery1;
        }

//System.out.println("################# Query for single product " + sqlQuery);
           return getRBUEmployees( sqlQuery,params ) ;
    }


// added by shannon - filter with report to emplid
    public Employee[] getRBUEmployees(UserFilter uFilter,String reportto, String productCD,String section, String selectedBU, String selectedRBU){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(productCD.equalsIgnoreCase("Aricept PC")) productCD="ARCPPC";
        if(productCD.equalsIgnoreCase("Aricept SM")) productCD="ARCPSM";
        if(productCD.equalsIgnoreCase("Caduet")) productCD="CADT";
        if(productCD.equalsIgnoreCase("Chantix")) productCD="CHTX";
        if(productCD.equalsIgnoreCase("Celebrex")) productCD="CLBR";
        if(productCD.equalsIgnoreCase("Eraxis")) productCD="ERXS";
        if(productCD.equalsIgnoreCase("Geodon PC")) productCD="GEODPC";
        if(productCD.equalsIgnoreCase("Geodon SM")) productCD="GEODSM";
        if(productCD.equalsIgnoreCase("HS/L Toviaz")) productCD="HSLTOVZ";
        if(productCD.equalsIgnoreCase("Lipitor")) productCD="LPTR";
        if(productCD.equalsIgnoreCase("Lyrica PC")) productCD="LYRCPC";
        if(productCD.equalsIgnoreCase("Lyrica SM")) productCD="LYRCSM";
        if(productCD.equalsIgnoreCase("OAB Toviaz")) productCD="OABTOVZ";
        if(productCD.equalsIgnoreCase("Selzentry")) productCD="SLZN";
        if(productCD.equalsIgnoreCase("Spiriva")) productCD="SPRV";
        if(productCD.equalsIgnoreCase("Vfend")) productCD="VFEN";
        if(productCD.equalsIgnoreCase("Viagra")) productCD="VIAG";
        if(productCD.equalsIgnoreCase("Zyvox")) productCD="ZYVX";

            thisSection="NC";
            courseStatus="Not Complete";

        String sqlQuery = "";
       if(thisSection.equals("NC")){
            
            String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND future.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
            }

            String orderQuery=" ORDER BY 1,2 ";
           String sqlQuery1= " SELECT FE.LAST_NAME LASTNAME,"+
                 " FE.FIRST_NAME FIRSTNAME,"+
                 " VP.EMPLID EMPLID, "+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu,VP.PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE," +
                 " VP.EMPLID EMPLID,"+
                 " '"+courseStatus+"' coursestatus"+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_FUTURE_ALIGNMENT future, V_RBU_Live_Feed fe  " +
                 " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='"+thisSection+"' " + 
                 " AND vp.emplid = future.emplid(+)" +
                 " AND fe.emplid(+) = vp.emplid "
                 + "  and vp.emplid in (select distinct emplid from v_rbu_future_alignment where REPORTS_TO_EMPLID = '"+ reportto + "') ";
            sqlQuery1 =  sqlQuery1 + condQuery ;  
              String sqlQuery2  = " UNION"+
                 " SELECT  FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,VP.EMPLID EMPLID,"+
                 " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu,"+
                 " VP. PRODUCT_CD as PRODUCT_CD,"+
                 " VP.EXAM_DISPLAY_NAME as EXAM_DISPLAY_NAME, VP.TEST_SCORE as TEST_SCORE,VP.EMPLID EMPLID,"+
                 " 'Complete' as  status "+
                 " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                 " WHERE  vp.emplid = fe.emplid(+)  and future.emplid(+) = vp.emplid and VP.PRODUCT_CD = '"+productCD+"'"+
                 " AND VP.STATUS='C' "
                 + "  and vp.emplid in (select distinct emplid from v_rbu_future_alignment where REPORTS_TO_EMPLID = '"+ reportto + "') " 
                 + " and (select count(*) from  MV_RBU_PED_SCE_DATA  where emplid = vp.EMPLID  and PRODUCT_CD = '"+productCD+"' and status in('NC')) > 0";	
                                    
        sqlQuery2 =  sqlQuery2 + condQuery +  orderQuery;  
        sqlQuery =   sqlQuery1 + sqlQuery2;     
        }
//System.out.println("################# Query for single product " + sqlQuery);

           return getRBUEmployees( sqlQuery,params ) ;
    }
    //This method is used for General Session Entry.
    /*public GeneralSessionEmployee[] getGeneralSessionEmployees(){
		GeneralSessionEmployee[] ret = null;
        String thisSection="";
        String[] params = new String[0];

        String sqlQuery= " SELECT VP.DISTRICT_DESC as DISTRICTDESC,"+
                         " VP.TEAM_DESC  as TEAMCODE,"+
                         " VP.LAST_NAME as LASTNAME,"+
                         " VP.FIRST_NAME as FIRSTNAME,"+
                         " VP.ROLE_CD as ROLE,"+
                         " VP.EMPLID as EMPLID"+
                         " FROM   V_PWRA_PLC_DATA VP  " +
                         " WHERE  VP.EXAM_TYPE = 'ATD'";





		ResultSet rs = null;
		//PreparedStatement st = null;
		Connection conn = null;
        Statement st = null;

		try {


			Timer timer = new Timer();

			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();

            st = conn.createStatement();

			List tempList = new ArrayList();

			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				GeneralSessionEmployee curr = new GeneralSessionEmployee();
                curr.setDistrictDesc( rs.getString("districtDesc".toUpperCase()) );
                curr.setTeamCode( rs.getString("teamCode".toUpperCase()) );
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setRole( rs.getString("role".toUpperCase()) );
				curr.setEmplId( rs.getString("EMPLID") );
               	tempList.add( curr );
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
    }*/



    public Employee[] getPOAOverAllEmployees(UserFilter uFilter,String productCD,String section){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="I";
            courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="P";
            courseStatus="Completed";
        }

        String sqlQuery= " SELECT VP.DISTRICT_DESC DISTRICTDESC,"+
                         " FTM.TEAM_SHORT_DESC TEAMCODE,"+
                         " VP.LAST_NAME LASTNAME,"+
                         " VP.FIRST_NAME FIRSTNAME,"+
                         " VP.TERRITORY_ROLE_CD ROLE,"+
                         " VP.EMPLID EMPLID, "+
                         " VP.EMAIL_ADDRESS as email, "+
                         " VP. PRODUCT_CD, DECODE(VP.STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "+
                         " FROM   v_powers_midpoa1_data VP ,MV_TEAM_CODE_MAP ftm " +
                         " WHERE  OVERALL_STATUS='"+thisSection+"' " +
                         " AND PRODUCT_CD NOT IN ('NA') " +
                         " AND FTM.TEAM_CD=VP.TEAM_CD ";


             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(form.getArea())){
             condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
            }

            if(!"ALL".equalsIgnoreCase(form.getTeam())){
             condQuery=condQuery+ " AND VP.TEAM_CD='"+form.getTeam()+"' ";
            }

            if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
            }

            String orderQuery=" order by LASTNAME,FIRSTNAME ";

            sqlQuery=sqlQuery+condQuery+orderQuery;

          //System.out.println("Query Here is in EmployeeHandler for POA OverAll--"+sqlQuery);
          return getPOAOverAllEmployees(sqlQuery,params ) ;



    }
    //added by shannon for reorpt to filter
    public Employee[] getRBUOverAllEmployees(UserFilter uFilter,String reportto, String productCD,String section, String selectedBU, String selectedRBU){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
            thisSection="NC";
            courseStatus="Not Complete";
        //System.out.println("###################" + thisSection);
         String sqlQuery = "";
                String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                  condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
                }
    
                String orderQuery=" order by FE.LAST_NAME";
            String sqlQuery1= " SELECT  FE.LAST_NAME LASTNAME,"+
                         " FE.FIRST_NAME FIRSTNAME,"+
                         " VP.EMPLID EMPLID, "+
                         " fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu, "+
                         " VP. PRODUCT_CD as PRODUCT_CD,"+
                         " 'Not Complete' as  status  "+
                         " FROM   MV_RBU_PED_SCE_DATA VP, V_RBU_Live_Feed fe, v_rbu_future_alignment future " +
                         " WHERE  vp.emplid = fe.emplid(+)  and future.emplid(+) = VP.emplid" +
                         " AND VP.STATUS='"+thisSection+"'  "
                         + "  and vp.emplid in (select distinct emplid from v_rbu_future_alignment where REPORTS_TO_EMPLID = '"+ reportto + "') ";
              sqlQuery1=sqlQuery1+condQuery + orderQuery ;              
                      
           sqlQuery = sqlQuery1;
            //System.out.println("Query to get over all employees >>>>>>>>>>> " + sqlQuery);
          return getRBUOverAllEmployees(sqlQuery,params ) ;
    }

    public Employee[] getRBUOverAllEmployees(UserFilter uFilter,String productCD,String section, String selectedBU, String selectedRBU, String dummy, String emplid){
        TerritoryFilterForm form = uFilter.getFilterForm();
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
        //System.out.println("###################" + thisSection);
         String sqlQuery = "";
          if(thisSection.equals("C")){
              //ConditionQuery
                String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                 condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"'  ";
                }
    
                String orderQuery=" order by FE.LAST_NAME";
                String sqlQuery1 = " select distinct FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,"+
                                  " ds.EMPLID EMPLID, fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu, "+
                                  " ds.PRODUCT_CD as PRODUCT_CD,DECODE(ds.status, "+
                                  " 'C', 'Complete',"+
                                  " 'NC','Not Complete') as status"+
                                  " from V_RBU_Dashboard_Status ds, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                                  " WHERE ds.emplid =  fe.emplid(+) and ds.emplid =  future.emplid(+)"+
                                  " and ds.overall_status = 'C'";
                 if(emplid != null && !emplid.equals("")){
	                    sqlQuery1 = sqlQuery1 + " and ds.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }
                  sqlQuery=sqlQuery1+condQuery+orderQuery;         
          } 
          else if(thisSection.equals("NC")){
                String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                  condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"' ";
                }
    
                String orderQuery=" order by FE.LAST_NAME"; 
               String sqlQuery1 = " select distinct FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,"+
                                  " ds.EMPLID EMPLID,fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu, "+
                                  " ds.PRODUCT_CD as PRODUCT_CD,DECODE(ds.status,"+ 
                                  " 'C', 'Complete','NC','Not Complete') as status"+
                                  " from V_RBU_Dashboard_Status ds, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                                  " WHERE ds.emplid =  fe.emplid(+)"+
                                  " and ds.emplid =  future.emplid(+)"+
                                  " and ds.overall_status = 'NC'" ;
                                  
                if(emplid != null && !emplid.equals("")){
	                    sqlQuery1 = sqlQuery1 + " and ds.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
                 }                                  
           sqlQuery=sqlQuery1+condQuery+orderQuery; 
          } 
          else if(thisSection.equals("L")){
            //ConditionQuery
                String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                 condQuery=condQuery+ " AND future.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND future.rbu_desc='"+selectedRBU+"'";
                }
    
              String orderQuery=" order by 1";
              String sqlQuery1 = " select distinct FE.LAST_NAME LASTNAME, FE.FIRST_NAME FIRSTNAME,"+
                                 " ds.EMPLID EMPLID,fe.EMAIL_ADDRESS as EMAIL, future.bu as bu, future.rbu_desc as rbu, "+
                                 " ds.PRODUCT_CD as PRODUCT_CD,DECODE(ds.status, "+
                                 " 'C', 'Complete','NC','Not Complete','L', 'On Leave') as status"+
                                 " from V_RBU_Dashboard_Status ds, V_RBU_Live_Feed fe, v_rbu_future_alignment future"+
                                 " WHERE ds.emplid =  fe.emplid(+)"+
                                 " and ds.emplid =  future.emplid(+)"+
                                 " and ds.overall_status = 'L'";
                                 
                 if(emplid != null && !emplid.equals("")){
	                sqlQuery1 = sqlQuery1 + " and ds.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }
               sqlQuery1 = sqlQuery1 + condQuery;                   
             /* String sqlQuery2 =  " UNION"+
                         " SELECT  FE.LAST_NAME LASTNAME,FE.FIRST_NAME FIRSTNAME,rbu.EMPLID EMPLID,fe.EMAIL_ADDRESS as EMAIL, future.bu as bu,future.rbu_desc as rbu,"+
                         " rbu.REQUIRED_PRODUCT as PRODUCT_CD, 'On Leave' as Status from V_RBU_TRAINING_REQUIRED rbu, v_rbu_future_alignment future,"+
                         " V_RBU_Live_Feed fe"+
                         " WHERE  rbu.emplid = future.emplid(+) and rbu.emplid = fe.emplid(+)"+
                         " and rbu.emplid not in (select emplid from V_RBU_Dashboard_Status)  and fe.empl_status in ('L','P') " +
                         " AND future.rbu <> 'CGC' AND future.territory_role_cd <> 'RC' ";
                if(emplid != null && !emplid.equals("")){
	                    sqlQuery2 = sqlQuery2 + " and rbu.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                         
            sqlQuery2=sqlQuery2+condQuery+orderQuery;   */             
            sqlQuery = sqlQuery1 ;
          } 

            //System.out.println("Query to get over all employees >>>>>>>>>>> " + sqlQuery);
          return getRBUOverAllEmployees(sqlQuery,params ) ;
    }
    
    
     public Employee[] getToviazLaunchAttendanceEmployees(UserFilter uFilter,String productCD,String section, String selectedBU, String selectedRBU,String emplid){
        TerritoryFilterForm form = uFilter.getFilterForm();
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

        //System.out.println("###################" + thisSection);
         String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                 condQuery=condQuery+ " AND toviaz.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND toviaz.rbu_desc='"+selectedRBU+"'  ";
                }
                String orderQuery=" order by toviaz.LAST_NAME";
         String sqlQuery = "";
          if(thisSection.equals("C")){
              //ConditionQuery
                 String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status , toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.ATTENDANCE = 'Complete' ";          
           if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }   
                             
                  sqlQuery=sqlQuery1+condQuery+orderQuery;         
          } 
          else if(thisSection.equals("NC"))
          {
             String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL"+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.ATTENDANCE = 'Not Complete' ";          
                   if(emplid != null && !emplid.equals("")){
                      sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
                  }             
                             
                  sqlQuery=sqlQuery1+condQuery+orderQuery;      
                           
          } 
          else if(thisSection.equals("L")){
              String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.ATTENDANCE = 'On Leave' ";          
              if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                  
                  sqlQuery=sqlQuery1+condQuery+orderQuery;    
          } 
            //System.out.println("Query to get over all employees >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchOverAllEmployees(sqlQuery,params ) ;
    }

     public Employee[] getToviazLaunchAttendanceEmployeesForExec(UserFilter uFilter,String productCD,String section, String selectedBU, String selectedRBU,String emplid){
        TerritoryFilterForm form = uFilter.getFilterForm();
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

        //System.out.println("###################" + thisSection);
         String condQuery="";
               if(!"ALL".equalsIgnoreCase(selectedBU)){
                 condQuery=condQuery+ " AND toviaz.bu='"+selectedBU+"' ";
                }
    
                if(!"ALL".equalsIgnoreCase(selectedRBU)){
                 condQuery=condQuery+ " AND toviaz.rbu_desc='"+selectedRBU+"'  ";
                }
                String orderQuery=" order by toviaz.LAST_NAME";
         String sqlQuery = "";
          if(thisSection.equals("C")){
              //ConditionQuery
                 String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status , toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL "+
                             " from V_TOVIAZ_LAUNCH_EXEC_STATUS toviaz where toviaz.ATTENDANCE = 'Complete' ";          
           if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }   
                  sqlQuery=sqlQuery1+condQuery+orderQuery;         
          } 
          else if(thisSection.equals("NC"))
          {
             String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL"+
                             " from V_TOVIAZ_LAUNCH_EXEC_STATUS toviaz where toviaz.ATTENDANCE = 'Not Complete' ";          
                   if(emplid != null && !emplid.equals("")){
                      sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
                  }             
                             
                  sqlQuery=sqlQuery1+condQuery+orderQuery;      
                           
          } 
          else if(thisSection.equals("L")){
              String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.ATTENDANCE as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from V_TOVIAZ_LAUNCH_EXEC_STATUS toviaz where toviaz.ATTENDANCE = 'On Leave' ";          
              if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                  
            
            sqlQuery=sqlQuery1+condQuery+orderQuery;    
          } 
            //System.out.println("Query to get over all employees >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchOverAllEmployees(sqlQuery,params ) ;
    }

    public Employee[] getToviazLaunchOverallEmployees(String section, String selectedBU, String selectedRBU,String emplid){
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

        //System.out.println("###################" + thisSection);
          String sqlQuery = "";
           String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND toviaz.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND toviaz.rbu_desc='"+selectedRBU+"' ";
            } 
            String orderQuery=" ORDER BY toviaz.LAST_NAME";
         if(thisSection.equals("C")){
            String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, toviaz.ATTENDANCE as ATTENDANCE ,toviaz.OVERALL as status,toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.OVERALL = 'Complete' ";          
            if(emplid != null && !emplid.equals("")){
                sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          } 
        else if(thisSection.equals("NC")){
         String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, toviaz.ATTENDANCE as ATTENDANCE ,toviaz.OVERALL as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.OVERALL = 'Not Complete' ";          
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }   
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
        }
        else if(thisSection.equals("L")){
           String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, toviaz.ATTENDANCE as ATTENDANCE , toviaz.OVERALL as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz.OVERALL = 'On Leave' ";          
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
        }
            //System.out.println("Query to get over all employees >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchOverAllEmployeesForList(sqlQuery,params) ;
    }
    
    public Employee[] getToviazLaunchSpecificEmployees(String examName,String section, String selectedBU, String selectedRBU,String emplid){
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

        //System.out.println("###################" + thisSection);
          String sqlQuery = "";
          String condQuery="";

           if(!"ALL".equalsIgnoreCase(selectedBU)){
             condQuery=condQuery+ " AND toviaz.bu='"+selectedBU+"'";
            }

            if(!"ALL".equalsIgnoreCase(selectedRBU)){
             condQuery=condQuery+ " AND toviaz.rbu_desc='"+selectedRBU+"' ";
            } 
            String orderQuery=" ORDER BY toviaz.LAST_NAME"; 
          
         if(thisSection.equals("C")){
            
            
            String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, 'Complete' as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz."+examName+" not in ('Not Complete', 'On Leave','N/A') ";          
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          } 
        else if(thisSection.equals("NC")){
            
          
          
           String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, toviaz."+examName+" as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL  "+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz."+examName+"  = 'Not Complete' ";          
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;   
          
        }
        else if(thisSection.equals("L")){
           String sqlQuery1 =   " SELECT  toviaz.LAST_NAME LASTNAME,"+
                             " toviaz.FIRST_NAME FIRSTNAME,"+
                             " toviaz.EMPLID EMPLID, "+
                             " toviaz.EMAIL_ADDRESS as EMAIL, toviaz.bu as bu, toviaz.rbu_desc as rbu,"+
                             " toviaz.PED1 as PED1, toviaz.PED2 as PED2, toviaz."+examName+" as status, toviaz.REPORTS_TO_EMAIL as REPORTSTOEMAIL"+
                             " from MV_TOVIAZ_LAUNCH_STATUS toviaz where toviaz."+examName+"  = 'On Leave' ";          
            if(emplid != null && !emplid.equals("")){
                    sqlQuery1 = sqlQuery1 + "and toviaz.emplid in(select reports from V_RBU_REPORTING_HIERARCHY where emplid = '"+emplid+"') ";                                    
            }                                
                  sqlQuery=sqlQuery1+condQuery+orderQuery;     
        }
            //System.out.println("Query to get specific employees >>>>>>>>>>> " + sqlQuery);
          return getToviazLaunchEmployees(sqlQuery,params) ;
    }


   //This function is made more generic so that it can be reused.
   public Employee[] getSPFEmployees(UserFilter uFilter,String productCD,String section)
   {
    TerritoryFilterForm form = uFilter.getFilterForm();
    Employee[] ret = null;
    String thisSection="";
    String[] params = new String[0];
    String courseStatus="";
    if(productCD.equalsIgnoreCase("Geodon")) productCD="GEOD";
    if(productCD.equalsIgnoreCase("Aricept")) productCD="ARCP";
    if(productCD.equalsIgnoreCase("Lyrica")) productCD="LYRC";
    if(productCD.equalsIgnoreCase("Celebrex")) productCD="CLBR";
    if(productCD.equalsIgnoreCase("Rebif")) productCD="REBF";
    if(section.equalsIgnoreCase("Not Complete")) {
        thisSection="I";
        courseStatus="Not Complete";
    }
    else
    if(section.equalsIgnoreCase("On Leave")) {
        thisSection="L";
        courseStatus="On Leave";
    }
    else
    if(section.equalsIgnoreCase("Complete")) {
        thisSection="P";
        courseStatus="Completed";
    }

    String sqlQuery= " SELECT VP.DISTRICT_DESC DISTRICTDESC,"+
                     " VP.TEAM_DESC  TEAMCODE,"+
                     " VP.LAST_NAME LASTNAME,"+
                     " VP.FIRST_NAME FIRSTNAME,"+
                     " VP.ROLE_CD ROLE,"+
                     " VP.EMAIL_ADDRESS as email, "+
                     " VP.EMPLID EMPLID,"+
                     " '"+courseStatus+"' coursestatus"+
                     " FROM   V_SPF_HS_DATA VP  " +
                     " WHERE  VP.PRODUCT_CD = '"+productCD+"'"+
                     " AND STATUS='"+thisSection+"' ";



         //ConditionQuery
        String condQuery="";


        if(!"ALL".equalsIgnoreCase(form.getArea())){
         condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
        }

        if(!"ALL".equalsIgnoreCase(form.getTeam())){
         condQuery=condQuery+ " AND VP.TEAM_CD='"+form.getTeam()+"' ";
        }

        if(!"ALL".equalsIgnoreCase(form.getRegion())){
         condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
         condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }

        String orderQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

        sqlQuery=sqlQuery+condQuery+orderQuery;



      //System.out.println("Query Here is in EmployeeHandler for POA--"+sqlQuery);

       return getRBUEmployees( sqlQuery,params ) ;
 }

	/**
	 * Searches users by name, will filter against employees that are managed
	 * by the user.
	 */
	public EmpSearch[] getEmployeesByName( EmplSearchForm eform, UserSession uSession ) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
		for (Iterator it = uSession.getUser().getProducts().iterator(); it.hasNext(); ) {
			Product prod = (Product)it.next();
			productCodes.add( prod.getProductCode() );
		}

		String productStr = Util.delimit(productCodes,"','");
		TerritoryFilterForm form = uSession.getUserFilterForm();

		criteria.append(" and p.product_cd in ('" + productStr + "') ");

		if ( !Util.isEmpty( eform.getFname() ) ) {
			criteria.append( " and (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%')" );
		}
		if ( !Util.isEmpty( eform.getLname() ) ) {
			criteria.append( " and upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%'" );
		}
        if(!Util.isEmpty(eform.getEmplid())){
            criteria.append("and e.emplid='"+eform.getEmplid().trim()+"'");
        }
        if(!Util.isEmpty(eform.getTerrId())){
            criteria.append("and e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
		}
		if ( !uSession.getUser().isAdmin() ) {
			criteria.append(" and e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}
		criteria.append( " order by e.emplid " );
		String sql = searchEmployeSql + criteria.toString();
		log.info("Search SQL FOR THE EMPLOYEE---"+ sql );
		return getEmployeesSearch ( sql ) ;
	}

	public Employee[] getEmployeeByRole( String role ) {

		String sql =
				" select  " +
					" emplid as emplId, " +
                    " geo_type_desc as geoType, " +
					" promotion_date as promoDate, " +
					" effective_hire_date as hireDate, " +
					" sex as gender, " +
					" email_address as email, " +
					" reports_to_emplid as reportsToEmplid," +
                    " DECODE(empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ," +
					" area_cd as areaCd, " +
					" area_desc as areaDesc, " +
					" region_cd as regionCd, " +
					" region_desc as regionDesc, " +
					" district_id as districtId, " +
					" district_desc as districtDesc, " +
					" territory_id as territoryId, " +
					" territory_role_cd as role, " +
					" team_cd as teamCode, " +
					" cluster_cd as clusterCode, " +
					" last_name as lastName, " +
					" first_name as firstName, " +
					" middle_name as middleName, " +
					" preferred_name as preferredName " +
				" from  " +
					" v_new_field_employee " +
				" where  " +
				"  cluster_cd in ('Cust Bus Unit','Steere','Pratt','Powers','Specialty Markets','Pratt Steere PR','Powers - PR','SM PR','ONC Bus Unit')   ";

		String[] params = new String[0];
		sql = sql + " and territory_role_cd in (" + role + ") order by last_name,first_name  ";

		return getEmployees( sql, params );
	}
	public HashMap getEmployeesForEmail() {

		/*String sql  = " SELECT DISTINCT future.emplid AS reportstoemplid, future.reports AS emplid,"+
                      " mv.first_name AS firstname, mv.last_name AS lastname"+
                      " FROM v_rbu_reporting_hierarchy future,v_rbu_dashboard_status ds,v_rbu_live_feed mv,"+
                      " rbu_sustainability_mgr_roles smr,rbu_sustainability_empl_roles ser,v_rbu_future_alignment fa1,"+
                      " v_rbu_future_alignment fa2 WHERE future.reports = ds.emplid"+
                      " AND future.emplid = fa1.emplid AND future.reports = fa2.emplid"+
                      " AND future.reports = mv.emplid AND future.emplid IS NOT NULL"+
                      " AND ds.overall_status = 'NC' AND future.reportslevel = 1"+
                      " AND fa1.territory_role_cd = smr.role_cd  AND smr.email_flag = 'Y'"+
                      " AND fa2.territory_role_cd NOT IN (SELECT role_cd  FROM rbu_sustainability_empl_roles"+
                      " WHERE email_flag = 'N')"+
                      " UNION"+
                      " SELECT DISTINCT future.emplid AS reportstoemplid, future.reports AS emplid,"+
                      " mv.first_name AS firstname, mv.last_name AS lastname"+
                      " FROM v_rbu_reporting_hierarchy future,"+
                      " mv_rbu_ped_sce_data psd,"+
                      " v_rbu_live_feed mv, rbu_sustainability_mgr_roles smr,"+
                      " rbu_sustainability_empl_roles ser, v_rbu_future_alignment fa1,"+
                      " v_rbu_future_alignment fa2  WHERE future.reports = psd.emplid"+
                      " AND future.emplid = fa1.emplid"+
                      " AND future.reports = fa2.emplid"+
                      " AND future.reports = mv.emplid"+
                      " AND future.emplid IS NOT NULL"+
                      " AND FLOOR (psd.test_score) < 80"+
                      " AND UPPER (psd.exam_type) = 'PED'"+
                      " AND future.reportslevel = 1"+
                      " AND fa1.territory_role_cd = smr.role_cd"+
                      " AND smr.email_flag = 'Y'"+
                      " AND fa2.territory_role_cd NOT IN ("+
                      " SELECT role_cd  FROM rbu_sustainability_empl_roles WHERE email_flag = 'N')"+
                      " order by 1";*/
		String sql = "SELECT REPORTS_TO_EMPLID as REPORTSTOEMPLID, EMPLID as EMPLID, FIRST_NAME as FIRSTNAME, LAST_NAME as LASTNAME, REPORTS_TO_EMAIL as REPORTSTOEMAIL from V_RBU_EMAIL_REMINDERS order by REPORTS_TO_EMPLID";

		String[] params = new String[0];
//System.out.println("Getting employees for sending reminder emails ################ " + sql);
		return getEmployeesForRBUEmail( sql, params );
	}
    
    


	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
            log.debug("SQL:" + sql);

			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/


			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(1000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
            //System.out.println("before run:" + timer.getFromLast());
			rs = st.executeQuery();

            //System.out.println("query run:" + timer.getFromLast());
			ret = convertRecords(rs);
            //System.out.println("query convert:" + timer.getFromLast());
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


/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private HashMap getEmployeesForRBUEmail( String sql, String params[] ) {
		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
       HashMap reportsToEmployees = new HashMap();
		try {
            log.debug("SQL:" + sql);

			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(1000);
			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            
            while(rs.next()){
              List employees ;  
              Employee currentEmployee = new Employee();
              String emplid = rs.getString("EMPLID");
              String reportsToEmplid = rs.getString("REPORTSTOEMPLID");
              if(reportsToEmployees.containsKey(reportsToEmplid)){
                    employees = (List)reportsToEmployees.get(reportsToEmplid);
                }
                else{
                    employees = new ArrayList();
                }
                currentEmployee.setEmplId(emplid);
                currentEmployee.setReportsToEmplid(reportsToEmplid);
                if(rs.getString("FIRSTNAME") != null){
                    currentEmployee.setFirstName(rs.getString("FIRSTNAME"));
                }
                if(rs.getString("LASTNAME") != null){
                    currentEmployee.setLastName(rs.getString("LASTNAME"));
                }
                if(rs.getString("REPORTSTOEMAIL") != null){
                    currentEmployee.setReportsToEmail(rs.getString("REPORTSTOEMAIL"));
                }
                employees.add(currentEmployee);
                reportsToEmployees.put(reportsToEmplid, employees);                
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
		return reportsToEmployees;
	}
    private EmpSearchPOA[] getPOAEmployeesSearch( String sql ) {
		EmpSearchPOA[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {


			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchPOA curr = new EmpSearchPOA();
				curr.setEmplId( rs.getString("EMPLID") );
				curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
				curr.setMiddleName( rs.getString("middleName".toUpperCase()) );
				curr.setPreferredName( rs.getString("preferredName".toUpperCase()) );
				curr.setProductCd( rs.getString("productCd".toUpperCase()) );
                curr.setFieldActive(rs.getString("fieldActive"));
                curr.setTerritoryId(rs.getString("territoryId"));
                curr.setRoleCd(rs.getString("roleCD"));
                curr.setCompletedDate(rs.getString("completedDate"));
                curr.setExamStatus(rs.getString("examStatus"));
                curr.setOverallExamStatus(rs.getString("overallExamStatus"));
				tempList.add( curr );
                //System.out.println("@@@@ Data output:" + rs.getString("EMPLID"));
			}

			ret = new EmpSearchPOA[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchPOA)tempList.get(j);
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

    private EmpSearchPDFHS[] getPDFHSEmployeesSearch( String sql ) {
		EmpSearchPDFHS[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {


			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchPDFHS curr = new EmpSearchPDFHS();
				curr.setEmplId( rs.getString("EMPLID") );
				curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
				curr.setProductCd( rs.getString("productCd".toUpperCase()) );
                curr.setFieldActive(rs.getString("fieldActive"));
                curr.setTerritoryId(rs.getString("territoryId"));
                curr.setRoleCd(rs.getString("roleCD"));
                curr.setCompletedDate(rs.getString("completedDate"));
                curr.setExamStatus(rs.getString("examStatus"));
                curr.setOverallExamStatus(rs.getString("overallExamStatus"));
                curr.setcompletedDatePLC(rs.getString("completedDatePLC"));
                curr.setoverallExamStatusPLC(rs.getString("overallExamStatusPLC"));
                curr.setPLCStatus(rs.getString("plcexamStatus"));
				tempList.add( curr );
                //System.out.println("@@@@ Data output:" + rs.getString("EMPLID"));
			}

			ret = new EmpSearchPDFHS[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchPDFHS)tempList.get(j);
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


    /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPOAEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {


			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/


			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();

			while (rs.next()) {
				Employee curr = new Employee();
                curr.setDistrictDesc( rs.getString("districtDesc".toUpperCase()) );
                curr.setTeamCode( rs.getString("teamCode".toUpperCase()) );
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setRole( rs.getString("role".toUpperCase()) );
				curr.setEmplId( rs.getString("EMPLID") );
                curr.setEmail(rs.getString("EMAIL") );
                curr.setCourseStatus(rs.getString("coursestatus"));
                /**
				curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				curr.setRegionDesc( rs.getString("regionDesc".toUpperCase()) );
				curr.setDistrictId( rs.getString("districtId".toUpperCase()) );

				curr.setTerritoryId( rs.getString("territoryId".toUpperCase()) );


				curr.setClusterCode( rs.getString("clusterCode".toUpperCase()) );
				curr.setMiddleName( rs.getString("middleName".toUpperCase()) );
				curr.setPreferredName( rs.getString("preferredName".toUpperCase()) );

				curr.setGender( rs.getString("gender".toUpperCase()) );
				curr.setEmail( rs.getString("email".toUpperCase()) );

				curr.setReportsToEmplid( rs.getString("reportsToEmplid".toUpperCase()) );


                curr.setEmployeeStatus(rs.getString("fieldactive"));
				Date hireDate = rs.getDate("hireDate".toUpperCase());
				if ( hireDate != null ) {
					curr.setHireDate( new Date( hireDate.getTime() )  );
				}
				Date promoDate = rs.getDate("promoDate".toUpperCase());
				if ( promoDate != null ) {
					curr.setPromoDate( new Date( promoDate.getTime() )  );
				}

				 **/


               	tempList.add( curr );
			}

			ret = new Employee[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (Employee)tempList.get(j);
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
    
    
    public static List getExamList( String productCD) {
		List examList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
			Timer timer = new Timer();
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
            String sql = "SELECT EXAM_DISPLAY_NAME from RBU_P2L_CODES where PRODUCT_CD =  '"+productCD+"' and upper(EXAM_DISPLAY_NAME) not in('OVERALL') order by EXAM_DISPLAY_NAME asc";
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);
			rs = st.executeQuery();
			while (rs.next()) {
			
                String name = rs.getString("EXAM_DISPLAY_NAME");	
               	examList.add( name );
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
		return examList;
	}
    
    
    public static List getExamScores( String productCD, String emplid) {
		List examList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement st = null;
        ResultSet rs1 = null;
		PreparedStatement st1 = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
			Timer timer = new Timer();
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
            String sql = "SELECT EXAM_DISPLAY_NAME from rbu_p2l_codes where PRODUCT_CD =  '"+productCD+"' " +
            " order by EXAM_DISPLAY_NAME asc";
			//System.out.println("Query first ################# " + sql);
            st = conn.prepareCall(sql);
			st.setFetchSize(5000);
			rs = st.executeQuery();
            
			while (rs.next()) {
			    String name = rs.getString("EXAM_DISPLAY_NAME");
                // Get the scores for each of the exams
                String sql1 = "Select EXAM_DISPLAY_NAME, TEST_SCORE from V_RBU_PED_SCE_DATA where EXAM_DISPLAY_NAME = '"+name+"' and emplid = '"+emplid+"' and PRODUCT_CD =  '"+productCD+"'"	;
               // System.out.println("Query second ################# " + sql1);
                st1 = conn.prepareCall(sql1);
                    st1.setFetchSize(5000);
                    rs1 = st1.executeQuery(); 
                 while(rs1 != null && rs1.next()){
                    Map testScores = new HashMap();
                        String examName = rs1.getString("EXAM_DISPLAY_NAME");
                        String testScore = rs1.getString("TEST_SCORE");
                        //String testScore = "100";
                        //System.out.println("Test score >>>>>>>>>> " + testScore);
                        testScores.put(examName, testScore);
                        examList.add(testScores);  
                 } 
			}
           // System.out.println("Size of list in action ############ " + examList.size());

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
            if ( rs1 != null) {
				try {
					rs1.close();
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
            if ( st1 != null) {
				try {
					st1.close();
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
		return examList;
	}
    

    /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getRBUEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
    //System.out.println("In getRBUEmployees ##############################");
		try {

             HashMap empHashMap=new LinkedHashMap();
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            String emplid="";
            Employee curr = null;
            List examScores = null;
			while (rs.next()) {
                emplid=rs.getString("EMPLID");
                 if(empHashMap.containsKey(emplid)){
                    curr=(Employee)empHashMap.get(emplid);
                    examScores = curr.getAvailableExams();
                }else{
                    curr = new Employee();
                    examScores = new ArrayList();
                }
                String productCD = rs.getString("PRODUCT_CD");
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setEmail(rs.getString("EMAIL") );
                curr.setFutureBU(rs.getString("bu".toUpperCase()));
                curr.setFutureRBU(rs.getString("rbu".toUpperCase()));
                curr.setCourseStatus(rs.getString("coursestatus").toUpperCase());
                curr.setEmplId(emplid);
                HashMap scores = new HashMap();
                if(rs.getString("EXAM_DISPLAY_NAME") != null){
                    //System.out.println("Exam added ############### " + rs.getString("EXAM_DISPLAY_NAME"));
                    scores.put(rs.getString("EXAM_DISPLAY_NAME"), rs.getString("TEST_SCORE"));
                }
                examScores.add(scores);
                curr.setAvailableExams(examScores);
               empHashMap.put(emplid,curr);
			}

			String emplidTemp="";
            //System.out.println("HashMap Size here is for employees for a particular product "+empHashMap.size());
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
    
    
    /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getToviazLaunchEmployees( String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
  		
    //System.out.println("In getToviazLaunchEmployees ##############################");
		try {

             HashMap empHashMap=new LinkedHashMap();
             
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
			
             List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
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
                curr.setPed1(rs.getString("PED1"));
                curr.setPed2(rs.getString("PED2"));
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
            //System.out.println("HashMap Size here is for employees for a particular product "+empHashMap.size());
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


/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getToviazLaunchOverAllEmployeesForList( String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		
    //System.out.println("In getToviazLaunchOverAllEmployees ##############################");
		try {

             HashMap empHashMap=new LinkedHashMap();
             
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/
             
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            String emplid="";
            Employee curr = null;
            List examScores = null;
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
                curr.setPed1(rs.getString("PED1"));
                curr.setPed2(rs.getString("PED2"));
                curr.setAttendance(rs.getString("ATTENDANCE"));
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
            //System.out.println("HashMap Size here is for employees for a particular product "+empHashMap.size());
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

     /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPOAOverAllEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
  		
		try {


			Timer timer = new Timer();
            HashMap empHashMap=new LinkedHashMap();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/


			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            String emplid="";
            Employee curr=null;
			while (rs.next()) {
                //Build a hashmap with Emplid as key and EmpBean as object
                emplid=rs.getString("EMPLID");
                if(empHashMap.containsKey(emplid)){
                    curr=(Employee)empHashMap.get(emplid);
                }else{
                    curr = new Employee();
                }
                curr.setDistrictDesc( rs.getString("districtDesc".toUpperCase()) );
                curr.setTeamCode( rs.getString("teamCode".toUpperCase()) );
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setRole( rs.getString("role".toUpperCase()) );
                curr.setEmail(rs.getString("EMAIL") );
				curr.setEmplId( emplid );
                curr.addToproductStatusMap(rs.getString("PRODUCT_CD").toUpperCase(),rs.getString("Status"));
                empHashMap.put(emplid,curr);
                /**
				curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				curr.setRegionDesc( rs.getString("regionDesc".toUpperCase()) );
				curr.setDistrictId( rs.getString("districtId".toUpperCase()) );

				curr.setTerritoryId( rs.getString("territoryId".toUpperCase()) );


				curr.setClusterCode( rs.getString("clusterCode".toUpperCase()) );
				curr.setMiddleName( rs.getString("middleName".toUpperCase()) );
				curr.setPreferredName( rs.getString("preferredName".toUpperCase()) );

				curr.setGender( rs.getString("gender".toUpperCase()) );
				curr.setEmail( rs.getString("email".toUpperCase()) );

				curr.setReportsToEmplid( rs.getString("reportsToEmplid".toUpperCase()) );


                curr.setEmployeeStatus(rs.getString("fieldactive"));
				Date hireDate = rs.getDate("hireDate".toUpperCase());
				if ( hireDate != null ) {
					curr.setHireDate( new Date( hireDate.getTime() )  );
				}
				Date promoDate = rs.getDate("promoDate".toUpperCase());
				if ( promoDate != null ) {
					curr.setPromoDate( new Date( promoDate.getTime() )  );
				}

				 **/


			}
            String emplidTemp="";
            //System.out.println("HashMap Size here is "+empHashMap.size());
            ret = new Employee[empHashMap.size()];
            int c=0;
            for(Iterator iter=empHashMap.keySet().iterator();iter.hasNext();){
            emplidTemp = iter.next().toString();
            ret[c++]=(Employee)empHashMap.get(emplidTemp);
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


     /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getRBUOverAllEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
  		
		try {
            HashMap empHashMap=new LinkedHashMap();
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/


			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            String emplid="";
            Employee curr=null;
			while (rs.next()) {
                //Build a hashmap with Emplid as key and EmpBean as object
                emplid=rs.getString("EMPLID");
                if(empHashMap.containsKey(emplid)){
                    curr=(Employee)empHashMap.get(emplid);
                }else{
                    curr = new Employee();
                }
                // Set some of the items to empty..
                curr.setDistrictDesc("" );
                curr.setTeamCode( "" );
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
                curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setEmail(rs.getString("EMAIL") );
                curr.setFutureBU(rs.getString("bu"));
                curr.setFutureRBU(rs.getString("rbu"));
				curr.setEmplId( emplid );
                curr.addToproductStatusMap(rs.getString("PRODUCT_CD").toUpperCase(),rs.getString("Status"));
                empHashMap.put(emplid,curr);
			}
            String emplidTemp="";
            //System.out.println("HashMap Size here is "+empHashMap.size());
            ret = new Employee[empHashMap.size()];
            int c=0;
            for(Iterator iter=empHashMap.keySet().iterator();iter.hasNext();){
            emplidTemp = iter.next().toString();
            ret[c++]=(Employee)empHashMap.get(emplidTemp);
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

 /**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getToviazLaunchOverAllEmployees( String sql, String params[] ) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		
		try {
            HashMap empHashMap=new LinkedHashMap();
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/


			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			rs = st.executeQuery();
            String emplid="";
            Employee curr=null;
			while (rs.next()) {
                //Build a hashmap with Emplid as key and EmpBean as object
                emplid=rs.getString("EMPLID");
                if(empHashMap.containsKey(emplid)){
                    curr=(Employee)empHashMap.get(emplid);
                }else{
                    curr = new Employee();
                }
                // Set some of the items to empty..
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
                curr.setFirstName( rs.getString("firstName".toUpperCase()) );
                curr.setEmail(rs.getString("EMAIL") );
                curr.setFutureBU(rs.getString("bu"));
                curr.setFutureRBU(rs.getString("rbu"));
				curr.setEmplId( emplid );
                // Get the reports to email for the employee
                String email = "";
                if(rs.getString("REPORTSTOEMAIL") != null){
                    email = rs.getString("REPORTSTOEMAIL");
                    curr.setReportsToEmail(email);
                }
                empHashMap.put(emplid,curr);
			}
            String emplidTemp="";
            System.out.println("HashMap Size here is "+empHashMap.size());
            ret = new Employee[empHashMap.size()];
            int c=0;
            for(Iterator iter=empHashMap.keySet().iterator();iter.hasNext();){
            emplidTemp = iter.next().toString();
            ret[c++]=(Employee)empHashMap.get(emplidTemp);
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



    private String getExamStatus(String product, String emplid){
     String status = "";
     ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 

		try {

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/

        }
         catch (Exception e) {
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
     
     
     return status;   
        
    }  

     	/**
	 * Searches users by name, will filter against employees that are managed
	 * by the user.
	 */
	public EmpSearchPOA[] getPOAEmployeesById( String emplid ) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" where product_cd <> 'NA' and emplid = '" + emplid + "' ");

		String sql = searchPOAEmployeeSql + criteria.toString();

        //System.out.println("@@@@ SQL" + sql);

        log.info("Search SQL FOR THE EMPLOYEE DETAIL PAGE---"+ sql );
		return getPOAEmployeesSearch ( sql ) ;
	}


     	/**
	 * Searches users by name, will filter against employees that are managed
	 * by the user.
	 */
	public EmpSearchPDFHS[] getPDFHSEmployeesById( String emplid ) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" where product_cd <> 'NA' and emplid = '" + emplid + "' ");

		String sql = searchPDFHSEmployeeSql + criteria.toString();

        //System.out.println("@@@@ SQL" + sql);

        log.info("Search SQL FOR THE EMPLOYEE DETAIL PAGE---"+ sql );
		return getPDFHSEmployeesSearch ( sql ) ;
	}


	private EmpSearch[] getEmployeesSearch( String sql ) {
		EmpSearch[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {


			Timer timer = new Timer();

			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

			conn =   ds.getConnection();*/
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearch curr = new EmpSearch();
				curr.setEmplId( rs.getString("EMPLID") );
				curr.setLastName( rs.getString("lastName".toUpperCase()) );
				curr.setFirstName( rs.getString("firstName".toUpperCase()) );
				curr.setMiddleName( rs.getString("middleName".toUpperCase()) );
				curr.setPreferredName( rs.getString("preferredName".toUpperCase()) );
				curr.setProductCd( rs.getString("productCd".toUpperCase()) );
                curr.setFieldActive(rs.getString("fieldActive"));
                curr.setExam_issued_on(rs.getString("exam_issued_on"));
                curr.setTerritoryId(rs.getString("territoryId"));
                curr.setMaterialOrderDate(rs.getString("MATERIALDATEORDERED"));
                curr.setRoleCd(rs.getString("roleCD"));
                curr.setTrainingNeed(rs.getString("trainingNeed"));
				tempList.add( curr );
			}

			ret = new EmpSearch[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearch)tempList.get(j);
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

	public boolean isTrainingExempted(String emplid, String productCd) {

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		try {
			/*Context ctx = new InitialContext();*/

			Timer timer = new Timer();

			/*DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/

			String sql =
					" select " +
						"  training_required " +
					" from  " +
						" mv_fft_products_training " +
					 " where  " +
						" emplid = ? and product_cd = ? ";
			st = conn.prepareStatement(sql);
			st.setFetchSize(5000);
			st.setString(1,emplid);
			st.setString(2,productCd);

			rs = st.executeQuery();

			if ( rs.next() ) {
				String tmp = rs.getString("training_required".toUpperCase());
				if ("Exempted".equals(tmp)) {
					return true;
				}
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

		return false;


	}
    
    public String getReportsToEmail(String emplid) {

		ResultSet rs = null;
		PreparedStatement st = null;
		/*Connection conn = null;*/
		 /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */ 
		
        String email = "";
		try {
			/*Context ctx = new InitialContext();*/

			Timer timer = new Timer();

			/*DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();*/

			String sql =
					" select EMAIL_ADDRESS from V_RBU_LIVE_FEED where emplid = '"+emplid+"'";
			st = conn.prepareStatement(sql);
			st.setFetchSize(5000);
			rs = st.executeQuery();
			while ( rs.next() ) {
				email = rs.getString("EMAIL_ADDRESS");
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

		return email;


	}
    
    public Employee[] getPLCEmployees(UserFilter uFilter,String productCD,String section){
        return getPLCEmployees(uFilter,productCD,section,"V_PWRA_PLC_DATA");
    }
    public Employee[] getPLCEmployeesSPF(UserFilter uFilter,String productCD,String section){
        return getPLCEmployees(uFilter,productCD,section,"V_SPF_PLC_DATA");
    }

    private Employee[] getPLCEmployees(UserFilter uFilter,String productCD,String section,String tableName){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(productCD.equalsIgnoreCase("Geodon")) productCD="GEOD";
        if(productCD.equalsIgnoreCase("Aricept")) productCD="ARCP";
        if(productCD.equalsIgnoreCase("Lyrica")) productCD="LYRC";
        if(productCD.equalsIgnoreCase("Celebrex")) productCD="CLBR";
        if(productCD.equalsIgnoreCase("Rebif")) productCD="REBF";
        if(productCD.equalsIgnoreCase("General Session")) productCD="PLCA";
        if(productCD.equalsIgnoreCase("Detrol LA")) productCD="DETR";
        if(productCD.equalsIgnoreCase("Chantix")) productCD="CHTX";
        if(productCD.equalsIgnoreCase("Revatio")) productCD="RVTO";
        if(productCD.equalsIgnoreCase("Spiriva")) productCD="SPRV";
        if(productCD.equalsIgnoreCase("Viagra")) productCD="VIAG";

        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID, ";
        String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

        if(productCD.equalsIgnoreCase("PLCA")){
            if(section.equalsIgnoreCase("Not Complete")) {
                sqlQuery = "SELECT "+data+" 'Not Complete' coursestatus "+"FROM "+tableName+" WHERE STATUS = 'I' AND PRODUCT_CD='PLCA'"+condQuery+orderByQuery;
            }else if(section.equalsIgnoreCase("On Leave")) {
                sqlQuery = "SELECT "+data+" 'On Leave' coursestatus "+"FROM "+tableName+" WHERE STATUS = 'L' AND PRODUCT_CD='PLCA'"+condQuery+orderByQuery;
            }else if(section.equalsIgnoreCase("Complete")) {
                sqlQuery = "SELECT "+data+" 'Complete' coursestatus "+"FROM "+tableName+" WHERE STATUS = 'P' AND PRODUCT_CD='PLCA'"+condQuery+orderByQuery;
            }
        }else{
            if(section.equalsIgnoreCase("Not Complete")) {
                //thisSection="I";
                //courseStatus="Not Complete";
                String sqlInCompleteQueryFirst="SELECT  DISTRICTDESC, TEAMCODE, LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID,  'Not Complete' coursestatus FROM ";
                String sqlInCompleteQueryI=" SELECT "+data+" 'Not Complete' coursestatus "+" FROM  "+tableName+" WHERE STATUS='I' AND PRODUCT_CD='"+productCD+"' ";
                String sqlInCompleteQueryMINUS=" MINUS ";
                String sqlInCompleteQueryL=" SELECT "+data+" 'Not Complete' coursestatus "+" FROM "+tableName+" WHERE STATUS='L'AND PRODUCT_CD='"+productCD+"' ";
                sqlInCompleteQueryI=sqlInCompleteQueryI+condQuery+groupBy;
                sqlInCompleteQueryL=sqlInCompleteQueryL+condQuery+groupBy;
                String sqlInCompleteQuery = sqlInCompleteQueryFirst+"("+sqlInCompleteQueryI+sqlInCompleteQueryMINUS+sqlInCompleteQueryL+")"+" ORDER BY LASTNAME, FIRSTNAME";
                sqlQuery = sqlInCompleteQuery;

            }else if(section.equalsIgnoreCase("On Leave")) {
                //thisSection="L";
                //courseStatus="On Leave";
                String sqlQueryOnLeave="SELECT "+data+" 'On Leave' coursestatus "+" FROM "+tableName+" WHERE STATUS='L'  AND PRODUCT_CD='"+productCD+"' ";
                sqlQueryOnLeave = sqlQueryOnLeave+condQuery;
                sqlQuery = sqlQueryOnLeave+groupBy+orderByQuery;

            }else if(section.equalsIgnoreCase("Complete")) {
                //thisSection="P";
                //courseStatus="Completed";
                String sqlCompleteQueryMain="SELECT  DISTRICTDESC, TEAMCODE, LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'Complete' coursestatus "+" FROM "  ;
                String sqlCompleteQueryPED=" SELECT "+data+" 'Complete' coursestatus "+" FROM "+tableName+" WHERE STATUS = 'P' AND EXAM_TYPE='PED' AND PRODUCT_CD='"+productCD+"' ";
                String sqlCompleteQuerySCE=" SELECT "+data+" 'Complete' coursestatus "+" FROM "+tableName+" WHERE STATUS = 'P' AND EXAM_TYPE='SCE' AND PRODUCT_CD='"+productCD+"' ";
                String sqlCompleteQueryINTERSECT=" INTERSECT ";
                sqlCompleteQueryPED = sqlCompleteQueryPED+condQuery+groupBy;
                sqlCompleteQuerySCE = sqlCompleteQuerySCE+condQuery+groupBy;
                String sqlCompleteQuery = sqlCompleteQueryMain+"("+sqlCompleteQueryPED+sqlCompleteQueryINTERSECT+sqlCompleteQuerySCE+")";
                sqlQuery = sqlCompleteQuery+" ORDER BY LASTNAME, FIRSTNAME";
            }
        }
        return getPOAEmployees ( sqlQuery,params ) ;
    }
	public Employee[] getPLCOverAllEmployees(UserFilter uFilter,String productCD,String section){
        return getPLCOverAllEmployees(uFilter,productCD,section,"V_PWRA_PLC_DATA_OVERALL");
    }
    public Employee[] getPLCOverAllEmployeesSPF(UserFilter uFilter,String productCD,String section){
        return getPLCOverAllEmployees(uFilter,productCD,section,"V_SPF_PLC_DATA_OVERALL");
    }

    private Employee[] getPLCOverAllEmployees(UserFilter uFilter,String productCD,String section,String tableName){
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="I";
            //courseStatus="Not Complete";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
            //courseStatus="On Leave";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            thisSection="P";
            //courseStatus="Completed";
        }
        String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status "
                    +" FROM "+tableName
                    +" WHERE OVERALL_STATUS='"+thisSection+"' "
                    +" AND PRODUCT_CD NOT IN ('NA') ";
             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(form.getArea())){
             condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getTeam())){
             condQuery=condQuery+ " AND TEAM_CD='"+form.getTeam()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
            }
            String orderQuery=" order by LASTNAME,FIRSTNAME ";
            sql=sql+condQuery+orderQuery;

          return getPOAOverAllEmployees(sql,params ) ;
    }


    public Employee[] getGNSMOverAllEmployees(UserFilter uFilter,String productCD,String section){
        String tableName = "V_GNSM_DATA_OVERALL";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="I";
        }
        else if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
        }
        else if(section.equalsIgnoreCase("Complete")) {
            thisSection="P";
        }
        String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,TRT_COURSE_CODE PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
                    +" FROM "+tableName
                    +" WHERE OVERALL_STATUS='"+thisSection+"' ";

             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(form.getArea())){
             condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getTeam())){
             condQuery=condQuery+ " AND TEAM_CD='"+form.getTeam()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
            }
            String orderQuery=" order by LASTNAME,FIRSTNAME ";
            sql=sql+condQuery+orderQuery;

          return getPOAOverAllEmployees (sql,params) ;
    }

    public Employee[] getGNSMEmployees(UserFilter uFilter,String productCD,String section){
        String tableName = "V_GNSM_DATA";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(productCD.equalsIgnoreCase("Geodon Exam 1")) productCD="GEODE1";
        if(productCD.equalsIgnoreCase("Geodon Exam 2")) productCD="GEODE2";
        if(productCD.equalsIgnoreCase("Geodon Learning System Survey")) productCD="GEODSY";
        if(productCD.equalsIgnoreCase("Video Link")) productCD="GEODVL";

        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
        //String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

        if(section.equalsIgnoreCase("Not Complete")) {
            sqlQuery = "SELECT "+data +" , 'Not Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("On Leave")) {
            sqlQuery = "SELECT "+data +", 'On Leave' coursestatus FROM "+tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("Complete")) {
            sqlQuery = "SELECT "+data +", 'Complete' coursestatus FROM "+tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }
        sqlQuery = sqlQuery+orderByQuery;
        return getPOAEmployees ( sqlQuery,params ) ;
    }


    /* Method added for Vista Rx Spiriva enhancement
    * Author: Meenakshi
    * Data: 12-Sep-2008
    */
     public Employee[] getVRSOverAllEmployees(UserFilter uFilter,String productCD,String section){
        String tableName = "V_SPIRIVA_DATA_OVERALL";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(section.equalsIgnoreCase("Not Complete")) {
            thisSection="I";
        }
        else if(section.equalsIgnoreCase("On Leave")) {
            thisSection="L";
        }
        else if(section.equalsIgnoreCase("Complete")) {
            thisSection="P";
        }
        String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,TRT_COURSE_CODE PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
                    +" FROM "+tableName
                    +" WHERE OVERALL_STATUS='"+thisSection+"' ";

             //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(form.getArea())){
             condQuery=condQuery+ " AND AREA_CD='"+form.getArea()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getTeam())){
             condQuery=condQuery+ " AND TEAM_CD='"+form.getTeam()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ " AND REGION_CD='"+form.getRegion()+"' ";
            }
            if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ " AND DISTRICT_ID='"+form.getDistrict()+"' ";
            }
            String orderQuery=" order by LASTNAME,FIRSTNAME ";
            sql=sql+condQuery+orderQuery;

          return getPOAOverAllEmployees (sql,params) ;
    }

    public Employee[] getVRSEmployees(UserFilter uFilter,String productCD,String section){
        String tableName = "V_VISTASPIRIVA_DATA";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        if(productCD.equalsIgnoreCase("Spiriva Exam 1")) productCD="VISTASPRV1";
        if(productCD.equalsIgnoreCase("Spiriva Exam 2")) productCD="VISTASPRV2";


        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
        //String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

        if(section.equalsIgnoreCase("Not Complete")) {
            sqlQuery = "SELECT "+data +" , 'Not Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("On Leave")) {
            sqlQuery = "SELECT "+data +", 'On Leave' coursestatus FROM "+tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("Complete")) {
            sqlQuery = "SELECT "+data +", 'Complete' coursestatus FROM "+tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }
        sqlQuery = sqlQuery+orderByQuery;
        return getPOAEmployees ( sqlQuery,params ) ;
    }


    public EmpSearchGNSM[] getVRSEmployees( EmplSearchForm eform, UserSession uSession) {

        EmpSearchGNSM[] ret  = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
        sql = sql+" ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
        sql = sql+" ,DECODE(OVERALL_STATUS,'I','Registered','P','Complete','L','On-Leave')  OVERALL_STATUS ";
        sql = sql+" FROM V_SPIRIVA_DATA_OVERALL e ";

        StringBuffer criteria = new StringBuffer();

        TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

        boolean firstWhere = true;

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
            firstWhere = false;
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
            firstWhere = false;
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
	 		criteria.append(" and e.district_id = '" + form.getDistrict() + "' ");
            firstWhere = false;
		}

		if(!Util.isEmpty(eform.getFname())||!Util.isEmpty( eform.getLname())){
            if ( !Util.isEmpty( eform.getFname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " (upper(e.first_name) like '" +  eform.getFname().toUpperCase().trim() + "%'  ) " );
            }
            if ( !Util.isEmpty( eform.getLname() ) ) {
                if(firstWhere){
                    criteria.append(" where ");
                    firstWhere = false;
                }else{
                    criteria.append(" and ");
                }
                criteria.append( " upper(e.last_name) like '" + eform.getLname().toUpperCase().trim()  + "%' " );
            }
        }else if(!Util.isEmpty(eform.getEmplid())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.emplid='"+eform.getEmplid().trim()+"'");
        }else if(!Util.isEmpty(eform.getTerrId())){
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
            criteria.append(" e.territory_id='"+eform.getTerrId().trim()+"'");
        }

		if ( !uSession.getUser().isAdmin() ) {
            if(firstWhere){
                criteria.append(" where ");
                firstWhere = false;
            }else{
                criteria.append(" and ");
            }
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster() + "'");
		}
		criteria.append( " order by e.emplid,TRT_COURSE_NAME " );

        sql = sql+criteria;

        log.info("Search SQL FOR THE EMPLOYEE---"+ sql );

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

			List tempList = new ArrayList();

			//log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
                EmpSearchGNSM e = new EmpSearchGNSM();
                e.setTeamCode(rs.getString("TEAM_DESC"));
                e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
                e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
                e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
                e.setRole(rs.getString("ROLE_CD"));
                e.setEmplId(rs.getString("EMPLID"));
                e.setActivityName(rs.getString("TRT_COURSE_NAME"));
                e.setActivityStatus(rs.getString("STATUS"));
                e.setOverallStatus(rs.getString("OVERALL_STATUS"));
                tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (EmpSearchGNSM)tempList.get(j);
			}

		} catch (Exception e) {
            e.printStackTrace();
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


/* End of addition */


    public Employee[] getMSEPIEmployees(UserFilter uFilter,String productCD,String section){
        String tableName = "V_MSEPI_NSM_DATA";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        productCD="MSEPIATT";

        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LAST_NAME,FIRST_NAME ";

        if(section.equalsIgnoreCase("Not Complete")) {
            sqlQuery = "SELECT "+data +" , 'Not Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("On Leave")) {
            sqlQuery = "SELECT "+data +", 'On Leave' coursestatus FROM "+tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }else if(section.equalsIgnoreCase("Complete")) {
            sqlQuery = "SELECT "+data +", 'Complete' coursestatus FROM "+tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"+productCD+"' " +condQuery;
        }
        sqlQuery = sqlQuery+orderByQuery;
        return getPOAEmployees ( sqlQuery,params ) ;
    }



    public Employee[] getGNSMEmployeesAttendance(UserFilter uFilter,String productCD,String section){
        String tableName = "";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
        //String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LASTNAME,FIRSTNAME ";

        if(section.equalsIgnoreCase("Not Complete")) {
            //tableName = " V_GNSM_ATTENDANCE_DATA ";
            //sqlQuery = "SELECT "+data +" , 'Not Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'I' " +condQuery;
            tableName = " ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'I' "+ condQuery;
            tableName = tableName+" MINUS (  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'P' "+ condQuery;
            tableName = tableName+" UNION  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' "+ condQuery+" ) ) ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Not Complete' coursestatus FROM "+tableName;
        }else if(section.equalsIgnoreCase("On Leave")) {
            tableName = "  ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' "+ condQuery;
            tableName = tableName+" MINUS  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'P' "+condQuery+") ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'On Leave' coursestatus FROM "+tableName;
        }else if(section.equalsIgnoreCase("Complete")) {
            tableName = " V_GNSM_ATTENDANCE_DATA ";
            sqlQuery = "SELECT "+data +" , 'Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'P' " +condQuery;
            /*
            tableName = " ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'P' ";
            tableName = tableName+" MINUS (  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' ";
            tableName = tableName+" UNION  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' ) ) ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Complete' coursestatus FROM "+tableName + condQuery;
            */
        }
        sqlQuery = sqlQuery+orderByQuery;
        return getPOAEmployees ( sqlQuery,params ) ;
    }

    /* Added for the Vista Rx Spiriva enhancement
     * Auhor: Meenakshi
     * Date:13-Sep-2008
    */


    public Employee[] getVRSEmployeesAttendance(UserFilter uFilter,String productCD,String section){
        String tableName = "";
        TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
        String thisSection="";
        String[] params = new String[0];
        String courseStatus="";
        String sqlQuery = "";
        String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
        //String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
        //ConditionQuery
        String condQuery="";
        if(!"ALL".equalsIgnoreCase(form.getArea())){
            condQuery=condQuery+ "AND AREA_CD='"+form.getArea()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            condQuery=condQuery+ "AND TEAM_CD='"+form.getTeam()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getRegion())){
             condQuery=condQuery+ "AND REGION_CD='"+form.getRegion()+"' ";
        }
        if(!"ALL".equalsIgnoreCase(form.getDistrict())){
             condQuery=condQuery+ "AND DISTRICT_ID='"+form.getDistrict()+"' ";
        }
        String orderByQuery=" ORDER BY LASTNAME,FIRSTNAME ";

        if(section.equalsIgnoreCase("Not Complete")) {
            //tableName = " V_GNSM_ATTENDANCE_DATA ";
            //sqlQuery = "SELECT "+data +" , 'Not Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'I' " +condQuery;
            tableName = " ( SELECT "+data+" FROM V_SPIRIVA_ATTENDANCE_DATA WHERE STATUS = 'I' "+ condQuery;
            tableName = tableName+" MINUS (  SELECT "+data+" FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' "+ condQuery;
            tableName = tableName+" UNION  SELECT "+data+" FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'L' "+ condQuery+" ) ) ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Not Complete' coursestatus FROM "+tableName;
        }else if(section.equalsIgnoreCase("On Leave")) {
            tableName = "  ( SELECT "+data+" FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'L' "+ condQuery;
            tableName = tableName+" MINUS  SELECT "+data+" FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' "+condQuery+") ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'On Leave' coursestatus FROM "+tableName;
        }else if(section.equalsIgnoreCase("Complete")) {
            tableName = " V_SPIRIVA_ATTENDANCE_DATA ";
            sqlQuery = "SELECT "+data +" , 'Complete' coursestatus  FROM "+ tableName + " WHERE STATUS = 'P' " +condQuery;
            /*
            tableName = " ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'P' ";
            tableName = tableName+" MINUS (  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' ";
            tableName = tableName+" UNION  SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' ) ) ";
            sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Complete' coursestatus FROM "+tableName + condQuery;
            */
        }
        sqlQuery = sqlQuery+orderByQuery;
        return getPOAEmployees ( sqlQuery,params ) ;
    }

    /* End of addition */

    public static Employee[] convertRecords(ResultSet rs) throws Exception {
        Employee[] ret = null;
        List tempList = new ArrayList();
        ResultSetMetaData rsmd = rs.getMetaData();

        int ncols = rsmd.getColumnCount();


        HashMap fields = new HashMap();

        for (int i=1; i<=ncols; i++) {
            String key = rsmd.getColumnName(i);
            fields.put(key,"yes");
        }
        while (rs.next()) {
            Employee curr = new Employee();
            curr.setEmplId( rs.getString("EMPLID") );

            if ( fields.containsKey("GUID") )
                curr.setGuid( ( rs.getString("guid").toUpperCase() ) );

            if ( fields.containsKey("GEOTYPE") )
                curr.setGeoType( ( rs.getString("GEOTYPE").toUpperCase() ) );

            if ( fields.containsKey( "fieldactive".toUpperCase() ))
                curr.setEmployeeStatus(rs.getString("fieldactive"));



            if ( fields.containsKey("areaCd".toUpperCase() ) )
                curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );

            if ( fields.containsKey( "areaDesc".toUpperCase() ))
                curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );

            if ( fields.containsKey( "regionCd".toUpperCase() ))
                curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );

            if ( fields.containsKey( "regionDesc".toUpperCase() ))
                curr.setRegionDesc( rs.getString("regionDesc".toUpperCase()) );

            if ( fields.containsKey( "districtId".toUpperCase() ))
                curr.setDistrictId( rs.getString("districtId".toUpperCase()) );

            if ( fields.containsKey( "districtDesc".toUpperCase() ))
                curr.setDistrictDesc( rs.getString("districtDesc".toUpperCase()) );
            if ( fields.containsKey( "territoryId".toUpperCase() ))
                curr.setTerritoryId( rs.getString("territoryId".toUpperCase()) );
            if ( fields.containsKey( "role".toUpperCase() ))
                curr.setRole( rs.getString("role".toUpperCase()) );
            if ( fields.containsKey( "teamCode".toUpperCase() ))
              curr.setTeamCode( rs.getString("teamCode".toUpperCase()) );
            if ( fields.containsKey( "clusterCode".toUpperCase() ))
                curr.setClusterCode( rs.getString("clusterCode".toUpperCase()) );
            if ( fields.containsKey( "lastName".toUpperCase() ))
                curr.setLastName( rs.getString("lastName".toUpperCase()) );
            if ( fields.containsKey( "firstName".toUpperCase() ))
                curr.setFirstName( rs.getString("firstName".toUpperCase()) );
            if ( fields.containsKey( "middleName".toUpperCase() ))
                curr.setMiddleName( rs.getString("middleName".toUpperCase()) );
            if ( fields.containsKey( "preferredName".toUpperCase() ))
                curr.setPreferredName( rs.getString("preferredName".toUpperCase()) );
            if ( fields.containsKey( "gender".toUpperCase() ))
                curr.setGender( rs.getString("gender".toUpperCase()) );
            if ( fields.containsKey( "email".toUpperCase() ))
                curr.setEmail( rs.getString("email".toUpperCase()) );

            if ( fields.containsKey( "reportsToEmplid".toUpperCase() ))
                curr.setReportsToEmplid( rs.getString("reportsToEmplid".toUpperCase()) );



            if ( fields.containsKey( "hireDate".toUpperCase() ))   {
                Date hireDate = rs.getDate("hireDate".toUpperCase());
                if ( hireDate != null ) {
                    curr.setHireDate( new Date( hireDate.getTime() )  );
                }
            }
            if ( fields.containsKey( "hireDate".toUpperCase() ))   {
                Date promoDate = rs.getDate("promoDate".toUpperCase());
                if ( promoDate != null )
                    curr.setPromoDate( new Date( promoDate.getTime() )  );
            }



            tempList.add( curr );
        }

        ret = new Employee[tempList.size()];

        for ( int j=0; j < tempList.size(); j++ ) {
            ret[j] = (Employee)tempList.get(j);
        }

        return ret;
    }

	public Employee[] getEmployees(TerritoryFilterForm form, UserFilter uFilter, String additionalCriteriaSql, boolean isdetail ) {
		Employee[] ret = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[0];
        criteria.append( " e.emplid = e.emplid ");
        if(!"ALL".equalsIgnoreCase(form.getTeam())){
            criteria.append(" AND e.new_team_cd='"+form.getTeam()+"' ");
        }

		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			/*if (uFilter.isAdmin()) {
				params = new String[0];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[1];
				params[1] = uFilter.getClusterCode();
			}*/
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[1];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[2];
			//	params[1] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[2];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[3];
			//	params[2] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
			params[1] = form.getRegion();
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
	 		criteria.append(" and e.district_id = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[3];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[4];
			//	params[3] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
			params[1] = form.getRegion();
			params[2] = form.getDistrict();
		}

		String sql = "";
        if ( isdetail ) {
            sql = employeSql + criteria.toString() + additionalCriteriaSql;
        } else {
            sql = basicemployeSql + criteria.toString() + additionalCriteriaSql;
        }
		//System.out.println(sql);
		//log.info(sql);
		return getEmployees ( sql,params ) ;
	}

}

