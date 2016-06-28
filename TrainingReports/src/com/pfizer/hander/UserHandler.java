package com.pfizer.hander;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.Employee;
import com.pfizer.db.Feedbackuserlist;
import com.pfizer.db.HQuserlist;
import com.pfizer.db.PhaseEvaluation;
import com.pfizer.db.Product;
import com.pfizer.db.SceEvaluation;
import com.pfizer.db.UserAccess;
import com.pfizer.db.UserGroups;
import com.pfizer.service.Service;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserTerritory;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;

public class UserHandler {
	protected static final Log log = LogFactory.getLog(UserHandler.class);

	public UserHandler() {
	}

	private String accessLevel;

	/**
	 * Main login Method
	 */
	public User getUserByEmployeeId(String id) {

		System.out.println("-----------------------getUserByEmployeeId");
		User user = new User();
		UserAccess au = null;
		String visibility = "";

		if ("admin".equals(id)) {
			return getUserByUp("-2", "THANGAVELS", "amer");
		}

		EmployeeHandler eHander = Service.getServiceFactory()
				.getEmployeeHandler();
		System.out.print("After Employee Handler");

		// Get employ record
		Employee employee = eHander.getEmployeeById(id);// trDb.getUserByEmployeeId(
														// id );

		/*
		 * Log: Added by Meenakshi.M.B on 14-May-2010 Added for CSO requirements
		 */
		System.out.println("-----------------------Before calling visibility");
		visibility = eHander.getScoresVisiblity(employee
				.getSalesPostionTypeCode());
		if (visibility != null || !visibility.equalsIgnoreCase("")) {
			user.setScoresVisible(visibility);
		} else {
			user.setScoresVisible("N");
		}
		System.out
				.println("Current scores visbility" + user.getScoresVisible());
		/* End of addition */

		/* Adding this condition for retaining old values for Special Events */
		Employee employeeOld = eHander.getOldEmployeeById(id);

		user.setUserAcess(au);

		user.setEmployee(employee);
		user.setOldEmployee(employeeOld);

		if (employee == null && au == null) {
			return null;
		}
		// validateUser(user,employee);
		getIsSpecialRole(user, employee);

		if (!user.getValidUser()) {
			System.out.println("Before calling getisRC");
			// getIsSpecialRole(user,employee);
			validateUser(user, employee);
			System.out.println("After calling getisRC");
		}
		System.out.println("Getttttttttttting" + user.getEmplid());
		System.out.println("Getttttttttttting" + user.getSalesPositionId());
		// get territory info
		user.setUserTerritory(getUserTerritory(user));
		user.setUserTerritoryOld(getUserTerritoryOld(user));
		// get product list
		user.setProducts(getEmployeeProducts(employee));
		/* Addition for SCE Feedback form enhancement */
		getUserGroupsData(user);
		/* End of addition */
		// user.setValidUser(true);
		return user;
	}

	private void validateUser(User user, Employee employee) {

		int valid = 0;
		int checkSql = 0;
		// Added condition for TRT Phase 2 - HQ Users Requirement
		if (user.isHQUser()) {
			valid = getValidHQUser(user);
		} else {
			valid = getReportToRel(user);
		}
		System.out.println("Valid" + valid);
		if (valid == 0) {
			// getIsSpecialRole(user,employee);
			if (user.getValidUser()) {
				user.setValidUser(true);
			} else {
				user.setValidUser(false);
			}
		} else {
			user.setValidUser(true);
		}
	}

	// Start: Added to validate HQ user. Added for TRT Phase 2 enhancement HQ
	// Users Requirement
	public int getValidHQUser(User user) {
		ReadProperties props = new ReadProperties();
		int flag = 0;
		ResultSet rs = null;
		PreparedStatement statement = null;
		String validHQUserSql;
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
			validHQUserSql = props.getValue("hqUserSql");
			// System.out.println("QUERY FOR USER"+validHQUserSql);
			// System.out.println("USER EMPLID"+user.getEmplid());
			statement = conn.prepareStatement(validHQUserSql);
			statement.setString(1, user.getEmplid());
			rs = statement.executeQuery();
			int i = 0;
			// System.out.println("Result Set--Fetch Size");
			while (rs.next()) {
				i++;
			}
			System.out.println("i is" + i);
			if (i > 0) {
				// Valid User
				flag = 1;
			} else {
				// Invalid User
				flag = 0;
			}

		}

		catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (statement != null) {
				try {
					statement.close();
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
		return flag;
	}

	// Ends here

	/* Method added for RBU */
	private void getIsSpecialRole(User user, Employee emp) {
		ReadProperties props = new ReadProperties();
		int flag = 0;
		ResultSet rs = null;
		PreparedStatement statement = null;
		boolean isSpecialRole = false;
		String alevel = "";
		isSpecialRole = getSpecialRole(user.getRole());
		alevel = this.getAccessLevel();
		// System.out.println("aLevel is"+alevel);
		// System.out.println("getIsSpecialRole"+isSpecialRole);
		if (isSpecialRole) {
			String specialRoleDetailsSql;
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
				specialRoleDetailsSql = props.getValue("specialRoleDetailsSql");
				statement = conn.prepareStatement(specialRoleDetailsSql);
				statement.setString(1, user.getSalesPositionId());
				statement.setString(2, alevel);
				rs = statement.executeQuery();
				System.out.println("Inside get RC" + specialRoleDetailsSql);
				String tempEmp = null;
				String tempId = null;
				String tempEmpId = null;
				if (rs != null) {
					while (rs.next()) {
						// Valid User
						tempId = rs.getString("salespositionid".toUpperCase());
						tempEmpId = rs.getString("emplid".toUpperCase());
					}
					user.setIsSpecialRole(true);
					// emp.setEmplId(user.getReportsToEmplid());

					emp.setEmplIdForSpRole(tempEmpId);
					emp.setSalesPositionIdForSpRole(tempId);
					// emp.setEmplId(tempEmpId);// commented by neha
					// emp.setSalesPositionId(tempId); //commented by neha
					user.setValidUser(true);
					// System.out.println("NEW EMPLID"+emp.getEmplId());
					// System.out.println("NEW SALES POSITION ID"+emp.getSalesPositionId());
				}

				else if (rs == null) {
					user.setValidUser(false);
				}

			}

			catch (Exception e) {
				log.error(e, e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e2) {
						log.error(e2, e2);
					}
				}
				if (statement != null) {
					try {
						statement.close();
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
	}

	private boolean getSpecialRole(String role) {
		ReadProperties props = new ReadProperties();
		boolean flag = false;
		ResultSet rs = null;
		PreparedStatement statement = null;
		String specialRoleSql = props.getValue("specialRoleSql");
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
			// System.out.println("getSpecialRole Query:"+specialRoleSql);
			statement = conn.prepareStatement(specialRoleSql);
			statement.setString(1, role);
			rs = statement.executeQuery();
			int i = 0;
			System.out.println("Result Set--Fetch Size");
			while (rs.next()) {
				i++;
				this.setAccessLevel(rs.getString("ACCESS_LEVEL"));
			}
			System.out.println("i is" + i);
			if (i > 0) {
				// Valid User
				flag = true;
			} else {
				// Invalid User
				flag = false;
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
			if (statement != null) {
				try {
					statement.close();
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
		// System.out.println("Returning flag"+flag);
		return flag;
	}

	/* Adding getter and setter methids for access level */
	public String getAccessLevel() {
		return this.accessLevel;
	}

	public void setAccessLevel(String str) {
		this.accessLevel = str;
	}

	/* End of addition for RBU enhancement */

	public User getUserByUp(String id, String ntid, String domain) 
	{
		System.out.print("For Admin loop------In getUserByUp");
		User user = new User();
		EmployeeHandler eHander = Service.getServiceFactory().getEmployeeHandler();
		String visibility = "";
		System.out.print("in getUserByUp user ntid is :" + ntid);
		
		UserAccess au = getUserAccess(ntid, domain);
		
		user.setUserAcess(au);
		// System.out.print("in getUserByUp user type is :"+au.getUserType());
		
		System.out.println(id + "datwer");
		if ("admin".equals(id)
				|| (au != null && (User.USER_TYPE_ADMIN
						.equals(au.getUserType())
						|| User.USER_TYPE_SUPER_ADMIN.equals(au.getUserType())
						|| User.USER_TYPE_TSR.equals(au.getUserType()) || User.USER_TYPE_FBU
							.equals(au.getUserType())))) {
			if (au == null) {
				System.out.print("inside getUserByUp if loop");
				au = new UserAccess();
				au.setFname("site");
				au.setLname("admin");
				au.setUserType("ADMIN");
			}
			return getAdminUser(au);
		}
		// Added for TRT Phase 2 - Requirement no. F3 HQ Users
		else if (au != null && (User.USER_TYPE_HQ.equals(au.getUserType()))) {
			return getHqUser(au);
		}
		// Get employ record
		Employee employee = eHander.getEmployeeById(id);
		// Added for retaining the old values for Special Events
		Employee oEmployee = eHander.getOldEmployeeById(id);

		if (employee == null) {
			user.setValidUser(false);
			return user;
		}
		/*
		 * Log: Added by Meenakshi.M.B on 14-May-2010 Added for CSO requirements
		 */
		visibility = eHander.getScoresVisiblity(employee
				.getSalesPostionTypeCode());
		if (visibility != null || !visibility.equalsIgnoreCase("")) {
			user.setScoresVisible(visibility);
		} else {
			user.setScoresVisible("N");
		}
		System.out.println("Current scores visbility "
				+ user.getScoresVisible());
		user.setEmployee(employee);
		user.setOldEmployee(oEmployee);

		UserTerritory ut = getUserTerritory(user);
		UserTerritory ut1 = getUserTerritoryOld(user);
		// get territory info
		user.setUserTerritory(ut);
		user.setUserTerritoryOld(ut1);
		// get product list
		user.setProducts(getEmployeeProducts(employee));
		// validateUser(user,employee);
		getIsSpecialRole(user, employee);
		if (!user.getValidUser()) {
			// getIsSpecialRole(user,employee);
			validateUser(user, employee);
		}
		return user;
	}

	// Start : Added for TRT Phase 2 enhancement - HQ Users requirement
	private User getHqUser(UserAccess ua) {
		System.out.print("Inside HQ");
		User user = new User();

		// create admin Employee record
		Employee employee = new Employee();
		employee.setRole(ua.getUserType());
		employee.setFirstName(ua.getFname());
		employee.setLastName(ua.getLname());

		user.setEmployee(employee);
		// user.setOldEmployee(employee);
		user.setUserAcess(ua);
		// get territory info
		user.setUserTerritory(getUserTerritory(user));
		user.setUserTerritoryOld(getUserTerritoryOld(user));
		// get product list
		// user.setProducts( getAllProducts() );
		/*
		 * Commented for RBU on 03-Feb-2009. It is used to check validity of the
		 * user
		 */
		// validateUser(user,employee);
		/* Adding for SCE feedback enhancement */
		getUserGroupsData(user);
		/*
		 * Log: Added by Meenakshi.M.B on 18-May-2010 Added for CSO requirements
		 */

		//
		validateUser(user, employee);
		user.setScoresVisible("Y");
		System.out.println("Score visibility for Admin----"
				+ user.getValidUser());
		// user.setValidUser(true);
		return user;
	}

	// Ends here
	private User getAdminUser(UserAccess ua) {
		System.out.println("Inside getAdminUser");
		User user = new User();

		// create admin Employee record
		Employee employee = new Employee();
		employee.setRole(ua.getUserType());
		employee.setFirstName(ua.getFname());
		employee.setLastName(ua.getLname());

		user.setEmployee(employee);
		user.setOldEmployee(employee);
		user.setUserAcess(ua);
		// get territory info

		user.setUserTerritory(getUserTerritory(user));

		user.setUserTerritoryOld(getUserTerritoryOld(user));

		// get product list
		user.setProducts(getAllProducts());

		/*
		 * Commented for RBU on 03-Feb-2009. It is used to check validity of the
		 * user
		 */
		// validateUser(user,employee);
		/* Adding for SCE feedback enhancement */
		getUserGroupsData(user);

		/*
		 * Log: Added by Meenakshi.M.B on 18-May-2010 Added for CSO requirements
		 */
		user.setScoresVisible("Y");
		System.out.println("Score visibility for Admin----"
				+ user.getScoresVisible());
		user.setValidUser(true);
		return user;
	}

	public List getEmployeeProducts(Employee employee) {
		List products = new ArrayList();

		String cluster_cd = employee.getClusterCode();
		String team_cd = employee.getTeamCode();
		// if ("SM Oph-Endo Mgt".equals( employee.getTeamCode() )) {
		// team_cd = "SM Endo Mgmt";
		// }
		String criteria = " where cluster_desc = '" + cluster_cd
				+ "' and team_desc = '" + team_cd + "'";

		Product[] ret = getProducts(criteria);

		for (int i = 0; i < ret.length; i++) {
			products.add(ret[i]);
		}

		return products;
	}

	private List getAllProducts() {
		List products = new ArrayList();

		Product[] ret = getProducts(" ");

		for (int i = 0; i < ret.length; i++) {
			products.add(ret[i]);
		}

		return products;
	}

	private UserTerritory getUserTerritory(User user) {
		UserTerritory ut = null;
		/* System.out.println( "GeoType:" + user.getGeoType()); */
		ReadProperties props = new ReadProperties();
		TerritoryHandler tHandler = Service.getServiceFactory()
				.getTerritoryHandler();

		/* Added for RBU changes */
		if (user.isAdmin() || user.isSuperAdmin() || user.isTsrAdmin()) {
			System.out.println("User Handler Admin Loop");
			ut = tHandler.getAdminSalesPosition();
			System.out.println("\nUser Handler Admin Loop1"
					+ ut.getFirstDropdown().size());
		} else {
			System.out.println("TERRITORY HANDLER--EMPLID" + user.getEmplid());
			System.out.println("TERRITORY HANDLER-- GEO ID"
					+ user.getGeographyId());
			ut = tHandler.getUserSalesPosition(user.getEmplid(),
					user.getSalesPositionId());
		}
		System.out.println(ut.getType());
		return ut;
	}

	/* Adding new method for retaining special events */
	private UserTerritory getUserTerritoryOld(User user) {
		UserTerritory ut = null;
		System.out.println("Calling getUserTerritoryOld");
		ReadProperties props = new ReadProperties();
		TerritoryHandler tHandler = Service.getServiceFactory()
				.getTerritoryHandler();
		System.out.println("Role is" + user.getOldRole());
		if ("DM".equals(user.getOldRole())) {
			ut = tHandler.getUtEmployeeId(user.getEmplid());
		} else if ("RM".equals(user.getOldRole())
				|| ("SD".equals(user.getOldRole()) && props
						.getValue("CLUSTER8").equals(user.getCluster()))
				|| ("SD".equals(user.getOldRole()) && props
						.getValue("CLUSTER9").equals(user.getCluster()))
				|| "ASD".equals(user.getOldRole())
				|| ("NSD".equals(user.getOldRole())
						&& "PWCM".equals(user.getTeam()) && props.getValue(
						"CLUSTER4").equals(user.getCluster()))
				|| "RC".equals(user.getOldRole())
				|| "ARM".equals(user.getOldRole())
				|| "SDIR".equals(user.getOldRole())
				|| "CAD".equals(user.getOldRole())
				|| "SDTL".equals(user.getOldRole())) {
			ut = tHandler.getUtByAreReg(user.getAreaCd(), user.getRegionCd(),
					user.getCluster());
		} else if ("VP".equals(user.getOldRole())
				|| "DDO".equals(user.getOldRole())
				|| "DAO".equals(user.getOldRole()) ||
				// Added to give access to DO role also
				"DO".equals(user.getOldRole())) {
			ut = tHandler.getUserTerritoryByAreaCd(user.getAreaCd(),
					user.getCluster());
		} else if ("SVP".equals(user.getOldRole())
				|| ("SD".equals(user.getOldRole()) && props
						.getValue("CLUSTER2").equals(user.getCluster()))
				|| "DCO".equals(user.getOldRole())
				|| "NSD".equals(user.getOldRole())) {
			ut = tHandler.getUserTerritoryByNational(user.getCluster());
		} else if (User.USER_TYPE_ADMIN.equals(user.getRole())
				|| User.USER_TYPE_TSR.equals(user.getRole())
				|| User.USER_TYPE_SUPER_ADMIN.equals(user.getRole())) {
			ut = tHandler.getAdminUserTerritory();
		}
		// _ System.out.println("\n Returning UT------------"+ut);
		return ut;
	}

	/* End of new method USER_TYPE_FEEDBACK */

	private UserAccess getUserAccess(String ntid, String domain) {
		System.out.print("For Admin loop------getUserAccess");
		StringBuffer criteria = new StringBuffer();

		criteria.append(" upper(ua.nt_id) = '" + ntid.toUpperCase() + "' ");
		criteria.append(" and upper(ua.nt_domain) = '" + domain.toUpperCase()
				+ "' ");
		criteria.append(" and ua.status = 'Active'");

		String search = " select 	* " + " from " + " user_access ua "
				+ " where  " + criteria;
		System.out.print(search);
		return getUserAccess(search);
	}

	public UserAccess getUserAccessById(String id) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" ua.user_id = '" + id + "' ");

		String search = " select 	* " + " from " + " user_access ua "
				+ " where  " + criteria;
		return getUserAccess(search);
	}

	private UserAccess getUserAccess(String sql) {
		UserAccess ret = null;

		ResultSet rs = null;
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
			List tempList = new ArrayList();

			rs = st.executeQuery(sql);
			st.setFetchSize(5);

			while (rs.next()) {
				ret = new UserAccess();
				ret.setEmail(rs.getString("email".toUpperCase()));
				ret.setEmplid(rs.getString("emplid".toUpperCase()));
				ret.setFname(rs.getString("fname".toUpperCase()));
				ret.setLname(rs.getString("lname".toUpperCase()));
				ret.setNtDomain(rs.getString("nt_domain".toUpperCase()));
				ret.setNtId(rs.getString("nt_id".toUpperCase()));
				ret.setUserId(rs.getString("user_id".toUpperCase()));
				ret.setUserType(rs.getString("type".toUpperCase()));
				ret.setStatus(rs.getString("status".toUpperCase()));
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

	public List getUsersByStatus(String status) {
		String sql = " Select * from user_access ";
		String whereClause = " where status = '" + status + "' ";

		if (!"All".equals(status)) {
			sql = sql + whereClause;
		}

		return getUserList(sql);
	}

	private List getUserList(String sql) {
		List userList = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();

			rs = st.executeQuery(sql);
			st.setFetchSize(20);

			while (rs.next()) {
				UserAccess ua = new UserAccess();
				ua.setEmail(rs.getString("email".toUpperCase()));
				ua.setEmplid(rs.getString("emplid".toUpperCase()));
				ua.setFname(rs.getString("fname".toUpperCase()));
				ua.setLname(rs.getString("lname".toUpperCase()));
				ua.setNtDomain(rs.getString("nt_domain".toUpperCase()));
				ua.setNtId(rs.getString("nt_id".toUpperCase()));
				ua.setUserId(rs.getString("user_id".toUpperCase()));
				ua.setUserType(rs.getString("type".toUpperCase()));
				ua.setStatus(rs.getString("status".toUpperCase()));
				userList.add(ua);
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
		return userList;
	}

	public void insertUserAccess(UserAccess ua) {
		String retString = null;

		ResultSet rs = null;
		PreparedStatement statement = null;
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

			String updateUserSql = "insert into user_access "
					+ " (user_id, emplid, nt_id, nt_domain, email, type, fname, lname, status) "
					+ " values (user_access_seq.nextval,?,?,?,?,?,?,?,?) ";
			statement = conn.prepareStatement(updateUserSql);
			log.info(updateUserSql);

			statement.setString(1, ua.getEmplid());
			statement.setString(2, ua.getNtId());
			statement.setString(3, ua.getNtDomain());
			statement.setString(4, ua.getEmail());
			statement.setString(5, ua.getUserType());
			statement.setString(6, ua.getFname());
			statement.setString(7, ua.getLname());
			statement.setString(8, ua.getStatus());

			statement.executeUpdate();
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
			if (statement != null) {
				try {
					statement.close();
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

	public void updateUserAccess(UserAccess ua) {
		String retString = null;

		ResultSet rs = null;
		PreparedStatement statement = null;
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
			String updateUserSql = "update user_access "
					+ "  set emplid = ?, nt_id = ?, nt_domain = ?, email = ?, type = ?, fname = ?, lname = ?, status = ? "
					+ " where user_id = ?";
			log.info(updateUserSql);

			statement = conn.prepareStatement(updateUserSql);

			statement.setString(1, ua.getEmplid());
			statement.setString(2, ua.getNtId());
			statement.setString(3, ua.getNtDomain());
			statement.setString(4, ua.getEmail());
			statement.setString(5, ua.getUserType());
			statement.setString(6, ua.getFname());
			statement.setString(7, ua.getLname());
			statement.setString(8, ua.getStatus());
			statement.setString(9, ua.getUserId());

			statement.executeUpdate();
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
			if (statement != null) {
				try {
					statement.close();
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

	private Product[] getProducts(String criteria) {
		Product[] prods = null;

		ResultSet rs = null;
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

			String sql = " select " + "		distinct  "
					+ "		product_cd as productCode, "
					+ "		product_desc as productDesc " + " from  "
					+ "		fft_product_assignment " + criteria;

			st = conn.createStatement();
			st.setFetchSize(5);

			rs = st.executeQuery(sql);
			List tempList = new ArrayList();

			while (rs.next()) {
				Product curr = new Product();
				curr.setProductCode(rs.getString("productCode".toUpperCase()));
				curr.setProductDesc(rs.getString("productDesc".toUpperCase()));
				tempList.add(curr);
			}

			prods = new Product[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				prods[j] = (Product) tempList.get(j);
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
		return prods;
	}

	/* Method added for RBU */
	public int getReportToRel(User user) {
		ReadProperties props = new ReadProperties();
		int flag = 0;
		ResultSet rs = null;
		PreparedStatement statement = null;
		String validUserSql;
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
			validUserSql = props.getValue("validUserSql");
			// System.out.println("QUERY FOR USER"+validUserSql);
			// System.out.println("USER EMPLID"+user.getEmplid());
			statement = conn.prepareStatement(validUserSql);
			statement.setString(1, user.getEmplid());
			rs = statement.executeQuery();
			int i = 0;
			// System.out.println("Result Set--Fetch Size");
			while (rs.next()) {
				i++;
			}
			System.out.println("i is" + i);
			if (i > 0) {
				// Valid User
				flag = 1;
			} else {
				// Invalid User
				flag = 0;
			}

		}

		catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (statement != null) {
				try {
					statement.close();
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
		return flag;
	}

	/**
	 * This Function gets all the distinct BU, SalesOrg and Roles from the
	 * database and creates a HashMap with BU as the key and ArrayList of
	 * SalesOrg-Roles as the value.
	 */

	public HashMap getUserGroupHashMap() {
		// creating statements for query execution
		/*
		 * String buSalesRoleQuery =
		 * " select distinct bu business_unit, sales_group sales_organization, role_desc role "
		 * + " from mv_field_employee_rbu order by 1 asc, 2 asc, 3 asc ";
		 */
		String buSalesRoleQuery = " select distinct a.bu business_unit, "
				+ " CASE WHEN a.salesgroup IS NULL THEN 'Not Present' ELSE a.salesgroup END sales_organization, "
				+ " CASE WHEN b.role_desc IS NULL THEN 'Not Present' ELSE b.role_desc END role "
				+ " from mv_bu_salesgroup_rbu a, mv_field_employee_rbu b "
				+ " where a.BU = b.BU (+) " + " order by 1 asc, 2 asc, 3 asc ";

		ResultSet rs = null;
		Statement st = null;
		// temporary variables to hold the result set values
		String busUnit = null;
		String salesOrg = null;
		String role = null;
		// final HashMap
		HashMap completeUserGroupMap = null;
		completeUserGroupMap = new HashMap();

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
			st.setFetchSize(5);
			rs = st.executeQuery(buSalesRoleQuery);
			// temporary variables to hold BU and SalesOrg
			String temp1 = null;
			String temp2 = null;
			// ArrayList to hold user roles for a particular Sales Organization
			ArrayList userRoles = null;
			userRoles = new ArrayList();
			// HashMap to hold Sales(key)-UserRoles(Value)
			HashMap roleSalesMap = null;
			roleSalesMap = new HashMap();
			// ArrayList to hold HashMaps of SalesOrg for a particular BU
			ArrayList forEachBU = new ArrayList();
			// HashMap to hold BU(key)-ArrayList of roleSalesMap(Value)
			HashMap salesBuMap = null;
			salesBuMap = new HashMap();

			// result set processing
			while (rs.next()) {
				// setting temp1 and temp2 to initial BU and SalesOrg
				if (temp1 == null) {
					temp1 = rs.getString("business_unit".toUpperCase());
				}
				if (temp2 == null) {
					temp2 = rs.getString("sales_organization".toUpperCase());
				}

				busUnit = rs.getString("business_unit".toUpperCase());
				salesOrg = rs.getString("sales_organization".toUpperCase());
				role = rs.getString("role".toUpperCase());
				// check if the current BU is equal to the Previous BU
				if(temp1!=null && busUnit!=null && temp2!=null && salesOrg!=null){
				if (temp1.toString().equalsIgnoreCase(busUnit.toString())) {
					// check if the current SalesOrg is equal to the Previous
					// SalesOrg
					if (temp2.toString().equalsIgnoreCase(salesOrg.toString())) {
						userRoles.add(role);
					} else {
						// creating the HashMap for SalesOrg-Roles
						if (userRoles.size() >= 1)
							roleSalesMap.put(temp2, userRoles);
						else
							roleSalesMap.put(salesOrg, userRoles);
						// setting temp2 to present SalesOrg and Adding
						// SalesOrg-Role Map to the ArrayList
						temp2 = salesOrg;
						forEachBU.add(roleSalesMap);
						// reseting the Maps and ArrayLists
						roleSalesMap = new HashMap();
						userRoles = new ArrayList();
						userRoles.add(role);
					}
				} else {
					// creating the HashMap for SalesOrg-Roles
					roleSalesMap.put(temp2, userRoles);
					forEachBU.add(roleSalesMap);
					// creating the HashMap for BU-ArrayList of SalesOrg-Roles
					// Map
					salesBuMap.put(temp1, forEachBU);
					// resetting of Maps and Lists and setting temporary
					// variables
					forEachBU = new ArrayList();
					temp2 = salesOrg;
					temp1 = busUnit;
					roleSalesMap = new HashMap();
					userRoles = new ArrayList();
					userRoles.add(role);
				}
				}
			}
			// Creating the HashMap entry for the last record
			roleSalesMap.put(temp2, userRoles);
			forEachBU.add(roleSalesMap);
			salesBuMap.put(temp1, forEachBU);
			completeUserGroupMap = salesBuMap;
			// System.out.println(completeUserGroupMap);
			return completeUserGroupMap;
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
		// System.out.println("Returning completeUserGroupMap"+completeUserGroupMap);
		return completeUserGroupMap;

	}

	// added for RBU

	/* Added for Group Administration module -RBU changes */
	public List getGroupsByStatus() {
		String sql = " Select * from user_group order by GROUP_NAME asc ";
		/*
		 * String whereClause = " where status = '" + status + "' ";
		 * 
		 * if (!"All".equals(status)) { sql = sql + whereClause; }
		 */
		return getGroupList(sql);
	}

	private List getGroupList(String sql) {
		List userList = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();

			rs = st.executeQuery(sql);
			st.setFetchSize(20);
			// Added HQ users in group Administration
			while (rs.next()) {
				UserGroups ua = new UserGroups();
				ua.setBusUnit(rs.getString("Business_Unit".toUpperCase()));
				ua.setSalesOrg(rs.getString("Sales_organization".toUpperCase()));
				ua.setRole(rs.getString("role".toUpperCase()));
				ua.setGroupName(rs.getString("group_name".toUpperCase()));
				ua.setGroupId(rs.getInt("group_id".toUpperCase()));
				ua.setSelectedFBU(rs.getString("feedback_users".toUpperCase()));
				ua.setSelectedHQU(rs.getString("HQ_users".toUpperCase()));
				userList.add(ua);
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
		System.out.println("USERLIST" + userList.size());
		return userList;
	}

	public UserGroups getUserGroupsById(String id) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" ug.group_id = '" + id + "' ");

		String search = " select 	* " + " from " + " user_group ug "
				+ " where  " + criteria;
		return getUserGroups(search);
	}

	private UserGroups getUserGroups(String sql) {
		UserGroups ret = null;

		ResultSet rs = null;
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
			List tempList = new ArrayList();

			rs = st.executeQuery(sql);
			st.setFetchSize(5);

			while (rs.next()) {
				ret = new UserGroups();
				ret.setGroupId(rs.getInt("group_id".toUpperCase()));
				ret.setGroupName(rs.getString("group_name".toUpperCase()));
				ret.setSelectedBU(rs.getString("business_unit".toUpperCase()));
				ret.setSelectedSalesorg(rs.getString("sales_organization"
						.toUpperCase()));
				ret.setSelectedRole(rs.getString("role".toUpperCase()));
				ret.setSelectedFBU(rs.getString("feedback_users".toUpperCase()));
				ret.setSelectedHQU(rs.getString("HQ_users".toUpperCase()));
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

	public void insertUserGroups(UserGroups ug) {
		String retString = null;

		ResultSet rs = null;
		PreparedStatement statement = null;
		ReadProperties props = new ReadProperties();

		String updateUserSql;
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
			updateUserSql = props.getValue("usergroup");
			statement = conn.prepareStatement(updateUserSql);
			System.out.println(updateUserSql);
			statement.setString(1, ug.getSelectedBU());
			statement.setString(2, ug.getSelectedSalesorg());
			statement.setString(3, ug.getSelectedRole());
			statement.setString(4, ug.getGroupName());
			statement.setString(5, ug.getSelectedFBU());
			statement.setString(6, ug.getSelectedHQU());
			// statement.setInt(5,ug.getGroupId());
			// System.out.println("Selected BU in Insert"+ug.getSelectedBU());
			// System.out.println("Selected SO in Insert"+ug.getSelectedSalesorg());
			// System.out.println("Selected Role in Insert"+ug.getSelectedRole());
			// System.out.println("Selected GN in Insert"+ug.getGroupName());

			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (statement != null) {
				try {
					statement.close();
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

	public void updateUserGroups(UserGroups ug) {
		String retString = null;

		ResultSet rs = null;
		PreparedStatement statement = null;
		ReadProperties props = new ReadProperties();

		String updateUserSql;
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
			updateUserSql = props.getValue("updateusergroup");
			System.out.println(updateUserSql);

			statement = conn.prepareStatement(updateUserSql);
			statement.setString(1, ug.getSelectedBU());
			statement.setString(2, ug.getSelectedSalesorg());
			statement.setString(3, ug.getSelectedRole());
			statement.setString(4, ug.getGroupName());
			statement.setString(5, ug.getSelectedFBU());
			statement.setString(6, ug.getSelectedHQU());
			statement.setInt(7, ug.getGroupId());

			// System.out.println("Selected BU in Update"+ug.getSelectedBU());
			// System.out.println("Selected SO in Update"+ug.getSelectedSalesorg());
			// System.out.println("Selected Role in Update"+ug.getSelectedRole());
			// System.out.println("Selected GN in Update"+ug.getGroupName());
			// System.out.println("Selected GI in Update"+ug.getGroupId());

			statement.executeUpdate();
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
			if (statement != null) {
				try {
					statement.close();
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

	// added for RBU - Phase 2
	/* Added for Phase Evaluation Administration Module -RBU Phase 2 */
	public List getPhaseEvalStatusList(String queryCondition) {
		String sql = " select * from PHASE_EVALUATION_ACCESS " + queryCondition
				+ " order by role_desc asc ";
		/*
		 * String whereClause = " where status = '" + status + "' ";
		 * 
		 * if (!"All".equals(status)) { sql = sql + whereClause; }
		 */
		return getPhaseEvalStatusList(sql, "");
	}

	private List getPhaseEvalStatusList(String sql, String temp) {
		List phaseEvalStatusList = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();

			System.out.println("Phase Evaluation Query: " + sql);
			rs = st.executeQuery(sql);
			st.setFetchSize(20);

			while (rs.next()) {
				PhaseEvaluation phEval = new PhaseEvaluation();
				phEval.setRoleCd(rs.getString("role_cd".toUpperCase()));
				phEval.setRoleDesc(rs.getString("role_desc".toUpperCase()));
				phEval.setSave(rs.getString("save".toUpperCase()));
				phEval.setSubmit(rs.getString("submit".toUpperCase()));
				phEval.setReportType(rs.getString("report_type".toUpperCase()));
				phaseEvalStatusList.add(phEval);
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
		System.out.println("Phase Evaluation Access List"
				+ phaseEvalStatusList.size());
		return phaseEvalStatusList;
	}

	/* RBU Phase 2 Added */
	public void updatePhaseEvaluationAccess(String saveRoles,
			String submitRoles, String reportType) {
		String retString = null;

		ResultSet rs = null;
		Statement statement = null;

		String saveQuery = null;
		String saveQuerySetN = null;
		String submitQuery = null;
		String submitQuerySetN = null;

		if (saveRoles.equalsIgnoreCase("()")) {
			System.out.println("Save List Empty!!!");
			saveQuery = null;

			saveQuerySetN = " update PHASE_EVALUATION_ACCESS "
					+ " SET Save = 'N' " + " where report_type in ('"
					+ reportType + "') ";

		} else {
			saveQuery = " update PHASE_EVALUATION_ACCESS " + " SET Save = 'Y' "
					+ " where role_cd in " + saveRoles
					+ " and report_type in ('" + reportType + "') ";

			saveQuerySetN = " update PHASE_EVALUATION_ACCESS "
					+ " SET Save = 'N' " + " where role_cd not in " + saveRoles
					+ " and report_type in ('" + reportType + "') ";
		}

		if (submitRoles.equalsIgnoreCase("()")) {
			System.out.println("Submit List Empty!!!");
			submitQuery = null;

			submitQuerySetN = " update PHASE_EVALUATION_ACCESS "
					+ " SET Submit = 'N' " + " where report_type in ('"
					+ reportType + "') ";
		} else {
			submitQuery = " update PHASE_EVALUATION_ACCESS "
					+ " SET Submit = 'Y', " + "     Save = 'Y' "
					+ " where role_cd in " + submitRoles
					+ " and report_type in ('" + reportType + "') ";

			submitQuerySetN = " update PHASE_EVALUATION_ACCESS "
					+ " SET Submit = 'N' " + " where role_cd not in "
					+ submitRoles + " and report_type in ('" + reportType
					+ "') ";
		}

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
			statement = conn.createStatement();
			log.info(saveQuery);
			log.info(submitQuery);

			System.out.println("Save Access Query :" + saveQuery);
			System.out.println("Save Access Query N:" + saveQuerySetN);
			System.out.println("Submit Access Query :" + submitQuery);
			System.out.println("Submit Access Query N :" + submitQuerySetN);

			if (saveQuery != null)
				statement.executeUpdate(saveQuery);
			statement.executeUpdate(saveQuerySetN);
			if (submitQuery != null)
				statement.executeUpdate(submitQuery);
			statement.executeUpdate(submitQuerySetN);
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
			if (statement != null) {
				try {
					statement.close();
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

	/* RBU Phase 2 End f Addition */
	/* SCE evaluation addition */
	public List getsceEvalStatusList() {
		String sql = "select * from sce_evaluation_access order by usergroup asc ";
		return getsceEvalStatusList(sql);
	}

	private List getsceEvalStatusList(String sql) {
		List evalList = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();
			System.out.println("before execution");
			try {
				rs = st.executeQuery(sql);
			} catch (Exception e) {
				System.out.println("Exception" + e);
			}
			st.setFetchSize(20);

			while (rs.next()) {
				SceEvaluation se = new SceEvaluation();
				se.setUserGroup(rs.getString("USERGROUP".toUpperCase()));
				se.setSave(rs.getString("SAVE".toUpperCase()));
				se.setSubmit(rs.getString("SUBMIT".toUpperCase()));
				evalList.add(se);
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
		System.out.println("SCE eval list" + evalList.size());
		return evalList;
	}

	public void updateSceEvaluationAccess(String saveRoles, String submitRoles) {
		String retString = null;

		ResultSet rs = null;
		Statement statement = null;

		String saveQuery = null;
		String saveQueryN = null;
		String submitQuery = null;
		String submitQueryN = null;

		if (saveRoles.equalsIgnoreCase("()")) {
			System.out.println("Save List Empty!!!");
			saveQuery = null;
			saveQueryN = " update SCE_EVALUATION_ACCESS " + " SET Save = 'N' ";

		} else {
			saveQuery = " update SCE_EVALUATION_ACCESS " + " SET Save = 'Y' "
					+ " where usergroup in " + saveRoles;
			saveQueryN = " update SCE_EVALUATION_ACCESS " + " SET Save = 'N' "
					+ " where usergroup not in " + saveRoles;

		}

		if (submitRoles.equalsIgnoreCase("()")) {
			System.out.println("Submit List Empty!!!");
			submitQuery = null;
			submitQueryN = " update SCE_EVALUATION_ACCESS "
					+ " SET Submit = 'N' ";
		} else {
			submitQuery = " update SCE_EVALUATION_ACCESS "
					+ " SET Submit = 'Y' , Save = 'Y'" + " where usergroup in "
					+ submitRoles;
			submitQueryN = " update SCE_EVALUATION_ACCESS "
					+ " SET Submit = 'N' " + " where usergroup not in "
					+ submitRoles;
		}

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
			statement = conn.createStatement();
			log.info(saveQuery);
			log.info(submitQuery);
			System.out.println("Save Access Query :" + saveQuery);
			System.out.println("Save Access Query N:" + saveQueryN);
			System.out.println("Submit Access Query :" + submitQuery);
			System.out.println("Submit Access Query N :" + submitQueryN);

			if (saveQuery != null)
				statement.executeUpdate(saveQuery);
			statement.executeUpdate(saveQueryN);
			if (submitQuery != null)
				statement.executeUpdate(submitQuery);
			statement.executeUpdate(submitQueryN);

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
			if (statement != null) {
				try {
					statement.close();
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

	/* Addition for Feedback user */
	public List getfeedBackUsers() {
		String sql = " select fname,lname from user_access where type = 'Feedback User' order by 1 asc ";
		return getfeedBackUsersList(sql);
	}

	private List getfeedBackUsersList(String sql) {
		List FBlist = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();
			System.out.println("before execution");
			try {
				rs = st.executeQuery(sql);
			} catch (Exception e) {
				System.out.println("Exception" + e);
			}
			st.setFetchSize(20);

			while (rs.next()) {
				Feedbackuserlist fb = new Feedbackuserlist();
				fb.setFName((rs.getString(1).toUpperCase()));
				fb.setLName((rs.getString(2).toUpperCase()));
				FBlist.add(fb);
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
		System.out.println("Feed back user list list" + FBlist.size());
		return FBlist;
	}

	/* Addition for SCE Feedback form enhancement */

	public void getUserGroupsData(User user) {
		System.out.println("Entering UserGroupsData.................");
		ReadProperties props = new ReadProperties();
		int flag = 0;
		ResultSet rs = null;
		Statement statement = null;
		ArrayList groupsList = new ArrayList();
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
			statement = conn.createStatement();
			String userGroupsSql = props.getValue("userGroupsSql");
			rs = statement.executeQuery(userGroupsSql);
			System.out.println("Is feedback user" + user.isFeedbackUser());

			if (rs != null) {
				while (rs.next()) {
					String groupName = null;
					String businessUnit = null;
					String salesOrg = null;
					String role = null;
					String feedbackUsers = null;
					String hqUsers = null;
					groupName = rs.getString("group_name".toUpperCase());
					businessUnit = rs.getString("business_unit".toUpperCase());
					salesOrg = rs.getString("sales_organization".toUpperCase());
					role = rs.getString("role".toUpperCase());
					feedbackUsers = rs
							.getString("feedback_users".toUpperCase());
					hqUsers = rs.getString("HQ_users".toUpperCase());

					if ((Util.splitFields(businessUnit, user.getBusinessUnit())
							&& Util.splitFields(salesOrg,
									user.getSalesOrganization()) && Util
								.splitFields(role, user.getRoleDesc()))
							|| (user.isFeedbackUser() && Util.splitFields(
									feedbackUsers, user.getName()))
							|| (user.isHQUser() && Util.splitFields(hqUsers,
									user.getName()))) {
						groupsList.add(groupName);
						System.out.println("Adding group..." + groupName);
					}
				}
				System.out.println("Groups List" + groupsList.size());
				user.setGroups(groupsList);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/* End of addition */

	/* Addition for HQ user */
	public List getHQUsers() {
		String sql = " select fname,lname from user_access where type = 'HQ User' order by 1 asc ";
		return getHQUsersList(sql);
	}

	private List getHQUsersList(String sql) {
		List HQlist = new ArrayList();
		ResultSet rs = null;
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
			List tempList = new ArrayList();
			System.out.println("before execution");
			try {
				rs = st.executeQuery(sql);
			} catch (Exception e) {
				System.out.println("Exception" + e);
			}
			st.setFetchSize(20);

			while (rs.next()) {
				HQuserlist hq = new HQuserlist();
				hq.setFName((rs.getString(1).toUpperCase()));
				hq.setLName((rs.getString(2).toUpperCase()));
				HQlist.add(hq);
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
		System.out.println("HQ user list list" + HQlist.size());
		return HQlist;
	}

}