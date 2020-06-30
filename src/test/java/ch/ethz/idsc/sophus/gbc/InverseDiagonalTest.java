// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDiagonalTest extends TestCase {
  private static void _check(Tensor v) {
    Tensor h1 = v.dot(PseudoInverse.of(v));
    Tensor vt = Transpose.of(v);
    Tensor h2 = v.dot(PseudoInverse.of(vt.dot(v))).dot(vt);
    Chop._08.requireClose(h1, h2);
    Chop._08.requireAllZero(IdentityMatrix.of(h1.length()).subtract(h1).dot(v));
  }

  public void testHatMatrix() {
    _check(RandomVariate.of(UniformDistribution.unit(), 10, 3));
    _check(RandomVariate.of(UniformDistribution.unit(), 4, 9));
  }

  public void testNullFail() {
    try {
      InverseDiagonal.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
