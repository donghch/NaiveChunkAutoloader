package me.henrydhc.naivechunkautoloader.listeners;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import me.henrydhc.naivechunkautoloader.chunk.ChunkManager;
import me.henrydhc.naivechunkautoloader.utils.Util;

/**
 * This class implements all vehicle movement related listeners
 */
public class MovementListeners implements Listener {

    private final ChunkManager manager;

    public MovementListeners(ChunkManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onMinecartMove(VehicleMoveEvent event) {

        Vehicle targetVehicle = event.getVehicle();

        if (!(targetVehicle instanceof Minecart)) {
            return;
        }

        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        Chunk fromChunk = fromLocation.getChunk();
        Chunk toChunk = toLocation.getChunk();

        if (Util.isSameChunk(fromChunk, toChunk)) {
            return;
        }

        // Update if this chunk is already managed by the plugin
        if (manager.hasChunk(toChunk)) {
            manager.updateChunkExpiryTime(toChunk);
            return;
        }

        // Now we know we are handling a minecart moving from a chunk to another one
        manager.addChunk(toChunk);
    }

}