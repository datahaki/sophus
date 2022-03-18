// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnMemberQ;
import ch.alpine.sophus.hs.sn.TSnMemberQ;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.lie.so3.So3Exponential;
import ch.alpine.sophus.lie.so3.So3Manifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.sca.Chop;

public class BchBinaryAverageTest {
  @Test
  public void testSe2() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.1);
    Tensor y = Tensors.vector(0.2, -0.1, 0.2);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Scalar lambda = RealScalar.of(0.3);
    HsGeodesic hsGeodesic = new HsGeodesic(Se2CoveringManifold.INSTANCE);
    Tensor res = exponential.log(hsGeodesic.split(mX, mY, lambda));
    Tensor cmp = BchBinaryAverage.of(Se2Algebra.INSTANCE.bch(6)).split(x, y, lambda);
    Chop._08.requireClose(res, cmp);
  }

  @Test
  public void testSo3() {
    Exponential exponential = So3Exponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Scalar lambda = RealScalar.of(0.3);
    HsGeodesic hsGeodesic = new HsGeodesic(So3Manifold.INSTANCE);
    Tensor res = exponential.log(hsGeodesic.split(mX, mY, lambda));
    Tensor cmp = BchBinaryAverage.of(So3Algebra.INSTANCE.bch(6)).split(x, y, lambda);
    Chop._08.requireClose(res, cmp);
  }

  @Test
  public void testS2() {
    // TODO SOPHUS BCH test seems to be deactivated
    Tensor x = Tensors.vector(0.30, +0.15, 0);
    Tensor y = Tensors.vector(0.05, -0.35, 0);
    Tensor n = UnitVector.of(3, 2);
    Exponential exponential = SnManifold.INSTANCE.exponential(n);
    Tensor mx = exponential.exp(x);
    Tensor my = exponential.exp(y);
    SnMemberQ.INSTANCE.require(mx);
    SnMemberQ.INSTANCE.require(my);
    TSnMemberQ tSnMemberQ = new TSnMemberQ(n);
    tSnMemberQ.require(x);
    tSnMemberQ.require(y);
    Scalar lambda = RealScalar.of(0.3);
    HsGeodesic hsGeodesic = new HsGeodesic(SnManifold.INSTANCE);
    Tensor mz = exponential.log(hsGeodesic.split(mx, my, lambda));
    mz.map(Scalar::zero);
    for (int d = 1; d < 7; ++d) {
      BinaryOperator<Tensor> bch = So3Algebra.INSTANCE.bch(d);
      Tensor cmp = BchBinaryAverage.of(bch).split(x, y, lambda);
      cmp.map(Scalar::zero);
      // System.out.println(cmp);
      // System.out.println(mz);
      // System.out.println(Vector2Norm.between(cmp, mz));
      // System.out.println("---");
    }
    // Chop._08.requireClose(mz, cmp);
  }
}
