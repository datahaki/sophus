// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.LieDifferences;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2DifferencesTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    Tensor tensor = Serialization.copy(Se2Differences.INSTANCE).apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, 0));
  }

  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomSample.of(Se2RandomSample.of(distribution), 10);
    LieDifferences lieDifferences = Se2Differences.INSTANCE;
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 3));
  }
}
