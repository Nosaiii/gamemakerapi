package com.orangecheese.GameMakerAPI;

import com.orangecheese.GameMakerAPI.models.*;
import com.orangecheese.GameMakerAPI.models.mappers.*;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelRegistrationProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameMakerApiPlugin extends JavaPlugin {
    public ModelService registerModelService(DatabaseConnectionProperties connectionProperties) {
        ModelService modelService = new ModelService(connectionProperties);

        // Single
        modelService.register(new ModelRegistrationProperties(Game.class, new GameMapper()));
        modelService.register(new ModelRegistrationProperties(Team.class, new TeamMapper()));
        modelService.register(new ModelRegistrationProperties(SpawnPoint.class, new SpawnPointMapper()));
        modelService.register(new ModelRegistrationProperties(PlayerInfo.class, new PlayerInfoMapper()));
        modelService.register(new ModelRegistrationProperties(Stat.class, new StatMapper()));

        return modelService;
    }
}