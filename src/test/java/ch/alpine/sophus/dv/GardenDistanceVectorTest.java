// code by jph
package ch.alpine.sophus.dv;

import java.io.IOException;

import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class GardenDistanceVectorTest extends TestCase {
  public void testRn1() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy(GardenDistanceVector.of(vectorLogManifold, sequence));
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSn1() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  public void testEmpty() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, Tensors.empty());
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.empty());
  }

  public void testSingleton() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, Tensors.fromString("{{2,3,4}}"));
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.vector(0));
  }

  public void testNullFail() {
    AssertFail.of(() -> GardenDistanceVector.of(null, Tensors.empty()));
  }
}
