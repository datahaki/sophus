// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clips;

class SutherlandHodgmanAlgorithmTest {
  // the examples show that the algo is not symmetric A cap B != B cap A
  @Test
  void testSingle() {
    Tensor clip = Array.zeros(1, 2);
    PolyclipResult polyclipResult = SutherlandHodgmanAlgorithm.of(clip).apply(CirclePoints.of(4));
    assertEquals(polyclipResult.tensor(), Tensors.empty());
  }

  @Test
  void testSingle2() {
    Tensor clip = Array.zeros(1, 2);
    PolyclipResult polyclipResult = SutherlandHodgmanAlgorithm.of(CirclePoints.of(4)).apply(clip);
    assertEquals(polyclipResult.tensor(), clip);
  }

  @Test
  void testQuantity() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(Clips.absolute(Quantity.of(2, "m")));
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, random, 6, 2);
      PolyclipResult polyclipResult = //
          SutherlandHodgmanAlgorithm.of(CirclePoints.of(3 + count).map(s -> Quantity.of(s, "m"))) //
              .apply(tensor);
      Tensor tensor2 = polyclipResult.tensor();
      Unprotect.getUnitUnique(tensor2);
    }
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> SutherlandHodgmanAlgorithm.of(HilbertMatrix.of(2, 3)));
  }

  @Test
  void testLine() {
    Tensor tensor = SutherlandHodgmanAlgorithm.intersection( //
        Tensors.vector(1, 0), //
        Tensors.vector(2, 0), //
        Tensors.vector(3, 3), //
        Tensors.vector(3, 2));
    assertEquals(tensor, Tensors.vector(3, 0));
    ExactTensorQ.require(tensor);
  }

  @Test
  void testSingular() {
    assertThrows(Exception.class, () -> SutherlandHodgmanAlgorithm.intersection( //
        Tensors.vector(1, 0), //
        Tensors.vector(2, 0), //
        Tensors.vector(4, 0), //
        Tensors.vector(9, 0)));
  }
}
