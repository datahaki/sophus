// code by jph
package ch.alpine.sophus.gbc.it;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.alpine.sophus.gbc.amp.IdentRamp;
import ch.alpine.sophus.hs.Genesis;
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

/** attempts to produce positive weights for levers with zero in convex hull
 * 
 * @param genesis
 * @param beta
 * @param k */
public record IterativeTargetCoordinate(Genesis genesis, Scalar beta, int k) implements GenesisDeque {

  public static final Chop CHOP = Tolerance.CHOP;
  public IterativeTargetCoordinate {
    Objects.requireNonNull(genesis);
    Integers.requirePositiveOrZero(k);
  }

  @Override
  public Deque<WeightsFactors> deque(Tensor levers) {
    Deque<WeightsFactors> deque = new ArrayDeque<>();
    Tensor w = genesis.origin(levers); // weighting
    Tensor m = InfluenceMatrix.of(levers).residualMaker();
    Tensor n = NormalizeTotal.FUNCTION.apply(m.dot(w)); // coordinates
    deque.add(new WeightsFactors(n, n.map(Scalar::zero)));
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
        deque.add(new WeightsFactors(n, n.map(Scalar::zero)));
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
