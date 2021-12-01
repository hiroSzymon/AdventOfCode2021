package com.hiroszymon.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Common {
    public static List<String> readInput() throws IOException {
        return Files.readAllLines(Path.of("input.txt"));
    }
}
