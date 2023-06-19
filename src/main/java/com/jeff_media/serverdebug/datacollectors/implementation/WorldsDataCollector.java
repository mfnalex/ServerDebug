package com.jeff_media.serverdebug.datacollectors;

import com.jeff_media.serverdebug.YamlDataCollector;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldsDataCollector extends YamlDataCollector {
    public WorldsDataCollector(File directory) {
        super(directory, "worlds");
    }

    @Override
    public void collectData() {
        for(World world : Bukkit.getWorlds()) {
            String name = world.getName();
            String uuid = world.getUID().toString();
            String difficulty = world.getDifficulty().name();
            String environment = world.getEnvironment().name();
            set(name + ".uuid", uuid);
            set(name + ".difficulty", difficulty);
            set(name + ".environment", environment);
            for(String gamerule : world.getGameRules()) {
                set(name + ".gamerules." + gamerule, world.getGameRuleValue(gamerule));
            }
            set(name + ".seed", world.getSeed());
            set(name + ".pvp", world.getPVP());
            set(name + ".keepSpawnInMemory", world.getKeepSpawnInMemory());
            set(name + ".ticksPerAnimalSpawns", world.getTicksPerAnimalSpawns());
            set(name + ".ticksPerMonsterSpawns", world.getTicksPerMonsterSpawns());
            set(name + ".fullTime", world.getFullTime());
            set(name + ".time", world.getTime());
            set(name + ".weatherDuration", world.getWeatherDuration());
            set(name + ".thunderDuration", world.getThunderDuration());
            set(name + ".storm", world.hasStorm());
            set(name + ".thundering", world.isThundering());
            set(name + ".autoSave", world.isAutoSave());
            set(name + ".spawnLocation", world.getSpawnLocation());
            set(name + ".worldBorder", world.getWorldBorder());
            set(name + ".seaLevel", world.getSeaLevel());
        }
    }
}
