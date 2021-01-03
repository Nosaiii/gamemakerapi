package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.exceptions.ModelNotSyncedWithDatabaseException;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Team extends Model {
    public Team(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public Team(ModelService modelService, String teamName, Game game) {
        super(modelService);

        createProperty("name", teamName);
        createProperty("game_id", game.getProperty("id").<Long>get());
    }

    public Game getGame() {
        return hasOne(Game.class);
    }

    public SpawnPoint getSpawnPoint() {
        return hasOne(SpawnPoint.class);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}
