package me.korolkotov.superclock.command;

import me.korolkotov.superclock.Main;
import me.korolkotov.superclock.util.ChatUtil;
import me.korolkotov.superclock.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SchCMD implements TabExecutor {

    private final String[] schematicNames = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "dots", "bg_num", "bg_space", "bg_dots"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.sendConfigMessage(sender, "not_player");
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("superclock.setschematic")) {
            if (args.length == 0) {
                ChatUtil.sendConfigMessage(player, "not_enough_args");
                return true;
            }

            if (args[0].equalsIgnoreCase("place")) {
                if (args.length == 1) {
                    ChatUtil.sendConfigMessage(player, "not_enough_args");
                    return true;
                }

                Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(5));

                if (!ConfigManager.instance.setSchematic(location, args[1], player.getFacing())) {
                    ChatUtil.sendConfigMessage(player, "schematic_failed", Map.of("%name%", args[1]));
                    return true;
                }

                ChatUtil.sendConfigMessage(player, "schematic_placed", Map.of("%name%", args[1]));
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 1 || !Arrays.asList(schematicNames).contains(args[1])) {
                    ChatUtil.sendConfigMessage(player, "not_enough_args");
                    return true;
                }
                if (!Main.getInstance().getBlocks().containsKey(player.getUniqueId())) {
                    ChatUtil.sendConfigMessage(player, "not_selected");
                    return true;
                }
                Block block = Main.getInstance().getBlocks().get(player.getUniqueId());

                ConfigManager.instance.createSchematic(block.getLocation(), args[1], player.getFacing());

                ChatUtil.sendConfigMessage(player, "schematic_saved", Map.of("%name%", args[1]));
            }
        } else {
            ChatUtil.sendConfigMessage(player, "not_enough_perms");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return getEntered(args[0], List.of("place", "set"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("place"))
                return getEntered(args[1], ConfigManager.instance.getSavedSchematics());
            else
                return getEntered(args[1], Arrays.asList(schematicNames));
        }
        return null;
    }

    private List<String> getEntered(String arg, List<String> list) {
        if (arg.isBlank()) {
            return list;
        }

        List<String> result = new ArrayList<>();
        for (String str : list) {
            if (str.contains(arg)) {
                result.add(str);
            }
        }

        return result;
    }
}
