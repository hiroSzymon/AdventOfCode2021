package com.hiroszymon.aoc2021;

import org.jetbrains.annotations.Nullable;

public class Tree {
    public Node rootNode;

    private Tree() {
    }

    public Tree add(Tree toAdd) {
        Tree t = new Tree();

        Node tmp = rootNode;
        t.rootNode = new Node();
        t.rootNode.nodeDepth = 0;
        t.rootNode.nodeLeft = tmp;
        t.rootNode.nodeRight = toAdd.rootNode;

        t.rootNode.nodeLeft.parent = t.rootNode;
        t.rootNode.nodeRight.parent = t.rootNode;

        incrementNodeDepth(t.rootNode.nodeLeft);
        incrementNodeDepth(t.rootNode.nodeRight);

        return t;
    }

    public Tree reduce() {
        while (true) {
            boolean exploded = explodeNode(rootNode);
            boolean splitted = false;
            if (!exploded) {
                splitted = splitNode(rootNode);
            }

            if (!exploded && !splitted)
                break;
        }
        return this;
    }

    private static boolean explodeNode(Tree.Node node) {
        if (node == null)
            return false;

        if (node.getNodeDepth() >= 4 && node.value == -1) {
            node.explode();
            return true;
        }

        return explodeNode(node.getNodeLeft()) || explodeNode(node.getNodeRight());
    }

    private static boolean splitNode(Tree.Node node) {
        if (node == null)
            return false;

        if (node.getValue() >= 10) {
            node.split();
            return true;
        }

        return splitNode(node.getNodeLeft()) || splitNode(node.getNodeRight());

    }

    private void incrementNodeDepth(Node node) {
        node.nodeDepth += 1;
        if (node.nodeLeft != null)
            incrementNodeDepth(node.nodeLeft);
        if (node.nodeRight != null)
            incrementNodeDepth(node.nodeRight);
    }

    public static Tree fromString(String input) {
        Tree t = new Tree();
        t.rootNode = Node.fromString(null, input, 0);
        return t;
    }

    public static class Node {
        private Node parent = null;
        private Node nodeLeft;
        private Node nodeRight;
        private int value = -1;
        private int nodeDepth = 0;

        private Node() {
        }

        public static Node fromInt(@Nullable Node parent, int input, int nodeDepth) {
            Node n = new Node();
            n.nodeDepth = nodeDepth;
            n.parent = parent;

            n.value = input;

            return n;
        }

        public static Node fromString(@Nullable Node parent, String input, int nodeDepth) {
            Node n = new Node();
            n.nodeDepth = nodeDepth;
            n.parent = parent;
            int depth = 0;

            String left, right;

            if (input.matches("\\[\\d+,\\d+]")) {
                left = input.split(",")[0];
                left = left.substring(1);
                right = input.split(",")[1];
                right = right.substring(0, right.length() - 1);
            } else {

                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '[') {
                        depth++;
                        continue;
                    } else if (input.charAt(i) == ']') {
                        depth--;
                    }

                    if (depth == 1 && input.charAt(i) == ',') {
                        depth = i;
                        break;
                    }
                }


                left = input.substring(1, depth);
                right = input.substring(depth + 1, input.length() - 1);
            }

            if (left.matches("\\d+")) {
                n.nodeLeft = Node.fromInt(n, Integer.parseInt(left), nodeDepth + 1);
            } else {
                n.nodeLeft = Node.fromString(n, left, nodeDepth + 1);
            }

            if (right.matches("\\d+")) {
                n.nodeRight = Node.fromInt(n, Integer.parseInt(right), nodeDepth + 1);
            } else {
                n.nodeRight = Node.fromString(n, right, nodeDepth + 1);
            }

            return n;
        }

        public void explode() {
            Node tmpNode = this;

            //left value
            while (tmpNode.parent != null && tmpNode.parent.nodeRight != tmpNode) {
                tmpNode = tmpNode.parent;
            }

            if (tmpNode.parent != null) {
                if (tmpNode.parent.nodeLeft.value != -1) {
                    tmpNode.parent.nodeLeft.value += nodeLeft.value;
                } else {
                    tmpNode = tmpNode.parent.nodeLeft;

                    while (tmpNode.nodeRight != null) {
                        tmpNode = tmpNode.nodeRight;
                    }

                    tmpNode.value += nodeLeft.value;

                }
            }

            //right value
            tmpNode = this;

            while (tmpNode.parent != null && tmpNode.parent.nodeLeft != tmpNode) {
                tmpNode = tmpNode.parent;
            }

            if (tmpNode.parent != null) {
                if (tmpNode.parent.nodeRight.value != -1) {
                    tmpNode.parent.nodeRight.value += nodeRight.value;
                } else {
                    tmpNode = tmpNode.parent.nodeRight;

                    while (tmpNode.nodeLeft != null) {
                        tmpNode = tmpNode.nodeLeft;
                    }

                    tmpNode.value += nodeRight.value;

                }
            }

            this.value = 0;
            this.nodeLeft = null;
            this.nodeRight = null;

        }

        public void split() {
            //System.out.println("Splitting: "+(this.valueLeft>=10?valueLeft:valueRight));

            if (value >= 10) {
                nodeLeft = Node.fromInt(this, (int) Math.floor(value / 2f), nodeDepth + 1);
                nodeRight = Node.fromInt(this, (int) Math.ceil(value / 2f), nodeDepth + 1);
            }
            value = -1;
        }


        public long magnitude() {
            if (value != -1)
                return value;

            return 3 * nodeLeft.magnitude() + 2 * nodeRight.magnitude();

        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (nodeLeft != null)
                b.append("[").append(nodeLeft);
            if (nodeRight != null)
                b.append(",").append(nodeRight).append(']');

            if (value != -1) {
                b.append(value);
            }

            return b.toString();
        }

        public Node getNodeLeft() {
            return nodeLeft;
        }

        public Node getNodeRight() {
            return nodeRight;
        }

        public int getValue() {
            return value;
        }

        public int getNodeDepth() {
            return nodeDepth;
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

}
