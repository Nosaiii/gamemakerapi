package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import org.bukkit.entity.Player;

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

    public void addPlayer(Player player) {
        try {
            PlayerInfo existendPlayerInfo = modelService.getByPrimaryKey(PlayerInfo.class, player.getUniqueId().toString());
            if (existendPlayerInfo != null) {
                existendPlayerInfo.delete();
            }

            PlayerInfo playerInfo = new PlayerInfo(modelService, player, this);
            playerInfo.save();
        } catch (UndefinedModelException e) {
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return hasOne(Game.class);
    }

    public SpawnPoint getSpawnPoint() {
        return hasOne(SpawnPoint.class);
    }

    public Query<PlayerInfo> getPlayers() {
        return hasMany(PlayerInfo.class);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[]{
                "id"
        };
    }
}
