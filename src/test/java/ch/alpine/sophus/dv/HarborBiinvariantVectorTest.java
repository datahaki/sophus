// code by jph
package ch.alpine.sophus.dv;

import java.util.Random;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.BiinvariantVector;
import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class HarborBiinvariantVectorTest extends TestCase {
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction norm2(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new InfluenceBiinvariantVector(vectorLogManifold, sequence, (x, y) -> Matrix2Norm.of(x.subtract(y)));
  }

  public void testRn() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    int length = 4 + random.nextInt(6);
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor point = RandomVariate.of(distribution, 3);
    BiinvariantVectorFunction d1 = HarborBiinvariantVector.of(vectorLogManifold, sequence);
    BiinvariantVectorFunction d2 = norm2(vectorLogManifold, sequence);
    BiinvariantVectorFunction d3 = CupolaBiinvariantVector.of(vectorLogManifold, sequence);
    BiinvariantVector v1 = d1.biinvariantVector(point);
    BiinvariantVector v2 = d2.biinvariantVector(point);
    BiinvariantVector v3 = d3.biinvariantVector(point);
    Chop._10.requireClose(v1.weighting(s -> s), v2.weighting(s -> s));
    assertEquals(v1.vector().length(), v3.vector().length());
  }

  public void testSe2C() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    int length = 4 + random.nextInt(4);
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor point = RandomVariate.of(distribution, 3);
    BiinvariantVectorFunction d1 = HarborBiinvariantVector.of(vectorLogManifold, sequence);
    BiinvariantVectorFunction d2 = norm2(vectorLogManifold, sequence);
    BiinvariantVectorFunction d3 = CupolaBiinvariantVector.of(vectorLogManifold, sequence);
    BiinvariantVector v1 = d1.biinvariantVector(point);
    BiinvariantVector v2 = d2.biinvariantVector(point);
    BiinvariantVector v3 = d3.biinvariantVector(point);
    assertEquals(v1.vector().length(), v2.vector().length());
    assertEquals(v1.vector().length(), v3.vector().length());
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final Biinvariant[] BIINVARIANT = //
      { Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR };

  public void testRandom() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    for (Biinvariant biinvariant : BIINVARIANT)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor distances = biinvariant.distances(vectorLogManifold, points).apply(xya);
        Tensor shift = RandomVariate.of(distribution, 3);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
          Chop._05.requireClose(distances, //
              biinvariant.distances(vectorLogManifold, tensorMapping.slash(points)).apply(tensorMapping.apply(xya)));
      }
  }
}
