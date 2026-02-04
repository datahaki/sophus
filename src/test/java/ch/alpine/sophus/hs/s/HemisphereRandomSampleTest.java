package ch.alpine.sophus.hs.s;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.sca.Sign;

class HemisphereRandomSampleTest {
  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 2, 3, 4 })
  void test(int dimension) throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = HemisphereRandomSample.of(dimension);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    Sign.requirePositive(Last.of(tensor));
    Serialization.copy(randomSampleInterface);
  }
}
