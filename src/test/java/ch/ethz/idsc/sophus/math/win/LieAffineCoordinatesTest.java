// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LieAffineCoordinatesTest extends TestCase {
  public void testMean() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(points);
      LieAffineCoordinates lieAffineCoordinates = new LieAffineCoordinates(RnGroup.INSTANCE, RnExponential.INSTANCE, RnBiinvariantMean.INSTANCE, points);
      Tensor x = RandomVariate.of(distribution, 2);
      Tensor w1 = affineCoordinates.apply(x);
      Tensor w2 = lieAffineCoordinates.apply(x);
      Chop._10.requireClose(w1, w2);
    }
  }

  public void testUnity() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 5; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      LieAffineCoordinates lieAffineCoordinates = new LieAffineCoordinates( //
          Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE, biinvariantMean, points);
      Tensor x = RandomVariate.of(distribution, 3);
      Tensor weights = lieAffineCoordinates.apply(x);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      assertTrue(Scalars.lessThan(Norm._2.between(x, x_recreated), RealScalar.of(0.2)));
    }
  }
}
