package edu.northeastern.cs5200;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

	// singleton instance of Connection 
	private static Connection conn = null;
	private java.sql.Connection connection = null;
	// Private connection 
	private Connection() {

	}
	
	public static Connection CreateConnection() throws SQLException, ClassNotFoundException {
		if (conn ==null) {
			conn=new Connection();
			conn.connection = getConnection();
		}
		return conn;
	}
	
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://cs5200-fall2018-johal.cf2r7gcghtgx.us-east-2.rds.amazonaws.com/ec_johal";
	private static final String USER = "johal_h";
	private static final String PASSWORD = "Guruanne2421";

	public static java.sql.Connection getConnection() throws ClassNotFoundException, SQLException {
    	Class.forName(DRIVER);
    	return  DriverManager.getConnection(URL, USER, PASSWORD);
	}
	public java.sql.Connection getConnectionInstance(){
		return connection;
	}
	
	public static void closeConnection(Connection conn) {
   	 try {
   		  (conn.connection).close();
   		  conn = null;
   	 } catch (SQLException e) {
   		 e.printStackTrace();
   	 }
	}
	
	
}
