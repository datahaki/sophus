// code by jph
package ch.alpine.sophus.math.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Mean;

class BoxRandomSampleTest {
  @Test
  void testSimple3D() {
    Tensor offset = Tensors.vector(2, 2, 3);
    Tensor width = Tensors.vector(1, 1, 1);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(offset.subtract(width), offset.add(width));
    Tensor samples = RandomSample.of(randomSampleInterface, 100);
    Scalars.compare(Vector2Norm.of(Mean.of(samples).subtract(offset)), RealScalar.of(0.1));
    assertEquals(Dimensions.of(samples), Arrays.asList(100, 3));
  }

  @Test
  void testSingle() {
    Tensor offset = Tensors.vector(2, 2, 3);
    Tensor width = Tensors.vector(1, 1, 1);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(offset.subtract(width), offset.add(width));
    Tensor rand = RandomSample.of(randomSampleInterface);
    assertEquals(Dimensions.of(rand), Arrays.asList(3));
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(Tensors.vector(1, 2, 3), Tensors.vector(3, 4, 8));
    RandomSampleInterface copy = Serialization.copy(randomSampleInterface);
    Tensor tensor = RandomSample.of(copy);
    VectorQ.requireLength(tensor, 3);
  }

  @Test
  void testDimensionFail() {
    assertThrows(Exception.class, () -> BoxRandomSample.of(Tensors.vector(1, 2), Tensors.vector(1, 2, 3)));
  }

  @Test
  void testSignFail() {
    assertThrows(Exception.class, () -> BoxRandomSample.of(Tensors.vector(1, 2), Tensors.vector(2, 1)));
  }
}
