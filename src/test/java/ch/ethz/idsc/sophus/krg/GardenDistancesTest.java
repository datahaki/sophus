// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
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
  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariants.HARBOR.weighting(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = Biinvariants.GARDEN.weighting(vectorLogManifold, variogram, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      Chop._08.close(w1.apply(point), w2.apply(point));
    }
  }

  public void testRn1() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSn1() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testEmpty() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, Tensors.empty());
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.empty());
  }

  public void testSingleton() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistances.of(vectorLogManifold, Tensors.fromString("{{2,3,4}}"));
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.vector(0));
  }
}
