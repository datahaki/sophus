// code by jph
package ch.alpine.sophus.hs.ad;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.sn.SnAlgebra;
import ch.alpine.sophus.hs.sn.SnGeodesic;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsAdGeodesicTest extends TestCase {
  public void testSimple() {
    Distribution distribution = TriangularDistribution.with(0, 0.2);
    for (int n = 2; n < 5; ++n) {
      HsAdGeodesic hsAdGeodesic = new HsAdGeodesic(SnAlgebra.of(n));
      Tensor p = RandomVariate.of(distribution, n);
      Tensor q = RandomVariate.of(distribution, n);
      Scalar lambda = RealScalar.of(0.3);
      Tensor chk = hsAdGeodesic.split(p, q, lambda);
      Exponential exponential = SnManifold.INSTANCE.exponential(UnitVector.of(n + 1, n));
      Tensor sn_p = exponential.exp(p.copy().append(RealScalar.ZERO));
      Tensor sn_q = exponential.exp(q.copy().append(RealScalar.ZERO));
      Tensor sn_s = SnGeodesic.INSTANCE.split(sn_p, sn_q, lambda);
      Tensor cmp = exponential.log(sn_s).extract(0, n);
      Chop._05.requireClose(chk, cmp);
    }
  }
}