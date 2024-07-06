package me.henrydhc.naivechunkautoloader.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import me.henrydhc.naivechunkautoloader.chunk.ChunkManager;

public class ChunkUnloadTask extends BukkitRunnable {

    private final ChunkManager manager;

    public ChunkUnloadTask(ChunkManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.purgeExpiredChunk();
    }

}