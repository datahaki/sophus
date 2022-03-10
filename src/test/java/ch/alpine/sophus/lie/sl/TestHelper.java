// code by jph
package ch.alpine.sophus.lie.sl;

import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

enum TestHelper {
  ;
  private static final Random RANDOM = new SecureRandom();
  private static final Distribution DISTRIBUTIONXY = DiscreteUniformDistribution.of(-10, 10);
  private static final Distribution DISTRIBUTION_Z = DiscreteUniformDistribution.of(1, 10);

  static Tensor spawn_Sl2() {
    return spawn_Sl2(RANDOM);
  }

  static Tensor spawn_Sl2(Random random) {
    return RandomVariate.of(DISTRIBUTIONXY, random, 2).append(RandomVariate.of(DISTRIBUTION_Z, random));
  }
}
