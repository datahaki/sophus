// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

/** harbor == spring ! */
public class GardenDistancesTest extends TestCase {
  public void testRn() {
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariant.HARBOR.weighting(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator completeDistances = GardenDistances.of(vectorLogManifold, variogram, sequence);
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
    TensorUnaryOperator w1 = Biinvariant.HARBOR.weighting(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator completeDistances = GardenDistances.of(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = point -> NormalizeTotal.FUNCTION.apply(completeDistances.apply(point));
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      // boolean close =
      Chop._08.close(w1.apply(point), w2.apply(point));
      // System.out.println(close);
    }
  }

  public void testRn1() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, variogram, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSn1() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, variogram, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, variogram, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }
}
