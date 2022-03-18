// code by jph
package ch.alpine.sophus.gbc.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.gbc.AffineCoordinate;
import ch.alpine.sophus.gbc.amp.Amplifiers;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

public class IterativeAffineCoordinateTest {
  private static void _check(Tensor levers, Tensor weights) {
    Chop._10.requireAllZero(weights.dot(levers));
    Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
  }

  @Test
  public void testAmplifiers() {
    for (Amplifiers amplifiers : Amplifiers.values()) {
      for (int k : new int[] { 0, 1, 5, 10 }) {
        GenesisDeque genesis = new IterativeAffineCoordinate(amplifiers.supply(3), k);
        for (int n = 3; n < 10; ++n) {
          Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
          if (OriginEnclosureQ.isInsideConvexHull(levers)) {
            _check(levers, AffineCoordinate.INSTANCE.origin(levers));
            _check(levers, genesis.origin(levers));
            Deque<Evaluation> deque = genesis.deque(levers);
            for (Evaluation evaluation : deque)
              _check(levers, evaluation.weights());
          }
        }
      }
    }
  }

  @Test
  public void testArraysList() {
    List<Object> list = Arrays.asList();
    assertEquals(list.size(), 0);
  }
}
