// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.hn.HnWeierstrassCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class H2TangentSpaceTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 2));
    TangentSpace tangentSpace = new H2TangentSpace(x);
    Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 2));
    Tensor log = tangentSpace.vectorLog(y);
    assertEquals(log.length(), 2);
  }
}
