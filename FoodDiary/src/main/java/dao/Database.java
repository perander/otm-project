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
    
    public void init() {
        List<String> lauseet = sqliteLauseet();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Food (id integer PRIMARY KEY, "
                + "name varchar(255), carb REAL, protein REAL, fat REAL);");
        lista.add("CREATE TABLE User (id integer PRIMARY KEY, name varchar(255));");
        lista.add("CREATE TABLE Entry (id integer PRIMARY KEY, "
                + "user_id integer, food_id integer, date Date, amount REAL, "
                + "FOREIGN KEY (user_id) REFERENCES User (id), "
                + "FOREIGN KEY (food_id) REFERENCES Food (id));");

        return lista;
    }
}
