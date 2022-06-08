// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;

class Regularization2StepCyclicTest {
  private static final TensorUnaryOperator CYCLIC = //
      Regularization2Step.cyclic(RnGroup.INSTANCE, RationalScalar.of(1, 4));

  @Test
  public void testLo() {
    Tensor signal = Tensors.vector(1, 0, 0, 0, 0);
    Tensor tensor = CYCLIC.apply(signal);
    assertEquals(tensor, Tensors.fromString("{3/4, 1/8, 0, 0, 1/8}"));
    TensorUnaryOperator regularization2StepCyclic = Regularization2Step.cyclic(RnGroup.INSTANCE, RealScalar.of(0.25));
    assertEquals(tensor, regularization2StepCyclic.apply(signal));
  }

  @Test
  public void testHi() {
    Tensor signal = Tensors.vector(0, 0, 0, 0, 1);
    Tensor tensor = CYCLIC.apply(signal);
    assertEquals(tensor, Tensors.fromString("{1/8, 0, 0, 1/8, 3/4}"));
    TensorUnaryOperator tensorUnaryOperator = Regularization2Step.cyclic(RnGroup.INSTANCE, RealScalar.of(0.25));
    assertEquals(tensor, tensorUnaryOperator.apply(signal));
  }

  @Test
  public void testEmpty() {
    assertEquals(CYCLIC.apply(Tensors.empty()), Tensors.empty());
  }

  @Test
  public void testSingle() {
    assertEquals(CYCLIC.apply(Tensors.vector(3)), Tensors.vector(3));
  }

  @Test
  public void testTuple() {
    Tensor tensor = CYCLIC.apply(Tensors.vector(3, 2));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{11/4, 9/4}"));
  }

  @Test
  public void testZero() {
    Tensor signal = Tensors.vector(1, 1, 1, 2, 1, 1, 3, 1, 1, 1);
    Tensor tensor = Regularization2Step.cyclic(RnGroup.INSTANCE, RealScalar.ZERO).apply(signal);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, signal);
  }

  @Test
  public void testScalarFail() {
    TensorUnaryOperator tensorUnaryOperator = Regularization2Step.cyclic(RnGroup.INSTANCE, RationalScalar.HALF);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(RealScalar.ZERO));
  }
}
