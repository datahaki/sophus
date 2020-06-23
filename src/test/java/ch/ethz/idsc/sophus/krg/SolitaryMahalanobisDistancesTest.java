// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class SolitaryMahalanobisDistancesTest extends TestCase {
  public void testRn() {
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    TensorUnaryOperator w1 = PseudoDistances.SOLITARY.normalized(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator completeDistances = SolitaryMahalanobisDistances.of(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = point -> NormalizeTotal.FUNCTION.apply(completeDistances.apply(point));
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(UniformDistribution.unit(), 3);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    TensorUnaryOperator w1 = PseudoDistances.SOLITARY.normalized(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator completeDistances = SolitaryMahalanobisDistances.of(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = point -> NormalizeTotal.FUNCTION.apply(completeDistances.apply(point));
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }
}
