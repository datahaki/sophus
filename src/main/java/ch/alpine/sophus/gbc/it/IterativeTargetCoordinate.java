// code by jph
package ch.alpine.sophus.gbc.it;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.alpine.sophus.gbc.amp.IdentRamp;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.pi.LeastSquares;
import ch.alpine.tensor.mat.sv.SingularValueDecomposition;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;

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
    // TODO SOPHUS ALG also target values above 1
    Tensor b = n.map(IdentRamp.FUNCTION);
    // Tensor b = n.map(Abs.FUNCTION);
    if (!CHOP.allZero(b))
    // if (!n.stream().map(Scalar.class::cast).allMatch(Sign::isPositiveOrZero))
    {
      TensorUnaryOperator tuo = LeastSquares.operator(SingularValueDecomposition.of(m));
      for (int count = 0; count < k; ++count) {
        Tensor sol = tuo.apply(b);
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
