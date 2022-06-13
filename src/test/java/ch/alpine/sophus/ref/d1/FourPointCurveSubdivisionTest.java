// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.red.Nest;

class FourPointCurveSubdivisionTest {
  @Test
  void testSimple() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGroup.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    assertEquals(actual, Tensors.fromString("{{1, 0}, {5/8, 5/8}, {0, 1}, {-5/8, 5/8}, {-1, 0}, {-5/8, -5/8}, {0, -1}, {5/8, -5/8}}"));
  }

  @Test
  void testString() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(0, 1, 2, 3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Subdivide.of(0, 3, 6));
    ExactTensorQ.require(string);
  }

  @Test
  void testStringTwo() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(0, 1);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Subdivide.of(0, 1, 2));
    ExactTensorQ.require(string);
  }

  @Test
  void testStringOne() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.vector(3));
    ExactTensorQ.require(string);
  }

  @Test
  void testSimple1() {
    Tensor curve = Tensors.fromString("{{0, 0}, {1, 0}, {0, 1}}");
    TensorUnaryOperator curveSubdivision = //
        new FourPointCurveSubdivision(RnGroup.INSTANCE)::cyclic;
    Tensor n1 = Nest.of(curveSubdivision, curve, 1);
    assertEquals(n1.get(0), Array.zeros(2));
    assertEquals(n1.get(1), Tensors.fromString("{9/16, -1/8}"));
    assertEquals(n1.get(2), UnitVector.of(2, 0));
    assertEquals(n1.get(3), Tensors.fromString("{9/16, 9/16}"));
  }

  @Test
  void testCyclic() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGroup.INSTANCE);
    for (int n = 3; n < 10; ++n) {
      Tensor tensor = curveSubdivision.cyclic(CirclePoints.of(n));
      Tensor filter = Tensor.of(IntStream.range(0, tensor.length() / 2) //
          .map(i -> i * 2) //
          .mapToObj(tensor::get));
      assertEquals(filter, CirclePoints.of(n));
    }
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new FourPointCurveSubdivision(RnGroup.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new FourPointCurveSubdivision(null));
  }
}
