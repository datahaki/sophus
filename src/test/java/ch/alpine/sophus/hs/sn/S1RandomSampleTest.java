// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

class S1RandomSampleTest {
  @Test
  void testSimple() {
    Tensor tensor = RandomSample.of(S1RandomSample.INSTANCE, 10);
    assertEquals(Dimensions.of(tensor), Arrays.asList(10, 2));
  }
}
