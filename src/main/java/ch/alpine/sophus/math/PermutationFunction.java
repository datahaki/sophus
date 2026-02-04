// code by jph
package ch.alpine.sophus.math;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.Primitives;

public class PermutationFunction implements TensorUnaryOperator {
  private final int[] sigma;

  public PermutationFunction(Tensor sigma) {
    this.sigma = Integers.requirePermutation(Primitives.toIntArray(sigma));
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Integers.requireEquals(sigma.length, tensor.length());
    return Tensor.of(Arrays.stream(sigma).mapToObj(tensor::get));
  }
}
