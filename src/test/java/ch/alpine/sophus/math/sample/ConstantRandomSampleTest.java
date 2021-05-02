// code by jph
package ch.alpine.sophus.math.sample;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class ConstantRandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = HilbertMatrix.of(2, 3);
    RandomSampleInterface randomSampleInterface = new ConstantRandomSample(tensor);
    assertEquals(RandomSample.of(randomSampleInterface), tensor);
  }
}
