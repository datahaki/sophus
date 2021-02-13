// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Trace;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import junit.framework.TestCase;

public class GrassmannQTest extends TestCase {
  public void testSimple() {
    Tensor matrix = Tensors.fromString("{{1, 0}, {2, 3}}");
    assertFalse(GrassmannQ.of(matrix));
    AssertFail.of(() -> GrassmannQ.require(matrix));
  }

  public void testChop() {
    Tensor matrix = Tensors.fromString("{{1, 0}, {2, 3}}");
    assertFalse(GrassmannQ.of(matrix, Tolerance.CHOP));
    AssertFail.of(() -> GrassmannQ.require(matrix, Tolerance.CHOP));
  }

  public void testSimpleR2() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    Tensor matrix = Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
    Tensor nullsp = LeftNullSpace.of(matrix);
    OrthogonalMatrixQ.require(nullsp);
    Chop._08.requireClose(PseudoInverse.of(nullsp), Transpose.of(nullsp));
  }

  private static Tensor _check(VectorLogManifold vectorLogManifold, Tensor sequence, Tensor point) {
    HsDesign hsDesign = new HsDesign(vectorLogManifold);
    Tensor V = hsDesign.matrix(sequence, point);
    Tensor VT = Transpose.of(V);
    Tensor pinv = PseudoInverse.of(VT.dot(V));
    SymmetricMatrixQ.require(pinv, Chop._04);
    Tensor sigma_inverse = Symmetrize.of(pinv);
    // ---
    Tensor H = V.dot(sigma_inverse.dot(VT)); // "hat matrix"
    GrassmannQ.require(H, Chop._09);
    // ---
    Tensor traceh = Trace.of(H);
    Chop._07.requireClose(traceh, Round.of(traceh));
    // ---
    Tensor matrix = new HsDesign(vectorLogManifold).matrix(sequence, point);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(matrix);
    SymmetricMatrixQ.require(influenceMatrix.matrix());
    Chop._08.requireClose(H, influenceMatrix.matrix());
    Tensor n = LeftNullSpace.usingQR(V);
    Tensor M = influenceMatrix.residualMaker();
    GrassmannQ.require(M, Chop._09);
    Chop._08.requireClose(M, Transpose.of(n).dot(n));
    // ---
    Tensor Xinv = PseudoInverse.of(V);
    Tensor p = V.dot(Xinv);
    Chop._08.requireClose(H, p);
    // ---
    Tensor d1 = influenceMatrix.leverages_sqrt();
    Tensor d2 = Tensor.of(influenceMatrix.matrix().stream().map(VectorNorm2::of));
    Chop._08.requireClose(d1, d2);
    return sigma_inverse;
  }

  public void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor sigma_inverse = _check(vectorLogManifold, sequence, point);
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
    }
  }

  public void testSe2AnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2Manifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor sigma_inverse = _check(vectorLogManifold, sequence, point);
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
    }
  }

  public void testSnCAnchorIsTarget() {
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 4; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 1; count < 7; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor sigma_inverse = _check(vectorLogManifold, sequence, point);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(sigma_inverse, Chop._08));
      }
    }
  }
}
