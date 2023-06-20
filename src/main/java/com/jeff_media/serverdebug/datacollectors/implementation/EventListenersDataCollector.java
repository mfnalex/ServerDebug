package com.jeff_media.serverdebug.datacollectors.implementation;

import com.jeff_media.serverdebug.Utils;
import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;

public class EventListenersDataCollector extends YamlDataCollector {

    private final Method getEventListenersMethod;

    public EventListenersDataCollector(File directory) {
        super(directory, "eventListeners");
        options().pathSeparator(';');
        try {
            getEventListenersMethod = SimplePluginManager.class.getDeclaredMethod("getEventListeners", Class.class);
            getEventListenersMethod.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void collectData() {
        List<String> eventClasses = Utils.listAllClasses(Bukkit.class, className -> className.startsWith("org.bukkit.event."));
//        Map<String,Map<String,Map<String,String>>> map = new HashMap<>();
        for(String className : eventClasses) {
            List<Map<String,String>> list = new ArrayList<>();
            try {
                Class<?> clazz = Class.forName(className);
                if(Event.class.isAssignableFrom(clazz)) {
                    String event = clazz.getName();
                    Class<? extends Event> eventClazz = clazz.asSubclass(Event.class);
                    HandlerList listeners = (HandlerList) getEventListenersMethod.invoke(Bukkit.getPluginManager(), eventClazz);
                    for(RegisteredListener listener : listeners.getRegisteredListeners()) {
                        Map<String,String> map = new LinkedHashMap<>();
                        //System.out.println("Found listener for " + event + ": " + listener.getListener().toString());
                        map.put("plugin", listener.getPlugin().getName());
                        map.put("class", listener.getListener().getClass().getName());
                        //map.put("listener", listener.getListener().toString());
                        map.put("priority", listener.getPriority().toString());
                        list.add(map);
                    }
                }
            } catch (ReflectiveOperationException ignored) {
                //System.out.println("Error while collecting data for " + className);
                //throw new RuntimeException("Error while collecting data for " + className, ex);
            }
            if(!list.isEmpty()) {
                set(className, list);
            }
        }
    }

    private static final Comparator<RegisteredListener> LISTENER_COMPARATOR = Comparator.comparing(RegisteredListener::getPriority).thenComparing(o -> o.getPlugin().getName()).thenComparing(o -> o.getListener().toString());
}
