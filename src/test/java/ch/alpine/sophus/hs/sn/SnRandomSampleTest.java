// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.IOException;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnRandomSampleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int dimension = 0; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(SnRandomSample.of(dimension));
      Tensor tensor = RandomSample.of(randomSampleInterface);
      Chop._12.requireClose(Vector2Norm.of(tensor), RealScalar.ONE);
      assertEquals(tensor.length(), dimension + 1);
    }
  }

  public void testS1() {
    assertEquals(SnRandomSample.of(1), S1RandomSample.INSTANCE);
  }

  public void testSNegFail() {
    AssertFail.of(() -> SnRandomSample.of(-1));
  }
}
