package com.orangecheese.GameMakerAPI.orm.connection;

import com.orangecheese.GameMakerAPI.orm.exceptions.EmptyQueryResultException;

import java.sql.*;

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

        if(result == null) {
            throw new EmptyQueryResultException(query);
        }

        return result;
    }

    public int executeUpdateQuery(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}