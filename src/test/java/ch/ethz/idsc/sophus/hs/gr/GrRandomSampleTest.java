// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrRandomSampleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int k = 1; k < 5; ++k) {
      RandomSampleInterface grRandomSample = Serialization.copy(GrRandomSample.of(k + 3, k));
      Tensor x = RandomSample.of(grRandomSample);
      GrMemberQ.of(Chop._07).requirePoint(x);
    }
  }

  public void testNN() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface grRandomSample = Serialization.copy(GrRandomSample.of(n, n));
      Tensor x = RandomSample.of(grRandomSample);
      GrMemberQ.of(Chop._07).requirePoint(x);
      Chop._09.requireClose(x, IdentityMatrix.of(n));
    }
  }

  public void testFail() {
    AssertFail.of(() -> GrRandomSample.of(-3, 2));
    AssertFail.of(() -> GrRandomSample.of(3, 4));
    AssertFail.of(() -> GrRandomSample.of(3, 0));
  }
}
