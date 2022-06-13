// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

class So2RandomSampleTest {
  @Test
  void testSimple() {
    Tensor r1 = RandomSample.of(So2RandomSample.INSTANCE);
    Tensor r2 = RandomSample.of(SoRandomSample.of(2));
    assertEquals(Dimensions.of(r1), Dimensions.of(r2));
  }
}
