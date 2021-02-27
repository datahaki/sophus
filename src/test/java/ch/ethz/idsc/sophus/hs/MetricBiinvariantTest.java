// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class MetricBiinvariantTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(MetricBiinvariant.EUCLIDEAN);
  }
}
