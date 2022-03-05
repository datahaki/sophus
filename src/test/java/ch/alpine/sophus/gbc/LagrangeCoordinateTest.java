// code by jph
package ch.alpine.sophus.gbc;

import java.io.IOException;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.d.NegativeBinomialDistribution;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Imag;
import junit.framework.TestCase;

public class LagrangeCoordinateTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    AffineQ.require(weights, Chop._10);
    Chop._08.requireAllZero(weights.dot(levers));
  }

  public void testReal() throws ClassNotFoundException, IOException {
    Genesis idw = new InverseDistanceWeighting(InversePowerVariogram.of(2));
    Genesis genesis = Serialization.copy(new LagrangeCoordinate(idw));
    Genesis idc = new MetricCoordinate(idw);
    for (int d = 1; d < 4; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, d);
        _check(levers, genesis.origin(levers));
        _check(levers, idc.origin(levers));
      }
  }

  public void testComplex() throws ClassNotFoundException, IOException {
    Genesis idw = new InverseDistanceWeighting(InversePowerVariogram.of(2));
    Genesis genesis = Serialization.copy(new LagrangeCoordinate(idw));
    for (int d = 1; d < 4; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor levers = Entrywise.with(ComplexScalar::of).apply( //
            RandomVariate.of(NormalDistribution.standard(), n, d), //
            RandomVariate.of(NormalDistribution.standard(), n, d));
        {
          Tensor weights = idw.origin(levers);
          Tolerance.CHOP.allZero(Imag.of(weights));
        }
        {
          Tensor weights = genesis.origin(levers);
          _check(levers, weights);
        }
      }
  }

  public void testLagrange() {
    Tensor sequence = RandomVariate.of(NegativeBinomialDistribution.of(3, 0.6), 10, 3);
    Tensor point = Mean.of(sequence);
    ExactTensorQ.require(point);
    // does not produce equal weights
    MetricBiinvariant.EUCLIDEAN.lagrainate(RnManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(point);
  }

  public void testNullFail() {
    AssertFail.of(() -> new LagrangeCoordinate(null));
  }
}
