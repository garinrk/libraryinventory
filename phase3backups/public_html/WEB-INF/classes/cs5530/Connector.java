package cs5530;

import java.sql.*;

public class Connector {
	public Connection con;
	public Statement stmt;
	public static String userName;
	public static String password;
	public Connector() throws Exception {
		try{
		 	 userName = "cs5530u18";
	   		 password = "f96qb5pr";
	        	String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
		        Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        		con = DriverManager.getConnection (url, userName, password);

			//DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        	//stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt = con.createStatement();
			//stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(Exception e) {
			System.err.println("Unable to open mysql jdbc connection. The error is as follows,\n");
            		System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public void closeConnection() throws Exception{
		con.close();
	}
	
 	public void closeStatement() throws Exception{
		stmt.close();
	}
}
