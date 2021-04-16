// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.MatrixDotTranspose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.Symmetrize;

/** Reference: geomstats */
public class TStProjection implements TensorUnaryOperator {
  private final Tensor p;

  /** @param p */
  public TStProjection(Tensor p) {
    this.p = p;
  }

  @Override
  public Tensor apply(Tensor v) {
    return v.subtract(Symmetrize.of(MatrixDotTranspose.of(p, v)).dot(p));
  }
}
