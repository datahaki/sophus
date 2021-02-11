// code by jph
package ch.ethz.idsc.sophus.lie.sopq;

import ch.ethz.idsc.sophus.math.ScalarProductForm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** projects a square matrix to an element from the Lie algebra SO(p, q) */
public class TSopqProject implements TensorUnaryOperator {
  private final Tensor form;

  public TSopqProject(int p, int q) {
    form = ScalarProductForm.of(p, q);
  }

  @Override
  public Tensor apply(Tensor x) {
    return x.subtract(form.dot(Transpose.of(x)).dot(form));
  }
}
