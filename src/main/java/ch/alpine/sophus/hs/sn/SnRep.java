// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;

// FIXME SOPHUS API
@Deprecated
public class SnRep {
  private final Tensor neutral;

  public SnRep(int n) {
    neutral = UnitVector.of(n + 1, n);
  }

  public Tensor toGroupElementMatrix(Tensor p) {
    return SnRotationMatrix.of(neutral, p);
  }

  public Tensor toPoint(Tensor matrix) {
    // TODO SOPHUS ALG simplify using extraction
    return matrix.dot(neutral);
  }
}
