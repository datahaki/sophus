// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class GardenCoordinateTest {
  @Test
  void testR1equiv() {
    // in R^d we have w^H = w^G
    // but not in R2 etc.
    Manifold manifold = RnGroup.INSTANCE;
    GardenBiinvariant gardenBiinvariant = new GardenBiinvariant(manifold);
    HarborBiinvariant harborBiinvariant = new HarborBiinvariant(manifold);
    ScalarUnaryOperator variogram = s -> s;
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.TWO));
    for (int d = 1; d < 5; ++d)
      for (int n = d + 1; n < d + 4; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        Tensor origin = RandomVariate.of(distribution, d);
        Chop._08.requireClose( //
            gardenBiinvariant.weighting(variogram, sequence).sunder(origin), //
            harborBiinvariant.weighting(variogram, sequence).sunder(origin));
      }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new GardenCoordinate(RnGroup.INSTANCE, null, HilbertMatrix.of(10, 3)));
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(GardenCoordinate.class.getModifiers()));
  }
}
