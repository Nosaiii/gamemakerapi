package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatModel extends Model {
    public StatModel(DatabaseConnection connection, String tableName, ResultSet resultSet) throws SQLException {
        super(connection, tableName, resultSet);
    }

    public StatModel(DatabaseConnection connection, String tableName, String uuid) {
        super(connection, tableName);

        createProperty("uuid", uuid);
        createProperty("kills", 0);
        createProperty("coins", 0);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "uuid"
        };
    }
}
