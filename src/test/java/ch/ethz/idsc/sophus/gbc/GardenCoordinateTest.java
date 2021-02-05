// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class GardenCoordinateTest extends TestCase {
  public void testR1equiv() {
    // in R^d we have w^H = w^G
    // but not in R2 etc.
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.TWO));
    for (int d = 1; d < 5; ++d)
      for (int n = d + 1; n < d + 4; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        Tensor origin = RandomVariate.of(distribution, d);
        Chop._08.requireClose( //
            Biinvariants.GARDEN.weighting(vectorLogManifold, variogram, sequence).apply(origin), //
            Biinvariants.HARBOR.weighting(vectorLogManifold, variogram, sequence).apply(origin));
        // Chop._05.requireClose( //
        // Biinvariants.GARDEN.coordinate(vectorLogManifold, variogram, sequence).apply(origin), //
        // Biinvariants.HARBOR.coordinate(vectorLogManifold, variogram, sequence).apply(origin));
      }
  }

  public void testNullFail() {
    AssertFail.of(() -> GardenCoordinate.of(RnManifold.INSTANCE, null, HilbertMatrix.of(10, 3)));
  }
}
