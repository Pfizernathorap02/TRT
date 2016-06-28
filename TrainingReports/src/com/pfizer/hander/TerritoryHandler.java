package com.pfizer.hander;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.SalesPosition;
import com.pfizer.db.Territory;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.user.Area;
import com.pfizer.webapp.user.District;
import com.pfizer.webapp.user.Region;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.LabelValueBean;

public class TerritoryHandler {
	protected static final Log log = LogFactory.getLog(TerritoryHandler.class);

	/* Variables added for RBU changes */
	ArrayList completeSalesPositionList = new ArrayList();
	HashMap salesPositionHashMap = new HashMap();
	HashMap salesPositionDescIdHashMap = new HashMap();
	HashMap salesPositionIdDescHashMap = new HashMap();
	ArrayList salesGroupList = new ArrayList();
	int salesPositionHierarchyLevel;
	ArrayList firstDropdown = new ArrayList();

	public TerritoryHandler() {
	}

	/* Method added for RBU changes */
	public UserTerritory getUserSalesPosition(String reportsToEmplId,
			String reportsToSalesPosId) {
		ReadProperties props = new ReadProperties();
		StringBuffer criteria = new StringBuffer();
		String[] paramsSalesPos = new String[1];
		String[] paramsSal = new String[1];
		// criteria.append(" emplid = ? ");
		System.out.print(reportsToEmplId);
		System.out.print(reportsToSalesPosId);
		// paramsGeo[0] = reportsToEmplId;
		paramsSalesPos[0] = reportsToSalesPosId;
		paramsSal[0] = reportsToEmplId;
		String sqlSalesPos = props.getValue("SalesPositionSql");
		String sqlSal = props.getValue("SalesGroupSql");
		System.out.print(paramsSalesPos[0]);
		getSalesPosition(sqlSalesPos, paramsSalesPos);
		getSalesGroup(sqlSal, paramsSal);
		return userSalesPositionHirearchy();
	}

	/* Added method for RBU changes */
	public UserTerritory getAdminSalesPosition() {
		ReadProperties props = new ReadProperties();
		StringBuffer criteria = new StringBuffer();
		String sqlSal = props.getValue("AdminSalesGroupSql");
		String adminSalesPosSql = props.getValue("AdminSalesPositionSql");
		getAdminChildSalesPosition(adminSalesPosSql);
		return userSalesPositionHirearchy();
		// getAdminSalesGroup(sqlSal,paramsSal);
	}

	private void getAdminChildSalesPosition(String sql) {
		SalesPosition[] ret = null;

		ResultSet result = null;
		Statement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);
			// System.out.println("QUERY IS"+sql);
			// System.out.println("QUERY IS"+sql);
			result = st.executeQuery(sql);

			ArrayList salesPosDataRowList = null;
			HashMap salesPosDescIdHashMap = null;
			HashMap salesPosIdDescHashMap = null;
			// parameters to hold ResultSet values
			String parentSalesPosId = null;
			String childSalesPosId = null;
			String childSalesPosDesc = null;
			String salesPosLevel = null;
			ArrayList firstSalesPosDropdown = null;
			int maxLevel = 0;
			// int parentnode=1;

			// to hold the arrayList of Geography Objects
			salesPosDataRowList = new ArrayList();
			salesPosDescIdHashMap = new HashMap();
			salesPosIdDescHashMap = new HashMap();
			// ArrayList to hold the first level of Geography Drop down
			firstSalesPosDropdown = new ArrayList();
			// result set processing

