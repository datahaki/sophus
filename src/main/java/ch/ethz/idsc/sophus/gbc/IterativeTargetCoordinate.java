// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SingularValueDecomposition;
import ch.ethz.idsc.tensor.sca.Ramp;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeTargetCoordinate implements Genesis, Serializable {
  private final Genesis genesis;
  private final int k;

  public IterativeTargetCoordinate(Genesis genesis, int k) {
    this.genesis = Objects.requireNonNull(genesis);
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor w = genesis.origin(levers);
    Tensor m = IdentityMatrix.of(levers.length()).subtract(levers.dot(PseudoInverse.of(levers)));
    SingularValueDecomposition svd = SingularValueDecomposition.of(m);
    Tensor n = NormalizeTotal.FUNCTION.apply(m.dot(w));
    for (int count = 0; count < k; ++count) {
      Tensor b = n.negate().map(Ramp.FUNCTION);
      Tensor sol = LeastSquares.of(svd, b);
      w = w.add(sol);
      n = NormalizeTotal.FUNCTION.apply(m.dot(w));
    }
    // System.out.println(n.map(Round._3));
    return n;
  }
}
