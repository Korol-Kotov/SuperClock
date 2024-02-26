package me.korolkotov.superclock.event;

import me.korolkotov.superclock.Main;
import me.korolkotov.superclock.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class StickEvent implements Listener {

    @EventHandler
    private void onBlockClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.isOp()) {
                if (player.getInventory().getItemInMainHand().getType() == Material.STICK) {
                    event.setCancelled(true);
                    Main.getInstance().setPlayerBlock(player, event.getClickedBlock());
                    String locStr = "(" + event.getClickedBlock().getX() + ", " + event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ() + ")";
                    ChatUtil.sendConfigMessage(
                            player,
                            "position_selected",
                            Map.of("%location%", locStr)
                    );
                }
            }
        }
    }
}
