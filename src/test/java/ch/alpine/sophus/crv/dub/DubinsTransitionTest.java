// code by jph
package ch.alpine.sophus.crv.dub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.Transition;
import ch.alpine.sophus.crv.TransitionSpace;
import ch.alpine.sophus.crv.TransitionWrap;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class DubinsTransitionTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    TransitionSpace transitionSpace = Serialization.copy(DubinsTransitionSpace.of(RealScalar.of(2), DubinsPathComparators.LENGTH));
    Tensor start = Tensors.vector(1, 2, 3);
    Tensor end = Tensors.vector(3, -8, 1);
    Transition transition = transitionSpace.connect(start, end);
    TransitionWrap transitionWrap = transition.wrapped(RealScalar.of(0.3));
    assertEquals(transitionWrap.samples().length(), transitionWrap.spacing().length());
    assertTrue(transitionWrap.spacing().stream().map(Scalar.class::cast).allMatch(Sign::isPositive));
  }

  @Test
  void testTrivial() {
    TransitionSpace transitionSpace = DubinsTransitionSpace.of(RealScalar.of(1), DubinsPathComparators.LENGTH);
    Tensor start = Tensors.vector(0, 0, 0);
    Tensor end = Tensors.vector(4, 0, 0);
    Transition transition = transitionSpace.connect(start, end);
    Tensor sampled = transition.sampled(RealScalar.of(2));
    Chop._12.requireClose(sampled, Tensors.fromString("{{2, 0, 0}, {4, 0, 0}}"));
  }

  @Test
  void testTrivial2() {
    TransitionSpace transitionSpace = DubinsTransitionSpace.of(RealScalar.of(1), DubinsPathComparators.LENGTH);
    Tensor start = Tensors.vector(0, 0, 0);
    Tensor end = Tensors.vector(4, 0, 0);
    Transition transition = transitionSpace.connect(start, end);
    Tensor sampled = transition.sampled(RealScalar.of(1.9));
    Chop._12.requireClose(sampled, Tensors.fromString("{{4/3, 0, 0}, {8/3, 0, 0}, {4, 0, 0}}"));
  }
}
