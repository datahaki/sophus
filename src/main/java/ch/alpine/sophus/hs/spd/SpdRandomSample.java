// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;

public class SpdRandomSample implements RandomSampleInterface, Serializable {
  private final TSpdRandomSample tSpdRandomSample;

  /** @param n strictly positive
   * @param distribution */
  public SpdRandomSample(int n, Distribution distribution) {
    tSpdRandomSample = new TSpdRandomSample(n, distribution);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Spd0Exponential.INSTANCE.exp(tSpdRandomSample.randomSample(random));
  }
}
