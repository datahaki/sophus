// code by jph
package ch.alpine.sophus.hs.spd;

import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

/* package */ enum TestHelper {
  ;
  private static final Random RANDOM = new SecureRandom();

  public static Tensor generateSim(int n) {
    return generateSim(n, RANDOM);
  }

  /** @param n
   * @return symmetric matrix */
  public static Tensor generateSim(int n, Random random) {
    Integers.requirePositive(n);
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor matrix = RandomVariate.of(distribution, n, n);
    return Symmetrize.of(matrix);
  }

  public static Tensor generateSpd(int n) {
    return generateSpd(n, RANDOM);
  }

  /** @param n
   * @return symmetric positive definite */
  public static Tensor generateSpd(int n, Random random) {
    return Spd0Exponential.INSTANCE.exp(generateSim(n, random));
  }
}
