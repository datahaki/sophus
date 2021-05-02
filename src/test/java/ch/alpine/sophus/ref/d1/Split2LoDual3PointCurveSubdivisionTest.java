// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Sort;
import junit.framework.TestCase;

public class Split2LoDual3PointCurveSubdivisionTest extends TestCase {
  private static final CurveSubdivision CURVE_SUBDIVISION = //
      Split2LoDual3PointCurveSubdivision.of(RnGeodesic.INSTANCE, RationalScalar.of(1, 3), RationalScalar.of(1, 4));

  public void testCyclic() {
    Tensor cyclic = CURVE_SUBDIVISION.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{11/4, 9/4, 7/4, 9/4, 11/4, 13/4, 11/4, 9/4}"));
    ExactTensorQ.require(cyclic);
  }

  public void testString() {
    Tensor string = CURVE_SUBDIVISION.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4}"));
    ExactTensorQ.require(string);
    assertEquals(string, Sort.of(string));
  }
}
