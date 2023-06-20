package com.jeff_media.serverdebug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static boolean deleteFolderRecursively(@NotNull File directory) {
        boolean success = true;
        if(!directory.exists()) {
            return success;
        }
        if(!directory.isDirectory()) {
            throw new RuntimeException("Not a directory: " + directory.getAbsolutePath());
        }
        File[] contents = directory.listFiles();
        if(contents == null) {
            throw new RuntimeException("Could not list files in directory: " + directory.getAbsolutePath());
        }
        for(File file : contents) {
            if(file.isDirectory()) {
                success = deleteFolderRecursively(file) && success;
            } else {
                success = file.delete() && success;
            }
        }
        return directory.delete() && success;
    }

    public static File resetAndGetTempDirectory(File parentFolder) {
        if (!parentFolder.exists()) {
            throw new RuntimeException("Parent folder does not exist: " + parentFolder.getAbsolutePath());
        }
        File tempDirectory = new File(parentFolder, "temp");
        if (!deleteFolderRecursively(tempDirectory)) {
            throw new RuntimeException("Could not delete temp directory: " + tempDirectory.getAbsolutePath());
        }
        if (!tempDirectory.mkdirs()) {
            throw new RuntimeException("Could not create temp directory: " + tempDirectory.getAbsolutePath());
        }
        return tempDirectory;
    }

    public static File getWorkingDirectory() {
        //Path currentRelativePath = Paths.get("");
        File workigDirectory = new File(".");
        if(!workigDirectory.exists()) {
            throw new RuntimeException("Working directory does not exist: " + workigDirectory.getAbsolutePath());
        }
        if(!workigDirectory.isDirectory()) {
            throw new RuntimeException("Working directory is not a directory: " + workigDirectory.getAbsolutePath());
        }
        return workigDirectory;
    }

    public static void writeLine(File file, String line) {
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8)) {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file: " + file.getAbsolutePath(), e);
        }
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        Path target = Paths.get(targetFile.getAbsolutePath());
        Path source = Paths.get(sourceFile.getAbsolutePath());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyDirectory(String sourceDirectory, String targetDirectory)
            throws IOException {
        try(Stream<Path> paths = Files.walk(Paths.get(sourceDirectory))) {
            paths.forEach(source -> {
                Path destination = Paths.get(targetDirectory, source.toString()
                        .substring(sourceDirectory.length()));
                try {
                    Files.copy(source, destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @NotNull
    public static List<String> listAllClasses(@NotNull final Class<?> clazz, Predicate<String> filter) {
        final CodeSource source = clazz.getProtectionDomain().getCodeSource();
        if (source == null) return Collections.emptyList();
        final URL url = source.getLocation();
        try (
                final ZipInputStream zip = new ZipInputStream(url.openStream())) {
            final List<String> classes = new ArrayList<>();
            while (true) {
                final ZipEntry entry = zip.getNextEntry();
                if (entry == null) break;
                if (entry.isDirectory()) continue;
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    name = name.replace('/', '.').substring(0, name.length() - 6);
                    if(filter.test(name)) {
                        classes.add(name);
                    }
                }
            }
            return classes;
        } catch (IOException exception) {
            return Collections.emptyList();
        }
    }

}
