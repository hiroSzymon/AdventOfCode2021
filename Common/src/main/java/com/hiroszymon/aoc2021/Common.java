package com.hiroszymon.aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class Common {
    public static List<String> readInput() {
        try {
            return Files.readAllLines(Path.of("input.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return Collections.emptyList();
        }
    }
}
