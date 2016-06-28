/*

 * Created on Jan 23, 2005

 *

 * TODO To change the template for this generated file go to

 * Window - Preferences - Java - Code Style - Code Templates

 */

package com.tgix.Utils;



import java.io.IOException;

import java.io.InputStream;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;

import java.util.PropertyResourceBundle;



import javax.sql.DataSource;



import oracle.jdbc.pool.OracleDataSource;



/**

 *  Reads in a Database.properties file with these properties:

 *  jdbcDriver=oracle.jdbc.driver.OracleDriver (example)

 *  jdbcUrl=

 *  user=

 *  password=

 *

 *  @author joe

 */

public class Database {



    private Database() {

    }



    private static String getString(PropertyResourceBundle b, String name)

            throws IOException {



        String s = b.getString(name);

        if (s == null) {

            throw new IOException("Database:getString count not find property"

                    + name);

        }

        return s;

    }



    /**

     * Used to get a JDBC database connection

     */

    public static Connection getConnection() throws IOException,

            ClassNotFoundException, SQLException {



        InputStream i = ClassLoader

                .getSystemResourceAsStream("Database.properties");



        if (i == null) {

            throw new IOException(

                    "Could not locate Database.properties in classpath");

        }



        PropertyResourceBundle b = new PropertyResourceBundle(i);

        // read the properties from the bundle

        String jdbcDriver = getString(b, "jdbcDriver");

        String jdbcUrl = getString(b, "jdbcUrl");

        String username = getString(b, "user");

        String password = getString(b, "password");



        // register the driver and return a connection

        Class.forName(jdbcDriver);

        return DriverManager.getConnection(jdbcUrl, username, password);

    }



    /**

     * Used to get an Oracle Datasource.

     * @return

     * @throws IOException

     * @throws SQLException

     */

    public static DataSource getOracleDataSource() throws IOException, SQLException {



        // create an input stream based on the property file

        InputStream i = ClassLoader

                .getSystemResourceAsStream("Database.properties");

        if (i == null)

            throw new IOException(

                    "Could not locate Database.properties in classpath");



        // create a property resource bundle based on the stream

        PropertyResourceBundle b = new PropertyResourceBundle(i);



        // read the properties from the bundle

        String jdbcUrl = getString(b, "jdbcUrl");

        String username = getString(b, "user");

        String password = getString(b, "password");



        // setup DataSource and return

        OracleDataSource ds = new OracleDataSource();

        ds.setURL(jdbcUrl);

        ds.setUser(username);

        ds.setPassword(password);

        return ds;

    }

	

	public static DataSource getOracleDataSource(String jdbcUrl, String username, String password) throws IOException, SQLException {



        // setup DataSource and return

        OracleDataSource ds = new OracleDataSource();

        ds.setURL(jdbcUrl);

        ds.setUser(username);

        ds.setPassword(password);

        return ds;



	}



}