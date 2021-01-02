package com.orangecheese.GameMakerAPI;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    public void onEnable() {
        getLogger().info("GameMakerAPI enabled!");
    }

    public void onDisable() {
        getLogger().info("GameMakerAPI disabled!");
    }
}