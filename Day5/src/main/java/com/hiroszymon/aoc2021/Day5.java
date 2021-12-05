package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Day5 {
    static HashMap<Pair<Integer, Integer>, MutableInt> pointToCount = new HashMap<>();
    static List<String> input = Common.readInput();
    static List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> lines = input.stream().map(e -> {
        String[] lineS = e.split("->");
        String[] lineSBeginningXY = lineS[0].split(",");
        String[] lineSEndXY = lineS[1].split(",");
        return Pair.of(Pair.of(Integer.parseInt(lineSBeginningXY[0].trim()), Integer.parseInt(lineSBeginningXY[1].trim())),
                Pair.of(Integer.parseInt(lineSEndXY[0].trim()), Integer.parseInt(lineSEndXY[1].trim())));
    }).toList();

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        pointToCount.clear();
        List<Line> linesO = lines.stream().map(e -> new Line(e.getKey(), e.getValue())).filter(e -> e.isHorizontal || e.isVertical).toList();
        linesO.forEach(e -> e.linePoints.forEach(p -> {
            if (pointToCount.containsKey(p))
                pointToCount.get(p).increment();
            else
                pointToCount.put(p, new MutableInt(1));
        }));

        System.out.println("Part 1: overlapping points = " + pointToCount.entrySet().stream().filter(e -> e.getValue().getValue() > 1).count());

    }

    public static void part2() {
        pointToCount.clear();
        List<Line> linesO = lines.stream().map(e -> new Line(e.getKey(), e.getValue())).filter(e -> e.isValidForPart2).toList();

        linesO.forEach(e -> e.linePoints.forEach(p -> {
            if (pointToCount.containsKey(p))
                pointToCount.get(p).increment();
            else
                pointToCount.put(p, new MutableInt(1));
        }));

        System.out.println("Part 2: overlapping points = " + pointToCount.entrySet().stream().filter(e -> e.getValue().getValue() > 1).count());

    }

    private static class Line {
        Pair<Integer, Integer> beginning;
        Pair<Integer, Integer> end;
        ArrayList<Pair<Integer, Integer>> linePoints;
        boolean isHorizontal = false;
        boolean isVertical = false;
        boolean isDiagonal;
        boolean isValidForPart2; //Diagonal, horizontal or vertical

        /*
         *  y=ax+b
         *  y1=ax1+b
         *  y2=ax2+b
         *
         *  b=y1-ax1
         *  a=(y2-b)/x2
         *
         *  y1+y2=ax1-ax2
         *  y1-y2=a(x1-x2)
         *  a=(y1-y2)/(x1-x2) == |1|
         *
         *  y=((y2-y1-ax1)/x2)x+y1-((y2-b)/x2)x1
         *
         * */

        Line(Pair<Integer, Integer> beginning, Pair<Integer, Integer> end) {
            this.beginning = beginning;
            this.end = end;
            linePoints = new ArrayList<>(findLinePoints());
            if (Objects.equals(beginning.getKey(), end.getKey()))
                isVertical = true;
            if (Objects.equals(beginning.getValue(), end.getValue()))
                isVertical = true;
            isDiagonal = isDiagonal();
            isValidForPart2 = isVertical || isHorizontal || isDiagonal;
        }

        private List<Pair<Integer, Integer>> findLinePoints() {
            ArrayList<Pair<Integer, Integer>> linePoints = new ArrayList<>();
            int xMin = beginning.getKey() > end.getKey() ? end.getKey() : beginning.getKey();
            int xMax = beginning.getKey() > end.getKey() ? beginning.getKey() : end.getKey();
            int yMin = beginning.getValue() > end.getValue() ? end.getValue() : beginning.getValue();
            int yMax = beginning.getValue() > end.getValue() ? beginning.getValue() : end.getValue();

            ArrayList<Integer> xs = new ArrayList<>();
            ArrayList<Integer> ys = new ArrayList<>();

            if (beginning.getKey() < end.getKey())
                for (int x = xMin; x <= xMax; x++) {
                    xs.add(x);
                }
            else
                for (int x = xMax; x >= xMin; x--) {
                    xs.add(x);
                }

            if (beginning.getValue() < end.getValue())
                for (int y = yMin; y <= yMax; y++) {
                    ys.add(y);
                }
            else
                for (int y = yMax; y >= yMin; y--) {
                    ys.add(y);
                }

            for (int i = 0; i < xs.size() || i < ys.size(); i++) {
                int curX = i;
                int curY = i;

                if (i >= xs.size())
                    curX = xs.size() - 1;
                if (i >= ys.size())
                    curY = ys.size() - 1;

                linePoints.add(Pair.of(xs.get(curX), ys.get(curY)));
            }

            return linePoints;
        }

        private boolean isDiagonal() {
            //a=(y1-y2)/(x1-x2) == |1|
            if ((beginning.getKey() - end.getKey()) == 0)
                return false;
            return Math.abs((beginning.getValue() - end.getValue()) / (beginning.getKey() - end.getKey())) == 1;
        }

    }
}

