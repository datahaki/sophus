// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.NullSpace;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.gr.InfluenceMatrixQ;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Round;

class BiinvariantVectorTest {
  @Test
  void testSimpleR2() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
    Manifold manifold = RnGroup.INSTANCE;
    Tensor matrix = Tensor.of(sequence.stream().map(manifold.exponential(point)::vectorLog));
    Tensor nullsp = NullSpace.of(Transpose.of(matrix));
    OrthogonalMatrixQ.require(nullsp);
    Chop._08.requireClose(PseudoInverse.of(nullsp), Transpose.of(nullsp));
  }

  private static Tensor _check(Manifold manifold, Tensor sequence, Tensor point) {
    HsDesign hsDesign = new HsDesign(manifold);
    Tensor V = hsDesign.matrix(sequence, point);
    Tensor VT = Transpose.of(V);
    Tensor pinv = PseudoInverse.of(VT.dot(V));
    SymmetricMatrixQ.require(pinv, Chop._04);
    Tensor sigma_inverse = Symmetrize.of(pinv);
    // ---
    Tensor H = V.dot(sigma_inverse.dot(VT)); // "hat matrix"
    InfluenceMatrixQ.require(H, Chop._09);
    // ---
    Tensor traceh = Trace.of(H);
    Chop._07.requireClose(traceh, Round.of(traceh));
    // ---
    Tensor matrix = new HsDesign(manifold).matrix(sequence, point);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(matrix);
    SymmetricMatrixQ.require(influenceMatrix.matrix());
    Chop._08.requireClose(H, influenceMatrix.matrix());
    Tensor n = NullSpace.of(Transpose.of(V));
    Tensor M = influenceMatrix.residualMaker();
    InfluenceMatrixQ.require(M, Chop._09);
    Chop._08.requireClose(M, Transpose.of(n).dot(n));
    // ---
    Tensor Xinv = PseudoInverse.of(V);
    Tensor p = V.dot(Xinv);
    Chop._08.requireClose(H, p);
    // ---
    Tensor d1 = influenceMatrix.leverages_sqrt();
    Tensor d2 = Tensor.of(influenceMatrix.matrix().stream().map(Vector2Norm::of));
    Chop._08.requireClose(d1, d2);
    return sigma_inverse;
  }

  @Test
  void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    RandomGenerator random = new Random(3);
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, random, count, 3);
      Tensor point = RandomVariate.of(distribution, random, 3);
      Tensor sigma_inverse = _check(manifold, sequence, point);
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
    }
  }

  @Test
  void testSe2AnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    Manifold manifold = Se2Group.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor sigma_inverse = _check(manifold, sequence, point);
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
    }
  }

  @Test
  void testSnCAnchorIsTarget() {
    Manifold manifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 4; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 1; count < 7; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor sigma_inverse = _check(manifold, sequence, point);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(sigma_inverse, Chop._08));
      }
    }
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(BiinvariantVector.class.getModifiers()));
  }
}
