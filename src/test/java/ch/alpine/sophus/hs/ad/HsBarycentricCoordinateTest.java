// code by jph
package ch.alpine.sophus.hs.ad;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.LeveragesGenesis;
import ch.alpine.sophus.hs.sn.SnAlgebra;
import ch.alpine.sophus.math.sample.BallRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;

class HsBarycentricCoordinateTest {
  @Test
  public void testSimple() {
    Random random = new Random(3);
    for (int d = 2; d < 5; ++d) {
      HsAlgebra hsAlgebra = SnAlgebra.of(d);
      RandomSampleInterface randomSampleInterface = BallRandomSample.of(Array.zeros(d), RealScalar.of(0.05));
      BarycentricCoordinate barycentricCoordinate = new HsBarycentricCoordinate(hsAlgebra, new LeveragesGenesis(InversePowerVariogram.of(2)));
      for (int n = d + 1; n < d + 4; ++n) {
        Tensor sequence = RandomSample.of(randomSampleInterface, random, n);
        Tensor point = RandomSample.of(randomSampleInterface, random);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
        Tensor hsmean = biinvariantMean.mean(sequence, weights);
        Tolerance.CHOP.requireClose(point, hsmean);
      }
    }
  }
}
