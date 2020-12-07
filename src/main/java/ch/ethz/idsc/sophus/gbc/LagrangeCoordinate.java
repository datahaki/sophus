// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.AppendOne;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;

/** attempts to produce positive weights for levers with zero in convex hull */
public class LagrangeCoordinate implements Genesis, Serializable {
  private final Genesis genesis;

  /** @param genesis
   * @param k */
  public LagrangeCoordinate(Genesis genesis) {
    this.genesis = Objects.requireNonNull(genesis);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor wbar = genesis.origin(levers);
    int n = levers.length();
    Tensor x = Tensor.of(levers.stream().map(AppendOne.FUNCTION));
    int d = levers.get(0).length();
    Tensor u = UnitVector.of(d + 1, d);
    Tensor rhs = Join.of(wbar, u);
    Tensor top = Join.of(1, IdentityMatrix.of(n), x);
    Tensor bot = Join.of(1, Transpose.of(x), Array.zeros(d + 1, d + 1));
    Tensor sol = LinearSolve.of(Join.of(top, bot), rhs);
    return sol.extract(0, n);
  }
}
