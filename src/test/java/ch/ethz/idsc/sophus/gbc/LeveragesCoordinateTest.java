// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class LeveragesCoordinateTest extends TestCase {
  public void testR1equiv() {
    // in R1 we have W^ID = w^IL
    // but not in R2 etc.
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.TWO));
    for (int length = 3; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 1);
      Tensor origin = RandomVariate.of(distribution, 1);
      Chop._08.requireClose( //
          MetricBiinvariant.EUCLIDEAN.weighting(vectorLogManifold, variogram, sequence).apply(origin), //
          Biinvariants.LEVERAGES.weighting(vectorLogManifold, variogram, sequence).apply(origin));
    }
  }

  public void testDiagonalNorm() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    Tensor betas = RandomVariate.of(UniformDistribution.of(1, 2), 4);
    for (Tensor beta_ : betas) {
      Scalar beta = (Scalar) beta_;
      BarycentricCoordinate bc1 = LeveragesCoordinate.of(SnManifold.INSTANCE, InversePowerVariogram.of(beta));
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

  public void testSe2() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    ScalarUnaryOperator variogram = s -> s;
    BarycentricCoordinate targetCoordinate = LeveragesCoordinate.of(Se2CoveringManifold.INSTANCE, variogram);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      targetCoordinate.weights(sequence, point);
    }
  }
}
