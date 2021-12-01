package com.hiroszymon.aoc2021;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day1 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    public static void part1() throws Exception {
        ArrayList<String> input = new ArrayList<>(Common.readInput());
        AtomicInteger largerThanPreviousCount = new AtomicInteger();
        AtomicInteger previousValue = new AtomicInteger(Integer.parseInt(input.get(0)));
        input.stream().mapToInt(Integer::parseInt).forEachOrdered(e -> {
            if (e > previousValue.get()) {
                largerThanPreviousCount.getAndIncrement();
            }
            previousValue.set(e);
        });
        System.out.println("Part1: " + largerThanPreviousCount);
    }

    public static void part2() throws Exception {
        ArrayList<String> input = new ArrayList<>(Common.readInput());
        int sumLargerThanPreviousCount = 0;
        int previousSum = 0;

        ArrayList<Integer> inputParsed = input.stream().map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 3, prev = 0; i < inputParsed.size(); i++) {
            int currentSum = inputParsed.subList(prev, i).stream().mapToInt(e -> e).sum();
            if (currentSum > previousSum)
                sumLargerThanPreviousCount++;
            prev = i - 2;
            previousSum = currentSum;
        }

        System.out.println("Part 2: " + sumLargerThanPreviousCount);
    }
}
