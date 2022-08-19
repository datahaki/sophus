// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

class StRandomSampleTest {
  @Test
  void testMember() {
    StRandomSample stRandomSample = new StRandomSample(5, 3);
    Tensor matrix = RandomSample.of(stRandomSample);
    assertEquals(Dimensions.of(matrix), List.of(3, 5));
    // StMemberQ.INSTANCE.require(matrix);
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new StRandomSample(3, -1));
    assertThrows(Exception.class, () -> new StRandomSample(3, 4));
    assertThrows(Exception.class, () -> new StRandomSample(-3, -4));
  }
}
