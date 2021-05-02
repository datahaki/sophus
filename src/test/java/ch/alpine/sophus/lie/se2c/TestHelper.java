// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

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
