package com.hiroszymon.aoc2021;

import java.util.ArrayList;
import java.util.List;

public class Day25 {
    private static final List<String> input = Common.readInput();

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        List<ArrayList<Character>> inputAsChars = input.stream().map(e -> new ArrayList<>(e.chars().mapToObj(c -> (char) c).toList())).toList();

        List<ArrayList<Character>> orig = new ArrayList<>(inputAsChars.stream().map(ArrayList::new).toList());

        int step = 0;

        do {
            for (int row = 0; row < inputAsChars.size(); row++) {
                for (int col = 0; col < inputAsChars.get(row).size(); col++) {
                    if (inputAsChars.get(row).get(col) == '>') {
                        if (col < inputAsChars.get(row).size() - 1) {
                            if (inputAsChars.get(row).get(col + 1) == '.') {
                                inputAsChars.get(row).set(col + 1, '>');
                                inputAsChars.get(row).set(col, '.');
                            }
                        } else {
                            if (inputAsChars.get(row).get(0) == '.') {
                                inputAsChars.get(row).set(0, '>');
                                inputAsChars.get(row).set(col, '.');
                            }
                        }
                    }
                }
            }
            for (int row = 0; row < inputAsChars.size(); row++) {
                for (int col = 0; col < inputAsChars.get(row).size(); col++) {
                    if (inputAsChars.get(row).get(col) == 'v') {
                        if (row < inputAsChars.size() - 1) {
                            if (inputAsChars.get(row + 1).get(col) == 'v') {
                                inputAsChars.get(row + 1).set(col, '>');
                                inputAsChars.get(row).set(col, '.');
                            }
                        } else {
                            if (inputAsChars.get(0).get(col) == '.') {
                                inputAsChars.get(0).set(col, 'v');
                                inputAsChars.get(row).set(col, '.');
                            }
                        }
                    }
                }
            }
            if (!orig.equals(inputAsChars)) {
                orig = new ArrayList<>(inputAsChars);
                step++;
            } else
                break;
        } while (true);

        System.out.println(step);
    }
}
