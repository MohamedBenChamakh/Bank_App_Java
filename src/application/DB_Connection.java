package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connection {

	public Connection getConnection() throws SQLException {
		String dbName="jdbc:mysql://localhost:3306/AppBancaire?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String username="root";
		String password="";
		Connection connexion = null;
		connexion= DriverManager.getConnection(dbName,username,password);
		return connexion;
		
	}
}
