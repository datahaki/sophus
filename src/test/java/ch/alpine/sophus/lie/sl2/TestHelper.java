// code by jph
package ch.alpine.sophus.lie.sl2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.DiscreteUniformDistribution;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

enum TestHelper {
  ;
  private static final Distribution DISTRIBUTIONXY = DiscreteUniformDistribution.of(-10, 10);
  private static final Distribution DISTRIBUTION_Z = DiscreteUniformDistribution.of(1, 10);

  static Tensor spawn_Sl2() {
    return RandomVariate.of(DISTRIBUTIONXY, 2).append(RandomVariate.of(DISTRIBUTION_Z));
  }
}
