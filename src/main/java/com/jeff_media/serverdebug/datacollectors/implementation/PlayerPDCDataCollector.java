package com.jeff_media.serverdebug.datacollectors.implementation;

import com.jeff_media.serverdebug.PDCSerializer;
import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class PlayerPDCDataCollector extends YamlDataCollector {
    public PlayerPDCDataCollector(File directory) {
        super(directory, "playerPDCs");
    }

    @Override
    public void collectData() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            PersistentDataContainer pdc = player.getPersistentDataContainer();
            set(name, PDCSerializer.serialize(pdc));
        }
    }
}
