package com.orangecheese.GameMakerAPI;

import com.orangecheese.GameMakerAPI.models.GameData;
import com.orangecheese.GameMakerAPI.models.SpawnPointData;
import com.orangecheese.GameMakerAPI.models.StatData;
import com.orangecheese.GameMakerAPI.models.mappers.GameMapper;
import com.orangecheese.GameMakerAPI.models.mappers.SpawnPointMapper;
import com.orangecheese.GameMakerAPI.models.mappers.StatMapper;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelRegistrationProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameMakerApiPlugin extends JavaPlugin {
    public ModelService registerModelService(DatabaseConnectionProperties connectionProperties) {
        ModelService modelService = new ModelService(connectionProperties);

        modelService.register(new ModelRegistrationProperties(GameData.class, new GameMapper()));
        modelService.register(new ModelRegistrationProperties(SpawnPointData.class, new SpawnPointMapper()));
        modelService.register(new ModelRegistrationProperties(StatData.class, new StatMapper()));

        return modelService;
    }
}