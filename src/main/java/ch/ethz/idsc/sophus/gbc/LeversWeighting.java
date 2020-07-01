// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ class LeversWeighting implements TensorUnaryOperator {
  private final ScalarUnaryOperator variogram;

  public LeversWeighting(ScalarUnaryOperator variogram) {
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor apply(Tensor matrix) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(matrix.stream() //
        .map(Norm._2::ofVector) //
        .map(variogram)));
  }
}
