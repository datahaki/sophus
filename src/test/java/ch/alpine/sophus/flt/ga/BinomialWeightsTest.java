// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.red.Total;

public class BinomialWeightsTest {
  @Test
  public void testBinomial() {
    for (int size = 1; size < 5; ++size) {
      Tensor mask = BinomialWeights.INSTANCE.apply(size);
      assertEquals(Total.of(mask), RealScalar.ONE);
      ExactTensorQ.require(mask);
    }
  }

  @Test
  public void testSpecific() {
    Tensor result = BinomialWeights.INSTANCE.apply(5);
    Tensor expect = Tensors.fromString("{1/16, 1/4, 3/8, 1/4, 1/16}");
    assertEquals(result, expect);
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> BinomialWeights.INSTANCE.apply(-1));
  }
}
