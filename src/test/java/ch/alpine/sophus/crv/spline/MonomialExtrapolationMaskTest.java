// code by jph
package ch.alpine.sophus.crv.spline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.InterpolatingPolynomial;

public class MonomialExtrapolationMaskTest {
  @Test
  public void testSimple() {
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(1), Tensors.vector(1));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(2), Tensors.vector(-1, 2));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(3), Tensors.vector(1, -3, 3));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(4), Tensors.vector(-1, 4, -6, 4));
  }

  @Test
  public void testInterp() {
    InterpolatingPolynomial interpolatingPolynomial = InterpolatingPolynomial.of(Tensors.vector(3, 4, 5, 6));
    Tensor values = Tensors.vector(4, 2, 7, -1);
    ScalarUnaryOperator scalarUnaryOperator = interpolatingPolynomial.scalarUnaryOperator(values);
    Scalar scalar = scalarUnaryOperator.apply(RealScalar.of(7));
    Tensor mask = MonomialExtrapolationMask.INSTANCE.apply(4);
    Scalar altcom = (Scalar) values.dot(mask);
    ExactScalarQ.require(altcom);
    ExactScalarQ.require(scalar);
    assertEquals(scalar, altcom);
  }
}
