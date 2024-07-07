package me.henrydhc.naivechunkautoloader.config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigLoader {
    
    public static FileConfiguration config;

    public static void loadConfig(Plugin plugin) {

        // Check file
        File file = new File("plugins/NaiveChunkAutoLoader/config.yml");
        if (!file.isFile()) {
            plugin.saveDefaultConfig();
        }

        config = plugin.getConfig();
        return;

    }

}
