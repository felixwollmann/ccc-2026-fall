import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level2_AI {

    public static void main(String[] args) throws IOException {
        run();
    }

    public static void run() throws IOException {
        Path dir = findLevel2Dir();
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

    public static String solve(String content) {
        List<String> rawLines = content.lines().toList();
        if (rawLines.isEmpty()) {
            return "";
        }

        int idx = 0;
        while (idx < rawLines.size() && rawLines.get(idx).trim().isEmpty()) {
            idx++;
        }
        if (idx >= rawLines.size()) {
            return "";
        }

        int n = Integer.parseInt(rawLines.get(idx).trim());
        idx++;

        StringBuilder result = new StringBuilder();

        for (int b = 0; b < n; b++) {
            while (idx < rawLines.size() && rawLines.get(idx).trim().isEmpty()) {
                idx++;
            }
            if (idx >= rawLines.size()) {
                break;
            }

            String[] header = rawLines.get(idx).trim().split("\\s+");
            idx++;
            int radius = Integer.parseInt(header[0]);
            int numRows = Integer.parseInt(header[1]);
            int numCols = Integer.parseInt(header[2]);

            char[][] grid = new char[numRows][numCols];
            for (int r = 0; r < numRows; r++) {
                String line = rawLines.get(idx++);
                for (int c = 0; c < numCols; c++) {
                    grid[r][c] = (c < line.length()) ? line.charAt(c) : '-';
                }
            }

            simulateLaser(grid, numRows, numCols);

            for (int r = 0; r < numRows; r++) {
                result.append(grid[r]);
                result.append("\n");
            }
            result.append("\n");
        }

        return result.toString();
    }

    private static void simulateLaser(char[][] grid, int numRows, int numCols) {
        int emitterRow = -1;
        int emitterCol = -1;
        char emitterDir = ' ';

        outer:
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                char ch = grid[r][c];
                if (ch >= '1' && ch <= '6') {
                    emitterRow = r;
                    emitterCol = c;
                    emitterDir = ch;
                    break outer;
                }
            }
        }

        if (emitterRow == -1) {
            return;
        }

        int dr = 0;
        int dc = 0;
        switch (emitterDir) {
            case '1' -> { dr =  0; dc =  2; }
            case '2' -> { dr =  1; dc =  1; }
            case '3' -> { dr =  1; dc = -1; }
            case '4' -> { dr =  0; dc = -2; }
            case '5' -> { dr = -1; dc = -1; }
            case '6' -> { dr = -1; dc =  1; }
        }

        int currR = emitterRow + dr;
        int currC = emitterCol + dc;

        while (currR >= 0 && currR < numRows && currC >= 0 && currC < numCols) {
            char ch = grid[currR][currC];
            if (ch == '*') {
                break;
            } else if (ch == 'O') {
                grid[currR][currC] = 'M';
            }
            currR += dr;
            currC += dc;
        }
    }

    private static Path findLevel2Dir() {
        List<Path> candidates = List.of(
                Path.of("src/level-2"),
                Path.of("com.felixwollmann.ccc-2026-fall/src/level-2"),
                Path.of("level-2"),
                Path.of(".")
        );
        for (Path p : candidates) {
            if (Files.exists(p.resolve("in_level-2_0-example.txt"))) {
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
