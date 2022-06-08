// code by jph
package ch.alpine.sophus.lie.se2c;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class Se2CoveringRandomSampleTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = //
        Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));
    Serialization.copy(randomSampleInterface);
    VectorQ.requireLength(RandomSample.of(randomSampleInterface), 3);
  }
}
