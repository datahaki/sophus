// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.gr.Mahalanobis;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class HsDesignTest {
  @Test
  void testRn() {
    Manifold manifold = RnGroup.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      Distribution distribution = UniformDistribution.unit();
      for (int count = dimension + 1; count < 8; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, dimension);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(new HsDesign(manifold).matrix(sequence, point)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveDefiniteMatrixQ.ofHermitian(form));
        Tensor point = RandomVariate.of(distribution, dimension);
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(new Mahalanobis(new HsDesign(manifold).matrix(sequence, point)).sigma_inverse()));
      }
    }
  }

  @Test
  void testRnExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    Tensor design = RandomVariate.of(distribution, 6, 3);
    Mahalanobis mahalanobis = new Mahalanobis(design);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
    ExactTensorQ.require(mahalanobis.sigma_inverse());
    assertTrue(mahalanobis.toString().startsWith("Mahalanobis"));
    assertEquals(mahalanobis.leverages(), influenceMatrix.leverages());
  }

  @Test
  void testSn() {
    Manifold manifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 2; count < 8; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(new HsDesign(manifold).matrix(sequence, point)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._06)); // has excess dimension
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor matrix = new HsDesign(manifold).matrix(sequence, point);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(new Mahalanobis(matrix).sigma_inverse(), Chop._08));
      }
    }
  }

  @Test
  void testSe2C() {
    RandomGenerator random = new Random(1);
    Distribution distribution = UniformDistribution.of(-10, +10);
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, random, count, 3);
      for (Tensor point : sequence) {
        Tensor design = new HsDesign(manifold).matrix(sequence, point);
        Mahalanobis mahalanobis = new Mahalanobis(design);
        InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
        Chop._08.requireClose(mahalanobis.leverages(), influenceMatrix.leverages());
        Chop._08.requireClose(mahalanobis.leverages_sqrt(), influenceMatrix.leverages_sqrt());
        Tensor sigma_inverse = mahalanobis.sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
        SymmetricMatrixQ.require(mahalanobis.sigma_n(), Chop._08);
      }
    }
  }

  @Test
  void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int count = 4; count < 8; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor design = new HsDesign(manifold).matrix(sequence, point);
      Mahalanobis mahalanobis = new Mahalanobis(design);
      Tensor sigma_inverse = mahalanobis.sigma_inverse();
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      // ---
      Tensor vt = design;
      Tensor v = Transpose.of(vt);
      Tensor dot = IdentityMatrix.of(count).subtract(vt.dot(sigma_inverse.dot(v)));
      Tensor matrix = new HsDesign(manifold).matrix(sequence, point);
      InfluenceMatrix hsInfluence = InfluenceMatrix.of(matrix);
      Chop._08.requireClose(dot, hsInfluence.residualMaker());
    }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  @Test
  void testSe2CadInvariant() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor leverages_sqrt = new Mahalanobis(new HsDesign(manifold).matrix(sequence, point)).leverages_sqrt();
      leverages_sqrt.stream().map(Scalar.class::cast).forEach(Clips.unit()::requireInside);
      Tensor shift = RandomVariate.of(distribution, 3);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor matrix = new HsDesign(manifold).matrix(tensorMapping.slash(sequence), tensorMapping.apply(point));
        Chop._05.requireClose(leverages_sqrt, //
            new Mahalanobis(matrix).leverages_sqrt());
      }
    }
  }
}
