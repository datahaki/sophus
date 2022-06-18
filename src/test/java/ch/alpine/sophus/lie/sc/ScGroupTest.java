// code by jph
package ch.alpine.sophus.lie.sc;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.HsCoordinates;
import ch.alpine.sophus.dv.MetricCoordinate;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class ScGroupTest {
  @Test
  void testSimple1() {
    Distribution distribution = UniformDistribution.of(-500, 500);
    for (int count = 0; count < 100; ++count) {
      Tensor x = RandomVariate.of(distribution, 1);
      Tensor exp = ScGroup.INSTANCE.exp(x);
      Tensor log = ScGroup.INSTANCE.log(exp);
      Chop._10.requireClose(x, log);
    }
  }

  public static final BarycentricCoordinate INSTANCE = new HsCoordinates( //
      new HsDesign(ScGroup.INSTANCE), //
      new MetricCoordinate(NormWeighting.of(ScVectorNorm.INSTANCE, InversePowerVariogram.of(1))));

  @Test
  void testSimple2() {
    Tensor sequence = Tensors.vector(2, 4).map(Tensors::of);
    Tensor target = Tensors.vector(1);
    Tensor weights = INSTANCE.weights(sequence, target);
    Tensor mean = ScBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._10.requireClose(target, mean);
  }

  @Test
  void testRandom() {
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
