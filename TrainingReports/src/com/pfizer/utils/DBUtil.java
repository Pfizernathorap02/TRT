package com.pfizer.utils;

import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

import javax.sql.*;
import javax.naming.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBUtil {
	static public Connection getConn(String dsName)
			throws java.sql.SQLException, NamingException {
		return getConn(dsName, false);
	}

	static public Connection getConn(String dsName,
			boolean a_blnNoManagedConnection) throws java.sql.SQLException,
			NamingException {

		Context ctx = new InitialContext();
		Connection conn = null;

		javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(dsName);
		conn = ds.getConnection();

		// Always use autocommit = false
		conn.setAutoCommit(false);

		return conn;
	}

	static public Connection getTRDBConn(String dsName,
			boolean a_blnNoManagedConnection) throws java.sql.SQLException,
			NamingException {

		Context ctx = new InitialContext();
		Connection conn = null;

		javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(dsName);
		conn = ds.getConnection();

		// Always use autocommit = false
		conn.setAutoCommit(false);

		return conn;
	}

	static public String getString(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		String strValue = a_resultSet.getString(a_intIndex);
		if (a_resultSet.wasNull())
			strValue = null;
		return strValue;
	}

	static public java.util.Date getDate(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		java.util.Date dtmValue = a_resultSet.getDate(a_intIndex);
		if (a_resultSet.wasNull())
			dtmValue = null;
		return dtmValue;
	}

	static public java.util.Date getTSDate(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		java.sql.Timestamp tsValue = a_resultSet.getTimestamp(a_intIndex);
		if (a_resultSet.wasNull())
			return null;
		return new java.util.Date(tsValue.getTime() + tsValue.getNanos()
				/ 1000000);
	}

	static public Long getLong(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Long lngValue = new Long(a_resultSet.getLong(a_intIndex));
		if (a_resultSet.wasNull())
			lngValue = null;
		return lngValue;
	}

	static public Integer getInt(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		return getInteger(a_resultSet, a_intIndex);
	}

	static public Integer getInteger(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Integer intValue = new Integer(a_resultSet.getInt(a_intIndex));
		if (a_resultSet.wasNull())
			intValue = null;
		return intValue;
	}

	static public Short getShort(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Short shtValue = new Short(a_resultSet.getShort(a_intIndex));
		if (a_resultSet.wasNull())
			shtValue = null;
		return shtValue;
	}

	static public Double getDouble(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Double dblValue = new Double(a_resultSet.getDouble(a_intIndex));
		if (a_resultSet.wasNull())
			dblValue = null;
		return dblValue;
	}

	static public BigDecimal getBigDecimal(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		BigDecimal bdcValue = a_resultSet.getBigDecimal(a_intIndex);
		if (a_resultSet.wasNull())
			bdcValue = null;
		return bdcValue;
	}

	static public Byte getByte(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Byte bytValue = new Byte(a_resultSet.getByte(a_intIndex));
		if (a_resultSet.wasNull())
			bytValue = null;
		return bytValue;
	}

	static public String getClobAsString(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Clob clobValue = a_resultSet.getClob(a_intIndex);
		try {
			return clobValue.getSubString(1, (int) clobValue.length());
		} catch (NullPointerException ee) {
			return null;
		}
	}

	static public byte[] getBlobAsBytes(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Blob blobValue = a_resultSet.getBlob(a_intIndex);
		try {
			return blobValue.getBytes(1, (int) blobValue.length());
		} catch (NullPointerException ee) {
			return null;
		}
	}

	static public Boolean getBoolean(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Boolean oblnValue = new Boolean(a_resultSet.getBoolean(a_intIndex));
		if (a_resultSet.wasNull())
			oblnValue = null;
		return oblnValue;
	}

	static public Object getObject(ResultSet a_resultSet, int a_intIndex)
			throws SQLException {
		Object objValue = a_resultSet.getObject(a_intIndex);
		if (a_resultSet.wasNull())
			objValue = null;
		return objValue;
	}

	static public String getString(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		String strValue = a_callableStatement.getString(a_intIndex);
		if (a_callableStatement.wasNull())
			strValue = null;
		return strValue;
	}

	static public java.util.Date getDate(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		java.util.Date dtmValue = a_callableStatement.getDate(a_intIndex);
		if (a_callableStatement.wasNull())
			dtmValue = null;
		return dtmValue;
	}

	static public java.util.Date getTSDate(
			CallableStatement a_callableStatement, int a_intIndex)
			throws SQLException {
		java.sql.Timestamp tsValue = a_callableStatement
				.getTimestamp(a_intIndex);
		if (a_callableStatement.wasNull())
			return null;
		return new java.util.Date(tsValue.getTime() + tsValue.getNanos()
				/ 1000000);
	}

	static public Long getLong(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Long lngValue = new Long(a_callableStatement.getLong(a_intIndex));
		if (a_callableStatement.wasNull())
			lngValue = null;
		return lngValue;
	}

	static public Integer getInt(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		return getInteger(a_callableStatement, a_intIndex);
	}

	static public Integer getInteger(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Integer intValue = new Integer(a_callableStatement.getInt(a_intIndex));
		if (a_callableStatement.wasNull())
			intValue = null;
		return intValue;
	}

	static public Short getShort(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Short shtValue = new Short(a_callableStatement.getShort(a_intIndex));
		if (a_callableStatement.wasNull())
			shtValue = null;
		return shtValue;
	}

	static public Double getDouble(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Double dblValue = new Double(a_callableStatement.getDouble(a_intIndex));
		if (a_callableStatement.wasNull())
			dblValue = null;
		return dblValue;
	}

	static public BigDecimal getBigDecimal(
			CallableStatement a_callableStatement, int a_intIndex)
			throws SQLException {
		BigDecimal bdcValue = a_callableStatement.getBigDecimal(a_intIndex);
		if (a_callableStatement.wasNull())
			bdcValue = null;
		return bdcValue;
	}

	static public Byte getByte(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Byte bytValue = new Byte(a_callableStatement.getByte(a_intIndex));
		if (a_callableStatement.wasNull())
			bytValue = null;
		return bytValue;
	}

	static public String getClobAsString(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Clob clobValue = (Clob) a_callableStatement.getObject(a_intIndex);
		try {
			return clobValue.getSubString(1, (int) clobValue.length());
		} catch (NullPointerException ee) {
			return null;
		}
	}

	static public byte[] getBlobAsBytes(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Blob blobValue = a_callableStatement.getBlob(a_intIndex);
		try {
			return blobValue.getBytes(1, (int) blobValue.length());
		} catch (NullPointerException ee) {
			return null;
		}
	}

	static public Boolean getBoolean(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Boolean oblnValue = new Boolean(
				a_callableStatement.getBoolean(a_intIndex));
		if (a_callableStatement.wasNull())
			oblnValue = null;
		return oblnValue;
	}

	static public Object getObject(CallableStatement a_callableStatement,
			int a_intIndex) throws SQLException {
		Object objValue = a_callableStatement.getObject(a_intIndex);
		if (a_callableStatement.wasNull())
			objValue = null;
		return objValue;
	}

	static public void setString(PreparedStatement a_stmt, int a_intIndex,
			String a_str) throws SQLException {
		if (a_str == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.VARCHAR);
		} else {
			a_stmt.setString(a_intIndex, a_str);
		}
	}

	static public void setDate(PreparedStatement a_stmt, int a_intIndex,
			java.util.Date a_date) throws SQLException {
		if (a_date == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.DATE);
		} else {
			a_stmt.setDate(a_intIndex, new java.sql.Date(a_date.getTime()));
		}
	}

	static public void setTSDate(PreparedStatement a_stmt, int a_intIndex,
			java.util.Date a_date) throws SQLException {
		if (a_date == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.TIMESTAMP);
		} else {
			a_stmt.setTimestamp(a_intIndex,
					new java.sql.Timestamp(a_date.getTime()));
		}
	}

	static public void setLong(PreparedStatement a_stmt, int a_intIndex,
			Long a_lng) throws SQLException {
		if (a_lng == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.BIGINT);
		} else {
			a_stmt.setLong(a_intIndex, a_lng.longValue());
		}
	}

	static public void setInt(PreparedStatement a_stmt, int a_intIndex,
			Integer a_int) throws SQLException {
		setInteger(a_stmt, a_intIndex, a_int);
	}

	static public void setInteger(PreparedStatement a_stmt, int a_intIndex,
			Integer a_int) throws SQLException {
		if (a_int == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.INTEGER);
		} else {
			a_stmt.setInt(a_intIndex, a_int.intValue());
		}
	}

	static public void setShort(PreparedStatement a_stmt, int a_intIndex,
			Short a_sht) throws SQLException {
		if (a_sht == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.SMALLINT);
		} else {
			a_stmt.setShort(a_intIndex, a_sht.shortValue());
		}
	}

	static public void setBigDecimal(PreparedStatement a_stmt, int a_intIndex,
			BigDecimal a_bdc) throws SQLException {
		if (a_bdc == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.NUMERIC);
		} else {
			a_stmt.setBigDecimal(a_intIndex, a_bdc);
		}
	}

	static public void setDouble(PreparedStatement a_stmt, int a_intIndex,
			Double a_dbl) throws SQLException {
		if (a_dbl == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.DOUBLE);
		} else {
			a_stmt.setDouble(a_intIndex, a_dbl.doubleValue());
		}
	}

	static public void setByte(PreparedStatement a_stmt, int a_intIndex,
			Byte a_byt) throws SQLException {
		if (a_byt == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.TINYINT);
		} else {
			a_stmt.setByte(a_intIndex, a_byt.byteValue());
		}
	}

	static public void setBoolean(PreparedStatement a_stmt, int a_intIndex,
			Boolean a_obln) throws SQLException {
		if (a_obln == null) {
			a_stmt.setNull(a_intIndex, java.sql.Types.BIT);
		} else {
			a_stmt.setBoolean(a_intIndex, a_obln.booleanValue());
		}
	}

	static public void setObject(PreparedStatement a_stmt, int a_intIndex,
			Object a_obj, int a_intSqlType) throws SQLException {
		if (a_obj == null) {
			a_stmt.setNull(a_intIndex, a_intSqlType);
		} else {
			a_stmt.setObject(a_intIndex, a_obj, a_intSqlType);
		}
	}

	static public String makeSPCallStr(String a_strSPName, int a_intNumParams,
			boolean a_blnHasReturnVal) {
		// Pre-size string buffer for performance
		StringBuffer strbResult = new StringBuffer((a_intNumParams * 2) + // ?,?,?,
																			// ...
				a_strSPName.length() + 9 + // "{call ()}"
				(a_blnHasReturnVal ? 4 : 0) // "? = "
		);

		strbResult.append("{");
		if (a_blnHasReturnVal) {
			strbResult.append("? = ");
		}
		strbResult.append("call ");
		strbResult.append(a_strSPName);
		strbResult.append("(");
		for (int i = 0; i < a_intNumParams; i++) {
			strbResult.append(i == 0 ? "?" : ",?");
		}
		strbResult.append(")}");

		return strbResult.toString();
	}

	public static void logJDBCCleanupWarning(SQLException a_se) {
		// LoggerHelper.logSystemWarning(" DBUtil Error encountered while trying to clean up JDBC resources: "
		// + a_se);
		a_se.printStackTrace(System.err);
	}

	public static void closeDBObjects(Connection a_conn, ResultSet a_rs,
			Statement a_stmt) {
		try {
			if (a_rs != null)
				a_rs.close();
			if (a_stmt != null)
				a_stmt.close();
		} catch (SQLException se2) {
			logJDBCCleanupWarning(se2);
		}

		// Close a_conn in its own try block to make every possible effort to
		// close
		// the Connection object and prevent Connection object leakage
		try {
			if (a_conn != null)
				a_conn.close();
		} catch (SQLException se3) {
			logJDBCCleanupWarning(se3);
		}
	}

	public static void closeDBObjects(Connection a_conn, ResultSet a_rs,
			Statement a_stmt, boolean a_blnSuccess) throws Exception {
		try {
			if (a_conn != null) {
				if (a_blnSuccess) {
					a_conn.commit();
				} else {
					a_conn.rollback();
				}
			}
		} catch (SQLException se) {
			throw new Exception("error in commit or rollback", se);
		} finally {
			// Separate handling of db object closing from commit/rollback of
			// transactions
			try {
				if (a_rs != null)
					a_rs.close();
				if (a_stmt != null)
					a_stmt.close();
			} catch (SQLException se2) {
				logJDBCCleanupWarning(se2);
			}

			// Close a_conn in its own try block to make every possible effort
			// to close
			// the Connection object and prevent Connection object leakage
			try {
				if (a_conn != null)
					a_conn.close();
			} catch (SQLException se3) {
				logJDBCCleanupWarning(se3);
			}
		}
	}

	public static void closeJTADBObjects(Connection a_conn, ResultSet a_rs,
			Statement a_stmt1) throws Exception {
		try {
			if (a_rs != null)
				a_rs.close();
			if (a_stmt1 != null)
				a_stmt1.close();
		} catch (SQLException se2) {
			logJDBCCleanupWarning(se2);
		}

		// Close a_conn in its own try block to make every possible effort to
		// close
		// the Connection object and prevent Connection object leakage
		try {
			if (a_conn != null)
				a_conn.close();
		} catch (SQLException se3) {
			logJDBCCleanupWarning(se3);
		}
	}

	public static void closeDBObjects(Connection a_conn, ResultSet a_rs,
			Statement a_stmt1, Statement a_stmt2, boolean a_blnSuccess)
			throws Exception {
		try {
			if (a_conn != null) {
				if (a_blnSuccess) {
					a_conn.commit();
				} else {
					a_conn.rollback();
				}
			}
		} catch (SQLException se) {
			throw new Exception("error in commit or rollback", se);
		} finally {
			// Separate handling of db object closing from commit/rollback of
			// transactions
			try {
				if (a_rs != null)
					a_rs.close();
				if (a_stmt1 != null)
					a_stmt1.close();
				if (a_stmt2 != null)
					a_stmt2.close();
			} catch (SQLException se2) {
				logJDBCCleanupWarning(se2);
			}

			// Close a_conn in its own try block to make every possible effort
			// to close
			// the Connection object and prevent Connection object leakage
			try {
				if (a_conn != null)
					a_conn.close();
			} catch (SQLException se3) {
				logJDBCCleanupWarning(se3);
			}
		}
	}

	public static String escapeSingleQuotes(String strText) {
		if (strText.indexOf("'") < 0)
			return strText; // immediately exit if strText doesn't contain a
							// single quote

		StringBuffer strbTmp = new StringBuffer();
		char[] acharTmp = strText.toCharArray();
		for (int i = 0; i < acharTmp.length; i++) {
			if (acharTmp[i] == '\'') {
				strbTmp.append("''");
			} else {
				strbTmp.append(acharTmp[i]);
			}
		}
		return strbTmp.toString();
	}

	/*
	 * public static String makeSafeQLString ( String a_str ) { return
	 * EligibilityUtils.replace(a_str, "'", "''"); }
	 * 
	 * public static String makeSafeDQLString ( String a_str ) { return
	 * EligibilityUtils.replace(a_str, "/", "@"); }
	 * 
	 * public static String removeBackSlashDQL ( String a_str ) { return
	 * EligibilityUtils.replace(a_str, "@", "/"); }
	 */

	public static final String replace(String srcStr, String oldString,
			String newString) {
		if (srcStr == null) {
			return null;
		}
		int i = 0;
		if ((i = srcStr.indexOf(oldString, i)) >= 0) {
			char[] srcStr2 = srcStr.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(srcStr2.length);
			buf.append(srcStr2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = srcStr.indexOf(oldString, i)) > 0) {
				buf.append(srcStr2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(srcStr2, j, srcStr2.length - j);
			return buf.toString().trim();
		}
		return srcStr.trim();
	}

	public static String makeSafeLikeQLString(String a_str, char a_chrEscape) {
		StringBuffer strbResult = new StringBuffer();
		int intLen = a_str.length();

		for (int i = 0; i < intLen; i++) {
			char c = a_str.charAt(i);
			if (c == '\'') {
				strbResult.append("''");
			} else {
				if (c == '%' || c == '_' || c == a_chrEscape) {
					strbResult.append(a_chrEscape);
				}

				strbResult.append(c);
			}
		}

		return strbResult.toString();
	}

	public static String makeSafeLikeQLBindString(String a_str, char a_chrEscape) {
		StringBuffer strbResult = new StringBuffer();
		int intLen = a_str.length();

		for (int i = 0; i < intLen; i++) {
			char c = a_str.charAt(i);
			if (c == '%' || c == '_' || c == a_chrEscape) {
				strbResult.append(a_chrEscape);
			}

			strbResult.append(c);
		}

		return strbResult.toString();
	}

	/*
	 * public static List executeSql( String sql, String datasource ) {
	 * ResultSet rs = null; Statement st = null; Connection conn = null;
	 * ArrayList ret = new ArrayList(); try { Context ctx = new
	 * InitialContext();
	 * 
	 * 
	 * DataSource ds = (DataSource)ctx.lookup(datasource); conn =
	 * ds.getConnection(); st = conn.createStatement(); st.setFetchSize(2000);
	 * 
	 * 
	 * rs = st.executeQuery(sql);
	 * 
	 * ResultSetMetaData rsmd = rs.getMetaData(); com.tgix.trt.Utils.Timer timer
	 * = new com.tgix.trt.Utils.Timer(); int ncols = rsmd.getColumnCount();
	 * while (rs.next()) { HashMap record = new HashMap();
	 * //System.out.println("jhelloasdfasdfasdf"); for (int i=1; i<=ncols; i++)
	 * { String key = rsmd.getColumnName(i); Object val; val = rs.getObject(i);
	 * record.put(key,val); } ret.add( record ); } } catch (Exception e) {
	 * e.printStackTrace(); } finally { if ( rs != null) { try { rs.close(); }
	 * catch ( Exception e2) { e2.printStackTrace(); } } if ( st != null) { try
	 * { st.close(); } catch ( Exception e2) { e2.printStackTrace(); } } if (
	 * conn != null) { try { conn.close(); } catch ( Exception e2) {
	 * e2.printStackTrace(); } } } //log.info("ret size:" + ret.size()); return
	 * ret; }
	 */

	public static List executeSql(String sql, String datasource) {
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
			st.setFetchSize(2000);

			rs = st.executeQuery(sql);

			ResultSetMetaData rsmd = rs.getMetaData();
			//com.tgix.Utils.Timer timer = new com.tgix.Utils.Timer();
			int ncols = rsmd.getColumnCount();
			while (rs.next()) {
				HashMap record = new HashMap();
				// System.out.println("jhelloasdfasdfasdf");
				for (int i = 1; i <= ncols; i++) {
					String key = rsmd.getColumnName(i);
					Object val;
					val = rs.getObject(i);
					record.put(key, val);
				}
				ret.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
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
		// log.info("ret size:" + ret.size());
		return ret;
	}

	// added method to retrieve columns in a order
	public static List executeSql(String sql, String datasource, String order) {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		ArrayList ret = new ArrayList();
		try {
			/* Infosys - Weblogic to Jboss migration changes start here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(datasource); conn = ds.getConnection();
			 */
			conn = JdbcConnectionUtil.getJdbcConnection();
			/* Infosys - Weblogic to Jboss migration changes end here */
			com.tgix.Utils.Timer timer = new com.tgix.Utils.Timer();
			st = conn.createStatement();
			st.setFetchSize(2000);
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int ncols = rsmd.getColumnCount();
			while (rs.next()) {
				LinkedHashMap record = new LinkedHashMap();
				// System.out.println("jhelloasdfasdfasdf");
				for (int i = 1; i <= ncols; i++) {
					String key = rsmd.getColumnName(i);
					Object val;
					val = rs.getObject(i);
					record.put(key, val);
				}
				ret.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
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
		// log.info("ret size:" + ret.size());
		return ret;
	}

	public static int executeUpdate(String sql, String datasource) {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		ArrayList ret = new ArrayList();
		try {
			/* Infosys - Weblogic to Jboss migration changes start here */
			/*
			 * Context ctx = new InitialContext();
			 * 
			 * 
			 * DataSource ds = (DataSource)ctx.lookup(datasource); conn =
			 * ds.getConnection();
			 */
			conn = JdbcConnectionUtil.getJdbcConnection();
			/* Infosys - Weblogic to Jboss migration changes end here */
			st = conn.createStatement();

			st.setFetchSize(2000);

			return st.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
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
		// log.info("ret size:" + ret.size());
		return 0;
	}

}
