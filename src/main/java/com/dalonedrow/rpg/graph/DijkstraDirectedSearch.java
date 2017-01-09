package com.dalonedrow.rpg.graph;

import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * 
 * @author drau
 *
 */
public final class DijkstraDirectedSearch {
    private final double[]            distTo;          // distTo[v] = distance of
    // shortest s->v path
    private final WeightedGraphEdge[] edgeTo;    // edgeTo[v] = last edge on
    // shortest s->v path
    private final IndexMinPQ<Double>  pq;    // priority queue of vertices
	/**
	 * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
	 * the edge-weighted digraph <tt>G</tt>.
	 * @param graph the edge-weighted digraph
	 * @param source the source vertex
	 * @throws IllegalArgumentException if an edge weight is negative
	 * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; 
	 * <tt>V</tt> - 1
	 */
    public DijkstraDirectedSearch(final EdgeWeightedDirectedGraph graph,
    		final int source) throws RPGException {
    	for (int i = graph.getNumberOfEdges() - 1; i >= 0; i--) {
    		WeightedGraphEdge e = graph.getEdge(i);
    		if (e.getCost() < 0) {
    			throw new RPGException(ErrorMessage.INTERNAL_BAD_ARGUMENT, 
    					"edge " + e + " has negative weight");
    		}
    	}
		distTo = new double[graph.getNumberOfVertices()];
		edgeTo = new WeightedGraphEdge[graph.getNumberOfVertices()];
		for (int v = 0; v < graph.getNumberOfVertices(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		distTo[source] = 0.0;
		
		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Double>(graph.getNumberOfVertices());
		pq.insert(source, distTo[source]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			WeightedGraphEdge[] adj = graph.getVertexAdjacencies(v);
			for (int i = adj.length - 1; i >= 0; i--) {
				relax(adj[i], v);
			}
		}

		// check optimality conditions
		assert check(graph, source);
	}
	// check optimality conditions:
	// (i) for all edges e: distTo[e.to()] <= distTo[e.from()] + e.weight()
	// (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] +
	// e.weight()
	private boolean check(final EdgeWeightedDirectedGraph graph, 
			final int s) {
		// check that edge weights are nonnegative
		for (int i = graph.getNumberOfEdges() - 1; i >= 0; i--) {
			WeightedGraphEdge e = graph.getEdge(i);
			if (e.getCost() < 0) {
				System.err.println("negative edge weight detected");
				return false;
			}
		}
		// check that distTo[v] and edgeTo[v] are consistent
		if (distTo[s] != 0.0 || edgeTo[s] != null) {
			System.err.println("distTo[s] and edgeTo[s] inconsistent");
			return false;
		}
		for (int v = graph.getNumberOfVertices() - 1; v >= 0; v--) {
			if (v == s) {
				continue;
			}
			if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
				System.err.println("distTo[] and edgeTo[] inconsistent");
				return false;
			}
		}
		
		// check that all edges e = v->w satisfy distTo[w] <= distTo[v] +
		// e.weight()
		for (int v = graph.getNumberOfVertices() - 1; v >= 0; v--) {
			WeightedGraphEdge[] adj = graph.getVertexAdjacencies(v);
			for (int i = adj.length - 1; i >= 0; i--) {
				WeightedGraphEdge e = adj[i];
				int w;
				if (v == e.getTo()) {
					w = e.getFrom();
				} else {
					w = e.getTo();
				}
				if (distTo[v] + e.getCost() < distTo[w]) {
					System.err.println("edge " + e + " not relaxed");
					return false;
				}
			}
		}
		
		// check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] +
		// e.weight()
		for (int w = 0; w < graph.getNumberOfVertices(); w++) {
			if (edgeTo[w] == null) {
				continue;
			}
			WeightedGraphEdge e = edgeTo[w];
			int v = e.getFrom();
			if (w != e.getTo()) {
				return false;
			}
			if (distTo[v] + e.getCost() != distTo[w]) {
				System.err.println("edge " + e + " on shortest path not tight");
				return false;
			}
		}
		return true;
	}
	/**
	 * Returns the length of a shortest path from the source vertex <tt>s</tt>
	 * to vertex <tt>v</tt>.
	 * @param v the destination vertex
	 * @return the length of a shortest path from the source vertex <tt>s</tt>
	 *         to vertex <tt>v</tt>; <tt>Double.POSITIVE_INFINITY</tt> if no
	 *         such path
	 */
	public double distanceTo(final int v) {
		return distTo[v];
	}
	/**
	 * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
	 * @param v the destination vertex
	 * @return <tt>true</tt> if there is a path from the source vertex
	 *         <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
	 */
	public boolean hasPathTo(final int v) {
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	/**
	 * Returns a shortest path from the source vertex <tt>s</tt> to vertex
	 * <tt>v</tt>.
	 * @param v the destination vertex
	 * @return a shortest path from the source vertex <tt>s</tt> to vertex
	 *         <tt>v</tt> as an iterable of edges, and <tt>null</tt> if no such
	 *         path
	 */
	public WeightedGraphEdge[] pathTo(final int v) {
		WeightedGraphEdge[] path = null;
		if (hasPathTo(v)) {
			path = new WeightedGraphEdge[0];
			for (WeightedGraphEdge e = edgeTo[v]; e != null; 
					e = edgeTo[e.getFrom()]) {
				path = ArrayUtilities.getInstance().extendArray(e, path);
			}
		}
		return path;
	}
	/**
	 * Relax the edge and update the priority queue if needed.
	 * @param edge the edge
	 * @param source the source vertex where the edge leads from
	 */
	private void relax(final WeightedGraphEdge edge, final int source) {
		int v, w;
		if (source == edge.getFrom()) {
			v = edge.getFrom();
			w = edge.getTo();
		} else {
			v = edge.getTo();
			w = edge.getFrom();
		}
		if (distTo[w] > distTo[v] + edge.getCost()) {
			distTo[w] = distTo[v] + edge.getCost();
			edgeTo[w] = edge;
			if (pq.contains(w)) {
				pq.decreaseKey(w, distTo[w]);
			} else {
				pq.insert(w, distTo[w]);
			}
		}
	}
}
