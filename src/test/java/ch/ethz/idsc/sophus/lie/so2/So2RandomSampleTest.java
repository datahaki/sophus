// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import java.util.Random;

import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class So2RandomSampleTest extends TestCase {
  public void testSimple() {
    Random random = new Random();
    Tensor r1 = So2RandomSample.INSTANCE.randomSample(random);
    Tensor r2 = SonRandomSample.of(2).randomSample(random);
    assertEquals(Dimensions.of(r1), Dimensions.of(r2));
  }
}
