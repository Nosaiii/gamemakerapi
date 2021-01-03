package com.orangecheese.GameMakerAPI.models.mappers;

import com.orangecheese.GameMakerAPI.models.StatModel;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatMapper implements IModelMapper {
    @Override
    public Model Map(DatabaseConnection connection, String tableName, ResultSet resultSet) throws SQLException {
        return new StatModel(connection, tableName, resultSet);
    }
}