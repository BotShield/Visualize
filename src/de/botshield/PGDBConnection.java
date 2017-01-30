package de.botshield;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author jstrebel
 *
 */

public class PGDBConnection {
    private Connection db = null;

    /**
     * Establishes a DB connection to a PostgreSQL DB.
     *
     * @param strUser
     *            the username
     * @param strPW
     *            the password
     * @param strDB
     *            the DB name
     * @return 0 for success, -1 for failure
     */
    public int establishConnection(String strUser, String strPW, String strDB) {
        String url = "jdbc:postgresql://localhost/" + strDB;
        Statement st;

        try {
            Class.forName("org.postgresql.Driver");
            db = DriverManager.getConnection(url, strUser, strPW);

            st = db.createStatement();
            // ResultSet rs = st.executeQuery("SELECT * FROM \"T_TW_USERS\"");
            ResultSet rs = st.executeQuery("SELECT * FROM T_User");
            System.out.println(rs.toString());

            rs.close();
            st.close();

            db.setAutoCommit(false);

            return 0;
        } catch (Exception e) {
            System.err.println(e.toString());
            return -1;
        }
    }// establishConnection

    public int closeConnection() {
        try {
            db.close();
            return 0;
        } catch (Exception e) {
            System.err.println(e.toString());
            return -1;
        }
    }// closeConnection

}// class
