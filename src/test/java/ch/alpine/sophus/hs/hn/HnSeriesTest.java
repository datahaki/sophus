// code by jph
package ch.alpine.sophus.hs.hn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;

class HnSeriesTest {
  @Test
  public void testSimple() {
    Tolerance.CHOP.requireClose(HnSeries.of(RealScalar.of(1.0)), Tensors.vector(1.5430806348152437, 1.1752011936438014));
    Tolerance.CHOP.requireClose(HnSeries.of(RealScalar.of(2.0)), Tensors.vector(2.1781835566085710, 1.368298872008591));
  }
}
