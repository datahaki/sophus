// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;

public class RnUniformResampleTest {
  @Test
  public void testString() throws ClassNotFoundException, IOException {
    CurveSubdivision curveSubdivision = Serialization.copy(RnUniformResample.of(Quantity.of(2, "m")));
    Tensor tensor = curveSubdivision.string(Tensors.fromString("{{0[m]}, {1[m]}, {3[m]}, {6[m]}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.fromString("{{0[m]}, {2[m]}, {4[m]}}"));
  }

  @Test
  public void testCyclic() {
    CurveSubdivision curveSubdivision = RnUniformResample.of(Quantity.of(2, "m"));
    Tensor tensor = curveSubdivision.cyclic(Tensors.fromString("{{0[m]}, {1[m]}, {3[m]}, {6[m]}}"));
    assertEquals(ExactTensorQ.require(tensor), Tensors.fromString("{{0[m]}, {2[m]}, {4[m]}, {6[m]}, {4[m]}, {2[m]}}"));
  }

  @Test
  public void testNegativeFail() {
    AssertFail.of(() -> RnUniformResample.of(Quantity.of(0.0, "m")));
    AssertFail.of(() -> RnUniformResample.of(Quantity.of(-1, "m")));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> RnUniformResample.of(null));
  }
}
