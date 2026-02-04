// code by jph
package ch.alpine.sophus.math.sample;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class DnRandomSampleTest {
  @Test
  void test() throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = DnRandomSample.of(UniformDistribution.unit());
    Serialization.copy(randomSampleInterface);
  }
}
