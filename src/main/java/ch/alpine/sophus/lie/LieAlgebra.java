// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.ad.JacobiIdentity;

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
