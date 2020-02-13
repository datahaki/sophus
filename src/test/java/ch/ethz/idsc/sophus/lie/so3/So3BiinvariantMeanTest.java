// code by jph
package ch.ethz.idsc.sophus.lie.so3;

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
  public void testSimple() {
    Tensor sequence = Tensors.of( //
        So3Exponential.INSTANCE.exp(Tensors.vector(+1 + 0.3, 0, 0)), //
        So3Exponential.INSTANCE.exp(Tensors.vector(+0 + 0.3, 0, 0)), //
        So3Exponential.INSTANCE.exp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = So3BiinvariantMeanDefect.INSTANCE.defect( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), So3Exponential.INSTANCE.exp(Tensors.vector(+0.3, 0, 0)));
    Chop._10.requireAllZero(log);
  }

  public void testConvergence() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor w2 = So3InverseDistanceCoordinates.INSTANCE.weights(sequence, mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
      Chop._08.requireClose(mean, o2);
    }
  }

  public void testConvergenceExact() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    int n = 4;
    Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
    Tensor mean = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
    Tensor w2 = So3InverseDistanceCoordinates.INSTANCE.weights(sequence, mean);
    Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, w2);
    Chop._08.requireClose(mean, o2.get());
    Chop._08.requireClose(weights, w2);
  }
}
