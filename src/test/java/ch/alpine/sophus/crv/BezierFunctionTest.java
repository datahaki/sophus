// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.sca.Chop;

class BezierFunctionTest {
  @Test
  public void testSimple() {
    Tensor control = Tensors.fromString("{{0, 1}, {1, 0}, {2, 1}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(RnGroup.INSTANCE, control);
    Tensor tensor = scalarTensorFunction.apply(RationalScalar.of(1, 4));
    assertEquals(tensor, Tensors.fromString("{1/2, 5/8}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  public void testRn() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}, {3, 1}}");
    ScalarTensorFunction stf1 = BezierFunction.of(RnGroup.INSTANCE, control);
    Scalar scalar = RationalScalar.of(1, 4);
    Tensor tensor = stf1.apply(scalar);
    assertEquals(tensor, Tensors.fromString("{3/4, 7/16}"));
    ExactTensorQ.require(tensor);
    Tensor domain = Subdivide.of(0, 1, 7);
    ScalarTensorFunction stf2 = BezierFunction.of(RnBiinvariantMean.INSTANCE, control);
    assertEquals(domain.map(stf1), domain.map(stf2));
  }

  @Test
  public void testSe2Covering() {
    Tensor control = Tensors.fromString("{{0, 0, 0}, {1, 0, 1/2}, {2, 0.4, 2/5}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGroup.INSTANCE, control);
    Scalar scalar = RationalScalar.of(1, 4);
    Tensor tensor = scalarTensorFunction.apply(scalar);
    assertEquals(tensor.Get(2), RationalScalar.of(17, 80));
    ExactScalarQ.require(tensor.Get(2));
  }

  @Test
  public void testOutsideFail() {
    Tensor control = Tensors.fromString("{{0, 0, 0}, {1, 0, 1/2}, {2, 0.4, 2/5}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGroup.INSTANCE, control);
    Scalar scalar = RationalScalar.of(-1, 4);
    Tensor tensor = scalarTensorFunction.apply(scalar);
    Chop._12.requireClose(tensor, Tensors.vector(-0.45359613406197646, 0.22282532025418184, -23 / 80.));
    ExactScalarQ.require(tensor.Get(2));
  }

  @Test
  public void testSingleton() {
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGroup.INSTANCE, Array.zeros(1, 3));
    for (Tensor _x : Subdivide.of(-1, 2, 3 * 3)) {
      Tensor tensor = scalarTensorFunction.apply((Scalar) _x);
      assertEquals(tensor, Array.zeros(3));
      ExactTensorQ.require(tensor);
    }
  }

  @Test
  public void testFailEmpty() {
    assertThrows(Exception.class, () -> BezierFunction.of(Se2CoveringGroup.INSTANCE, Tensors.empty()));
    assertThrows(Exception.class, () -> BezierFunction.of(RnBiinvariantMean.INSTANCE, Tensors.empty()));
  }

  @Test
  public void testFailScalar() {
    assertThrows(Exception.class, () -> BezierFunction.of(Se2CoveringGroup.INSTANCE, RealScalar.ZERO));
  }

  @Test
  public void testFailNull() {
    assertThrows(Exception.class, () -> BezierFunction.of((BiinvariantMean) null, Tensors.vector(1, 2, 3)));
    assertThrows(Exception.class, () -> BezierFunction.of((BinaryAverage) null, Tensors.vector(1, 2, 3)));
  }
}
