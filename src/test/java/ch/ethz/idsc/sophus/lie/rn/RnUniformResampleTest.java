// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import java.io.IOException;

import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.qty.Quantity;
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
