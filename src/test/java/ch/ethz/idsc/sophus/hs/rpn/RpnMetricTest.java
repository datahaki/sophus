// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class RpnMetricTest extends TestCase {
  public void testSimple() {
    Tolerance.CHOP.requireZero(RpnMetric.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(+10, 0, 0)));
    Tolerance.CHOP.requireZero(RpnMetric.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(-10, 0, 0)));
  }
}
