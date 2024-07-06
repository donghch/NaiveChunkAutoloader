package me.henrydhc.naivechunkautoloader.chunk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;

import me.henrydhc.naivechunkautoloader.utils.Util;

/**
 * ChunkManager
 */
public class ChunkManager {

    private final Map<Chunk, Long> chunkMap;
    private final long chunkLife;
    private final Logger logger;

    public ChunkManager(long chunkLife, JavaPlugin plugin) {
        chunkMap = new HashMap<>();
        this.chunkLife = chunkLife;
        this.logger = plugin.getLogger();
    }

    /**
     * Check if a chuck is loaded my chunk manager
     * @param chunk
     * @return
     */
    public boolean hasChunk(Chunk chunk) {
        for (Chunk chunk2 : chunkMap.keySet()) {
            if (Util.isSameChunk(chunk2, chunk)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load a new chunk and add that chunk into the manager
     * @param chunk Chunk to load
     * @return {True}
     */
    public boolean addChunk(Chunk chunk) {

        if (chunk.isForceLoaded()) {
            // Emm, it's already force loaded. Just don't intervene
            // Also, that means it may already in our list.
            return false;
        }
        chunk.setForceLoaded(true);
        chunkMap.putIfAbsent(chunk, new Date().getTime() + chunkLife);
        logger.info("Loaded a chunk");
        return true;
    }

    /**
     * Remove chunk 
     * @param chunk Chunk to remove
     */
    public void removeChunk(Chunk chunk) {
        if (!chunkMap.containsKey(chunk)) {
            return;
        }
        chunkMap.remove(chunk);
        chunk.setForceLoaded(false);
    }

    /**
     * Update chunk expiry time
     * @param chunk target chunk
     */
    public void updateChunkExpiryTime(Chunk chunk) {
        chunkMap.replace(chunk, new Date().getTime() + chunkLife);
    }

    /**
     * Remove expired chunks
     */
    public void purgeExpiredChunk() {
        Map<Chunk, Long> entryToRemove = new HashMap<>();
        long currentTime = new Date().getTime();

        for (Map.Entry<Chunk, Long> entry: chunkMap.entrySet()) {
            if (entry.getValue() < currentTime) {
                entryToRemove.put(entry.getKey(), entry.getValue());
            }
        }

        for (Chunk chunk : entryToRemove.keySet()) {
            removeChunk(chunk);
        }
        logger.info(String.format("Purged %d chunk(s)", entryToRemove.size()));
    }

    /**
     * Purge all force loaded chunks
     */
    public void purgeAllChunks() {
        for (Chunk chunk: chunkMap.keySet()) {
            chunk.setForceLoaded(false);
        }
    }

}