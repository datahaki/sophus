// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.api.TensorIteration;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class Hermite3SubdivisionTest {
  @Test
  void testStringLength2() {
    Tensor control = Tensors.fromString("{{3, 4}, {1, -3}}");
    TensorIteration tensorIteration1 = RnHermite3Subdivisions.standard().string(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnGroup.INSTANCE, Tolerance.CHOP) //
            .string(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  @Test
  void testStringReverseRn() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    TensorIteration tensorIteration1 = //
        Hermite3Subdivisions.of(RnGroup.INSTANCE, Tolerance.CHOP) //
            .string(RealScalar.ONE, cp1);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnGroup.INSTANCE, Tolerance.CHOP) //
            .string(RealScalar.ONE, Reverse.of(cp2));
    for (int count = 0; count < 3; ++count) {
      Tensor result1 = tensorIteration1.iterate();
      Tensor result2 = Reverse.of(tensorIteration2.iterate());
      result2.set(Tensor::negate, Tensor.ALL, 1);
      Chop._12.requireClose(result1, result2);
    }
  }

  @Test
  void testCyclic() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    TensorIteration tensorIteration1 = RnHermite3Subdivisions.standard().cyclic(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnGroup.INSTANCE, Tolerance.CHOP) //
            .cyclic(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  @Test
  void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite3Subdivisions.of(RnGroup.INSTANCE, Tolerance.CHOP));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> Hermite3Subdivisions.of(null, Tolerance.CHOP));
    assertThrows(Exception.class, () -> Hermite3Subdivisions.of(Se2CoveringGroup.INSTANCE, null));
  }
}
