// code by jph, ob 
package ch.ethz.idsc.sophus.hs.r2;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringParametricTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    TensorMetric tensorMetric = Serialization.copy(Se2CoveringParametric.INSTANCE);
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    Chop._11.requireClose(scalar, RealScalar.of(5));
  }

  public void testInfinity() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3 + Math.PI * 2));
    assertTrue(Scalars.lessThan(RealScalar.of(1e10), scalar));
  }

  public void testHalf() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1, 2 + 2 * 3, 3 + Math.PI));
    Chop._14.requireClose(scalar, RealScalar.of(3 * Math.PI));
  }

  public void testSE2() {
    double rand = Math.random();
    Scalar scalarSE2C = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.vector(1, 2, 0), Tensors.vector(1, 2 + 2 * 3, rand * Math.PI));
    Scalar scalarSE2 = Se2Parametric.INSTANCE.distance(Tensors.vector(1, 2, 0), Tensors.vector(1, 2 + 2 * 3, rand * Math.PI));
    Chop._14.requireClose(scalarSE2, scalarSE2C);
  }

  public void testSimpleUnits() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{4[m], 6[m], 3}"));
    assertEquals(scalar, Quantity.of(5, "m"));
  }

  public void testOtherUnits() {
    Scalar scalar = Se2CoveringParametricDeprecat.INSTANCE.distance(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{4[m], 6[m], 3.3}"));
    Chop._12.requireClose(scalar, Quantity.of(5.018799335788676, "m"));
  }

  public void testUnitCircle54Pi() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 + 2 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.HALF.add(Pi.TWO));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  public void testUnitCircle34Pi() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 - 2 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.TWO.multiply(RationalScalar.of(3, 4)));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  public void testUnitCircle34Pib() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(1, 1, Math.PI / 2 - 4 * Math.PI);
    Scalar se2 = Se2Parametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2, Pi.HALF);
    Scalar se2c = Se2CoveringParametricDeprecat.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, Pi.TWO.multiply(RationalScalar.of(7, 4)));
    Scalar se2d = Se2CoveringParametric.INSTANCE.distance(p, q);
    Chop._14.requireClose(se2c, se2d);
  }

  public void testOrderInvariant() {
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
