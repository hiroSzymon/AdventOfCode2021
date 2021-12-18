package com.hiroszymon.aoc2021;

import java.util.ArrayList;
import java.util.List;

public class Day18 {
    private static final List<String> input = Common.readInput();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        List<Tree> trees = new ArrayList<>();
        for (String s : input) {
            trees.add(Tree.fromString(s));
        }

        Tree mainTree = trees.get(0);
        for (int i = 1; i < trees.size(); i++) {
            mainTree = mainTree.add(trees.get(i));
            mainTree.reduce();
        }

        System.out.println("Part 1: " + mainTree.getRootNode().magnitude());
    }

    private static void part2() {
        List<Long> magnitudes = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (i == j)
                    continue;

                List<Tree> trees = new ArrayList<>();
                for (String s : input) {
                    trees.add(Tree.fromString(s));
                }

                magnitudes.add(trees.get(i).add(trees.get(j)).reduce().getRootNode().magnitude());
            }
        }
        System.out.println("Part 2: " + magnitudes.stream().mapToLong(e -> e).max().orElseThrow());
    }

}
