// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class ArcLengthParameterizationTest extends TestCase {
  public void testSimpleR2String() {
    ScalarTensorFunction scalarTensorFunction = ArcLengthParameterization.of(Tensors.vector(1, 1, 1, 1), RnGeodesic.INSTANCE, Range.of(0, 5));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RealScalar.ZERO)), RealScalar.of(0));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RationalScalar.HALF)), RealScalar.of(2));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RealScalar.ONE)), RealScalar.of(4));
    AssertFail.of(() -> scalarTensorFunction.apply(RealScalar.of(-0.1)));
    AssertFail.of(() -> scalarTensorFunction.apply(RealScalar.of(1.1)));
  }

  public void testFailLength() {
    AssertFail.of(() -> ArcLengthParameterization.of(Tensors.vector(1, 2, 3), RnGeodesic.INSTANCE, Tensors.vector(1, 2, 3)));
  }

  public void testFailNonVector() {
    AssertFail.of(() -> ArcLengthParameterization.of(HilbertMatrix.of(3), RnGeodesic.INSTANCE, Tensors.vector(1, 2, 3, 4)));
  }

  public void testFailNull() {
    AssertFail.of(() -> ArcLengthParameterization.of(Tensors.vector(1, 2, 3), null, Tensors.vector(1, 2, 3, 4)));
  }
}
