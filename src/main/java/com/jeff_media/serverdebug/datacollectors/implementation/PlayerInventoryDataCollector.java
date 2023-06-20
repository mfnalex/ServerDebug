package com.jeff_media.serverdebug.datacollectors.implementation;

import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInventoryDataCollector extends YamlDataCollector {
    public PlayerInventoryDataCollector(File directory) {
        super(directory, "playerInventories");
    }

    @Override
    public void collectData() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            set(name + ".selected", player.getInventory().getItemInMainHand());
            set(name + ".inventory", player.getInventory().getContents());
            set(name + ".armor", player.getInventory().getArmorContents());
            set(name + ".offhand", player.getInventory().getItemInOffHand());
            set(name + ".enderchest", player.getEnderChest().getContents());
        }
    }
}
