// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnRandomSampleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int dimension = 0; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(SnRandomSample.of(dimension));
      Tensor tensor = RandomSample.of(randomSampleInterface);
      Chop._12.requireClose(Norm._2.ofVector(tensor), RealScalar.ONE);
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
