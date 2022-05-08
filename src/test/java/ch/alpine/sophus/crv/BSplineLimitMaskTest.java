// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.ga.GeodesicCenter;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;

class BSplineLimitMaskTest {
  @Test
  public void testLimitMask() {
    assertEquals(BSplineLimitMask.FUNCTION.apply(0 * 2 + 1), Tensors.fromString("{1}"));
    assertEquals(BSplineLimitMask.FUNCTION.apply(1 * 2 + 1), Tensors.fromString("{1/6, 2/3, 1/6}"));
    assertEquals(BSplineLimitMask.FUNCTION.apply(2 * 2 + 1), Tensors.fromString("{1/120, 13/60, 11/20, 13/60, 1/120}"));
    assertEquals(BSplineLimitMask.FUNCTION.apply(3 * 2 + 1), Tensors.fromString("{1/5040, 1/42, 397/1680, 151/315, 397/1680, 1/42, 1/5040}"));
  }

  @Test
  public void testEvenFail() {
    for (int i = 0; i < 10; ++i) {
      int fi = i;
      assertThrows(Exception.class, () -> BSplineLimitMask.FUNCTION.apply(fi * 2));
    }
  }

  @Test
  public void testNegativeFail() {
    for (int i = 1; i < 4; ++i) {
      int fi = i;
      assertThrows(Exception.class, () -> BSplineLimitMask.FUNCTION.apply(-fi));
    }
  }

  private static final TensorUnaryOperator TENSOR_UNARY_OPERATOR = //
      GeodesicCenter.of(RnGeodesic.INSTANCE, BSplineLimitMask.FUNCTION);

  @Test
  public void testSimple3() {
    Tensor tensor = TENSOR_UNARY_OPERATOR.apply(Tensors.vector(1, 2, 3));
    assertEquals(tensor, RealScalar.of(2));
  }

  @Test
  public void testSimple5() {
    Tensor tensor = TENSOR_UNARY_OPERATOR.apply(Tensors.vector(1, 2, 3, 4, 5));
    assertEquals(tensor, RealScalar.of(3));
  }

  @Test
  public void testAdvanced5() {
    Tensor tensor = TENSOR_UNARY_OPERATOR.apply(Tensors.vector(3, 2, 3, 4, 5));
    assertEquals(tensor, RationalScalar.of(181, 60));
  }

  @Test
  public void testEvenVectorFail() {
    assertThrows(Exception.class, () -> TENSOR_UNARY_OPERATOR.apply(Tensors.vector(1, 2, 3, 4)));
  }

  @Test
  public void testScalarFail() {
    assertThrows(Exception.class, () -> TENSOR_UNARY_OPERATOR.apply(RealScalar.ONE));
  }
}
