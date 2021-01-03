package com.orangecheese.GameMakerAPI;

import com.orangecheese.GameMakerAPI.models.Game;
import com.orangecheese.GameMakerAPI.models.Stat;
import com.orangecheese.GameMakerAPI.models.mappers.GameMapper;
import com.orangecheese.GameMakerAPI.models.mappers.StatMapper;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelRegistrationProperties;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

public class Main extends GameMakerApiPlugin {
    public void onEnable() {
        getLogger().info("GameMakerAPI enabled!");

        ModelService modelService = registerModelService(new DatabaseConnectionProperties("localhost", 3306, "gamemakerapitest", "root", "PixelKaasNL1"));
        modelService.register(new ModelRegistrationProperties(Stat.class, new StatMapper()));
        modelService.register(new ModelRegistrationProperties(Game.class, new GameMapper()));
    }

    public void onDisable() {
        getLogger().info("GameMakerAPI disabled!");
    }
}