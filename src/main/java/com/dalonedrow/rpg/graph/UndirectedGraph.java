package com.dalonedrow.rpg.graph;

import java.io.InputStream;

import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * A Graph is a set of vertices and a collection of edges that each connect a
 * pair of vertices. An undirected graph is a graph where all edges are
 * bidirectional
 * @author drau
 */
public class UndirectedGraph {
    /** the graph's set of edges. */
    private GraphEdge[] edges;
    /** the graph's set of vertices. */
    private GraphNode[] vertices;
    /**
     * Creates a new instance of {@link UndirectedGraph} from an existing 
     * source.
     * @param g the graph being cloned
     */
    public UndirectedGraph(final UndirectedGraph g) {
        vertices = new GraphNode[g.vertices.length];
        System.arraycopy(g.vertices, 0, vertices, 0, g.vertices.length);
        edges = new GraphEdge[g.edges.length];
        System.arraycopy(g.edges, 0, edges, 0, g.edges.length);
    }
    /**
     * Reads a graph from an input stream.
     * @param in the input stream
     * @throws Exception if an error occurs
     */
    public UndirectedGraph(final InputStream in) throws Exception {
        // TODO Auto-generated method stub
    }
    /**
     * Creates a new instance of {@link UndirectedGraph} with a specific number
     * of vertices and no edges.
     * @param numVertices the number of vertices
     * @throws RPGException if an error occurs
     */
    public UndirectedGraph(final int numVertices) throws RPGException {
        vertices = new GraphNode[numVertices];
        for (int i = numVertices - 1; i >= 0; i--) {
            vertices[i] = new GraphNode(i);
        }
        edges = new GraphEdge[0];
    }
    /**
     * Adds edge v-w to this graph.
     * @param v vertex v's id
     * @param w vertex w's id
     * @throws RPGException if an error occurs
     */
    public final void addEdge(final int v, final int w) throws RPGException {
        if (!hasEdge(v, w)) {
            if (!hasVertex(v)) {
                addVertex(v);
            }
            if (!hasVertex(w)) {
                addVertex(w);
            }
            edges = ArrayUtilities.getInstance().extendArray(
            		new GraphEdge(v, w), edges);
        }
    }
    /**
     * Adds edge v-w to this graph.
     * @param e {@link GraphEdge} v-w
     * @throws RPGException if an error occurs
     */
    public final void addEdge(final GraphEdge e) throws RPGException {
        if (!hasEdge(e)) {
            if (!hasVertex(e.getFrom())) {
                addVertex(e.getFrom());
            }
            if (!hasVertex(e.getTo())) {
                addVertex(e.getTo());
            }
            edges = ArrayUtilities.getInstance().extendArray(
            		new GraphEdge(e), edges);
        }
    }
    /**
     * Adds a vertex to the graph.
     * @param v the vertex instance
     * @throws RPGException if an error occurs
     */
    public final void addVertex(final GraphNode v) throws RPGException {
        int i = getEmptyVertexSlot();
        if (i == -1) {
            i = vertices.length;
            vertices = ArrayUtilities.getInstance().extendArray(v, vertices);
        } else {
        	vertices[i] = v;
        }
    }
    /**
     * Adds a vertex to the graph.
     * @param v the vertex id
     * @throws RPGException if an error occurs
     */
    public final void addVertex(final int v) throws RPGException {
        int i = getEmptyVertexSlot();
        GraphNode n = new GraphNode(v);
        if (i == -1) {
            i = vertices.length;
            vertices = ArrayUtilities.getInstance().extendArray(n, vertices);
        } else {
        	vertices[i] = new GraphNode(v);
        }
    }
    /**
     * Gets the set of all vertices adjacent to vertex v.
     * @param v vertex v
     * @return <code>int</code>[]
     */
    public final int[] getAdjacencies(final int v) {
        int[] adjacencies = new int[0];
        for (int i = edges.length - 1; i >= 0; i--) {
            if (edges[i].getFrom() == v) {
            	adjacencies = ArrayUtilities.getInstance().extendArray(
            			edges[i].getTo(), adjacencies);
            } else if (edges[i].getTo() == v) {
            	adjacencies = ArrayUtilities.getInstance().extendArray(
            			edges[i].getFrom(), adjacencies);
            }
        }
        return adjacencies;
    }
    /**
     * Gets the location of an empty index in the set of vertices.
     * @return <code>int</code>
     */
    private int getEmptyVertexSlot() {
        int index = -1;
        for (int i = 0, len = vertices.length; i < len; i++) {
            if (vertices[i] == null) {
                index = i;
                break;
            } else if (vertices[i].getIndex() == -1) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Gets the graph's number of edges.
     * @return <code>int</code>
     */
    public final int getNumberOfEdges() {
        int numEdges = 0;
        for (int i = edges.length - 1; i >= 0; i--) {
        	if (edges[i] != null) {
        		numEdges++;
        	}
        }
        return numEdges;
    }
    /**
     * Gets the graph's number of vertices.
     * @return <code>int</code>
     */
    public final int getNumberOfVertices() {
        int numVertices = 0;
        for (int i = vertices.length - 1; i >= 0; i--) {
            if (vertices[i].getIndex() > -1) {
                numVertices++;
            }
        }
        return numVertices;
    }
    /**
     * Gets a vertex by its id.
     * @param id the vertex' id
     * @return {@link GraphNode}
     */
    public final GraphNode getVertex(final int id) {
        GraphNode v = null;
        for (int i = vertices.length - 1; i >= 0; i--) {
            if (vertices[i].getIndex() == id) {
                v = vertices[i];
                break;
            }
        }
        return v;
    }
    /**
     * Determines if edge v-w exists on the graph.
     * @param v vertex v
     * @param w vertex w
     * @return true if edge v-w exists; false otherwise
     */
    private boolean hasEdge(final int v, final int w) {
        boolean exists = false;
        for (int i = edges.length - 1; i >= 0; i--) {
            if (edges[i].getFrom() == v && edges[i].getTo() == w) {
                exists = true;
                break;
            }
            if (edges[i].getFrom() == w && edges[i].getTo() == v) {
                exists = true;
                break;
            }
        }
        return exists;
    }
    /**
     * Determines if edge v-w exists on the graph.
     * @param e {GraphEdge} v-w
     * @return true if edge v-w exists; false otherwise
     */
    private boolean hasEdge(final GraphEdge e) {
        boolean exists = false;
        for (int i = edges.length - 1; i >= 0; i--) {
            if (edges[i].equalsUndirected(e)) {
                exists = true;
                break;
            }
        }
        return exists;
    }
    /**
     * Determines if vertex v exists in the graph.
     * @param vertexId the id of vertex v
     * @return true if vertex v exists; false otherwise
     */
    private boolean hasVertex(final int vertexId) {
        boolean exists = false;
        for (int i = vertices.length - 1; i >= 0; i--) {
            if (vertices[i].getIndex() == vertexId) {
                exists = true;
                break;
            }
        }
        return exists;
    }
    /**
     * Removes edge v-w from the set of edges.
     * @param v vertex v
     * @param w vertex w
     * @return true if the edge was removed; false otherwise
     */
    public final boolean removeEdge(final int v, final int w) {
        boolean removed = false;
        if (hasEdge(v, w)) {
            int i;
            for (i = edges.length - 1; i >= 0; i--) {
                if (edges[i].equalsUndirected(v, w)) {
                    break;
                }
            }
            edges = ArrayUtilities.getInstance().removeIndex(i, edges);
            removed = true;
        }
        return removed;
    }
}
