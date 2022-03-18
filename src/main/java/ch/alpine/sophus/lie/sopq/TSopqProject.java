// code by jph
package ch.alpine.sophus.lie.sopq;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.MatrixDotTranspose;

/** projects a square matrix to an element from the Lie algebra SO(p, q) */
public class TSopqProject implements TensorUnaryOperator {
  private final Tensor form;

  public TSopqProject(int p, int q) {
    form = ScalarProductForm.of(p, q);
  }

  @Override
  public Tensor apply(Tensor x) {
    // TODO SOPHUS ALG work with form as vector {1,1,1,...,-1,-1}
    return x.subtract(MatrixDotTranspose.of(form, x).dot(form));
  }
}
