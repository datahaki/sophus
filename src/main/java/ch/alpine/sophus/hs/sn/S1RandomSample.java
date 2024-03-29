// code by jph
package ch.alpine.sophus.hs.sn;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

/** sample from unit circle S1 parameterized by {cos(angle), sin(angle)} in the plane
 * 
 * @see SnRandomSample */
/* package */ enum S1RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(Pi.VALUE));

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return AngleVector.of(RandomVariate.of(DISTRIBUTION, randomGenerator));
  }
}
