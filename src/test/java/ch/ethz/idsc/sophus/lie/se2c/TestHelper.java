// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(-10, 10);

  static Tensor spawn_Se2C() {
    return RandomVariate.of(DISTRIBUTION, 3);
  }

  static Tensor spawn_se2C() {
    return RandomVariate.of(DISTRIBUTION, 3);
  }
}
