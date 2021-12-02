package com.hiroszymon.aoc2021;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Day2 {
    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        AtomicInteger depth = new AtomicInteger();
        AtomicInteger horizontalPos = new AtomicInteger();
        ArrayList<String> input = new ArrayList<>(Common.readInput());
        input.forEach(e -> {
            String[] split = e.split(" ");
            String action = split[0];
            int value = Integer.parseInt(split[1]);

            switch (action) {
                case "forward" -> horizontalPos.getAndAdd(value);
                case "down" -> depth.getAndAdd(value);
                case "up" -> depth.getAndAdd(-value);
            }
        });

        System.out.println("Part1 horizontal pos * depth: " + horizontalPos.get() * depth.get());

    }

    public static void part2() {
        AtomicInteger depth = new AtomicInteger();
        AtomicInteger horizontalPos = new AtomicInteger();
        AtomicInteger aim = new AtomicInteger();
        ArrayList<String> input = new ArrayList<>(Common.readInput());
        input.forEach(e -> {
            String[] split = e.split(" ");
            String action = split[0];
            int value = Integer.parseInt(split[1]);

            switch (action) {
                case "forward" -> {
                    horizontalPos.getAndAdd(value);
                    depth.getAndAdd(aim.get() * value);
                }
                case "down" -> aim.getAndAdd(value);
                case "up" -> aim.getAndAdd(-value);
            }
        });

        System.out.println("Part2 horizontal pos * depth: " + horizontalPos.get() * depth.get());

    }
}
