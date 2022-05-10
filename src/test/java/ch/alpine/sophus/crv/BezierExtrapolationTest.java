// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;

class BezierExtrapolationTest {
  @Test
  public void testSimple() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(RnGeodesic.INSTANCE);
    for (int index = 2; index < 10; ++index)
      assertEquals(tensorUnaryOperator.apply(Range.of(0, index)), RealScalar.of(index));
  }

  @Test
  public void testCircle2() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(Se2Group.INSTANCE);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.fromString("{{0, 0, 0}, {1, 1, " + Math.PI / 2 + "}}"));
    Tensor result = Tensors.vector(0, 2, -Math.PI);
    Chop._14.requireClose(tensor, result);
  }

  @Test
  public void testCircle3() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(Se2Group.INSTANCE);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.fromString("{{0, 0, 0}, {1, 1, " + Math.PI / 2 + "}, {0, 2, " + Math.PI + "}}"));
    Tensor result = Tensors.vector(-1, 1, -Math.PI * 1 / 2);
    Chop._14.requireClose(tensor, result);
  }

  @Test
  public void testFailScalar() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(RnGeodesic.INSTANCE);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(RealScalar.ONE));
  }

  @Test
  public void testFailEmpty() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(Se2Group.INSTANCE);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(Tensors.empty()));
  }

  @Test
  public void testFailSe2_1() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(Se2Group.INSTANCE);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(Tensors.vector(1)));
  }

  @Test
  public void testFailSe2() {
    TensorUnaryOperator tensorUnaryOperator = BezierExtrapolation.of(Se2Group.INSTANCE);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(Tensors.vector(1, 2, 3)));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> BezierExtrapolation.of(null));
  }
}
