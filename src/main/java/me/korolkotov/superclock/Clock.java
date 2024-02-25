package me.korolkotov.superclock;

import me.korolkotov.superclock.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class Clock {

    private static final List<Clock> clocks = new ArrayList<>();

    public static void startScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Clock clock : clocks) {
                    clock.update();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    private final BoundingBox blocksBackground;
    private final BoundingBox blocksNumbers;
    private final BlockFace direction;
    private final World world;

    private final List<Location> locations;

    public Clock(BoundingBox blocksBackground, BoundingBox blocksNumbers, BlockFace direction, World world) {
        this.blocksBackground = blocksBackground;
        this.blocksNumbers = blocksNumbers;
        this.direction = direction;
        this.world = world;

        this.locations = new ArrayList<>();
        Location start = blocksNumbers.getMin().toLocation(world).add(0, 4, 0);
        locations.add(start.clone());

        int[] multipliers = {5, 4, 3, 5};

        for (int multiplier : multipliers) {
            if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH) {
                start = start.clone().add(multiplier * -direction.getModZ(), 0, 0);
            } else {
                start = start.clone().add(0, 0, multiplier * direction.getModX());
            }
            locations.add(start.clone());
        }

        update();

        clocks.add(this);
    }

    public void update() {
        Calendar time = Calendar.getInstance();
        List<Character> chars = getCharList(time);
        for (Location location : locations) {
            String chr = chars.get(locations.indexOf(location)).toString().replace(":", "dots");
            ConfigManager.instance.setSchematic(location, chr, direction);
        }
    }

    private List<Character> getCharList(Calendar calendar) {
        List<Character> result = new ArrayList<>();
        String str = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        for (int i = 0; i < str.length(); i++) {
            result.add(str.charAt(i));
        }
        return result;
    }
}
