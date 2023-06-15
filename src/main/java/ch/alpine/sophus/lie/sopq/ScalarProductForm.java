// code by jph
package ch.alpine.sophus.lie.sopq;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.DiagonalMatrix;

/* package */ enum ScalarProductForm {
  ;
  /** @param p
   * @param q
   * @return */
  // TODO SOPHUS dot should be implemented more efficiently
  public static Tensor of(int p, int q) {
    return DiagonalMatrix.with(Tensors.vector(i -> i < p //
        ? RealScalar.ONE
        : RealScalar.ONE.negate(), p + q));
  }
}
