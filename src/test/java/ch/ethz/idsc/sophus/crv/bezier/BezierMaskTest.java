// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Total;
import junit.framework.TestCase;

public class BezierMaskTest extends TestCase {
  public void testSimple2() {
    assertEquals(BezierMask.of(RationalScalar.HALF, 2), Tensors.fromString("{1/2, 1/2}"));
    assertEquals(BezierMask.of(RealScalar.of(0), 2), Tensors.fromString("{1, 0}"));
    assertEquals(BezierMask.of(RealScalar.of(1), 2), Tensors.fromString("{0, 1}"));
  }

  public void testSimple3() {
    assertEquals(BezierMask.of(RationalScalar.HALF, 3), Tensors.fromString("{1/4, 1/2, 1/4}"));
    assertEquals(BezierMask.of(RealScalar.of(0), 3), Tensors.fromString("{1, 0, 0}"));
    assertEquals(BezierMask.of(RealScalar.of(1), 3), Tensors.fromString("{0, 0, 1}"));
  }

  public void testFunctionMatch() {
    int n = 5;
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(RnGeodesic.INSTANCE, IdentityMatrix.of(n));
    Scalar p = RationalScalar.of(2, 7);
    Tensor vector = scalarTensorFunction.apply(p);
    Tensor weight = BezierMask.of(p, n);
    ExactTensorQ.require(weight);
    ExactTensorQ.require(vector);
    assertEquals(weight, vector);
  }

  public void testExtrapolate() {
    assertEquals(BezierMask.extrapolate(2), Tensors.fromString("{-1, 2}"));
    assertEquals(BezierMask.extrapolate(3), Tensors.fromString("{1/4, -3/2, 9/4}"));
    assertEquals(BezierMask.extrapolate(4), Tensors.fromString("{-1/27, 4/9, -16/9, 64/27}"));
    for (int n = 2; n < 10; ++n) {
      Tensor mask = BezierMask.extrapolate(n);
      assertEquals(Total.of(mask), RealScalar.ONE);
      ExactTensorQ.require(mask);
    }
  }
}
