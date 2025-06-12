package com.fileorganizer;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar file-organizer.jar <folder-path> [--dry-run] [--log] [--delete-empty]");
            return;
        }

        String folderPath = args[0];
        List<String> flags = Arrays.asList(args).subList(1, args.length);

        boolean dryRun = flags.contains("--dry-run");
        boolean log = flags.contains("--log");
        boolean deleteEmpty = flags.contains("--delete-empty");

        FileMover.organize(folderPath, dryRun, log, deleteEmpty);
    }
}
