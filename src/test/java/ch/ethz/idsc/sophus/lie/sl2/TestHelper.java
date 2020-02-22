// code by jph
package ch.ethz.idsc.sophus.lie.sl2;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

enum TestHelper {
  ;
  private static final Distribution DISTRIBUTIONXY = DiscreteUniformDistribution.of(-10, 10);
  private static final Distribution DISTRIBUTION_Z = DiscreteUniformDistribution.of(1, 10);

  static Tensor spawn_Sl2() {
    return RandomVariate.of(DISTRIBUTIONXY, 2).append(RandomVariate.of(DISTRIBUTION_Z));
  }
}
