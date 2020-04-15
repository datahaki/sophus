// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class NormalizeAffineTest extends TestCase {
  static Tensor deprec(Tensor vector, Tensor nullsp) {
    return vector.dot(PseudoInverse.of(nullsp)).dot(nullsp);
  }

  public void testSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor vector = RandomVariate.of(distribution, 10);
      Tensor matrix = RandomVariate.of(distribution, 10, 3);
      Tensor nullsp = LeftNullSpace.usingQR(matrix);
      Tensor p1 = deprec(vector, nullsp);
      Tensor p2 = NormalizeAffine.product(vector, nullsp);
      Tolerance.CHOP.requireClose(p1, p2);
    }
  }
}
