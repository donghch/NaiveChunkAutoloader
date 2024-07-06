package me.henrydhc.naivechunkautoloader;

import org.bukkit.plugin.java.JavaPlugin;

import me.henrydhc.naivechunkautoloader.chunk.ChunkManager;
import me.henrydhc.naivechunkautoloader.listeners.MovementListeners;
import me.henrydhc.naivechunkautoloader.tasks.ChunkUnloadTask;

public class NaiveChunkAutoLoader extends JavaPlugin{

    private ChunkManager manager;

    @Override
    public void onEnable() {

        manager = new ChunkManager(1000 * 30, this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new MovementListeners(manager), this);

        // Start tasks
        new ChunkUnloadTask(manager).runTaskTimer(this, 0, 20 * 10);

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        manager.purgeAllChunks();
    }

    
}