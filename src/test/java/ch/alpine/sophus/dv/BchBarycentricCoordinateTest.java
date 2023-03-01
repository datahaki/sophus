// code by jph
package ch.alpine.sophus.dv;

import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.ad.HsBiinvariantMean;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class BchBarycentricCoordinateTest {
  @Test
  void testSe2() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    RandomGenerator random = new Random(1);
    BinaryOperator<Tensor> bch = Se2Algebra.INSTANCE.bch(6);
    // System.out.println(bch.getClass().getSimpleName());
    Tensor ad = Se2Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2.5);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Tensor x = RandomVariate.of(distribution, random, 3);
      BarycentricCoordinate barycentricCoordinate = //
          new BchBarycentricCoordinate(bch, new LeveragesGenesis(variogram));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = HsBiinvariantMean.of(hsAlgebra).mean(sequence, weights);
      Chop._06.requireClose(mean, x);
      // ---
      Tensor seqG = Tensor.of(sequence.stream().map(Se2CoveringGroup.INSTANCE::exp));
      BarycentricCoordinate bc = LeveragesCoordinate.of(new HsDesign(Se2CoveringGroup.INSTANCE), variogram);
      Tensor weights2 = bc.weights(seqG, Se2CoveringGroup.INSTANCE.exp(x));
      Chop._08.requireClose(weights, weights2);
    }
  }

  @Test
  void testSo3MeanRandom() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    RandomGenerator random = new Random(1);
    BinaryOperator<Tensor> bch = So3Algebra.INSTANCE.bch(6);
    Tensor ad = So3Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Tensor x = RandomVariate.of(distribution, random, 3);
      BarycentricCoordinate barycentricCoordinate = //
          new BchBarycentricCoordinate(bch, new LeveragesGenesis(InversePowerVariogram.of(2)));
      Tensor weights = barycentricCoordinate.weights(sequence, x);
      Tensor mean = HsBiinvariantMean.of(hsAlgebra).mean(sequence, weights);
      Tolerance.CHOP.requireClose(mean, x);
    }
  }
}
