// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.tensor.Tensor;

public class ConstantRandomSample implements RandomSampleInterface, Serializable {
  private final Tensor tensor;

  /** @param tensor */
  public ConstantRandomSample(Tensor tensor) {
    this.tensor = tensor.unmodifiable();
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return tensor;
  }
}
