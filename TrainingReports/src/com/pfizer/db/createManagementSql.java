package com.pfizer.db;

import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.utils.DBUtil;
import com.pfizer.webapp.AppConst;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createManagementSql {
	
	private StringBuffer selectClause1;
	private String fromClause1;
	private StringBuffer selectClause2;
	private StringBuffer fromClause2;
	private StringBuffer whereClause;
	private String completionFilter = "";
	private String registrationFilter = "";
	private String groupByClause1;
	private String groupByClause2;
	private StringBuffer orderByClause;
	private String sql = "";
    boolean admin;
    private String emplID;

	// String
	ManagementSummaryReport mReport = new ManagementSummaryReport();
	

	public createManagementSql(ManagementSummaryReport track,boolean admin, String emplID) {
		this.mReport = track;
        this.admin = admin;
        this.emplID = emplID;
        System.out.println(admin+""+emplID);
	}

	public String createMgtSql() {

		ArrayList salesOrgList = new ArrayList();
		List ActivityIDList = mReport.getActivityIdList();//new ArrayList();
        System.out.println(ActivityIDList+"ActivityIdList");
        List CourseCodeList = mReport.getcourseCodeList();
  //      System.out.println("CourseCodeList beginning ==="+CourseCodeList.toString());
		//ArrayList CourseCodeList = new ArrayList();
		String salesOrg = mReport.getSalesOrg();
/*		String[] soArr1 = salesOrg.split(",");
		for (int j = 0; j < soArr1.length; j++) {
			salesOrgList.add(soArr1[j]);
			// System.out.println("salesOrgList=="+salesOrgList);
		}*/
        List groupBy = mReport.getGroupByList();
        
        HashMap columns = new HashMap();
        columns.put("sales_group","SALESORGANIZATION");
        columns.put("bu","BUSINESSUNIT");
        columns.put("role_cd","ROLECODE");
        columns.put("sex","GENDER");
        columns.put("activityfk","activityFk");
        columns.put("status","Status");
        columns.put("source","Source");
        
        String temp = "";
	//	selectClause1 = new StringBuffer(" SELECT DISTINCT ";// + 
             if(!groupBy.get(0).equals("")){
                if (temp.length() > 0) temp = temp + " ,";
                temp =  temp + groupBy.get(0) + " AS " + columns.get(groupBy.get(0));
             }
             if(!groupBy.get(1).equals("")){
                 if(temp.length() > 0) temp = temp + " ,";
                 temp =  temp +  groupBy.get(1) + " AS " + columns.get(groupBy.get(1)); 
             }
             if(!groupBy.get(2).equals("")){
                 if(temp.length() > 0) temp = temp + " ,";
                temp =  temp + groupBy.get(2) + " AS " + columns.get(groupBy.get(2));
             }
             if(!groupBy.get(3).equals("")){
                 if(temp.length() > 0) temp = temp + " ,";
				temp =  temp + groupBy.get(3) + " AS " + columns.get(groupBy.get(3));// + " ," + 
             }   
             if(!groupBy.get(4).equals("")){
                if(temp.length() > 0) temp = temp + " ,";
                temp =  temp + groupBy.get(4) + " AS " + columns.get(groupBy.get(4));
             }
             if(!groupBy.get(5).equals("")){
                if(temp.length() > 0) temp = temp + " ,";
                temp =  temp + groupBy.get(5) + " AS " + columns.get(groupBy.get(5));
             }
          selectClause1 = new StringBuffer(" SELECT DISTINCT ");
          selectClause1.append(temp);   
            
        System.out.println(" Select Clause : "+selectClause1);
		
        fromClause1 = " FROM (";

		selectClause2 = new StringBuffer(
				" SELECT DISTINCT  b.sex AS sex,b.sales_group AS sales_group "
					//	+ ",b.bu AS bu, b.role_cd AS role_cd, b.sales_position_type_cd AS source "
                        + ",b.bu AS bu, b.role_cd AS role_cd "
                        + ",DECODE (b.is_hq_user, '0', 'Field Force','1', 'HQ') AS SOURCE"
						+ ",DECODE (c.status,'C', 'Completed','R', 'Registered') AS status ");

		if (ActivityIDList != null) {
			for (int i = 0; i < ActivityIDList.size(); i++) {
                Map mapCourse = (Map)CourseCodeList.get(i); 
                Map mapActivityId = (Map)ActivityIDList.get(i);
               // System.out.println("filter get ==="+CourseCodeList.get(i));
                System.out.println("filter descr ==="+(String)mapCourse.get("FILTER_DESCRIPTION"));
				System.out.println("filter code ==="+(String)mapActivityId.get("FILTER_CODE"));
                String filterDesc = (String)mapCourse.get("FILTER_DESCRIPTION");
                if(filterDesc.length() > 30 && filterDesc!=null){
                    filterDesc = filterDesc.substring(0,30);
                    System.out.println(filterDesc.substring(0,30));
                }
                selectClause1.append(",SUM(Activity" + i + ") AS "
						//+ CourseCodeList.get(i));
                //       +"\"" +(String)mapCourse.get("FILTER_DESCRIPTION")+"\"");
                         +"\"" + filterDesc +"\"");
				selectClause2.append(",(CASE WHEN activityfk = "
						//+ ActivityIDList.get(i)
                        +(String)mapActivityId.get("FILTER_CODE")
						+ " THEN COUNT (c.activityfk) ELSE 0 END) AS Activity"
						+ i);
			}
		}
       if(admin){ 
		fromClause2 = new StringBuffer(
				" FROM mv_field_employee_rbu b,mv_usp_compl_and_reg c ");
		whereClause = new StringBuffer(" WHERE c.emp_no = b.guid ");
       }
       else{
       fromClause2 = new StringBuffer(
				" FROM (select emplid from mv_field_employee_rbu start with emplid = " + emplID
					+ " connect by prior emplid = reports_to_emplid) r, mv_field_employee_rbu b, mv_usp_compl_and_reg c ");
		whereClause = new StringBuffer(" WHERE c.emp_no = b.guid and b.emplid = r.emplid ");
       }
		if (mReport.getHireStartDate() != null
				&& mReport.getHireEndDate() != null) {
			whereClause.append(" AND b.hire_date BETWEEN TO_DATE ('"
					+ mReport.getHireStartDate()
					+ "','mm/dd/yyyy') AND TO_DATE ('"
					+ mReport.getHireEndDate() + "','mm/dd/yyyy') ");
		}

		if (mReport.getSalesOrg()!=null && mReport.getSalesOrg().length() > 0) {
			fromClause2
					.append(",(SELECT filter_code FROM management_code_desc "
							+ "WHERE filter_id = '" + mReport.getTrackId()
							+ "' " + "AND filter_type = 'sales_group') so ");
			whereClause.append(" AND b.group_cd = so.filter_code ");
		}

		if (mReport.getBusinessUnit()!=null && mReport.getBusinessUnit().length() > 0) {
			fromClause2
					.append(",(SELECT filter_code FROM management_code_desc "
							+ "WHERE filter_id = '" + mReport.getTrackId()
							+ "' " + "AND filter_type = 'bu') bu ");
			whereClause.append(" AND b.bu = bu.filter_code ");
		}
		if (mReport.getRoleCode()!=null && mReport.getRoleCode().length() > 0) {
			fromClause2
					.append(",(SELECT filter_code FROM management_code_desc "
							+ "WHERE filter_id = '" + mReport.getTrackId()
							+ "' " + "AND filter_type = 'role_desc') rc ");
			whereClause.append(" AND b.role_cd = rc.filter_code ");
		}
		if (mReport.getCourseCode()!=null && mReport.getCourseCode().length() > 0) {
			fromClause2
					.append(",(SELECT filter_code FROM management_code_desc "
							+ "WHERE filter_id = '" + mReport.getTrackId()
							+ "' " + "AND filter_type = 'activityfk') cc ");
			whereClause.append(" AND c.activityfk = cc.filter_code ");
		}
        if(mReport.getGender() !=null && mReport.getGender().length() > 0){
            fromClause2
					.append(",(SELECT filter_code FROM management_code_desc "
							+ "WHERE filter_id = '" + mReport.getTrackId()
							+ "' " + "AND filter_type = 'sex') gender ");
			whereClause.append(" AND b.sex = gender.filter_code ");
        }
		if (mReport.getTrainingCompletionStartdate() != null
				&& mReport.getTrainingCompletionEndDate() != null) {
			completionFilter = " (c.status_date BETWEEN TO_DATE ('"
					+ mReport.getTrainingCompletionStartdate()
					+ "','mm/dd/yyyy')  AND TO_DATE ('"
					+ mReport.getTrainingCompletionEndDate()
					+ "','mm/dd/yyyy') AND c.Status = 'C') ";
		}

		if (mReport.getTrainingRegistrationStartDate() != null
				&& mReport.getTrainingRegistrationStartDate() != null) {
			registrationFilter = " (c.status_date BETWEEN TO_DATE ('"
					+ mReport.getTrainingRegistrationStartDate()
					+ "','mm/dd/yyyy')  AND TO_DATE ('"
					+ mReport.getTrainingRegistrationEndDate()
					+ "','mm/dd/yyyy') AND c.Status = 'R') ";
		}

		if (completionFilter.length() > 0 && registrationFilter.length() > 0) {
			whereClause.append(" AND ( " + completionFilter + " OR "
					+ registrationFilter + " )");
		} else {
			if (completionFilter.length() > 0) {
				whereClause.append(" AND " + completionFilter);
			} else if (registrationFilter.length() > 0) {
				whereClause.append(" AND " + registrationFilter);
			}
		}

		groupByClause1 = " GROUP BY sales_group,bu,role_cd,sex,activityfk,sales_position_type_cd,is_hq_user,status ) ";
	//	groupByClause2 = " GROUP BY status, role_cd, bu, sex, sales_group ";
/*		orderByClause = new StringBuffer(" ORDER BY " + groupBy.get(0) + " ,"
				+ groupBy.get(1) + " ," + groupBy.get(2) + " ,"
				+ groupBy.get(3)  + " ASC"); //+ " ," + groupBy.get(4)*/
		orderByClause = new StringBuffer(" Order by ");//salesorganization, businessunit, gender, role_cd ASC");
        String tempOrder="";
        if(!groupBy.get(0).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(0);
        }
        if(!groupBy.get(1).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(1);
        }
        if(!groupBy.get(2).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(2);
        }
        if(!groupBy.get(3).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(3);
        }
        if(!groupBy.get(4).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(4);
        }
        if(!groupBy.get(5).equals("")){
                if (tempOrder.length() > 0) tempOrder = tempOrder + " ,";
                tempOrder = tempOrder + groupBy.get(5);
        }
        groupByClause2 = " GROUP BY "+ tempOrder;
        orderByClause.append(tempOrder);
        System.out.println(" orderBy clause=="+orderByClause);
		sql = selectClause1 + fromClause1 + selectClause2 + fromClause2
				+ whereClause + groupByClause1 + groupByClause2 + orderByClause;
      //  System.out.println("sql=="+sql);
		return sql;
	}
    

	public List executeSql2(String sql) {
		List result = DBUtil.executeSql(sql, AppConst.APP_DATASOURCE);
		return result;
	}

}
