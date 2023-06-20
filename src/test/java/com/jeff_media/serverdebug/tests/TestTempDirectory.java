package com.jeff_media.serverdebug.tests;

import com.jeff_media.serverdebug.Utils;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTempDirectory {

    @Test
    public void testDeleteDirectory() throws IOException {
        File tempDir = new File("testTempDirectory");
        tempDir.mkdirs();
        File testFile = new File(tempDir, "testFile.txt");
        testFile.createNewFile();
        Assertions.assertTrue(tempDir.exists());
        Assertions.assertTrue(tempDir.isDirectory());
        Assertions.assertTrue(testFile.exists());
        Assertions.assertTrue(testFile.isFile());

        Assertions.assertTrue(Utils.deleteFolderRecursively(tempDir), "Could not delete temp directory");
        Assertions.assertFalse(tempDir.exists());
        Assertions.assertFalse(tempDir.isDirectory());
        Assertions.assertFalse(testFile.exists());
        Assertions.assertFalse(testFile.isFile());
    }

    @Test
    public void testResetAndGetTempDirectory() {
        File tempDirectory = Utils.resetAndGetTempDirectory(Utils.getWorkingDirectory());
        Assertions.assertTrue(tempDirectory.exists());
        Assertions.assertTrue(tempDirectory.isDirectory());
        File[] contents = tempDirectory.listFiles();
        Assertions.assertNotNull(contents);
        Assertions.assertEquals(0, contents.length);
        Assertions.assertTrue(Utils.deleteFolderRecursively(tempDirectory), "Could not delete temp directory");
    }

}
