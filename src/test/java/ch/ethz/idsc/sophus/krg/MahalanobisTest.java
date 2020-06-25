// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class MahalanobisTest extends TestCase {
  public void testSn() {
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = dimension + 2; count < 10; ++count) {
        Tensor sequence = RandomSample.of(randomSampleInterface, count);
        Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
        Tensor forms = Tensor.of(sequence.stream().map(point -> mahalanobis.new Form(sequence, point).sigma_inverse()));
        for (Tensor form : forms)
          assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form, Chop._08));
      }
    }
  }

  public void testSe2C() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(UniformDistribution.of(-10, +10), count, 3);
      Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
      Tensor forms = Tensor.of(sequence.stream().map(point -> mahalanobis.new Form(sequence, point).sigma_inverse()));
      for (Tensor form : forms) {
        assertTrue(PositiveSemidefiniteMatrixQ.ofHermitian(form));
      }
    }
  }
}
