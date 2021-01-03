package com.orangecheese.GameMakerAPI.orm.model;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IModelMapper {
    Model Map(DatabaseConnection connection, String tableName, ResultSet resultSet) throws SQLException;
}