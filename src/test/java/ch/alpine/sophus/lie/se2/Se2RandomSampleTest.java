// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.pdf.ExponentialDistribution;
import junit.framework.TestCase;

public class Se2RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = RandomSample.of(Se2RandomSample.of(ExponentialDistribution.standard()));
    VectorQ.requireLength(tensor, 3);
  }
}
