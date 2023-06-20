package com.jeff_media.serverdebug.datacollectors;

import com.jeff_media.serverdebug.ServerDebug;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlDataCollector extends YamlConfiguration implements DataCollector {

    private final File file;
    private final List<String> lines = new ArrayList<>();

    public YamlDataCollector(File directory, String fileName) {
        this.file = new File(directory, fileName + ".yml");
        if(file.exists()) {
            throw new RuntimeException("File already exists: " + file.getAbsolutePath());
        }
        lines.add("# ServerDebug v" + ServerDebug.getVersion() + " (C) " + Calendar.getInstance().get(Calendar.YEAR) + " JEFF Media GbR / mfnalex");
        lines.add("# Generated at " + Instant.now().toString());
        lines.add("# Generator: " + getClass().getName());
        lines.add("# File: " + file.getName());
    }

    private void writeLines(List<String> lines) throws IOException {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(String.join(System.lineSeparator(), lines));
        }
    }

    private void writeLine(String line) {
        this.lines.add(line);
    }

    public final void save() throws IOException {
        collectData();
        addLines(saveToString().split(System.lineSeparator()));
        writeLines(lines);
    }

    public final File getFile() {
        return file;
    }

    public abstract void collectData();

    private void addLines(String[] split) {
        for(String line : split) {
            writeLine(line);
        }
    }
}
