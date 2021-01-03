package com.orangecheese.GameMakerAPI.models.mappers;

import com.orangecheese.GameMakerAPI.models.Stat;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatMapper implements IModelMapper {
    @Override
    public Model Map(DatabaseConnection connection, ResultSet resultSet) throws SQLException {
        return new Stat(connection, resultSet);
    }
}