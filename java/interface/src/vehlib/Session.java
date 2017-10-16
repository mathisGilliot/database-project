/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehlib;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author contet
 */
public class Session {

    private static final String CONN_URL = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
    private static final String USER = "gilliotm";
    private static final String PASSWD = "gilliotm";
    private static final String TMUVMSTMT = "select id_vehicule, AVG(heure_a+24*(TO_DATE(date_a, 'DD-MM-YYYY') - TO_DATE(date_d, 'DD-MM-YYYY')) - heure_d) AS Utilisation_Moyenne_en_heure FROM Locations WHERE TO_CHAR(date_a, 'MM') = ? AND TO_CHAR(date_d, 'MM') = ? GROUP BY id_vehicule";
    private static final String TMUCMSTMT = "select id_cat, AVG(heure_a+24*(TO_DATE(date_a, 'DD-MM-YYYY') - TO_DATE(date_d, 'DD-MM-YYYY')) - heure_d) AS Utilisation_Moyenne_en_heure FROM Vehicules NATURAL JOIN Locations WHERE TO_CHAR(date_d, 'MM') = ? GROUP BY id_cat";
    //private static final String MUCTASTMT = "SELECT id_cat, SUM(heure_a+24*(TO_DATE(date_a, 'DD-MM-YYYY') - TO_DATE(date_d, 'DD-MM-YYYY')) - heure_d) AS somme FROM Vehicules NATURAL JOIN Locations NATURAL JOIN Reserve NATURAL JOIN Abonnes WHERE TO_CHAR(Abonnes.date_naissance, 'YYYY') <= TO_CHAR(sysdate, 'YYYY')-20 AND TO_CHAR(Abonnes.date_naissance) >= TO_CHAR(sysdate, 'YYYY')-29 GROUP BY id_cat";
    private static final String MUCTASTMT = "SELECT id_cat, SUM( heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d) AS somme FROM Vehicules NATURAL JOIN Locations NATURAL JOIN RESERVE NATURAL JOIN ABONNES WHERE TO_CHAR(ABONNES.DATE_NAISSANCE,'YYYY')<=TO_CHAR(sysdate, 'YYYY')-? AND TO_CHAR(ABONNES.DATE_NAISSANCE,'YYYY')>=TO_CHAR(sysdate, 'YYYY')-? GROUP BY id_cat";
    private static final String TOSJSTMT = "SELECT nom_station, nbpark/total*100 AS Taux_occupation FROM (SELECT nom_station,SUM(nb_places_station) AS total FROM Accueille GROUP BY nom_station) NATURAL JOIN (SELECT nom_station, COUNT(*) AS nbpark FROM ESTGARE GROUP BY nom_station) WHERE nom_station=?"; // ? Pour le nom de la station voulu

