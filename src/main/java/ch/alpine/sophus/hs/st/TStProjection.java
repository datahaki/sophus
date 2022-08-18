// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.MatrixDotTranspose;

/** Reference: geomstats */
public record TStProjection(Tensor p) implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor v) {
    return v.subtract(Symmetrize.of(MatrixDotTranspose.of(p, v)).dot(p));
  }
}
