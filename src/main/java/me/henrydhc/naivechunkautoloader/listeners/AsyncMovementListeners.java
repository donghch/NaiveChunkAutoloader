package me.henrydhc.naivechunkautoloader.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import me.henrydhc.naivechunkautoloader.utils.Util;
/**
 * This listener is design to be used in folia servers
 */
public class AsyncMovementListeners implements Listener {

    @EventHandler
    public void onMinecartMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();

        if (!(vehicle instanceof Minecart)) {
            return;
        }

        if (!Bukkit.isOwnedByCurrentRegion(vehicle)) {
            return;
        }

        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        Chunk curChunk = fromLocation.getChunk();
        Chunk toChunk = toLocation.getChunk();

        if (Util.isSameChunk(curChunk, toChunk)) {
            return;
        }

        if (Bukkit.isOwnedByCurrentRegion(toLocation)) {
            
        } else {

        }
    }
    
}
