// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.ProjectedCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3BiinvariantMeanTest extends TestCase {
  private static final ProjectedCoordinate[] PROJECTED_COORDINATES = { //
      AbsoluteCoordinate.linear(So3Manifold.INSTANCE), //
      AbsoluteCoordinate.smooth(So3Manifold.INSTANCE) };
  public static final MeanDefect INSTANCE = BiinvariantMeanDefect.of(So3Manifold.INSTANCE);

  public void testSimple() {
    Tensor sequence = Tensors.of( //
        Rodrigues.vectorExp(Tensors.vector(+1 + 0.3, 0, 0)), //
        Rodrigues.vectorExp(Tensors.vector(+0 + 0.3, 0, 0)), //
        Rodrigues.vectorExp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = INSTANCE.defect( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), Rodrigues.vectorExp(Tensors.vector(+0.3, 0, 0)));
    Chop._10.requireAllZero(log);
  }

  public void testConvergence() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES)
      for (int n = 3; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
        Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
        Tensor w2 = projectedCoordinate.weights(sequence, mean);
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
        Chop._08.requireClose(mean, o2);
      }
  }

  public void testConvergenceExact() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    int n = 4;
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor w2 = projectedCoordinate.weights(sequence, mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
      Chop._08.requireClose(mean, o2.get());
      Chop._08.requireClose(weights, w2);
    }
  }
}
