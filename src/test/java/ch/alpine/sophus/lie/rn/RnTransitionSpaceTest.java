// code by jph, gjoel
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.Transition;
import ch.alpine.sophus.crv.TransitionWrap;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;

class RnTransitionSpaceTest {
  @Test
  void testLength() throws ClassNotFoundException, IOException {
    Tensor start = Tensors.fromString("{1[m], 2[m]}");
    Tensor end = Tensors.fromString("{1[m], 6[m]}");
    Transition transition = Serialization.copy(RnTransitionSpace.INSTANCE).connect(start, end);
    assertEquals(Quantity.of(4, "m"), transition.length());
    ExactScalarQ.require(transition.length());
    assertEquals(start, transition.start());
    assertEquals(end, transition.end());
  }

  @Test
  void testSamples() {
    Tensor start = Tensors.fromString("{1[m], 2[m]}");
    Tensor end = Tensors.fromString("{1[m], 6[m]}");
    Transition transition = RnTransitionSpace.INSTANCE.connect(start, end);
    {
      Scalar res = Quantity.of(0.5, "m");
      Tensor samples = transition.sampled(res);
      ExactTensorQ.require(samples);
      assertEquals(8, samples.length());
      assertNotSame(start, samples.get(0));
      assertEquals(end, Last.of(samples));
    }
    // {
    // Tensor samples = transition.sampled(8);
    // assertEquals(8, samples.length());
    // assertNotSame(start, samples.get(0));
    // assertEquals(end, Last.of(samples));
    // }
  }

  @Test
  void testWrap() {
    Tensor start = Tensors.fromString("{1[m], 2[m]}");
    Tensor end = Tensors.fromString("{1[m], 6[m]}");
    Transition transition = RnTransitionSpace.INSTANCE.connect(start, end);
    {
      Scalar res = Quantity.of(0.5, "m");
      TransitionWrap wrap = transition.wrapped(res);
      assertEquals(8, wrap.samples().length());
      assertNotSame(start, wrap.samples().get(0));
      assertEquals(end, Last.of(wrap.samples()));
      wrap.spacing().forEach(s -> assertEquals(res, s));
    }
    // {
    // TransitionWrap wrap = transition.wrapped(8);
    // assertEquals(8, wrap.samples().length());
    // assertNotSame(start, wrap.samples().get(0));
    // assertEquals(end, Last.of(wrap.samples()));
    // wrap.spacing().stream().forEach(s -> assertEquals(transition.length().divide(RealScalar.of(8)), s));
    // }
  }
}
