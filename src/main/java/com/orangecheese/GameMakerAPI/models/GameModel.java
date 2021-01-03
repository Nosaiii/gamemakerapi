package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameModel extends Model {
    public GameModel(DatabaseConnection connection, String tableName, ResultSet resultSet) throws SQLException {
        super(connection, tableName, resultSet);
    }

    public GameModel(DatabaseConnection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}
