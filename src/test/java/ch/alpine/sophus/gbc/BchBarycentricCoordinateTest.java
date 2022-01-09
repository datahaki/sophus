// code by jph
package ch.alpine.sophus.gbc;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.bm.BchBiinvariantMean;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class BchBarycentricCoordinateTest extends TestCase {
  public void testSe2() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    BinaryOperator<Tensor> bch = Se2Algebra.INSTANCE.bch(6);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor x = RandomVariate.of(distribution, 3);
      BarycentricCoordinate barycentricCoordinate = //
          BchBarycentricCoordinate.of(bch, InversePowerVariogram.of(2));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = BchBiinvariantMean.of(bch, Chop._08).mean(sequence, weights);
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
    BinaryOperator<Tensor> bch = So3Algebra.INSTANCE.bch(6);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor x = RandomVariate.of(distribution, 3);
      BarycentricCoordinate barycentricCoordinate = //
          BchBarycentricCoordinate.of(bch, InversePowerVariogram.of(2));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = BchBiinvariantMean.of(bch, Chop._08).mean(sequence, weights);
      Chop._06.requireClose(mean, x);
    }
  }
}
