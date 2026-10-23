void main() {
    // String[] files = {"in_level_1_1-small.txt", "in_level_1-large.txt"};

    // for (String file: files) {
    // String contents = File.
    // }

    // }

    int level = 1;
    // String[] files = {"level" + level + "_example.in", "level" + level + "_1.in",
    // "level" + level + "_2.in", "level" + level + "_3.in", "level" + level +
    // "_4.in", "level" + level + "_5.in"};

    String[] parts = { "0-example", "1-small", "2-large" };

    String[] files = new String[parts.length];
    for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        files[i] = "in_level-" + level + "_" + part + ".txt";
    }

    for (String file : files) {
        IO.println(file);
    }
    // Files.writeString(Path.of("resources/level" + level + "/" + file + "__out"),
    // level1(Files.readString(Path.of("resources/level" + level + "/" + file))));
}
// }