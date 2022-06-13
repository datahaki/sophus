// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;

class Regularization2StepStringTest {
  private static final TensorUnaryOperator STRING = //
      Regularization2Step.string(RnGroup.INSTANCE, RationalScalar.of(1, 4));

  @Test
  void testLo() throws ClassNotFoundException, IOException {
    Tensor signal = Tensors.vector(1, 0, 0, 0, 0);
    Tensor tensor = Serialization.copy(STRING).apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(1, 0.125, 0, 0, 0));
    TensorUnaryOperator tensorUnaryOperator = Regularization2Step.string(RnGroup.INSTANCE, RealScalar.of(0.25));
    assertEquals(tensor, tensorUnaryOperator.apply(signal));
  }

  @Test
  void testHi() {
    Tensor signal = Tensors.vector(0, 0, 0, 0, 1);
    Tensor tensor = STRING.apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(0, 0, 0, 0.125, 1));
    TensorUnaryOperator tensorUnaryOperator = Regularization2Step.string(RnGroup.INSTANCE, RealScalar.of(0.25));
    assertEquals(tensor, tensorUnaryOperator.apply(signal));
  }

  @Test
  void testEmpty() {
    assertEquals(STRING.apply(Tensors.empty()), Tensors.empty());
  }

  @Test
  void testSingle() {
    assertEquals(STRING.apply(Tensors.vector(2)), Tensors.vector(2));
  }

  @Test
  void testTuple() {
    assertEquals(STRING.apply(Tensors.vector(3, 2)), Tensors.vector(3, 2));
  }

  @Test
  void testSimple() {
    TensorUnaryOperator STRING = //
        Regularization2Step.string(RnGroup.INSTANCE, RationalScalar.of(1, 2));
    Tensor signal = Tensors.vector(1, 1, 1, 2, 1, 1, 1, 1, 1, 1);
    Tensor tensor = STRING.apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{1, 1, 5/4, 3/2, 5/4, 1, 1, 1, 1, 1}"));
  }

  @Test
  void testMatrix() {
    TensorUnaryOperator STRING = //
        Regularization2Step.string(RnGroup.INSTANCE, RationalScalar.of(1, 2));
    Tensor signal = Tensors.fromString("{{1, 2}, {2, 2}, {3, 2}, {4, 2}, {3, 3}}");
    Tensor tensor = STRING.apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{{1, 2}, {2, 2}, {3, 2}, {7/2, 9/4}, {3, 3}}"));
  }

  @Test
  void testZero() {
    Tensor signal = Tensors.vector(1, 1, 1, 2, 1, 1, 3, 1, 1, 1);
    Tensor tensor = Regularization2Step.string(RnGroup.INSTANCE, RealScalar.ZERO).apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, signal);
  }

  @Test
  void testScalarFail() {
    TensorUnaryOperator tensorUnaryOperator = Regularization2Step.string(RnGroup.INSTANCE, RationalScalar.HALF);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(RealScalar.ZERO));
  }
}
