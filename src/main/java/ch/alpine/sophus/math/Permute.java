// code by jph
package ch.alpine.sophus.math;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.Primitives;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/Combinatorica/ref/Permute.html">Permute</a> */
public class Permute implements TensorUnaryOperator {
  /** @param sigma
   * @return */
  public static TensorUnaryOperator of(Tensor sigma) {
    return new Permute(Integers.requirePermutation(Primitives.toIntArray(sigma)));
  }

  // ---
  private final int[] sigma;

  /** @param sigma for instance {0, 1, 4, 2, 3} */
  private Permute(int[] sigma) {
    this.sigma = sigma;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Integers.requireEquals(sigma.length, tensor.length());
    return Tensor.of(Arrays.stream(sigma).mapToObj(tensor::get));
  }
}
