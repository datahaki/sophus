// code by jph
package ch.alpine.sophus.bm;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.lie.so3.So3Exponential;
import ch.alpine.sophus.lie.so3.So3Manifold;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import junit.framework.TestCase;

public class BchBinaryAverageTest extends TestCase {
  private static final BinaryOperator<Tensor> BCH_SE2 = BakerCampbellHausdorff.of(N.DOUBLE.of(Tensors.fromString( //
      "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}")), 6);
  private static final BinaryOperator<Tensor> BCH_SO3 = BakerCampbellHausdorff.of(N.DOUBLE.of(LeviCivitaTensor.of(3).negate()), 6);

  public void testSe2() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.1);
    Tensor y = Tensors.vector(0.2, -0.1, 0.2);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Scalar lambda = RealScalar.of(0.3);
    HsGeodesic hsGeodesic = new HsGeodesic(Se2CoveringManifold.INSTANCE);
    Tensor res = exponential.log(hsGeodesic.split(mX, mY, lambda));
    Tensor cmp = BchBinaryAverage.of(BCH_SE2).split(x, y, lambda);
    Chop._08.requireClose(res, cmp);
  }

  public void testSo3() {
    Exponential exponential = So3Exponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Scalar lambda = RealScalar.of(0.3);
    HsGeodesic hsGeodesic = new HsGeodesic(So3Manifold.INSTANCE);
    Tensor res = exponential.log(hsGeodesic.split(mX, mY, lambda));
    Tensor cmp = BchBinaryAverage.of(BCH_SO3).split(x, y, lambda);
    Chop._08.requireClose(res, cmp);
  }
}
