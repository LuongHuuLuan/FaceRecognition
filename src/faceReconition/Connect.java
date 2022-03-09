package faceReconition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private static Connect connect;
	private static Connection connection;

	private Connect() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String url = "jdbc:ucanaccess://Database\\Database.accdb;memory=true";
			connection = DriverManager.getConnection(url);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (connect != null) {
			return connection;
		} else {
			connect = new Connect();
		}
		return connection;
	}
	
	
	
	
	
	
	
	
	
	
	
}
