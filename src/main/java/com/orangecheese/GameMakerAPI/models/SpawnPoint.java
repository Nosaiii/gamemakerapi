package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpawnPoint extends Model {
    public SpawnPoint(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public SpawnPoint(ModelService modelService, String worldName, double x, double y, double z, float yaw, float pitch) {
        super(modelService);

        createProperty("world", worldName);
        createProperty("x", x);
        createProperty("y", y);
        createProperty("z", z);
        createProperty("yaw", yaw);
        createProperty("pitch", pitch);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}