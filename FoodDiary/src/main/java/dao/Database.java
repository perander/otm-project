package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() throws SQLException {
        List<String> stats = initStatements();

        Connection conn = getConnection();
        Statement st = conn.createStatement();

        for (String stat : stats) {
            st.executeUpdate(stat);
        }

    }

    public void kill() throws SQLException {
        List<String> stats = killStatements();

        Connection conn = getConnection();
        Statement st = conn.createStatement();

        for (String stat : stats) {
            st.executeUpdate(stat);
        }

        conn.close();
    }

    private List<String> initStatements() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE IF NOT EXISTS Food (id integer PRIMARY KEY, "
                + "userId integer, name varchar(255), carb REAL, protein REAL, fat REAL, amount REAL, date Date);");
        lista.add("CREATE TABLE IF NOT EXISTS User (id integer PRIMARY KEY, name varchar(255), password varchar(255));");

        return lista;
    }

    private List<String> killStatements() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("DROP TABLE Food");
        lista.add("DROP TABLE User");

        return lista;
    }
}
