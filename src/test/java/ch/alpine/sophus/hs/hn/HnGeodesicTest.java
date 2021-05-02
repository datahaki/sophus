// code by jph
package ch.alpine.sophus.hs.hn;

import java.io.IOException;

import ch.alpine.sophus.math.Geodesic;
import ch.alpine.sophus.ref.d1.BSpline2CurveSubdivision;
import ch.alpine.tensor.NumberQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnGeodesicTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Geodesic geodesicInterface = Serialization.copy(HnGeodesic.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor midpoint = geodesicInterface.midpoint(p, q);
      HnMemberQ.INSTANCE.require(midpoint);
    }
  }

  public void testSubdiv() {
    BSpline2CurveSubdivision bSpline2CurveSubdivision = new BSpline2CurveSubdivision(HnGeodesic.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor r = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor result = Nest.of(bSpline2CurveSubdivision::cyclic, Tensors.of(p, q, r), 3);
      assertTrue(result.flatten(-1).map(Scalar.class::cast).allMatch(NumberQ::of));
    }
  }

  public void testFlip() {
    Distribution distribution = NormalDistribution.of(0, 1);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnExponential exp_p = new HnExponential(p);
      Tensor f1 = exp_p.exp(exp_p.log(q).negate());
      Tensor f2 = exp_p.flip(q);
      Chop._05.requireClose(f1, f2);
      Tensor f3 = HnManifold.INSTANCE.flip(p, q);
      Chop._05.requireClose(f1, f3);
    }
  }
}