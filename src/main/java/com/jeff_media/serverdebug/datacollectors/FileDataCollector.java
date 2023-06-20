package com.jeff_media.serverdebug.datacollectors;

import com.jeff_media.serverdebug.Utils;
import java.io.File;
import java.io.IOException;

public class FileDataCollector implements DataCollector {

    private final File sourceFile;
    private final File targetFile;

    public FileDataCollector(File directory, File sourceFile) {
        directory.mkdirs();
        this.sourceFile = sourceFile;
        this.targetFile = new File(directory, sourceFile.getName());
    }

    @Override
    public File getFile() {
        return targetFile;
    }

    @Override
    public void save() throws IOException {
        if(!sourceFile.exists()) {
            return;
        }
        Utils.copyFile(sourceFile, targetFile);
    }
}
