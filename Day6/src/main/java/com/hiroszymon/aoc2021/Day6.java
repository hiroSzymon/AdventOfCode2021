package com.hiroszymon.aoc2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 {
    static List<String> input = Common.readInput();
    static ArrayList<Long> fishesPerDay = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            fishesPerDay.add(0L);

        input.forEach(e -> Arrays.stream(e.split(",")).forEach(i -> fishesPerDay.set(Integer.parseInt(i),
                fishesPerDay.get(Integer.parseInt(i)) + 1)));

        part1();
        part2();
    }

    public static void part1() {
        for (int day = 0; day < 80; day++) {
            doFishDay(fishesPerDay);
        }

        System.out.println("Part1: " + fishesPerDay.stream().mapToLong(e -> e).sum());
    }

    public static void part2() {
        for (int day = 80; day < 256; day++) {
            doFishDay(fishesPerDay);
        }

        System.out.println("Part2: " + fishesPerDay.stream().mapToLong(e -> e).sum());
    }

    private static void doFishDay(ArrayList<Long> fishesPerDay) {
        long tmp = fishesPerDay.get(0);
        fishesPerDay.set(9, fishesPerDay.get(0));
        fishesPerDay.set(0, 0L);

        for (int i = 1; i < 10; i++) {
            fishesPerDay.set(i - 1, fishesPerDay.get(i));
        }

        fishesPerDay.set(6, fishesPerDay.get(6) + tmp);
        fishesPerDay.set(9, 0L);

    }
}
