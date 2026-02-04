// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** @param n strictly positive
 * @param distribution */
public record Spd0RandomSample(int n, Distribution distribution) implements RandomSampleInterface, Serializable {
  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return Spd0Exponential.INSTANCE.exp(new TSpdRandomSample(n, distribution).randomSample(randomGenerator));
  }
}
