import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Level1_AI {

    public static void main(String[] args) throws IOException {
        run();
    }

    public static void run() throws IOException {
        Path dir = findLevel1Dir();
        System.out.println("Using directory: " + dir.toAbsolutePath());

        List<Path> inputFiles = findInputFiles(dir);
        if (inputFiles.isEmpty()) {
            System.out.println("No input files found in " + dir);
            return;
        }

        for (Path inputPath : inputFiles) {
            String fileName = inputPath.getFileName().toString();
            String outputFileName = fileName.startsWith("in_")
                    ? "out_" + fileName.substring(3)
                    : fileName + ".out";
            Path outputPath = dir.resolve(outputFileName);

            System.out.println("Processing " + fileName + " -> " + outputFileName + "...");
            String content = Files.readString(inputPath);
            String output = solve(content);
            Files.writeString(outputPath, output);
            System.out.println("Done: " + outputFileName);
        }
    }

    public static String solve(String input) {
        Scanner sc = new Scanner(input);
        if (!sc.hasNextInt()) {
            return "";
        }
        int n = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (!sc.hasNextInt()) break;
            int r = sc.nextInt();
            sb.append(generateBoard(r));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String generateBoard(int r) {
        StringBuilder sb = new StringBuilder();
        int totalRows = 2 * r + 1;
        for (int row = 0; row < totalRows; row++) {
            int d = Math.abs(r - row);
            String prefix = "-".repeat(d);
            String suffix = "-".repeat(d);
            String mid;
            if (d == r) {
                // Top or bottom border: * followed by r occurrences of -*
                mid = "*" + "-*".repeat(r);
            } else {
                // Inner row: * followed by ((2 * r - 1) - d) occurrences of -O, then -*
                int oCount = (2 * r - 1) - d;
                mid = "*" + "-O".repeat(oCount) + "-*";
            }
            sb.append(prefix).append(mid).append(suffix).append("\n");
        }
        return sb.toString();
    }

    private static Path findLevel1Dir() {
        List<Path> candidates = List.of(
                Path.of("src/level-1"),
                Path.of("com.felixwollmann.ccc-2026-fall/src/level-1"),
                Path.of("level-1"),
                Path.of(".")
        );
        for (Path p : candidates) {
            if (Files.exists(p.resolve("in_level-1_0-example.txt"))) {
                return p;
            }
        }
        return Path.of(".");
    }

    private static List<Path> findInputFiles(Path dir) throws IOException {
        List<Path> inputFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "in_*.txt")) {
            for (Path entry : stream) {
                inputFiles.add(entry);
            }
        }
        Collections.sort(inputFiles);
        return inputFiles;
    }
}
