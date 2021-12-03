package com.hiroszymon.aoc2021;

import com.hiroszymon.aoc2021.Common;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        ArrayList<String> input = new ArrayList<>(Common.readInput());
        ArrayList<String> columns = new ArrayList<>();

        for (int column = 0; column < input.get(0).length(); column++) {
            StringBuilder tmp = new StringBuilder();
            for (String s : input) {
                tmp.append(s.charAt(column));
            }
            columns.add(tmp.toString());
        }

        char[] msbits = new char[columns.size()];
        char[] lsbits = new char[columns.size()];

        for (int column = 0; column < columns.size(); column++) {
            boolean equal = columns.get(column).chars().filter(c -> c == '1').count() == columns.get(column).length() / 2;
            boolean moreOnes = columns.get(column).chars().filter(c -> c == '1').count() > columns.get(column).length() / 2;

            if (equal)
                throw new Error();

            if (moreOnes) {
                msbits[column] = '1';
                lsbits[column] = '0';
            } else {
                lsbits[column] = '1';
                msbits[column] = '0';
            }
        }

        System.out.println("Part1: (msbits as long) * (lsbits as long) =" + Long.parseLong(new String(msbits), 2) * Long.parseLong(new String(lsbits), 2));
    }

    public static void part2() {
        List<String> input = new ArrayList<>(Common.readInput());
        ArrayList<String> columns = makeColumnsFromInput(input);

        char[] msbits = findMSBsAtPos(columns, 0);
        char[] lsbits = findLSBsAtPos(columns, 0);

        char[] o2GenRating = filterInputBySetBit(input, msbits, 0, true);
        char[] co2ScrubRating = filterInputBySetBit(input, lsbits, 0, false);

        long o2GenRatingL = Long.parseLong(new String(o2GenRating), 2);
        long co2ScrubRatingL = Long.parseLong(new String(co2ScrubRating), 2);

        System.out.println("Part2 o2GenRating * co2ScrubRating =" + o2GenRatingL * co2ScrubRatingL);
    }


    public static char[] filterInputBySetBit(List<String> input, char[] bits, int bitPos, boolean findMSB) {
        char bit = bits[bitPos];
        List<String> filtered = input.stream().filter(e -> e.charAt(bitPos) == bit).toList();

        ArrayList<String> columns = makeColumnsFromInput(filtered);

        char[] msbits = findMSBsAtPos(columns, bitPos);
        char[] lsbits = findLSBsAtPos(columns, bitPos);

        if (filtered.size() > 1) {
            return filterInputBySetBit(filtered, findMSB ? msbits : lsbits, bitPos + 1, findMSB);
        } else {
            return filtered.get(0).toCharArray();
        }
    }

    public static char[] findMSBsAtPos(List<String> columns, int bitPos) {
        char[] msbits = new char[columns.size()];

        for (int column = 0; column < columns.size(); column++) {
            boolean equal = columns.get(column).chars().filter(c -> c == '1').count() == columns.get(column).chars().filter(c -> c == '0').count();
            boolean moreOnes = columns.get(column).chars().filter(c -> c == '1').count() > columns.get(column).length() / 2;

            if (moreOnes || equal) {
                msbits[column] = '1';
            } else {
                msbits[column] = '0';
            }
        }
        return msbits;
    }


    public static char[] findLSBsAtPos(List<String> columns, int bitPos) {
        char[] lsbits = new char[columns.size()];

        for (int column = 0; column < columns.size(); column++) {
            boolean equal = columns.get(column).chars().filter(c -> c == '1').count() == columns.get(column).chars().filter(c -> c == '0').count();
            boolean moreOnes = columns.get(column).chars().filter(c -> c == '1').count() > columns.get(column).length() / 2;

            if (moreOnes || equal) {
                lsbits[column] = '0';
            } else {
                lsbits[column] = '1';
            }
        }
        return lsbits;
    }

    public static ArrayList<String> makeColumnsFromInput(List<String> input) {
        ArrayList<String> columns = new ArrayList<>();
        for (int column = 0; column < input.get(0).length(); column++) {
            StringBuilder tmp = new StringBuilder();
            for (String s : input) {
                tmp.append(s.charAt(column));
            }
            columns.add(tmp.toString());
        }

        return columns;
    }
}
