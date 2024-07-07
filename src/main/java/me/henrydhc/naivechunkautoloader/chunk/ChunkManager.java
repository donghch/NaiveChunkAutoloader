package me.henrydhc.naivechunkautoloader.chunk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;

import me.henrydhc.naivechunkautoloader.config.ConfigLoader;
import me.henrydhc.naivechunkautoloader.utils.Util;

/**
 * ChunkManager
 */
public class ChunkManager {

    private final Map<Chunk, Long> chunkMap;
    private final long chunkLife;
    private final Logger logger;
    private final Lock lock;

    public ChunkManager(long chunkLife, JavaPlugin plugin) {
        chunkMap = new HashMap<>();
        this.chunkLife = chunkLife;
        this.logger = plugin.getLogger();
        this.lock = new ReentrantLock();
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

        lock.lock();
        if (chunk.isForceLoaded()) {
            // Emm, it's already force loaded. Just don't intervene
            // Also, that means it may already in our list.
            lock.unlock();
            return false;
        }
        chunk.setForceLoaded(true);
        chunkMap.putIfAbsent(chunk, new Date().getTime() + chunkLife);

        if (ConfigLoader.config.getBoolean("detailed-msg"))
            logger.info("Loaded a chunk");
        lock.unlock();
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
        lock.lock();
        chunkMap.replace(chunk, new Date().getTime() + chunkLife);
        lock.unlock();
    }

    /**
     * Remove expired chunks
     * This function could be split into 2 individual funtions in 
     * the future:
     * - An async task to find all "purgable chunks"
     *      - This only reads data
     * - An sync task to remove those chunks
     */
    public void purgeExpiredChunk() {
        lock.lock();
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
        if (ConfigLoader.config.getBoolean("detailed-msg"))
            logger.info(String.format("Purged %d chunk(s)", entryToRemove.size()));
        lock.unlock();
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