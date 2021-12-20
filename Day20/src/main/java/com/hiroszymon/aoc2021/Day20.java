package com.hiroszymon.aoc2021;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day20 {
    //Slow as hell but works
    private static final List<String> input = Common.readInput();
    private static final String algorithm = input.get(0);
    private static final Map<Pair<Integer, Integer>, Character> inputImage = new HashMap<>();
    private static final List<String> inputImageString = input.subList(2, input.size());
    private static final List<List<Pair<Integer, Integer>>> bitPositions = generateBitPositions();

    public static void main(String[] args) {
        fillInputImage();
        part1();
        part2();
    }

    private static void fillInputImage() {
        //Fill with 10 "empty" characters around original input to make enhancement easier
        for (int row = 0; row < inputImageString.size() + 20; row++) {
            for (int col = 0; col < inputImageString.get(0).length() + 20; col++) {
                inputImage.put(Pair.of(row, col), '.');
            }
        }


        for (int row = 0; row < inputImageString.size(); row++) {
            for (int col = 0; col < inputImageString.get(row).length(); col++) {
                inputImage.put(Pair.of(row + 10, col + 10), inputImageString.get(row).charAt(col));
            }
        }
    }

    private static void part1() {
        Map<Pair<Integer, Integer>, Character> outputImage = enhanceImage(2);
        System.out.println("Part 1: " + outputImage.entrySet().stream().filter(e -> e.getValue() == '#').count());

    }

    private static void part2() {
        Map<Pair<Integer, Integer>, Character> outputImage = enhanceImage(50);
        System.out.println("Part 2: " + outputImage.entrySet().stream().filter(e -> e.getValue() == '#').count());
    }

    private static Map<Pair<Integer, Integer>, Character> enhanceImage(int maxCount) {
        Map<Pair<Integer, Integer>, Character> currentInputImage = new HashMap<>(inputImage);

        Map<Pair<Integer, Integer>, Character> outputImage = new HashMap<>();
        for (int count = 0; count < maxCount; count++) {

            for (Map.Entry<Pair<Integer, Integer>, Character> inputCharacter : currentInputImage.entrySet()) {
                for (int i = 0; i < 9; i++) {
                    Pair<Pair<Integer, Integer>, Character> entry = generateOutputPixelFromCharacterAsPos(inputCharacter, i, count, currentInputImage);
                    outputImage.put(entry.getKey(), entry.getValue());
                }
            }
            currentInputImage = new HashMap<>(outputImage);
        }
        return outputImage;
    }

    @SuppressWarnings("DuplicatedCode")
    private static List<List<Pair<Integer, Integer>>> generateBitPositions() {
        List<List<Pair<Integer, Integer>>> bitPos = new ArrayList<>();

        //inputCharacter\.getKey\(\)\.get(Right|Left)\(\)\s+[\+\-]

        ArrayList<Pair<Integer, Integer>> bitPosCase0 = new ArrayList<>();
        bitPosCase0.add(Pair.of(0, 0));
        bitPosCase0.add(Pair.of(0, 1));
        bitPosCase0.add(Pair.of(0, 2));
        bitPosCase0.add(Pair.of(1, 0));
        bitPosCase0.add(Pair.of(1, 1));
        bitPosCase0.add(Pair.of(1, 2));
        bitPosCase0.add(Pair.of(2, 0));
        bitPosCase0.add(Pair.of(2, 1));
        bitPosCase0.add(Pair.of(2, 2));
        bitPos.add(bitPosCase0);

        ArrayList<Pair<Integer, Integer>> bitPosCase1 = new ArrayList<>();
        bitPosCase1.add(Pair.of(0, -1));
        bitPosCase1.add(Pair.of(0, 0));
        bitPosCase1.add(Pair.of(0, 1));
        bitPosCase1.add(Pair.of(1, -1));
        bitPosCase1.add(Pair.of(1, 0));
        bitPosCase1.add(Pair.of(1, 1));
        bitPosCase1.add(Pair.of(2, -1));
        bitPosCase1.add(Pair.of(2, 0));
        bitPosCase1.add(Pair.of(2, 1));
        bitPos.add(bitPosCase1);

        ArrayList<Pair<Integer, Integer>> bitPosCase2 = new ArrayList<>();
        bitPosCase2.add(Pair.of(0, -2));
        bitPosCase2.add(Pair.of(0, -1));
        bitPosCase2.add(Pair.of(0, 0));
        bitPosCase2.add(Pair.of(1, -2));
        bitPosCase2.add(Pair.of(1, -1));
        bitPosCase2.add(Pair.of(1, 0));
        bitPosCase2.add(Pair.of(2, -2));
        bitPosCase2.add(Pair.of(2, -1));
        bitPosCase2.add(Pair.of(2, 0));
        bitPos.add(bitPosCase2);

        ArrayList<Pair<Integer, Integer>> bitPosCase3 = new ArrayList<>();
        bitPosCase3.add(Pair.of(-1, 0));
        bitPosCase3.add(Pair.of(-1, 1));
        bitPosCase3.add(Pair.of(-1, 2));
        bitPosCase3.add(Pair.of(0, 0));
        bitPosCase3.add(Pair.of(0, 1));
        bitPosCase3.add(Pair.of(0, 2));
        bitPosCase3.add(Pair.of(1, 0));
        bitPosCase3.add(Pair.of(1, 1));
        bitPosCase3.add(Pair.of(1, 2));
        bitPos.add(bitPosCase3);


        ArrayList<Pair<Integer, Integer>> bitPosCase4 = new ArrayList<>();
        bitPosCase4.add(Pair.of(-1, -1));
        bitPosCase4.add(Pair.of(-1, 0));
        bitPosCase4.add(Pair.of(-1, 1));
        bitPosCase4.add(Pair.of(0, -1));
        bitPosCase4.add(Pair.of(0, 0));
        bitPosCase4.add(Pair.of(0, 1));
        bitPosCase4.add(Pair.of(1, -1));
        bitPosCase4.add(Pair.of(1, 0));
        bitPosCase4.add(Pair.of(1, 1));
        bitPos.add(bitPosCase4);

        ArrayList<Pair<Integer, Integer>> bitPosCase5 = new ArrayList<>();
        bitPosCase5.add(Pair.of(-1, -2));
        bitPosCase5.add(Pair.of(-1, -1));
        bitPosCase5.add(Pair.of(-1, 0));
        bitPosCase5.add(Pair.of(0, -2));
        bitPosCase5.add(Pair.of(0, -1));
        bitPosCase5.add(Pair.of(0, 0));
        bitPosCase5.add(Pair.of(1, -2));
        bitPosCase5.add(Pair.of(1, -1));
        bitPosCase5.add(Pair.of(1, 0));
        bitPos.add(bitPosCase5);

        ArrayList<Pair<Integer, Integer>> bitPosCase6 = new ArrayList<>();
        bitPosCase6.add(Pair.of(-2, 0));
        bitPosCase6.add(Pair.of(-2, 1));
        bitPosCase6.add(Pair.of(-2, 2));
        bitPosCase6.add(Pair.of(-1, 0));
        bitPosCase6.add(Pair.of(-1, 1));
        bitPosCase6.add(Pair.of(-1, 2));
        bitPosCase6.add(Pair.of(0, 0));
        bitPosCase6.add(Pair.of(0, 1));
        bitPosCase6.add(Pair.of(0, 2));
        bitPos.add(bitPosCase6);

        ArrayList<Pair<Integer, Integer>> bitPosCase7 = new ArrayList<>();
        bitPosCase7.add(Pair.of(-2, -1));
        bitPosCase7.add(Pair.of(-2, 0));
        bitPosCase7.add(Pair.of(-2, 1));
        bitPosCase7.add(Pair.of(-1, -1));
        bitPosCase7.add(Pair.of(-1, 0));
        bitPosCase7.add(Pair.of(-1, 1));
        bitPosCase7.add(Pair.of(0, -1));
        bitPosCase7.add(Pair.of(0, 0));
        bitPosCase7.add(Pair.of(0, 1));
        bitPos.add(bitPosCase7);

        ArrayList<Pair<Integer, Integer>> bitPosCase8 = new ArrayList<>();
        bitPosCase8.add(Pair.of(-2, -2));
        bitPosCase8.add(Pair.of(-2, -1));
        bitPosCase8.add(Pair.of(-2, 0));
        bitPosCase8.add(Pair.of(-1, -2));
        bitPosCase8.add(Pair.of(-1, -1));
        bitPosCase8.add(Pair.of(-1, 0));
        bitPosCase8.add(Pair.of(0, -2));
        bitPosCase8.add(Pair.of(0, -1));
        bitPosCase8.add(Pair.of(0, 0));
        bitPos.add(bitPosCase8);

        return bitPos;
    }

    private static Pair<Pair<Integer, Integer>, Character> generateOutputPixelFromCharacterAsPos(Map.Entry<Pair<Integer, Integer>, Character> inputCharacter,
                                                                                                 int pos, int iteration, Map<Pair<Integer, Integer>, Character> currentInputImage) {
        final int FIFTH_BIT_POS = 4;
        String emptyBit = iteration % 2 == 0 ? "." : "#";

        StringBuilder tmpBinary = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            Pair<Integer, Integer> bitPosition = Pair.of(inputCharacter.getKey().getKey() + bitPositions.get(pos).get(i).getKey(), inputCharacter.getKey().getValue() + bitPositions.get(pos).get(i).getValue());
            tmpBinary.append(currentInputImage.containsKey(bitPosition) ? currentInputImage.get(bitPosition) : emptyBit);
        }

        Pair<Integer, Integer> bitPosition = Pair.of(inputCharacter.getKey().getKey() + bitPositions.get(pos).get(FIFTH_BIT_POS).getKey(), inputCharacter.getKey().getValue() + bitPositions.get(pos).get(FIFTH_BIT_POS).getValue());
        return Pair.of(bitPosition, algorithm.charAt(Integer.parseInt(tmpBinary.toString().replace('#', '1').replace('.', '0'), 2)));

    }


}
