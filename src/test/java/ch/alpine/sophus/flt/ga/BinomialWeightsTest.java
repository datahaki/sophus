// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.red.Total;
import junit.framework.TestCase;

public class BinomialWeightsTest extends TestCase {
  public void testBinomial() {
    for (int size = 1; size < 5; ++size) {
      Tensor mask = BinomialWeights.INSTANCE.apply(size);
      assertEquals(Total.of(mask), RealScalar.ONE);
      ExactTensorQ.require(mask);
    }
  }

  public void testSpecific() {
    Tensor result = BinomialWeights.INSTANCE.apply(5);
    Tensor expect = Tensors.fromString("{1/16, 1/4, 3/8, 1/4, 1/16}");
    assertEquals(result, expect);
  }

  public void testFail() {
    AssertFail.of(() -> BinomialWeights.INSTANCE.apply(-1));
  }
}
