// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3InverseDistanceCoordinateTest extends TestCase {
  public void testSimple() {
    Tensor g1 = So3Exponential.INSTANCE.exp(Tensors.vector(0.2, 0.3, 0.4));
    Tensor g2 = So3Exponential.INSTANCE.exp(Tensors.vector(0.1, 0.0, 0.5));
    Tensor g3 = So3Exponential.INSTANCE.exp(Tensors.vector(0.3, 0.5, 0.2));
    Tensor g4 = So3Exponential.INSTANCE.exp(Tensors.vector(0.5, 0.2, 0.1));
    Tensor sequence = Tensors.of(g1, g2, g3, g4);
    Tensor mean = So3Exponential.INSTANCE.exp(Tensors.vector(0.4, 0.2, 0.3));
    Tensor weights = So3InverseDistanceCoordinate.INSTANCE.weights(sequence, mean);
    Tensor defect = So3BiinvariantMeanDefect.INSTANCE.defect(sequence, weights, mean);
    Chop._10.requireAllZero(defect);
  }

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
      Tensor mean = So3Exponential.INSTANCE.exp(RandomVariate.of(d2, 3));
      Tensor weights1 = So3InverseDistanceCoordinate.INSTANCE.weights(sequence, mean);
      Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
      Chop._08.requireClose(mean, o2);
      // ---
      LieGroupElement lieGroupElement = So3Group.INSTANCE.element(TestHelper.spawn_So3());
      Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
      Tensor weights2 = So3InverseDistanceCoordinate.INSTANCE.weights(seqlft, lieGroupElement.combine(mean));
      Chop._10.requireClose(weights1, weights2);
      // ---
      Tensor seqinv = new LieGroupOps(So3Group.INSTANCE).allI(sequence);
      Tensor weights3 = So3InverseDistanceCoordinate.INSTANCE.weights( //
          seqinv, So3Group.INSTANCE.element(mean).inverse().toCoordinate());
      Chop._10.requireClose(weights1, weights3);
    }
  }

  public void testLagrange() {
    Distribution distribution = NormalDistribution.of(0.0, 0.1);
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
      int index = 0;
      for (Tensor mean : sequence) {
        Tensor weights = So3InverseDistanceCoordinate.INSTANCE.weights(sequence, mean);
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
        Tensor mean = So3Exponential.INSTANCE.exp(RandomVariate.of(d2, 3));
        So3InverseDistanceCoordinate.INSTANCE.weights(sequence, mean);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}
