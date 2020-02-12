// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3InverseDistanceCoordinatesTest extends TestCase {
  public void testSimple() {
    Tensor g1 = So3Exponential.INSTANCE.exp(Tensors.vector(0.2, 0.3, 0.4));
    Tensor g2 = So3Exponential.INSTANCE.exp(Tensors.vector(0.1, 0.0, 0.5));
    Tensor g3 = So3Exponential.INSTANCE.exp(Tensors.vector(0.3, 0.5, 0.2));
    Tensor g4 = So3Exponential.INSTANCE.exp(Tensors.vector(0.5, 0.2, 0.1));
    Tensor sequence = Tensors.of(g1, g2, g3, g4);
    TensorUnaryOperator tensorUnaryOperator = So3InverseDistanceCoordinates.INSTANCE.of(sequence);
    Tensor mean = So3Exponential.INSTANCE.exp(Tensors.vector(0.4, 0.2, 0.3));
    Tensor weights = tensorUnaryOperator.apply(mean);
    Tensor defect = So3BiinvariantMeanDefect.INSTANCE.defect(sequence, weights, mean);
    Chop._10.requireAllZero(defect);
  }

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
      TensorUnaryOperator tensorUnaryOperator = So3InverseDistanceCoordinates.INSTANCE.of(sequence);
      Tensor mean = So3Exponential.INSTANCE.exp(RandomVariate.of(d2, 3));
      Tensor weights = tensorUnaryOperator.apply(mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
      Chop._08.requireClose(mean, o2);
    }
  }

  public void testLagrange() {
    Distribution distribution = NormalDistribution.of(0.0, 0.1);
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
      TensorUnaryOperator tensorUnaryOperator = So3InverseDistanceCoordinates.INSTANCE.of(sequence);
      int index = 0;
      for (Tensor mean : sequence) {
        Tensor weights = tensorUnaryOperator.apply(mean);
        Chop._08.requireClose(weights, UnitVector.of(n, index));
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(mean, o2);
        ++index;
      }
    }
  }

  public void testSpanFail() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (int n = 1; n < 4; ++n)
      try {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
        TensorUnaryOperator tensorUnaryOperator = So3InverseDistanceCoordinates.INSTANCE.of(sequence);
        Tensor mean = So3Exponential.INSTANCE.exp(RandomVariate.of(d2, 3));
        tensorUnaryOperator.apply(mean);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}
