// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.IOException;

import ch.ethz.idsc.sophus.krg.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Entrywise;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Imag;
import junit.framework.TestCase;

public class LagrangeCoordinateTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    AffineQ.require(weights, Chop._10);
    Chop._08.requireAllZero(weights.dot(levers));
  }

  public void testReal() throws ClassNotFoundException, IOException {
    Genesis idw = InverseDistanceWeighting.of(InversePowerVariogram.of(2));
    Genesis genesis = Serialization.copy(LagrangeCoordinate.of(idw));
    Genesis idc = MetricCoordinate.of(idw);
    for (int d = 1; d < 4; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, d);
        _check(levers, genesis.origin(levers));
        _check(levers, idc.origin(levers));
      }
  }

  public void testComplex() throws ClassNotFoundException, IOException {
    Genesis idw = InverseDistanceWeighting.of(InversePowerVariogram.of(2));
    Genesis genesis = Serialization.copy(LagrangeCoordinate.of(idw));
    for (int d = 1; d < 4; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor levers = Entrywise.with(ComplexScalar::of).apply( //
            RandomVariate.of(NormalDistribution.standard(), n, d), //
            RandomVariate.of(NormalDistribution.standard(), n, d));
        {
          Tensor weights = idw.origin(levers);
          Chop._12.allZero(Imag.of(weights));
        }
        {
          Tensor weights = genesis.origin(levers);
          // System.out.println(Imag.of(weights));
          _check(levers, weights);
        }
      }
  }

  public void testNullFail() {
    AssertFail.of(() -> LagrangeCoordinate.of(null));
  }
}
