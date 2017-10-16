package scriptsBD;

import java.sql.*;

public class Creation {

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

			/* Requêtes de remplissage */

			/* Remplissage Véhicules */

			String creation[] = new String[13];

			creation[0] = "CREATE TABLE Categories (id_cat INT PRIMARY KEY, nb_places_veh INT, "
					+ " CHECK (nb_places_veh>0), duree_max INT CONSTRAINT DureePositive CHECK (duree_max>0),"
					+ " caution INT CONSTRAINT cautionPositive CHECK (caution>0),"
					+ " prix_cat INT CONSTRAINT prixCatPositif CHECK (prix_cat>0))";

			creation[1] = "CREATE TABLE Vehicules (id_vehicule INT PRIMARY KEY,"
					+ " id_cat INT REFERENCES Categories)";

			creation[2] = "Create Table Stations (  "
					+ "nom_station VARCHAR(50) PRIMARY KEY,"
					+ " adresse VARCHAR(255))";

			creation[3] = "CREATE TABLE Locations (id_location INT PRIMARY KEY,"
					+ " date_d DATE, date_a DATE, "
					+ " heure_d INT CONSTRAINT HeureExistenteD check ((heure_d >= 0) AND (heure_d < 24)),"
					+ " heure_a INT CONSTRAINT HeureExistenteA check ((heure_a >= 0) AND (heure_a < 24)),"
					+ " id_vehicule INT REFERENCES Vehicules,"
					+ " station_d VARCHAR(50) REFERENCES Stations)";

			creation[4] = "Create Table Abonnes (num_carte INT PRIMARY KEY,"
					+ " nom_abonne VARCHAR(50), prenom VARCHAR(50),"
					+ " date_naissance DATE, adresse VARCHAR(50))";

			creation[5] = "CREATE TABLE Forfaits (id_forfait INT PRIMARY KEY)";

			creation[6] = "CREATE TABLE Illimites (id_forfait INT not NULL,"
					+ " duree INT, debut DATE, prix_illim INT, remise INT,"
					+ " CONSTRAINT uniq_id FOREIGN KEY (id_forfait)"
					+ " REFERENCES Forfaits(id_forfait))";

			creation[7] = "CREATE TABLE Limites (id_forfait INT not NULL,"
					+ " prix_lim INT, Nb_loc_max INT, CONSTRAINT uniq_id2"
					+ " FOREIGN KEY (id_forfait)"
					+ " REFERENCES Forfaits(id_forfait))";

			creation[8] = "CREATE TABLE ESTGARE (id_vehicule REFERENCES Vehicules,"
					+ " nom_station varchar(50) REFERENCES Stations,"
					+ " CONSTRAINT KestGare PRIMARY KEY(id_vehicule, nom_station))";

			creation[9] = "CREATE TABLE TERMINEA (id_location INT REFERENCES Locations,"
					+ " nom_station VARCHAR(50) REFERENCES Stations,"
					+ "CONSTRAINT Ktermine PRIMARY KEY(id_location, nom_station))";

			creation[10] = "CREATE TABLE Reserve (id_location INT REFERENCES Locations,"
					+ " num_carte INT REFERENCES Abonnes,"
					+ " CONSTRAINT Kreservation PRIMARY KEY(id_location, num_carte))";

			creation[11] = "CREATE TABLE Accueille ("
					+ " nom_station VARCHAR(50) REFERENCES Stations,"
					+ " id_cat INT REFERENCES Categories,"
					+ " nb_places_station INT constraint NbPlacesStationPositif check (nb_places_station>0),"
					+ " CONSTRAINT Kaccueil PRIMARY KEY(nom_station, id_cat))";	

			creation[12] = "CREATE TABLE SOUSCRIRE (num_carte INT REFERENCES Abonnes,"
					+ "id_forfait INT REFERENCES Forfaits,"
					+ "id_cat INT REFERENCES Categories,"
					+ "CONSTRAINT Ksouscription PRIMARY KEY(num_carte, id_forfait, id_cat)) ";

			for(int i = 0; i < 13; i++){

				String STMT = creation[i];
				/* Création de la requete */
				Statement stmt = conn.createStatement();

				/* Exécution de la requête */
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