// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdRiemannTest extends TestCase {
  public void testSimple2() {
    SpdRiemann spdRiemann = new SpdRiemann(IdentityMatrix.of(2));
    Tensor e11 = Tensors.fromString("{{1, 0}, {0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1}, {1, 0}}");
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    ExactScalarQ.require(s1112);
    assertEquals(s1112, RationalScalar.of(-1, 4));
  }

  public void testSimple3() {
    SpdRiemann spdRiemann = new SpdRiemann(IdentityMatrix.of(3));
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    ExactScalarQ.require(s1112);
    assertEquals(s1112, RationalScalar.of(-1, 4));
    Scalar s1213 = spdRiemann.sectional(e12, e13);
    ExactScalarQ.require(s1213);
    assertEquals(s1213, RationalScalar.of(-1, 8));
  }

  public void testTransport3() {
    Tensor p = IdentityMatrix.of(3);
    Tensor q = TestHelper.generateSpd(3);
    SpdRiemann spdRiemann = new SpdRiemann(q);
    TensorUnaryOperator tuo = SpdTransport.INSTANCE.shift(p, q);
    Tensor e11 = tuo.apply(Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}"));
    Tensor e12 = tuo.apply(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}"));
    Tensor e13 = tuo.apply(Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}"));
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    Chop._10.requireClose(s1112, RationalScalar.of(-1, 4));
    Scalar s1213 = spdRiemann.sectional(e12, e13);
    Chop._10.requireClose(s1213, RationalScalar.of(-1, 8));
  }

  public void testMultiT() {
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Tensor p = IdentityMatrix.of(3);
    for (int c = 0; c < 5; ++c) {
      Tensor q = TestHelper.generateSpd(3);
      TensorUnaryOperator tuo = SpdTransport.INSTANCE.shift(p, q);
      e11 = tuo.apply(e11);
      e12 = tuo.apply(e12);
      e13 = tuo.apply(e13);
      SpdRiemann spdRiemann = new SpdRiemann(q);
      Scalar s1112 = spdRiemann.sectional(e11, e12);
      Chop._10.requireClose(s1112, RationalScalar.of(-1, 4));
      Scalar s1213 = spdRiemann.sectional(e12, e13);
      Chop._10.requireClose(s1213, RationalScalar.of(-1, 8));
      p = q;
    }
  }
}
