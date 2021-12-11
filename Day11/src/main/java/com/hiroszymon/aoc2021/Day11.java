package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Day11 {
    private static final List<String> input = Common.readInput();
    private static List<List<Integer>> octopuses = new ArrayList<>(input.stream().map(e -> new ArrayList<>(e.chars().mapToObj(c -> c - 0x30).toList())).toList());
    private static final List<Pair<Integer, Integer>> blownInStep = new ArrayList<>();


    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        long flashes = 0;

        for (int step = 0; step < 100; step++) {
            blownInStep.clear();
            for (int row = 0; row < octopuses.size(); row++) {
                for (int col = 0; col < octopuses.get(row).size(); col++) {
                    incrementAndCheck(row, col);
                }
            }
            flashes += blownInStep.size();
        }
        System.out.println("Part 1: " + flashes);
    }

    private static void part2() {
        int step = 0;
        blownInStep.clear();
        octopuses = new ArrayList<>(input.stream().map(e -> new ArrayList<>(e.chars().mapToObj(c -> c - 0x30).toList())).toList());

        while (!octopuses.stream().allMatch(e -> e.stream().allMatch(c -> c == 0))) {
            blownInStep.clear();
            for (int row = 0; row < octopuses.size(); row++) {
                for (int col = 0; col < octopuses.get(row).size(); col++) {
                    incrementAndCheck(row, col);
                }
            }
            step++;
        }
        System.out.println("Part 2: " + step);
    }

    private static void incrementAndCheck(int row, int col) {
        if (row < 0 || row >= octopuses.size())
            return;
        if (col < 0 || col >= octopuses.size())
            return;
        if (blownInStep.contains(Pair.of(row, col)))
            return;

        int currentValue = octopuses.get(row).get(col);
        if (currentValue >= 9) {
            blownInStep.add(Pair.of(row, col));
            incrementAround(row, col);
            octopuses.get(row).set(col, 0);
        } else {
            octopuses.get(row).set(col, currentValue + 1);
        }
    }

    private static void incrementAround(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            if (row < 0 || row >= octopuses.size())
                continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (col < 0 || col >= octopuses.size())
                    continue;
                if (blownInStep.contains(Pair.of(i, j)))
                    continue;
                incrementAndCheck(i, j);

            }
        }
    }
}
