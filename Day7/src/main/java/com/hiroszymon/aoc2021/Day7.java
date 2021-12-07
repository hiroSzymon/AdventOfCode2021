package com.hiroszymon.aoc2021;

import java.util.*;

public class Day7 {
    static List<Integer> input = Arrays.stream(Common.readInput().get(0).split(",")).map(e -> Integer.parseInt(e.strip())).toList();
    final static int minimum = input.stream().mapToInt(e -> e).min().orElseThrow();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        HashMap<Integer, Integer> posToSpentFuel = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            int spentFuel = 0;
            for (Integer crab : input) {
                spentFuel += Math.abs(crab - (minimum + i));
            }
            posToSpentFuel.put(i, spentFuel);
        }

        System.out.println("Part 1:" + posToSpentFuel.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)));
    }

    private static void part2() {
        HashMap<Integer, Integer> posToSpentFuel = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            int spentFuel = 0;
            for (Integer crab : input) {
                spentFuel += sumOfSeriesFromZeroToParam(Math.abs(crab - (minimum + i)));
            }
            posToSpentFuel.put(i, spentFuel);
        }

        System.out.println("Part 2:" + posToSpentFuel.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)));
    }

    private static long sumOfSeriesFromZeroToParam(int param) {
        long tmp = 0;
        for (int i = 0; i < param + 1; i++) {
            tmp += i;
        }
        return tmp;
    }
}
