// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class MonomialExtrapolationMaskTest extends TestCase {
  public void testSimple() {
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(1), Tensors.vector(1));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(2), Tensors.vector(-1, 2));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(3), Tensors.vector(1, -3, 3));
    assertEquals(MonomialExtrapolationMask.INSTANCE.apply(4), Tensors.vector(-1, 4, -6, 4));
  }

  public void testInterp() {
    InterpolatingPolynomial interpolatingPolynomial = InterpolatingPolynomial.of(Tensors.vector(3, 4, 5, 6));
    Tensor values = Tensors.vector(4, 2, 7, -1);
    ScalarUnaryOperator scalarUnaryOperator = interpolatingPolynomial.scalarUnaryOperator(values);
    Scalar scalar = scalarUnaryOperator.apply(RealScalar.of(7));
    Tensor mask = MonomialExtrapolationMask.INSTANCE.apply(4);
    Scalar altcom = values.dot(mask).Get();
    ExactScalarQ.require(altcom);
    ExactScalarQ.require(scalar);
    assertEquals(scalar, altcom);
  }
}
