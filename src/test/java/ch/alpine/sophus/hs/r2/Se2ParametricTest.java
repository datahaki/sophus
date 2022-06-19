// code by jph
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class Se2ParametricTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(1 + 3, 2 + 4, 3);
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    assertEquals(scalar, RealScalar.of(5));
    TensorMetric tensorMetric = Serialization.copy(Se2Parametric.INSTANCE);
    Chop._10.requireClose(scalar, tensorMetric.distance(p, q));
  }

  @Test
  void testHalf() {
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(1, 2 + 2 * 3, 3 + Math.PI);
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    Chop._14.requireClose(scalar, RealScalar.of(3 * Math.PI));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  @Test
  void testSimpleUnits() {
    Tensor p = Tensors.fromString("{1[m], 2[m], 3}");
    Tensor q = Tensors.fromString("{4[m], 6[m], 3}");
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    assertEquals(scalar, Quantity.of(5, "m"));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  @Test
  void testOtherUnits() {
    Tensor p = Tensors.fromString("{1[m], 2[m], 3}");
    Tensor q = Tensors.fromString("{4[m], 6[m], 3.3}");
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    Chop._12.requireClose(scalar, Quantity.of(5.018799335788676, "m"));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  @Test
  void testOrigin() {
    assertEquals(Se2ParametricDeprecate.INSTANCE.distance(Tensors.vector(0, 0, 0), Tensors.vector(0, 0, 0)), RealScalar.of(0));
  }

  @Test
  void testOrderInvariant() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar dpq = Se2ParametricDeprecate.INSTANCE.distance(p, q);
      Scalar dqp = Se2ParametricDeprecate.INSTANCE.distance(q, p);
      Scalar epq = Se2Parametric.INSTANCE.distance(p, q);
      Scalar eqp = Se2Parametric.INSTANCE.distance(q, p);
      Chop._14.requireClose(dpq, dqp);
      Chop._14.requireClose(dpq, eqp);
      Chop._14.requireClose(epq, eqp);
    }
  }

  @Test
  void testSymmetrize() {
    Distribution distribution = NormalDistribution.standard();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Scalar pq = Se2Parametric.INSTANCE.distance(p, q);
    Scalar qp = Se2Parametric.INSTANCE.distance(q, p);
    Tolerance.CHOP.requireClose(pq, qp);
  }
}
