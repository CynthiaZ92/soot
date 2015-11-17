package soot.jimple.interproc.ifds;


/**
 * An edge function computes how a V-type value changes when flowing from one
 * super-graph node to another. See Sagiv, Reps, Horwitz 1996.
 * 
 * <b>NOTE:</b> Methods defined on this type may be called simultaneously by different threads.
 * Hence, classes implementing this interface should synchronize accesses to
 * any mutable shared state.
 *  
 * @param <V> The type of values to be computed along flow edges.
 */
public interface EdgeFunction<V> {

	/**
	 * Computes the value resulting from applying this function to source.
	 */
	V computeTarget(V source);
	
	/**
	 * Composes this function with the secondFunction, effectively returning
	 * a summary function that maps sources to targets exactly as if
	 * first this function had been applied and then the secondFunction. 
	 */
	EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction);
	
	/**
	 * Inverts this edge function. The semantics is: e1.composeWith
	 * (e2).composeWith(e2.invert()).equals(e1)==true. Implementors
	 * must provide functions "composeWith" and "invert" that adhere
	 * to this invariant.
	 * @return The inverse of this edge function
	 */
	EdgeFunction<V> invert();

	/**
	 * Returns a function that represents that (element-wise) join
	 * of this function with otherFunction. Naturally, this is a
	 * symmetric operation.
	 * @see JoinLattice#join(Object, Object)
	 */
	EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction);	
		
	/**
	 * Returns true is this function represents exactly the same 
	 * source to target mapping as other.
	 */
	public boolean equalTo(EdgeFunction<V> other);

}
