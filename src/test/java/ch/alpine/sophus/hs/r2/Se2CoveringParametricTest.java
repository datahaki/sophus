// code by jph, ob
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class Se2CoveringParametricTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    TensorMetric tensorMetric = Serialization.copy(Se2CoveringParametric.INSTANCE);
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    Chop._11.requireClose(scalar, RealScalar.of(5));
  }

  @Test
  void testInfinity() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3 + Math.PI * 2));
    assertTrue(Scalars.lessThan(RealScalar.of(1e10), scalar));
  }

  @Test
  void testHalf() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1, 2 + 2 * 3, 3 + Math.PI));
    Chop._14.requireClose(scalar, RealScalar.of(3 * Math.PI));
  }

  @Test
  void testSE2() {
    double rand = Math.random();
    Scalar scalarSE2C = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 0), Tensors.vector(1, 2 + 2 * 3, rand * Math.PI));
    Scalar scalarSE2 = Se2Parametric.INSTANCE.distance(Tensors.vector(1, 2, 0), Tensors.vector(1, 2 + 2 * 3, rand * Math.PI));
    Chop._14.requireClose(scalarSE2, scalarSE2C);
  }

  @Test
  void testSimpleUnits() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{4[m], 6[m], 3}"));
    assertEquals(scalar, Quantity.of(5, "m"));
  }

  @Test
  void testOtherUnits() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{4[m], 6[m], 3.3}"));
    Chop._12.requireClose(scalar, Quantity.of(5.018799335788676, "m"));
  }

  @Test
  void testUnitCircle54Pi() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 + 2 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.HALF.add(Pi.TWO));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  @Test
  void testUnitCircle34Pi() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 - 2 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.TWO.multiply(RationalScalar.of(3, 4)));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  @Test
  void testUnitCircle34Pib() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 - 4 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.TWO.multiply(RationalScalar.of(7, 4)));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  @Test
  void testOrderInvariant() {
    Distribution distribution = UniformDistribution.of(-5, 5);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar dpq = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
      Scalar dqp = Se2CoveringParametricDeprecat.INSTANCE.distance(q, p);
      Chop._14.requireClose(dpq, dqp);
      Scalar epq = Se2CoveringParametric.INSTANCE.distance(p, q);
      Scalar eqp = Se2CoveringParametric.INSTANCE.distance(q, p);
      Chop._10.requireClose(dpq, eqp);
      Chop._10.requireClose(epq, eqp);
    }
  }
}
