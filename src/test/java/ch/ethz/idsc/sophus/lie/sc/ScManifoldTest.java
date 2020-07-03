// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.math.NormWeighting;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ScManifoldTest extends TestCase {
  public static final BarycentricCoordinate INSTANCE = MetricCoordinate.custom( //
      ScManifold.INSTANCE, //
      NormWeighting.of(ScVectorNorm.INSTANCE, InversePowerVariogram.of(1)));

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
