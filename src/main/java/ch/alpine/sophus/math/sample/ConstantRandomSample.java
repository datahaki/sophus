// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;

public record ConstantRandomSample(Tensor tensor) implements RandomSampleInterface, Serializable {
  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator random) {
    return tensor.copy();
  }
}
