// code by jph
package ch.ethz.idsc.sophus.lie.so;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.MatrixQ;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.mat.re.Det;
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
