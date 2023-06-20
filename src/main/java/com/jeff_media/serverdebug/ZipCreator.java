package com.jeff_media.serverdebug;

import com.jeff_media.serverdebug.datacollectors.DataCollector;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;

public class ZipCreator {

    private final ZipFile zipFile;
    private final File tempDirectory;
    private final ZipParameters parameters;
    private final List<DataCollector> contents;

    public ZipCreator(File zipFile, File tempDirectory, List<DataCollector> contents) {
        if(zipFile.exists()) {
            if(!zipFile.delete()) {
                throw new RuntimeException("File already exists and couldn't be deleted: " + zipFile.getAbsolutePath());
            }
        }
        if(!tempDirectory.exists()) {
            throw new RuntimeException("Directory does not exist: " + tempDirectory.getAbsolutePath());
        }
        if(!tempDirectory.isDirectory()) {
            throw new RuntimeException("Not a directory: " + tempDirectory.getAbsolutePath());
        }
        this.contents = contents;
        this.tempDirectory = tempDirectory;
        this.zipFile = new ZipFile(zipFile);
        this.parameters = new ZipParameters();
        this.parameters.setIncludeRootFolder(false);
        parameters.setCompressionLevel(CompressionLevel.ULTRA);

    }

    public void createZip() {
        for(DataCollector dataCollector : contents) {
            try {
                dataCollector.save();
            } catch (IOException ex) {
                new IOException("Could not save file: " + dataCollector.getFile().getAbsolutePath(), ex).printStackTrace();
            }
        }

        try {
            this.zipFile.addFolder(tempDirectory, parameters);
        } catch (ZipException ex) {
            throw new RuntimeException(ex);
        }
    }

}
