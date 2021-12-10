package com.hiroszymon.aoc2021;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Day10 {
    private static final List<String> input = Common.readInput();
    private static final List<String> corrupted = new ArrayList<>();
    private static final List<String> incomplete = new ArrayList<>();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        List<Character> invalidCharacters = new ArrayList<>();
        Deque<Character> bracketsStack = new ArrayDeque<>();

        outer:
        for (String line : input) {
            for (char e : line.toCharArray())
                switch (e) {
                    case '{', '[', '(', '<' -> bracketsStack.push(e);
                    case '}', ']', ')', '>' -> {
                        char c = bracketsStack.pop();
                        if (c == '{' && e != '}') {
                            invalidCharacters.add(e);
                            corrupted.add(line);
                            continue outer;
                        } else if (c == '[' && e != ']') {
                            invalidCharacters.add(e);
                            corrupted.add(line);
                            continue outer;
                        } else if (c == '(' && e != ')') {
                            invalidCharacters.add(e);
                            corrupted.add(line);
                            continue outer;
                        } else if (c == '<' && e != '>') {
                            invalidCharacters.add(e);
                            corrupted.add(line);
                            continue outer;
                        }
                    }
                }
        }
        System.out.println("Part 1: " + invalidCharacters.stream().mapToLong(e -> switch (e) {
            case '}' -> 1197;
            case ']' -> 57;
            case ')' -> 3;
            case '>' -> 25137;
            default -> throw new UnsupportedOperationException(e.toString());
        }).sum());

    }

    private static void part2() {
        List<String> incomplete = input.stream().filter(e -> !corrupted.contains(e)).toList();
        List<List<Character>> lineCompletions = new ArrayList<>();

        for (String line : incomplete) {
            ArrayList<Character> lineCompletion = new ArrayList<>();
            Deque<Character> bracketsStack = new ArrayDeque<>();

            for (char e : line.toCharArray())
                switch (e) {
                    case '{', '[', '(', '<' -> bracketsStack.push(e);
                    case '}', ']', ')', '>' -> bracketsStack.pop();
                }

            bracketsStack.forEach(e -> {
                switch (e) {
                    case '{' -> lineCompletion.add('}');
                    case '[' -> lineCompletion.add(']');
                    case '(' -> lineCompletion.add(')');
                    case '<' -> lineCompletion.add('>');
                    default -> throw new UnsupportedOperationException(e.toString());
                }
            });
            lineCompletions.add(lineCompletion);
        }

        List<Long> lineScores = new ArrayList<>();

        lineCompletions.forEach(e -> {
            AtomicLong score = new AtomicLong();

            e.forEach(c -> {
                        score.updateAndGet(v -> v * 5);
                        switch (c) {
                            case ')' -> score.addAndGet(1);
                            case ']' -> score.addAndGet(2);
                            case '}' -> score.addAndGet(3);
                            case '>' -> score.addAndGet(4);
                            default -> throw new UnsupportedOperationException();
                        }
                    }
            );
            lineScores.add(score.get());
        });

        lineScores.sort(Comparator.comparingLong(e -> e));

        System.out.println("Part 2: " + lineScores.get(lineScores.size() / 2));
    }
}
