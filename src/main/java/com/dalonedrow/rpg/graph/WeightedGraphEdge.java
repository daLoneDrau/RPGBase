package com.dalonedrow.rpg.graph;

/**
 * 
 * @author drau
 *
 */
public final class WeightedGraphEdge extends GraphEdge {
	/** the cost of traversing the <code>GraphEdge</code>. */
	private double	cost;
	/**
	 * Creates a new instance of {@link WeightedGraphEdge}.
	 * @param f the index of the 1st {@link GraphNode}
	 * @param t the index of the 2nd {@link GraphNode}
	 * @param c the cost of traversing the {@link WeightedGraphEdge}
	 */
	public WeightedGraphEdge(final int f, final int t, final double c) {
		super(f, t);
		cost = c;
	}
	/**
	 * Creates a new instance of {@link WeightedGraphEdge}.
	 * @param edge the edge being cloned
	 */
	public WeightedGraphEdge(final WeightedGraphEdge edge) {
		super(edge);
		cost = edge.cost;
	}
	/**
	 * Gets the cost of traversing the {@link WeightedGraphEdge}.
	 * @return <code>double</code>
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * Sets the cost of traversing the {@link WeightedGraphEdge}.
	 * @param c the cost to set
	 */
	public void setCost(final double c) {
		cost = c;
	}
}
