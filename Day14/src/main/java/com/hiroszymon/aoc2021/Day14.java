package com.hiroszymon.aoc2021;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Day14 {
    private static final List<String> input = Common.readInput();
    private static final HashMap<String, String> insertionRules = new HashMap<>();
    private static final String originalTemplate = init();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        System.out.println("Part 1: " + process(10));
    }

    private static void part2() {
        System.out.println("Part 2: " + process(40));
    }

    private static String init() {
        String template = input.remove(0);

        for (String line : input) {
            if (line.isBlank())
                continue;

            String[] values = line.split("->");
            insertionRules.put(values[0].trim(), values[1].trim());
        }
        return template;
    }

    private static long process(int stepCount) {
        HashMap<String, AtomicLong> pairToCount = new HashMap<>();

        for (int pair = 0; pair < Day14.originalTemplate.length() - 1; pair++) {
            String currentPair = Day14.originalTemplate.substring(pair, pair + 2);
            if (pairToCount.containsKey(currentPair))
                pairToCount.get(currentPair).incrementAndGet();
            else
                pairToCount.put(currentPair, new AtomicLong(1));
        }

        Map<Character, AtomicLong> charToCount = new HashMap<>();
        Day14.originalTemplate.chars().mapToObj(e -> (char) e).forEach(e -> {
            if (charToCount.containsKey(e)) {
                charToCount.get(e).incrementAndGet();
            } else {
                charToCount.put(e, new AtomicLong(1));
            }
        });

        for (int step = 0; step < stepCount; step++) {
            HashMap<String, AtomicLong> tmp = new HashMap<>();

            for (Map.Entry<String, AtomicLong> e : pairToCount.entrySet()) {
                long originalCount = e.getValue().get();
                if (insertionRules.containsKey(e.getKey())) {
                    if (e.getValue().get() > 0)
                        e.getValue().decrementAndGet();

                    if (charToCount.containsKey(insertionRules.get(e.getKey()).charAt(0))) {
                        charToCount.get(insertionRules.get(e.getKey()).charAt(0)).addAndGet(originalCount);
                    } else {
                        charToCount.put(insertionRules.get(e.getKey()).charAt(0), new AtomicLong(1));
                    }

                    String valuePreceding = e.getKey().charAt(0) + insertionRules.get(e.getKey());
                    String valueFollowing = insertionRules.get(e.getKey()) + e.getKey().charAt(1);

                    if (tmp.containsKey(valuePreceding)) {
                        tmp.put(valuePreceding, new AtomicLong(tmp.get(valuePreceding).addAndGet(originalCount)));
                    } else {
                        tmp.put(valuePreceding, new AtomicLong(originalCount));
                    }

                    if (tmp.containsKey(valueFollowing)) {
                        tmp.put(valueFollowing, new AtomicLong(tmp.get(valueFollowing).addAndGet(originalCount)));
                    } else {
                        tmp.put(valueFollowing, new AtomicLong(originalCount));
                    }
                }
            }
            pairToCount = tmp;
        }


        return charToCount.values().stream().mapToLong(AtomicLong::get).max().orElseThrow() - charToCount.values().stream().mapToLong(AtomicLong::get).min().orElseThrow();
    }

}
