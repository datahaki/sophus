// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.red.Nest;

class MSpline4CurveSubdivisionTest {
  @Test
  public void testBivariate() {
    CurveSubdivision curveSubdivision = MSpline4CurveSubdivision.of(RnBiinvariantMean.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    assertEquals(actual.extract(0, 3), Tensors.fromString("{{5/8, -1/4}, {5/8, 1/4}, {1/4, 5/8}}"));
  }

  @Test
  public void testUnivariate() {
    CurveSubdivision curveSubdivision = MSpline4CurveSubdivision.of(RnBiinvariantMean.INSTANCE);
    Tensor tensor = curveSubdivision.string(UnitVector.of(5, 2));
    assertEquals(tensor, Tensors.fromString("{0, 1/16, 5/16, 5/8, 5/8, 5/16, 1/16, 0}"));
  }
}
