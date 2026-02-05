// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.s.SnManifold;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
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
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;

class HsDesignTest {
  @Test
  void testRn() {
    Manifold manifold = RGroup.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      Distribution distribution = UniformDistribution.unit();
      for (int count = dimension + 1; count < 8; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, dimension);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(manifold.exponential(point).log().slash(sequence)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveDefiniteMatrixQ.ofHermitian(form));
        Tensor point = RandomVariate.of(distribution, dimension);
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(new Mahalanobis(manifold.exponential(point).log().slash(sequence)).sigma_inverse()));
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
      RandomSampleInterface randomSampleInterface = new Sphere(dimension);
      for (int count = dimension + 2; count < 8; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(manifold.exponential(point).log().slash(sequence)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._06)); // has excess dimension
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor matrix = manifold.exponential(point).log().slash(sequence);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(new Mahalanobis(matrix).sigma_inverse(), Chop._08));
      }
    }
  }

  @Test
  void testSe2C() {
    RandomGenerator randomGenerator = new Random(1);
    Distribution distribution = UniformDistribution.of(-10, +10);
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, randomGenerator, count, 3);
      for (Tensor point : sequence) {
        Tensor design = manifold.exponential(point).log().slash(sequence);
        Mahalanobis mahalanobis = new Mahalanobis(design);
        InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
        Chop._08.requireClose(mahalanobis.leverages(), influenceMatrix.leverages());
        Chop._08.requireClose(mahalanobis.leverages_sqrt(), influenceMatrix.leverages_sqrt());
        Tensor sigma_inverse = mahalanobis.sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
        new SymmetricMatrixQ(Chop._08).requireMember(mahalanobis.sigma_n());
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
      Tensor design = manifold.exponential(point).log().slash(sequence);
      Mahalanobis mahalanobis = new Mahalanobis(design);
      Tensor sigma_inverse = mahalanobis.sigma_inverse();
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      // ---
      Tensor vt = design;
      Tensor v = Transpose.of(vt);
      Tensor dot = IdentityMatrix.of(count).subtract(vt.dot(sigma_inverse.dot(v)));
      Tensor matrix = manifold.exponential(point).log().slash(sequence);
      InfluenceMatrix hsInfluence = InfluenceMatrix.of(matrix);
      Chop._08.requireClose(dot, hsInfluence.residualMaker());
    }
  }
}
