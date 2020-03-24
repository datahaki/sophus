// code by jph
package ch.ethz.idsc.sophus.lie.son;

import java.io.IOException;
import java.util.Random;

import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SonRandomSampleTest extends TestCase {
  private static final Random RANDOM = new Random();

  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(SonRandomSample.of(n));
      for (int count = 0; count < 10; ++count) {
        Tensor tensor = randomSampleInterface.randomSample(RANDOM);
        Tolerance.CHOP.requireClose(Det.of(tensor), RealScalar.ONE);
      }
    }
  }
}
