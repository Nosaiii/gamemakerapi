package com.orangecheese.GameMakerAPI;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameMakerApiPlugin extends JavaPlugin {
    public ModelService registerModelService(DatabaseConnectionProperties connectionProperties) {
        return new ModelService(connectionProperties);
    }
}