package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.models.enums.GameStatus;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Game extends Model {
    public Game(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public Game(ModelService modelService, boolean instance) {
        super(modelService);

        createProperty("name", "Game");
        createProperty("min_players", 2);
        createProperty("max_players", 18);
        createProperty("status", GameStatus.LOBBY.toString());

        if(instance) {
            createProperty("server_ip", "127.0.0.1");
            createProperty("server_port", 25565);
            createProperty("fallback_server_ip", "127.0.0.1");
            createProperty("fallback_server_port", 25565);
        }

        createProperty("lobby_world", "world");
        createProperty("lobby_x", 0);
        createProperty("lobby_y", 0);
        createProperty("lobby_z", 0);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}
