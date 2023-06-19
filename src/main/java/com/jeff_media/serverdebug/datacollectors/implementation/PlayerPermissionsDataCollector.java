package com.jeff_media.serverdebug.datacollectors;

import com.jeff_media.serverdebug.YamlDataCollector;
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
        super(directory, "playerPermisions");
    }

    @Override
    public void collectData() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            set(name + ".uuid", player.getUniqueId().toString());
            set(name + ".isOp", player.isOp());
            List<PermissionAttachmentInfo> perms = player.getEffectivePermissions().stream().sorted(Comparator.comparing(PermissionAttachmentInfo::getPermission)).collect(Collectors.toList());
            List<Map<String,Object>> permsMap = perms.stream().map(info -> {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("name", info.getPermission());
                map.put("value", info.getValue());
                Permission perm = Bukkit.getPluginManager().getPermission(info.getPermission());
                if(perm != null) {
                    map.put("default", perm.getDefault().toString());
                }
                return map;
            }).collect(Collectors.toList());
            set(name + ".permissions", permsMap);
        }
    }
}
