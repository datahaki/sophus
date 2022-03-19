// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.red.Nest;

public class MSpline3CurveSubdivisionTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = new MSpline3CurveSubdivision(RnBiinvariantMean.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    Tensor expected = Tensors.fromString("{{3/4, 0}, {1/2, 1/2}, {0, 3/4}, {-1/2, 1/2}, {-3/4, 0}, {-1/2, -1/2}, {0, -3/4}, {1/2, -1/2}}");
    assertEquals(expected, actual);
  }

  @Test
  public void testString() {
    Tensor curve = Tensors.vector(0, 1, 2, 3);
    CurveSubdivision curveSubdivision = new MSpline3CurveSubdivision(RnBiinvariantMean.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1, 3/2, 2, 5/2, 3}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    CurveSubdivision curveSubdivision = new MSpline3CurveSubdivision(RnBiinvariantMean.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testStringOne() {
    Tensor curve = Tensors.vector(1);
    CurveSubdivision curveSubdivision = new MSpline3CurveSubdivision(RnBiinvariantMean.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new MSpline3CurveSubdivision(RnBiinvariantMean.INSTANCE);
    assertEquals(curveSubdivision.string(curve), Tensors.empty());
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new MSpline3CurveSubdivision(null));
  }
}
