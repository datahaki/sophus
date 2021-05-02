// code by jph
package ch.alpine.sophus.ply.d2;

import java.util.List;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class ParametricResampleTest extends TestCase {
  public void testSimple() {
    Scalar threshold = RealScalar.of(33);
    Scalar ds = RealScalar.of(.3);
    Tensor points = Tensors.fromString("{{100, 0}, {100, 2}, {100, 3}, {10, 10}, {10, 10.2}, {10, 10.4}, {20, 40}}");
    DeprecatedUniformResample uniformResample = new DeprecatedUniformResample(threshold, ds);
    ParametricResample parametricResample = new ParametricResample(threshold, ds);
    ResampleResult resampleResult = parametricResample.apply(points);
    List<Tensor> list = uniformResample.apply(points);
    List<Tensor> pnts = resampleResult.getPoints();
    assertEquals(list.size(), pnts.size());
    List<Tensor> spin = resampleResult.getPointsSpin(RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(list.size(), spin.size());
    for (int index = 0; index < list.size(); ++index) {
      Chop._10.requireClose(list.get(index), pnts.get(index));
      Chop._10.requireClose(list.get(index), spin.get(index));
    }
  }

  public void testDistances() {
    Scalar threshold = RealScalar.of(33);
    Scalar ds = RealScalar.of(.3);
    ParametricResample parametricResample = new ParametricResample(threshold, ds);
    Tensor points = CirclePoints.of(200).multiply(RealScalar.of(1));
    ResampleResult resampleResult = parametricResample.apply(points);
    List<Tensor> pnts = resampleResult.getPoints();
    assertEquals(pnts.size(), 1);
    Tensor difs = Differences.of(pnts.get(0));
    Tensor norm = Tensor.of(difs.stream().map(Vector2Norm::of));
    Clip clip = Clips.interval(0.297, 0.299);
    norm.stream().map(Scalar.class::cast).forEach(clip::requireInside);
  }

  public void testMore() {
    Scalar threshold = RealScalar.of(33);
    Scalar ds = RealScalar.of(.3);
    Tensor points = Tensors.fromString("{{10, -100}, {100, 0}, {100, 0.1}, {100, 0.2}, {100, 2}, {100, 2}, {100, 2}, {100, 3}, {100, 7}, {10, 10}}");
    DeprecatedUniformResample uniformResample = new DeprecatedUniformResample(threshold, ds);
    ParametricResample parametricResample = new ParametricResample(threshold, ds);
    ResampleResult resampleResult = parametricResample.apply(points);
    List<Tensor> list = uniformResample.apply(points);
    List<Tensor> pnts = resampleResult.getPoints();
    assertEquals(list.size(), pnts.size());
    List<Tensor> spin = resampleResult.getPointsSpin(RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(list.size(), spin.size());
    for (int index = 0; index < list.size(); ++index) {
      Chop._10.requireClose(list.get(index), pnts.get(index));
      Chop._10.requireClose(list.get(index), spin.get(index));
    }
    assertEquals(pnts.size(), 1);
    Tensor seq = pnts.get(0);
    Tensor ys = Tensor.of(seq.stream().map(r -> r.Get(1)));
    Tensor cs = Range.of(0, seq.length()).multiply(ds);
    Chop._10.requireClose(ys, cs);
  }
}
