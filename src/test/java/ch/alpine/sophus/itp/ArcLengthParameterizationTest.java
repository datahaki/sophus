// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.HilbertMatrix;

class ArcLengthParameterizationTest {
  @Test
  void testSimpleR2String() {
    ScalarTensorFunction scalarTensorFunction = ArcLengthParameterization.of(Tensors.vector(1, 1, 1, 1), RnGroup.INSTANCE, Range.of(0, 5));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RealScalar.ZERO)), RealScalar.of(0));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RationalScalar.HALF)), RealScalar.of(2));
    assertEquals(ExactTensorQ.require(scalarTensorFunction.apply(RealScalar.ONE)), RealScalar.of(4));
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(RealScalar.of(-0.1)));
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(RealScalar.of(1.1)));
  }

  @Test
  void testFailLength() {
    assertThrows(Exception.class, () -> ArcLengthParameterization.of(Tensors.vector(1, 2, 3), RnGroup.INSTANCE, Tensors.vector(1, 2, 3)));
  }

  @Test
  void testFailNonVector() {
    assertThrows(Exception.class, () -> ArcLengthParameterization.of(HilbertMatrix.of(3), RnGroup.INSTANCE, Tensors.vector(1, 2, 3, 4)));
  }

  @Test
  void testFailNull() {
    assertThrows(Exception.class, () -> ArcLengthParameterization.of(Tensors.vector(1, 2, 3), null, Tensors.vector(1, 2, 3, 4)));
  }
}
