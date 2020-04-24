// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
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

public class KrigingTest extends TestCase {
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
      PseudoDistances pseudoDistances = FlattenLogWarp.RELATIVE.pseudoDistances(Se2CoveringManifold.INSTANCE, powerVariogram, points);
      Kriging kriging1 = Krigings.regression( //
          pseudoDistances, //
          points, values, covariance);
      Tensor est1 = kriging1.estimate(xya);
      Scalar var1 = kriging1.variance(xya);
      Tensor shift = RandomVariate.of(distribution, 3);
      { // invariant under left action
        Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
        PseudoDistances pseudoDL = FlattenLogWarp.RELATIVE.pseudoDistances(Se2CoveringManifold.INSTANCE, powerVariogram, seqlft);
        Kriging krigingL = Krigings.regression( //
            pseudoDL, //
            seqlft, values, covariance);
        Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
        Chop._10.requireClose(est1, krigingL.estimate(xyalft));
        Chop._10.requireClose(var1, krigingL.variance(xyalft));
      }
      { // invariant under right action
        Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
        PseudoDistances pseudoDR = FlattenLogWarp.RELATIVE.pseudoDistances(Se2CoveringManifold.INSTANCE, powerVariogram, seqrgt);
        Kriging krigingR = Krigings.regression( //
            pseudoDR, //
            seqrgt, values, covariance);
        Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
        Chop._10.requireClose(est1, krigingR.estimate(xyargt));
        Chop._10.requireClose(var1, krigingR.variance(xyargt));
      }
      { // invariant under inversion
        Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
        PseudoDistances pseudoDI = FlattenLogWarp.RELATIVE.pseudoDistances(Se2CoveringManifold.INSTANCE, powerVariogram, seqinv);
        Kriging krigingI = Krigings.regression( //
            pseudoDI, //
            seqinv, values, covariance);
        Tensor xyainv = LIE_GROUP_OPS.invert(xya);
        Chop._10.requireClose(est1, krigingI.estimate(xyainv));
        Chop._10.requireClose(var1, krigingI.variance(xyainv));
      }
    }
  }
}
