package com.jeff_media.serverdebug;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCSerializer {

    private static final List<PersistentDataType<?, ?>> PERSISTENT_DATA_TYPE_LIST = new ArrayList<>();

    static {
        for (Field field : PersistentDataType.class.getDeclaredFields()) {
            if (field.getType() == PersistentDataType.class) {
                try {
                    PERSISTENT_DATA_TYPE_LIST.add((PersistentDataType<?, ?>) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static YamlConfiguration serialize(PersistentDataContainer pdc) {
        YamlConfiguration section = new YamlConfiguration();
        section.options().pathSeparator(';');
        for (NamespacedKey key : pdc.getKeys()) {
            PersistentDataType<?, ?> type = getPersistentDataType(pdc, key);
            Object value;
            if (type == PersistentDataType.TAG_CONTAINER) {
                //section.set(key.toString(), serialize(Objects.requireNonNull(pdc.get(key, PersistentDataType.TAG_CONTAINER))));
                value = serialize(pdc.get(key, PersistentDataType.TAG_CONTAINER));
            } else if (type == PersistentDataType.TAG_CONTAINER_ARRAY) {
                List<ConfigurationSection> list = new ArrayList<>();
                for (PersistentDataContainer container : Objects.requireNonNull(pdc.get(key, PersistentDataType.TAG_CONTAINER_ARRAY))) {
                    list.add(serialize(container));
                }
                //section.set(key.toString(), list);
                value = list;
            } else {
                //section.set(key.toString(), pdc.get(key, type));
                value = pdc.get(key, type);
            }
            section.set(key.toString() + ";type", type.getPrimitiveType().getSimpleName());
            section.set(key.toString() + ";value", value);
        }
        return section;
    }

    private static PersistentDataType<?, ?> getPersistentDataType(PersistentDataContainer pdc, NamespacedKey key) {
        for (PersistentDataType<?, ?> type : PERSISTENT_DATA_TYPE_LIST) {
            if (pdc.has(key, type)) {
                return type;
            }
        }
        throw new RuntimeException("Could not find PersistentDataType for key " + key);
    }

}
