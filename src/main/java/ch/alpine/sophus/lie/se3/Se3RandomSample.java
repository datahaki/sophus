// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;

public class Se3RandomSample implements RandomSampleInterface, Serializable {
  private final RandomSampleInterface rsi;

  public Se3RandomSample(Distribution p, Distribution v) {
    rsi = new TSe3RandomSample(p, v);
  }

  @Override
  public Tensor randomSample(Random random) {
    return Se3Exponential.INSTANCE.exp(rsi.randomSample(random));
  }
}
