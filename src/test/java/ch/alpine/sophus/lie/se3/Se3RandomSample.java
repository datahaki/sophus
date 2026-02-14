// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;

class Se3RandomSample implements RandomSampleInterface, Serializable {
  private final RandomSampleInterface rsi;

  public Se3RandomSample(Distribution p, Distribution v) {
    rsi = new TSe3RandomSample(p, v);
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return Se3Group.INSTANCE.exponential0().exp(rsi.randomSample(randomGenerator));
  }
}
