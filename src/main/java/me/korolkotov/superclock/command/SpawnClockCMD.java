package me.korolkotov.superclock.command;

import me.korolkotov.superclock.Clock;
import me.korolkotov.superclock.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.List;

public class SpawnClockCMD implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.sendConfigMessage(sender, "not_player");
            return true;
        }

        Player player = (Player) sender;

        BoundingBox backgroundBlocks = null;
        BoundingBox numbersBlocks = null;

        Location playerLoc = player.getEyeLocation();
        Block center = player.getEyeLocation().clone().add(player.getEyeLocation().clone().getDirection().multiply(5)).getBlock();
        Block precenter = player.getEyeLocation().clone().add(player.getEyeLocation().clone().getDirection().multiply(4)).getBlock();

        if (Math.abs(center.getZ() - playerLoc.getZ()) > Math.abs(center.getX() - playerLoc.getX())) {
            int addZ;
            Block firstPos = new Location(center.getWorld(), center.getX() + 10, center.getY() + 3, center.getZ()).getBlock();
            Block lastPos = new Location(center.getWorld(), center.getX() - 10, center.getY() - 3, center.getZ()).getBlock();
            numbersBlocks = new BoundingBox(firstPos.getX(), firstPos.getY(), firstPos.getZ(), lastPos.getX(), lastPos.getY(), lastPos.getZ());
            if (center.getZ() - playerLoc.getZ() < 0)
                addZ = 1;
            else
                addZ = -1;
            firstPos = firstPos.getLocation().add(0, 0, addZ).getBlock();
            lastPos = lastPos.getLocation().add(0, 0, addZ).getBlock();
            backgroundBlocks = new BoundingBox(firstPos.getX(), firstPos.getY(), firstPos.getZ(), lastPos.getX(), lastPos.getY(), lastPos.getZ());
        } else {
            int addX;
            Block firstPos = new Location(center.getWorld(), center.getX(), center.getY() + 3, center.getZ() + 10).getBlock();
            Block lastPos = new Location(center.getWorld(), center.getX(), center.getY() - 3, center.getZ() - 10).getBlock();
            numbersBlocks = new BoundingBox(firstPos.getX(), firstPos.getY(), firstPos.getZ(), lastPos.getX(), lastPos.getY(), lastPos.getZ());
            if (center.getX() - playerLoc.getX() < 0)
                addX = 1;
            else
                addX = -1;
            firstPos = firstPos.getLocation().add(addX, 0, 0).getBlock();
            lastPos = lastPos.getLocation().add(addX, 0, 0).getBlock();
            backgroundBlocks = new BoundingBox(firstPos.getX(), firstPos.getY(), firstPos.getZ(), lastPos.getX(), lastPos.getY(), lastPos.getZ());
        }

        Clock clock = new Clock(
                System.currentTimeMillis(),
                backgroundBlocks,
                numbersBlocks,
                center.getFace(precenter),
                playerLoc.getWorld()
        );

        clock.test();

        ChatUtil.sendMessage(player, "test");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
