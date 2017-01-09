package com.dalonedrow.rpg.graph;

/**
 * An edge is a pair of vertices, ordered or unordered.
 * @author DaLoneDrau
 */
public class GraphEdge {
    /** the index of the 1st of the {@link GraphNode}s this edge connects. */
    private final int from;
    /** the index of the 2nd of the {@link GraphNode}s this edge connects. */
    private final int to;
    /**
     * Creates a new instance of {@link GraphEdge} from an existing source.
     * @param edge the edge being cloned
     */
    public GraphEdge(final GraphEdge edge) {
        from = edge.from;
        to = edge.to;
    }
	/**
     * Creates a new instance of {@link GraphEdge}.
     * @param f the index of the 1st {@link GraphNode}
     * @param t the index of the 2nd {@link GraphNode}
     */
    public GraphEdge(final int f, final int t) {
        from = f;
        to = t;
    }
    /**
     * Determines if this {@link GraphEdge} connects two nodes exactly in the
     * direction provided.
     * @param e the second {@link GraphEdge}
     * @return true if the {@link GraphEdge} connects two nodes in either
     *         direction; false otherwise
     */
    public final boolean equalsDirected(final GraphEdge e) {
        return from == e.from && to == e.to;
    }
    /**
     * Determines if this {@link GraphEdge} connects two nodes exactly in the
     * direction provided.
     * @param f the node the edge is coming from
     * @param t the node the edge is going to
     * @return true if the {@link GraphEdge} connects two nodes exactly in the
     *         direction provided; false otherwise
     */
    public final boolean equalsDirected(final int f, final int t) {
        return from == f && to == t;
    }
    /**
     * Determines if this {@link GraphEdge} is the same as a second
     * {@link GraphEdge}.
     * @param e the second {@link GraphEdge}
     * @return true if the {@link GraphEdge} connects two nodes in either
     *         direction; false otherwise
     */
    public final boolean equalsUndirected(final GraphEdge e) {
        return (from == e.from && to == e.to) || (from == e.to && to == e.from);
    }
    /**
     * Determines if this {@link GraphEdge} connects two nodes in either
     * direction.
     * @param e0 the first node
     * @param e1 the second node
     * @return true if the {@link GraphEdge} connects two nodes in either
     *         direction; false otherwise
     */
    public final boolean equalsUndirected(final int e0, final int e1) {
        return (from == e0 && to == e1) || (from == e1 && to == e0);
    }
    /**
     * Gets the index of the 1st {@link GraphNode}s this edge connects.
     * @return <code>int</code>
     */
    public final int getFrom() {
        return from;
    }
    /**
     * Gets the index of the 2nd {@link GraphNode}s this edge connects.
     * @return <code>int</code>
     */
    public final int getTo() {
        return to;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
		return "[from=" + from + ",to=" + to + "]";
    }

}
