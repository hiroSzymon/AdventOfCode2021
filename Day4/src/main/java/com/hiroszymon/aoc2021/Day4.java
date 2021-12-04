package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Day4 {
    private static List<Integer> drawnNumbers;

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        List<String> input = Common.readInput();
        drawnNumbers = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).toList();
        ArrayList<ArrayList<ArrayList<MutablePair<Integer, Boolean>>>> boards = new ArrayList<>();

        boards.add(new ArrayList<>());
        for (int line = 2, boardI = 0; line < input.size(); line++) {
            String currentLineS = input.get(line);
            if (currentLineS.isBlank()) {
                ++boardI;
                boards.add(new ArrayList<>());
                continue;
            }
            boards.get(boardI).add(new ArrayList<>(Arrays.stream(currentLineS.split("\\s")).filter(e -> !e.isBlank()).map(String::trim).map(e -> MutablePair.of(Integer.parseInt(e), false)).toList()));

        }
        Pair<Integer, Integer> boardWonIndexAndLastDrawn = gameOn(boards);
        long sum = boards.get(boardWonIndexAndLastDrawn.getKey()).stream().mapToLong(b -> b.stream().filter(c -> !c.getValue()).mapToInt(Pair::getKey).sum()).sum();
        System.out.println("Part 1: Board won: " + boardWonIndexAndLastDrawn + ", sum: " + sum + ", score: " + sum * boardWonIndexAndLastDrawn.getValue());

    }

    public static void part2() {
        List<String> input = Common.readInput();
        drawnNumbers = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).toList();
        ArrayList<ArrayList<ArrayList<MutablePair<Integer, Boolean>>>> boards = new ArrayList<>();

        boards.add(new ArrayList<>());
        for (int line = 2, boardI = 0; line < input.size(); line++) {
            String currentLineS = input.get(line);
            if (currentLineS.isBlank()) {
                ++boardI;
                boards.add(new ArrayList<>());
                continue;
            }
            boards.get(boardI).add(new ArrayList<>(Arrays.stream(currentLineS.split("\\s")).filter(e -> !e.isBlank()).map(String::trim).map(e -> MutablePair.of(Integer.parseInt(e), false)).toList()));

        }

        Pair<Integer, Integer> boardWonIndexAndLastDrawn = Pair.of(-1, -1);

        while (boards.size() > 0) {
            boardWonIndexAndLastDrawn = gameOn(boards);
            if (boards.size() > 1)
                boards.remove((int) boardWonIndexAndLastDrawn.getKey());
            else
                break;
        }

        long sum = boards.get(boardWonIndexAndLastDrawn.getKey()).stream().mapToLong(b -> b.stream().filter(c -> !c.getValue()).mapToInt(Pair::getKey).sum()).sum();
        System.out.println("Part 2: Board won last: " + boardWonIndexAndLastDrawn + ", sum: " + sum + ", score: " + sum * boardWonIndexAndLastDrawn.getValue());
    }

    public static void markNumber(ArrayList<ArrayList<MutablePair<Integer, Boolean>>> board, int value) {
        board.forEach(r -> r.forEach(c -> {
            if (c.getKey() == value)
                c.setValue(true);
        }));
    }

    public static boolean checkBoardWon(ArrayList<ArrayList<MutablePair<Integer, Boolean>>> board) {
        return board.stream().anyMatch(r -> r.stream().allMatch(Pair::getValue)) ||
                checkColumnWon(board);
    }

    public static boolean checkColumnWon(ArrayList<ArrayList<MutablePair<Integer, Boolean>>> board) {
        ArrayList<ArrayList<Boolean>> columnValues = new ArrayList<>();
        for (int col = 0; col < board.get(0).size(); col++) {
            columnValues.add(new ArrayList<>());
            for (ArrayList<MutablePair<Integer, Boolean>> row : board) {
                columnValues.get(col).add(row.get(col).getValue());
            }
        }
        return columnValues.stream().anyMatch(c -> c.stream().allMatch(b -> b));
    }

    public static Pair<Integer, Integer> gameOn(ArrayList<ArrayList<ArrayList<MutablePair<Integer, Boolean>>>> boards) {
        for (Integer drawn : drawnNumbers) {
            for (ArrayList<ArrayList<MutablePair<Integer, Boolean>>> board : boards) {
                markNumber(board, drawn);
                if (checkBoardWon(board))
                    return Pair.of(boards.indexOf(board), drawn);
            }
        }
        return Pair.of(-1, -1);
    }

}
