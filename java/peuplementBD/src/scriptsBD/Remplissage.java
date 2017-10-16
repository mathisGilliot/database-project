package scriptsBD;

import java.sql.*;

/* Script de remplissage de la BD */

public class Remplissage {
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
			String req[][] = new String[13][];
			
			/* Données */
			String adresses[] = {"imag", "laSalle", "flandrin"};

			/* Remplissage des Catégories */
			int nb_cat = 5;
			String cat[] = new String[nb_cat];
			
			/* Catégorie 1 : Voitures, 2h de location max, 2000€ de caution, 5€/h */
			cat[0] = "INSERT INTO Categories VALUES(0, 5, 2, 2000, 5)";

			/* Catégorie 2 : Vélos, 3h de location max, 250€ de caution, 1€/h de location */
			cat[1] = "INSERT INTO Categories VALUES(1, 1, 3, 250, 1)";

			/* Catégorie 3 : Vélos électriques, 2h de location max, 350€ de caution, 2€/h */
			cat[2] = "INSERT INTO Categories VALUES(2, 1, 2, 350, 2)";

			/* Catégorie 4 : Vélos à remorque, 2h de location max, 400€ de caution, 3€/h */
			cat[3] = "INSERT INTO Categories VALUES(3, 1, 2, 400, 3)";

			/* Catégorie 5 : Petits utilitaires, 2h de location max, 1000€ de caution, 5€/h */
			cat[4] = "INSERT INTO Categories VALUES(4, 2, 2, 1000, 5)";
			
			req[0] = cat;
			

			/* Remplissage des Véhicules */
			int nb_vehicules = 10;
			String veh[] = new String[nb_vehicules];

			for(int i = 0; i < nb_vehicules; i++){
				veh[i] = "INSERT INTO Vehicules VALUES (" + i + ", " + i%5  + ")";
				
			}
			req[1] = veh;
			
			/* Remplissage des Abonnés */
			int nb_abonnes = 4;
			String abo[] = new String[nb_abonnes];

			for(int i = 0; i < nb_abonnes; i++){
				int mois = (int)(Math.random() * 12) + 1;
				int jour = (int)(Math.random() * 28) + 1;
				int annee = (int)(Math.random() * 50) + 1950;
				String dateAlea = "TO_DATE('" + mois + "/" + jour + "/" + annee + "', 'MM/DD/YYYY')";
				abo[i] = "INSERT INTO Abonnes VALUES (" + i + ", " + "'nom" + i + "', " + "'prenom" + i + "', " + dateAlea + ", " + "'13100'" + ")";
			}
			req[2] = abo;
			
			/* Remplissage des Stations */
			int nb_stations = 3;
			String stations[] = new String[nb_stations];
			
			stations[0] = "INSERT INTO Stations VALUES ('" + adresses[0] + "', 'rue de la passerelle')";
			stations[1] = "INSERT INTO Stations VALUES ('" + adresses[1] + "', 'Stade des Alpes')";
			stations[2] = "INSERT INTO Stations VALUES ('" + adresses[2] + "', 'avenue jeanne darc')";

			req[3] = stations;

			/* Remplissage des Locations */
			int nb_loc = 4;
			String locations[] = new String[nb_loc];

			for(int i = 0; i < nb_loc-1; i++){
				int heure1 = (int)(Math.random()*24);
				int jour1 = (int)(Math.random()*28) + 1;
				int mois1 = (int)(Math.random()*12) + 1;
				int annee1 = 2016;
				
				int temps_location = (int)(Math.random()*24);
				
				int heure2 = heure1 + temps_location;
				int jour2 = jour1;
				if (heure2 >= 24){
					heure2 = heure2 % 24;
					jour2 ++;
				}
				int mois2 = mois1;
				if (jour2 >= 28){
					jour2 = jour2 % 28 + 1;
					mois2 ++;
				}
				int annee2 = annee1;
				if (mois2 > 12){
					mois2 = mois2 % 12 + 1;
					annee2 ++;
				}
				
				String dateAlea1 = "TO_DATE('" + mois1 + "/" + jour1 + "/" + annee1 + "', 'MM/DD/YYYY')";
				String dateAlea2 = "TO_DATE('" + mois2 + "/" + jour2 + "/" + annee2 + "', 'MM/DD/YYYY')";
				
				locations[i] = "INSERT INTO Locations VALUES (" + i + ", " + dateAlea1 + "," + dateAlea2 + ", " + heure1 + ", " + heure2 + ", " + i + ", '" + adresses[((i%5 + 2)/3 )] + "')";
			}
			locations[nb_loc-1] = "INSERT INTO Locations VALUES (3, TO_DATE('11/20/1994', 'MM/DD/YYYY'), NULL, 4, NULL, 9, '" + adresses[2] + "')";
			
