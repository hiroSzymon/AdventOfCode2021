package com.hiroszymon.aoc2021;

import java.util.List;

public class Day16 {
    private static final List<String> input = Common.readInput();

    public static void main(String[] args) {
        Packet packet = Packet.decode(input.get(0));
        part1(packet);
        part2(packet);
    }

    private static void part1(Packet outermostPacket) {
        System.out.println("Part 1: " + outermostPacket.getSubpacketsVersionSum());
    }

    private static void part2(Packet outermostPacket) {
        System.out.println("Part 2: " + outermostPacket.performAction());
    }


}
