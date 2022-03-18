// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.LeveragesGenesis;
import ch.alpine.sophus.hs.ad.HsAdGeodesic;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.ad.HsBarycentricCoordinate;
import ch.alpine.sophus.hs.ad.HsBiinvariantMean;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;

public class StAlgebraTest {
  @Test
  public void test5x3() {
    HsAlgebra hsAlgebra = StAlgebra.of(5, 3, 8);
    assertFalse(hsAlgebra.isHTrivial());
    assertEquals(hsAlgebra.dimM(), 9);
    assertEquals(hsAlgebra.dimG(), 10);
  }

  @Test
  public void test5x2Geodesic() {
    HsAlgebra hsAlgebra = StAlgebra.of(5, 2, 8);
    assertFalse(hsAlgebra.isHTrivial());
    assertEquals(hsAlgebra.dimM(), 7);
    assertEquals(hsAlgebra.dimG(), 10);
    HsAdGeodesic hsAdGeodesic = new HsAdGeodesic(hsAlgebra);
    Distribution distribution = TriangularDistribution.with(0, 0.1);
    Tensor x = RandomVariate.of(distribution, 7);
    Tensor y = RandomVariate.of(distribution, 7);
    hsAdGeodesic.split(x, y, RationalScalar.of(1, 3));
  }

  @Test
  public void test5x2Bary() {
    HsAlgebra hsAlgebra = StAlgebra.of(5, 2, 8);
    assertFalse(hsAlgebra.isHTrivial());
    assertEquals(hsAlgebra.dimM(), 7);
    assertEquals(hsAlgebra.dimG(), 10);
    Distribution distribution = TriangularDistribution.with(0, 0.01);
    Tensor sequence = RandomVariate.of(distribution, 12, 7);
    Tensor point = RandomVariate.of(distribution, 7);
    HsBarycentricCoordinate hsBarycentricCoordinate = //
        new HsBarycentricCoordinate(hsAlgebra, LeveragesGenesis.DEFAULT);
    Tensor weights = hsBarycentricCoordinate.weights(sequence, point);
    BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
    Tensor mean = biinvariantMean.mean(sequence, weights);
    Tolerance.CHOP.requireClose(point, mean);
  }

  @Test
  public void testInteresting() {
    StAlgebra.of(5, 5, 8);
    AssertFail.of(() -> StAlgebra.of(5, 6, 8));
  }

  @Test
  public void testFails() {
    AssertFail.of(() -> StAlgebra.of(5, -1, 8));
  }
}
