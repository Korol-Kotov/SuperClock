package me.korolkotov.superclock;

import me.korolkotov.superclock.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private final BoundingBox blocksNumbers;
    private final BlockFace direction;
    private final World world;
    private final String timezone;

    public Clock(BoundingBox blocksNumbers, BlockFace direction, World world, String timezone) {
        this.blocksNumbers = blocksNumbers;
        this.direction = direction;
        this.world = world;
        this.timezone = timezone;

        update();

        clocks.add(this);
    }

    public void update() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
        LocalDateTime time = zonedDateTime.toLocalDateTime();
        List<Character> chars = getCharList(time);
        List<List<Integer>> nums = getNums(format(time, ConfigManager.instance.get("config").getString("format")));
        List<Location> locations = new ArrayList<>();
        Location loc = blocksNumbers.getMax().toLocation(world);
        if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH)
            loc = loc.clone().add(-5 * -direction.getModZ(), 0, 0);
        else
            loc = loc.clone().add(0, 0, -5 * direction.getModX());
        for (List<Integer> slot : nums) {
            for (int i = 0; i < slot.size(); i++) {
                if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH) {
                    if (i == 0 && locations.size() != 0)
                        loc = loc.clone().add(3 * -direction.getModZ(), 0, 0);
                    else
                        loc = loc.clone().add(5 * -direction.getModZ(), 0, 0);
                    locations.add(loc);
                } else {
                    if (i == 0 && locations.size() != 0)
                        loc = loc.clone().add(0, 0, 3 * direction.getModX());
                    else
                        loc = loc.clone().add(0, 0, 5 * direction.getModX());
                    locations.add(loc);
                }
            }
            if (nums.indexOf(slot) != nums.size() - 1 && locations.size() < 8) {
                if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH)
                    loc = loc.clone().add(4 * -direction.getModZ(), 0, 0);
                else {
                    loc = loc.clone().add(0, 0, 4 * direction.getModX());
                }
                locations.add(loc);
            }
        }

        for (Location location : locations) {
            String chr = chars.get(locations.indexOf(location)).toString().replace(":", "dots");
            Location bgLoc = location.clone().add(direction.getModX(), direction.getModY(), direction.getModZ());
            if (chr.length() == 1 && Character.isDigit(chr.toCharArray()[0])) {
                ConfigManager.instance.setSchematic(bgLoc, "bg_num", direction);
                if (locations.indexOf(location) < locations.size() - 1 && location.distance(locations.get(locations.indexOf(location) + 1)) == 5D) {
                    if (direction == BlockFace.SOUTH || direction == BlockFace.NORTH)
                        bgLoc = bgLoc.clone().add(4 * -direction.getModZ(), 0, 0);
                    else
                        bgLoc = bgLoc.clone().add(0, 0, 4 * direction.getModX());
                    ConfigManager.instance.setSchematic(bgLoc, "bg_space", direction);
                }
            } else if (chr.equals("dots"))
                ConfigManager.instance.setSchematic(bgLoc, "bg_dots", direction);
            ConfigManager.instance.setSchematic(location, chr, direction);
        }
    }

    private List<Character> getCharList(LocalDateTime time) {
        List<Character> result = new ArrayList<>();
        String str = format(time, ConfigManager.instance.get("config").getString("format"));
        for (int i = 0; i < str.length(); i++) {
            result.add(str.charAt(i));
        }
        return result;
    }

    private String format(LocalDateTime time, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(time);
    }

    private List<List<Integer>> getNums(String str) {
        String[] strings = str.split(":");
        List<List<Integer>> result = new ArrayList<>();

        for (String numsStr : strings) {
            List<Integer> nums = new ArrayList<>();
            for (char num : numsStr.toCharArray()) {
                String numStr = String.valueOf(num);
                nums.add(Integer.valueOf(numStr));
            }
            result.add(nums);
        }

        return result;
    }
}
