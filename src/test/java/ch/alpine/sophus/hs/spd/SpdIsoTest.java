// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class SpdIsoTest {
  @Test
  void test() {
    int n = 3;
    Tensor g = IdentityMatrix.inplaceAdd(RandomVariate.of(NormalDistribution.of(0, 0.1), n, n));
    SpdIsometry spdIso = new SpdIsometry(g);
    SpdNManifold spdNManifold = new SpdNManifold(n);
    Tensor p = RandomSample.of(spdNManifold);
    spdNManifold.isPointQ().require(p);
    spdNManifold.isPointQ().require(spdIso.apply(p));
  }
}
