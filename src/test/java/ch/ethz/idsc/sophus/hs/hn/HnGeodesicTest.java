// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.crv.subdiv.BSpline2CurveSubdivision;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Nest;
import junit.framework.TestCase;

public class HnGeodesicTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    GeodesicInterface geodesicInterface = Serialization.copy(HnGeodesic.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 2; d < 4; ++d)
      for (int count = 0; count < 10; ++count) {
        Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
        Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
        Tensor midpoint = geodesicInterface.midpoint(p, q);
        StaticHelper.requirePoint(midpoint);
      }
  }

  public void testSubdiv() {
    BSpline2CurveSubdivision bSpline2CurveSubdivision = new BSpline2CurveSubdivision(HnGeodesic.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 2; d < 4; ++d)
      for (int count = 0; count < 10; ++count) {
        Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
        Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
        Tensor r = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
        Tensor result = Nest.of(bSpline2CurveSubdivision::cyclic, Tensors.of(p, q, r), 3);
        assertTrue(result.flatten(-1).allMatch(NumberQ::of));
      }
  }
}
