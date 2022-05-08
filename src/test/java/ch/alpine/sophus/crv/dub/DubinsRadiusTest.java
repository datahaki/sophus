// code by jph
package ch.alpine.sophus.crv.dub;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class DubinsRadiusTest {
  @Test
  public void testSimple() {
    Clip clip = Clips.interval(0.3, 1.3);
    for (DubinsType dubinsType : DubinsType.values()) {
      Scalar maxRadius = DubinsRadius.getMax(Tensors.vector(1, 2, -1), dubinsType, clip);
      clip.requireInside(maxRadius);
    }
  }

  @Test
  public void testQuantity() {
    Clip clip = Clips.interval( //
        Quantity.of(0.3, "m"), //
        Quantity.of(1.3, "m"));
    for (DubinsType dubinsType : DubinsType.values()) {
      Scalar maxRadius = DubinsRadius.getMax(Tensors.fromString("{1[m],2[m],-1}"), dubinsType, clip);
      clip.requireInside(maxRadius);
    }
  }
}
