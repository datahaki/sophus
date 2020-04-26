// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Clips;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = So3RandomSample.INSTANCE;

  static Tensor spawn_So3() {
    return RandomSample.of(RANDOM_SAMPLE_INTERFACE);
  }

  static Tensor spawn_so3() {
    Tensor m = RandomVariate.of(DISTRIBUTION, 3, 3);
    return Transpose.of(m).subtract(m);
  }
}
