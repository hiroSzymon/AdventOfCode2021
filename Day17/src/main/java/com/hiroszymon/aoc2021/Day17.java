package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day17 {
    private static final List<String> input = Common.readInput();
    private static final String[] data = Arrays.copyOfRange(input.get(0).split(" "), 2, 4);
    private static final String[] xVals = data[0].substring(data[0].indexOf('=') + 1, data[0].length() - 1).split("\\.\\.");
    private static final String[] yVals = data[1].substring(data[1].indexOf('=') + 1).split("\\.\\.");
    private static final int xMin = Integer.parseInt(xVals[0]);
    private static final int xMax = Integer.parseInt(xVals[1]);
    private static final int yMin = Integer.parseInt(yVals[0]);
    private static final int yMax = Integer.parseInt(yVals[1]);
    private static final Deque<Pair<Pair<Integer, Integer>, List<Pair<Integer, Integer>>>> paths = new ArrayDeque<>();

    public static void main(String[] args) {
        findValidPaths();
        part1();
        part2();
    }

    private static void part1() {
        System.out.println("Part 1: " + paths.stream().mapToInt(e -> e.getValue().stream().mapToInt(Pair::getValue)
                .max().orElseThrow()).max().orElseThrow());
    }

    private static void part2() {
        //To be honest, I'm not sure why it works :|
        System.out.println("Part 2: " + (long) paths.size());

    }

    private static void findValidPaths() {
        MutablePair<Integer, Integer> probe;
        MutablePair<Integer, Integer> velocity;

        for (int x = 0; x <= xMax; x++) {
            for (int y = yMin; y < Math.abs(yMin) * 2; y++) {
                probe = MutablePair.of(0, 0);
                velocity = MutablePair.of(x, y);
                paths.add(Pair.of(velocity, new ArrayList<>()));

                while (!checkTarget(probe)) {
                    step(probe, velocity);
                    if (checkOOB(probe)) {
                        paths.removeLast();
                        break;
                    }
                }
            }
        }
    }

    private static void step(MutablePair<Integer, Integer> probe, MutablePair<Integer, Integer> velocity) {
        probe.setLeft(probe.getLeft() + velocity.getLeft());
        probe.setRight(probe.getRight() + velocity.getRight());
        if (velocity.getLeft() > 0)
            velocity.setLeft(velocity.getLeft() - 1);
        else if (velocity.getLeft() < 0)
            velocity.setLeft(velocity.getLeft() + 1);

        velocity.setRight(velocity.getRight() - 1);

        if (paths.peekLast() != null) {
            paths.peekLast().getValue().add(Pair.of(probe.getLeft(), probe.getRight()));
        }
    }

    private static boolean checkTarget(Pair<Integer, Integer> probe) {
        return xMin <= probe.getKey() && probe.getKey() <= xMax && yMin <= probe.getValue() && probe.getValue() <= yMax;
    }

    private static boolean checkOOB(Pair<Integer, Integer> probe) {
        //assuming target is always lower than the starting point!
        return probe.getKey() > xMax || probe.getValue() < yMin;
    }
}
