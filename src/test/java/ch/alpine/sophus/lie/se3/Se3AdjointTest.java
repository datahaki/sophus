// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class Se3AdjointTest {
  @Test
  void testForwardInverse() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor g = Se3Matrix.of(Rodrigues.vectorExp(RandomVariate.of(distribution, 3)), RandomVariate.of(distribution, 3));
      TensorUnaryOperator se3Adjoint = Se3Adjoint.forward(g);
      Tensor u_w = RandomVariate.of(distribution, 2, 3);
      Tensor out = se3Adjoint.apply(u_w);
      assertEquals(Dimensions.of(out), Arrays.asList(2, 3));
      TensorUnaryOperator se3Inverse = Se3Adjoint.inverse(g);
      Tensor apply = se3Inverse.apply(out);
      Chop._11.requireClose(u_w, apply);
    }
  }

  @Test
  void testForwardInsteadInverse() {
    TensorUnaryOperator se3Adjoint = new Se3Adjoint(IdentityMatrix.of(3), Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = se3Adjoint.apply(Tensors.of(Tensors.vector(1, 0, 1), Tensors.vector(1, 0, 1))); // more forward and turn left
    assertEquals(tensor, Tensors.fromString("{{2, 0, 0}, {1, 0, 1}}")); // only rotation
    ExactTensorQ.require(tensor);
  }

  @Test
  void testRotationSideLeft() {
    TensorUnaryOperator se3Adjoint = new Se3Adjoint(IdentityMatrix.of(3), Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = se3Adjoint.apply(Tensors.of(Tensors.vector(1, 0, 0), Tensors.vector(0, 0, -1))); // more forward and turn right
    assertEquals(tensor, Tensors.fromString("{{0, 0, 0}, {0, 0, -1}}")); // only rotation
    ExactTensorQ.require(tensor);
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> Se3Adjoint.forward(Tensors.vector(1, 2, 3, 4)));
    assertThrows(Exception.class, () -> Se3Adjoint.forward(HilbertMatrix.of(4, 3)));
  }
}
