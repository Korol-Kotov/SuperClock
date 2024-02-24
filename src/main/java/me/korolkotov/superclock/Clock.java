package me.korolkotov.superclock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Clock {

    private final Date date;
    private final BoundingBox blocksBackground;
    private final BoundingBox blocksNumbers;
    private final BlockFace direction;
    private final World world;

    public Clock(Long date, BoundingBox blocksBackground, BoundingBox blocksNumbers, BlockFace direction, World world) {
        this.date = new Date(date);
        this.blocksBackground = blocksBackground;
        this.blocksNumbers = blocksNumbers;
        this.direction = direction;
        this.world = world;
    }

    public void test() {
        fill(blocksBackground, Material.OBSIDIAN);
        fill(blocksNumbers, Material.REDSTONE_BLOCK);
    }

    private void fill(BoundingBox boundingBox, Material material) {
        Location min = boundingBox.getMin().toLocation(world);
        Location max = boundingBox.getMax().toLocation(world);
        for (int x = (int) max.getX(); x > min.getX(); x--) {
            for (int y = (int) max.getY(); y > min.getY(); y--) {
                for (int z = (int) max.getZ(); z > min.getX(); z--) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }
}
