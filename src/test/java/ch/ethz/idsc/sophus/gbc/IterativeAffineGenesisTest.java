// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class IterativeAffineGenesisTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    Chop._10.requireAllZero(weights.dot(levers));
    Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
  }

  public void testSimple() {
    List<ScalarUnaryOperator> list = Arrays.asList( //
        Amplifiers.exp(3), Amplifiers.exp(5), //
        Amplifiers.ramp(3), Amplifiers.ramp(5), //
        Amplifiers.arctan(3), Amplifiers.arctan(5));
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers))
        for (ScalarUnaryOperator amp : list)
          _check(levers, new IterativeAffineGenesis(amp, Chop._08).origin(levers));
    }
  }
}
