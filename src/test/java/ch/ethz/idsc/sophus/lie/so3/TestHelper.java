// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Clips;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));

  static Tensor spawn_So3() {
    return So3Exponential.INSTANCE.exp(spawn_so3());
  }

  static Tensor spawn_so3() {
    return RandomVariate.of(DISTRIBUTION, 3);
  }
}
