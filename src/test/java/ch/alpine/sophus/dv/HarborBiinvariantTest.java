// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2RandomSample;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class HarborBiinvariantTest {
  /** @param manifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction norm2(Manifold manifold, Tensor sequence) {
    return new BiinvariantVectorFunction(new HsDesign(manifold), sequence, (x, y) -> Matrix2Norm.of(x.subtract(y)));
  }

  @Test
  void testRn() {
    RandomGenerator random = new Random();
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold manifold = RnGroup.INSTANCE;
    int length = 4 + random.nextInt(6);
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor point = RandomVariate.of(distribution, 3);
    BiinvariantVectorFunction d1 = new HarborBiinvariant(manifold).biinvariantVectorFunction(sequence);
    BiinvariantVectorFunction d2 = norm2(manifold, sequence);
    BiinvariantVectorFunction d3 = new CupolaBiinvariant(manifold).biinvariantVectorFunction(sequence);
    BiinvariantVector v1 = d1.biinvariantVector(point);
    BiinvariantVector v2 = d2.biinvariantVector(point);
    BiinvariantVector v3 = d3.biinvariantVector(point);
    Chop._10.requireClose(v1.weighting(s -> s), v2.weighting(s -> s));
    assertEquals(v1.vector().length(), v3.vector().length());
  }

  @Test
  void testSe2C() {
    RandomGenerator random = new Random();
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    int length = 4 + random.nextInt(4);
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor point = RandomVariate.of(distribution, 3);
    BiinvariantVectorFunction d1 = new HarborBiinvariant(manifold).biinvariantVectorFunction(sequence);
    BiinvariantVectorFunction d2 = norm2(manifold, sequence);
    BiinvariantVectorFunction d3 = new CupolaBiinvariant(manifold).biinvariantVectorFunction(sequence);
    BiinvariantVector v1 = d1.biinvariantVector(point);
    BiinvariantVector v2 = d2.biinvariantVector(point);
    BiinvariantVector v3 = d3.biinvariantVector(point);
    assertEquals(v1.vector().length(), v2.vector().length());
    assertEquals(v1.vector().length(), v3.vector().length());
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  @Test
  void testRandom() {
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(manifold);
    for (Biinvariant biinvariant : map.values())
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor distances = biinvariant.distances(points).sunder(xya);
        Tensor shift = RandomVariate.of(distribution, 3);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
          Chop._05.requireClose(distances, //
              biinvariant.distances(tensorMapping.slash(points)).sunder(tensorMapping.apply(xya)));
      }
  }

  @Test
  void testSymmetric() {
    Tensor sequence = RandomSample.of(Se2RandomSample.of(NormalDistribution.standard()), 15);
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(Se2CoveringGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Sedarim sedarim = biinvariant.distances(sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(sedarim::sunder));
      SquareMatrixQ.require(matrix);
      SymmetricMatrixQ.require(matrix);
    }
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(HarborBiinvariant.class.getModifiers()));
  }
}
