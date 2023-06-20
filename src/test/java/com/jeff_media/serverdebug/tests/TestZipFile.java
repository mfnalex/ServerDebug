package com.jeff_media.serverdebug.tests;

import com.jeff_media.serverdebug.datacollectors.DataCollector;
import com.jeff_media.serverdebug.datacollectors.YamlDataCollector;
import com.jeff_media.serverdebug.Utils;
import com.jeff_media.serverdebug.ZipCreator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.Test;

public class TestZipFile {

    @Test
    public void testZipFile() throws IOException {
        File directory = Utils.resetAndGetTempDirectory(Utils.getWorkingDirectory());
        File file = new File("testZipFile.zip");
        file.delete();
        ZipFile zipFile = new ZipFile(file);
        List<DataCollector> contents = new ArrayList<>();
        contents.add(new TestDataCollector(directory));
        new ZipCreator(file, directory, contents).createZip();
    }

    private static class TestDataCollector extends YamlDataCollector {
        public TestDataCollector(File directory) {
            super(directory, "testFile");
        }

        @Override
        public void collectData() {
            set("test", "test");
        }
    }
}
