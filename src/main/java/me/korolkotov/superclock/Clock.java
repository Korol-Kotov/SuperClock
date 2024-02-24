package me.korolkotov.superclock;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Date;

public class Clock {

    private Date time;
    private Material background;
    private Material number;
    private Location firstPos;
    private Location secondPos;

    public Clock(Long time, Material background, Material number) {
        this.time = new Date(time);
        this.background = background;
        this.number = number;
    }


}
