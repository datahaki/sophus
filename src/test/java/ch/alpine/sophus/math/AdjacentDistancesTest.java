// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.r2.Se2Parametric;
import ch.alpine.sophus.lie.rn.RnMetric;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;

class AdjacentDistancesTest {
  @Test
  void testR2() throws ClassNotFoundException, IOException {
    Tensor tensor = Serialization.copy(new AdjacentDistances(RnMetric.INSTANCE)) //
        .apply(Tensors.fromString("{{1, 2}, {2, 2}, {2, 4}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.vector(1, 2));
  }

  @Test
  void testSe2() {
    Tensor tensor = new AdjacentDistances(Se2Parametric.INSTANCE).apply(Tensors.fromString("{{1, 2, 3}, {2, 2, 4}, {2, 4, 3}}"));
    Chop._12.requireClose(tensor, Tensors.vector(1.042914821466744, 2.085829642933488));
  }

  @Test
  void testR2Single() {
    Tensor tensor = new AdjacentDistances(RnMetric.INSTANCE).apply(Tensors.fromString("{{1, 2}}"));
    assertEquals(tensor, Tensors.empty());
  }

  @Test
  void testR2SingleFail() {
    assertThrows(Exception.class, () -> new AdjacentDistances(null));
  }

  @Test
  void testScalarFail() {
    assertThrows(Exception.class, () -> new AdjacentDistances(RnMetric.INSTANCE).apply(Pi.HALF));
  }
}
