// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class GardenDistanceVectorTest {
  @Test
  void testRn1() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold manifold = RnGroup.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Sedarim tensorUnaryOperator = Serialization.copy(new GardenDistanceVector(manifold, sequence));
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  void testSn1() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Manifold manifold = SnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      Sedarim tensorUnaryOperator = new GardenDistanceVector(manifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Sedarim tensorUnaryOperator = new GardenDistanceVector(manifold, sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder));
      Chop._10.requireAllZero(Diagonal.of(matrix));
    }
  }

  @Test
  void testEmpty() {
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    Sedarim tensorUnaryOperator = new GardenDistanceVector(manifold, Tensors.empty());
    Tensor result = tensorUnaryOperator.sunder(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.empty());
  }

  @Test
  void testSingleton() {
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    Sedarim tensorUnaryOperator = new GardenDistanceVector(manifold, Tensors.fromString("{{2,3,4}}"));
    Tensor result = tensorUnaryOperator.sunder(Tensors.vector(1, 2, 3));
    assertEquals(result, Tensors.vector(0));
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(GardenDistanceVector.class.getModifiers()));
  }
}
