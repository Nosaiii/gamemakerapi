package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Team extends Model {
    public Team(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public Team(ModelService modelService, String teamName) {
        super(modelService);

        createProperty("name", teamName);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}
