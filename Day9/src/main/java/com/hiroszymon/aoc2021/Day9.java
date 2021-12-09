package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day9 {
    private static final List<String> input = Common.readInput();
    private static final HashMap<Pair<Integer, Integer>, Integer> lowPoints = new HashMap<>();
    private static final List<List<Character>> heightmap = input.stream().map(e -> e.chars().mapToObj(c -> (char) c).toList()).toList();
    private static final HashMap<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> basins = new HashMap<>();


    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        for (int row = 0; row < heightmap.size(); row++) {
            for (int col = 0; col < heightmap.get(row).size(); col++) {
                int heightCurrentlyChecked = heightmap.get(row).get(col);
                boolean isLowest = true;

                try {
                    isLowest = heightmap.get(row - 1).get(col) > heightCurrentlyChecked;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.err.println("Out of bounds. Continuing...");
                }

                try {
                    isLowest &= heightmap.get(row + 1).get(col) > heightCurrentlyChecked;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.err.println("Out of bounds. Continuing...");
                }
                try {
                    isLowest &= heightmap.get(row).get(col - 1) > heightCurrentlyChecked;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.err.println("Out of bounds. Continuing...");
                }

                try {
                    isLowest &= heightmap.get(row).get(col + 1) > heightCurrentlyChecked;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.err.println("Out of bounds. Continuing...");
                }

                if (isLowest) {
                    lowPoints.put(Pair.of(row, col), heightCurrentlyChecked - 0x30); //change char to int
                }

            }
        }
        System.out.println("Part 1: " + lowPoints.values().stream().mapToLong(e -> e + 1).sum());
    }

    public static void part2() {
        for (Map.Entry<Pair<Integer, Integer>, Integer> e : lowPoints.entrySet()) {
            basins.put(e.getKey(), new ArrayList<>());
            basins.get(e.getKey()).add(e.getKey());
            checkInFrontOf(e.getKey(), e.getKey(), e.getValue());
            checkBehind(e.getKey(), e.getKey(), e.getValue());
            checkAbove(e.getKey(), e.getKey(), e.getValue());
            checkBelow(e.getKey(), e.getKey(), e.getValue());
        }
        List<Pair<Pair<Integer, Integer>, Integer>> basinLowestToSizeSorted = basins.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue().size())).sorted(Comparator.comparingInt(Pair::getValue)).toList();
        List<Pair<Pair<Integer, Integer>, Integer>> threeBiggestBasins = basinLowestToSizeSorted.subList(basinLowestToSizeSorted.size() - 3, basinLowestToSizeSorted.size());
        AtomicInteger result = new AtomicInteger(1);
        threeBiggestBasins.forEach(e -> result.updateAndGet(v -> v * e.getValue()));
        System.out.println("Part2 : " + result);
    }

    private static void checkInFrontOf(final Pair<Integer, Integer> original, Pair<Integer, Integer> pos, int checkedVal) {
        int currentIndex = 1;
        int currentRow = pos.getKey();
        int currentCol = pos.getValue() + currentIndex;

        try {
            while (heightmap.get(currentRow).get(currentCol) != '9' &&
                    heightmap.get(currentRow).get(currentCol) > checkedVal) {

                currentIndex = checkAboveAndBelow(original, currentIndex, currentRow, currentCol);
                currentCol = pos.getValue() + currentIndex;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            //System.err.println("Out of bounds. Continuing...");
        }
    }

    private static int checkAboveAndBelow(Pair<Integer, Integer> original, int currentIndex, int currentRow, int currentCol) {
        if (!basins.get(original).contains(Pair.of(currentRow, currentCol)))
            basins.get(original).add(Pair.of(currentRow, currentCol));

        checkAbove(original, Pair.of(currentRow, currentCol), heightmap.get(currentRow).get(currentCol));
        checkBelow(original, Pair.of(currentRow, currentCol), heightmap.get(currentRow).get(currentCol));
        currentIndex++;
        return currentIndex;
    }

    private static void checkBehind(final Pair<Integer, Integer> original, Pair<Integer, Integer> pos, int checkedVal) {
        int currentIndex = 1;
        int currentRow = pos.getKey();
        int currentCol = pos.getValue() - currentIndex;

        try {
            while (heightmap.get(currentRow).get(currentCol) != '9' &&
                    heightmap.get(currentRow).get(currentCol) > checkedVal) {

                currentIndex = checkAboveAndBelow(original, currentIndex, currentRow, currentCol);
                currentCol = pos.getValue() - currentIndex;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            //System.err.println("Out of bounds. Continuing...");
        }
    }

    private static void checkAbove(final Pair<Integer, Integer> original, Pair<Integer, Integer> pos, int checkedVal) {
        int currentIndex = 1;
        int currentRow = pos.getKey() - currentIndex;
        int currentCol = pos.getValue();

        try {
            while (heightmap.get(currentRow).get(currentCol) != '9' &&
                    heightmap.get(currentRow).get(currentCol) > checkedVal) {

                currentIndex = checkInFrontAndBehind(original, currentIndex, currentRow, currentCol);
                currentRow = pos.getKey() - currentIndex;
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            //System.err.println("Out of bounds. Continuing...");
        }
    }

    private static int checkInFrontAndBehind(Pair<Integer, Integer> original, int currentIndex, int currentRow, int currentCol) {
        if (!basins.get(original).contains(Pair.of(currentRow, currentCol)))
            basins.get(original).add(Pair.of(currentRow, currentCol));

        checkInFrontOf(original, Pair.of(currentRow, currentCol), heightmap.get(currentRow).get(currentCol));
        checkBehind(original, Pair.of(currentRow, currentCol), heightmap.get(currentRow).get(currentCol));
        currentIndex++;
        return currentIndex;
    }

    private static void checkBelow(final Pair<Integer, Integer> original, Pair<Integer, Integer> pos, int checkedVal) {
        int currentIndex = 1;
        int currentRow = pos.getKey() + currentIndex;
        int currentCol = pos.getValue();

        try {
            while (heightmap.get(currentRow).get(currentCol) != '9' &&
                    heightmap.get(currentRow).get(currentCol) > checkedVal) {

                currentIndex = checkInFrontAndBehind(original, currentIndex, currentRow, currentCol);
                currentRow = pos.getKey() + currentIndex;
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            //System.err.println("Out of bounds. Continuing...");
        }
    }


}
