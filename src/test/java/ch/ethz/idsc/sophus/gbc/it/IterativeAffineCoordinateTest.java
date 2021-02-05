// code by jph
package ch.ethz.idsc.sophus.gbc.it;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.amp.Amplifiers;
import ch.ethz.idsc.sophus.gbc.it.GenesisDeque;
import ch.ethz.idsc.sophus.gbc.it.IterativeAffineCoordinate;
import ch.ethz.idsc.sophus.gbc.it.IterativeAffineCoordinate.Evaluation;
import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class IterativeAffineCoordinateTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    Chop._10.requireAllZero(weights.dot(levers));
    Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
  }

  public void testAmplifiers() {
    for (Amplifiers amplifiers : Amplifiers.values()) {
      for (int k : new int[] { 0, 1, 5, 10 }) {
        GenesisDeque genesis = new IterativeAffineCoordinate(amplifiers.supply(3), k);
        for (int n = 3; n < 10; ++n) {
          Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
          if (ConvexHull.isInside(levers)) {
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

  public void testArraysList() {
    List<Object> list = Arrays.asList();
    assertEquals(list.size(), 0);
  }
}
