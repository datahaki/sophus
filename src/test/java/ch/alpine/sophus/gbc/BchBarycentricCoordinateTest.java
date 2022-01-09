// code by jph
package ch.alpine.sophus.gbc;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.bm.BchBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import junit.framework.TestCase;

public class BchBarycentricCoordinateTest extends TestCase {
  private static final BinaryOperator<Tensor> BCH_SE2 = BakerCampbellHausdorff.of(N.DOUBLE.of(Tensors.fromString( //
      "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}")), 6);
  private static final BinaryOperator<Tensor> BCH_SO3 = BakerCampbellHausdorff.of(N.DOUBLE.of(LeviCivitaTensor.of(3).negate()), 6);

  public void testSe2() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor x = RandomVariate.of(distribution, 3);
      BarycentricCoordinate barycentricCoordinate = //
          BchBarycentricCoordinate.of(BCH_SE2, InversePowerVariogram.of(2));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = BchBiinvariantMean.of(BCH_SE2, Chop._08).mean(sequence, weights);
      Chop._06.requireClose(mean, x);
      // ---
      Tensor seqG = Tensor.of(sequence.stream().map(Se2CoveringExponential.INSTANCE::exp));
      BarycentricCoordinate bc = LeveragesCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2));
      Tensor weights2 = bc.weights(seqG, Se2CoveringExponential.INSTANCE.exp(x));
      Chop._08.requireClose(weights, weights2);
    }
  }

  public void testSo3MeanRandom() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor x = RandomVariate.of(distribution, 3);
      BarycentricCoordinate barycentricCoordinate = //
          BchBarycentricCoordinate.of(BCH_SO3, InversePowerVariogram.of(2));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = BchBiinvariantMean.of(BCH_SO3, Chop._08).mean(sequence, weights);
      Chop._06.requireClose(mean, x);
    }
  }
}
