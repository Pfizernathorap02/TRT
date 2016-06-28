package com.pfizer.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.pfizer.webapp.AppConst;

import oracle.jdbc.driver.OracleDriver;

public class JdbcConnectionUtil {

	private static Connection conn = null;

	public static Connection getJdbcConnection() {

		try {
			
		
			
			Context ctx = new InitialContext(); 
			DataSource ds =(DataSource)ctx.lookup(AppConst.APP_DATASOURCE); 
			conn = ds.getConnection();
			
			
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return conn;
	}

	public static void closeJdbcConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
