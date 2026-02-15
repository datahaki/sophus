// code by jph
package ch.alpine.sophus.hs.rpn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.sca.Sign;

class HemisphereRandomSampleTest {
  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 2, 3, 4 })
  void testSimple(int dimension) throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = HemisphereRandomSample.of(dimension);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    Sign.requirePositive(Last.of(tensor));
    Serialization.copy(randomSampleInterface);
  }

  @Test
  void testSingle() {
    RandomSampleInterface rsi = HemisphereRandomSample.of(0);
    boolean allMatch = RandomSample.stream(rsi).limit(100).allMatch(Tensors.vector(1)::equals);
    assertTrue(allMatch);
  }

  @Test
  void test2() {
    RandomSampleInterface randomSampleInterface = HemisphereRandomSample.of(2);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    VectorQ.requireLength(tensor, 3);
  }
}
