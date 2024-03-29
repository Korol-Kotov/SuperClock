package me.korolkotov.superclock;

import me.korolkotov.superclock.command.SchCMD;
import me.korolkotov.superclock.command.SpawnClockCMD;
import me.korolkotov.superclock.event.StickEvent;
import me.korolkotov.superclock.util.ConfigManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;

    private static final Map<UUID, Block> blocks = new HashMap<>();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("spawnclock").setExecutor(new SpawnClockCMD());
        getCommand("spawnclock").setTabCompleter(new SpawnClockCMD());

        getCommand("sch").setExecutor(new SchCMD());
        getCommand("sch").setTabCompleter(new SchCMD());

        getServer().getPluginManager().registerEvents(new StickEvent(), this);

        ConfigManager.instance.initResources();
        Clock.startScheduler();

        getLogger().info("Plugin " + getName() + " enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin " + getName() + " disabled!");
    }

    public Map<UUID, Block> getBlocks() {
        return blocks;
    }

    public void setPlayerBlock(Player player, Block block) {
        blocks.put(player.getUniqueId(), block);
    }
}
