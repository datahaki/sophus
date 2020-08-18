// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidDistanceTest extends TestCase {
  public void testSimple() {
    Chop._10.requireClose(ClothoidDistance.SE2.norm(Tensors.vector(10, 0, 0)), RealScalar.of(10));
    Chop._10.requireClose(ClothoidDistance.SE2_COVERING.norm(Tensors.vector(10, 0, 0)), RealScalar.of(10));
    Chop._10.requireClose(ClothoidDistance.SE2.norm(Tensors.vector(23, 0, 0)), RealScalar.of(23));
    Chop._10.requireClose(ClothoidDistance.SE2_COVERING.norm(Tensors.vector(23, 0, 0)), RealScalar.of(23));
  }
}
