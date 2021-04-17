// code by jph
package ch.ethz.idsc.sophus.gbc.it;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.amp.IdentRamp;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.mat.gr.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.sv.SingularValueDecomposition;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.sca.Chop;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeTargetCoordinate implements GenesisDeque, Serializable {
  public static final Chop CHOP = Tolerance.CHOP;
  // ---
  private final Genesis genesis;
  // private final Scalar beta;
  private final int k;

  /** @param genesis
   * @param k */
  public IterativeTargetCoordinate(Genesis genesis, Scalar beta, int k) {
    this.genesis = Objects.requireNonNull(genesis);
    // this.beta = beta;
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override
  public Deque<Evaluation> deque(Tensor levers) {
    Deque<Evaluation> deque = new ArrayDeque<>();
    Tensor w = genesis.origin(levers); // weighting
    Tensor m = InfluenceMatrix.of(levers).residualMaker();
    Tensor n = NormalizeTotal.FUNCTION.apply(m.dot(w)); // coordinates
    deque.add(new Evaluation(n, n.map(Scalar::zero)));
    // TODO also target values above 1
    Tensor b = n.map(IdentRamp.FUNCTION);
    // Tensor b = n.map(Abs.FUNCTION);
    if (!CHOP.allZero(b))
    // if (!n.stream().map(Scalar.class::cast).allMatch(Sign::isPositiveOrZero))
    {
      SingularValueDecomposition svd = SingularValueDecomposition.of(m);
      for (int count = 0; count < k; ++count) {
        Tensor sol = LeastSquares.of(svd, b);
        w = w.add(sol);
        n = NormalizeTotal.FUNCTION.apply(m.dot(w));
        deque.add(new Evaluation(n, n.map(Scalar::zero)));
        b = n.map(IdentRamp.FUNCTION);
        // b = n.map(Abs.FUNCTION);
        if (CHOP.allZero(b))
          // if (n.stream().map(Scalar.class::cast).allMatch(Sign::isPositiveOrZero))
          break;
      }
    }
    return deque;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return deque(levers).peekLast().weights();
  }
}
