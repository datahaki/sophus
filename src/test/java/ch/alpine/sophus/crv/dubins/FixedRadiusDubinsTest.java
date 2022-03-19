// code by jph
package ch.alpine.sophus.crv.dubins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;

public class FixedRadiusDubinsTest {
  @Test
  public void testTest() {
    DubinsPathGenerator dubinsPathGenerator = FixedRadiusDubins.of(Tensors.vector(10, 2, Math.PI / 2), RealScalar.of(1));
    assertEquals(dubinsPathGenerator.stream().count(), 4);
  }

  @Test
  public void testUnits() {
    DubinsPathGenerator dubinsPathGenerator = FixedRadiusDubins.of(Tensors.fromString("{10[m], 2[m]}").append(Pi.HALF), Quantity.of(1, "m"));
    assertEquals(dubinsPathGenerator.stream().count(), 4);
  }

  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    DubinsPathGenerator dubinsPathGenerator = FixedRadiusDubins.of(Tensors.vector(10, 2, Math.PI / 2), RealScalar.of(1));
    DubinsPathGenerator copy = Serialization.copy(dubinsPathGenerator);
    assertEquals(copy.stream().count(), 4);
  }

  @Test
  public void testMin2() {
    for (int count = 0; count < 100; ++count) {
      Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 3);
      DubinsPathGenerator dubinsPathGenerator = FixedRadiusDubins.of(tensor, RealScalar.of(0.1));
      assertTrue(2 <= dubinsPathGenerator.stream().count());
    }
  }

  @Test
  public void testNegativeFail() {
    assertThrows(Exception.class, () -> FixedRadiusDubins.of(Tensors.vector(1, 2, 3), RealScalar.of(-0.1)));
  }
}
