package use.tool.dev.editor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexReplaceTool {

    // Regex: fängt vec...get(Ziffer) ein
    private static final Pattern P = Pattern.compile("(vec\\w*\\.get\\([0-9]\\))");

    // Replacement: korrekt geklammert
    private static final String REPLACEMENT = "((Object)$1).toString()";

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Verwendung:");
            System.out.println("  java RegexReplaceTool <pfad-zum-ordner>");
            System.out.println("  java RegexReplaceTool <pfad-zur-datei.java>");
            return;
        }

        Path inputPath = Paths.get(args[0]);
        if (Files.isDirectory(inputPath)) {
            // Alle .java-Dateien im Verzeichnisbaum durchlaufen
            Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        processFile(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else if (Files.isRegularFile(inputPath) && inputPath.toString().endsWith(".java")) {
            // Nur eine konkrete Datei verarbeiten
            processFile(inputPath);
        } else {
            System.out.println("Ungültiger Pfad: " + inputPath);
        }
    }

    private static void processFile(Path file) {
        try {
            String content = new String(Files.readAllBytes(file), UTF8);

            Matcher m = P.matcher(content);
            String newContent = m.replaceAll(REPLACEMENT);

            if (!content.equals(newContent)) {
                // Backup anlegen (.bak-Datei)
                Path backupFile = Paths.get(file.toString() + ".bak");
                if (!Files.exists(backupFile)) {
                    Files.copy(file, backupFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Backup erstellt: " + backupFile);
                }

                // Neue Version schreiben
                Files.write(file, newContent.getBytes(UTF8));
                System.out.println("Geändert: " + file);
            }
        } catch (IOException e) {
            System.err.println("Fehler bei Datei " + file + ": " + e.getMessage());
        }
    }
}
