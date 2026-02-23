// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class SpdBilinearFormTest {
  @Test
  void testFromId() {
    int n = 3;
    SpdNManifold spdNManifold = new SpdNManifold(n);
    Tensor p = IdentityMatrix.of(n);
    Tensor q = RandomSample.of(spdNManifold);
    Scalar d1 = spdNManifold.distance(p, q);
    Scalar d2 = Spd0Exponential.INSTANCE.distance(q);
    Tolerance.CHOP.requireClose(d1, d2);
    Tensor g = IdentityMatrix.inplaceAdd(RandomVariate.of(NormalDistribution.of(0, 0.1), n, n));
    SpdIso spdIso = new SpdIso(g);
    Tensor ps = spdIso.apply(p);
    Tensor qs = spdIso.apply(q);
    Scalar d3 = spdNManifold.distance(ps, qs);
    Tolerance.CHOP.requireClose(d1, d3);
  }
}
