// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class Normal2DTest extends TestCase {
  public void testStringLength() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
    }
  }

  public void testStringZerosLength() {
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = Array.zeros(count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
      assertEquals(tensor, string);
    }
  }
}
