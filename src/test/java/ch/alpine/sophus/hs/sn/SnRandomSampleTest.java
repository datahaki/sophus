// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

public class SnRandomSampleTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int dimension = 0; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(SnRandomSample.of(dimension));
      Tensor tensor = RandomSample.of(randomSampleInterface);
      Chop._12.requireClose(Vector2Norm.of(tensor), RealScalar.ONE);
      assertEquals(tensor.length(), dimension + 1);
    }
  }

  @Test
  public void testS1() {
    assertEquals(SnRandomSample.of(1), S1RandomSample.INSTANCE);
  }

  @Test
  public void testSNegFail() {
    assertThrows(Exception.class, () -> SnRandomSample.of(-1));
  }
}
