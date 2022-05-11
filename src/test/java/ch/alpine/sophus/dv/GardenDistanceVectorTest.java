// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class GardenDistanceVectorTest {
  @Test
  public void testRn1() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold vectorLogManifold = RnGroup.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy(GardenDistanceVector.of(vectorLogManifold, sequence));
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  public void testSn1() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Manifold vectorLogManifold = SnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold vectorLogManifold = Se2CoveringGroup.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  public void testEmpty() {
    Manifold vectorLogManifold = Se2CoveringGroup.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, Tensors.empty());
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.empty());
  }

  @Test
  public void testSingleton() {
    Manifold vectorLogManifold = Se2CoveringGroup.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GardenDistanceVector.of(vectorLogManifold, Tensors.fromString("{{2,3,4}}"));
    Tensor result = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.vector(0));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> GardenDistanceVector.of(null, Tensors.empty()));
  }
}
