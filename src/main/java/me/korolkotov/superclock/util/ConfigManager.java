package me.korolkotov.superclock.util;

import me.korolkotov.superclock.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public final static ConfigManager instance = new ConfigManager();

    public Map<String, YamlConfiguration> configs = new HashMap<>();

    public void init(String fileName) {
        fileName = fileName + ".yml";

        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName);

        if (!file.exists()) {
            Main.getInstance().saveResource(fileName, false);
        }

        configs.put(fileName, YamlConfiguration.loadConfiguration(file));
    }

    public YamlConfiguration get(String fileName) {
        if (!configs.containsKey(fileName + ".yml"))
            init(fileName);

        return configs.get(fileName + ".yml");
    }

    public void setSchematic(Location location, String fileName) {
        fileName = "chars/" + fileName + ".yml";
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration schematic = YamlConfiguration.loadConfiguration(file);

        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 4; x++) {
                List<String> strings = new ArrayList<>();
                if (schematic.contains(x + "")) {
                    strings = schematic.getStringList(x + "");
                }
                Block block = new Location(location.getWorld(), location.getX() + x, location.getY() - y, location.getZ()).getBlock();
                strings.add(block.getType().name());
                schematic.set(x + "", strings);
            }
        }

        try {
            schematic.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
