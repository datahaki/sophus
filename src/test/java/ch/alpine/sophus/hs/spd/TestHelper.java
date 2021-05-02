// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

/* package */ enum TestHelper {
  ;
  /** @param n
   * @return symmetric matrix */
  static Tensor generateSim(int n) {
    Integers.requirePositive(n);
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor matrix = RandomVariate.of(distribution, n, n);
    return Symmetrize.of(matrix);
  }

  /** @param n
   * @return symmetric positive definite */
  static Tensor generateSpd(int n) {
    return Spd0Exponential.INSTANCE.exp(generateSim(n));
  }
}
