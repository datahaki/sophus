// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
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
      }
  }

  public void testNullFail() {
    AssertFail.of(() -> GardenCoordinate.of(RnManifold.INSTANCE, null, HilbertMatrix.of(10, 3)));
  }
}