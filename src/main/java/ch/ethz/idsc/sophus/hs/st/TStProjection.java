// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.MatrixDotTranspose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.Symmetrize;

/** Reference: geomstats */
public class TStProjection implements TensorUnaryOperator {
  private final Tensor x;

  /** @param x */
  public TStProjection(Tensor x) {
    this.x = x;
  }

  @Override
  public Tensor apply(Tensor v) {
    Tensor aux = MatrixDotTranspose.of(x, v); // k x k
    return v.subtract(Symmetrize.of(aux).dot(x));
  }
}
