// code by jph
package ch.alpine.sophus.hs.rpn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;

class RpnRandomSampleTest {
  @Test
  void test0() {
    RandomSampleInterface randomSampleInterface = RpnRandomSample.of(0);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    assertEquals(tensor, Tensors.vector(1));
  }

  @Test
  void test2() {
    RandomSampleInterface randomSampleInterface = RpnRandomSample.of(2);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    VectorQ.requireLength(tensor, 3);
  }
}
