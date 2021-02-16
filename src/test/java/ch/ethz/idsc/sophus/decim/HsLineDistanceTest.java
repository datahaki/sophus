// code by jph
package ch.ethz.idsc.sophus.decim;

import java.io.IOException;

import ch.ethz.idsc.sophus.decim.HsLineDistance.NormImpl;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class HsLineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    HsLineDistance hsLineDistance = //
        Serialization.copy(new HsLineDistance(RnManifold.INSTANCE));
    NormImpl tensorNorm = //
        Serialization.copy(hsLineDistance.tensorNorm(Tensors.vector(1, 2), Tensors.vector(10, 2)));
    assertEquals(tensorNorm.norm(Tensors.vector(5, 2)), RealScalar.ZERO);
    assertEquals(tensorNorm.norm(Tensors.vector(5, 3)), RealScalar.ONE);
  }
}