			req[4] = locations;
			
			/* Remplissage des Forfaits */
			int nb_forfaits = 4;
			String forfaits[] = new String[nb_forfaits];
			
			for(int i = 0; i < nb_forfaits; i++){
				forfaits[i] = "INSERT INTO Forfaits VALUES (" + i + ")";
			}
			
			req[5] = forfaits;
			
			/* Remplissage des Limites */
			int nb_limites = 2;
			String limites[] = new String[nb_limites];
			
			limites[0] = "INSERT INTO Limites VALUES (0, 100, 3)";
			limites[1] = "INSERT INTO Limites VALUES (1, 200, 5)";

			req[6] = limites;
			
			/* Remplissage des Illimites */
			int nb_illimites = 2;
			String illimites[] = new String[nb_illimites];
			
			illimites[0] = "INSERT INTO Illimites VALUES (2, 2, TO_DATE('10/18/2016', 'MM/DD/YYYY'), 100, 0)";
			illimites[1] = "INSERT INTO Illimites VALUES (3, 4, TO_DATE('01/15/2011', 'MM/DD/YYYY'), 200, 10)";

			req[7] = illimites;
			
			/* Remplissage des EstGare */
			int nb_garees = 9;
			String EstGare[] = new String[nb_garees];
			
			for(int i = 0; i < nb_garees; i ++){
				 EstGare[i] = "INSERT INTO EstGare VALUES (" + i + ", '" + adresses[((i%5 + 2)/3 )] + "')";
			}

			req[8] = EstGare;
			
			/* Remplissage des TermineA */
			int nb_termines = 3;
			String termineA[] = new String[nb_termines];
			
			for(int i = 0; i < nb_termines; i ++){
				 termineA[i] = "INSERT INTO TermineA VALUES (" + i + ", '" + adresses[((i%5 + 2)/3 )] + "')";
			}

			req[9] = termineA;
			
			/* Remplissage des Accueils */
			int nb_accueils = 3*nb_stations;
			String Accueils[] = new String[nb_accueils];
			
			for(int i = 0; i < nb_stations; i++){
				for (int j = 0; j < 3; j++){
					Accueils[3*i+j] = "INSERT INTO Accueille VALUES ('" + adresses[i] + "', " + (i+j)%nb_cat + ", " + ((int)(Math.random()*19) +10) + ")";
				}
			}

			req[10] = Accueils;
			
			/* Remplissage des Reserve */
			int nb_reservations = 4;
			String reservations[] = new String[nb_reservations];
			
			for(int i = 0; i < nb_reservations; i ++){
				 reservations[i] = "INSERT INTO Reserve VALUES (" + i + ", " + i + ")";
			}

			req[11] = reservations;
			
			/* Remplissage des Souscriptions */
			int nb_souscriptions = 4;
			String souscriptions[] = new String[nb_souscriptions];
			
			for(int i = 0; i < nb_souscriptions-1; i ++){
				 souscriptions[i] = "INSERT INTO Souscrire VALUES (" + i + ", " + i + ", " + i + ")";
			}
			 souscriptions[3] = "INSERT INTO Souscrire VALUES (" + 3 + ", " + 3 + ", " + 4 + ")";

			req[12] = souscriptions;
			

			for(int i = 0; i < req.length; i ++){
				for(int j = 0; j < req[i].length; j++){
					System.out.println(req[i][j]);
					/* Création de la requête */
					Statement stmt = conn.createStatement();

					/* Exécution de la requête*/ 
					ResultSet rset = stmt.executeQuery(req[i][j]);

					/* Destruction */
					rset.close();
					stmt.close();
				}
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
