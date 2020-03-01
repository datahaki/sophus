// code by jph
package ch.ethz.idsc.sophus.hs.s1;

import java.util.Random;

import ch.ethz.idsc.sophus.lie.so2.AngleVector;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Clips;

/** sample from unit circle S2 */
public enum S1RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(Pi.VALUE));

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return AngleVector.of(RandomVariate.of(DISTRIBUTION));
  }
}
