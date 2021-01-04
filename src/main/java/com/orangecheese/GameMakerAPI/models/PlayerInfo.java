package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfo extends Model {
    public PlayerInfo(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public PlayerInfo(ModelService modelService, Player player, Team team) {
        super(modelService);

        createProperty("uuid", player.getUniqueId().toString());
        createProperty("team_id", team.getProperty("id").get());
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "uuid"
        };
    }
}