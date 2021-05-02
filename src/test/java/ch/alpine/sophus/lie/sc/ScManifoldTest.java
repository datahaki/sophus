// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.ExponentialDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class ScManifoldTest extends TestCase {
  public static final BarycentricCoordinate INSTANCE = HsCoordinates.wrap( //
      ScManifold.INSTANCE, //
      MetricCoordinate.of(NormWeighting.of(ScVectorNorm.INSTANCE, InversePowerVariogram.of(1))));

  public void testSimple() {
    Tensor sequence = Tensors.vector(2, 4).map(Tensors::of);
    Tensor target = Tensors.vector(1);
    Tensor weights = INSTANCE.weights(sequence, target);
    Tensor mean = ScBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._10.requireClose(target, mean);
  }

  public void testRandom() {
    for (int n = 4; n < 10; ++n) {
      Distribution distribution = ExponentialDistribution.of(1);
      Tensor sequence = RandomVariate.of(distribution, n, 1);
      Tensor target = Tensors.vector(1);
      Tensor weights = INSTANCE.weights(sequence, target);
      Tensor mean = ScBiinvariantMean.INSTANCE.mean(sequence, weights);
      Chop._10.requireClose(target, mean);
    }
  }
}
