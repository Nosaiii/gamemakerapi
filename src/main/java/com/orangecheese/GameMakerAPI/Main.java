package com.orangecheese.GameMakerAPI;

public class Main extends GameMakerApiPlugin {
    public void onEnable() {
        getLogger().info("GameMakerAPI enabled!");
    }

    public void onDisable() {
        getLogger().info("GameMakerAPI disabled!");
    }
}