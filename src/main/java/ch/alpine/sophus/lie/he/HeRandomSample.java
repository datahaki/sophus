// code by jph
package ch.alpine.sophus.lie.he;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class HeRandomSample implements RandomSampleInterface, Serializable {
  private final int n;
  private final Distribution distribution;

  public HeRandomSample(int n, Distribution distribution) {
    this.n = Integers.requirePositive(n);
    this.distribution = distribution;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(distribution, randomGenerator, 2 * n + 1); // element
  }
}
