// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdBilinearFormTest {
  @Test
  void testFromId() {
    int n = 3;
    SpdNManifold spdNManifold = new SpdNManifold(n);
    Tensor p = IdentityMatrix.of(n);
    Tensor q = RandomSample.of(spdNManifold.randomSampleInterface());
    Scalar d1 = spdNManifold.distance(p, q);
    Scalar d2 = Spd0Exponential.INSTANCE.distance(q);
    Tolerance.CHOP.requireClose(d1, d2);
    Tensor g = IdentityMatrix.inplaceAdd(RandomVariate.of(NormalDistribution.of(0, 0.1), n, n));
    SpdIsometry spdIso = new SpdIsometry(g);
    Tensor ps = spdIso.apply(p);
    assertFalse(Chop._04.isClose(p, ps));
    Tensor qs = spdIso.apply(q);
    Scalar d3 = spdNManifold.distance(ps, qs);
    Tolerance.CHOP.requireClose(d1, d3);
    Scalar d4 = spdNManifold.tangentSpace(ps).distance(qs);
    Tolerance.CHOP.requireClose(d1, d4);
  }
}
