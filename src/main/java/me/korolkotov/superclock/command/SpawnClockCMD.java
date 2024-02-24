package me.korolkotov.superclock.command;

import me.korolkotov.superclock.util.ChatUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.List;

public class SpawnClockCMD implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.sendConfigMessage(sender, "not_player");
            return true;
        }

        Player player = (Player) sender;
        Block
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
