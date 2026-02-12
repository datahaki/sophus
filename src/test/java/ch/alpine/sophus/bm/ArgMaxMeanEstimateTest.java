// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class ArgMaxMeanEstimateTest {
  @Test
  void test() {
    Tensor tensor = ArgMaxMeanEstimate.INSTANCE.estimate(Tensors.vector(0, 1, 2, 3, 4), Tensors.vector(0, 0, 0, 1, 0));
    assertEquals(tensor, RealScalar.of(3));
  }
}
