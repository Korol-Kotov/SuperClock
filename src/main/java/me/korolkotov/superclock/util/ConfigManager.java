package me.korolkotov.superclock.util;

import me.korolkotov.superclock.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigManager {

    public final static ConfigManager instance = new ConfigManager();

    public Map<String, YamlConfiguration> configs = new HashMap<>();

    public void initResources() {
        String[] names = {"config", "messages", "chars/dots", "chars/bg_num", "chars/bg_space", "chars/bg_dots"};
        for (String name : names) {
            init(name);
        }
        for (int i = 0; i < 10; i++) {
            init("chars/" + i);
        }
    }

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

    public void createSchematic(Location location, String fileName, BlockFace face) {
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/chars/" + fileName + ".yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration schematic = YamlConfiguration.loadConfiguration(file);

        int maxXZ = 4;
        if (fileName.equalsIgnoreCase("dots") || fileName.equalsIgnoreCase("bg_dots"))
            maxXZ = 3;
        else if (fileName.equalsIgnoreCase("bg_space"))
            maxXZ = 1;
        for (int y = 0; y < 7; y++) {
            List<String> strings = new ArrayList<>();
            for (int xz = 0; xz < maxXZ; xz++) {
                Location locationSet = location;
                if (face == BlockFace.NORTH || face == BlockFace.SOUTH)
                    locationSet = locationSet.clone().add(xz * -face.getModZ(), -y, 0);
                if (face == BlockFace.WEST || face == BlockFace.EAST)
                    locationSet = locationSet.clone().add(0, -y, xz * face.getModX());
                Block block = locationSet.getBlock();
                strings.add(block.getType().name());
            }
            schematic.set(y + "", strings);
        }

        try {
            schematic.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public Map<String, List<Material>> getSchematic(String chr) {
        String fileName = "chars/" + chr + ".yml";
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName);

        if (!file.exists()) {
            return null;
        }

        YamlConfiguration schem = YamlConfiguration.loadConfiguration(file);
        Map<String, List<Material>> result = new HashMap<>();

        for (String num : schem.getKeys(false)) {
            List<String> materialNames = schem.getStringList(num);
            List<Material> materials = new ArrayList<>();

            for (String material : materialNames) {
                materials.add(Material.valueOf(material));
            }

            result.put(num, materials);
        }

        return result;
    }

    public List<String> getSavedSchematics() {
        List<String> schematics = new ArrayList<>();
        File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/chars");

        if (!file.exists()) {
            return schematics;
        }

        for (File schem : file.listFiles()) {
            schematics.add(schem.getName().replace(".yml", ""));
        }

        return schematics;
    }

    public boolean setSchematic(Location location, String chr, BlockFace blockFace) {
        Map<String, List<Material>> schem = getSchematic(chr);
        if (schem == null) {
            Main.getInstance().getLogger().warning(ChatUtil.format("&cСхематика &e" + chr + " &cне найдена!"));
            return false;
        }
        if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
            for (String num : schem.keySet()) {
                List<Material> materials = schem.get(num);
                for (int i = 0; i < materials.size(); i++) {
                    Material material = materials.get(i);
                    Location blockLoc = location.clone().add(i * -blockFace.getModZ(), -Integer.parseInt(num), 0);
                    Block block = blockLoc.getBlock();
                    if (block.getType() != material)
                        block.setType(material);
                }
            }

            return true;
        } else if (blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) {
            for (String num : schem.keySet()) {
                List<Material> materials = schem.get(num);
                for (int i = 0; i < materials.size(); i++) {
                    Material material = materials.get(i);
                    Location blockLoc = location.clone().add(0, -Integer.parseInt(num), i * blockFace.getModX());
                    Block block = blockLoc.getBlock();
                    if (block.getType() != material)
                        block.setType(material);
                }
            }

            return true;
        }

        return false;
    }
}
