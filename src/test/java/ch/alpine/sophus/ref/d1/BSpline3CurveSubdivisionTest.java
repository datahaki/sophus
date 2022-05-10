// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilder;
import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.nrm.Normalize;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;

class BSpline3CurveSubdivisionTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
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
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1, 3/2, 2, 5/2, 3}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testStringOne() {
    Tensor curve = Tensors.vector(1);
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  public void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
    assertEquals(curveSubdivision.string(curve), Tensors.empty());
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  @Test
  public void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(CLOTHOID_BUILDER);
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
    assertEquals(curveSubdivision.string(singleton), singleton);
  }

  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new BSpline3CurveSubdivision(RnGroup.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  @Test
  public void testR3S2() {
    Tensor tensor = Subdivide.of(-0.5, 0.8, 6) //
        .map(scalar -> Tensors.of(scalar, RealScalar.of(0.3), RealScalar.ONE));
    tensor = Tensor.of(tensor.stream() //
        .map(Normalize.with(Vector2Norm::of)) //
        .map(row -> Tensors.of(row, row)));
    TensorUnaryOperator string = new BSpline3CurveSubdivision(R3S2Geodesic.INSTANCE)::string;
    Tensor apply = string.apply(tensor);
    assertEquals(Dimensions.of(apply), Arrays.asList(13, 2, 3));
  }

  @Test
  public void testLaneRiesenfeldRn() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 3);
    int depth = 5;
    final Tensor bs;
    final Tensor lr;
    {
      CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
      Timing timing = Timing.started();
      bs = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("bs=" + timing.seconds());
    }
    {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 3);
      Timing timing = Timing.started();
      lr = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("lr=" + timing.seconds());
    }
    Chop._10.requireClose(bs, lr);
  }

  @Test
  public void testLaneRiesenfeldSe2Covering() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 3);
    int depth = 5;
    final Tensor bs;
    final Tensor lr;
    {
      CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(Se2CoveringGroup.INSTANCE);
      Timing timing = Timing.started();
      bs = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("bs=" + timing.seconds());
    }
    {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(Se2CoveringGroup.INSTANCE, 3);
      Timing timing = Timing.started();
      lr = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("lr=" + timing.seconds());
    }
    Chop._10.requireClose(bs, lr);
    // System.out.println(bs.subtract(lr).map(Scalar::abs).flatten(-1).reduce(Max::of).get());
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new BSpline3CurveSubdivision(null));
  }
}
