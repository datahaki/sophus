// code by jph
package ch.alpine.sophus.decim;

import java.io.IOException;

import ch.alpine.sophus.decim.HsLineDistance.NormImpl;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
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
