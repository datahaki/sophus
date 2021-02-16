// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
      SpdExponential spdExp = Serialization.copy(new SpdExponential(p));
      Tensor w = spdExp.log(q);
      Tensor exp = spdExp.exp(w);
      Chop._08.requireClose(q, exp);
      Tensor f1 = spdExp.flip(q);
      Tensor f2 = spdExp.exp(w.negate());
      Chop._08.requireClose(f1, f2);
      Tensor m1 = spdExp.midpoint(q);
      Tensor m2 = spdExp.exp(w.multiply(RationalScalar.HALF));
      Chop._08.requireClose(m1, m2);
    }
  }

  public void testSpdToSym() {
    for (int n = 1; n < 5; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      SymmetricMatrixQ.require(MatrixLog.of(p), Chop._07);
    }
  }

  public void testMidpoint() {
    for (int n = 1; n < 5; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
      SpdExponential spdExpP = new SpdExponential(p);
      SpdExponential spdExpQ = new SpdExponential(q);
      Tensor pqw = spdExpP.log(q);
      Tensor qpw = spdExpQ.log(p);
      Tensor ph = spdExpP.exp(pqw.multiply(RationalScalar.HALF));
      Tensor qh = spdExpQ.exp(qpw.multiply(RationalScalar.HALF));
      Chop._08.requireClose(ph, qh);
      Tensor vector = spdExpP.vectorLog(q);
      VectorQ.requireLength(vector, n * (n + 1) / 2);
    }
  }

  public void testIdentity() {
    for (int n = 1; n < 4; ++n) {
      Exponential exponential = new SpdExponential(IdentityMatrix.of(n));
      Tensor x = TestHelper.generateSim(n);
      Chop._08.requireClose(exponential.exp(x), Spd0Exponential.INSTANCE.exp(x));
      Tensor q = TestHelper.generateSpd(n);
      Chop._08.requireClose(exponential.log(q), Spd0Exponential.INSTANCE.log(q));
      Chop._08.requireClose(exponential.vectorLog(q), Spd0Exponential.INSTANCE.vectorLog(q));
    }
  }

  public void testNonSymmetricFail() {
    AssertFail.of(() -> new SpdExponential(RandomVariate.of(UniformDistribution.of(-2, 2), 3, 3)));
  }

  public void testNullFail() {
    AssertFail.of(() -> new SpdExponential(null));
  }
}
