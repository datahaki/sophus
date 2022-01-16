// code by jph
package ch.alpine.sophus.crv.d2;

import java.util.Random;

import ch.alpine.sophus.crv.d2.SutherlandHodgmanAlgorithm.PolyclipResult;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class SutherlandHodgmanAlgorithmTest extends TestCase {
  // the examples show that the algo is not symmetric A cap B != B cap A
  public void testSingle() {
    Tensor clip = Array.zeros(1, 2);
    PolyclipResult polyclipResult = SutherlandHodgmanAlgorithm.of(clip).apply(CirclePoints.of(4));
    assertEquals(polyclipResult.tensor(), Tensors.empty());
  }

  public void testSingle2() {
    Tensor clip = Array.zeros(1, 2);
    PolyclipResult polyclipResult = SutherlandHodgmanAlgorithm.of(CirclePoints.of(4)).apply(clip);
    assertEquals(polyclipResult.tensor(), clip);
  }

  public void testQuantity() {
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

  public void testFail() {
    AssertFail.of(() -> SutherlandHodgmanAlgorithm.of(HilbertMatrix.of(2, 3)));
  }

  public void testLine() {
    Tensor tensor = SutherlandHodgmanAlgorithm.intersection( //
        Tensors.vector(1, 0), //
        Tensors.vector(2, 0), //
        Tensors.vector(3, 3), //
        Tensors.vector(3, 2));
    assertEquals(tensor, Tensors.vector(3, 0));
    ExactTensorQ.require(tensor);
  }

  public void testSingular() {
    AssertFail.of(() -> SutherlandHodgmanAlgorithm.intersection( //
        Tensors.vector(1, 0), //
        Tensors.vector(2, 0), //
        Tensors.vector(4, 0), //
        Tensors.vector(9, 0)));
  }
}
