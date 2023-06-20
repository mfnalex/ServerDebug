package com.jeff_media.serverdebug;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.serverdebug.datacollectors.DataCollector;
import com.jeff_media.serverdebug.datacollectors.FileDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.EventListenersDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.PlayerInventoryDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.PlayerPDCDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.PlayerPermissionsDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.PluginsDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.ServerDataCollector;
import com.jeff_media.serverdebug.datacollectors.implementation.WorldsDataCollector;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerDebug extends JavaPlugin {

    private final List<DataCollector> dataCollectors = new ArrayList<>();
    private final File tempDirectory;

    private static String version = "Undefined";

    {
        version = getDescription().getVersion();
        getDataFolder().mkdirs();
        tempDirectory = Utils.resetAndGetTempDirectory(getDataFolder());

        dataCollectors.add(new ServerDataCollector(tempDirectory));
        dataCollectors.add(new PluginsDataCollector(tempDirectory));
        dataCollectors.add(new WorldsDataCollector(tempDirectory));
        dataCollectors.add(new PlayerPermissionsDataCollector(tempDirectory));
        dataCollectors.add(new EventListenersDataCollector(tempDirectory));
        dataCollectors.add(new PlayerInventoryDataCollector(tempDirectory));
        dataCollectors.add(new PlayerPDCDataCollector(tempDirectory ));

        for(String fileName : new String[] {"bukkit.yml", "commands.yml", "ops.json", "permissions.yml", "spigot.yml", "paper.yml", "pufferfish.yml", "purpur.yml", "server.properties", "whitelist.json"}) {
            dataCollectors.add(new FileDataCollector(new File(tempDirectory, "serverConfigs"), new File(fileName)));
        }

        dataCollectors.add(new FileDataCollector(tempDirectory, new File(new File("logs"),"latest.log")));

    }

    public static String getVersion() {
        return version;
    }

    @Override
    public void onEnable() {
        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new DebugCommand(this));

    }

    public void createZip() {
        new ZipCreator(new File(getDataFolder(), "serverdebug.zip"), tempDirectory, dataCollectors).createZip();
    }
}
