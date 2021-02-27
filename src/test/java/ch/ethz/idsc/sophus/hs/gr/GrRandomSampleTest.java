// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrRandomSampleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int k = 1; k < 5; ++k) {
      RandomSampleInterface grRandomSample = Serialization.copy(GrRandomSample.of(k + 3, k));
      Tensor x = RandomSample.of(grRandomSample);
      GrMemberQ.INSTANCE.require(x);
    }
  }

  public void testNN() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(GrRandomSample.of(n, n));
      Tensor x = RandomSample.of(randomSampleInterface);
      GrMemberQ.INSTANCE.require(x);
      Chop._09.requireClose(x, IdentityMatrix.of(n));
    }
  }

  public void testN0() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, 0);
      for (int k = 0; k < 3; ++k) {
        Tensor x = RandomSample.of(randomSampleInterface);
        GrMemberQ.INSTANCE.require(x);
        assertEquals(x, Array.zeros(n, n));
        x.set(RealScalar.ONE::add, Tensor.ALL, Tensor.ALL);
      }
    }
  }

  public void testFail() {
    AssertFail.of(() -> GrRandomSample.of(-3, 2));
    AssertFail.of(() -> GrRandomSample.of(3, 4));
  }
}
