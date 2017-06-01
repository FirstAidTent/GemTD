package com.example.firstaidtent.gemtd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

class AStar {
    private Map<Node, Node> cameFrom = new HashMap<>();
    private Map<Node, Double> costSoFar = new HashMap<>();

    private Node start;
    private Node goal;

    private static double heuristic(Node a, Node b) {
        return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }

    AStar(WeightedGraph<Node> graph, int x1, int y1, int x2, int y2) {
        Comparator<NodeTuple> nodeComparator = new Comparator<NodeTuple>() {
            @Override
            public int compare(NodeTuple o1, NodeTuple o2) {
                if (o1.getPriority() < o2.getPriority()) {
                    return -1;
                } else if (o1.getPriority() > o2.getPriority()) {
                    return 1;
                }
                return 0;
            }
        };

        Queue<NodeTuple> frontier = new PriorityQueue<>(100, nodeComparator);

        start = graph.getClosestNode(x1, y1);
        goal = graph.getClosestNode(x2, y2);

        frontier.add(new NodeTuple(start, 0.0));

        cameFrom.put(start, start);
        costSoFar.put(start, 0.0);

        while (frontier.size() > 0) {
            Node current = frontier.poll().getNode();

            if (current.equals(goal)) {
                break;
            }

            for (Node next : graph.neighbors(current)) {
                double new_cost = costSoFar.get(current) + graph.cost(next);

                if (!costSoFar.containsKey(next) || new_cost < costSoFar.get(next)) {
                    costSoFar.put(next, new_cost);
                    double priority = new_cost + heuristic(next, goal);
                    frontier.add(new NodeTuple(next, priority));
                    cameFrom.put(next, current);
                }
            }
        }
    }

    public Map<Node, Node> getCameFrom() {
        return cameFrom;
    }

    public Map<Node, Double> getCostSoFar() {
        return costSoFar;
    }

    public Node getStart() {
        return start;
    }

    public Node getGoal() {
        return goal;
    }
}

interface WeightedGraph<N> {
    double cost(N node);
    Set<N> neighbors(N node);
    Node getClosestNode(int x, int y);
}



class NodeTuple {
    private Node node;
    private double priority;

    NodeTuple(Node node, double priority) {
        this.node = node;
        this.priority = priority;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }
}



class Node {
    public int x;
    public int y;
    private double cost;
    private boolean passable;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = 0.0;
        passable = true;
    }

    Node(int x, int y, double cost) {
        this(x, y);
        this.cost = cost;
        passable = true;
    }

    Node(int x, int y, double cost, boolean passable) {
        this(x, y, cost);
        this.passable = passable;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }
}




class SquareGrid implements WeightedGraph<Node> {
    private List<Node> nodes = new ArrayList<>();
    private int width, height;
    private double space;


    SquareGrid(int width, int height, int space, int divider) {
        this.width = width;
        this.height = height;
        this.space = ((double) space) / divider;
        int x;
        int y;

        double multiplier = 1.0 / divider;
        for (int iy = 0; iy <= (width / space) * 2; iy++) {
            y = (int) (space * (multiplier * iy));
            for (int ix = 0; ix <= (width / space) * 2; ix++) {
                x = (int) (space * (multiplier * ix));
                Node n = new Node(x, y, 1.0);
                nodes.add(n);
            }
        }
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x <= width
                && y >= 0 && y <= height;
    }

    public double cost(Node node) {
        return node.getCost();
    }

    public Set<Node> neighbors(Node node) {
        Set<Node> neighborNodes = new HashSet<>();
        double distance;
        double diag_space;
        Node a, b;

        for (Node next : nodes) {
            if (next.equals(node)) {
                continue;
            }

            distance = Math.sqrt(Math.pow(next.x - node.x, 2) + Math.pow(next.y - node.y, 2));
            diag_space = Math.sqrt(Math.pow(space, 2) * 2);

            if (distance <= Math.ceil(diag_space) && next.isPassable()) {
                if (Math.abs(next.x - node.x) > 0 && Math.abs(next.y - node.y) > 0) {
                    a = getClosestNode(node.x + (next.x - node.x), node.y);
                    b = getClosestNode(node.x, node.y + (next.y - node.y));
                    if (!a.isPassable() && !b.isPassable()) {
                        continue;
                    }
                }
                neighborNodes.add(next);
            }
//            if (distance <= Math.ceil(space) && next.isPassable()) {
//                neighborNodes.add(next);
//            }
        }

        return neighborNodes;
    }

    public Node getClosestNode(int x, int y) {
        double distance;
        double min = width + height;
        Node closest = null;

        if (!inBounds(x, y)) {
            return null;
        }

        for (Node node : nodes) {
            distance = Math.sqrt(Math.pow(x - node.x, 2) + Math.pow(y - node.y, 2));
            if (distance < min) {
                min = distance;
                closest = node;
                if (min < space / 2.0) {
                    break;
                }
            }
        }
        return closest;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}