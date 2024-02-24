package me.korolkotov.superclock;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Plugin " + getName() + " is enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin " + getName() + " is disabled!");
    }
}