    private static final String RECUPINFOVEHICSTMT = "SELECT id_vehicule, id_cat, duree_max, caution, prix_cat, heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d AS TempsLoc FROM Categories NATURAL JOIN Vehicules NATURAL JOIN Locations WHERE (Locations.id_location=?)";
    private static final String RECUPINFOABOSTMT = "SELECT num_carte, TO_CHAR(sysdate,'YYYY')-TO_CHAR(date_naissance, 'YYYY') FROM Reserve NATURAL JOIN Abonnes WHERE id_location=?";
    private static final String RECUPINFOFORFAITILLSTMT = "SELECT * FROM Illimites WHERE ID_FORFAIT=(SELECT id_forfait FROM Souscrire WHERE num_carte=? AND id_cat=?)";
    private static final String RECUPINFOFORFAITLIMSTMT = "SELECT * FROM Limites WHERE ID_FORFAIT=(SELECT id_forfait FROM Souscrire WHERE num_carte=? AND id_cat=?)";
    private static final int[] TRANCHESAGEMIN = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90};
    private static final int[] TRANCHESAGEMAX = {9, 19, 29, 39, 49, 59, 69, 79, 89, 99};

    private Connection connection;

    public Session() {
        try {
            // Enregistrement du driver Oracle
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");

            // Etablissement de la connection
            System.out.print("Connecting to the database... ");
            this.connection = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("connected");
            this.connection.setAutoCommit(false);

        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
    }

    public void facturationLocation(javax.swing.JLabel labDuree, javax.swing.JLabel labDureeMax, javax.swing.JLabel labPrixH, javax.swing.JLabel labReduc, javax.swing.JLabel labCaution, javax.swing.JLabel labForfait, javax.swing.JLabel labPrixTotal, int id_loc) {
        int prixLoc = 0;
        try {
            PreparedStatement pstmtloc = this.connection.prepareStatement(RECUPINFOVEHICSTMT);
            int idv = 0, id_cat = 0, duree_max = 0, caution = 0, prix_cat = 0, tpsloc = 0;
            boolean forfaitLim = false;
            pstmtloc.setInt(1, id_loc);
            try (ResultSet rset = pstmtloc.executeQuery()) {
                while (rset.next()) {
                    ResultSetMetaData rsetmd = rset.getMetaData();
                    idv = (rset.getString(1) == null) ? -1 : Integer.parseInt(rset.getString(1));
                    id_cat = (rset.getString(2) == null) ? -1 : Integer.parseInt(rset.getString(2));
                    duree_max = (rset.getString(3) == null) ? -1 : Integer.parseInt(rset.getString(3));
                    caution = (rset.getString(4) == null) ? -1 : Integer.parseInt(rset.getString(4));
                    prix_cat = (rset.getString(5) == null) ? -1 : Integer.parseInt(rset.getString(5));
                    tpsloc = (rset.getString(6) == null) ? -1 : Integer.parseInt(rset.getString(6));
                }
            }

            PreparedStatement pstmtabo = this.connection.prepareStatement(RECUPINFOABOSTMT);
            int num_carte = 0, age = 0;
            pstmtabo.setInt(1, id_loc);
            try (ResultSet rset = pstmtabo.executeQuery()) {
                while (rset.next()) {
                    ResultSetMetaData rsetmd = rset.getMetaData();
                    num_carte = (rset.getString(1) == null) ? -1 : Integer.parseInt(rset.getString(1));
                    age = (rset.getString(2) == null) ? -1 : Integer.parseInt(rset.getString(2));
                }
            }

            PreparedStatement pstmtforfaitill = this.connection.prepareStatement(RECUPINFOFORFAITILLSTMT);
            int id_forfait, duree, debut, prix_illim, remise;
            pstmtforfaitill.setInt(1, id_cat);
            pstmtforfaitill.setInt(2, num_carte);
            try (ResultSet rset = pstmtforfaitill.executeQuery()) {
                while (rset.next()) {
                    forfaitLim = false;
                    id_forfait = (rset.getString(1) == null) ? -1 : Integer.parseInt(rset.getString(1));
                    duree = (rset.getString(2) == null) ? -1 : Integer.parseInt(rset.getString(2));
                    prix_illim = (rset.getString(4) == null) ? -1 : Integer.parseInt(rset.getString(4));
                    remise = Integer.parseInt(rset.getString(5));
                }
            }

            PreparedStatement pstmtforfaitlim = this.connection.prepareStatement(RECUPINFOFORFAITLIMSTMT);
            int prix_lim, nb_loc_max;
            pstmtforfaitlim.setInt(1, id_cat);
            pstmtforfaitlim.setInt(2, num_carte);
            try (ResultSet rset = pstmtforfaitlim.executeQuery()) {
                while (rset.next()) {
                    forfaitLim = true;
                    //ResultSetMetaData rsetmd = rset.getMetaData();
                    prix_lim = (rset.getString(1) == null) ? -1 : Integer.parseInt(rset.getString(1));
                    nb_loc_max = (rset.getString(2) == null) ? -1 : Integer.parseInt(rset.getString(2));
                    //dumpResultSet(rset);
                }
            }
            if (!forfaitLim && tpsloc != -1) {
                prixLoc += (tpsloc - 1) * prix_cat;

                if (age < 25 || age > 65) {
                    prixLoc = prixLoc * 3 / 4;
                }
            } else {
                prixLoc = 0;
            }
            if (tpsloc > duree_max) {
                prixLoc += caution;
            }
            labDuree.setText((tpsloc == -1) ? "Non terminé" : "" + tpsloc);
            labDureeMax.setText("" + duree_max);
            labPrixH.setText("" + prix_cat);
            labReduc.setText(((age < 25 || age > 65) ? "25%" : "0%") + " (" + age + " ans)");
            labCaution.setText("" + caution);
            labForfait.setText((forfaitLim) ? "forfait limité" : "forfait illimité");
            labPrixTotal.setText("" + prixLoc);

        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    public void getTmuvm(javax.swing.JTable table, int mois) {
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(TMUVMSTMT);
            pstmt.setInt(1, mois);
            pstmt.setInt(2, mois);
            try (ResultSet rset = pstmt.executeQuery()) {
                this.chargerDonneesTableau(table, rset);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    public void getTmucm(javax.swing.JTable table, int mois) {
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(TMUCMSTMT);
            pstmt.setInt(1, mois);
            try (ResultSet rset = pstmt.executeQuery()) {
                this.chargerDonneesTableau(table, rset);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    public void getMucta(javax.swing.JTable table, int trancheAge) {
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(MUCTASTMT);
            pstmt.setInt(1, TRANCHESAGEMIN[trancheAge]);
            pstmt.setInt(2, TRANCHESAGEMAX[trancheAge]);
            try (ResultSet rset = pstmt.executeQuery()) {
                this.chargerDonneesTableau(table, rset);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    public void getTosj(javax.swing.JTable table, String nomStation) {
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(TOSJSTMT);
            pstmt.setString(1, nomStation);
            try (ResultSet rset = pstmt.executeQuery()) {
                this.chargerDonneesTableau(table, rset);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    public ArrayList recupNomsStations() {
        try {
            ArrayList noms = new ArrayList();
            PreparedStatement pstmt = this.connection.prepareStatement("SELECT nom_station FROM Stations");
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    noms.add(rset.getString(1));
                }
//                    dumpResultSet(rset);
            } catch (SQLException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }

            return noms;
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
    }

    public ArrayList recupIdLocations() {
        try {
            ArrayList noms = new ArrayList();
            PreparedStatement pstmt = this.connection.prepareStatement("SELECT id_location FROM Locations");
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    noms.add(rset.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
            return noms;
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
    }

    public static void chargerDonneesTableau(javax.swing.JTable table, ResultSet rset) {
        try {
            ResultSetMetaData rsetmd = rset.getMetaData();
            int nbColonnes = rsetmd.getColumnCount();
            Vector titre = new Vector();
            for (int k = 1; k <= nbColonnes; k++) {
                titre.add(rsetmd.getColumnName(k));
            }

            Vector valeurs = new Vector();
            Vector valeursInitiales = new Vector();
            DefaultTableModel tableModel = new DefaultTableModel(valeursInitiales, titre);

            while (rset.next()) {
                for (int j = 1; j <= nbColonnes; j++) {
                    valeurs.add(rset.getString(j));
                }
                tableModel.addRow(valeurs);
                valeurs = new Vector();
            }

            table.setModel(tableModel);

        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
    }

    private static void dumpResultSet(ResultSet rset) throws SQLException {
        ResultSetMetaData rsetmd = rset.getMetaData();
        int i = rsetmd.getColumnCount();
        for (int k = 1; k <= i; k++) {
            System.err.println("for dumRsultSet");
            System.out.print(rsetmd.getColumnName(k) + "\t");
        }
        System.out.println();

        while (rset.next()) {
            System.out.println("While dans dumpresult");
            for (int j = 1; j <= i; j++) {
                System.out.print(rset.getString(j) + "\t");
            }
            System.out.println();
        }
    }
}
