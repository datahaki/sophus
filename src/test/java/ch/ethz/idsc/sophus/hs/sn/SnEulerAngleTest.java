// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.CauchyDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class SnEulerAngleTest extends TestCase {
  public void testSimple() {
    for (int d = 0; d < 6; ++d) {
      Tensor angles = RandomVariate.of(CauchyDistribution.standard(), d);
      Tensor tensor = SnEulerAngle.of(angles);
      SnMemberQ.INSTANCE.require(tensor);
    }
  }
}
