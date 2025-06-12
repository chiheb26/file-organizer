package com.fileorganizer;

import java.nio.file.Path;

public class FileTypeClassifier {
    public static String classify(Path file) {
        String name = file.getFileName().toString().toLowerCase();

        if (name.endsWith(".jpg") || name.endsWith(".png")) return "images";
        if (name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".txt")) return "docs";
        if (name.endsWith(".zip") || name.endsWith(".rar")) return "archives";
        return "others";
    }
}
