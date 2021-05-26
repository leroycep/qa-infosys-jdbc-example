package com.qa.jdbcexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {
        String db_url = System.getenv("JDBCExample_DB_URL");
        String db_username = System.getenv("JDBCExample_DB_USERNAME");
        String db_password = System.getenv("JDBCExample_DB_PASSWORD");

        if (db_url == null) {
            LOGGER.error("JDBCExample_DB_URL must be defined");
            System.exit(1); 
        }
        if (db_username == null) {
            LOGGER.error("JDBCExample_DB_USERNAME must be defined");
            System.exit(1); 
        }
        if (db_password == null) {
            LOGGER.error("JDBCExample_DB_PASSWORD must be defined");
            System.exit(1); 
        }

        Scanner scanner = new Scanner(System.in);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(db_url, db_username, db_password);

            boolean running = true;
            while (running) {
                String[] parts = scanner.nextLine().split(" ");

                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "readall":
                        readAllCustomers(connection);
                        break;
                    case "exit":
                    case "quit":
                        running = false;
                        break;
                }
            }
        } catch (NoSuchElementException e) {
        } catch (SQLException e) {
            LOGGER.error(e.getStackTrace());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            scanner.close();
        }
    }

    public static void readAllCustomers(Connection connection) throws SQLException
    {
        PreparedStatement pStatement = connection.prepareStatement("SELECT id, email, name FROM customer");

        System.out.println( "id" + "\t" + "email     " + "\t" + "name" );

        ResultSet results = pStatement.executeQuery();
        while (results.next()) {
            int id = results.getInt(1);
            String email = results.getString(2);
            String name = results.getString(3);

            System.out.println( id + "\t" + email + "\t" + name );
        }
    }
}
