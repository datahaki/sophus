// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.Geodesic;
import ch.ethz.idsc.sophus.ref.d1.BSpline2CurveSubdivision;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Nest;
import ch.ethz.idsc.tensor.sca.Chop;
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
