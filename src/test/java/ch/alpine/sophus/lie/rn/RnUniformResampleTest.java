// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.IOException;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class RnUniformResampleTest extends TestCase {
  public void testString() throws ClassNotFoundException, IOException {
    CurveSubdivision curveSubdivision = Serialization.copy(RnUniformResample.of(Quantity.of(2, "m")));
    Tensor tensor = curveSubdivision.string(Tensors.fromString("{{0[m]}, {1[m]}, {3[m]}, {6[m]}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.fromString("{{0[m]}, {2[m]}, {4[m]}}"));
  }

  public void testCyclic() {
    CurveSubdivision curveSubdivision = RnUniformResample.of(Quantity.of(2, "m"));
    Tensor tensor = curveSubdivision.cyclic(Tensors.fromString("{{0[m]}, {1[m]}, {3[m]}, {6[m]}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.fromString("{{0[m]}, {2[m]}, {4[m]}, {6[m]}, {4[m]}, {2[m]}}"));
  }

  public void testNegativeFail() {
    AssertFail.of(() -> RnUniformResample.of(Quantity.of(0.0, "m")));
    AssertFail.of(() -> RnUniformResample.of(Quantity.of(-1, "m")));
  }

  public void testNullFail() {
    AssertFail.of(() -> RnUniformResample.of(null));
  }
}
