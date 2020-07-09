// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.hs.HsProjection.Matrix;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.Trace;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import ch.ethz.idsc.tensor.sca.Sqrt;
import junit.framework.TestCase;

public class HsProjectionTest extends TestCase {
  public void testSimple() {
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
    // ---
    Scalar scalar = Trace.of(H);
    Chop._07.requireClose(scalar, Round.of(scalar));
    // ---
    Matrix matrix = new HsProjection(vectorLogManifold).new Matrix(sequence, point);
    Chop._08.requireClose(H, matrix.influence());
    Tensor n = LeftNullSpace.usingQR(V);
    Chop._08.requireClose(matrix.residualMaker(), Transpose.of(n).dot(n));
    // ---
    Tensor Xinv = PseudoInverse.of(V);
    Tensor p = V.dot(Xinv);
    Chop._08.requireClose(H, p);
    // ---
    Tensor d1 = Tensor.of(matrix.leverages().stream() //
        .map(Scalar.class::cast) //
        .map(Sqrt.FUNCTION));
    Tensor d2 = Tensor.of(matrix.influence().stream().map(Norm._2::ofVector));
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
    for (int dimension = 2; dimension < 5; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 1; count < 10; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor sigma_inverse = _check(vectorLogManifold, sequence, point);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(sigma_inverse));
      }
    }
  }
}
