// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantKrigingTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testSimple() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    PowerVariogram powerVariogram = PowerVariogram.of(1, 1.4);
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor values = RandomVariate.of(distributiox, n);
      Tensor covariance = DiagonalMatrix.with(ConstantArray.of(RealScalar.of(0.02), n));
      Kriging kriging1 = BiinvariantKriging.regression( //
          powerVariogram, Se2CoveringBiinvariantCoordinate.INSTANCE, //
          points, values, covariance);
      Tensor est1 = kriging1.estimate(xya);
      Scalar var1 = kriging1.variance(xya);
      Tensor shift = RandomVariate.of(distribution, 3);
      { // invariant under left action
        Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
        Kriging krigingL = BiinvariantKriging.regression( //
            powerVariogram, Se2CoveringBiinvariantCoordinate.INSTANCE, //
            seqlft, values, covariance);
        Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
        Chop._10.requireClose(est1, krigingL.estimate(xyalft));
        Chop._10.requireClose(var1, krigingL.variance(xyalft));
      }
      { // invariant under right action
        Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
        Kriging krigingR = BiinvariantKriging.regression( //
            powerVariogram, Se2CoveringBiinvariantCoordinate.INSTANCE, //
            seqrgt, values, covariance);
        Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
        Chop._10.requireClose(est1, krigingR.estimate(xyargt));
        Chop._10.requireClose(var1, krigingR.variance(xyargt));
      }
      { // invariant under inversion
        Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
        Kriging krigingI = BiinvariantKriging.regression( //
            powerVariogram, Se2CoveringBiinvariantCoordinate.INSTANCE, //
            seqinv, values, covariance);
        Tensor xyainv = LIE_GROUP_OPS.invert(xya);
        Chop._10.requireClose(est1, krigingI.estimate(xyainv));
        Chop._10.requireClose(var1, krigingI.variance(xyainv));
      }
    }
  }
}
