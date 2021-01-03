package com.orangecheese.GameMakerAPI.orm.connection;

import com.orangecheese.GameMakerAPI.orm.exceptions.EmptyQueryResultException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection(DatabaseConnectionProperties properties) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String uri = "jdbc:mysql://" + properties.getHost() + ":" + properties.getPort() + "/" + properties.getDatabase() + "?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection(uri, properties.getUsername(), properties.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) throws EmptyQueryResultException {
        ResultSet result = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new EmptyQueryResultException(query);
        }

        return result;
    }

    public Object[] executeUpdateQuery(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            ResultSetMetaData resultSetMetaData = generatedKeys.getMetaData();

            if (generatedKeys.next()) {
                int columnAmount = resultSetMetaData.getColumnCount();
                Object[] keyValues = new Object[columnAmount];

                for (int i = 1; i <= columnAmount; i++) {
                    keyValues[i - 1] = generatedKeys.getObject(i);
                }

                return keyValues;
            } else {
                throw new SQLException("An error occured trying to insert data to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Object[]{};
    }
}