// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.EqualsReduce;
import ch.alpine.tensor.red.Total;

class Normal2DTest {
  @Test
  void testStringLength() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
    }
  }

  @Test
  void testStringUnit() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
      if (0 < string.length())
        assertEquals(EqualsReduce.zero(string), RealScalar.ZERO);
    }
  }

  @Test
  void testStringZerosLength() {
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = Array.zeros(count, 2);
      Tensor string = Normal2D.string(tensor);
      assertEquals(string.length(), count);
      assertEquals(tensor, string);
    }
  }

  @Test
  void testQuantity() {
    Distribution distribution = TriangularDistribution.with(Quantity.of(0, "m"), Quantity.of(1, "m"));
    {
      int n = 0;
      Tensor result = Normal2D.string(RandomVariate.of(distribution, n, 2));
      assertEquals(Dimensions.of(result), List.of(n));
      Total.of(result);
    }
    for (int n = 1; n < 5; ++n) {
      Tensor result = Normal2D.string(RandomVariate.of(distribution, n, 2));
      assertEquals(Dimensions.of(result), Arrays.asList(n, 2));
      Total.of(result);
    }
  }

  @Test
  void testTriple() {
    Distribution distribution = TriangularDistribution.with(Quantity.of(0, "m"), Quantity.of(1, "m"));
    assertThrows(Exception.class, () -> Normal2D.string(RandomVariate.of(distribution, 1, 3)));
  }
}
