// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
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
        Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
        Tensor forms = Tensor.of(sequence.stream().map(point -> mahalanobis.new Form(sequence, point).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveDefiniteMatrixQ.ofHermitian(form));
        Tensor point = RandomVariate.of(distribution, dimension);
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(mahalanobis.new Form(sequence, point).sigma_inverse()));
      }
    }
  }

  public void testSn() {
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 2; count < 10; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
        Tensor forms = Tensor.of(sequence.stream().map(point -> mahalanobis.new Form(sequence, point).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._08)); // has excess dimension
        Tensor point = RandomSample.of(randomSampleInterface);
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(mahalanobis.new Form(sequence, point).sigma_inverse()));
      }
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
      for (Tensor point : sequence) {
        Tensor sigma_inverse = mahalanobis.new Form(sequence, point).sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      }
    }
  }

  public void testSe2CAnchorIsTarget() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
      Tensor point = RandomVariate.of(distribution, 3);
      Form form = mahalanobis.new Form(sequence, point);
      Tensor sigma_inverse = form.sigma_inverse();
      assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      // ---
      Tensor vt = form.levers();
      Tensor v = Transpose.of(vt);
      Tensor dot = IdentityMatrix.of(count).subtract(vt.dot(sigma_inverse.dot(v)));
      HsProjection hsProjection = new HsProjection(vectorLogManifold);
      Tensor projection = hsProjection.projection(sequence, point);
      Chop._08.requireClose(dot, projection);
    }
  }

  public void testNullFail() {
    try {
      new Mahalanobis(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testEmptyFail() {
    Mahalanobis mahalanobis = new Mahalanobis(RnManifold.INSTANCE);
    try {
      mahalanobis.new Form(Tensors.empty(), Tensors.vector(1, 2, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
