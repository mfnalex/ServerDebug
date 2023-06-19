package com.jeff_media.serverdebug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlDataCollector extends YamlConfiguration {

    private final File file;
    private final List<String> lines = new ArrayList<>();

    public YamlDataCollector(File directory, String fileName) {
        this.file = new File(directory, fileName + ".yml");
        if(file.exists()) {
            throw new RuntimeException("File already exists: " + file.getAbsolutePath());
        }
    }

    private void writeLines0(List<String> lines) throws IOException {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(String.join(System.lineSeparator(), lines));
        }
    }

    private void writeLine(String line) {
        this.lines.add(line);
    }

    public final void save() throws IOException {
        writeLines0(lines);
    }

    public final File getFile() {
        return file;
    }

    public abstract void collectData();

    public final void saveAll() {
        collectData();
        writeLines(saveToString().split(System.lineSeparator()));
    }

    private void writeLines(String[] split) {
        for(String line : split) {
            writeLine(line);
        }
    }
}
