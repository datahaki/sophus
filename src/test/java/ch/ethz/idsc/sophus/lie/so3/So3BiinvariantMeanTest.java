// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.util.Random;

import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3BiinvariantMeanTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(So3Manifold.INSTANCE);

  public void testSimple() {
    Tensor sequence = Tensors.of( //
        Rodrigues.vectorExp(Tensors.vector(+1 + 0.3, 0, 0)), //
        Rodrigues.vectorExp(Tensors.vector(+0 + 0.3, 0, 0)), //
        Rodrigues.vectorExp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = new MeanDefect( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), //
        So3Manifold.INSTANCE.exponential(Rodrigues.vectorExp(Tensors.vector(+0.3, 0, 0)))).tangent();
    Chop._10.requireAllZero(log);
  }

  public void testConvergence() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int n = 4 + random.nextInt(6);
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(Rodrigues::vectorExp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), random, n));
      Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor w2 = barycentricCoordinate.weights(sequence, mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
      Chop._08.requireClose(mean, o2);
    }
  }

  public void testConvergenceExact() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    int n = 4;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor w2 = barycentricCoordinate.weights(sequence, mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
      Chop._08.requireClose(mean, o2.get());
      Chop._08.requireClose(weights, w2);
    }
  }
}
