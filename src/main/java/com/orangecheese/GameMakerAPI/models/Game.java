package com.orangecheese.GameMakerAPI.models;

import com.orangecheese.GameMakerAPI.models.enums.GameStatus;
import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Model {
    private List<Team> teams;

    public Game(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);

        teams = new ArrayList<>();
    }

    public Game(ModelService modelService, boolean serverInstance) {
        super(modelService);

        teams = new ArrayList<>();

        createProperty("name", "Game");
        createProperty("min_players", 2);
        createProperty("max_players", 18);
        createProperty("status", GameStatus.LOBBY.toString());

        if(serverInstance) {
            createProperty("server_ip", "127.0.0.1");
            createProperty("server_port", 25565);
            createProperty("fallback_server_ip", "127.0.0.1");
            createProperty("fallback_server_port", 25565);
        }

        createProperty("lobby_world", "world");
        createProperty("lobby_x", 0);
        createProperty("lobby_y", 0);
        createProperty("lobby_z", 0);
        createProperty("lobby_yaw", 0);
        createProperty("lobby_pitch", 0);
    }

    public Team addTeam(String teamName) {
        Team team = new Team(modelService, teamName, this);
        try {
            team.save();
        } catch (UndefinedModelException e) {
            e.printStackTrace();
        }

        teams.add(team);

        return team;
    }

    public Query<SpawnPoint> getSpawnPoints() {
        try {
            return modelService.get(SpawnPoint.class)
                    .where(sp -> sp.getProperty("game_id").<Long>get().equals(getProperty("id").<Long>get()));
        } catch (UndefinedModelException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Query<Team> getTeams() {
        return hasMany(Team.class);
    }

    @Override
    public String[] getPrimaryKeyColumns() {
        return new String[] {
                "id"
        };
    }
}
