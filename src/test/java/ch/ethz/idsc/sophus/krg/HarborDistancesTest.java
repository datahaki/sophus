// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class HarborDistancesTest extends TestCase {
  public void testRn() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      HarborDistances d1 = HarborDistances.frobenius(vectorLogManifold, sequence);
      HarborDistances d2 = HarborDistances.norm2(vectorLogManifold, sequence);
      BiinvariantVector v1 = d1.biinvariantVector(point);
      BiinvariantVector v2 = d2.biinvariantVector(point);
      Chop._10.requireClose(v1.weighting(s -> s), v2.weighting(s -> s));
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      HarborDistances d1 = HarborDistances.frobenius(vectorLogManifold, sequence);
      HarborDistances d2 = HarborDistances.norm2(vectorLogManifold, sequence);
      BiinvariantVector v1 = d1.biinvariantVector(point);
      BiinvariantVector v2 = d2.biinvariantVector(point);
      assertEquals(v1.distances().length(), v2.distances().length());
      // Chop._10.requireClose(v1.coordinate(), v2.coordinate());
    }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final Biinvariant[] BIINVARIANT = { Biinvariants.TARGET, Biinvariants.HARBOR, Biinvariants.GARDEN };

  public void testRandom() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    for (Biinvariant biinvariant : BIINVARIANT)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor shift = RandomVariate.of(distribution, 3);
        Tensor x_lft;
        { // invariant under left action
          Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
          Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
          x_lft = biinvariant.distances(vectorLogManifold, seqlft).apply(xyalft);
        }
        { // result invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor x_rgt = biinvariant.distances(vectorLogManifold, seqrgt).apply(xyargt);
          Chop._05.requireClose(x_lft, x_rgt);
        }
        { // result invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor x_rgt = biinvariant.distances(vectorLogManifold, seqinv).apply(xyainv);
          Chop._05.requireClose(x_lft, x_rgt);
        }
      }
  }
}
