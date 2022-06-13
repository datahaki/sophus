// code by jph
package ch.alpine.sophus.hs;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class MetricBiinvariantTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(MetricBiinvariant.EUCLIDEAN);
  }
}
