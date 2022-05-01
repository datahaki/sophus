// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

public class Split2HiDual3PointCurveSubdivisionTest {
  private static final CurveSubdivision CURVE_SUBDIVISION = //
      Split2HiDual3PointCurveSubdivision.of(RnGeodesic.INSTANCE, RationalScalar.of(1, 3), RationalScalar.of(1, 4));

  @Test
  public void testCyclic() {
    Tensor cyclic = CURVE_SUBDIVISION.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{37/12, 23/12, 17/12, 31/12, 29/12, 43/12, 37/12, 23/12}"));
    ExactTensorQ.require(cyclic);
  }

  @Test
  public void testString() {
    Tensor string = CURVE_SUBDIVISION.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{5/4, 17/12, 31/12, 29/12, 43/12, 15/4}"));
    ExactTensorQ.require(string);
  }
}
