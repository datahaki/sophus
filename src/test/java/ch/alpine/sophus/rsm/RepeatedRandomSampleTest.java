// code by jph
package ch.alpine.sophus.rsm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class RepeatedRandomSampleTest {
  @Test
  void test() {
    RandomSampleInterface rsi = RandomVariate.array(UniformDistribution.unit(), 2, 3);
    Tensor tensor = RandomSample.of(rsi);
    List<Integer> list = Dimensions.of(tensor);
    assertEquals(list, List.of(2, 3));
  }
}
