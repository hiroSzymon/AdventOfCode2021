package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 {
    private static final List<String> input = Common.readInput();
    private static final List<Pair<List<String>, List<String>>> entries = input.stream().map(e -> {
        String[] splitByPipe = e.split("\\|");
        List<String> stringsLeft = Arrays.stream(splitByPipe[0].split(" ")).map(String::strip)
                .filter(s -> !s.isBlank()).toList();
        List<String> stringsRight = Arrays.stream(splitByPipe[1].split(" ")).map(String::strip)
                .filter(s -> !s.isBlank()).toList();
        return Pair.of(stringsLeft, stringsRight);
    }).toList();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        long sum = entries.stream().mapToLong(e ->
                e.getRight().stream().mapToInt(String::length).filter(s -> s == 2 || s == 4 || s == 3 || s == 7).count()
        ).sum();

        System.out.println("Part1: " + sum);
    }

    private static void part2() {

        entries.stream().mapToLong(e -> {
            HashMap<Set<Character>, Integer> signalToDigit = getSignalToDigitMap(e.getLeft());
            return Integer.parseInt(e.getRight().stream().map(s ->
                            signalToDigit.get(stringToSetOfCharacters(s)) + "")
                    .collect(Collectors.joining()));
        }).forEach(System.out::println);

        long sum = entries.stream().mapToLong(e -> {
            HashMap<Set<Character>, Integer> signalToDigit = getSignalToDigitMap(e.getLeft());
            return Integer.parseInt(e.getRight().stream().map(s ->
                            signalToDigit.getOrDefault(stringToSetOfCharacters(s), 0) + "")
                    .collect(Collectors.joining()));
        }).sum();

        System.out.println("Part2: " + sum);

    }

    private static HashMap<Set<Character>, Integer> getSignalToDigitMap(List<String> signalsP) {
        ArrayList<String> signals = new ArrayList<>(signalsP);
        HashMap<Set<Character>, Integer> digitToSignal = new HashMap<>();

        signals.sort(Comparator.comparingInt(String::length));

        Set<Character> digitOne = stringToSetOfCharacters(signals.get(0));
        Set<Character> digitSeven = stringToSetOfCharacters(signals.get(1));
        Set<Character> digitFour = stringToSetOfCharacters(signals.get(2));
        Set<Character> digitEight = stringToSetOfCharacters(signals.get(signals.size() - 1));
        Set<Character> digitFive = Collections.emptySet();
        Set<Character> digitThree = Collections.emptySet();


        digitSeven.removeAll(digitOne);
        digitSeven.stream().findAny().ifPresent(Segment.TOP::setMapped);

        //digits 0,6,9
        ArrayList<Set<Character>> sixSegmentDigits = new ArrayList<>(signals.stream().filter(e -> e.length() == 6)
                .map(Day8::stringToSetOfCharacters).toList());

        for (Set<Character> s : sixSegmentDigits) {
            HashSet<Character> set = new HashSet<>(s.stream().toList());

            set.removeAll(digitFour);
            set.remove(Segment.TOP.getMapped());
            if (set.size() == 1) {
                digitEight.removeAll(s);
                if (digitEight.size() == 1) {
                    digitEight.stream().findAny().ifPresent(Segment.LEFT_DOWN::setMapped);
                    break;
                }
            }
        }

        //digits 2,3,5
        ArrayList<Set<Character>> fiveSegmentDigits = new ArrayList<>(signals.stream().filter(e -> e.length() == 5)
                .map(Day8::stringToSetOfCharacters).toList());

        for (Set<Character> s : fiveSegmentDigits) {
            HashSet<Character> set = new HashSet<>(s.stream().toList());

            if (set.containsAll(digitOne))
                continue;
            set.removeAll(digitFour);
            set.remove(Segment.TOP.getMapped());
            if (set.size() == 1) {
                digitFive = s;
                set.stream().findAny().ifPresent(Segment.BOTTOM::setMapped);
                break;

            }
        }

        for (Set<Character> s : fiveSegmentDigits) {
            HashSet<Character> set = new HashSet<>(s.stream().toList());

            set.removeAll(digitFive);
            if (set.size() == 1) {
                digitThree = s;
                set.stream().findAny().ifPresent(Segment.RIGHT_UP::setMapped);
                break;

            }
        }

        HashSet<Character> digitThreeTmp = new HashSet<>(digitThree);
        digitThreeTmp.removeAll(digitOne);
        digitThreeTmp.remove(Segment.TOP.getMapped());
        digitThreeTmp.remove(Segment.BOTTOM.getMapped());

        if (digitThreeTmp.size() == 1) {
            digitThreeTmp.stream().findAny().ifPresent(Segment.MIDDLE::setMapped);
        }


        digitOne.remove(Segment.RIGHT_UP.getMapped());
        if (digitOne.size() == 1) {
            digitOne.stream().findAny().ifPresent(Segment.RIGHT_DOWN::setMapped);
        }

        digitFive.removeAll(digitThree);
        if (digitFive.size() == 1) {
            digitFive.stream().findAny().ifPresent(Segment.LEFT_UP::setMapped);
        }

        for (Digit d : Digit.values()) {
            digitToSignal.put(d.getDigitCharacters(), d.value);
        }


        return digitToSignal;
    }

    private static Set<Character> stringToSetOfCharacters(String param) {
        return param.chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
    }

    private static class Segment {
        public static Segment TOP = new Segment("");
        public static Segment BOTTOM = new Segment("");
        public static Segment LEFT_UP = new Segment("");
        public static Segment LEFT_DOWN = new Segment("");
        public static Segment RIGHT_UP = new Segment("");
        public static Segment RIGHT_DOWN = new Segment("");
        public static Segment MIDDLE = new Segment("");

        private char mapped = 0;

        private Segment(String s) {
            if (s.length() == 1)
                mapped = s.charAt(0);
        }

        public void setMapped(char mapped) {
            this.mapped = mapped;
        }

        public char getMapped() {
            return mapped;
        }

        public static Segment[] values() {
            return new Segment[]{TOP,
                    BOTTOM,
                    LEFT_UP,
                    LEFT_DOWN,
                    RIGHT_UP,
                    RIGHT_DOWN,
                    MIDDLE};
        }

    }

    private record Digit(int value, Segment... segments) {
        public static Digit ZERO = new Digit(0, Segment.TOP, Segment.LEFT_UP, Segment.RIGHT_UP, Segment.LEFT_DOWN, Segment.RIGHT_DOWN, Segment.BOTTOM);
        public static Digit ONE = new Digit(1, Segment.RIGHT_DOWN, Segment.RIGHT_UP);
        public static Digit TWO = new Digit(2, Segment.TOP, Segment.RIGHT_UP, Segment.MIDDLE, Segment.LEFT_DOWN, Segment.BOTTOM);
        public static Digit THREE = new Digit(3, Segment.TOP, Segment.RIGHT_UP, Segment.MIDDLE, Segment.RIGHT_DOWN, Segment.BOTTOM);
        public static Digit FOUR = new Digit(4, Segment.LEFT_UP, Segment.MIDDLE, Segment.RIGHT_UP, Segment.RIGHT_DOWN);
        public static Digit FIVE = new Digit(5, Segment.TOP, Segment.LEFT_UP, Segment.MIDDLE, Segment.RIGHT_DOWN, Segment.BOTTOM);
        public static Digit SIX = new Digit(6, Segment.TOP, Segment.LEFT_UP, Segment.MIDDLE, Segment.LEFT_DOWN, Segment.RIGHT_DOWN, Segment.BOTTOM);
        public static Digit SEVEN = new Digit(7, Segment.TOP, Segment.RIGHT_UP, Segment.RIGHT_DOWN);
        public static Digit EIGHT = new Digit(8, Segment.TOP, Segment.LEFT_UP, Segment.RIGHT_UP, Segment.MIDDLE, Segment.LEFT_DOWN, Segment.RIGHT_DOWN, Segment.BOTTOM);
        public static Digit NINE = new Digit(9, Segment.TOP, Segment.LEFT_UP, Segment.RIGHT_UP, Segment.MIDDLE, Segment.RIGHT_DOWN, Segment.BOTTOM);

        public static Digit[] values() {
            return new Digit[]{ZERO,
                    ONE,
                    TWO,
                    THREE,
                    FOUR,
                    FIVE,
                    SIX,
                    SEVEN,
                    EIGHT,
                    NINE};
        }


        public Set<Character> getDigitCharacters() {
            return Arrays.stream(this.segments).map(Segment::getMapped).collect(Collectors.toSet());
        }

    }

}

