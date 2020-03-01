// code by jph
package ch.ethz.idsc.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

// TODO class is only used once
/* package */ class UniformRandomSample implements RandomSampleInterface, Serializable {
  private final Distribution distribution;
  private final int length;

  public UniformRandomSample(Distribution distribution, int length) {
    this.distribution = distribution;
    this.length = length;
  }

  @Override
  public Tensor randomSample(Random random) {
    return RandomVariate.of(distribution, random, length);
  }
}
