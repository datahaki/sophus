// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Sqrt;

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

  /** @param matrix symmetric
   * @return
   * @throws Exception if matrix is not symmetric */
  public static Tensor sqrt(Tensor matrix) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor a = eigensystem.vectors();
    Tensor ainv = Transpose.of(a);
    return ainv.dot(eigensystem.values().map(Sqrt.FUNCTION).pmul(a));
  }
}
