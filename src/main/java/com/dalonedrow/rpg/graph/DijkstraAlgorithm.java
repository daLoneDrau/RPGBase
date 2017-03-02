package com.dalonedrow.rpg.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {
    private Map<GraphNode, Double> distance;
    private final List<GraphEdge> edges;
    private final List<GraphNode> nodes;
    private Map<GraphNode, GraphNode> predecessors;
    private Set<GraphNode> settledNodes;
    private Set<GraphNode> unSettledNodes;
    public DijkstraAlgorithm(EdgeWeightedUndirectedGraph graph) {
        // create a copy of the array so that we can operate on this array
        nodes = new ArrayList<GraphNode>(Arrays.asList(graph.getVertexes()));
        edges = new ArrayList<GraphEdge>(Arrays.asList(graph.getEdges()));
    }
    public void execute(GraphNode source) {
        settledNodes = new HashSet<GraphNode>();
        unSettledNodes = new HashSet<GraphNode>();
        distance = new HashMap<GraphNode, Double>();
        predecessors = new HashMap<GraphNode, GraphNode>();
        distance.put(source, 0d);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            GraphNode node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }
    /**
     * Finds the minimal distances between a node and its neighbors.
     * @param node the node
     */
    private void findMinimalDistances(final GraphNode node) {
        List<GraphNode> adjacentNodes = getNeighbors(node);
        for (GraphNode target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }
    private double getDistance(GraphNode node, GraphNode target) {
        for (GraphEdge edge : edges) {
            if ((edge.getFrom() == node.getIndex()
                    && edge.getTo() == target.getIndex())
                    || (edge.getTo() == node.getIndex()
                            && edge.getFrom() == target.getIndex())) {
                return ((WeightedGraphEdge) edge).getCost();
            }
        }
        throw new RuntimeException("Should not happen");
    }
    private GraphNode getMinimum(Set<GraphNode> vertexes) {
        GraphNode minimum = null;
        for (GraphNode vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(
                        minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }
    /**
     * Gets all neighbors to a vertex.
     * @param node the vertex
     * @return {@link List}<{@link GraphNode}>
     */
    private List<GraphNode> getNeighbors(final GraphNode node) {
        List<GraphNode> neighbors = new ArrayList<GraphNode>();
        for (GraphEdge edge : edges) {
            // check each edge BI-DIRECTIONALLY
            if (edge.getFrom() == node.getIndex()
                    && !isSettled(getNodeByIndex(edge.getTo()))) {
                neighbors.add(getNodeByIndex(edge.getTo()));
            } else if (edge.getTo() == node.getIndex()
                    && !isSettled(getNodeByIndex(edge.getFrom()))) {
                neighbors.add(getNodeByIndex(edge.getFrom()));
            }
        }
        return neighbors;
    }
    /**
     * Gets a node by its index.
     * @param index the node index
     * @return {@link GraphNode}
     */
    private GraphNode getNodeByIndex(final int index) {
        GraphNode node = null;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nodes.get(i).getIndex() == index) {
                node = nodes.get(i);
            }
        }
        return node;
    }
    public LinkedList<GraphNode> getPath(final GraphNode target) {
        LinkedList<GraphNode> path = new LinkedList<GraphNode>();
        GraphNode step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
    /**
     * Gets the shortest distance to a node.  If no distance has been set,
     * Integer.MAX_VALUE is returned.
     * @param destination the destination {@link GraphNode}
     * @return {@link double}
     */
    private double getShortestDistance(final GraphNode destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }
    /**
     * Determines if a node has been settled yet.
     * @param vertex the node
     * @return <tt>true</tt> if the node was settled; <tt>false</tt> otherwise
     */
    private boolean isSettled(final GraphNode vertex) {
        return settledNodes.contains(vertex);
    }
}
