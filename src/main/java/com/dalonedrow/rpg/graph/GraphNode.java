package com.dalonedrow.rpg.graph;

import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * A vertex (plural vertices) or node is the fundamental unit of which graphs
 * are formed.
 * @author DaLoneDrau
 */
public class GraphNode {
    /** the <code>GraphNode</code>'s index. must be >= 0. */
    private int    index;
    /** the <code>GraphNode</code>'s name. can be null. */
    private char[] name;
    /**
     * Creates a new instance of {@link GraphNode}.
     */
    public GraphNode() {
        index = -1;
    }
    /**
     * Creates a new instance of {@link GraphNode}.
     * @param newName the <code>GraphNode</code>'s name
     * @param i the <code>GraphNode</code>'s index. must be >= 0
     */
    public GraphNode(final char[] newName, final int i) throws RPGException {
        if (i < 0) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
            		"Index must be greater than or equal to 0");
        }
        name = newName;
        index = i;
    }
    /**
     * Creates a new instance of {@link GraphNode} from an existing source.
     * @param vertex the source vertex
     */
    public GraphNode(final GraphNode vertex) {
        index = vertex.index;
        name = new char[vertex.name.length];
        System.arraycopy(vertex.name, 0, name, 0, name.length);
    }
    /**
     * Creates a new instance of {@link GraphNode}.
     * @param ind the index to set
     * @throws RPGException if the index < 0
     */
    public GraphNode(final int ind) throws RPGException {
        if (ind < 0) {
            throw new RPGException(ErrorMessage.BAD_PARAMETERS,
            		"Index must be greater than or equal to 0");
        }
        index = ind;
    }
    /**
     * Creates a new instance of {@link GraphNode}.
     * @param newName the <code>GraphNode</code>'s name
     * @param i the <code>GraphNode</code>'s index. must be >= 0
     * @throws RPGException if the index < 0
     */
    public GraphNode(final String newName, final int i) throws RPGException {
        this(newName.toCharArray(), i);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        boolean equals = false;
        if (this == obj) {
            equals = true;
        } else if (obj != null) {
            if (obj instanceof GraphNode) {
                if (((GraphNode) obj).index == index) {
                    equals = true;
                }
            }
        }
        return equals;
    }
    /**
     * Gets the the {@link GraphNode}'s index.
     * @return <code>int</code>
     */
    public final int getIndex() {
        return index;
    }
    /**
     * Gets the name.
     * @return {@link char[]}
     */
    public final char[] getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    /**
     * Sets the the {@link GraphNode}'s index.
     * @param ind the index to set
     */
    public final void setIndex(final int ind) {
        index = ind;
    }
    /**
     * Sets the value for the name.
     * @param val the value to set
     */
    public final void setName(final char[] val) {
        name = val;
    }
    /**
     * Sets the value for the name.
     * @param val the value to set
     */
    public final void setName(final String val) {
        name = val.toCharArray();
    }
}
