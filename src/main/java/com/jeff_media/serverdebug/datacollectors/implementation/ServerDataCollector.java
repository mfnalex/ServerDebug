package com.jeff_media.serverdebug.datacollectors.implementation;

import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import java.io.File;
import org.bukkit.Bukkit;

public class ServerDataCollector extends YamlDataCollector {
    public ServerDataCollector(File directory) {
        super(directory, "server");
    }

    @Override
    public void collectData() {
        set("version", Bukkit.getVersion());
        set("bukkit-version", Bukkit.getBukkitVersion());
    }
}