			while (result.next()) {
				// Integer parentNode=new Integer(parentnode);
				parentSalesPosId = result
						.getString("REPORTS_TO_SALES_POSITION_ID");
				childSalesPosId = result.getString("SALES_POSITION_ID");
				childSalesPosDesc = result
						.getString("SALES_POSITIONID_ID_DESC");
				// childGeographyType =result.getString("Geo_Type");
				salesPosLevel = result.getString("Sales_Level");
				// System.out.println("child: "+childGeographyId);
				// get the max Level for drop down creation
				int temp = result.getInt("Sales_Level");
				// ArrayList to hold the first level of Geography Drop down
				if (temp == 1) {
					LabelValueBean labelValueBean;
					labelValueBean = new LabelValueBean(childSalesPosDesc,
							childSalesPosId);
					firstSalesPosDropdown.add(labelValueBean);
				}
				if (temp > maxLevel) {
					maxLevel = temp;
				}
				// populating the Geography Object
				SalesPosition salesPosNode = new SalesPosition();

				salesPosNode.setReportToSalesPosId(parentSalesPosId);
				salesPosNode.setChildSalesPosId(childSalesPosId);
				salesPosNode.setChildSalesPosDesc(childSalesPosDesc);
				// geoNode.setChildGeographyType(childGeographyType);
				salesPosNode.setSalesLevel(salesPosLevel);
				// arrayList of GeographyObjects
				if (temp != 1) {
					salesPosDataRowList.add(salesPosNode);
				}
				// create HashMap for Geography Id and Description
				salesPosDescIdHashMap.put(childSalesPosDesc, childSalesPosId);
				// create HashMap for Geography Id and Description
				salesPosIdDescHashMap.put(childSalesPosId, childSalesPosDesc);
			}
			// populate the class variables with Geography details
			completeSalesPositionList = null;
			completeSalesPositionList = salesPosDataRowList;
			salesPositionHierarchyLevel = maxLevel;
			System.out.println("Territory Handler:No of Levels"
					+ salesPositionHierarchyLevel);
			salesPositionDescIdHashMap = salesPosDescIdHashMap;
			salesPosIdDescHashMap = salesPosIdDescHashMap;
			this.firstDropdown = firstSalesPosDropdown;

		} catch (Exception e) {
			log.error(e, e);
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
	}

	public UserTerritory getUtEmployeeId(String employeeId) {
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[1];
		criteria.append(" emplid = ? ");
		params[0] = employeeId;
		String sql = territorySql + criteria.toString();
		Territory[] terr = getTerritory(sql, params);// db.getTerritoryByEmpId(
														// employeeId );
		return userTerritoryByArray(terr);
	}

	public UserTerritory getUtByAreReg(String area, String region,
			String cluster) {
		Territory[] terr;// = db.getTerritoryByAreReg( area, region, cluster );
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[3];

		criteria.append(" area_cd = ? ");
		criteria.append(" and region_cd = ? ");
		criteria.append(" and cluster_cd = ? ");
		params[0] = area;
		params[1] = region;
		params[2] = cluster;
		System.out.println("Area" + area);
		System.out.println("Region" + region);
		System.out.println("Cluster" + cluster);
		String sql = territorySql + criteria.toString();
		terr = getTerritory(sql, params);
		return userTerritoryByArray(terr);
	}

	public UserTerritory getUserTerritoryByAreaCd(String area, String cluster) {
		Territory[] terr;// = db.getTerritoryByArea( area, cluster);

		StringBuffer criteria = new StringBuffer();
		String[] params = new String[2];

		criteria.append(" area_cd = ? ");
		criteria.append(" and cluster_cd = ? ");
		params[0] = area;
		params[1] = cluster;
		String sql = territorySql + criteria.toString();
		terr = getTerritory(sql, params);

		return userTerritoryByArray(terr);
	}

	public UserTerritory getUserTerritoryByNational(String cluster) {
		Territory[] terr;// = db.getTerritoryByNational( cluster );

		StringBuffer criteria = new StringBuffer();
		String[] params = new String[1];

		criteria.append(" cluster_cd = ? ");
		params[0] = cluster;
		String sql = territorySql + criteria.toString();
		terr = getTerritory(sql, params);

		return userTerritoryByArray(terr);
	}

	public UserTerritory getAdminUserTerritory() {
		Territory[] terr; // = db.getAdminTerritory( );
		ReadProperties props = new ReadProperties();
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[9];
		System.out.println("\nInside getAdminUserTerritory\n");
		 criteria.append(" cluster_cd in (?,?,?,?,?,?,?,?,?) ");
		//criteria.append(" cluster_cd in (:clust_cd) ");

		/*
		 * params[0] = "Steere"; params[1] = "Pratt"; params[2] = "Powers";
		 * params[3] = "Specialty Marke"; params[4] = "Pratt Steere PR";
		 * params[5] = "Powers - PR"; params[6] = "SM PR"; params[7] = "CBU";
		 * params[8] = "ONC Bus Unit";
		 */
		params[0] = props.getValue("CLUSTERCODE1");
		params[1] = props.getValue("CLUSTERCODE2");
		params[2] = props.getValue("CLUSTERCODE3");
		params[3] = props.getValue("CLUSTERCODE4");
		params[4] = props.getValue("CLUSTERCODE5");
		params[5] = props.getValue("CLUSTERCODE6");
		params[6] = props.getValue("CLUSTERCODE7");
		params[7] = props.getValue("CLUSTERCODE8");
		params[8] = props.getValue("CLUSTERCODE9");

		String sql = territorySql + criteria.toString();
		terr = getTerritory(sql, params);
		System.out.println("\nAfter getAdminUserTerritory\n"
				+ terr[1].getAreaDesc());
		return userTerritoryByArray(terr);
	}

	public Territory getTerritory(UserFilter uFilter) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Territory ret = null;
		Territory[] tempTerr;

		StringBuffer criteria = new StringBuffer();
		String[] params;
		if (form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER) {
			ret = new Territory();
			ret.setAreaCd("All");
			ret.setAreaDesc("All");
			ret.setRegionCd("All");
			ret.setRegionDesc("All");
			ret.setDistrictDesc("All");
			ret.setDistrictId("All");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			params = new String[1];
			criteria.append(" area_cd = ? and  ROWNUM=1 ");
			params[0] = form.getArea();
			String sql = territorySql + criteria.toString();
			tempTerr = getTerritory(sql, params);
			if (tempTerr != null) {
				ret = tempTerr[0];
				// change other values to 'All'
				ret.setRegionCd("All");
				ret.setRegionDesc("All");
				ret.setDistrictDesc("All");
				ret.setDistrictId("All");
			}
			// db.getTerritoryArea( form.getArea() );
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			params = new String[2];
			criteria.append(" region_cd = ? ");
			criteria.append(" and area_cd = ?  and ROWNUM=1 ");
			params[0] = form.getRegion();
			params[1] = form.getArea();
			String sql = territorySql + criteria.toString();
			tempTerr = getTerritory(sql, params);
			if (tempTerr != null) {
				ret = tempTerr[0];
				ret.setDistrictDesc("All");
				ret.setDistrictId("All");
			}
			// ret = db.getTerritoryAreaRegion( form.getArea(), form.getRegion()
			// );
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			params = new String[3];
			criteria.append(" district_id = ? ");
			criteria.append(" and region_cd = ? ");
			criteria.append(" and area_cd = ?  and ROWNUM=1 ");
			params[0] = form.getDistrict();
			params[1] = form.getRegion();
			params[2] = form.getArea();
			String sql = territorySql + criteria.toString();
			tempTerr = getTerritory(sql, params);
			// added by Shannon
			if (tempTerr != null && tempTerr.length > 0) {
				ret = tempTerr[0];
			}
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_ALL_SALESPOS_FILTER) {
			ret = new Territory();
			System.out.print("Setting GeoLevels ALL");

			ret.setLevel1("All");
			ret.setLevel2("All");
			ret.setLevel3("All");
			ret.setLevel4("All");
			ret.setLevel5("All");
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL1_FILTER) {
			System.out.print("Setting GeoLevel2 and 3 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2("All");
			ret.setLevel3("All");
			ret.setLevel4("All");
			ret.setLevel5("All");
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL2_FILTER) {
			System.out.print("Setting GeoLevel3 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3("All");
			ret.setLevel4("All");
			ret.setLevel5("All");
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL3_FILTER) {
			System.out.print("Setting GeoLevel4 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4("All");
			ret.setLevel5("All");
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL4_FILTER) {
			System.out.print("Setting GeoLevel5 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5("All");
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL5_FILTER) {
			System.out.print("Setting GeoLevel6 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6("All");
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL6_FILTER) {
			System.out.print("Setting GeoLevel7 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6(form.getLevel6());
			ret.setLevel7("All");
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL7_FILTER) {
			System.out.print("Setting GeoLevel8 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6(form.getLevel6());
			ret.setLevel7(form.getLevel7());
			ret.setLevel8("All");
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL8_FILTER) {
			System.out.print("Setting GeoLevel9 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6(form.getLevel6());
			ret.setLevel7(form.getLevel7());
			ret.setLevel8(form.getLevel8());
			ret.setLevel9("All");
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL9_FILTER) {
			System.out.print("Setting GeoLevel10 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6(form.getLevel6());
			ret.setLevel7(form.getLevel7());
			ret.setLevel8(form.getLevel8());
			ret.setLevel9(form.getLevel9());
			ret.setLevel10("All");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL10_FILTER) {
			System.out.print("Setting GeoLevel11 ALL");
			ret = new Territory();
			ret.setLevel1(form.getLevel1());
			ret.setLevel2(form.getLevel2());
			ret.setLevel3(form.getLevel3());
			ret.setLevel4(form.getLevel4());
			ret.setLevel5(form.getLevel5());
			ret.setLevel6(form.getLevel6());
			ret.setLevel7(form.getLevel7());
			ret.setLevel8(form.getLevel8());
			ret.setLevel9(form.getLevel9());
			ret.setLevel10(form.getLevel10());
		}
		return ret;

	}

	private Territory[] getTerritory(String sql, String[] params) {
		Territory[] ret = null;
		
		ResultSet rs = null;
		PreparedStatement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.prepareStatement(sql);
			for (int i = 1; i <= params.length; i++) {
				st.setObject(i, params[i - 1]);

			}
			st.setFetchSize(5000);

			// System.out.println("QUERY IS"+st.toString());

			// log.info(sql);
			ArrayList tempList = new ArrayList();

			rs = st.executeQuery();

			while (rs.next()) {
				Territory curr = new Territory();
				curr.setAreaCd(rs.getString("areaCd".toUpperCase()));
				curr.setAreaDesc(rs.getString("AreaDesc".toUpperCase()));
				curr.setDistrictDesc(rs.getString("DistrictDesc".toUpperCase()));
				curr.setDistrictId(rs.getString("DistrictId".toUpperCase()));
				curr.setRegionCd(rs.getString("RegionCd".toUpperCase()));
				curr.setRegionDesc(rs.getString("RegionDesc".toUpperCase()));
				curr.setTerritoryId(rs.getString("TerritoryId".toUpperCase()));
				tempList.add(curr);
			}
			ret = new Territory[tempList.size()];
			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (Territory) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/* Method added for RBU changes */
	private void getSalesPosition(String sql, String[] params) {
		SalesPosition[] ret = null;

		ResultSet result = null;
		PreparedStatement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.prepareStatement(sql);
			System.out.println("PARAMETERS LENGTH" + params.length);
			System.out.println("PARAMETERS 1" + params[0]);

			for (int i = 1; i <= params.length; i++) {
				st.setObject(i, params[i - 1]);
				System.out.println("Setting params" + params[i - 1]);
			}
			st.setFetchSize(5000);
			// System.out.println("QUERY IS"+st.toString());
			// System.out.println("QUERY IS"+sql);
			result = st.executeQuery();

			ArrayList salesPositionDataRowList = null;
			HashMap salesPosDescIdHashMap = null;
			HashMap salesPosIdDescHashMap = null;
			// parameters to hold ResultSet values
			String reportToSalesPosId = null;
			String childSalesPosId = null;
			String childSalesPosDesc = null;
			String salesLevel = null;

			ArrayList firstSalesPosDropdown = null;
			int maxLevel = 0;
			// to hold the arrayList of Geography Objects
			salesPositionDataRowList = new ArrayList();
			salesPosDescIdHashMap = new HashMap();
			salesPosIdDescHashMap = new HashMap();
			// ArrayList to hold the first level of Geography Drop down
			firstSalesPosDropdown = new ArrayList();
			// result set processing
			while (result.next()) {
				reportToSalesPosId = result
						.getString("REPORTS_TO_SALES_POSITION_ID");
				childSalesPosId = result.getString("SALES_POSITION_ID");
				childSalesPosDesc = result
						.getString("SALES_POSITIONID_ID_DESC");
				// childGeographyType =result.getString("Geo_Type");
				salesLevel = result.getString("Sales_Level");
				// get the max Level for drop down creation
				int temp = result.getInt("Sales_Level");
				// ArrayList to hold the first level of Geography Drop down
				if (temp == 1) {
					LabelValueBean labelValueBean;
					labelValueBean = new LabelValueBean(childSalesPosDesc,
							childSalesPosId);
					firstSalesPosDropdown.add(labelValueBean);
				}
				if (temp > maxLevel) {
					maxLevel = temp;
				}
				// populating the Geography Object
				SalesPosition salesNode = new SalesPosition();
				salesNode.setReportToSalesPosId(reportToSalesPosId);
				salesNode.setChildSalesPosId(childSalesPosId);
				salesNode.setChildSalesPosDesc(childSalesPosDesc);
				// geoNode.setChildGeographyType(childGeographyType);
				salesNode.setSalesLevel(salesLevel);
				// arrayList of GeographyObjects
				salesPositionDataRowList.add(salesNode);
				// create HashMap for Geography Id and Description
				salesPosDescIdHashMap.put(childSalesPosDesc, childSalesPosId);
				// create HashMap for Geography Id and Description
				salesPosIdDescHashMap.put(childSalesPosId, childSalesPosDesc);
			}

			for (int k = 0; k < firstDropdown.size(); k++) {
				LabelValueBean tempLabelValueBean;
				tempLabelValueBean = (LabelValueBean) firstDropdown.get(k);
				// System.out.println("Label :"+tempLabelValueBean.getLabel());
				// System.out.println("Value :"+tempLabelValueBean.getValue());
				// System.out.println("\n");
			}
			// populate the class variables with Geography details
			completeSalesPositionList = null;
			completeSalesPositionList = salesPositionDataRowList;
			salesPositionHierarchyLevel = maxLevel;
			System.out.println("Territory Handler:No of Levels"
					+ salesPositionHierarchyLevel);
			salesPositionDescIdHashMap = salesPosDescIdHashMap;
			salesPositionIdDescHashMap = salesPosIdDescHashMap;
			this.firstDropdown = firstSalesPosDropdown;

		} catch (Exception e) {
			log.error(e, e);
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
	}

	/**
	 * This function creates the ArrayList of Sales Groups given the Employee id
	 * 
	 * @param preStmt
	 * @param parentEmpId
	 *            salesGroupList: Populates the ArrayList for Sales Groups
	 */
	private void getSalesGroup(String sql, String[] params) {

		// to hold the ResultSet and arrayList of Geography Objects
		ResultSet result = null;
		PreparedStatement st = null;

		// to hold the ResultSet and arrayList of Sales Group
		ArrayList salesGroupList = null;
		// parameters to hold ResultSet values
		String salesGroup = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.prepareStatement(sql);
			for (int i = 1; i <= params.length; i++) {
				st.setObject(i, params[i - 1]);
			}
			st.setFetchSize(5000);
			// System.out.println("QUERY IS"+sql);
			result = st.executeQuery();
			// to hold the ResultSet and arrayList of Sales Group
			salesGroupList = new ArrayList();
			// result set processing
			while (result.next()) {
				salesGroup = result.getString("Sales_Group");
				// arrayList of Sales Groups under the Employee
				if (!salesGroupList.contains(salesGroup))
					salesGroupList.add(salesGroup);
			}
			// populate the class variables with Sales Group details
			this.salesGroupList = salesGroupList;
		} catch (Exception e) {
			log.error(e, e);
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
	}

	/**
	 * Will convert a Territory result list to a UserTerritory object
	 */
	/*
	 * private UserTerritory userTerritoryByArray(Territory[] array) {
	 * UserTerritory ut = new UserTerritory();
	 * 
	 * if (array != null && array.length > 0) { Area tmp; for (int i = 0; i <
	 * array.length; i++) { tmp = territoryToArea(array[i]); ut.addArea(tmp); }
	 * } return ut; }
	 */

	/**
	 * This function creates the Geography HashMap with Key as Geography Id and
	 * Value as the ArrayList of Geography Objects associated with it
	 * geographyHashMap: Populates the HashMap created
	 */
	private UserTerritory userSalesPositionHirearchy() {
		UserTerritory ut = new UserTerritory();
		ArrayList salesPosDataRowList = null;
		ArrayList salesPosHierarchy = null;
		HashMap salesPosMap = null;
		// initialization
		salesPosMap = new HashMap();
		salesPosDataRowList = new ArrayList();
		salesPosDataRowList = completeSalesPositionList;

		ut.setCompleteSalesPositionList(completeSalesPositionList);
		ut.setSalesPositionHierarchyLevel(salesPositionHierarchyLevel);
		ut.setFirstDropdown(firstDropdown);
		ut.setSalesPositionDescIdHashMap(salesPositionDescIdHashMap);

		try {
			// Geography HashMap creation from ArrayList of Geography Objects
			int totCount = salesPosDataRowList.size();
			for (int i = 0; i < totCount; i++) {
				SalesPosition salesPosNode = new SalesPosition();
				SalesPosition temp = new SalesPosition();
				salesPosHierarchy = new ArrayList();
				salesPosNode = (SalesPosition) salesPosDataRowList.get(i);
				String reportsToSalesPosId = (String) salesPosNode
						.getReportToSalesPosId();
				// / String parentGeographyDesc = null;
				// parentGeographyDesc = getGeographyDesc(parentGeography);
				// check whether parent geography id is already present
				// if not create the HashMap
				if (!salesPosMap.containsKey((Object) reportsToSalesPosId)) {
					for (int j = i; j < totCount; j++) {
						temp = (SalesPosition) salesPosDataRowList.get(j);
						if(temp.getChildSalesPosId()!=null)
						if (temp.getReportToSalesPosId().toString()
								.equalsIgnoreCase(reportsToSalesPosId)) {

							LabelValueBean labelValueBean;
							labelValueBean = new LabelValueBean(
									temp.getChildSalesPosDesc(),
									temp.getChildSalesPosId());

							if (!salesPosHierarchy.contains(labelValueBean))
								salesPosHierarchy.add(labelValueBean);
						}
					}
					if (reportsToSalesPosId != null)
						salesPosMap.put(reportsToSalesPosId, salesPosHierarchy);
					/*
					 * else geoMap.put(parentGeography, geoHierarchy);
					 */
				}
			}
			// assigning HashMap to Class variable
			salesPositionHashMap = salesPosMap;
			// System.out.println(geoMap);
			ut.setSalesPositionHashMap(salesPositionHashMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ut;
	}

	/**
	 * This function returns the Geography Description for the given Geography
	 * Id from the HashMap
	 * 
	 * @param geographyDesc
	 * @return geographyId
	 */
	private String getSalesPositionDesc(String salesPositionId) {

		String salesPositionDesc = null;
		try {
			// check whether the HashMap have the geography ID
			if (salesPositionIdDescHashMap
					.containsKey((Object) salesPositionId)) {
				salesPositionDesc = (String) salesPositionIdDescHashMap
						.get(salesPositionId);
			}
			return salesPositionDesc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesPositionDesc;
	}

	/**
	 * This function returns the ArrayList of Geography Descriptions for the
	 * given Geography Description from the HashMap
	 * 
	 * @param parentGeoDesc
	 * @return ArrayList
	 */
	private ArrayList getDropdownSalesPositionDesc(String parentSalesPosDesc) {

		ArrayList salesPosHierarchy = null;
		salesPosHierarchy = new ArrayList();
		ArrayList salesPositionDescs = null;
		salesPositionDescs = new ArrayList();
		String parentSalesPositionId = null;
		parentSalesPositionId = getSalesPositionId(parentSalesPosDesc);
		try {
			// check whether the HashMap have the geography Id Key
			if (salesPositionHashMap
					.containsKey((Object) parentSalesPositionId)) {
				// System.out.println("Displaying Geography Details..loading..");
				salesPosHierarchy = (ArrayList) salesPositionHashMap
						.get(parentSalesPositionId);
				int totCount = salesPosHierarchy.size();
				for (int i = 0; i < totCount; i++) {
					SalesPosition salesPosNode = new SalesPosition();
					salesPosNode = (SalesPosition) salesPosHierarchy.get(i);
					// System.out.println(geoNode.getChildGeographyDesc());
					salesPositionDescs.add(salesPosNode.getChildSalesPosDesc());
				}
			}
			return salesPositionDescs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesPositionDescs;
	}

	/**
	 * This function returns the Geography Id for the given Geography
	 * Description from the HashMap
	 * 
	 * @param geographyDesc
	 * @return geographyId
	 */
	private String getSalesPositionId(String salesPosDesc) {

		String salesPositionId = null;
		try {
			// check whether the HashMap have the geography Description Key
			if (salesPositionDescIdHashMap.containsKey((Object) salesPosDesc)) {
				salesPositionId = (String) salesPositionDescIdHashMap
						.get(salesPosDesc);
			}
			return salesPositionId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesPositionId;
	}

	/**
	 * Will convert a Territory result list to a UserTerritory object
	 */
	private UserTerritory userTerritoryByArray(Territory[] array) {
		UserTerritory ut = new UserTerritory();

		if (array != null && array.length > 0) {
			Area tmp;
			for (int i = 0; i < array.length; i++) {
				tmp = territoryToArea(array[i]);
				ut.addArea(tmp);
			}
		}
		return ut;
	}

	/**
	 * Converts 1 Territory Record into an Area object with Region and District
	 * info if available.
	 */
	private Area territoryToArea(Territory terr) {
		Area area = new Area(terr.getAreaCd(), terr.getAreaDesc());

		if (!Util.isEmpty(terr.getRegionCd())) {
			Region region = new Region(terr.getRegionDesc(),
					terr.getRegionCd(), area);
			area.addRegion(region);

			if (!Util.isEmpty(terr.getDistrictId())) {
				District district = new District(terr.getDistrictDesc(),
						terr.getDistrictId(), region);
				region.addDistrict(district);
			}
		}

		return area;
	}

	public List getRegionNoDm() {
		String sql = "SELECT DISTINCT FE1.DISTRICT_ID,"
				+ "                 FE1.REGION_CD,"
				+ "                 FE1.AREA_CD," + "				   FE1.CLUSTER_CD "
				+ " FROM   V_NEW_FIELD_EMPLOYEE FE1"
				+ " WHERE  (FE1.CLUSTER_CD IN ('Steere',"
				+ "                            'Pratt',"
				+ "                            'Powers')"
				+ "          OR (CLUSTER_CD = 'Specialty Marke'"
				+ "              AND TEAM_CD IN ('SM Oph-Endo Mgt',"
				+ "                              'SM Endo Mgmt')))"
				+ "        AND FE1.DISTRICT_ID IS NOT NULL " + " MINUS "
				+ " SELECT DISTINCT FE2.DISTRICT_ID,"
				+ "                 FE2.REGION_CD,"
				+ "                 FE2.AREA_CD," + "				   FE2.CLUSTER_CD "
				+ " FROM   V_NEW_FIELD_EMPLOYEE FE2"
				+ " WHERE  FE2.TERRITORY_ROLE_CD = 'DM'";

		ResultSet rs = null;
		Statement st = null;

		ArrayList ret = new ArrayList();

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			rs = st.executeQuery(sql);

			ResultSetMetaData rsmd = rs.getMetaData();

			int ncols = rsmd.getColumnCount();

			while (rs.next()) {
				HashMap record = new HashMap();

				for (int i = 1; i <= ncols; i++) {
					String key = rsmd.getColumnName(i);
					String val;
					val = rs.getString(i);
					record.put(key, val);
				}

				ret.add(record);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	private String territorySql = " select  " + "		area_cd as areaCd, "
			+ "		area_desc as areaDesc, " + "		region_cd as regionCd, "
			+ "		region_desc as regionDesc, " + "		district_id as districtId, "
			+ "		district_desc as districtDesc, "
			+ "		territory_id as territoryId " + " from  "
			+ "		v_new_field_employee " + " where  ";

}
