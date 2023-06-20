package com.jeff_media.serverdebug.datacollectors.implementation;

import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import java.io.File;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerPermissionsDataCollector extends YamlDataCollector {
    public PlayerPermissionsDataCollector(File directory) {
        super(directory, "playerPermissions");
        options().pathSeparator(';');
    }

    @Override
    public void collectData() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            //System.out.println("Collecting permissions for " + player.getName());
            String name = player.getName();
            set(name + ";uuid", player.getUniqueId().toString());
            set(name + ";isOp", player.isOp());
            player.getEffectivePermissions().stream().sorted(Comparator.comparing(PermissionAttachmentInfo::getPermission)).forEachOrdered(info -> {
                //System.out.println("Found permission " + info.getPermission() + " with value " + info.getValue());
                Map<String,String> map = new LinkedHashMap<>();
                map.put("value", String.valueOf(info.getValue()));
                Permission perm = Bukkit.getPluginManager().getPermission(info.getPermission());
                if(perm != null) {
                    map.put("default", perm.getDefault().toString());
                }
                set(name + ";permissions;" + info.getPermission(), map);
            });
        }
    }
}
