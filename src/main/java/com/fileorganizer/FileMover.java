package com.fileorganizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class FileMover {

    public static void organize(String folderPath, boolean dryRun, boolean log, boolean deleteEmpty) {
        try {
            Path root = Paths.get(folderPath);
            if (!Files.exists(root) || !Files.isDirectory(root)) {
                System.out.println("Invalid folder path.");
                return;
            }

            // Open log writer if --log is enabled
            final BufferedWriter logWriter = (log
                    ? Files.newBufferedWriter(Paths.get("organizer.log"),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                    : null);

            try (Stream<Path> files = Files.list(root)) {
                files.filter(Files::isRegularFile).forEach(file -> {
                    String type = FileTypeClassifier.classify(file);
                    Path targetDir = root.resolve(type);

                    String message = (dryRun ? "[DRY RUN] Would move: " : "Moved: ")
                            + file.getFileName() + " -> " + type;

                    try {
                        if (!dryRun) Files.createDirectories(targetDir);

                        System.out.println(message);
                        if (logWriter != null) logWriter.write(message + "\n");

                        if (!dryRun) {
                            Files.move(file, targetDir.resolve(file.getFileName()),
                                    StandardCopyOption.REPLACE_EXISTING);
                        }

                    } catch (IOException e) {
                        System.err.println("Error processing: " + file.getFileName());
                    }
                });
            }

            // Handle --delete-empty
            if (deleteEmpty && !dryRun) {
                try (Stream<Path> folders = Files.list(root)) {
                    folders.filter(Files::isDirectory).forEach(sub -> {
                        try (Stream<Path> contents = Files.list(sub)) {
                            if (!contents.findAny().isPresent()) {
                                Files.delete(sub);
                                String msg = "Deleted empty folder: " + sub.getFileName();
                                System.out.println(msg);
                                if (logWriter != null) logWriter.write(msg + "\n");
                            }
                        } catch (IOException ignore) {}
                    });
                }
            }

            if (logWriter != null) logWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
