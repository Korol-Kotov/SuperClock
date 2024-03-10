package me.korolkotov.superclock.command;

import me.korolkotov.superclock.Clock;
import me.korolkotov.superclock.util.ChatUtil;
import me.korolkotov.superclock.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class SpawnClockCMD implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.sendConfigMessage(sender, "not_player");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("superclock.spawnclock")) {
            ChatUtil.sendConfigMessage(player, "not_enough_perms");
            return true;
        }

        BoundingBox numbersBlocks;

        Location playerLoc = player.getEyeLocation();
        Block center = player.getEyeLocation().clone().add(player.getEyeLocation().clone().getDirection().multiply(5)).getBlock();

        Block firstPos, lastPos;

        if (Math.abs(center.getZ() - playerLoc.getZ()) > Math.abs(center.getX() - playerLoc.getX())) {
            firstPos = new Location(center.getWorld(), center.getX() + 10, center.getY() + 3, center.getZ()).getBlock();
            lastPos = new Location(center.getWorld(), center.getX() - 10, center.getY() - 3, center.getZ()).getBlock();
        } else {
            firstPos = new Location(center.getWorld(), center.getX(), center.getY() + 3, center.getZ() + 10).getBlock();
            lastPos = new Location(center.getWorld(), center.getX(), center.getY() - 3, center.getZ() - 10).getBlock();
        }

        numbersBlocks = new BoundingBox(firstPos.getX(), firstPos.getY(), firstPos.getZ(), lastPos.getX(), lastPos.getY(), lastPos.getZ());

        String timezone = ConfigManager.instance.get("config").getString("time-zone");

        if (args.length >= 1)
            timezone = args[0];
        else if (timezone == null)
            timezone = TimeZone.getDefault().getID();

        new Clock(
                numbersBlocks,
                player.getFacing(),
                playerLoc.getWorld(),
                timezone
        );

        ChatUtil.sendConfigMessage(player, "clock_spawned");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return getEntered(args[0], Arrays.asList(TimeZone.getAvailableIDs()));
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
