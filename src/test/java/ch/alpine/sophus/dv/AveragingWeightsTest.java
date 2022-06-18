// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;

class AveragingWeightsTest {
  @Test
  void testSimple() {
    assertEquals(AveragingWeights.INSTANCE.origin(Tensors.vector(1, 2, 3)), Tensors.fromString("{1/3,1/3,1/3}"));
  }
}
