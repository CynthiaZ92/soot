package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	@Override
	public V computeTarget(V source) {
		return source;
	}

	@Override
	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	@Override
	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	@Override
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	@Override
	public String toString() {
		return "id";
	}

	@Override
	public EdgeFunction<V> invert() {
		return this;
	}

}
