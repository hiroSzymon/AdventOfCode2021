package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12 {
    private static final List<String> input = Common.readInput();
    private static final TreeMap<String, List<String>> pathsForNodes = new TreeMap<>();
    private static List<String> validPaths = new ArrayList<>();


    public static void main(String[] args) {
        init();
        part1();
        part2();
    }

    private static void part1() {
        checkPossiblePathsPart1(new ArrayDeque<>(List.of("start")));
        System.out.println("Part 1: " + validPaths.size());
    }

    private static void part2() {
        validPaths = new ArrayList<>();
        checkPossiblePathsPart2(new ArrayDeque<>(List.of("start")));
        System.out.println("Part 2: " + validPaths.size());
    }

    private static void init() {
        input.forEach(e -> {
            String[] entries = e.split("-");

            if (pathsForNodes.containsKey(entries[0]))
                pathsForNodes.get(entries[0]).add(entries[1]);
            else
                pathsForNodes.put(entries[0], new ArrayList<>(List.of(entries[1])));

            if (pathsForNodes.containsKey(entries[1]))
                pathsForNodes.get(entries[1]).add(entries[0]);
            else
                pathsForNodes.put(entries[1], new ArrayList<>(List.of(entries[0])));
        });

    }

    private static void checkPossiblePathsPart1(Deque<String> currentPath) {
        if (currentPath.getLast().equals("end")) {
            validPaths.add(String.join(",", currentPath));
            return;
        }

        for (String node : pathsForNodes.get(currentPath.getLast())) {
            if (currentPath.stream().filter(e -> e.equals(node)).noneMatch(StringUtils::isAllLowerCase)) {
                ArrayDeque<String> tmpStack = new ArrayDeque<>(currentPath);
                tmpStack.add(node);
                checkPossiblePathsPart1(tmpStack);
            }
        }
    }

    private static void checkPossiblePathsPart2(Deque<String> currentPath) {
        if (currentPath.getLast().equals("end")) {
            validPaths.add(String.join(",", currentPath));
            return;
        }

        for (String node : pathsForNodes.get(currentPath.getLast())) {
            if (node.equals("start"))
                continue;

            if (shouldAdd(currentPath, node)) {
                ArrayDeque<String> tmpStack = new ArrayDeque<>(currentPath);
                tmpStack.add(node);
                checkPossiblePathsPart2(tmpStack);
            }
        }
    }

    private static boolean shouldAdd(Deque<String> currentPath, String node) {
        if (!StringUtils.isAllLowerCase(node))
            return true;
        if (node.equals("end"))
            return true;

        Map<String, Long> charCount = currentPath.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (!charCount.containsKey(node))
            return true;
        return charCount.entrySet().stream().filter(e -> StringUtils.isAllLowerCase(e.getKey())).noneMatch(e -> e.getValue() == 2);
    }
}



