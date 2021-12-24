package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

//with help from /u/snakebehindme's reddit post and github comment
public class Day24 {
    private static final List<String> input = Common.readInput();
    private static final List<Integer> checks = new ArrayList<>();
    private static final List<Integer> divs = new ArrayList<>();
    private static final List<Integer> offsets = new ArrayList<>();
    private static List<Character> validValue;
    private static final List<Triple<Integer, Integer, Integer>> rules = new ArrayList<>();

    public static void main(String[] args) {
        init();
        part1();
        part2();
    }

    private static void init() {

        for (int i = 0; i < input.size(); i += 18) {
            divs.add(Integer.parseInt(input.get(4 + i).split(" ")[2].trim()));
            checks.add(Integer.parseInt(input.get(5 + i).split(" ")[2].trim()));
            offsets.add(Integer.parseInt(input.get(15 + i).split(" ")[2].trim()));
        }

        Deque<Pair<Integer, Integer>> deque = new ArrayDeque<>();

        if (checks.size() == divs.size() && divs.size() == offsets.size() && offsets.size() == 14) {
            for (int i = 0; i < 14; i++) {
                if (checks.get(i) >= 0) {
                    //System.out.println("PUSH input["+i+"] + "+offsets.get(i));
                    deque.push(Pair.of(i, offsets.get(i)));
                } else {
                    Pair<Integer, Integer> top = deque.pop();
                    //System.out.println("input[" + i + "] == input[" + top.getKey() + "] + " + (checks.get(i) + top.getValue()));
                    rules.add(Triple.of(i, top.getKey(), (checks.get(i) + top.getValue())));
                }
            }
        } else
            throw new UnsupportedOperationException();
    }

    private static void part1() {
        validValue = new ArrayList<>(Collections.nCopies(14, '9'));
        List<Boolean> allRulesPassed = new ArrayList<>(Collections.nCopies(validValue.size(), false));
        do {
            for (Triple<Integer, Integer, Integer> r : rules) {
                int tmpVal = (validValue.get(r.getMiddle()) + r.getRight());
                if (tmpVal > '0' && tmpVal < '9' + 1) {
                    validValue.set(r.getLeft(), (char) tmpVal);
                    allRulesPassed.set(r.getLeft(), true);
                    allRulesPassed.set(r.getMiddle(), true);
                } else {
                    validValue.set(r.getMiddle(), (char) (validValue.get(r.getMiddle()) - 1));
                    allRulesPassed.set(r.getLeft(), false);
                    allRulesPassed.set(r.getMiddle(), false);
                }
            }
        } while (!validValue.stream().allMatch(Character::isDigit) || !allRulesPassed.stream().allMatch(e -> e));


        System.out.print("Part 1: ");
        for (char c : validValue)
            System.out.print(c);
        System.out.println();
    }

    private static void part2() {
        validValue = new ArrayList<>(Collections.nCopies(14, '0'));
        List<Boolean> allRulesPassed = new ArrayList<>(Collections.nCopies(validValue.size(), false));

        do {
            for (Triple<Integer, Integer, Integer> r : rules) {
                int tmpVal = (validValue.get(r.getMiddle()) + r.getRight());
                if (tmpVal > '0' && tmpVal < '9' + 1 && validValue.get(r.getMiddle()) != '0') {
                    validValue.set(r.getLeft(), (char) tmpVal);
                    allRulesPassed.set(r.getLeft(), true);
                    allRulesPassed.set(r.getMiddle(), true);
                } else {
                    validValue.set(r.getMiddle(), (char) (validValue.get(r.getMiddle()) + 1));
                    allRulesPassed.set(r.getLeft(), false);
                    allRulesPassed.set(r.getMiddle(), false);
                }
            }
        } while (!validValue.stream().allMatch(Character::isDigit) || validValue.stream().anyMatch(e -> e == '0') || !allRulesPassed.stream().allMatch(e -> e));

        System.out.print("Part 2: ");

        for (char c : validValue)
            System.out.print(c);
        System.out.println();
    }


}
