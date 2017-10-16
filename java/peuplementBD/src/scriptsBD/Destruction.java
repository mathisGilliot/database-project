package scriptsBD;

import java.sql.*;

public class Destruction {
	static final String CONN_URL = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
	static final String USER = "gilliotm";
	static final String PASSWD = "gilliotm";

	public static void main(String[] args) {
		try {
			/* Enregistrement du driver Oracle */
			System.out.print("Loading Oracle driver... "); 
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("loaded");

			/* Établissement de la connexion */
			System.out.print("Connecting to the database... "); 
			Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			System.out.println("connected");
			conn.setAutoCommit(false);

			/* Suppression des tables */

			for(Entites e : Entites.values()){
				String STMT = "DROP TABLE " + e;
				/* Création de la requête */
				Statement stmt = conn.createStatement();

				/* Éxecution de la requête */
				ResultSet rset = stmt.executeQuery(STMT);

				/* Destruction */
				rset.close();
				stmt.close();
			}

			/* Fermeture */
			conn.commit();
			conn.close();
			System.out.println("Connection over");

		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
		}
	}
}