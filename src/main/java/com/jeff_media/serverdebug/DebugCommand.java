package com.jeff_media.serverdebug;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.jeff_media.persistentdataserializer.PersistentDataSerializer;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("serverdebug")
public class DebugCommand extends BaseCommand {

    private final ServerDebug plugin;

    public DebugCommand(ServerDebug plugin) {
        this.plugin = plugin;
    }
    @Default
    public void onCommand(CommandSender sender) {
        plugin.createZip();
    }




}
