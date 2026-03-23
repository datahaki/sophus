// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;

class ClothoidImplTest {
  @Test
  void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = LagrangeQuadratic.interp(Pi.HALF, Pi.TWO, Pi.VALUE);
    for (ClothoidIntegration clothoidIntegration : ClothoidIntegrations.values()) {
      ClothoidIntegral clothoidIntegral = clothoidIntegration.clothoidIntegral(lagrangeQuadratic);
      assertThrows(Exception.class, () -> new ClothoidImpl(null, clothoidIntegral, Tensors.vector(1, 2)));
    }
  }
}
