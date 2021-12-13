package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Day13 {
    private static List<String> input = Common.readInput();
    private static final List<Pair<Integer, String>> foldings = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = input.indexOf(""); i < input.size(); i++) {
            if (input.get(i).contains("x")) {
                String[] line = input.get(i).split("=");
                foldings.add(Pair.of(Integer.parseInt(line[1]), "x"));
            } else if (input.get(i).contains("y")) {
                String[] line = input.get(i).split("=");
                foldings.add(Pair.of(Integer.parseInt(line[1]), "y"));
            }
        }
        input = input.subList(0, input.indexOf(""));
        part1();
        part2();
    }

    private static void part1() {
        List<MutablePair<Integer, Integer>> points = input.stream().map(e ->
                MutablePair.of(Integer.parseInt(e.split(",")[0]), Integer.parseInt(e.split(",")[1]))).toList();

        for (Pair<Integer, String> fold : foldings) {
            for (MutablePair<Integer, Integer> point : points) {
                transpose(point, fold);
            }
            break;
        }

        System.out.println("Part 1: " + points.stream().distinct().count());

    }

    private static void part2() {
        List<MutablePair<Integer, Integer>> points = input.stream().map(e ->
                MutablePair.of(Integer.parseInt(e.split(",")[0]), Integer.parseInt(e.split(",")[1]))).toList();

        for (Pair<Integer, String> fold : foldings) {
            for (MutablePair<Integer, Integer> point : points) {
                transpose(point, fold);
            }
        }

        System.out.println("Part 2: ");

        int maxY = points.stream().mapToInt(Pair::getValue).max().orElseThrow() + 1;
        int maxX = points.stream().mapToInt(Pair::getKey).max().orElseThrow() + 1;

        List<List<String>> linesOut = new ArrayList<>();

        //Poor man's fill with spaces
        for (int y = 0; y < maxY; y++) {
            linesOut.add(new ArrayList<>());
            for (int x = 0; x < maxX; x++) {
                linesOut.get(y).add(" ");
            }
        }

        points.forEach(e -> linesOut.get(e.getValue()).set(e.getKey(), "#"));

        linesOut.forEach(e -> System.out.println(String.join("", e)));

    }

    private static void transpose(MutablePair<Integer, Integer> point, Pair<Integer, String> fold) {
        switch (fold.getValue()) {
            case "y":
                if (point.getValue() > fold.getKey()) {
                    point.setValue(point.getValue() - (point.getValue() - fold.getKey()) * 2);
                }
                break;
            case "x":
                if (point.getKey() > fold.getKey()) {
                    point.setLeft(point.getLeft() - (point.getLeft() - fold.getKey()) * 2);
                }
                break;

        }
    }


}
