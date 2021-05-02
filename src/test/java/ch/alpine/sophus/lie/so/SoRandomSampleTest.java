// code by jph
package ch.alpine.sophus.lie.so;

import java.io.IOException;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.MatrixQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.MatrixLog;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import junit.framework.TestCase;

public class SoRandomSampleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(SoRandomSample.of(n));
      for (int count = 0; count < 5; ++count) {
        Tensor tensor = RandomSample.of(randomSampleInterface);
        Tolerance.CHOP.requireClose(Det.of(tensor), RealScalar.ONE);
        OrthogonalMatrixQ.require(tensor);
        MatrixQ.requireSize(tensor, n, n);
        Tensor log = MatrixLog.of(tensor);
        AntisymmetricMatrixQ.require(log);
      }
    }
  }

  public void testZeroFail() {
    AssertFail.of(() -> SoRandomSample.of(0));
  }
}
