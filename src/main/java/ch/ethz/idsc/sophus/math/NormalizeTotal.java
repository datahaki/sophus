// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Total;

/** Hint: if the weights sum up to zero, then the normalization fails, for example
 * NormalizeTotal.FUNCTION.apply({+1, -1}) throws an Exception
 * 
 * <p>Hint: normalization is not consistent with Mathematica for empty vectors:
 * Mathematica::Normalize[{}, Total] == {}
 * Tensor-Lib.::NormalizeTotal[{}] throws an Exception */
public enum NormalizeTotal implements TensorUnaryOperator {
  FUNCTION;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Total::ofVector);

  @Override
  public Tensor apply(Tensor vector) {
    return NORMALIZE.apply(vector);
  }
}
