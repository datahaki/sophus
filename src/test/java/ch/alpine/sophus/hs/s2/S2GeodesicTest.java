// code by jph
package ch.alpine.sophus.hs.s2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.chq.FiniteTensorQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class S2GeodesicTest {
  @Test
  public void testSimple() {
    Tensor p = UnitVector.of(3, 0);
    Tensor q = UnitVector.of(3, 1);
    Tensor split = S2Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    assertEquals(Vector2Norm.of(split), RealScalar.ONE);
    assertEquals(split.Get(0), split.Get(1));
    assertTrue(Scalars.isZero(split.Get(2)));
  }

  @Test
  public void testSame() {
    Tensor p = UnitVector.of(3, 2);
    Tensor q = UnitVector.of(3, 2);
    Tensor split = S2Geodesic.INSTANCE.split(p, q, RandomVariate.of(NormalDistribution.standard()));
    ExactTensorQ.require(split);
    assertEquals(split, p);
  }

  @Test
  public void testOpposite() {
    Tensor p = UnitVector.of(3, 2);
    Tensor q = UnitVector.of(3, 2).negate();
    Tensor split = S2Geodesic.INSTANCE.split(p, q, RandomVariate.of(NormalDistribution.standard()));
    assertTrue(FiniteTensorQ.of(split));
  }

  @Test
  public void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Chop._14.requireClose(p, S2Geodesic.INSTANCE.split(p, q, RealScalar.ZERO));
      Tensor r = S2Geodesic.INSTANCE.split(p, q, RealScalar.ONE);
      Chop._12.requireClose(q, r);
      Chop._12.requireClose(Vector2Norm.of(r), RealScalar.ONE);
    }
  }

  @Test
  public void testArticle() {
    Tensor p = Tensors.vector(1, 0, 0);
    Tensor q = Tensors.vector(0, 1 / Math.sqrt(5), 2 / Math.sqrt(5));
    Tensor tensor = S2Geodesic.INSTANCE.split(p, q, RealScalar.of(0.4));
    // in sync with Mathematica
    Tensor expect = Tensors.vector(0.8090169943749473, 0.2628655560595668, 0.5257311121191336);
    Chop._12.requireClose(tensor, expect);
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> S2Geodesic.INSTANCE.split(UnitVector.of(4, 0), UnitVector.of(3, 1), RealScalar.ZERO));
    S2Geodesic.INSTANCE.split(Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RationalScalar.HALF);
  }
}
