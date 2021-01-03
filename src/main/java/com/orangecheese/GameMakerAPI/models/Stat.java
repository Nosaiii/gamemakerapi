package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stat extends Model {
    public Stat(DatabaseConnection connection, ResultSet resultSet) throws SQLException {
        super(connection, resultSet);
    }

    public Stat(DatabaseConnection connection, String uuid) {
        super(connection);

        createProperty("uuid", uuid);
        createProperty("kills", 0);
        createProperty("coins", 0);
    }

    @Override
    public String getTableName() {
        return "stat";
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "uuid"
        };
    }
}
