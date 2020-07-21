// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class MahalanobisTest extends TestCase {
  public void testRn() {
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      Distribution distribution = UniformDistribution.unit();
      for (int count = dimension + 1; count < 10; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, dimension);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(vectorLogManifold.logAt(point), sequence).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveDefiniteMatrixQ.ofHermitian(form));
        Tensor point = RandomVariate.of(distribution, dimension);
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(new Mahalanobis(vectorLogManifold.logAt(point), sequence).sigma_inverse()));
      }
    }
  }

  public void testSn() {
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 2; count < 10; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(vectorLogManifold.logAt(point), sequence).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._06)); // has excess dimension
        Tensor point = RandomSample.of(randomSampleInterface);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(new Mahalanobis(vectorLogManifold.logAt(point), sequence).sigma_inverse(), Chop._08));
      }
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      for (Tensor point : sequence) {
        Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold.logAt(point), sequence);
        Tensor sigma_inverse = mahalanobis.sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      }
    }
  }

  public void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold.logAt(point), sequence);
      Tensor sigma_inverse = mahalanobis.sigma_inverse();
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      // ---
      Tensor vt = mahalanobis.matrix();
      Tensor v = Transpose.of(vt);
      Tensor dot = IdentityMatrix.of(count).subtract(vt.dot(sigma_inverse.dot(v)));
      HsInfluence hsInfluence = new HsInfluence(vectorLogManifold.logAt(point), sequence);
      Chop._08.requireClose(dot, hsInfluence.residualMaker());
    }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testSe2CadInvariant() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor leverages_sqrt = new Mahalanobis(vectorLogManifold.logAt(point), sequence).leverages_sqrt();
      Tensor shift = RandomVariate.of(distribution, 3);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
        Chop._05.requireClose(leverages_sqrt, //
            new Mahalanobis(vectorLogManifold.logAt(tensorMapping.apply(point)), tensorMapping.slash(sequence)).leverages_sqrt());
    }
  }

  public void testNullFail() {
    try {
      new Mahalanobis(null, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
