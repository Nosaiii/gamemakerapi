package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatData extends Model {
    public StatData(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public StatData(ModelService modelService, String uuid) {
        super(modelService);

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
