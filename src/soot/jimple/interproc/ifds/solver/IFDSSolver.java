package soot.jimple.interproc.ifds.solver;

import static soot.jimple.interproc.ifds.solver.IFDSSolver.BinaryDomain.BOTTOM;
import static soot.jimple.interproc.ifds.solver.IFDSSolver.BinaryDomain.TOP;

import java.util.Set;

import soot.SootMethod;
import soot.Unit;
import soot.jimple.interproc.ifds.EdgeFunction;
import soot.jimple.interproc.ifds.EdgeFunctions;
import soot.jimple.interproc.ifds.FlowFunctions;
import soot.jimple.interproc.ifds.IDETabulationProblem;
import soot.jimple.interproc.ifds.IFDSTabulationProblem;
import soot.jimple.interproc.ifds.InterproceduralCFG;
import soot.jimple.interproc.ifds.JoinLattice;
import soot.jimple.interproc.ifds.edgefunc.AllBottom;
import soot.jimple.interproc.ifds.edgefunc.AllTop;
import soot.jimple.interproc.ifds.edgefunc.EdgeIdentity;
import soot.jimple.interproc.incremental.UpdatableWrapper;

/**
 * A solver for an {@link IFDSTabulationProblem}. This solver in effect uses the {@link IDESolver}
 * to solve the problem, as any IFDS problem can be intepreted as a special case of an IDE problem.
 * See Section 5.4.1 of the SRH96 paper. In effect, the IFDS problem is solved by solving an IDE
 * problem in which the environments (D to N mappings) represent the set's characteristic function.
 * 
 * @param <N> The type of nodes in the interprocedural control-flow graph. Typically {@link Unit}.
 * @param <D> The type of data-flow facts to be computed by the tabulation problem.
 * @param <M> The type of objects used to represent methods. Typically {@link SootMethod}.
 * @param <I> The type of inter-procedural control-flow graph being used.
 * @see IFDSTabulationProblem
 */
public class IFDSSolver<N extends UpdatableWrapper<?>,D extends UpdatableWrapper<?>,M extends UpdatableWrapper<?>,
		I extends InterproceduralCFG<N, M>> extends IDESolver<N,D,M,IFDSSolver.BinaryDomain,I> {

	static enum BinaryDomain { TOP,BOTTOM } 
	
	private final static EdgeFunction<BinaryDomain> ALL_BOTTOM = new AllBottom<BinaryDomain>(BOTTOM);
	
	/**
	 * Creates a solver for the given problem. The solver must then be started by calling
	 * {@link #solve()}.
	 */
	public IFDSSolver(final IFDSTabulationProblem<N,D,M,I> ifdsProblem) {
		super(new IDETabulationProblem<N,D,M,BinaryDomain,I>() {

			@Override
			public FlowFunctions<N,D,M> flowFunctions() {
				return ifdsProblem.flowFunctions();
			}

			@Override
			public I interproceduralCFG() {
				return ifdsProblem.interproceduralCFG();
			}

			@Override
			public Set<N> initialSeeds() {
				return ifdsProblem.initialSeeds();
			}

			@Override
			public D zeroValue() {
				return ifdsProblem.zeroValue();
			}

			@Override
			public EdgeFunctions<N,D,M,BinaryDomain> edgeFunctions() {
				return new IFDSEdgeFunctions();
			}

			@Override
			public JoinLattice<BinaryDomain> joinLattice() {
				return new JoinLattice<BinaryDomain>() {

					@Override
					public BinaryDomain topElement() {
						return BinaryDomain.TOP;
					}

					@Override
					public BinaryDomain bottomElement() {
						return BinaryDomain.BOTTOM;
					}

					@Override
					public BinaryDomain join(BinaryDomain left, BinaryDomain right) {
						if(left==TOP && right==TOP) {
							return TOP;
						} else {
							return BOTTOM;
						}
					}
				};
			}

			@Override
			public EdgeFunction<BinaryDomain> allTopFunction() {
				return new AllTop<BinaryDomain>(TOP);
			}
			
			class IFDSEdgeFunctions implements EdgeFunctions<N,D,M,BinaryDomain> {
		
				public EdgeFunction<BinaryDomain> getNormalEdgeFunction(N src,D srcNode,N tgt,D tgtNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v();
				}
				
				@Override		
				public EdgeFunction<BinaryDomain> getCallEdgeFunction(N callStmt,D srcNode,M destinationMethod,D destNode) {
					if(srcNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				@Override
				public EdgeFunction<BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod,N exitStmt,D exitNode,N returnSite,D retNode) {
					if(exitNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
		
				@Override
				public EdgeFunction<BinaryDomain> getCallToReturnEdgeFunction(N callStmt,D callNode,N returnSite,D returnSideNode) {
					if(callNode==ifdsProblem.zeroValue()) return ALL_BOTTOM;
					return EdgeIdentity.v(); 
				}
			}

			@Override
			public void updateCFG(I cfg) {
				ifdsProblem.updateCFG(cfg);
			}

			});
	}
	
	/**
	 * Returns the set of facts that hold at the given statement.
	 */
	public Set<D> ifdsResultsAt(N statement) {
		return resultsAt(statement).keySet();
	}

}
