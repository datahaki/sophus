// code by jph
package ch.ethz.idsc.sophus.decim;

import java.io.IOException;

import ch.ethz.idsc.sophus.decim.HsLineDistance.NormImpl;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.num.Pi;
import junit.framework.TestCase;

public class HsLineDistanceTest extends TestCase {
  public void testRnMatch() throws ClassNotFoundException, IOException {
    HsLineDistance hsLineDistance = //
        Serialization.copy(new HsLineDistance(RnManifold.INSTANCE));
    NormImpl tensorNorm = //
        Serialization.copy(hsLineDistance.tensorNorm(Tensors.vector(1, 2), Tensors.vector(10, 2)));
    assertEquals(tensorNorm.norm(Tensors.vector(5, 2)), RealScalar.ZERO);
    assertEquals(tensorNorm.norm(Tensors.vector(5, 3)), RealScalar.ONE);
  }

  public void testSnMatch() throws ClassNotFoundException, IOException {
    HsLineDistance hsLineDistance = //
        Serialization.copy(new HsLineDistance(SnManifold.INSTANCE));
    Tensor p = UnitVector.of(3, 0);
    Tensor q = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 0));
    NormImpl normImpl = hsLineDistance.tensorNorm(p, q);
    Tolerance.CHOP.requireClose(normImpl.norm(UnitVector.of(3, 2)), Pi.HALF);
    Tolerance.CHOP.requireClose(normImpl.norm(UnitVector.of(3, 2).negate()), Pi.HALF);
  }
}
