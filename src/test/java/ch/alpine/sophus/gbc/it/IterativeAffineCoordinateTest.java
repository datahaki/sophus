// code by jph
package ch.alpine.sophus.gbc.it;

import java.util.Deque;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.dv.AffineCoordinate;
import ch.alpine.sophus.gbc.amp.Amplifiers;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class IterativeAffineCoordinateTest {
  private static void _check(Tensor levers, Tensor weights) {
    Chop._10.requireAllZero(weights.dot(levers));
    Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
  }

  @Test
  void testAmplifiers() {
    for (Amplifiers amplifiers : Amplifiers.values()) {
      for (int k : new int[] { 0, 1, 5, 10 }) {
        GenesisDeque genesis = new IterativeAffineCoordinate(amplifiers.supply(3), k);
        for (int n = 3; n < 10; ++n) {
          Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
          if (OriginEnclosureQ.isInsideConvexHull(levers)) {
            _check(levers, AffineCoordinate.INSTANCE.origin(levers));
            _check(levers, genesis.origin(levers));
            Deque<WeightsFactors> deque = genesis.deque(levers);
            for (WeightsFactors evaluation : deque)
              _check(levers, evaluation.weights());
          }
        }
      }
    }
  }
}
