// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class RnGroupTest {
  @Test
  void testSimple4() {
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor result = RGroup.INSTANCE.combine(p, Tensors.vector(4, -2, -7));
    assertEquals(result, Tensors.vector(5, 0, -4));
    ExactTensorQ.require(result);
  }

  @Test
  void testAdjoint() {
    assertEquals(Tensor.of(IdentityMatrix.of(3).stream().map(Tensor::copy)), IdentityMatrix.of(3));
    assertEquals(RGroup.INSTANCE.dL(null, HilbertMatrix.of(5)), HilbertMatrix.of(5));
    assertEquals(RGroup.INSTANCE.dR(null, HilbertMatrix.of(5)), HilbertMatrix.of(5));
  }

  @Test
  void testSimple1() {
    Tensor matrix = HilbertMatrix.of(2, 3);
    assertEquals(RGroup.INSTANCE.exponential0().exp(matrix), matrix);
    assertEquals(RGroup.INSTANCE.exponential0().log(matrix), matrix);
  }

  @Test
  void testSimple2() {
    Tensor actual = RGroup.INSTANCE.split(Tensors.vector(10, 1), Tensors.vector(11, 0), RealScalar.of(-1));
    ExactTensorQ.require(actual);
    assertEquals(Tensors.vector(9, 2), actual);
  }

  @Test
  void testSimple3() {
    GeodesicSpace geodesicSpace = RGroup.INSTANCE;
    ScalarTensorFunction scalarTensorFunction = geodesicSpace.curve(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertEquals(scalarTensorFunction.apply(RealScalar.ZERO), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.vector(0.5, 0.5, 0));
  }

  @Test
  void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomVariate.of(distribution, 7);
      Tensor q = RandomVariate.of(distribution, 7);
      Chop._14.requireClose(p, RGroup.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._14.requireClose(q, RGroup.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  @Test
  void testDeBoor() {
    Tensor knots = Tensors.vector(1, 2, 3, 4);
    Tensor control = Tensors.vector(9, 3, 4);
    DeBoor.of(RGroup.INSTANCE, knots, control);
    assertThrows(Exception.class, () -> DeBoor.of(null, knots, control));
  }

  @Test
  void testMetric() {
    MetricManifold tensorMetric = RGroup.INSTANCE;
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    assertEquals(scalar, RealScalar.of(5));
    ExactScalarQ.require(scalar);
  }
}
