// code by jph
package ch.alpine.sophus.lie.se3;

import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/* package */ enum TestHelper {
  ;
  private static final Random RANDOM = new SecureRandom();
  private static final Distribution DISTRIBUTION_T = NormalDistribution.of(4, 1);
  private static final Distribution DISTRIBUTION_R = NormalDistribution.of(0, 0.1);

  static Tensor spawn_se3() {
    return spawn_se3(RANDOM);
  }

  static Tensor spawn_se3(Random random) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T, random, 3), RandomVariate.of(DISTRIBUTION_R, random, 3));
  }

  static Tensor spawn_Se3() {
    return spawn_Se3(RANDOM);
  }

  static Tensor spawn_Se3(Random random) {
    return Se3Exponential.INSTANCE.exp(spawn_se3(random));
  }
}
