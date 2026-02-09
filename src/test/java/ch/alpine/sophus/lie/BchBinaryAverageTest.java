// code by jph
package ch.alpine.sophus.lie;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.s.SnManifold;
import ch.alpine.sophus.hs.s.TSnMemberQ;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.so.So3Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.Chop;

class BchBinaryAverageTest {
  @Test
  void testSe2() {
    Exponential exponential = Se2CoveringGroup.INSTANCE.exponential0();
    Tensor x = Tensors.vector(0.1, 0.2, 0.1);
    Tensor y = Tensors.vector(0.2, -0.1, 0.2);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Scalar lambda = RealScalar.of(0.3);
    GeodesicSpace hsGeodesic = Se2CoveringGroup.INSTANCE;
    Tensor res = exponential.log(hsGeodesic.split(mX, mY, lambda));
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(LieAlgebraAds.se(2), 6);
    Tensor cmp = new BchBinaryAverage(bch).split(x, y, lambda);
    Chop._08.requireClose(res, cmp);
  }

  @Test
  void testS2() {
    // TODO SOPHUS BCH test seems to be deactivated
    Tensor x = Tensors.vector(0.30, +0.15, 0);
    Tensor y = Tensors.vector(0.05, -0.35, 0);
    Tensor n = UnitVector.of(3, 2);
    Exponential exponential = SnManifold.INSTANCE.exponential(n);
    Tensor mx = exponential.exp(x);
    Tensor my = exponential.exp(y);
    SnManifold.INSTANCE.requireMember(mx);
    SnManifold.INSTANCE.requireMember(my);
    TSnMemberQ tSnMemberQ = new TSnMemberQ(n);
    tSnMemberQ.requireMember(x);
    tSnMemberQ.requireMember(y);
    Scalar lambda = RealScalar.of(0.3);
    GeodesicSpace hsGeodesic = SnManifold.INSTANCE;
    Tensor mz = exponential.log(hsGeodesic.split(mx, my, lambda));
    mz.maps(Scalar::zero);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    for (int d = 1; d < 7; ++d) {
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(matrixAlgebra.ad(), d);
      Tensor cmp = new BchBinaryAverage(bch).split(x, y, lambda);
      cmp.maps(Scalar::zero);
    }
  }
}
