// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import java.io.IOException;
import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.num.Rationalize;
import ch.ethz.idsc.tensor.red.Nest;
import junit.framework.TestCase;

public class FourPointCurveSubdivisionTest extends TestCase {
  public void testSimple() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGeodesic.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    assertEquals(actual, Tensors.fromString("{{1, 0}, {5/8, 5/8}, {0, 1}, {-5/8, 5/8}, {-1, 0}, {-5/8, -5/8}, {0, -1}, {5/8, -5/8}}"));
  }

  public void testString() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(0, 1, 2, 3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Subdivide.of(0, 3, 6));
    ExactTensorQ.require(string);
  }

  public void testStringTwo() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(0, 1);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Subdivide.of(0, 1, 2));
    ExactTensorQ.require(string);
  }

  public void testStringOne() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.vector(3));
    ExactTensorQ.require(string);
  }

  public void testSimple1() {
    Tensor curve = Tensors.fromString("{{0, 0}, {1, 0}, {0, 1}}");
    TensorUnaryOperator curveSubdivision = //
        new FourPointCurveSubdivision(RnGeodesic.INSTANCE)::cyclic;
    Tensor n1 = Nest.of(curveSubdivision, curve, 1);
    assertEquals(n1.get(0), Array.zeros(2));
    assertEquals(n1.get(1), Tensors.fromString("{9/16, -1/8}"));
    assertEquals(n1.get(2), UnitVector.of(2, 0));
    assertEquals(n1.get(3), Tensors.fromString("{9/16, 9/16}"));
  }

  public void testCyclic() {
    CurveSubdivision curveSubdivision = new FourPointCurveSubdivision(RnGeodesic.INSTANCE);
    for (int n = 3; n < 10; ++n) {
      Tensor tensor = curveSubdivision.cyclic(CirclePoints.of(n));
      Tensor filter = Tensor.of(IntStream.range(0, tensor.length() / 2) //
          .map(i -> i * 2) //
          .mapToObj(tensor::get));
      assertEquals(filter, CirclePoints.of(n));
    }
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new FourPointCurveSubdivision(RnGeodesic.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  public void testNullFail() {
    AssertFail.of(() -> new FourPointCurveSubdivision(null));
  }
}
