// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.MatrixDotTranspose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Symmetrize;

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
