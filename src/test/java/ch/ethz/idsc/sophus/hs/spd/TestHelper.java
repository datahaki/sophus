// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;

/* package */ enum TestHelper {
  ;
  /** @param n
   * @return symmetric matrix */
  static Tensor generateSim(int n) {
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor matrix = RandomVariate.of(distribution, n, n);
    return Symmetrize.of(matrix);
  }

  /** @param n
   * @return symmetric positive definite */
  static Tensor generateSpd(int n) {
    return SpdExponential.INSTANCE.exp(generateSim(n));
  }
}
