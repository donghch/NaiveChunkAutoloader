package me.henrydhc.naivechunkautoloader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.henrydhc.naivechunkautoloader.chunk.ChunkManager;
import me.henrydhc.naivechunkautoloader.config.ConfigLoader;
import me.henrydhc.naivechunkautoloader.listeners.MovementListeners;
import me.henrydhc.naivechunkautoloader.tasks.ChunkUnloadTask;

public class NaiveChunkAutoLoader extends JavaPlugin{

    private ChunkManager manager;
    private boolean isFolia;

    @Override
    public void onEnable() {

        ConfigLoader.loadConfig(this);
        FileConfiguration config = ConfigLoader.config;

        manager = new ChunkManager(1000 * config.getInt("chunk-lifetime"), this);

        isFolia = isFolia();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new MovementListeners(manager), this);

        // Start tasks
        new ChunkUnloadTask(manager).runTaskTimer(this, 0, 20 * config.getInt("chunk-purge-period"));

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        manager.purgeAllChunks();
    }

    /**
     * Check if the current server core is folia
     * @return
     */
    private boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
}