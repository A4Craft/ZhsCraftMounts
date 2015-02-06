package com.thejavanerds.mounts;

import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

/**
 * Created by ZhsCraft on 2/5/2015.
 */
public class Mount implements Listener {

    public Main main;

    public Mount(Main main) {
        this.main = main;
    }

    @EventHandler
    public void Dismount(VehicleEnterEvent e) {
        Horse horse = (Horse) e.getVehicle();
        horse.remove();
    }
}
