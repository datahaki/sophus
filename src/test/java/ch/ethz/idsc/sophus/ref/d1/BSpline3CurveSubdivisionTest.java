// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.clt.ClothoidBuilder;
import ch.ethz.idsc.sophus.clt.ClothoidBuilders;
import ch.ethz.idsc.sophus.hs.r3s2.R3S2Geodesic;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.ext.Timing;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.nrm.Normalize;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.num.Rationalize;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Nest;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BSpline3CurveSubdivisionTest extends TestCase {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  public void testSimple() {
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    Tensor expected = Tensors.fromString("{{3/4, 0}, {1/2, 1/2}, {0, 3/4}, {-1/2, 1/2}, {-3/4, 0}, {-1/2, -1/2}, {0, -3/4}, {1/2, -1/2}}");
    assertEquals(expected, actual);
  }

  public void testString() {
    Tensor curve = Tensors.vector(0, 1, 2, 3);
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1, 3/2, 2, 5/2, 3}"));
    ExactTensorQ.require(refined);
  }

  public void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1}"));
    ExactTensorQ.require(refined);
  }

  public void testStringOne() {
    Tensor curve = Tensors.vector(1);
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{1}"));
    ExactTensorQ.require(refined);
  }

  public void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
    assertEquals(curveSubdivision.string(curve), Tensors.empty());
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  public void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(CLOTHOID_BUILDER);
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
    assertEquals(curveSubdivision.string(singleton), singleton);
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

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

  public void testLaneRiesenfeldRn() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 3);
    int depth = 5;
    final Tensor bs;
    final Tensor lr;
    {
      CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(RnGeodesic.INSTANCE);
      Timing timing = Timing.started();
      bs = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("bs=" + timing.seconds());
    }
    {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGeodesic.INSTANCE, 3);
      Timing timing = Timing.started();
      lr = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("lr=" + timing.seconds());
    }
    Chop._10.requireClose(bs, lr);
  }

  public void testLaneRiesenfeldSe2Covering() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 3);
    int depth = 5;
    final Tensor bs;
    final Tensor lr;
    {
      CurveSubdivision curveSubdivision = new BSpline3CurveSubdivision(Se2CoveringGeodesic.INSTANCE);
      Timing timing = Timing.started();
      bs = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("bs=" + timing.seconds());
    }
    {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(Se2CoveringGeodesic.INSTANCE, 3);
      Timing timing = Timing.started();
      lr = Nest.of(curveSubdivision::string, tensor, depth);
      timing.stop();
      // System.out.println("lr=" + timing.seconds());
    }
    Chop._10.requireClose(bs, lr);
    // System.out.println(bs.subtract(lr).map(Scalar::abs).flatten(-1).reduce(Max::of).get());
  }

  public void testNullFail() {
    AssertFail.of(() -> new BSpline3CurveSubdivision(null));
  }
}
