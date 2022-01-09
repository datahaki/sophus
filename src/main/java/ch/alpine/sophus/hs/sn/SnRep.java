// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;

// FIXME
@Deprecated
public class SnRep {
  private final Tensor neutral;

  public SnRep(int n) {
    neutral = UnitVector.of(n + 1, n);
  }

  public Tensor toGroupElementMatrix(Tensor p) {
    return SnManifold.INSTANCE.endomorphism(neutral, p);
  }

  public Tensor toPoint(Tensor matrix) {
    // TODO simplify using extraction
    return matrix.dot(neutral);
  }
}
