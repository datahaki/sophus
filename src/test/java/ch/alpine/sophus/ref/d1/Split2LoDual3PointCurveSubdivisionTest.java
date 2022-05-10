// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Sort;
import ch.alpine.tensor.chq.ExactTensorQ;

class Split2LoDual3PointCurveSubdivisionTest {
  private static final CurveSubdivision CURVE_SUBDIVISION = //
      Split2LoDual3PointCurveSubdivision.of(RnGroup.INSTANCE, RationalScalar.of(1, 3), RationalScalar.of(1, 4));

  @Test
  public void testCyclic() {
    Tensor cyclic = CURVE_SUBDIVISION.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{11/4, 9/4, 7/4, 9/4, 11/4, 13/4, 11/4, 9/4}"));
    ExactTensorQ.require(cyclic);
  }

  @Test
  public void testString() {
    Tensor string = CURVE_SUBDIVISION.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4}"));
    ExactTensorQ.require(string);
    assertEquals(string, Sort.of(string));
  }
}
