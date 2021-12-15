package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class Day15 {
    private static final List<String> input = Common.readInput();

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        SimpleDirectedWeightedGraph<Pair<Integer, Integer>, Integer> graph = getGraph(input);

        Pair<Integer, Integer> from = Pair.of(0, 0);
        Pair<Integer, Integer> to = Pair.of(input.size() - 1, (input.get(input.size() - 1).length()) - 1);

        GraphPath<Pair<Integer, Integer>, Integer> shortestPath = new DijkstraShortestPath<>(graph).getPath(from, to);
        System.out.println("Part 1: " + (int) shortestPath.getWeight());
    }

    private static void part2() {
        ArrayList<String> newInput = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            int originalRow = i % input.size();
            StringBuilder row = new StringBuilder(input.get(originalRow));
            for (int j = 0; j < input.get(originalRow).length() * 4; j++) {
                int originalVal = row.charAt(j) - 0x30;
                int newVal = originalVal + 1;
                if (newVal > 9) {
                    newVal -= 9;
                }
                row.append(newVal);
            }
            newInput.add(row.toString());
        }

        for (int i = 0; i < input.size() * 4; i++) {
            int originalRow = i % newInput.size();
            StringBuilder row = new StringBuilder(newInput.get(originalRow));
            int currentlyAddedSet;
            for (int j = 0; j < newInput.get(originalRow).length(); j++) {
                currentlyAddedSet = i / newInput.size() + 1;
                int originalVal = newInput.get(originalRow).charAt(j) - 0x30;
                int newVal = originalVal + currentlyAddedSet;
                if (newVal > 9) {
                    newVal -= 9;
                }
                row.setCharAt(j, (char) (newVal + 0x30));
            }
            newInput.add(row.toString());

        }

        SimpleDirectedWeightedGraph<Pair<Integer, Integer>, Integer> graph = getGraph(newInput);

        Pair<Integer, Integer> from = Pair.of(0, 0);
        Pair<Integer, Integer> to = Pair.of(newInput.size() - 1, (newInput.get(newInput.size() - 1).length()) - 1);

        GraphPath<Pair<Integer, Integer>, Integer> shortestPath = new DijkstraShortestPath<>(graph).getPath(from, to);
        System.out.println("Part 2: " + (int) shortestPath.getWeight());
    }

    private static SimpleDirectedWeightedGraph<Pair<Integer, Integer>, Integer> getGraph(List<String> input) {
        SimpleDirectedWeightedGraph<Pair<Integer, Integer>, Integer> graph = new SimpleDirectedWeightedGraph<>(Integer.class);
        int i = 0;
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row % input.size()).length(); col++) {
                try {
                    graph.addVertex(Pair.of(row, col));

                    if (row - 1 >= 0) {
                        int valOne = ((input.get((row - 1) % input.size()).charAt(col % input.get(row % input.size()).length()) - 0x30) + (row / input.size()));
                        graph.addVertex(Pair.of(row - 1, col));
                        graph.addEdge(Pair.of(row, col), Pair.of(row - 1, col), i);
                        graph.setEdgeWeight(i++, valOne);
                    }

                    if (col - 1 >= 0) {
                        int valTwo = ((input.get(row % input.size()).charAt((col - 1) % input.get(row % input.size()).length()) - 0x30) + (row / input.size()));
                        graph.addVertex(Pair.of(row, col - 1));
                        graph.addEdge(Pair.of(row, col), Pair.of(row, col - 1), i);
                        graph.setEdgeWeight(i++, valTwo);
                    }

                    if (row + 1 < input.size()) {
                        int valThree = ((input.get((row + 1) % input.size()).charAt(col % input.get(row % input.size()).length()) - 0x30) + (row / input.size()));
                        graph.addVertex(Pair.of(row + 1, col));
                        graph.addEdge(Pair.of(row, col), Pair.of(row + 1, col), i);
                        graph.setEdgeWeight(i++, valThree);

                    }

                    if (col + 1 < input.get(row % input.size()).length()) {
                        int valFour = ((input.get(row % input.size()).charAt((col + 1) % input.get(row % input.size()).length()) - 0x30) + (row / input.size()));
                        graph.addVertex(Pair.of(row, col + 1));
                        graph.addEdge(Pair.of(row, col), Pair.of(row, col + 1), i);
                        graph.setEdgeWeight(i++, valFour);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return graph;
    }


}
