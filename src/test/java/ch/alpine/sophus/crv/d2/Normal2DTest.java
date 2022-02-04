// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Unit;
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

  public void testStringUnit() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
      if (0 < string.length())
        assertEquals(Unprotect.getUnitUnique(string), Unit.ONE);
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
