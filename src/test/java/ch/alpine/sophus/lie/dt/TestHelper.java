// code by jph
package ch.alpine.sophus.lie.dt;

import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/* package */ enum TestHelper {
  ;
  private static final Random RANDOM = new SecureRandom();
  private static final Distribution DISTRIBUTION_L = ExponentialDistribution.of(1);
  private static final Distribution DISTRIBUTION_T = NormalDistribution.standard();

  static Tensor spawn_St(int n) {
    return spawn_St(RANDOM, n);
  }

  static Tensor spawn_St(Random random, int n) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_L, random), RandomVariate.of(DISTRIBUTION_T, random, n));
  }

  static Tensor spawn_st(int n) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T, n));
  }

  static Tensor spawn_St1() {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_L), RandomVariate.of(DISTRIBUTION_T));
  }

  static Tensor spawn_st1() {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T));
  }
}
