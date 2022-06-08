// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieDifferences;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class Se2DifferencesTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    Tensor tensor = Serialization.copy(Se2Differences.INSTANCE).apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, 0));
  }

  @Test
  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomSample.of(Se2RandomSample.of(distribution), 10);
    LieDifferences lieDifferences = Se2Differences.INSTANCE;
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 3));
  }
}
