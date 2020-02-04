// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Norm;
import junit.framework.TestCase;

public class InverseDistanceFromOriginTest extends TestCase {
  public void testSimple() {
    InverseDistanceFromOrigin inverseDistance = new InverseDistanceFromOrigin(Norm._2::ofVector);
    Tensor weights = inverseDistance.apply(Tensors.vector(1, 3).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.of(3, 4), RationalScalar.of(1, 4)));
  }

  public void testFailNull() {
    try {
      new InverseDistanceFromOrigin(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
