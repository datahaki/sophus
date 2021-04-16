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
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.gr.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.gr.Mahalanobis;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class HsDesignTest extends TestCase {
  public void testRn() {
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      Distribution distribution = UniformDistribution.unit();
      for (int count = dimension + 1; count < 8; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, dimension);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(new HsDesign(vectorLogManifold).matrix(sequence, point)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveDefiniteMatrixQ.ofHermitian(form));
        Tensor point = RandomVariate.of(distribution, dimension);
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(new Mahalanobis(new HsDesign(vectorLogManifold).matrix(sequence, point)).sigma_inverse()));
      }
    }
  }

  public void testRnExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    Tensor design = RandomVariate.of(distribution, 6, 3);
    Mahalanobis mahalanobis = new Mahalanobis(design);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
    ExactTensorQ.require(mahalanobis.sigma_inverse());
    assertTrue(mahalanobis.toString().startsWith("Mahalanobis"));
    assertEquals(mahalanobis.leverages(), influenceMatrix.leverages());
  }

  public void testSn() {
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 2; count < 8; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Tensor forms = Tensor.of(sequence.stream().map(point -> new Mahalanobis(new HsDesign(vectorLogManifold).matrix(sequence, point)).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._06)); // has excess dimension
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor matrix = new HsDesign(vectorLogManifold).matrix(sequence, point);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(new Mahalanobis(matrix).sigma_inverse(), Chop._08));
      }
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      for (Tensor point : sequence) {
        Tensor design = new HsDesign(vectorLogManifold).matrix(sequence, point);
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

  public void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 8; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor design = new HsDesign(vectorLogManifold).matrix(sequence, point);
      Mahalanobis mahalanobis = new Mahalanobis(design);
      Tensor sigma_inverse = mahalanobis.sigma_inverse();
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      // ---
      Tensor vt = design;
      Tensor v = Transpose.of(vt);
      Tensor dot = IdentityMatrix.of(count).subtract(vt.dot(sigma_inverse.dot(v)));
      Tensor matrix = new HsDesign(vectorLogManifold).matrix(sequence, point);
      InfluenceMatrix hsInfluence = InfluenceMatrix.of(matrix);
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
      Tensor leverages_sqrt = new Mahalanobis(new HsDesign(vectorLogManifold).matrix(sequence, point)).leverages_sqrt();
      leverages_sqrt.stream().map(Scalar.class::cast).forEach(Clips.unit()::requireInside);
      Tensor shift = RandomVariate.of(distribution, 3);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor matrix = new HsDesign(vectorLogManifold).matrix(tensorMapping.slash(sequence), tensorMapping.apply(point));
        Chop._05.requireClose(leverages_sqrt, //
            new Mahalanobis(matrix).leverages_sqrt());
      }
    }
  }
}
