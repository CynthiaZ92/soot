package soot.jimple.interproc.ifds;

import java.util.Set;

import soot.SootMethod;
import soot.Unit;
import soot.jimple.interproc.ifds.solver.IFDSSolver;
import soot.jimple.interproc.ifds.template.JimpleBasedInterproceduralCFG;
import soot.jimple.interproc.incremental.UpdatableWrapper;

/**
 * A tabulation problem for solving in an {@link IFDSSolver} as described
 * by the Reps, Horwitz, Sagiv 1995 (RHS95) paper.
 *
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 */
public interface IFDSTabulationProblem<N extends UpdatableWrapper<?>,D extends UpdatableWrapper<?>,
		M extends UpdatableWrapper<?>, I extends InterproceduralCFG<N,M>> {

	/**
	 * Returns a set of flow functions. Those functions are used to compute data-flow facts
	 * along the various kinds of control flows.
     *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	FlowFunctions<N,D,M> flowFunctions();
	
	/**
	 * Returns the interprocedural control-flow graph which this problem is computed over.
	 * Typically this will be a {@link JimpleBasedInterproceduralCFG}.
	 * 
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	I interproceduralCFG();
	
	/**
	 * Updates the locally cached control-flow graph over which the problem is computed.
	 * Typically this will be a {@link JimpleBasedInterproceduralCFG}.
	 * @param cfg The new control-flow graph
	 */
	void updateCFG(I cfg);
	
	/**
	 * Returns initial seeds to be used for the analysis. (a set of statements)
	 */
	Set<N> initialSeeds();
	
	/**
	 * This must be a data-flow fact of type {@link D}, but must <i>not</i>
	 * be part of the domain of data-flow facts. Typically this will be a
	 * singleton object of type {@link D} that is used for nothing else.
	 * It must holds that this object does not equals any object 
	 * within the domain.
	 *
	 * <b>NOTE:</b> this method could be called many times. Implementations of this
	 * interface should therefore cache the return value! 
	 */
	D zeroValue();
}
