// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.ad.JacobiIdentity;
import ch.alpine.tensor.Tensor;

public interface LieAlgebra {
  /** @return tensor of rank 3 satisfying {@link JacobiIdentity} */
  Tensor ad();

  /** @param degree
   * @return baker campbell hausdorff formula of given degree */
  BinaryOperator<Tensor> bch(int degree);

  /** @return
   * @throws UnsupportedOperationException */
  Tensor basis();
}
