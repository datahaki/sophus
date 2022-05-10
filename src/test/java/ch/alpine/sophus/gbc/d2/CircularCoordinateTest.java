// code by jph
package ch.alpine.sophus.gbc.d2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class CircularCoordinateTest {
  @Test
  public void testSimple() {
    int n = 5;
    Tensor circle = CirclePoints.of(n);
    Tensor noises = RandomVariate.of(UniformDistribution.of(-0.1, 0.1), n, 2);
    Tensor p = circle.add(noises);
    Tensor solve = CircularCoordinate.solve(p);
    Chop._10.requireClose(solve.dot(p), circle);
  }

  @Test
  public void testHello() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      Tensor weights = CircularCoordinate.INSTANCE.origin(sequence);
      MeanDefect meanDefect = new MeanDefect(sequence, weights, RnGroup.INSTANCE.exponential());
      Tensor tangent = meanDefect.tangent();
      Chop._07.requireAllZero(tangent);
    }
  }
}
