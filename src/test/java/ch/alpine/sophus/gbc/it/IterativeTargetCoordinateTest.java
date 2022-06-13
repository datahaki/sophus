// code by jph
package ch.alpine.sophus.gbc.it;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Deque;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Ramp;

class IterativeTargetCoordinateTest {
  @Test
  void testSimple() {
    int count = 0;
    IterativeTargetCoordinate genesis = //
        new IterativeTargetCoordinate(new InverseDistanceWeighting(InversePowerVariogram.of(2)), RealScalar.ONE, 100);
    for (int n = 5; n < 20; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (OriginEnclosureQ.isInsideConvexHull(levers)) {
        Deque<WeightsFactors> deque = genesis.deque(levers);
        Tensor vector = genesis.origin(levers);
        AffineQ.require(vector, Chop._10);
        Chop._08.requireAllZero(vector.dot(levers));
        if (1 < deque.size() && deque.size() < 100) {
          Tensor w0 = deque.peekFirst().weights();
          Tensor w1 = deque.peekLast().weights();
          assertFalse(Chop._10.allZero(w0.negate().map(Ramp.FUNCTION)));
          assertTrue(Chop._10.allZero(w1.negate().map(Ramp.FUNCTION)));
        }
        ++count;
      }
    }
    assertTrue(2 < count);
  }
}
