// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2ParametricTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(1 + 3, 2 + 4, 3);
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    assertEquals(scalar, RealScalar.of(5));
    TensorMetric tensorMetric = Serialization.copy(Se2Parametric.INSTANCE);
    Chop._10.requireClose(scalar, tensorMetric.distance(p, q));
  }

  public void testHalf() {
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(1, 2 + 2 * 3, 3 + Math.PI);
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    Chop._14.requireClose(scalar, RealScalar.of(3 * Math.PI));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  public void testSimpleUnits() {
    Tensor p = Tensors.fromString("{1[m], 2[m], 3}");
    Tensor q = Tensors.fromString("{4[m], 6[m], 3}");
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    assertEquals(scalar, Quantity.of(5, "m"));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  public void testOtherUnits() {
    Tensor p = Tensors.fromString("{1[m], 2[m], 3}");
    Tensor q = Tensors.fromString("{4[m], 6[m], 3.3}");
    Scalar scalar = Se2ParametricDeprecate.INSTANCE.distance(p, q);
    Chop._12.requireClose(scalar, Quantity.of(5.018799335788676, "m"));
    Chop._10.requireClose(scalar, //
        Se2Parametric.INSTANCE.distance(p, q));
  }

  public void testOrigin() {
    assertEquals(Se2ParametricDeprecate.INSTANCE.distance(Tensors.vector(0, 0, 0), Tensors.vector(0, 0, 0)), RealScalar.of(0));
  }

  public void testOrderInvariant() {
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
}
