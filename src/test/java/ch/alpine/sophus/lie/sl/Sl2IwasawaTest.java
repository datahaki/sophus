// code by jph
package ch.alpine.sophus.lie.sl;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class Sl2IwasawaTest {
  final Tensor ID = IdentityMatrix.of(2).unmodifiable();
  final SlNGroup slNGroup = new SlNGroup(2);

  @RepeatedTest(10)
  void testRandom() {
    Tensor p = RandomSample.of(slNGroup);
    Sl2Iwasawa iwasawa = Sl2Iwasawa.from(p);
    Tensor q = iwasawa.matrix();
    Tolerance.CHOP.requireClose(p, q);
  }

  @Test
  void testComponents() {
    Sl2Iwasawa iwasawa = Sl2Iwasawa.of(0, 0, 0);
    Tolerance.CHOP.requireClose(iwasawa.K(), ID);
    Tolerance.CHOP.requireClose(iwasawa.A(), ID);
    Tolerance.CHOP.requireClose(iwasawa.N(), ID);
    Tolerance.CHOP.requireClose(iwasawa.matrix(), ID);
  }

  @RepeatedTest(10)
  void testRandomVector() {
    Distribution distribution = NormalDistribution.standard();
    Tensor tst = RandomVariate.of(distribution, 3);
    Tensor q = new Sl2Iwasawa(tst.Get(0), tst.Get(1), tst.Get(2)).matrix();
    Sl2Iwasawa sl2Iwasawa = Sl2Iwasawa.from(q);
    Tolerance.CHOP.requireClose(tst, sl2Iwasawa.vector());
  }
}
