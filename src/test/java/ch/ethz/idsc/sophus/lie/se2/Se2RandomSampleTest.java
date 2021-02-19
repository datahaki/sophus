// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import junit.framework.TestCase;

public class Se2RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = RandomSample.of(Se2RandomSample.of(ExponentialDistribution.standard()));
    VectorQ.requireLength(tensor, 3);
  }
}
