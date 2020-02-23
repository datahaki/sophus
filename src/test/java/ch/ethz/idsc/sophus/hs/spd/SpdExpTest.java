// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdExpTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
      SpdExp spdExp = Serialization.copy(new SpdExp(p));
      Tensor w = spdExp.log(q);
      Tensor exp = spdExp.exp(w);
      Chop._08.requireClose(q, exp);
    }
  }

  public void testMidpoint() {
    for (int n = 1; n < 5; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
      SpdExp spdExpP = new SpdExp(p);
      SpdExp spdExpQ = new SpdExp(q);
      Tensor pqw = spdExpP.log(q);
      Tensor qpw = spdExpQ.log(p);
      Tensor ph = spdExpP.exp(pqw.multiply(RationalScalar.HALF));
      Tensor qh = spdExpQ.exp(qpw.multiply(RationalScalar.HALF));
      Chop._08.requireClose(ph, qh);
    }
  }

  public void testNonSymmetricFail() {
    try {
      new SpdExp(RandomVariate.of(UniformDistribution.of(-2, 2), 3, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullFail() {
    try {
      new SpdExp(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
