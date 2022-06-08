// code by jph
package ch.alpine.sophus.lie.se2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;

class Se2RandomSampleTest {
  @Test
  public void testSimple() {
    Tensor tensor = RandomSample.of(Se2RandomSample.of(ExponentialDistribution.standard()));
    VectorQ.requireLength(tensor, 3);
  }
}
