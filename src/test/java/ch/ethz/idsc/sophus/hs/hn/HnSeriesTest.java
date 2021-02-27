// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class HnSeriesTest extends TestCase {
  public void testSimple() {
    Tolerance.CHOP.requireClose(HnSeries.of(RealScalar.of(1.0)), Tensors.vector(1.5430806348152437, 1.1752011936438014));
    Tolerance.CHOP.requireClose(HnSeries.of(RealScalar.of(2.0)), Tensors.vector(2.1781835566085710, 1.368298872008591));
  }
}
