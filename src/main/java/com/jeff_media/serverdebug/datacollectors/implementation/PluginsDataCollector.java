package com.jeff_media.serverdebug.datacollectors;

import com.jeff_media.serverdebug.YamlDataCollector;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

public class PluginsDataCollector extends YamlDataCollector {
    public PluginsDataCollector(File directory) {
        super(directory, "plugins");
    }

    @Override
    public void collectData() {
        for(Plugin plugin : Arrays.stream(Bukkit.getPluginManager().getPlugins()).sorted(Comparator.comparing(Plugin::getName)).toArray(Plugin[]::new)) {
            String name = plugin.getName();
            set(name + ".version", plugin.getDescription().getVersion());
            set(name + ".enabled", plugin.isEnabled());

            List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers().stream().filter(worker -> worker.getOwner().equals(plugin)).collect(Collectors.toList());
            set(name + ".workers.count", workers.size());
            set(name + ".workers.ids", workers.stream().map(worker -> String.valueOf(worker.getTaskId())).collect(Collectors.toList()));

            List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks().stream().filter(task -> task.getOwner().equals(plugin)).collect(Collectors.toList());
            set(name + ".tasks.count", tasks.size());
            set(name + ".tasks.ids", tasks.stream().map(task -> String.valueOf(task.getTaskId())).collect(Collectors.toList()));
        }
    }

}
