// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
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
    List<TensorUnaryOperator> list = Arrays.asList( //
        Amplifiers.EXP.supply(3), Amplifiers.EXP.supply(5), //
        Amplifiers.RAMP.supply(3), Amplifiers.RAMP.supply(5), //
        Amplifiers.ARCTAN.supply(3), Amplifiers.ARCTAN.supply(5));
    for (TensorUnaryOperator suo : list)
      Chop._12.requireClose(suo.apply(RealScalar.ZERO), RealScalar.ONE);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers))
        for (TensorUnaryOperator amp : list)
          _check(levers, new IterativeAffineGenesis(amp, Chop._08).origin(levers));
    }
  }
}
