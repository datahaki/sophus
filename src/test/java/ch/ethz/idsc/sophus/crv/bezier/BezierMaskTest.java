// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Total;
import junit.framework.TestCase;

public class BezierMaskTest extends TestCase {
  public void testSimple1() {
    assertEquals(BezierMask.of(1).apply(RealScalar.of(0.2)), Tensors.vector(1));
  }

  public void testSimple2() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(BezierMask.of(2));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/2, 1/2}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 1}"));
  }

  public void testSimple3() {
    ScalarTensorFunction scalarTensorFunction = BezierMask.of(3);
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/4, 1/2, 1/4}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 0, 1}"));
  }

  public void testFunctionMatch() {
    int n = 5;
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(RnGeodesic.INSTANCE, IdentityMatrix.of(n));
    Scalar p = RationalScalar.of(2, 7);
    Tensor vector = scalarTensorFunction.apply(p);
    Tensor weight = BezierMask.of(n).apply(p);
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

  public void testNegFail() {
    AssertFail.of(() -> BezierMask.of(0));
    AssertFail.of(() -> BezierMask.of(-1));
  }
}
