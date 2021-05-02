// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class RnGroupElementTest extends TestCase {
  public void testSimple() {
    RnGroupElement rnGroupElement = new RnGroupElement(Tensors.vector(1, 2, 3));
    Tensor result = rnGroupElement.combine(Tensors.vector(4, -2, -7));
    assertEquals(result, Tensors.vector(5, 0, -4));
    ExactTensorQ.require(result);
  }

  public void testAdjoint() {
    RnGroupElement rnGroupElement = new RnGroupElement(Tensors.vector(-1, 0, 2));
    assertEquals(Tensor.of(IdentityMatrix.of(3).stream().map(rnGroupElement::adjoint)), IdentityMatrix.of(3));
    assertEquals(rnGroupElement.dL(HilbertMatrix.of(5)), HilbertMatrix.of(5));
    assertEquals(rnGroupElement.dR(HilbertMatrix.of(5)), HilbertMatrix.of(5));
    // try {
    // rnGroupElement.adjoint(UnitVector.of(4, 1));
    // fail();
    // } catch (Exception exception) {
    // // ---
    // }
  }
}
