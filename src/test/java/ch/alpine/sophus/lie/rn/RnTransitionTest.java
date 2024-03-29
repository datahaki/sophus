// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.TransitionWrap;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;

class RnTransitionTest {
  @Test
  void testSampled() throws ClassNotFoundException, IOException {
    Tensor start = Tensors.vector(1, 2);
    Tensor end = Tensors.vector(10, 2);
    RnTransition rnTransition = //
        Serialization.copy(RnTransitionSpace.INSTANCE.connect(start, end));
    Tensor tensor = rnTransition.sampled(RealScalar.of(0.1));
    assertEquals(tensor.length(), 90);
  }

  @Test
  void testLinearized() {
    Tensor start = Tensors.vector(1, 2);
    Tensor end = Tensors.vector(10, 2);
    RnTransition rnTransition = RnTransitionSpace.INSTANCE.connect(start, end);
    Tensor linearized = rnTransition.linearized(RealScalar.of(0.1));
    assertEquals(linearized, Tensors.fromString("{{1, 2}, {10, 2}}"));
  }

  @Test
  void testWrapped() {
    Tensor start = Tensors.vector(1, 2);
    Tensor end = Tensors.vector(10, 2);
    RnTransition rnTransition = RnTransitionSpace.INSTANCE.connect(start, end);
    TransitionWrap transitionWrap = rnTransition.wrapped(RealScalar.of(0.1));
    Tensor samples = transitionWrap.samples();
    Tensor spacing = transitionWrap.spacing();
    assertEquals(samples.length(), spacing.length());
    ExactTensorQ.of(spacing);
    Tensor diffnor = Tensor.of(Differences.of(samples).stream().map(Vector2Norm::of));
    assertEquals(spacing.extract(0, diffnor.length()), diffnor);
  }

  @Test
  void testFail() {
    Tensor start = Tensors.vector(1, 2);
    Tensor end = Tensors.vector(10, 2);
    RnTransition rnTransition = RnTransitionSpace.INSTANCE.connect(start, end);
    rnTransition.sampled(RealScalar.of(100));
    assertThrows(Exception.class, () -> rnTransition.sampled(RealScalar.ZERO));
    assertThrows(Exception.class, () -> rnTransition.sampled(RealScalar.of(-0.1)));
  }
}
