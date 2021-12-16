package com.hiroszymon.aoc2021;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Packet {
    private enum LENGTH_TYPE_ID {LENGTH, COUNT}

    private int packetVersion;
    private int packetTypeID;
    private LENGTH_TYPE_ID lengthTypeID;
    private int subpacketsLength = -1;
    private int bitLength;
    private final List<Integer> dataGroups = new ArrayList<>();
    private final List<Packet> subpackets = new ArrayList<>();


    public static final int PACKET_TYPE_SUM = 0, PACKET_TYPE_PRODUCT = 1, PACKET_TYPE_MIN = 2, PACKET_TYPE_MAX = 3,
            PACKET_TYPE_GT = 5, PACKET_TYPE_LT = 6, PACKET_TYPE_EQUAL = 7;

    private Packet() {
    }

    public static Packet decode(String hexString) {
        byte[] hexStringAsBytes = new byte[hexString.length() / 2];
        for (int i = 0, j = 0; i < hexString.length(); i += 2, j++) {
            hexStringAsBytes[j] = (byte) Integer.parseUnsignedInt(hexString.substring(i, i + 2), 16);
        }

        StringBuilder tmp = new StringBuilder();
        for (byte b : hexStringAsBytes) {
            tmp.append(String.format("%08d", new BigInteger(Integer.toUnsignedString(Byte.toUnsignedInt(b), 2))));
        }

        String b = tmp.toString();

        int bitIndex = 0;

        Packet p = new Packet();
        p.packetVersion = Integer.parseInt(b.substring(bitIndex, bitIndex += 3), 2);
        p.packetTypeID = Integer.parseInt(b.substring(bitIndex, bitIndex += 3), 2);

        if (p.isOperator()) {
            char lengthID = b.charAt(bitIndex++);
            p.lengthTypeID = lengthID == '1' ? LENGTH_TYPE_ID.COUNT : LENGTH_TYPE_ID.LENGTH;

            p.subpacketsLength = Integer.parseInt(b.substring(bitIndex, bitIndex += (p.lengthTypeID == LENGTH_TYPE_ID.COUNT ? 11 : 15)), 2);
            bitIndex += p.decodeSubpackets(b.substring(bitIndex));

        } else if (p.isLiteral()) {
            while (b.charAt(bitIndex) == '1') {
                p.dataGroups.add(Integer.parseInt(b.substring(bitIndex + 1, bitIndex += 5), 2));
            }
            p.dataGroups.add(Integer.parseInt(b.substring(bitIndex + 1, bitIndex += 5), 2));

        }
        p.bitLength = bitIndex;

        return p;
    }

    public static List<Packet> decodeBinary(String binaryString, int maxLength, LENGTH_TYPE_ID lengthTypeID) {

        List<Packet> packets = new ArrayList<>();
        int bitIndex = 0;
        while (lengthTypeID == LENGTH_TYPE_ID.COUNT ? packets.size() < maxLength : bitIndex < maxLength) {
            int prevoiusBitIndex = bitIndex;
            Packet p = new Packet();
            p.packetVersion = Integer.parseInt(binaryString.substring(bitIndex, bitIndex += 3), 2);
            p.packetTypeID = Integer.parseInt(binaryString.substring(bitIndex, bitIndex += 3), 2);

            if (p.isOperator()) {
                char lengthID = binaryString.charAt(bitIndex++);
                p.lengthTypeID = lengthID == '1' ? LENGTH_TYPE_ID.COUNT : LENGTH_TYPE_ID.LENGTH;

                p.subpacketsLength = Integer.parseInt(binaryString.substring(bitIndex, bitIndex += (p.lengthTypeID == LENGTH_TYPE_ID.COUNT ? 11 : 15)), 2);
                bitIndex += p.decodeSubpackets(binaryString.substring(bitIndex));

            } else if (p.isLiteral()) {
                while (binaryString.charAt(bitIndex) == '1') {
                    p.dataGroups.add(Integer.parseInt(binaryString.substring(bitIndex + 1, bitIndex += 5), 2));
                }
                p.dataGroups.add(Integer.parseInt(binaryString.substring(bitIndex + 1, bitIndex += 5), 2));
            }
            p.bitLength = bitIndex - prevoiusBitIndex;
            packets.add(p);
        }

        return packets;
    }

    private int decodeSubpackets(String binaryString) {
        int usedBits = 0;
        if (lengthTypeID == LENGTH_TYPE_ID.COUNT) {
            List<Packet> tmp = decodeBinary(binaryString, this.subpacketsLength, lengthTypeID);
            tmp = tmp.subList(0, subpacketsLength);
            subpackets.addAll(tmp);
            usedBits = tmp.stream().mapToInt(Packet::getBitLength).sum();
        } else if (lengthTypeID == LENGTH_TYPE_ID.LENGTH) {
            List<Packet> tmp = decodeBinary(binaryString, this.subpacketsLength, lengthTypeID);
            subpackets.addAll(tmp);
            usedBits = tmp.stream().mapToInt(e -> e.bitLength).sum();
        }
        return usedBits;
    }

    public long performAction() {
        switch (packetTypeID) {
            case PACKET_TYPE_SUM:
                return subpackets.stream().mapToLong(Packet::performAction).sum();
            case PACKET_TYPE_PRODUCT:
                AtomicLong productResult = new AtomicLong(1);
                subpackets.stream().mapToLong(Packet::performAction).forEach(e -> productResult.set(productResult.get() * e));
                return productResult.get();
            case PACKET_TYPE_MIN:
                return subpackets.stream().mapToLong(Packet::performAction).min().orElseThrow();
            case PACKET_TYPE_MAX:
                return subpackets.stream().mapToLong(Packet::performAction).max().orElseThrow();
            case PACKET_TYPE_GT:
                return subpackets.get(0).performAction() > subpackets.get(1).performAction() ? 1 : 0;
            case PACKET_TYPE_LT:
                return subpackets.get(0).performAction() < subpackets.get(1).performAction() ? 1 : 0;
            case PACKET_TYPE_EQUAL:
                return subpackets.get(0).performAction() == subpackets.get(1).performAction() ? 1 : 0;
            default:
                return getValue();
        }

    }

    public int getPacketVersion() {
        return packetVersion;
    }

    public int getSubpacketsVersionSum() {
        return subpackets.size() > 0 ? subpackets.stream().mapToInt(Packet::getSubpacketsVersionSum).sum() + packetVersion : packetVersion;
    }

    public int getPacketTypeID() {
        return packetTypeID;
    }

    public LENGTH_TYPE_ID getLengthTypeID() {
        return lengthTypeID;
    }

    public int getSubpacketsLength() {
        return subpacketsLength;
    }

    public boolean isOperator() {
        return packetTypeID != 4;
    }

    public boolean isLiteral() {
        return packetTypeID == 4;
    }

    public List<Integer> getDataGroups() {
        return dataGroups;
    }

    public long getValue() {
        StringBuilder s = new StringBuilder();
        for (Integer i : getDataGroups()) {
            s.append(String.format("%04d", new BigInteger(Integer.toUnsignedString(i, 2))));
        }
        return Long.parseLong(s.toString(), 2);
    }

    public int getBitLength() {
        return bitLength;
    }

    public List<Packet> getSubpackets() {
        return subpackets;
    }


}
