// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ class InverseDiagonal implements TensorUnaryOperator {
  private static final Scalar ONE = RealScalar.of(1.0);

  /** @param variogram non-null
   * @return */
  public static TensorUnaryOperator of(ScalarUnaryOperator variogram) {
    return new InverseDiagonal(Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;

  private InverseDiagonal(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor weights = Tensors.reserve(tensor.length());
    int index = 0;
    for (Tensor p : tensor) {
      weights.append(variogram.apply(Abs.between(p.Get(index), ONE)));
      ++index;
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
