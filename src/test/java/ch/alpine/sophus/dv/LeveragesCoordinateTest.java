// code by jph
package ch.alpine.sophus.dv;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class LeveragesCoordinateTest {
  @Test
  void testR1equiv() {
    // in R1 we have W^ID = w^IL
    // but not in R2 etc.
    Manifold manifold = RnGroup.INSTANCE;
    MetricBiinvariant metricBiinvariant = new MetricBiinvariant(manifold);
    LeveragesBiinvariant leveragesBiinvariant = new LeveragesBiinvariant(manifold);
    ScalarUnaryOperator variogram = s -> s;
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.TWO));
    for (int length = 3; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 1);
      Tensor origin = RandomVariate.of(distribution, 1);
      Chop._08.requireClose( //
          metricBiinvariant.weighting(variogram, sequence).sunder(origin), //
          leveragesBiinvariant.weighting(variogram, sequence).sunder(origin));
    }
  }

  @Test
  void testDiagonalNorm() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    Tensor betas = RandomVariate.of(UniformDistribution.of(1, 2), 4);
    for (Tensor beta_ : betas) {
      Scalar beta = (Scalar) beta_;
      BarycentricCoordinate bc1 = LeveragesCoordinate.of(new HsDesign(SnManifold.INSTANCE), InversePowerVariogram.of(beta));
      for (int d = 3; d < 7; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        for (int n = d + 1; n < d + 3; ++n) {
          Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream() //
              .map(mean::add) //
              .map(Vector2Norm.NORMALIZE));
          bc1.weights(sequence, mean);
        }
      }
    }
  }

  @Test
  void testSe2() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    ScalarUnaryOperator variogram = s -> s;
    BarycentricCoordinate targetCoordinate = LeveragesCoordinate.of(new HsDesign(Se2CoveringGroup.INSTANCE), variogram);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      targetCoordinate.weights(sequence, point);
    }
  }
}
