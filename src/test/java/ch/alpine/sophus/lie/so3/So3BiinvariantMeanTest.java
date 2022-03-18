// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class So3BiinvariantMeanTest {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(So3Manifold.INSTANCE);

  @Test
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

  @Test
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

  @Test
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
