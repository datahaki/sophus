// code by jph
package ch.ethz.idsc.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.tensor.Tensor;

public class ConstantRandomSample implements RandomSampleInterface, Serializable {
  private static final long serialVersionUID = -7897833715159384583L;
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
