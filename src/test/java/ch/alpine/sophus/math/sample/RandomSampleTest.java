// code by jph
package ch.alpine.sophus.math.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;

public class RandomSampleTest {
  @Test
  public void testSimple() {
    Tensor tensor = RandomSample.of(BallRandomSample.of(Tensors.vector(1, 2, 3), RealScalar.ONE), 6);
    assertEquals(Dimensions.of(tensor), Arrays.asList(6, 3));
  }
}
