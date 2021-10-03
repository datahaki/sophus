// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.lie.rn.RnExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class CircularCoordinateTest extends TestCase {
  public void testSimple() {
    int n = 5;
    Tensor circle = CirclePoints.of(n);
    Tensor noises = RandomVariate.of(UniformDistribution.of(-0.1, 0.1), n, 2);
    Tensor p = circle.add(noises);
    Tensor solve = CircularCoordinate.solve(p);
    Chop._10.requireClose(solve.dot(p), circle);
  }

  public void testHello() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      Tensor weights = CircularCoordinate.INSTANCE.origin(sequence);
      MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
      Tensor tangent = meanDefect.tangent();
      Chop._07.requireAllZero(tangent);
    }
  }
}